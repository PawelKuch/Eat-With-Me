package pl.dskimina.foodsy.entity.data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ListViewOrderData {

    private final String orderId;
    private final String restaurantName;
    private final Date closingDate;
    private final double orderValue;
    private final String description;
    private final boolean isClosed;
    private final String ownerFirstName;
    private final String ownerLastName;

    public ListViewOrderData(String orderId, String restaurantName, LocalDateTime closingDate, double orderValue, String description, boolean isClosed, String ownerFirstName, String ownerLastName) {
        this.orderId = orderId;
        this.restaurantName = restaurantName;
        this.closingDate = Date.from(closingDate.atZone(ZoneId.systemDefault()).toInstant());
        this.orderValue = orderValue;
        this.description = description;
        this.isClosed = isClosed;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

}
