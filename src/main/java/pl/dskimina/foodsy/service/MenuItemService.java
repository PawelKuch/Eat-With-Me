package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.controllers.MainController;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.repository.MenuItemRepository;
import pl.dskimina.foodsy.repository.RestaurantRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MenuItemService {
    private final RestaurantRepository restaurantRepository;
    MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @Transactional
    public void addMenuItem(String name, String category, String description, double price, String restaurantId){
        MenuItem menuItem = new MenuItem();
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(restaurant != null){
            LOG.info("restaurant has been found");
        }
        menuItem.setMenuItemId(UUID.randomUUID().toString());
        menuItem.setName(name);
        menuItem.setCategory(category);
        menuItem.setPrice(price);
        menuItem.setDescription(description);
        menuItem.setRestaurant(restaurant);
        menuItemRepository.save(menuItem);
    }

    @Transactional
    public List<MenuItem> getMenuItems(){
        return menuItemRepository.findAll();
    }

    @Transactional
    public MenuItem getMenuItemByMenuItemId(String menuItemId){
        return menuItemRepository.findByMenuItemId(menuItemId);
    }
}
