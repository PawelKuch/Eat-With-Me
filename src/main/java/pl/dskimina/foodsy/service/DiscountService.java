package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.Discount;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.UserOrderPayment;
import pl.dskimina.foodsy.repository.DiscountRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final OrderService orderService;
    private final ExtraPaymentService extraPaymentService;
    private final UserOrderPaymentService userOrderPaymentService;

    Logger LOG = LoggerFactory.getLogger(DiscountService.class);

    public DiscountService(DiscountRepository discountRepository, OrderService orderService, ExtraPaymentService extraPaymentService,
                           UserOrderPaymentService userOrderPaymentService) {
        this.discountRepository = discountRepository;
        this.orderService = orderService;
        this.extraPaymentService = extraPaymentService;
        this.userOrderPaymentService = userOrderPaymentService;
    }

    @Transactional
    public void createDiscount(String orderId, boolean isPercentage, String valueString){
        Discount discount = new Discount();
        Order order = orderService.findOrderByOrderId(orderId);
        double currentBaseOrderValue = order.getValue();
        Double extraPaymentsValueForOrder = extraPaymentService.getExtraPaymentsValueForOrder(order.getOrderId());
        if (extraPaymentsValueForOrder != null){
            currentBaseOrderValue -= extraPaymentsValueForOrder;
        }

        discount.setDiscountId(UUID.randomUUID().toString());
        double value = Double.parseDouble(valueString);
        if(isPercentage) {
            LOG.info("isPercentage is True");
            double percentageValue = value/100.0;
            discount.setValue(percentageValue);
            discount.setIsPercentage(true);
            double newOrderValue = currentBaseOrderValue - Math.round((currentBaseOrderValue*percentageValue)*100.0)/100.0;
            order.setValue(newOrderValue);
        } else {
            LOG.info("isPercentage is false");
            discount.setValue(value);
            discount.setIsPercentage(false);
            double newOrderValue = currentBaseOrderValue - value;
            order.setValue(newOrderValue);
        }
        discount.setOrder(order);
        orderService.saveOrder(order);
        discountRepository.save(discount);
    }

    public void addDiscountToUserOrderPayment(String orderId, String discountValueString, boolean isPercentage){
        double discountValue = Double.parseDouble(discountValueString);
        List<UserOrderPayment> userOrderPayments = userOrderPaymentService.findUserOrderPaymentsForOrderId(orderId);
        int howManyUsersInOrder = userOrderPayments.size();

        if(isPercentage){
            double percentageDiscountValue = discountValue / 100.0;
            userOrderPayments.forEach(data -> {
                data.setDiscountInPercentage(percentageDiscountValue);
                Double discountValueInCash = data.getDiscountValueInCash() + (data.getAmountToPay() * Math.round(percentageDiscountValue * 100.0) / 100.0);
                LOG.info("data.getDiscountValueInCash: " + data.getDiscountValueInCash());
                LOG.info("data.getAmountToPay: " + data.getAmountToPay());
                LOG.info("Double discountValueInCash: " + discountValueInCash);
                data.setDiscountValueInCash(discountValueInCash);
                data.setAmountToPay(data.getAmountToPay() - data.getDiscountValueInCash());
                userOrderPaymentService.saveUserOrderPayment(data);
            });
        } else {
            Double discountValueForUser = Math.round(discountValue / howManyUsersInOrder * 100.0) / 100.0;
            userOrderPayments.forEach(data -> {
                data.setDiscountValueInCash(data.getDiscountValueInCash() + discountValueForUser);
                data.setAmountToPay(data.getAmountToPay() - data.getDiscountValueInCash());
                userOrderPaymentService.saveUserOrderPayment(data);
            });
        }

    }
}
