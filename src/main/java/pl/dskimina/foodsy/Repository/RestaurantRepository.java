package pl.dskimina.foodsy.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dskimina.foodsy.Entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant FindByRestaurantId(String restaurantId);
}
