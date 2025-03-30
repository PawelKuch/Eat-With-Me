package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dskimina.foodsy.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(String orderId);

    @Query("SELECT COUNT(DISTINCT oi.user.userId) FROM Order o JOIN OrderItem oi ON o.orderId = oi.order.orderId WHERE o.orderId = :orderId")
    int getUsersAmountForOrder(@Param("orderId") String orderId);

    @Query("SELECT o.cashDiscount FROM Order o WHERE o.orderId = :orderId")
    Double getCashDiscountForOrder(@Param("orderId") String orderId);

    @Query("SELECT o.percentageDiscount FROM Order o WHERE o.orderId = :orderId")
    Double getPercentageDiscountForOrder(@Param("orderId") String orderId);

}
