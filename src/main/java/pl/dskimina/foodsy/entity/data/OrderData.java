package pl.dskimina.foodsy.entity.data;

import java.util.Date;
import java.util.List;

public class OrderData {

    private String orderId;
    private double value;
    private double netValue;
    private Date closingDateTime;
    private String description;
    private double minValue;
    private boolean isClosed;
    private Double percentageDiscount;
    private Double percentageDiscountCashValue;
    private Double cashDiscount;
    private List<OrderItemData> orderItemList;
    private RestaurantData restaurantData;
    private UserData owner;
    private double extraPaymentValue;

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

    public double getNetValue() {return netValue;}
    public void setNetValue(double netValue) {this.netValue = netValue;}

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

    public Double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(Double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public Double getPercentageDiscountCashValue() {
        return percentageDiscountCashValue;
    }

    public void setPercentageDiscountCashValue(Double percentageDiscountCashValue) {
        this.percentageDiscountCashValue = percentageDiscountCashValue;
    }

    public Double getCashDiscount() {
        return cashDiscount;
    }

    public void setCashDiscount(Double cashDiscount) {
        this.cashDiscount = cashDiscount;
    }


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

    public double getExtraPaymentValue(){return extraPaymentValue;}
    public void setExtraPaymentValue(double extraPaymentValue){this.extraPaymentValue = extraPaymentValue;}
}
