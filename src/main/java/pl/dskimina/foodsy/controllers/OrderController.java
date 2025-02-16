package pl.dskimina.foodsy.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
    private final RestaurantService restaurantService;

    public OrderController(OrderService orderService,
                           OrderItemService orderItemService, UserService userService, SessionService sessionService, RestaurantService restaurantService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.restaurantService = restaurantService;
    }

    Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @ModelAttribute
    public void fillModel(Model modelMap){
        modelMap.addAttribute("users", userService.getUsers());
        modelMap.addAttribute("currentUser", sessionService.getCurrentUser());
        modelMap.addAttribute("isActiveOrders", true);
    }

    @GetMapping("/orders/restaurants")
    public String getRestaurants(Model model){
        model.addAttribute("restaurantList", restaurantService.getRestaurants());
        return "orders-restaurant-list";
    }

    @PostMapping("/orders")
    public RedirectView createOrder(@RequestParam("restaurantId") String restaurantId, @RequestParam("closingDate") String closingDateString,
                                    @RequestParam("minValue") String minValue, @RequestParam("description") String description) {

        String userId = sessionService.getCurrentUser().getUserId();
        OrderData order = orderService.createOrder(restaurantId, userId, closingDateString, minValue, description);
        return new RedirectView("/orders/" + order.getOrderId() + "/orderItems");
    }

    @GetMapping({"/orders", "/"})
    public String orders(Model model) {
        List<OrderData> orderList = orderService.getOrders();
        model.addAttribute("orderList", orderList);
        return "orders";
    }

    @GetMapping("/orders/new/{restaurantId}")
    public String newOrder(@PathVariable String restaurantId, Model model){
        model.addAttribute("restaurantId", restaurantId);
        return "create-order";
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

    @GetMapping("/orders/{orderId}/orderItems")
    public String getOrderItems(@PathVariable("orderId") String orderId, Model model) {
        OrderData order = orderService.getOrderByOrderId(orderId);
        RestaurantData restaurant = order.getRestaurantData();
        List<MenuItemData> menuItemForRestaurant = restaurant.getMenuItems();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItemList", menuItemForRestaurant);
        model.addAttribute("order", order);
        return "order-menu";
    }

    @PostMapping("/orders/{orderId}/orderItems")
    public RedirectView createOrderItem(@PathVariable String orderId,
                                        @RequestParam("userId") String userId,
                                        @RequestParam("menuItemId") String menuItemId,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam(value = "price", required = false) String price) {
        orderItemService.createOrderItem(userId, menuItemId, price, orderId, description);

        return new RedirectView("/orders/" + orderId + "/orderItems" );
    }

    @DeleteMapping("/orders/{orderItemId}/orderItems")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable String orderItemId){
        if(orderItemService.deleteOrderItem(orderItemId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}