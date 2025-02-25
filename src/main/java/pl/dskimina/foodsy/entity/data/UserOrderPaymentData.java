package pl.dskimina.foodsy.entity.data;

public class UserOrderPaymentData {

    private String userOrderPaymentId;
    private OrderData orderData;
    private UserData user;
    private Double amountToPay;
    private Double extraPaymentValue;
    private boolean isPaid;
    private Double discountValueInCash;
    private Double discountInPercentage;

    public String getUserOrderPaymentId() {
        return userOrderPaymentId;
    }

    public void setUserOrderPaymentId(String userOrderPaymentId) {
        this.userOrderPaymentId = userOrderPaymentId;
    }

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Double getExtraPaymentValue() {
        return extraPaymentValue;
    }

    public void setExtraPaymentValue(Double extraPaymentValue) {
        this.extraPaymentValue = extraPaymentValue;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Double getDiscountValueInCash() {
        return discountValueInCash;
    }

    public void setDiscountValueInCash(Double discountValueInCash) {
        this.discountValueInCash = discountValueInCash;
    }

    public Double getDiscountInPercentage() {
        return discountInPercentage;
    }

    public void setDiscountInPercentage(Double discountInPercentage) {
        this.discountInPercentage = discountInPercentage;
    }
}
