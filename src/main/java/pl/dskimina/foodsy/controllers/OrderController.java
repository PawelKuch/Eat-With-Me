package pl.dskimina.foodsy.controllers;


import org.springframework.http.HttpStatus;
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
import java.util.Map;


@Controller
@RequestMapping({"/orders", "/"})
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final SessionService sessionService;
    private final RestaurantService restaurantService;
    private final ExtraPaymentService extraPaymentService;
    private final UserOrderPaymentService userOrderPaymentService;

    public OrderController(OrderService orderService,
                           OrderItemService orderItemService, UserService userService, SessionService sessionService,
                           RestaurantService restaurantService, ExtraPaymentService extraPaymentService, UserOrderPaymentService userOrderPaymentService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.restaurantService = restaurantService;
        this.extraPaymentService = extraPaymentService;
        this.userOrderPaymentService = userOrderPaymentService;
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
        model.addAttribute("userAmountForOrder", orderService.getUsersAmountForOrder(orderId));
        model.addAttribute("userOrderPaymentList", userOrderPaymentService.getUserOrderPaymentsForOrderId(orderId));

        return "order-summary";
    }

    @PostMapping("/{orderId}/summary")
        public RedirectView setFinalValueOfOrder(@PathVariable String orderId,
                                                       @RequestParam(value = "cashDiscount", required = false) String cashDiscount,
                                                       @RequestParam(value = "percentageDiscount", required = false) String percentageDiscount,
                                                       @RequestParam(value = "isPercentage", required = false) boolean isPercentage,
                                                       @RequestParam(value = "extraPayment", required = false) String extraPaymentPrice,
                                                       @RequestParam(value = "extraPaymentProduct", required = false) String extraPaymentProduct,
                                                       RedirectAttributes ra) {
            if(extraPaymentPrice != null && extraPaymentProduct != null ) {
                    extraPaymentService.createExtraPayment(orderId, extraPaymentProduct, extraPaymentPrice);
                    extraPaymentService.addExtraPaymentToUserOrderPayment(orderId, extraPaymentPrice);
            }
            if(!isPercentage && cashDiscount != null) {
               orderService.addCashDiscount(orderId, cashDiscount);
            }
            if(isPercentage && percentageDiscount != null) {
                orderService.addPercentageDiscount(orderId, percentageDiscount);
            }

            ra.addFlashAttribute("order", orderService.getOrderByOrderId(orderId));
            return new RedirectView("/orders/" + orderId + "/summary");
        }

    @PutMapping("/{orderId}/summary")
    public ResponseEntity<String> updateOrder(@PathVariable String orderId,
                                        @RequestBody(required = false) Map<String, String> requestedData) {

        if(requestedData != null && requestedData.containsKey("newCashDiscountValue")) {
            orderService.updateCashDiscount(orderId, requestedData.get("newCashDiscountValue"));
            return ResponseEntity.ok("Cash discount updated!");
        } else if(requestedData != null && requestedData.containsKey("newPercentageDiscountValue")) {
            orderService.updatePercentageDiscount(orderId, requestedData.get("newPercentageDiscountValue"));
            return ResponseEntity.ok("Percentage discount updated!");
        } else if(requestedData != null && requestedData.containsKey("extraPaymentId") && requestedData.containsKey("newExtraPaymentProduct") && requestedData.containsKey("newExtraPaymentPrice")) {
            String extraPaymentId = requestedData.get("extraPaymentId");
            String extraPaymentProduct = requestedData.get("newExtraPaymentProduct");
            String extraPaymentPrice = requestedData.get("newExtraPaymentPrice");
            if(orderService.updateOrder(orderId, extraPaymentId, extraPaymentProduct, extraPaymentPrice)){
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("Extra payment updated!");
            }
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Request failed!");
    }

    @DeleteMapping("/{orderId}/summary")
    public ResponseEntity<String> deleteExtraPayment(@PathVariable String orderId, @RequestBody Map<String, String> requestedObject) {
        if (requestedObject == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("ExtraPaymentId is null");
        }
        String extraPaymentId = requestedObject.get("extraPaymentId");
        if (extraPaymentService.deleteExtraPayment(orderId, extraPaymentId)){
            return ResponseEntity
                .status(HttpStatus.OK)
                .body("ExtraPayment deleted!");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("ExtraPayment not found!");
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
        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId(orderId, userId);
        return new RedirectView("/orders/" + orderId + "/orderItems" );
    }

    @DeleteMapping("/{orderItemId}/orderItems")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable String orderItemId){
        if(orderItemService.deleteOrderItem(orderItemId)){
            return ResponseEntity.ok().build();
        } else {
            throw new NullPointerException("OrderItem " + orderItemId + " not found");
        }
    }

}