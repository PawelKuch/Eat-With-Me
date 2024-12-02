package pl.dskimina.foodsy.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dskimina.foodsy.Entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
   MenuItem findByMenuItemId(String menuItemId);
}
