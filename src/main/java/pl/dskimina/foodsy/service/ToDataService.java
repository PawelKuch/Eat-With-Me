package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.*;
import pl.dskimina.foodsy.entity.data.*;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;


@Service
public class ToDataService {

    public MenuItemData convert(MenuItem menuItem) {
        MenuItemData menuItemData = new MenuItemData();
        menuItemData.setMenuItemId(menuItem.getMenuItemId());
        menuItemData.setName(menuItem.getName());
        menuItemData.setDescription(menuItem.getDescription());
        menuItemData.setCategory(menuItem.getCategory());
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
        orderData.setValue(order.getValue());
        orderData.setClosingDateTime(Date.from(order.getClosingDate().toInstant(ZoneOffset.ofHours(+1))));
        orderData.setDescription(order.getDescription());
        orderData.setMinValue(order.getMinValue());
        orderData.setIsClosed(order.getIsClosed());
        orderData.setOwner(convert(order.getOwner()));
        orderData.setExtraPaymentList(order.getExtraPayments().stream().map(this::convert).toList());
        orderData.setOrderItemList(order.getOrderItemList().stream().map(this::convert). toList());
        orderData.setDiscountList(order.getDiscounts().stream().map(this::convert).toList());
        orderData.setUserOrderPaymentList(order.getUserOrderPayments().stream().map(this::convert).toList());
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

    public ExtraPaymentData convert(ExtraPayment extraPayment){
        ExtraPaymentData extraPaymentData = new ExtraPaymentData();
        extraPaymentData.setExtraPaymentId(extraPayment.getExtraPaymentId());
        extraPaymentData.setExtraPaymentProduct(extraPayment.getProduct());
        extraPaymentData.setPrice(extraPayment.getPrice());
        return extraPaymentData;
    }

    public DiscountData convert(Discount discount){
        DiscountData discountData = new DiscountData();
        discountData.setDiscountId(discount.getDiscountId());
        discountData.setIsPercentage(discount.getIsPercentage());
        if(discount.getIsPercentage()){
            discountData.setValue(discount.getValue()*100.0);
        } else {
            discountData.setValue(discount.getValue());
        }
        return discountData;
    }

    public UserOrderPaymentData convert(UserOrderPayment userOrderPayment) {
        UserOrderPaymentData userPayment = new UserOrderPaymentData();
        userPayment.setUserOrderPaymentId(userOrderPayment.getUserOrderPaymentId());
        userPayment.setAmountToPay(userOrderPayment.getAmountToPay());
        userPayment.setExtraPaymentValue(userOrderPayment.getExtraPaymentValue());
        userPayment.setIsPaid(userOrderPayment.getIsPaid());
        userPayment.setUser(convert(userOrderPayment.getUser()));
        userPayment.setDiscountInPercentage(userOrderPayment.getDiscountInPercentage() * 100);
        userPayment.setDiscountValueInCash(userOrderPayment.getDiscountValueInCash());
        return userPayment;
    }
}
