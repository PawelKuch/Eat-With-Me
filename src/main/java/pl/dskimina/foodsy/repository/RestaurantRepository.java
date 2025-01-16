package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dskimina.foodsy.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findByRestaurantId(String restaurantId);

    @Query("SELECT r.image FROM Restaurant r WHERE r.restaurantId = ?1")
    byte[] getImageByRestaurantId(String restaurantId);
}
