package pl.dskimina.foodsy.entity.data;

public class MenuItemData {

    private String menuItemId;
    private String name;
    private String category;
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

    public String getCategory(){
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
