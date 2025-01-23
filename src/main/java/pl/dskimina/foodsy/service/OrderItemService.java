package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.OrderItem;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.repository.MenuItemRepository;
import pl.dskimina.foodsy.repository.OrderItemRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.UserRepository;

import java.util.UUID;

@Service
public class OrderItemService {

    OrderItemRepository orderItemRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    MenuItemRepository menuItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, UserRepository userRepository, OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public void createOrderItem(String userId, String menuItemId, String price, String orderId){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(UUID.randomUUID().toString());
        User user = userRepository.findByUserId(userId);
        MenuItem menuItem = menuItemRepository.findByMenuItemId(menuItemId);
        Order order = orderRepository.findByOrderId(orderId);

        orderItem.setUser(user);
        orderItem.setMenuItem(menuItem);
        orderItem.setOrder(order);
        orderItem.setPrice(Double.parseDouble(price));
        orderItemRepository.save(orderItem);
    }
}
