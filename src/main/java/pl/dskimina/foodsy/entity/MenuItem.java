package pl.dskimina.foodsy.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_item_id", length = 512, nullable = false)
    private String menuItemId;

    @Column(name = "name", nullable = false, length = 512)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description", nullable = true, length = 512)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = true)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public String getName() {
        return name;
    }

    public String getCategory(){
        return category;
    }
    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
