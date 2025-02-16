package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dskimina.foodsy.entity.MenuItem;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
   MenuItem findByMenuItemId(String menuItemId);

   @Query("SELECT i FROM MenuItem i WHERE i.restaurant.restaurantId = :restaurantId AND i.menuItemId = :menuItemId")
   MenuItem findMenuItemByRestaurantIdAndMenuItemId(@Param("restaurantId") String restaurantId, @Param("menuItemId") String menuItemId);

}
