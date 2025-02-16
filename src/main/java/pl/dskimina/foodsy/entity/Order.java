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

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;


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

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItem;
    }

    public void setOrderItemList(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
