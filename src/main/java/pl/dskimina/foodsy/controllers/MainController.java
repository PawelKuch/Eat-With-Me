package pl.dskimina.foodsy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.dskimina.foodsy.Service.RestaurantService;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    private RestaurantService restaurantService;

    public MainController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        LOG.debug("Index page");
        return "template";
    }

    @GetMapping("add-restaurant")
    public String addRestaurant(){
        return "add-restaurant";
    }

}
