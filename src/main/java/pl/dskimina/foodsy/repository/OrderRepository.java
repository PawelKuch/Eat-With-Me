package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.data.ListViewOrderData;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(String orderId);

    @Query("SELECT COUNT(DISTINCT oi.user.userId) FROM Order o JOIN OrderItem oi ON o.orderId = oi.order.orderId WHERE o.orderId = :orderId")
    int getUsersAmountForOrder(@Param("orderId") String orderId);

    @Query("SELECT DISTINCT (u.userId) FROM User u JOIN OrderItem oi ON u.userId = oi.user.userId WHERE oi.order.orderId = :orderId")
    List<String> getUsersIdForOrder(@Param("orderId") String orderId);

    @Query("SELECT new pl.dskimina.foodsy.entity.data.ListViewOrderData (o.orderId, r.name, o.closingDate, COALESCE(SUM(oi.price), 0), o.description, o.isClosed, o.owner.firstName, o.owner.lastName) " +
            "FROM Order o " +
            "LEFT JOIN OrderItem oi ON o = oi.order " +
            "JOIN Restaurant r ON o.restaurant = r " +
            "GROUP BY o.orderId, o.closingDate, o.description, o.isClosed, o.owner.firstName, o.owner.lastName, r.name")
    List<ListViewOrderData> getListViewOrderData();

}
