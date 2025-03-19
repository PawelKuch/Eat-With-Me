package pl.dskimina.foodsy.entity.data;

public class UserOrderPaymentData {

    private String userOrderPaymentId;
    private OrderData orderData;
    private UserData user;
    private double menuItemsValue;
    private double amountToPayWithoutExtraPayment;
    private Double amountToPay;
    private Double extraPaymentValue;
    private boolean isPaid;
    private Double discountValueInCash;
    private Double discountInPercentage;
    private Double generalDiscountValue;
    private Double discountInPercentageInCash;
    private Double baseForPercentageDiscount;

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

    public double getMenuItemsValue() {
        return menuItemsValue;
    }

    public void setMenuItemsValue(double menuItemsValue) {
        this.menuItemsValue = menuItemsValue;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }
    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public double getAmountToPayWithoutExtraPayment() {return amountToPayWithoutExtraPayment;}
    public void setAmountToPayWithoutExtraPayment(double amountToPayWithoutExtraPayment) {this.amountToPayWithoutExtraPayment = amountToPayWithoutExtraPayment;}

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

    public Double getGeneralDiscountValue() {
        return generalDiscountValue;
    }

    public void setGeneralDiscountValue(Double generalDiscountValue) {
        this.generalDiscountValue = generalDiscountValue;
    }

    public Double getDiscountInPercentageInCash() {
        return discountInPercentageInCash;
    }

    public void setDiscountInPercentageInCash(Double discountInPercentageInCash) {
        this.discountInPercentageInCash = discountInPercentageInCash;
    }

    public Double getBaseForPercentageDiscount() {
        return baseForPercentageDiscount;
    }

    public void setBaseForPercentageDiscount(Double baseForPercentageDiscount) {
        this.baseForPercentageDiscount = baseForPercentageDiscount;
    }
}
