package pl.dskimina.foodsy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.OrderService;
import pl.dskimina.foodsy.service.RestaurantService;

import java.util.List;

@Controller
public class OrderController {
    private final RestaurantService restaurantService;
    private final OrderService orderService;

    public OrderController(RestaurantService restaurantService, OrderService orderService) {
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    @GetMapping("/order-restaurant")
    public String orderRestaurant(Model model) {
        model.addAttribute("restaurantList", restaurantService.getRestaurants());
        return "order-restaurant";
    }

    @GetMapping("/create-order/{restaurantId}")
    public RedirectView createOrder(@PathVariable String restaurantId){
        OrderData order = orderService.createOrder(restaurantId);
        return new RedirectView("/order-menu/" + restaurantId + "/" + order.getOrderId());
    }

    @GetMapping("/order-menu/{restaurantId}/{orderId}")
    public String orderMenu(@PathVariable("restaurantId") String restaurantId,
                            @PathVariable("orderId") String orderId, Model model) {
        RestaurantData restaurant = restaurantService.getRestaurantByRestaurantId(restaurantId);
        OrderData order = orderService.getOrderByOrderId(orderId);
        List<MenuItemData> menuItemForRestaurant = restaurant.getMenuItems();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItemList", menuItemForRestaurant);
        model.addAttribute("order", order);
        model.addAttribute("menuItemListForOrder", order.getMenuItemList());
        return "order-menu";
    }

    @PostMapping("/order-menu/add-menu-item-to-order/{restaurantId}/{orderId}")
    public RedirectView addOrder(@PathVariable("restaurantId") String restaurantId,
                                 @PathVariable("orderId") String orderId,
                                 @RequestParam("menuItemId") String menuItemId,
                                 @RequestParam(value = "description", required = false) String description,
                                 @RequestParam(value = "price", required = false) String price) {
        orderService.addItemToOrder(orderId, menuItemId);

        return new RedirectView("/order-menu/" + restaurantId + "/" + orderId);
    }
}