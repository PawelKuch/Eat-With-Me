package pl.dskimina.foodsy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.service.RestaurantService;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    RestaurantService restaurantService;

    public MainController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        LOG.debug("Index page");
        return "template";
    }

    @GetMapping("/add-restaurant")
    public String addRestaurantView(){
        return "add-restaurant";
    }

    @PostMapping("/add-restaurant")
    public RedirectView addRestaurant(@RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("description") String description){
        restaurantService.addRestaurant(name, phone, description);
        return new RedirectView("/add-restaurant");
    }

}
