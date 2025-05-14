package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.exception.OrderNotFoundException;
import pl.dskimina.foodsy.repository.OrderItemRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import java.util.List;
import java.util.Objects;

@Service
public class OrderPriceCalculatingService {

    private final OrderRepository orderRepository;
    private final ToDataService toDataService;
    private final OrderItemRepository orderItemRepository;

    public OrderPriceCalculatingService(OrderRepository orderRepository, ToDataService toDataService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.toDataService = toDataService;
        this.orderItemRepository = orderItemRepository;
    }


    @Transactional
    public OrderData getCalculatedOrder(String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        if(order == null) throw new OrderNotFoundException("Order " + orderId + " not found");

        OrderData orderData = toDataService.convert(order);
        double orderNetValue = Objects.requireNonNullElse(orderItemRepository.getOrderItemsValueForOrder(order.getOrderId()), 0.0);
        double cashDiscount = order.getCashDiscount();
        double baseValue = Math.round((orderNetValue - cashDiscount) * 100.0) / 100.0;

        double percentageDiscount = Math.round((order.getPercentageDiscount() / 100.0) * 100.0) / 100.0;
        double percentageDiscountInCash =  Math.round((percentageDiscount * baseValue) * 100.0) / 100.0;
        double extraPayment = order.getExtraPaymentValue();
        double orderValue = percentageDiscount > 0.0? Math.round((orderNetValue - cashDiscount - percentageDiscountInCash + extraPayment) * 100.0) / 100.0 : orderNetValue - cashDiscount + extraPayment;
        orderData.setValue(orderValue);
        orderData.setNetValue(orderNetValue);
        orderData.setCashDiscount(cashDiscount);
        orderData.setPercentageDiscount(order.getPercentageDiscount());
        orderData.setPercentageDiscountCashValue(percentageDiscountInCash);
        orderData.setExtraPaymentValue(extraPayment);
        return orderData;
    }

    @Transactional
    public OrderData getCalculatedOrder(Order order){
        if(order == null) throw new OrderNotFoundException("Order not found");

        OrderData orderData = toDataService.convert(order);
        double orderNetValue = Objects.requireNonNullElse(orderItemRepository.getOrderItemsValueForOrder(order.getOrderId()), 0.0);
        double cashDiscount = order.getCashDiscount();
        double baseValue = Math.round((orderNetValue - cashDiscount) * 100.0) / 100.0;

        double percentageDiscount = Math.round((order.getPercentageDiscount() / 100.0) * 100.0) / 100.0;
        double percentageDiscountInCash =  Math.round((percentageDiscount * baseValue) * 100.0) / 100.0;
        double extraPayment = order.getExtraPaymentValue();
        double orderValue = percentageDiscount > 0.0? Math.round((orderNetValue - cashDiscount - percentageDiscountInCash + extraPayment) * 100.0) / 100.0 : orderNetValue - cashDiscount + extraPayment;
        orderData.setValue(orderValue);
        orderData.setNetValue(orderNetValue);
        orderData.setCashDiscount(cashDiscount);
        orderData.setPercentageDiscount(order.getPercentageDiscount());
        orderData.setPercentageDiscountCashValue(percentageDiscountInCash);
        orderData.setExtraPaymentValue(extraPayment);
        return orderData;
    }

    @Transactional
    public List<OrderData> getCalculatedOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::getCalculatedOrder).toList();
    }

}
