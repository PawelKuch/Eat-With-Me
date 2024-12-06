package pl.dskimina.foodsy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.service.MenuItemService;

@Controller
public class MenuItemController {
    MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/add-menuItem")
    public String addMenuItemView(){
        return "add-menuItem";
    }

    @PostMapping("/add-menuItem")
    public RedirectView addMenuItem(@RequestParam("name") String name,
                                    @RequestParam("price") double price,
                                    @RequestParam("description") String description){
        if(!name.isEmpty() && price > 0){
            if(description.isEmpty()) description = "";
            menuItemService.addMenuItem(name, description, price);
        }

        return new RedirectView("/");
    }

}
