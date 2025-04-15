package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.*;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final static Logger LOG = LoggerFactory.getLogger(OrderService.class);
    private final ToDataService toDataService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public OrderService(OrderRepository orderRepository, ToDataService toDataService, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.orderRepository = orderRepository;
        this.toDataService = toDataService;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;

    }

    @Transactional
    public OrderData getOrderByOrderId(String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        return toDataService.convert(order);
    }

    @Transactional
    public OrderData createOrder(String restaurantId, String userId, String closingDateString, String minValueString, String description) {
        Order order = new Order();
        User user = userRepository.findByUserId(userId);
        order.setOrderId(UUID.randomUUID().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime closingDate = LocalDateTime.parse(closingDateString, formatter);
        order.setClosingDate(closingDate);
        order.setDescription(description);
        Double minValue = Double.parseDouble(minValueString);
        order.setMinValue(minValue);
        order.setValue(0.0);
        order.setNetValue(0.0);
        order.setCashDiscount(0.0);
        order.setBaseValue(0.0);
        order.setPercentageDiscount(0.0);
        order.setPercentageDiscountCashValue(0.0);
        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItemList(orderItems);
        order.setOwner(user);
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        order.setRestaurant(restaurant);
        order.setIsClosed(false);
        orderRepository.save(order);
        return toDataService.convert(order);
    }

    @Transactional
    public List<OrderData> getOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(toDataService::convert).toList();
    }

    @Transactional
    public void addExtraPayment(String orderId, String newExtraPaymentString){
        Order order = orderRepository.findByOrderId(orderId);
        double extraPayment = Math.round(Double.parseDouble(newExtraPaymentString) * 100.0) / 100.0;
        if(order.getExtraPaymentValue() != extraPayment){
            double currentExtraPayment = order.getExtraPaymentValue();
            order.setExtraPaymentValue(extraPayment);
            order.setValue(order.getValue() - currentExtraPayment + extraPayment);
            orderRepository.save(order);
        }
    }

    @Transactional
    public void addPercentageDiscount(String orderId, String newPercentageDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        double newPercentageDiscountDouble = Double.parseDouble(newPercentageDiscountString);
        double newPercentageDiscount = Math.round((newPercentageDiscountDouble / 100.0) * 100.0) / 100.0;

        LOG.warn("Comaprison: order.getPercentageDiscount() VS newPercentageDiscountDouble: {}", order.getPercentageDiscount() + " VS " + newPercentageDiscountDouble);
        if(order.getPercentageDiscount() != newPercentageDiscountDouble){
            order.setPercentageDiscount(newPercentageDiscountDouble);
            double percentageDiscountInCash = Math.round((order.getBaseValue() * newPercentageDiscount) * 100.0) / 100.0;
            LOG.warn("baseValue: {}", order.getBaseValue());
            order.setPercentageDiscountCashValue(percentageDiscountInCash);
            LOG.warn("percentageDiscountInCash: {}", percentageDiscountInCash);
            double generalDiscount = order.getCashDiscount() + percentageDiscountInCash;
            order.setValue(order.getNetValue() - generalDiscount);
            LOG.warn("order.getBaseValue(): {}", order.getBaseValue());
            orderRepository.save(order);
            LOG.debug("order saved");
        }
    }

    @Transactional
    public void addCashDiscount(String orderId, String newCashDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        double newCashDiscount = Double.parseDouble(newCashDiscountString);
        LOG.warn("Comparison cashDiscount: order.getCashDiscount VS newCashDiscount: {}", order.getCashDiscount() + " VS " + newCashDiscount);
        if(order.getCashDiscount() != newCashDiscount){
            double baseValue = Math.round((order.getNetValue() - newCashDiscount) * 100.0) / 100.0;
            double updatedPercentageDiscountInCash = Math.round((baseValue * (order.getPercentageDiscount() / 100.0)) * 100.0) / 100.0;
            double currentGeneralDiscount = order.getCashDiscount() + order.getPercentageDiscountCashValue();
            order.setPercentageDiscountCashValue(updatedPercentageDiscountInCash);
            double newGeneralDiscount = order.getPercentageDiscountCashValue() + newCashDiscount;
            LOG.warn("newCashDiscount: {}", newCashDiscount);
            order.setBaseValue(baseValue);
            LOG.warn("order.getValue() before changing value: {}", order.getValue());
            order.setValue(order.getValue() + currentGeneralDiscount - newGeneralDiscount);
            LOG.warn("order.getValue(): {}", order.getValue());
            LOG.warn("currentGeneralDiscount: {}", currentGeneralDiscount);
            LOG.warn("newGeneralDiscount: {}", newGeneralDiscount);
            LOG.warn("order.getCashDiscount(): {}", order.getCashDiscount());
            order.setCashDiscount(newCashDiscount); // ustawienie nowego rabatu
            orderRepository.save(order);
        }
    }


    @Transactional
    public int getUsersAmountForOrder(String orderId){
        return orderRepository.getUsersAmountForOrder(orderId);
    }

}
