package pl.dskimina.foodsy.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.ExtraPayment;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.UserOrderPayment;
import pl.dskimina.foodsy.repository.ExtraPaymentRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.UserOrderPaymentRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ExtraPaymentService {
    private final ExtraPaymentRepository extraPaymentRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserOrderPaymentRepository userOrderPaymentRepository;

    Logger LOG = LoggerFactory.getLogger(ExtraPaymentService.class);

    public ExtraPaymentService(ExtraPaymentRepository extraPaymentRepository, OrderService orderService, OrderRepository orderRepository, UserOrderPaymentRepository userOrderPaymentRepository) {
        this.extraPaymentRepository = extraPaymentRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.userOrderPaymentRepository = userOrderPaymentRepository;
    }

    @Transactional
    public void createExtraPayment(String orderId, String product, String price){
        ExtraPayment extraPayment = new ExtraPayment();
        Order order = orderService.findOrderByOrderId(orderId);
        extraPayment.setExtraPaymentId(UUID.randomUUID().toString());
        extraPayment.setOrder(order);
        extraPayment.setProduct(product);
        double extraPaymentPrice = Double.parseDouble(price);
        extraPayment.setPrice(extraPaymentPrice);
        double currentOrderValue = order.getValue();
        double newOrderValue = currentOrderValue + extraPaymentPrice;
        order.setValue(newOrderValue);
        extraPaymentRepository.save(extraPayment);
        order.setExtraPaymentValue(Objects.requireNonNullElse(extraPaymentRepository.getExtraPaymentsValueForOrder(orderId), 0.0));
        orderService.saveOrder(order);
    }


    @Transactional
    public Double getExtraPaymentsValueForOrder(String orderId){
         Double extraPaymentsValue = extraPaymentRepository.getExtraPaymentsValueForOrder(orderId);
         return (extraPaymentsValue != null) ? extraPaymentsValue : 0.0;
    }

    @Transactional
    public void addExtraPaymentToUserOrderPayment(String orderId, String extraPaymentValueString){
        double extraPaymentValue = Double.parseDouble(extraPaymentValueString);
        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        int howManyUsersInOrder = userOrderPayments.size();
        Double extraPaymentValueForUser = Math.round(extraPaymentValue / howManyUsersInOrder * 100.0) / 100.0;
        userOrderPayments.forEach(data -> {
            data.setExtraPaymentValue(extraPaymentValueForUser);
            data.setAmountToPay(data.getAmountToPay() + extraPaymentValueForUser);
            userOrderPaymentRepository.save(data);
        });
    }

    @Transactional
    public boolean deleteExtraPayment(String orderId, String extraPaymentId) {
        ExtraPayment extraPayment = extraPaymentRepository.findByExtraPaymentIdAndOrderOrderId(extraPaymentId, orderId);
        if (extraPayment == null) {
            LOG.error("ExtraPayment not found");
            return false;
        }
        Order order = orderRepository.findByOrderId(orderId);
        order.setValue(order.getValue() - extraPayment.getPrice());
        extraPaymentRepository.delete(extraPayment);
        order.setExtraPaymentValue(extraPaymentRepository.getExtraPaymentsValueForOrder(orderId));
        orderRepository.save(order);
        return true;
    }

}
