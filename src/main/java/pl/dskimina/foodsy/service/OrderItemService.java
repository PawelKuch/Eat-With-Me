package pl.dskimina.foodsy.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.OrderItem;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.repository.MenuItemRepository;
import pl.dskimina.foodsy.repository.OrderItemRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemService {

    private final OrderService orderService;
    OrderItemRepository orderItemRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    MenuItemRepository menuItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, UserRepository userRepository, OrderRepository orderRepository, MenuItemRepository menuItemRepository, OrderService orderService) {
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderService = orderService;
    }

    @Transactional
    public void createOrderItem(String userId, String menuItemId, String price, String orderId, String description){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(UUID.randomUUID().toString());
        User user = userRepository.findByUserId(userId);
        MenuItem menuItem = menuItemRepository.findByMenuItemId(menuItemId);
        Order order = orderRepository.findByOrderId(orderId);

        orderItem.setUser(user);
        orderItem.setMenuItem(menuItem);
        orderItem.setOrder(order);
        orderItem.setDescription(description);
        orderItem.setPrice(Double.parseDouble(price));
        orderItemRepository.save(orderItem);
        setValueForOrder(order);
    }

    @Transactional
    public void setValueForOrder(Order order){
        List<OrderItem> orderItemList = order.getOrderItemList();
        Double value = orderItemList.stream().mapToDouble(OrderItem::getPrice).sum();
        order.setValue(value);
        orderRepository.save(order);
    }
}
