package pl.dskimina.foodsy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.MenuItemService;
import pl.dskimina.foodsy.service.RestaurantService;
import pl.dskimina.foodsy.service.ToDataService;

import java.util.List;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    private final MenuItemService menuItemService;
    RestaurantService restaurantService;
    ToDataService toDataService;

    public MainController(RestaurantService restaurantService, ToDataService toDataService, MenuItemService menuItemService) {
        this.restaurantService = restaurantService;
        this.toDataService = toDataService;
        this.menuItemService = menuItemService;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        LOG.debug("Index page");
        return "template";
    }

    @GetMapping("/restaurant")
    public String addRestaurantView(Model model){
        model.addAttribute("restaurantList", restaurantService.getRestaurants());
        return "restaurant";
    }

    @PostMapping("/restaurant")
    public RedirectView addRestaurant(@RequestParam("name") String name,
                                      @RequestParam("phone") String phone,
                                      @RequestParam("email") String email,
                                      @RequestParam("address") String address,
                                      @RequestParam("tags") String tags){
        restaurantService.addRestaurant(name, phone,email, address, tags);
        return new RedirectView("/restaurant");
    }

    @GetMapping("/restaurant-menu/{id}")
    public String restaurantMenuView(@PathVariable String id, Model model){
        List<MenuItemData> restaurantMenuItemList = menuItemService.getMenuItemListForRestaurantId(id);
        RestaurantData restaurant = restaurantService.getRestaurantByRestaurantId(id);

        model.addAttribute("restaurantMenuItemList", restaurantMenuItemList);
        model.addAttribute("restaurant", restaurant);
        return "restaurant-menu";
    }

    @PostMapping("/restaurant-menu")
    public RedirectView restaurantMenu(@RequestParam("name") String name,
                                       @RequestParam("price") double price,
                                       @RequestParam("description") String description,
                                       @RequestParam("category") String category,
                                       @RequestParam("restaurantId") String restaurantId){
        menuItemService.addMenuItem(name, category, description, price, restaurantId);
        return new RedirectView("/restaurant-menu/" + restaurantId);
    }

    @GetMapping("/restaurant-details/{id}")
    public String restaurantDetailsView(Model model, @PathVariable String id){
        RestaurantData restaurant = restaurantService.getRestaurantByRestaurantId(id);
        model.addAttribute("restaurant", restaurant);
        return "restaurant-details";
    }

    @GetMapping("get-restaurant-list")
    @ResponseBody
    public List<RestaurantData> getRestaurantListForFetch(){
        return restaurantService.getRestaurants();
    }
}
