package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.*;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.repository.OrderRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final ToDataService toDataService;
    private final OrderRepository orderRepository;
    private final RestaurantService restaurantService;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, ToDataService toDataService, RestaurantService restaurantService, UserService userService) {
        this.orderRepository = orderRepository;
        this.toDataService = toDataService;
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @Transactional
    public OrderData getOrderByOrderId(String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        return toDataService.convert(order);
    }

    @Transactional Order findOrderByOrderId(String orderId){
        return orderRepository.findByOrderId(orderId);
    }

    @Transactional
    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    @Transactional
    public OrderData createOrder(String restaurantId, String userId, String closingDateString, String minValueString, String description) {
        Order order = new Order();
        User user = userService.getUserInstanceById(userId);
        order.setOrderId(UUID.randomUUID().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime closingDate = LocalDateTime.parse(closingDateString, formatter);
        order.setClosingDate(closingDate);
        order.setDescription(description);
        Double minValue = Double.parseDouble(minValueString);
        order.setMinValue(minValue);
        order.setValue(0.0);
        List<OrderItem> orderItems = new ArrayList<>();
        List<ExtraPayment> extraPayments = new ArrayList<>();
        List<Discount> discounts = new ArrayList<>();
        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        order.setUserOrderPayments(userOrderPayments);
        order.setOrderItemList(orderItems);
        order.setExtraPayments(extraPayments);
        order.setDiscounts(discounts);
        order.setOwner(user);
        Restaurant restaurant = restaurantService.getRestaurantEntityByRestaurantId(restaurantId);
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
    public void addPercentageDiscount(String orderId, String percentageDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        Double orderValue = order.getValue();
        double discountPercentage = Double.parseDouble(percentageDiscountString);
        Double newOrderValue = orderValue - ((discountPercentage / 100) * orderValue);
        order.setValue(newOrderValue);
        orderRepository.save(order);
    }

    @Transactional
    public void addCashDiscount(String orderId, String cashDiscount){
        Order order = orderRepository.findByOrderId(orderId);
        Double orderValue = order.getValue();
        Double discountValue = Double.parseDouble(cashDiscount);
        Double newOrderValue = orderValue - discountValue;
        order.setValue(newOrderValue);
        orderRepository.save(order);
    }

    @Transactional
    public void addExtraPayment(String orderId, String extraPayment){
        Order order = orderRepository.findByOrderId(orderId);
        Double orderValue = order.getValue();
        Double extraPaymentValue = Double.parseDouble(extraPayment);
        Double newOrderValue = orderValue + extraPaymentValue;
        order.setValue(newOrderValue);
        orderRepository.save(order);
    }

    @Transactional
    public void closeOrder(String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        order.setIsClosed(true);
        orderRepository.save(order);
    }

    @Transactional
    public int getUsersAmountForOrder(String orderId){
        return orderRepository.getUsersAmountForOrder(orderId);
    }

}
