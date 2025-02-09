package pl.dskimina.foodsy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.*;

import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final SessionService sessionService;

    public OrderController(OrderService orderService,
                           OrderItemService orderItemService, UserService userService, SessionService sessionService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @ModelAttribute
    public void fillModel(Model modelMap){
        modelMap.addAttribute("users", userService.getUsers());
        modelMap.addAttribute("currentUser", sessionService.getCurrentUser());
        modelMap.addAttribute("isActiveOrders", true);
    }

    @GetMapping("/create-order/{restaurantId}/{userId}")
    public RedirectView createOrder(@PathVariable String restaurantId, @PathVariable String userId) {
        OrderData order = orderService.createOrder(restaurantId, userId);
        return new RedirectView("/orders/" + order.getOrderId() + "/items");
    }

    @GetMapping({"/orders", "/"})
    public String orders(Model model) {
        List<OrderData> orderList = orderService.getOrders();
        model.addAttribute("orderList", orderList);
        return "orders";
    }

    @GetMapping("/orders/{orderId}/items")
    public String orderMenu(@PathVariable("orderId") String orderId, Model model) {
        OrderData order = orderService.getOrderByOrderId(orderId);
        RestaurantData restaurant = order.getRestaurantData();
        List<MenuItemData> menuItemForRestaurant = restaurant.getMenuItems();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItemList", menuItemForRestaurant);
        model.addAttribute("order", order);
        return "order-menu";
    }

    @PostMapping("/orders/{orderId}/addMenuItem")
    public RedirectView addOrder(@PathVariable("orderId") String orderId,
                                 @RequestParam("userId") String userId,
                                 @RequestParam("menuItemId") String menuItemId,
                                 @RequestParam(value = "description", required = false) String description,
                                 @RequestParam(value = "price", required = false) String price) {
        orderItemService.createOrderItem(userId, menuItemId, price, orderId, description);

        return new RedirectView("/orders/" + orderId + "/items");
    }

    @GetMapping("/delete-order-item/{orderId}/{orderItemId}")
    public RedirectView deleteOrderItem(@PathVariable String orderId, @PathVariable String orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return new RedirectView("/orders/" + orderId + "/items");
    }

    @GetMapping("/orders/{orderId}/summary")
    public String orderSummary(@PathVariable String orderId, Model model) {
        OrderData order = orderService.getOrderByOrderId(orderId);
        RestaurantData restaurant = order.getRestaurantData();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("order", order);
        return "order-summary";
    }

    @PostMapping("/orders/{orderId}/summary")
    public RedirectView setFinalValueAndCloseOrder(@PathVariable String orderId,
                                                   @RequestParam(value = "cashDiscount", required = false) String cashDiscount,
                                                   @RequestParam(value = "percentageDiscount", required = false) String percentageDiscount,
                                                   @RequestParam(value = "extraPayment", required = false) String extraPayment,
                                                   RedirectAttributes ra) {
        if(extraPayment != null) {orderService.addExtraPayment(orderId, extraPayment);}
        if(cashDiscount != null) {orderService.addCashDiscount(orderId, cashDiscount);}
        if(percentageDiscount != null) {orderService.addPercentageDiscount(orderId, percentageDiscount);}
        orderService.closeOrder(orderId);
        ra.addFlashAttribute("order", orderService.getOrderByOrderId(orderId));
        return new RedirectView("/orders/" + orderId + "/summary");
    }

}