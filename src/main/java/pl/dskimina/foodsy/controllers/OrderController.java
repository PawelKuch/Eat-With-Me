package pl.dskimina.foodsy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.RestaurantService;

import java.util.List;

@Controller
public class OrderController {
    private final RestaurantService restaurantService;

    public OrderController(RestaurantService restaurantService) {this.restaurantService = restaurantService;}

    @GetMapping("/order-restaurant")
    public String orderRestaurant(Model model) {
        model.addAttribute("restaurantList", restaurantService.getRestaurants());
        return "order-restaurant";
    }

    @GetMapping("/order-menu/{restaurantId}")
    public String orderMenu(@PathVariable("restaurantId") String restaurantId, Model model) {
        RestaurantData restaurant = restaurantService.getRestaurantByRestaurantId(restaurantId);
        List<MenuItemData> menuItemForRestaurant = restaurant.getMenuItems();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItemList", menuItemForRestaurant);
        return "order-menu";
    }
}
