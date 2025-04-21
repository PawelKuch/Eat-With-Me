package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dskimina.foodsy.entity.OrderItem;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderItemId(String orderItemId);

    @Query("SELECT SUM(oi.price) FROM OrderItem oi WHERE oi.order.orderId = :orderId AND oi.user.userId = :userId")
    Double getOrderItemsValueForUserAndOrder(@Param("orderId") String orderId, @Param("userId") String userId);

    @Query("SELECT SUM(oi.price) FROM OrderItem oi WHERE oi.order.orderId = :orderId" )
    Double getOrderItemsValueForOrder(@Param("orderId") String orderId);




}
