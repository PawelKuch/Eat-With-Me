package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.controllers.MainController;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.RestaurantData;

import java.util.List;


@Service
public class ToDataService {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

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

    public MenuItemData getMenuItemByMenuItemId(String id){
        MenuItem menuItem = menuItemService.getMenuItemByMenuItemId(id);
        return convert(menuItem);
    }

    public RestaurantData getRestaurantByRestaurantId(String id){
        Restaurant restaurant = restaurantService.getRestaurantByRestaurantId(id);
        return convert(restaurant);
    }



    public MenuItemData convert(MenuItem menuItem) {
        MenuItemData menuItemData = new MenuItemData();
        menuItemData.setMenuItemDataId(menuItem.getMenuItemId());
        menuItemData.setName(menuItem.getName());
        menuItemData.setCategory(menuItem.getCategory());
        menuItemData.setPrice(menuItem.getPrice());
        Restaurant restaurant1 = menuItem.getRestaurant();
        if (restaurant1 == null)
        {
            LOG.info("Restaurat in toDataService has not been found");
        }else {
            LOG.info("restaurant in toDataService has been found");
            RestaurantData restaurantData = convert(restaurant1);
            menuItemData.setRestaurant(restaurantData);
        }
        return menuItemData;
    }

    public RestaurantData convert(Restaurant restaurant) {
        RestaurantData restaurantData = new RestaurantData();
        restaurantData.setRestaurantId(restaurant.getRestaurantId());
        restaurantData.setName(restaurant.getName());
        restaurantData.setPhone(restaurant.getPhone());
        restaurantData.setDescription(restaurant.getDescription());
        restaurantData.setImage(restaurant.getImage());
        List<MenuItemData> menuItemsData = restaurant.getMenuItems().stream().map(this::convert).toList();
        restaurantData.setMenuItems(menuItemsData);
        return restaurantData;
    }

}
