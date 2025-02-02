package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dskimina.foodsy.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderItemId(String orderItemId);
}
