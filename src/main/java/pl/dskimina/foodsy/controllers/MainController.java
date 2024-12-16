package pl.dskimina.foodsy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.RestaurantService;
import pl.dskimina.foodsy.service.ToDataService;

import java.util.List;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    RestaurantService restaurantService;
    ToDataService toDataService;

    public MainController(RestaurantService restaurantService, ToDataService toDataService) {
        this.restaurantService = restaurantService;
        this.toDataService = toDataService;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        LOG.debug("Index page");
        return "template";
    }

    @GetMapping("/restaurant")
    public String addRestaurantView(Model model){
        model.addAttribute("restaurantList", toDataService.getRestaurants());
        return "restaurant";
    }

    @PostMapping("/restaurant")
    public RedirectView addRestaurant(@RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("description") String description){
        restaurantService.addRestaurant(name, phone, description);
        return new RedirectView("/restaurant");
    }

    @GetMapping("/restaurant-details/{id}")
    public String restaurantDetailsView(Model model, @PathVariable String id){
        RestaurantData restaurant = toDataService.getRestaurantByRestaurantId(id);
        model.addAttribute("restaurant", restaurant);
        return "restaurant-details";
    }

    @GetMapping("get-restaurant-list")
    @ResponseBody
    public List<RestaurantData> getRestaurantListForFetch(){
        return toDataService.getRestaurants();
    }
}
