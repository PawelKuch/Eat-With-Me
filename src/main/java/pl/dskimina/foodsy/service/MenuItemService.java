package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.controllers.RestaurantController;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.repository.MenuItemRepository;
import pl.dskimina.foodsy.repository.RestaurantRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MenuItemService {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final ToDataService toDataService;
    private final WebInvocationPrivilegeEvaluator privilegeEvaluator;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository, ToDataService toDataService, WebInvocationPrivilegeEvaluator privilegeEvaluator) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.toDataService = toDataService;
        this.privilegeEvaluator = privilegeEvaluator;
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
    public List<MenuItemData> getMenuItems() {
        List<MenuItem> menuItemList = menuItemRepository.findAll();
        return menuItemList.stream()
                .map(toDataService::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuItemData getMenuItemByMenuItemId(String menuItemId) {
        MenuItem menuItem = menuItemRepository.findByMenuItemId(menuItemId);
        return toDataService.convert(menuItem);
    }

    @Transactional
    public List<MenuItemData> getMenuItemListForRestaurantId(String restaurantId) {
        List<MenuItem> menuItemDataList = menuItemRepository.getMenuItemListForRestaurantId(restaurantId);
        return menuItemDataList.stream().map(toDataService::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteMenuItemByMenuItemId(String menuItemId){
        MenuItem menuItem = menuItemRepository.findByMenuItemId(menuItemId);
        if (menuItem != null) {
            menuItemRepository.delete(menuItem);
            return true;
        }
        return false;
    }

    @Transactional
    public void updateMenuItem(String menuItemId, String name, String description, String price) {
        MenuItem menuItem = menuItemRepository.findByMenuItemId(menuItemId);
        double newPrice = Double.parseDouble(price);
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(newPrice);
        menuItemRepository.save(menuItem);
    }
}
