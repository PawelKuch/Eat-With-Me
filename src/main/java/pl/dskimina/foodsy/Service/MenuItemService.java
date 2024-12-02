package pl.dskimina.foodsy.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.Entity.MenuItem;
import pl.dskimina.foodsy.Repository.MenuItemRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MenuItemService {
    MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public void addMenuItem(String name, String description, double price){
        MenuItem menuItem = new MenuItem();
        menuItem.setMenuItemId(UUID.randomUUID().toString());
        menuItem.setName(name);

        if(description == null){
            menuItem.setDescription("No description");
        }else {
            menuItem.setDescription(description);
        }

        menuItem.setPrice(price);
        menuItemRepository.save(menuItem);
    }

    @Transactional
    public List<MenuItem> getMenuItems(){
        return menuItemRepository.findAll();
    }

}
