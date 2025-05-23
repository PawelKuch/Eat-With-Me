package pl.dskimina.foodsy.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.OrderItem;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.exception.MenuItemNotFoundException;
import pl.dskimina.foodsy.exception.OrderItemNotFoundException;
import pl.dskimina.foodsy.exception.OrderNotFoundException;
import pl.dskimina.foodsy.exception.UserNotFoundException;
import pl.dskimina.foodsy.repository.*;

import java.util.Objects;
import java.util.UUID;

@Service
public class OrderItemService {
    private final static Logger LOG = LoggerFactory.getLogger(OrderItemService.class);
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, UserRepository userRepository, OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public void createOrderItem(String userId, String menuItemId, String price, String orderId, String description) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(UUID.randomUUID().toString());
        User user = userRepository.findByUserId(userId);
        MenuItem menuItem = menuItemRepository.findByMenuItemId(menuItemId);
        Order order = orderRepository.findByOrderId(orderId);

        if(user == null) throw new UserNotFoundException("Nie znaleziono użytkownika o żądanym id: " + userId);
        if(menuItem == null) throw new MenuItemNotFoundException("Nie znaleziono pozycji menu o żądanym id: " + menuItemId);
        if(order == null) throw new OrderNotFoundException("Nie znaleziono zamówienia o żądanym id: " + orderId);

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
        double cashDiscountValue = order.getCashDiscount();
        double extraPaymentValueForOrder = order.getExtraPaymentValue();
        double orderItemsValueForOrder = Objects.requireNonNullElse(orderItemRepository.getOrderItemsValueForOrder(order.getOrderId()), 0.0);


        double baseForPercentageDiscount = orderItemsValueForOrder - cashDiscountValue;
        double newPercentageDiscountValueInCash = Math.round((baseForPercentageDiscount * (order.getPercentageDiscount() / 100)) * 100.0) / 100.0;
        LOG.debug("newPercentageDiscountValueInCash in setValueForOrder: {}", newPercentageDiscountValueInCash);
        double newValue = orderItemsValueForOrder - cashDiscountValue - newPercentageDiscountValueInCash + extraPaymentValueForOrder;
        order.setPercentageDiscountCashValue(newPercentageDiscountValueInCash);
        order.setNetValue(orderItemsValueForOrder);
        order.setValue(newValue);
        order.setBaseValue(orderItemsValueForOrder);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrderItem(String orderItemId) {
        OrderItem orderItem = orderItemRepository.findByOrderItemId(orderItemId);
        if(orderItem == null) throw new OrderItemNotFoundException("Brak pozycji w zamówieniu. ID: " + orderItemId);
        Order order = orderItem.getOrder();
        double currentValueOfOrder = order.getValue() - orderItem.getPrice();
        order.setValue(currentValueOfOrder);
        orderRepository.save(order);
        orderItemRepository.delete(orderItem);
    }
}
