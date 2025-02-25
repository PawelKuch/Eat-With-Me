package pl.dskimina.foodsy.entity;

import jakarta.persistence.*;

@Entity
public class UserOrderPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_order_payment_id")
    private String userOrderPaymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount_to_pay")
    private Double amountToPay;

    @Column(name = "extra_payment_value")
    private Double extraPaymentValue;

    @Column(name = "discount_value_in_cash")
    private Double discountValueInCash;

    @Column(name = "generalDiscountValue")
    private Double generalDiscountValue;

    @Column(name = "discount_in_percentage")
    private Double discountInPercentage;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getUserOrderPaymentId() {
        return userOrderPaymentId;
    }

    public void setUserOrderPaymentId(String userOrderPaymentId) {
        this.userOrderPaymentId = userOrderPaymentId;
    }

    public void setExtraPaymentValue(Double extraPaymentValue) {
        this.extraPaymentValue = extraPaymentValue;
    }
    public Double getExtraPaymentValue() {return extraPaymentValue;}

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
