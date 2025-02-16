package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.controllers.RestaurantController;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.repository.MenuItemRepository;
import pl.dskimina.foodsy.repository.RestaurantRepository;
import java.util.UUID;


@Service
public class MenuItemService {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantController.class);

    @Transactional
    public void addMenuItem(String name, String category, String description, double price, String restaurantId) {
        MenuItem menuItem = new MenuItem();
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if (restaurant != null) {
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
    public boolean deleteMenuItemByMenuItemIdAndRestaurantId(String restaurantId, String menuItemId){
        MenuItem menuItem = menuItemRepository.findMenuItemByRestaurantIdAndMenuItemId(restaurantId, menuItemId);
        if (menuItem != null) {
            menuItemRepository.delete(menuItem);
            return true;
        }
        return false;
    }

    @Transactional
    public void updateMenuItem(String restaurantId, String menuItemId, String name, String description, Double price) {
        MenuItem menuItem = menuItemRepository.findMenuItemByRestaurantIdAndMenuItemId(restaurantId, menuItemId);
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItemRepository.save(menuItem);
    }
}
