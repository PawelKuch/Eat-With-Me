package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.controllers.MainController;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.repository.MenuItemRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MenuItemService {
    MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @Transactional
    public void addMenuItem(String name, String description, double price){
        MenuItem menuItem = new MenuItem();
        menuItem.setMenuItemId(UUID.randomUUID().toString());
        menuItem.setName(name);

        menuItem.setDescription(description);

        menuItem.setPrice(price);
        if(name == null || name.isEmpty()){
            LOG.info("name is empty");
        }else if (description == null || description.isEmpty()){
            LOG.info("description is empty");
        }else if (price == 0){
            LOG.info("price is null");
        }
        menuItemRepository.save(menuItem);
    }

    @Transactional
    public List<MenuItem> getMenuItems(){
        return menuItemRepository.findAll();
    }

}
