package pl.dskimina.foodsy.entity;

import jakarta.persistence.*;


@Entity
public class ExtraPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "extra_payment_id", nullable = false)
    String extraPaymentId;

    @Column(name = "product", nullable = false)
    String product;

    @Column(name = "price", nullable = false)
    double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;

    public String getExtraPaymentId() {
        return extraPaymentId;
    }

    public void setExtraPaymentId(String extraPaymentId) {
        this.extraPaymentId = extraPaymentId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
