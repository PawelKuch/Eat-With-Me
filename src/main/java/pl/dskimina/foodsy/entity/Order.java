package pl.dskimina.foodsy.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "value", nullable = false)
    private double value;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<MenuItem> menuItemList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;


}
