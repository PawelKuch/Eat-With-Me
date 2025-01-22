package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.repository.MenuItemRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final ToDataService toDataService;
    public OrderRepository orderRepository;
    public RestaurantRepository restaurantRepository;
    public MenuItemRepository menuItemRepository;
    public RestaurantService restaurantService;

    public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository,
                        MenuItemRepository menuItemRepository, ToDataService toDataService,
                        RestaurantService restaurantService) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.toDataService = toDataService;
        this.restaurantService = restaurantService;
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
        List<MenuItem> menuItemList = new ArrayList<>();
        order.setMenuItemList(menuItemList);
        Restaurant restaurant = restaurantService.getRestaurantEntityByRestaurantId(restaurantId);
        order.setRestaurant(restaurant);
        orderRepository.save(order);
        return toDataService.convert(order);
    }

    @Transactional
    public void addItemToOrder(String orderId, String menuItemId) {
        Order order = orderRepository.findByOrderId(orderId);
        MenuItem menuItem = menuItemRepository.findByMenuItemId(menuItemId);
        order.addMenuItemToOrder(menuItem);
        orderRepository.save(order);
    }
}
