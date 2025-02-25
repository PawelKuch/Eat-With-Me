package pl.dskimina.foodsy.entity.data;

import java.util.Date;
import java.util.List;

public class OrderData {

    private String orderId;
    private double value;
    private Date closingDateTime;
    private String description;
    private double minValue;
    private boolean isClosed;
    private List<OrderItemData> orderItemList;
    private RestaurantData restaurantData;
    private UserData owner;
    private List<ExtraPaymentData> extraPaymentList;
    private List<DiscountData> discountList;
    private List<UserOrderPaymentData> userOrderPaymentList;

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

    public Date getClosingDateTime() {
        return closingDateTime;
    }
    public void setClosingDateTime(Date date) {
        this.closingDateTime = date;
    }

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
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

    public List<ExtraPaymentData> getExtraPaymentList() {
        return extraPaymentList;
    }
    public void setExtraPaymentList(List<ExtraPaymentData> extraPaymentDataList) {
        this.extraPaymentList = extraPaymentDataList;
    }

    public List<DiscountData> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<DiscountData> discountList) {
        this.discountList = discountList;
    }

    public List<UserOrderPaymentData> getUserOrderPaymentList() {
        return userOrderPaymentList;
    }

    public void setUserOrderPaymentList(List<UserOrderPaymentData> userOrderPaymentList) {
        this.userOrderPaymentList = userOrderPaymentList;
    }
}
