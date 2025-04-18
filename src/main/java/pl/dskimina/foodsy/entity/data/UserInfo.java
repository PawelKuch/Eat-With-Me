package pl.dskimina.foodsy.entity.data;

public class UserInfo {

    private String firstName;
    private String lastName;

    private double orderItemsValue;
    private double extraPayment;
    private double cashDiscount;
    private double percentageDiscount;
    private double percentageDiscountInCash;
    private double generalDiscount;
    private double amountToPayWithoutExtraPayment;
    private double amountToPay;

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

    public double getMenuItemsValue() {
        return orderItemsValue;
    }

    public void setMenuItemsValue(double menuItemsValue) {
        this.orderItemsValue = menuItemsValue;
    }

    public double getExtraPayment() {
        return extraPayment;
    }

    public void setExtraPayment(double extraPayment) {
        this.extraPayment = extraPayment;
    }

    public double getCashDiscount() {
        return cashDiscount;
    }

    public void setCashDiscount(double cashDiscount) {
        this.cashDiscount = cashDiscount;
    }

    public double getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public double getPercentageDiscountInCash() {
        return percentageDiscountInCash;
    }

    public void setPercentageDiscountInCash(double percentageDiscountInCash) {
        this.percentageDiscountInCash = percentageDiscountInCash;
    }

    public double getGeneralDiscount() {
        return generalDiscount;
    }

    public void setGeneralDiscount(double generalDiscount) {
        this.generalDiscount = generalDiscount;
    }

    public double getAmountToPayWithoutExtraPayment() {
        return amountToPayWithoutExtraPayment;
    }

    public void setAmountToPayWithoutExtraPayment(double amountToPayWithoutExtraPayment) {
        this.amountToPayWithoutExtraPayment = amountToPayWithoutExtraPayment;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }
}
