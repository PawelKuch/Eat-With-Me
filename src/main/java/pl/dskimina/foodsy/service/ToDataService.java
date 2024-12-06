package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entityData.MenuItemData;
import pl.dskimina.foodsy.entityData.RestaurantData;

import java.util.List;

@Service
public class ToDataService {
    MenuItemService menuItemService;
    RestaurantService restaurantService;

    public ToDataService(MenuItemService menuItemService, RestaurantService restaurantService) {
        this.menuItemService = menuItemService;
        this.restaurantService = restaurantService;
    }

    public List<RestaurantData> getRestaurants() {
        return restaurantService.getRestaurants().stream().map(this::convert).toList();
    }

    public List<MenuItemData> getMenuItems(){
        return menuItemService.getMenuItems().stream().map(this::convert).toList();
    }

    public MenuItemData convert(MenuItem menuItem) {
        MenuItemData menuItemData = new MenuItemData();
        menuItemData.setMenuItemDataId(menuItem.getMenuItemId());
        menuItemData.setName(menuItem.getName());
        menuItemData.setPrice(menuItem.getPrice());
        menuItemData.setDescription(menuItem.getDescription());
        return menuItemData;
    }

    public RestaurantData convert(Restaurant restaurant) {
        RestaurantData restaurantData = new RestaurantData();
        restaurantData.setRestaurantId(restaurant.getRestaurantId());
        restaurantData.setName(restaurant.getName());
        restaurantData.setDescription(restaurant.getDescription());
        restaurantData.setImage(restaurant.getImage());
        List<MenuItemData> menuItemsData = restaurant.getMenuItems().stream().map(this::convert).toList();
        restaurantData.setMenuItems(menuItemsData);
        return restaurantData;
    }

}
