package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.*;
import pl.dskimina.foodsy.entity.data.*;
import pl.dskimina.foodsy.repository.OrderItemRepository;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class ToDataService {

    private final OrderItemRepository orderItemRepository;

    public ToDataService(OrderItemRepository orderItemRepository) {this.orderItemRepository = orderItemRepository;}
    public MenuItemData convert(MenuItem menuItem) {
        MenuItemData menuItemData = new MenuItemData();
        menuItemData.setMenuItemId(menuItem.getMenuItemId());
        menuItemData.setName(menuItem.getName());
        menuItemData.setDescription(menuItem.getDescription());
        menuItemData.setPrice(menuItem.getPrice());
        return menuItemData;
    }

    public RestaurantData convert(Restaurant restaurant) {
        RestaurantData restaurantData = new RestaurantData();
        restaurantData.setRestaurantId(restaurant.getRestaurantId());
        restaurantData.setName(restaurant.getName());
        restaurantData.setPhone(restaurant.getPhone());
        restaurantData.setEmail(restaurant.getEmail());
        restaurantData.setAddress(restaurant.getAddress());
        restaurantData.setTags(restaurant.getTags());
        List<MenuItemData> menuItemsData = restaurant.getMenuItems().stream().map(this::convert).toList();
        restaurantData.setMenuItems(menuItemsData);
        return restaurantData;
    }

    public OrderData convert(Order order){
        OrderData orderData = new OrderData();
        orderData.setOrderId(order.getOrderId());
        orderData.setClosingDateTime(Date.from(order.getClosingDate().toInstant(ZoneOffset.ofHours(+1))));
        orderData.setDescription(order.getDescription());
        orderData.setMinValue(order.getMinValue());
        orderData.setIsClosed(order.getIsClosed());
        orderData.setOwner(convert(order.getOwner()));
        orderData.setOrderItemList(order.getOrderItemList().stream().map(this::convert). toList());
        orderData.setRestaurantData(convert(order.getRestaurant()));
        return orderData;
    }

    public UserData convert(User user){
        UserData userData = new UserData();
        userData.setUserId(user.getUserId());
        userData.setFirstName(user.getFirstName());
        userData.setLastName(user.getLastName());
        return userData;
    }

    public OrderItemData convert(OrderItem orderItem){
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setOrderItemId(orderItem.getOrderItemId());
        orderItemData.setPrice(orderItem.getPrice());
        orderItemData.setDescription(orderItem.getDescription());
        orderItemData.setUser(convert(orderItem.getUser()));
        orderItemData.setMenuItem(convert(orderItem.getMenuItem()));
        return orderItemData;
    }
}
