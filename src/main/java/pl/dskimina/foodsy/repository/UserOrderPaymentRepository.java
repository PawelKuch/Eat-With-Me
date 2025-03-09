package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dskimina.foodsy.entity.UserOrderPayment;

import java.util.List;

@Repository
public interface UserOrderPaymentRepository extends JpaRepository<UserOrderPayment, Long> {

    UserOrderPayment findByOrderOrderIdAndUserUserId(String orderId, String userId);
    boolean existsByOrderOrderIdAndUserUserId(String orderId, String userId);

    List<UserOrderPayment> findByOrderOrderId(String orderId);

    @Query("SELECT SUM(uop.extraPaymentValue) FROM UserOrderPayment uop WHERE uop.order.orderId = :orderId")
    Double getExtraPaymentValueForOrder(@Param("orderId") String orderId);

    @Query("SELECT SUM(uop.extraPaymentValue) FROM UserOrderPayment  uop WHERE uop.order.orderId = :orderId")
    Double getExtraPaymentValueForOrder(@Param("userId") String userId, @Param("orderId") String orderId);
}

