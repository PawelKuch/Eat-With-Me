package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dskimina.foodsy.entity.ExtraPayment;

@Repository
public interface ExtraPaymentRepository extends JpaRepository<ExtraPayment, Long> {
    ExtraPayment findByExtraPaymentId(String extraPaymentId);

    @Query("SELECT SUM(ep.price) FROM ExtraPayment ep WHERE ep.order.orderId = :orderId")
    Double getExtraPaymentsValueForOrder(@Param("orderId") String orderId);

    ExtraPayment findByExtraPaymentIdAndOrderOrderId(String extraPaymentId, String orderId);



}
