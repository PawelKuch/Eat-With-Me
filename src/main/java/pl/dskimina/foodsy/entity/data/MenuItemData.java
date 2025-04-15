package pl.dskimina.foodsy.entity.data;

public class MenuItemData {

    private String menuItemId;
    private String name;
    private String description;
    private double price;
    private RestaurantData restaurant;

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemDataId) {
        this.menuItemId = menuItemDataId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setRestaurant(RestaurantData restaurant) {
        this.restaurant = restaurant;
    }

    public RestaurantData getRestaurant() {
        return restaurant;
    }
    public void setPrice(double price) {
        this.price = price;
    }

}
