package pl.dskimina.foodsy.entityData;

public class MenuItemData {

    private String menuItemDataId;
    private String name;
    private String description;
    private double price;

    public String getMenuItemDataId() {
        return menuItemDataId;
    }

    public void setMenuItemDataId(String menuItemDataId) {
        this.menuItemDataId = menuItemDataId;
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

    public void setPrice(double price) {
        this.price = price;
    }

}
