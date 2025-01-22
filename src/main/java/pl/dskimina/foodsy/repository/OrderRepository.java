package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dskimina.foodsy.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(String orderId);

}
