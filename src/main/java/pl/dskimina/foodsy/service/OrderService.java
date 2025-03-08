package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.*;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.entity.data.UserOrderPaymentData;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.UserOrderPaymentRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final ToDataService toDataService;
    private final OrderRepository orderRepository;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final UserOrderPaymentRepository userOrderPaymentRepository;
    private final UserOrderPaymentService userOrderPaymentService;

    public OrderService(OrderRepository orderRepository, ToDataService toDataService,
                        RestaurantService restaurantService, UserService userService,
                        UserOrderPaymentRepository userOrderPaymentRepository, UserOrderPaymentService userOrderPaymentService) {
        this.orderRepository = orderRepository;
        this.toDataService = toDataService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.userOrderPaymentRepository = userOrderPaymentRepository;
        this.userOrderPaymentService = userOrderPaymentService;
    }

    Logger LOG = LoggerFactory.getLogger(OrderService.class);
    @Transactional
    public OrderData getOrderByOrderId(String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        return toDataService.convert(order);
    }

    @Transactional Order findOrderByOrderId(String orderId){
        return orderRepository.findByOrderId(orderId);
    }

    @Transactional
    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    @Transactional
    public OrderData createOrder(String restaurantId, String userId, String closingDateString, String minValueString, String description) {
        Order order = new Order();
        User user = userService.getUserInstanceById(userId);
        order.setOrderId(UUID.randomUUID().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime closingDate = LocalDateTime.parse(closingDateString, formatter);
        order.setClosingDate(closingDate);
        order.setDescription(description);
        Double minValue = Double.parseDouble(minValueString);
        order.setMinValue(minValue);
        order.setValue(0.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(0.0);
        order.setPercentageDiscountCashValue(0.0);
        List<OrderItem> orderItems = new ArrayList<>();
        List<ExtraPayment> extraPayments = new ArrayList<>();
        List<Discount> discounts = new ArrayList<>();
        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        order.setUserOrderPayments(userOrderPayments);
        order.setOrderItemList(orderItems);
        order.setExtraPayments(extraPayments);
        order.setDiscounts(discounts);
        order.setOwner(user);
        Restaurant restaurant = restaurantService.getRestaurantEntityByRestaurantId(restaurantId);
        order.setRestaurant(restaurant);
        order.setIsClosed(false);
        orderRepository.save(order);
        return toDataService.convert(order);
    }

    @Transactional
    public List<OrderData> getOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(toDataService::convert).toList();
    }

    @Transactional
    public void addPercentageDiscount(String orderId, String percentageDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        Double extraPaymentValue = userOrderPaymentRepository.getExtraPaymentValueForOrder(orderId);
        double currentBaseOrderValue = Math.round((order.getValue() - extraPaymentValue) * 100.0) / 100.0;
        double percentageDiscount = Double.parseDouble(percentageDiscountString);
        double percentageDiscountDouble = Math.round((percentageDiscount / 100.0) * 100.0) / 100.0;
        double discountValue = Math.round((percentageDiscountDouble * currentBaseOrderValue)*100.0) / 100.0;
        order.setPercentageDiscountCashValue(discountValue);
        Double newOrderValue = (currentBaseOrderValue - discountValue) + extraPaymentValue;
        order.setValue(newOrderValue);
        order.setPercentageDiscount(percentageDiscount);
        orderRepository.save(order);

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        userOrderPayments.forEach(data -> {
            data.setDiscountInPercentage(percentageDiscountDouble);
            double baseForPercentageDiscount = Math.round((data.getAmountToPay() - data.getExtraPaymentValue()) * 100.0) / 100.0;
            Double percentageDiscountValue = Math.round((baseForPercentageDiscount * percentageDiscountDouble)*100.0) / 100.0;

            data.setBaseForPercentageDiscount(baseForPercentageDiscount);
            data.setDiscountInPercentageInCash(percentageDiscountValue);
            data.setGeneralDiscountValue(data.getGeneralDiscountValue() + percentageDiscountValue);
            double amountToPay = Math.round((data.getAmountToPay() - percentageDiscountValue) * 100.0) / 100.0;
            data.setAmountToPay(amountToPay);
            userOrderPaymentRepository.save(data);
        });
    }

    @Transactional
    public void updatePercentageDiscount(String orderId, String newPercentageDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        Double extraPaymentValue = userOrderPaymentRepository.getExtraPaymentValueForOrder(orderId);
        Double currentPercentageDiscountInCashValue = order.getPercentageDiscountCashValue();
        double currentPercentageDiscount = Math.round((order.getPercentageDiscount() / 100.0) * 100.0) / 100.0;
        double noPercentageDiscountOrderValue = order.getValue() + currentPercentageDiscountInCashValue - extraPaymentValue;

        double currentBaseOrderValue = Math.round((noPercentageDiscountOrderValue - extraPaymentValue) * 100.0) / 100.0;
        double newPercentageDiscount = Double.parseDouble(newPercentageDiscountString);
        double newPercentageDiscountDouble = newPercentageDiscount / 100.0;
        double discountValue = Math.round((newPercentageDiscountDouble * currentBaseOrderValue)*100.0) / 100.0;

        Double newOrderValue = (currentBaseOrderValue - discountValue) + extraPaymentValue;
        order.setValue(newOrderValue);
        order.setPercentageDiscountCashValue(discountValue);
        order.setPercentageDiscount(newPercentageDiscount);
        orderRepository.save(order);

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        userOrderPayments.forEach(data -> userOrderPaymentService.updatePercentageDiscountInUserOrderPaymentForUser(orderId, data.getUser().getUserId(), newPercentageDiscountString));
    }

    @Transactional
    public void addCashDiscount(String orderId, String cashDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        Double orderValue = order.getValue();
        Double cashDiscount = Double.parseDouble(cashDiscountString);
        Double newOrderValue = orderValue - cashDiscount;
        order.setValue(newOrderValue);
        order.setCashDiscount(cashDiscount);
        orderRepository.save(order);

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        int howManyUsersInOrder = userOrderPayments.size();

        Double discountValueForUser = Math.round(cashDiscount / howManyUsersInOrder * 100.0) / 100.0;

        userOrderPayments.forEach(data -> {
            data.setDiscountValueInCash(discountValueForUser);
            data.setAmountToPay(data.getAmountToPay() - data.getDiscountValueInCash());
            data.setGeneralDiscountValue(data.getGeneralDiscountValue() + discountValueForUser);
            userOrderPaymentRepository.save(data);
        });
    }

    @Transactional
    public void updateCashDiscount(String orderId, String newCashDiscountString){
        Order order = orderRepository.findByOrderId(orderId);

        double newCashDiscount = Double.parseDouble(newCashDiscountString);
        double currentCashDiscount = order.getCashDiscount();
        double orderValueWithoutCashDiscount = order.getValue() + currentCashDiscount;
        double newOrderValue = orderValueWithoutCashDiscount - newCashDiscount;
        order.setValue(newOrderValue);

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        int howManyUsersInOrder = userOrderPayments.size();

        userOrderPayments.forEach(data -> {
            double currentCashDiscountForUser = data.getDiscountValueInCash();
            double amountToPayWithoutCurrentCashDiscount = data.getAmountToPay() + currentCashDiscountForUser;
            double newCashDiscountForUser = Math.round((newCashDiscount / howManyUsersInOrder) * 100.0) / 100.0;
            double newAmountToPay = amountToPayWithoutCurrentCashDiscount - newCashDiscountForUser;
            double generalDiscountValueWithoutCurrentCashDiscount = data.getGeneralDiscountValue() - currentCashDiscountForUser;
            double newGeneralDiscountValue = generalDiscountValueWithoutCurrentCashDiscount + newCashDiscountForUser;
            data.setDiscountValueInCash(newCashDiscountForUser);
            data.setGeneralDiscountValue(newGeneralDiscountValue);
            data.setAmountToPay(newAmountToPay);
            userOrderPaymentRepository.save(data);
        });

    }

    @Transactional
    public void addExtraPayment(String orderId, String extraPaymentString){
        Order order = orderRepository.findByOrderId(orderId);
        Double orderValue = order.getValue();
        Double extraPayment = Double.parseDouble(extraPaymentString);
        Double newOrderValue = orderValue + extraPayment;
        order.setValue(newOrderValue);
        orderRepository.save(order);


        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        int howManyUsersInOrder = userOrderPayments.size();
        Double extraPaymentValueForUser = Math.round(extraPayment / howManyUsersInOrder * 100.0) / 100.0;
        userOrderPayments.forEach(data -> {
            data.setExtraPaymentValue(extraPaymentValueForUser);
            data.setAmountToPay(data.getAmountToPay() + extraPaymentValueForUser);
            userOrderPaymentRepository.save(data);
        });
    }

    @Transactional
    public void closeOrder(String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        order.setIsClosed(true);
        orderRepository.save(order);
    }

    @Transactional
    public int getUsersAmountForOrder(String orderId){
        return orderRepository.getUsersAmountForOrder(orderId);
    }

}
