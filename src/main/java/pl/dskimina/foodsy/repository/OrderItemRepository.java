package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dskimina.foodsy.entity.OrderItem;
import pl.dskimina.foodsy.entity.data.ProductSummary;

import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderItemId(String orderItemId);

    @Query("SELECT SUM(oi.price) FROM OrderItem oi WHERE oi.order.orderId = :orderId AND oi.user.userId = :userId")
    Double getOrderItemsValueForUserAndOrder(@Param("orderId") String orderId, @Param("userId") String userId);

    @Query("SELECT SUM(oi.price) FROM OrderItem oi WHERE oi.order.orderId = :orderId" )
    Double getOrderItemsValueForOrder(@Param("orderId") String orderId);


    @Query("SELECT new pl.dskimina.foodsy.entity.data.ProductSummary(oi.menuItem.name, COUNT(oi), oi.price, oi.user.firstName, oi.user.lastName) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.orderId = :orderId " +
            "GROUP BY oi.menuItem.name, oi.menuItem.price, oi.user.firstName, oi.user.lastName")
    List<ProductSummary> findGroupedItemsByOrder(@Param("orderId") String orderId);
}
