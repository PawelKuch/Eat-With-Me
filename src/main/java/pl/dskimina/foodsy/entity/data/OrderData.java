package pl.dskimina.foodsy.entity.data;

import java.time.LocalDateTime;
import java.util.List;

public class OrderData {

    private String orderId;
    private double value;
    private LocalDateTime date;
    private OrderItemData orderItemData;
    private RestaurantData restaurantData;

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

    public RestaurantData getRestaurantData() {
        return restaurantData;
    }
    public void setRestaurantData(RestaurantData restaurantData) {
        this.restaurantData = restaurantData;
    }
}
