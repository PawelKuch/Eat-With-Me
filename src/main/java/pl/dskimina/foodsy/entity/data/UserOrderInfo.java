package pl.dskimina.foodsy.entity.data;


public class UserOrderInfo {

    private String orderId;
    private String userId;
    private String firstName;
    private String lastName;
    private Double menuItemsValue;
    private Double extraPaymentValueForUser;
    private Double discountValueForUser;
    private int howManyUsersInOrder;

    public UserOrderInfo(String orderId, String userId, String firstName, String lastName, Double menuItemsValue) {
        this.orderId = orderId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.menuItemsValue = menuItemsValue;
    }

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

    public Double getMenuItemsValue() {
        return menuItemsValue;
    }

    public void setMenuItemsValue(Double menuItemsValue) {
        this.menuItemsValue = menuItemsValue;
    }

    public Double getExtraPaymentValueForUser() {
        return extraPaymentValueForUser;
    }

    public void setExtraPaymentValueForUser(Double extraPaymentValueForUser) {
        this.extraPaymentValueForUser = extraPaymentValueForUser;
    }

    public Double getDiscountValueForUser() {
        return discountValueForUser;
    }

    public void setDiscountValueForUser(Double discountValueForUser) {
        this.discountValueForUser = discountValueForUser;
    }

    public int getHowManyUsersInOrder() {
        return howManyUsersInOrder;
    }

    public void setHowManyUsersInOrder(int howManyUsersInOrder) {
        this.howManyUsersInOrder = howManyUsersInOrder;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
