package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dskimina.foodsy.entity.OrderItem;
import pl.dskimina.foodsy.entity.data.UserOrderInfo;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderItemId(String orderItemId);

    @Query("SELECT new pl.dskimina.foodsy.entity.data.UserOrderInfo(oi.order.orderId, oi.user.userId, oi.user.firstName, oi.user.lastName, SUM(oi.price)) FROM OrderItem oi WHERE oi.order.orderId = :orderId AND oi.user.userId = :userId")
    UserOrderInfo getUserOrderInfo(@Param("orderId") String orderId, @Param("userId") String userId);




}
