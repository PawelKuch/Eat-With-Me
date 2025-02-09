package pl.dskimina.foodsy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.service.*;


@Controller
public class MenuItemController {
    MenuItemService menuItemService;
    private final SessionService sessionService;
    private final UserService userService;

    public MenuItemController(MenuItemService menuItemService,
                              SessionService sessionService, UserService userService) {
        this.menuItemService = menuItemService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @ModelAttribute
    public void fillModel(Model modelMap){
        modelMap.addAttribute("users", userService.getUsers());
        modelMap.addAttribute("currentUser", sessionService.getCurrentUser());
    }

    @PostMapping("/items")
    public RedirectView addMenuItem(@RequestParam("name") String name,
                                    @RequestParam("price") double price,
                                    @RequestParam("description") String description,
                                    @RequestParam("category") String category,
                                    @RequestParam("restaurantId") String restaurantId){
        if(!name.isEmpty()){
            if(description.isEmpty()) description = "";
            menuItemService.addMenuItem(name, category, description, price, restaurantId);
        }
        return new RedirectView("/restaurants/" + restaurantId);
    }

    @GetMapping("/delete-menuItem/{restaurantId}/{menuItemId}")
    public RedirectView deleteMenuItem(@PathVariable String restaurantId, @PathVariable String menuItemId){
        if(menuItemService.deleteMenuItemByMenuItemId(menuItemId)){
            return new RedirectView("/restaurants/" + restaurantId);
        }
        throw new RuntimeException();
    }

    @PostMapping("/update-menu-item/{restaurantId}/{menuItemId}")
    public RedirectView updateMenuItem(@PathVariable String restaurantId, @PathVariable String menuItemId,
                                       @RequestParam("name") String name, @RequestParam("description") String description,
                                       @RequestParam("price") String price) {
        menuItemService.updateMenuItem(menuItemId, name, description, price);
        return new RedirectView("/restaurants/" + restaurantId);
    }
}
