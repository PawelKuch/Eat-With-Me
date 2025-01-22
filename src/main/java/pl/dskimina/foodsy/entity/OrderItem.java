package pl.dskimina.foodsy.entity;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name="order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_item_id")
    private String orderItemId;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "user")
    private List<User> users;

    @OneToMany(mappedBy = "order")
    private List<Order> orders;

    @OneToMany(mappedBy = "menuItem")
    private List<MenuItem> menuItemList;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderItemId() {
        return orderItemId;
    }
    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }
    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }
}
