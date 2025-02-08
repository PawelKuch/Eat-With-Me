package pl.dskimina.foodsy.entity.data;

import java.time.LocalDateTime;
import java.util.List;

public class OrderData {

    private String orderId;
    private double value;
    private LocalDateTime date;
    private boolean isClosed;
    private List<OrderItemData> orderItemList;
    private RestaurantData restaurantData;
    private UserData owner;

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean getIsClosed(){return isClosed;}
    public void setIsClosed(boolean isClosed){this.isClosed = isClosed;}

    public RestaurantData getRestaurantData() {
        return restaurantData;
    }
    public void setRestaurantData(RestaurantData restaurantData) {
        this.restaurantData = restaurantData;
    }

    public List<OrderItemData> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemData> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public UserData getOwner() {
        return owner;
    }

    public void setOwner(UserData owner) {
        this.owner = owner;
    }
}
