package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dskimina.foodsy.entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
