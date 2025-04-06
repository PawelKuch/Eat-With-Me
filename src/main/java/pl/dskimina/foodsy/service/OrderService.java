package pl.dskimina.foodsy.service;

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
    private final static Logger LOG = LoggerFactory.getLogger(OrderService.class);
    private final ToDataService toDataService;
    private final OrderRepository orderRepository;
    private final UserOrderPaymentRepository userOrderPaymentRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public OrderService(OrderRepository orderRepository, ToDataService toDataService,
                        UserOrderPaymentRepository userOrderPaymentRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.orderRepository = orderRepository;
        this.toDataService = toDataService;
        this.userOrderPaymentRepository = userOrderPaymentRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;

    }

    @Transactional
    public OrderData getOrderByOrderId(String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        return toDataService.convert(order);
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
        order.setValue(Math.round((currentOrderValue - currentExtraPayment + newExtraPayment) * 100.0) / 100.0);
        orderRepository.save(order);

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        int howManyUsers = orderRepository.getUsersAmountForOrder(orderId);
        userOrderPayments.forEach(data -> {
            double newExtraPaymentForUser = Math.round((newExtraPayment / howManyUsers) * 100.0) / 100.0;
            double currentAmountToPay = Math.round(data.getAmountToPay() * 100.0) / 100.0;
            double currentExtraPaymentForUser = Math.round(data.getExtraPaymentValue() * 100.0) / 100.0;
            data.setExtraPaymentValue(Math.round(newExtraPaymentForUser * 100.0) / 100.0);
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
            double currentPercentageDiscountInCash = Math.round(order.getPercentageDiscountCashValue() * 100.0) / 100.0;
            double currentOrderValue = order.getValue();
            double currentExtraPaymentValue = order.getExtraPaymentValue();
            double baseOrderValue = currentOrderValue + currentPercentageDiscountInCash - currentExtraPaymentValue;
            double newPercentageDiscountInCashForOrder = Math.round((baseOrderValue * newPercentageDiscountDouble) * 100.0) / 100.0;
            order.setPercentageDiscount(newPercentageDiscount);
            LOG.warn("newPercentageDiscount: {}", newPercentageDiscount);
            order.setPercentageDiscountCashValue(newPercentageDiscountInCashForOrder);
            order.setValue(currentOrderValue + currentPercentageDiscountInCash - newPercentageDiscountInCashForOrder);
            orderRepository.save(order);

            List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);

            userOrderPayments.forEach(data -> {
                double currentPercentageDiscountInCashForUser = Math.round(data.getDiscountInPercentageInCash()* 100.0) / 100.0;
                double currentAmountToPay = data.getAmountToPay();
                double currentAmountToPayWithoutExtraPayment = data.getAmountToPayWithoutExtraPayment();
                double currentGeneralDiscountValue = data.getGeneralDiscountValue();
                double baseForPercentageDiscount = (data.getBaseForPercentageDiscount() != 0) ? data.getBaseForPercentageDiscount() : data.getMenuItemsValue();
                double newPercentageDiscountInCashForUser = baseForPercentageDiscount * newPercentageDiscountDouble;
                double amountToPayWithoutExtraPayment = Math.round((currentAmountToPayWithoutExtraPayment + currentPercentageDiscountInCashForUser - newPercentageDiscountInCashForUser) * 100.0) / 100.0;
                double amountToPay = Math.round((currentAmountToPay + currentPercentageDiscountInCashForUser - newPercentageDiscountInCashForUser) * 100.0) / 100.0;
                double generalDiscountValue = Math.round((currentGeneralDiscountValue - currentPercentageDiscountInCashForUser + newPercentageDiscountInCashForUser) * 100.0) / 100.0;

                data.setDiscountInPercentage(newPercentageDiscount);
                data.setDiscountInPercentageInCash(Math.round(newPercentageDiscountInCashForUser * 100.0) / 100.0);
                data.setAmountToPayWithoutExtraPayment(amountToPayWithoutExtraPayment);
                data.setAmountToPay(amountToPay);
                data.setGeneralDiscountValue(generalDiscountValue);
            });

            userOrderPaymentRepository.saveAll(userOrderPayments);
        }
    }

    @Transactional
    public void addCashDiscount(String orderId, String newCashDiscountString) {
        Order order = orderRepository.findByOrderId(orderId);
        double newCashDiscount = Double.parseDouble(newCashDiscountString);
        if (order.getCashDiscount() != newCashDiscount) {
            double currentCashDiscountForOrder = order.getCashDiscount();
            double currentOrderValue = order.getValue();
            order.setCashDiscount(newCashDiscount);
            order.setValue(Math.round((currentOrderValue + currentCashDiscountForOrder - newCashDiscount) * 100.0) / 100.0);

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
                data.setDiscountValueInCash(Math.round(newCashDiscountForUser * 100.0) / 100.0);
                double amountToPay = Math.round((currentAmountToPay + currentCashDiscountForUser - newCashDiscountForUser) * 100.0) / 100.0;
                data.setAmountToPay(amountToPay);
                double amountToPayWithoutExtraPayment = Math.round((currentAmountToPayWithoutExtraPayment + currentCashDiscountForUser - newCashDiscountForUser) * 100.0) / 100.0;
                data.setAmountToPayWithoutExtraPayment(amountToPayWithoutExtraPayment);
                data.setBaseForPercentageDiscount(data.getAmountToPayWithoutExtraPayment());
                LOG.warn("baseForPercentageDiscount in addCashDiscount method: {}", data.getBaseForPercentageDiscount());
                data.setGeneralDiscountValue(currentGeneralDiscountValue - currentCashDiscountForUser + newCashDiscountForUser);
            });
            userOrderPaymentRepository.saveAll(userOrderPayments);
        }
    }

    @Transactional
    public int getUsersAmountForOrder(String orderId){
        return orderRepository.getUsersAmountForOrder(orderId);
    }

}
