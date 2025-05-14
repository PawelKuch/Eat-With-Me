package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.*;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.exception.BadRequestException;
import pl.dskimina.foodsy.exception.OrderNotFoundException;
import pl.dskimina.foodsy.exception.RestaurantNotFoundException;
import pl.dskimina.foodsy.exception.UserNotFoundException;
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
    public OrderData getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        if(order == null) throw new OrderNotFoundException("Nie znaleziono zamówienia o żądanym id: " + orderId);

        return toDataService.convert(order);
    }

    @Transactional
    public OrderData createOrder(String restaurantId, String userId, String closingDateString, String minValueString, String description) {
        Order order = new Order();
        User user = userRepository.findByUserId(userId);
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);

        if(user == null) throw new UserNotFoundException("Nie znaleziono uzytkownika o żądanym id: " + userId);
        if(restaurant == null) throw new RestaurantNotFoundException("Nie znaleziono restauracji o żądanym id: " + restaurantId);

        order.setOrderId(UUID.randomUUID().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime closingDate = LocalDateTime.parse(closingDateString, formatter);
        order.setClosingDate(closingDate);
        order.setDescription(description);
        Double minValue = Double.parseDouble(minValueString);
        order.setMinValue(minValue);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(0.0);
        order.setPercentageDiscountCashValue(0.0);
        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItemList(orderItems);
        order.setOwner(user);

        order.setRestaurant(restaurant);
        order.setIsClosed(false);
        orderRepository.save(order);
        LOG.debug("debugging");
        return toDataService.convert(order);
    }

    @Transactional
    public void updateOrderPrice(String orderId, String newExtraPaymentString, String newPercentageDiscountString, String newCashDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        if(order == null) throw new OrderNotFoundException("Nie znaleziono zamówienia o żądanym id: " + orderId);
        if(newExtraPaymentString == null || newExtraPaymentString.isEmpty() || newExtraPaymentString.isBlank()) throw new BadRequestException("Brak lub niepoprawny format wartości dopłaty!");
        if(newPercentageDiscountString == null || newPercentageDiscountString.isEmpty() || newPercentageDiscountString.isBlank()) throw new BadRequestException("Brak lub niepoprawny format wartości rabatu!");
        if(newCashDiscountString == null || newCashDiscountString.isEmpty() || newCashDiscountString.isBlank()) throw new BadRequestException("Brak lub niepoprawny format wartości rabatu!");

        double extraPayment = Math.round(Double.parseDouble(newExtraPaymentString) * 100.0) / 100.0;
        double newPercentageDiscountDouble = Double.parseDouble(newPercentageDiscountString);
        double newCashDiscount = Double.parseDouble(newCashDiscountString);

        if(order.getExtraPaymentValue() != extraPayment){
            order.setExtraPaymentValue(extraPayment);
        }

        if(order.getPercentageDiscount() != newPercentageDiscountDouble){
            order.setPercentageDiscount(newPercentageDiscountDouble);
        }

        if(order.getCashDiscount() != newCashDiscount){
            order.setCashDiscount(newCashDiscount); // ustawienie nowego rabatu
        }
        orderRepository.save(order);
    }

    @Transactional
    public int getUsersAmountForOrder(String orderId){
        return orderRepository.getUsersAmountForOrder(orderId);
    }

}
