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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.*;

import java.io.IOException;


@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final UserService userService;
    private final SessionService sessionService;
    private final MenuItemService menuItemService;

    public RestaurantController(RestaurantService restaurantService,
                                UserService userService, SessionService sessionService, MenuItemService menuItemService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.menuItemService = menuItemService;
    }


    @ModelAttribute
    public void fillModel(Model modelMap){
        modelMap.addAttribute("users", userService.getUsers());
        modelMap.addAttribute("currentUser", sessionService.getCurrentUser());
        modelMap.addAttribute("isActiveRestaurants", true);
    }

    @GetMapping
    public String addRestaurantView(Model model){
        model.addAttribute("restaurantList", restaurantService.getRestaurants());
        return "restaurant";
    }

    @PostMapping
    public RedirectView addRestaurant(@RequestParam("name") String name,
                                      @RequestParam("phone") String phone,
                                      @RequestParam("email") String email,
                                      @RequestParam("address") String address,
                                      @RequestParam("tags") String tags,
                                      @RequestParam("image") MultipartFile image) throws IOException {
        if(image == null){
            LOG.error("image is null");
            return new RedirectView("/restaurants");
        }
        restaurantService.addRestaurant(name, phone,email, address, tags, image.getBytes());
        LOG.info("restaurant added");
        return new RedirectView("/restaurants");
    }

    @GetMapping("/{restaurantId}")
    public String restaurantDetailsView(Model model, @PathVariable String restaurantId) {
        RestaurantData restaurant = restaurantService.getRestaurantByRestaurantId(restaurantId);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("restaurantMenuItemList", restaurant.getMenuItems());
        return "restaurant-details";
    }

    @GetMapping("/{restaurantId}/logo")
    public ResponseEntity<byte[]> getLogoForRestaurant(@PathVariable String restaurantId){
        byte[] restaurantLogoBytes = restaurantService.getImageForRestaurantId(restaurantId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(restaurantLogoBytes, headers, HttpStatus.OK);
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<String> updateRestaurant(@PathVariable String restaurantId, @RequestBody RestaurantData restaurant) {
        if(restaurantId.equals(restaurant.getRestaurantId())){
            restaurantService.updateRestaurant(restaurant.getRestaurantId(), restaurant);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Restaurant updated successfully");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Restaurant not found");
    }

    @PutMapping("/{restaurantId}/logo")
    public ResponseEntity<String> updateRestaurantLogo(@PathVariable String restaurantId, @RequestBody MultipartFile image) throws IOException {
        restaurantService.updateRestaurantLogo(restaurantId, image);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Restaurant logo updated successfully");
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable String restaurantId) {
        restaurantService.deleteRestaurantByRestaurantId(restaurantId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Restaurant deleted successfully");
    }

    @PostMapping("/{restaurantId}/menuItems")
    public RedirectView addMenuItem(@RequestParam("name") String name,
                                    @RequestParam("price") double price,
                                    @RequestParam("description") String description,
                                    @RequestParam("category") String category,
                                    @PathVariable String restaurantId) {
        if(!name.isEmpty()){
            if(description.isEmpty()) description = "";
            menuItemService.addMenuItem(name, category, description, price, restaurantId);
        }
        return new RedirectView("/restaurants/" + restaurantId);
    }

    @DeleteMapping("/{restaurantId}/menuItems/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String restaurantId, @PathVariable String menuItemId) {
        menuItemService.deleteMenuItemByMenuItemIdAndRestaurantId(restaurantId, menuItemId);
        return ResponseEntity.ok().build();
    }

    Logger LOG = LoggerFactory.getLogger(RestaurantController.class);

    @PutMapping("/{restaurantId}/menuItems/{menuItemId}")
    public ResponseEntity<Void> updateMenuItem(@PathVariable String restaurantId, @PathVariable String menuItemId,
                                       @RequestBody MenuItemData menuItemData) {
        if(menuItemData.getDescription() == null){LOG.debug("menuItemData description is null!");}
        menuItemService.updateMenuItem(restaurantId, menuItemId, menuItemData.getName(), menuItemData.getDescription(), menuItemData.getPrice());
        return ResponseEntity.ok().build();
    }

}
