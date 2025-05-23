package pl.dskimina.foodsy.entity.data;

public class OrderItemData {
    private String orderItemId;
    private Double price;
    private String description;
    private UserData user;
    private OrderData order;
    private MenuItemData menuItem;

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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public OrderData getOrder() {
        return order;
    }

    public void setOrder(OrderData order) {
        this.order = order;
    }

    public MenuItemData getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemData menuItem) {
        this.menuItem = menuItem;
    }
}
