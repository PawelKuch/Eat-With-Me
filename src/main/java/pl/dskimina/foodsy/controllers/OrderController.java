package pl.dskimina.foodsy.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.data.MenuItemData;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.service.*;
import java.util.List;


@Controller
@RequestMapping({"/orders", "/"})
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final SessionService sessionService;
    private final RestaurantService restaurantService;
    private final UserInfoService discountAndExtraPaymentService;


    public OrderController(OrderService orderService,
                           OrderItemService orderItemService, UserService userService, SessionService sessionService,
                           RestaurantService restaurantService, UserInfoService discountAndExtraPaymentService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.restaurantService = restaurantService;
        this.discountAndExtraPaymentService = discountAndExtraPaymentService;
    }

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

    @PostMapping
    public RedirectView createOrder(@RequestParam("restaurantId") String restaurantId, @RequestParam("closingDate") String closingDateString,
                                    @RequestParam("minValue") String minValue, @RequestParam("description") String description) {
        String userId = sessionService.getCurrentUser().getUserId();
        OrderData order = orderService.createOrder(restaurantId, userId, closingDateString, minValue, description);
        return new RedirectView("/orders/" + order.getOrderId() + "/orderItems");
    }

    @GetMapping
    public String orders(Model model) {
        List<OrderData> orderList = orderService.getOrders();
        model.addAttribute("orderList", orderList);
        return "orders";
    }

    @GetMapping("/new/{restaurantId}")
    public String newOrder(@PathVariable String restaurantId, Model model){
        model.addAttribute("restaurantId", restaurantId);
        return "create-order";
    }

    @GetMapping("/{orderId}/summary")
    public String orderSummary(@PathVariable String orderId, Model model) {
        OrderData order = orderService.getOrderByOrderId(orderId);
        RestaurantData restaurant = order.getRestaurantData();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("order", order);
        model.addAttribute("userInfoList", discountAndExtraPaymentService.getUserInfoListForOrder(orderId));
        model.addAttribute("userAmountForOrder", orderService.getUsersAmountForOrder(orderId));
        return "order-summary";
    }

    @PostMapping("/{orderId}/summary")
        public RedirectView setFinalValueOfOrder(@PathVariable String orderId,
                                                       @RequestParam(value = "cashDiscount", required = false) String cashDiscount,
                                                       @RequestParam(value = "percentageDiscount", required = false) String percentageDiscount,
                                                       @RequestParam(value = "extraPayment", required = false) String extraPaymentPrice) {
        orderService.addExtraPayment(orderId, extraPaymentPrice);
        orderService.addCashDiscount(orderId, cashDiscount);
        orderService.addPercentageDiscount(orderId, percentageDiscount);
        return new RedirectView("/orders/" + orderId + "/summary");
        }

    @GetMapping("/{orderId}/orderItems")
    public String getOrderItems(@PathVariable("orderId") String orderId, Model model) {
        OrderData order = orderService.getOrderByOrderId(orderId);
        RestaurantData restaurant = order.getRestaurantData();
        List<MenuItemData> menuItemForRestaurant = restaurant.getMenuItems();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItemList", menuItemForRestaurant);
        model.addAttribute("order", order);
        return "order-menu";
    }

    @PostMapping("/{orderId}/orderItems")
    public RedirectView createOrderItem(@PathVariable String orderId,
                                        @RequestParam("userId") String userId,
                                        @RequestParam("menuItemId") String menuItemId,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam(value = "price", required = false) String price) {
        orderItemService.createOrderItem(userId, menuItemId, price, orderId, description);
        return new RedirectView("/orders/" + orderId + "/orderItems" );
    }

    @DeleteMapping("/{orderItemId}/orderItems")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable String orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.ok().build();
    }

}