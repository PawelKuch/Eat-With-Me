package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dskimina.foodsy.entity.MenuItem;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
   MenuItem findByMenuItemId(String menuItemId);

   @Query("SELECT i FROM MenuItems i WHERE i.restaurantId = :restaurantId")
   List<MenuItem> getMenuItemListForRestaurantId(@Param("restaurantId") String id);

}
