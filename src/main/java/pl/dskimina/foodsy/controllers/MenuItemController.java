package pl.dskimina.foodsy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.MenuItemService;
import pl.dskimina.foodsy.service.RestaurantService;
import pl.dskimina.foodsy.service.ToDataService;

import java.util.List;
;

@Controller
public class MenuItemController {
    private static final Logger LOG = LoggerFactory.getLogger(MenuItemController.class.getName());
    private final ToDataService toDataService;
    private final RestaurantService restaurantService;
    MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService, ToDataService toDataService, RestaurantService restaurantService) {
        this.menuItemService = menuItemService;
        this.toDataService = toDataService;
        this.restaurantService = restaurantService;
    }

    @GetMapping("/menu-item")
    public String addMenuItemView(Model model){
        List<MenuItemData> menuItemDataList = menuItemService.getMenuItems();
        List<RestaurantData> restaurantDataList = restaurantService.getRestaurants();
        model.addAttribute("menuItemDataList", menuItemDataList);
        model.addAttribute("restaurantDataList", restaurantDataList);
        return "menuItem";
    }

    @PostMapping("/menu-item")
    public RedirectView addMenuItem(@RequestParam("name") String name,
                                    @RequestParam("price") double price,
                                    @RequestParam("description") String description,
                                    @RequestParam("category") String category,
                                    @RequestParam("restaurantId") String restaurantId){

        if(!name.isEmpty()){
            if(description.isEmpty()) description = "";
            menuItemService.addMenuItem(name, category, description, price, restaurantId);
            LOG.info("Item has been created");
        }
        return new RedirectView("/menu-item");
    }

    @GetMapping("/item-details/{id}")
    public String itemDetailsView(@PathVariable String id, Model model){
        MenuItemData itemData = menuItemService.getMenuItemByMenuItemId(id);
        LOG.info("item details");
        model.addAttribute("item", itemData);
        return "item-details";
    }
}
