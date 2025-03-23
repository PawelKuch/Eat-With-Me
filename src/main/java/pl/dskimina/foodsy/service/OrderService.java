package pl.dskimina.foodsy.service;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.*;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.repository.*;

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
    private final ExtraPaymentRepository extraPaymentRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public OrderService(OrderRepository orderRepository, ToDataService toDataService,
                        RestaurantService restaurantService, UserService userService,
                        UserOrderPaymentRepository userOrderPaymentRepository,
                        UserOrderPaymentService userOrderPaymentService, ExtraPaymentRepository extraPaymentRepository,
                        UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.orderRepository = orderRepository;
        this.toDataService = toDataService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.userOrderPaymentRepository = userOrderPaymentRepository;
        this.userOrderPaymentService = userOrderPaymentService;
        this.extraPaymentRepository = extraPaymentRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;

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
        User user = userRepository.findByUserId(userId);
        order.setOrderId(UUID.randomUUID().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime closingDate = LocalDateTime.parse(closingDateString, formatter);
        order.setClosingDate(closingDate);
        order.setDescription(description);
        Double minValue = Double.parseDouble(minValueString);
        order.setMinValue(minValue);
        order.setValue(0.0);
        order.setNetValue(0.0);
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
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
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
    public void addExtraPayment(String orderId, String newExtraPaymentString){
        Order order = orderRepository.findByOrderId(orderId);
        double currentExtraPayment = order.getExtraPaymentValue();
        double currentOrderValue = order.getValue();
        double newExtraPayment = Math.round(Double.parseDouble(newExtraPaymentString) * 100.0) / 100.0;

        order.setExtraPaymentValue(newExtraPayment);
        order.setValue(currentOrderValue - currentExtraPayment + newExtraPayment);
        orderRepository.save(order);

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        int howManyUsers = orderRepository.getUsersAmountForOrder(orderId);
        userOrderPayments.forEach(data -> {
            double newExtraPaymentForUser = newExtraPayment / howManyUsers;
            double currentAmountToPay = data.getAmountToPay();
            double currentExtraPaymentForUser = data.getExtraPaymentValue();
            data.setExtraPaymentValue(newExtraPaymentForUser);
            data.setAmountToPay(currentAmountToPay - currentExtraPaymentForUser + newExtraPaymentForUser);
        });
        userOrderPaymentRepository.saveAll(userOrderPayments);

    }

    @Transactional
    public void addPercentageDiscount(String orderId, String newPercentageDiscountString){
        Order order = orderRepository.findByOrderId(orderId);

        double newPercentageDiscount = Double.parseDouble(newPercentageDiscountString);

        if(order.getPercentageDiscount() != newPercentageDiscount) {
            double newPercentageDiscountDouble = Math.round((newPercentageDiscount / 100) * 100.0) / 100.0;

            double currentPercentageDiscountForOrder = order.getPercentageDiscount();
            double currentPercentageDiscountInCash = order.getPercentageDiscountCashValue();

            LOG.warn("currentPercentageDiscountForOrder: {}", currentPercentageDiscountForOrder);
            LOG.warn("currentPercentageDiscountInCash: {}", currentPercentageDiscountInCash);
            double currentOrderValue = order.getValue();
            LOG.warn("currentOrderValue: {}", currentOrderValue);
            double currentExtraPaymentValue = order.getExtraPaymentValue();

            double baseOrderValue = currentOrderValue + currentPercentageDiscountInCash - currentExtraPaymentValue;
            LOG.warn("currentOrderValue: {}", currentOrderValue);
            LOG.warn("currentPercentageDiscountInCash: {}", currentPercentageDiscountInCash);
            LOG.warn("baseOrderValue: {}", baseOrderValue);
            double newPercentageDiscountInCashForOrder = baseOrderValue * newPercentageDiscountDouble;
            order.setPercentageDiscount(newPercentageDiscount);
            LOG.warn("newPercentageDiscount: {}", newPercentageDiscount);
            order.setPercentageDiscountCashValue(newPercentageDiscountInCashForOrder);
            LOG.warn("newPercentageDiscountCashValue: {}", newPercentageDiscountInCashForOrder);
            order.setValue(currentOrderValue + currentPercentageDiscountInCash - newPercentageDiscountInCashForOrder);
            LOG.warn("newOrderValue {}",currentOrderValue + currentPercentageDiscountInCash - newPercentageDiscountInCashForOrder);
            LOG.warn("____ currentOrderValue: " + currentOrderValue + " currentPercentageDiscountInCash: " + currentPercentageDiscountInCash + " newPercentageDiscountInCashForOrder: " + newPercentageDiscountInCashForOrder);
            orderRepository.save(order);

            List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);

            userOrderPayments.forEach(data -> {
                double currentPercentageDiscountInCashForUser = data.getDiscountInPercentageInCash();
                double currentAmountToPay = data.getAmountToPay();
                double currentAmountToPayWithoutExtraPayment = data.getAmountToPayWithoutExtraPayment();
                double currentGeneralDiscountValue = data.getGeneralDiscountValue();
                double baseForPercentageDiscount = (data.getBaseForPercentageDiscount() != 0) ? data.getBaseForPercentageDiscount() : data.getMenuItemsValue(); //nowododane
                double newPercentageDiscountInCashForUser = baseForPercentageDiscount * newPercentageDiscountDouble;

                data.setDiscountInPercentage(newPercentageDiscount);
                data.setDiscountInPercentageInCash(newPercentageDiscountInCashForUser);
                data.setAmountToPayWithoutExtraPayment(currentAmountToPayWithoutExtraPayment + currentPercentageDiscountInCashForUser - newPercentageDiscountInCashForUser);
                data.setAmountToPay(currentAmountToPay + currentPercentageDiscountInCashForUser - newPercentageDiscountInCashForUser);
                data.setGeneralDiscountValue(currentGeneralDiscountValue - currentPercentageDiscountInCashForUser + newPercentageDiscountInCashForUser);
            });

            userOrderPaymentRepository.saveAll(userOrderPayments);
        }
    }

    public void updatePercentageDiscount(Order order, String newPercentageDiscountString){

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(order.getOrderId());
        userOrderPayments.forEach(data -> {
            userOrderPaymentService.updatePercentageDiscountInUserOrderPaymentForUser(data, newPercentageDiscountString);
        });

    }

    @Transactional
    public void addCashDiscount(String orderId, String newCashDiscountString) {
        Order order = orderRepository.findByOrderId(orderId);
        double newCashDiscount = Double.parseDouble(newCashDiscountString);
        LOG.warn("order.getCashDiscount in addCashDiscount: {}", order.getCashDiscount());
        LOG.warn("newCashDiscount in addCashDiscount: {}", newCashDiscount);
        if (order.getCashDiscount() != newCashDiscount) {
            double currentCashDiscountForOrder = order.getCashDiscount();
            double currentOrderValue = order.getValue();
            order.setCashDiscount(newCashDiscount);
            order.setValue(currentOrderValue + currentCashDiscountForOrder - newCashDiscount);

            orderRepository.save(order);

            List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
            userOrderPayments.forEach(data -> {
                double currentCashDiscountForUser = data.getDiscountValueInCash();
                double currentAmountToPayWithoutExtraPayment = data.getAmountToPayWithoutExtraPayment();
                double currentAmountToPay = data.getAmountToPay();
                double currentGeneralDiscountValue = data.getGeneralDiscountValue();

                double newCashDiscountForUser = (data.getMenuItemsValue() / order.getNetValue()) * newCashDiscount;
                LOG.warn("data.getMenuItemsValue: {}", data.getMenuItemsValue());
                LOG.warn("order.getNetValue: {}", order.getNetValue());
                LOG.warn("newCashDiscountForUser: {}", newCashDiscountForUser);
                data.setDiscountValueInCash(newCashDiscountForUser);
                data.setAmountToPay(currentAmountToPay + currentCashDiscountForUser - newCashDiscountForUser);
                data.setAmountToPayWithoutExtraPayment(currentAmountToPayWithoutExtraPayment + currentCashDiscountForUser - newCashDiscountForUser);
                data.setBaseForPercentageDiscount(data.getAmountToPayWithoutExtraPayment());
                LOG.warn("baseForPercentageDiscount in addCashDiscount method: {}", data.getBaseForPercentageDiscount());
                data.setGeneralDiscountValue(currentGeneralDiscountValue - currentCashDiscountForUser + newCashDiscountForUser);
            });
            userOrderPaymentRepository.saveAll(userOrderPayments);
        }
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
