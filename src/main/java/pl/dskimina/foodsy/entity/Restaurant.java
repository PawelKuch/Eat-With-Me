package pl.dskimina.foodsy.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "restaurant_id", unique = true, nullable = false, length = 512)
    private String restaurantId;

    @Column(name = "name", nullable = false, length = 512)
    private String name;

    @Column(name="phone", nullable = false, length = 512)
    private String phone;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name="address", nullable = false, length = 512)
    private String address;

    @Column(name = "image", nullable = true)
    @Lob
    private byte[] image;

    @Column(name = "tags", nullable = false, length = 512)
    private String tags;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;

    @OneToMany(mappedBy ="restaurant", cascade = CascadeType.ALL)
    private List<Order> orders;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String restaurantName) {
        this.name = restaurantName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return email;}

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setTags(String tags){
        this.tags = tags;
    }
    public String getTags() {
        return tags;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
