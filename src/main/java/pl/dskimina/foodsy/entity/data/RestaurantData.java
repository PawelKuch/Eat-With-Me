package pl.dskimina.foodsy.entity.data;

import java.util.List;

public class RestaurantData {
    private String restaurantId;
    private String name;
    private String phone;
    private String email;
    private String address;
    private byte[] image;
    private String tags;
    private List<MenuItemData> menuItems;
    private String base64LogoUrl;

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

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

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

    public void setBase64LogoUrl(String base64LogoUrl) {
        this.base64LogoUrl = base64LogoUrl;
    }
    public String getBase64LogoUrl() {
        return base64LogoUrl;
    }

    public void setTags(String tags) {this.tags = tags;}
    public String getTags() {return tags;}

    public List<MenuItemData> getMenuItems() {
        return menuItems;
    }
    public void setMenuItems(List<MenuItemData> menuItems) {
        this.menuItems = menuItems;
    }
}
