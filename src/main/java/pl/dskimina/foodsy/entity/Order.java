package pl.dskimina.foodsy.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "order_value")
    private Double value;

    @Column(name = "order_date")
    private LocalDateTime closingDate;

    @Column(name = "description")
    private String description;

    @Column(name = "min_value")
    private Double minValue;

    @Column(name = "is_closed")
    private boolean isClosed;

    @Column(name = "percentage_discount")
    private Double percentageDiscount; //e.g. 10% = 0,1

    @Column(name = "percentage_discount_cash_value")
    private Double percentageDiscountCashValue;

    @Column(name = "cash_discount")
    private Double cashDiscount;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "order")
    private List<ExtraPayment> extraPayments;

    @OneToMany(mappedBy = "order")
    private List<Discount> discounts;

    @OneToMany(mappedBy = "order")
    private List<UserOrderPayment> userOrderPayments;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }
    public void setClosingDate(LocalDateTime date) {
        this.closingDate = date;
    }

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
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


    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItems;
    }

    public void setOrderItemList(List<OrderItem> orderItem) {
        this.orderItems = orderItem;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<ExtraPayment> getExtraPayments() {
        return extraPayments;
    }
    public void setExtraPayments(List<ExtraPayment> extraPayments) {
        this.extraPayments = extraPayments;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public List<UserOrderPayment> getUserOrderPayments() {
        return userOrderPayments;
    }

    public void setUserOrderPayments(List<UserOrderPayment> userOrderPayments) {
        this.userOrderPayments = userOrderPayments;
    }
}
