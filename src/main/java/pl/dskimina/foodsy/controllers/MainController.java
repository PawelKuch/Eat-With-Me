package pl.dskimina.foodsy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.entity.data.UserData;
import pl.dskimina.foodsy.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    private final MenuItemService menuItemService;
    RestaurantService restaurantService;
    ToDataService toDataService;
    UserService userService;
    SessionService sessionService;

    public MainController(RestaurantService restaurantService, ToDataService toDataService, MenuItemService menuItemService,
                          UserService userService, SessionService sessionService) {
        this.restaurantService = restaurantService;
        this.toDataService = toDataService;
        this.menuItemService = menuItemService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @ModelAttribute
    public void fillMode(Model modelMap){
        modelMap.addAttribute("users", userService.getUsers());
        modelMap.addAttribute("currentUser", sessionService.getCurrentUser());
    }


    @GetMapping("/index")
    public String index() {
        LOG.debug("Index page");
        return "template";
    }

    @GetMapping("/restaurant")
    public String addRestaurantView(Model model){
        model.addAttribute("restaurantList", restaurantService.getRestaurants());
        return "restaurant";
    }

    @PostMapping("/create-restaurant")
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
        model.addAttribute("restaurantMenuItemList", restaurant.getMenuItems());
        return "restaurant-details";
    }

   /* @GetMapping("get-restaurant-list")
    @ResponseBody
    public Map<String, Object> getRestaurantListForFetch(){
        Map<String, Object> response = new HashMap<>();
        response.put("restaurants: ", restaurantService.getRestaurants());
        response.put("restaurants amount: ", restaurantService.getRestaurants().size());
        return response;
    }*/


    @GetMapping("/logos/{restaurantId}")
    public ResponseEntity<byte[]> getLogoForRestaurant(@PathVariable String restaurantId){
        byte[] restaurantLogoBytes = restaurantService.getImageForRestaurantId(restaurantId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(restaurantLogoBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/update-restaurant/{restaurantId}")
    public RedirectView updatePhoneForRestaurant(@PathVariable String restaurantId, @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "tags", required = false) String tags,
                                                 @RequestParam(value = "email", required = false) String email,
                                                 @RequestParam(value = "address", required = false) String address){
        restaurantService.updateRestaurant(restaurantId, phone, address, email, tags);
        return new RedirectView("/restaurant-details/" + restaurantId);
    }

    @GetMapping("/sign-out")
    public RedirectView logout(){
        sessionService.logOut();
        return new RedirectView("/");
    }

    @PostMapping("/sign-in")
    public RedirectView signIn(@RequestParam("userId") String userId){
        sessionService.setCurrentUser(userId);
        return new RedirectView("/");
    }
}
