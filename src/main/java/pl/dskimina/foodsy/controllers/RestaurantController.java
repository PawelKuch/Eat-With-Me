package pl.dskimina.foodsy.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.*;

import java.util.List;

@Controller
public class RestaurantController {

    private final MenuItemService menuItemService;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final SessionService sessionService;

    public RestaurantController(RestaurantService restaurantService, MenuItemService menuItemService,
                                UserService userService, SessionService sessionService) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @ModelAttribute
    public void fillModel(Model modelMap){
        modelMap.addAttribute("users", userService.getUsers());
        modelMap.addAttribute("currentUser", sessionService.getCurrentUser());
        modelMap.addAttribute("isActiveRestaurants", true);
    }

    @GetMapping("/restaurants")
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

    @GetMapping("/restaurants/{restaurantId}")
    public String restaurantDetailsView(Model model, @PathVariable String restaurantId){
        RestaurantData restaurant = restaurantService.getRestaurantByRestaurantId(restaurantId);
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

}
