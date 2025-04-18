package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dskimina.foodsy.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(String orderId);

    @Query("SELECT COUNT(DISTINCT oi.user.userId) FROM Order o JOIN OrderItem oi ON o.orderId = oi.order.orderId WHERE o.orderId = :orderId")
    int getUsersAmountForOrder(@Param("orderId") String orderId);

    @Query("SELECT DISTINCT (u.userId) FROM User u JOIN OrderItem oi ON u.userId = oi.user.userId WHERE oi.order.orderId = :orderId")
    List<String> getUsersIdForOrder(@Param("orderId") String orderId);


}
