package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.OrderItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.repository.MenuItemRepository;
import pl.dskimina.foodsy.repository.OrderItemRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final ToDataService toDataService;
    private final OrderItemRepository orderItemRepository;
    public OrderRepository orderRepository;
    public RestaurantRepository restaurantRepository;
    public MenuItemRepository menuItemRepository;
    public RestaurantService restaurantService;

    public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository,
                        MenuItemRepository menuItemRepository, ToDataService toDataService,
                        RestaurantService restaurantService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.toDataService = toDataService;
        this.restaurantService = restaurantService;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderData getOrderByOrderId(String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        return toDataService.convert(order);
    }

    @Transactional
    public OrderData createOrder(String restaurantId) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setDate(LocalDateTime.now());
        order.setValue(0.0);
        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItemList(orderItems);
        Restaurant restaurant = restaurantService.getRestaurantEntityByRestaurantId(restaurantId);
        order.setRestaurant(restaurant);
        orderRepository.save(order);
        return toDataService.convert(order);
    }

    @Transactional
    public void addOrderItemToOrderItemList(String orderId, String menuItemId) {
        Order order = orderRepository.findByOrderId(orderId);
        MenuItem menuItem = menuItemRepository.findByMenuItemId(menuItemId);

        orderRepository.save(order);
    }

    @Transactional
    public List<OrderData> getOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(toDataService::convert).toList();
    }


}
