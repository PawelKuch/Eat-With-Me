package pl.dskimina.foodsy.entity.data;

import pl.dskimina.foodsy.entity.User;

import java.util.List;

public class ProductSummary {

    private String productName;
    private Long totalQuantity;
    private Double totalPrice;
    private String userFirstName;
    private String userLastname;

    public ProductSummary(String productName, Long totalQuantity, Double price, String userFirstName, String userLastname) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalQuantity * price;
        this.userFirstName = userFirstName;
        this.userLastname = userLastname;
    }

    public String getProductName() {
        return productName;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastname() {
        return userLastname;
    }
}
