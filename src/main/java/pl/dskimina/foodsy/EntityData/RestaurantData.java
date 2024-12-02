package pl.dskimina.foodsy.EntityData;

import java.util.List;

public class RestaurantData {
    private String restaurantId;
    private String name;
    private String phone;
    private String description;
    private byte[] image;
    private List<MenuItemData> menuItems;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<MenuItemData> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemData> menuItems) {
        this.menuItems = menuItems;
    }
}
