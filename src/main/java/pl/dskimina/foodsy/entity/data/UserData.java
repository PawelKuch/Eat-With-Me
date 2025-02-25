package pl.dskimina.foodsy.entity.data;

import java.util.List;

public class UserData {
    private String userId;
    private String firstName;
    private String lastName;
    private List<OrderItemData> orderItemDataList;
    private List<OrderData> orderList;
    private List<UserOrderPaymentData> userOrderPaymentData;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<OrderItemData> getOrderItemDataList() {
        return orderItemDataList;
    }

    public void setOrderItemDataList(List<OrderItemData> orderItemDataList) {
        this.orderItemDataList = orderItemDataList;
    }

    public List<OrderData> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderData> orderList) {
        this.orderList = orderList;
    }

    public List<UserOrderPaymentData> getUserOrderPaymentData() {
        return userOrderPaymentData;
    }

    public void setUserOrderPaymentData(List<UserOrderPaymentData> userOrderPaymentData) {
        this.userOrderPaymentData = userOrderPaymentData;
    }
}
