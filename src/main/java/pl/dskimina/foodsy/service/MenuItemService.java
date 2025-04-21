package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.exception.MenuItemNotFoundException;
import pl.dskimina.foodsy.exception.RestaurantNotFoundException;
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

    @Transactional
    public void addMenuItem(String name, String category, String description, double price, String restaurantId) {
        MenuItem menuItem = new MenuItem();
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(restaurant == null) throw new RestaurantNotFoundException("Nie znaleziono żądanej restauracji id: " + restaurantId);

        menuItem.setMenuItemId(UUID.randomUUID().toString());
        menuItem.setName(name);
        menuItem.setCategory(category);
        menuItem.setPrice(price);
        menuItem.setDescription(description);
        menuItem.setRestaurant(restaurant);
        menuItemRepository.save(menuItem);
    }

    @Transactional
    public void deleteMenuItemByMenuItemIdAndRestaurantId(String restaurantId, String menuItemId) {
        MenuItem menuItem = menuItemRepository.findMenuItemByRestaurantIdAndMenuItemId(restaurantId, menuItemId);
        if (menuItem == null) throw new MenuItemNotFoundException("Nie znaleziono pozycji menu id: " + menuItemId);
        menuItemRepository.delete(menuItem);
    }

    @Transactional
    public void updateMenuItem(String restaurantId, String menuItemId, String name, String description, Double price) {
        MenuItem menuItem = menuItemRepository.findMenuItemByRestaurantIdAndMenuItemId(restaurantId, menuItemId);
        if(menuItem == null) throw new MenuItemNotFoundException("Nie znaleziono pozycji menu id: " + menuItemId);

        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItemRepository.save(menuItem);
    }
}
