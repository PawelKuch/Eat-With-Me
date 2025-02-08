package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.OrderItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.entity.data.UserData;
import pl.dskimina.foodsy.repository.OrderRepository;

import java.time.LocalDateTime;
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

    @Transactional
    public OrderData createOrder(String restaurantId, String userId) {
        Order order = new Order();
        User user = userService.getUserInstanceById(userId);
        order.setOrderId(UUID.randomUUID().toString());
        order.setDate(LocalDateTime.now());
        order.setValue(0.0);
        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItemList(orderItems);
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
    public void addPercentageDiscount(String orderId, String percentageDiscount){
        Order order = orderRepository.findByOrderId(orderId);
        Double orderValue = order.getValue();
        double discountPercentageInt = Double.parseDouble(percentageDiscount);
        Double newOrderValue = orderValue - ((discountPercentageInt / 100) * orderValue);
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
}
