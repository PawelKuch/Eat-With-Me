package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.MenuItem;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.RestaurantData;

import java.util.List;


@Service
public class ToDataService {

    public MenuItemData convert(MenuItem menuItem) {
        MenuItemData menuItemData = new MenuItemData();
        menuItemData.setMenuItemId(menuItem.getMenuItemId());
        menuItemData.setName(menuItem.getName());
        menuItemData.setDescription(menuItem.getDescription());
        menuItemData.setCategory(menuItem.getCategory());
        menuItemData.setPrice(menuItem.getPrice());
        return menuItemData;
    }

    public RestaurantData convert(Restaurant restaurant) {
        RestaurantData restaurantData = new RestaurantData();
        restaurantData.setRestaurantId(restaurant.getRestaurantId());
        restaurantData.setName(restaurant.getName());
        restaurantData.setPhone(restaurant.getPhone());
        restaurantData.setEmail(restaurant.getEmail());
        restaurantData.setAddress(restaurant.getAddress());
        restaurantData.setImage(restaurant.getImage());
        restaurantData.setTags(restaurant.getTags());
        List<MenuItemData> menuItemsData = restaurant.getMenuItems().stream().map(this::convert).toList();
        restaurantData.setMenuItems(menuItemsData);
        return restaurantData;
    }
}
