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
    public void addPercentageDiscount(String orderId, String newPercentageDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        double currentPercentageDiscount = order.getPercentageDiscount();
        double extraPaymentValue = userOrderPaymentRepository.getExtraPaymentValueForOrder(orderId);
        double currentBaseOrderValue = Math.round((order.getValue() - extraPaymentValue) * 100.0) / 100.0;
        double currentPercentageDiscountValueInCash = order.getPercentageDiscountCashValue();

        double newPercentageDiscount = Double.parseDouble(newPercentageDiscountString);
        double newPercentageDiscountDouble = Math.round((newPercentageDiscount / 100.0) * 100.0) / 100.0;

        double newPercentageDiscountValueInCashForNewDiscount = Math.round((newPercentageDiscountDouble * currentBaseOrderValue)*100.0) / 100.0;



        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        if(currentPercentageDiscount < 0.01) {
            order.setPercentageDiscountCashValue(newPercentageDiscountValueInCashForNewDiscount);
            Double newOrderValue = (currentBaseOrderValue - newPercentageDiscountValueInCashForNewDiscount) + extraPaymentValue;
            order.setValue(newOrderValue);
            order.setPercentageDiscount(newPercentageDiscount);
            userOrderPaymentService.addPercentageDiscountInUserOrderPaymentForOrder(userOrderPayments, newPercentageDiscountDouble);
            orderRepository.save(order);
        }else {
            double orderValueWithoutCurrentPercentageDiscount = order.getValue() + currentPercentageDiscountValueInCash;
            double baseOrderValueForUpdatePercentageDiscount = Math.round((orderValueWithoutCurrentPercentageDiscount - extraPaymentValue) * 100.0) / 100.0;
            double newPercentageDiscountValueInCash = Math.round((newPercentageDiscountDouble * baseOrderValueForUpdatePercentageDiscount)*100.0) / 100.0;
            Double newOrderValue = (baseOrderValueForUpdatePercentageDiscount - newPercentageDiscountValueInCash) + extraPaymentValue;
            order.setValue(newOrderValue);
            order.setPercentageDiscountCashValue(newPercentageDiscountValueInCash);
            order.setPercentageDiscount(newPercentageDiscount);
            orderRepository.save(order);

            updatePercentageDiscount(orderId, newPercentageDiscountString);
        }



    }

    @Transactional
    public void updatePercentageDiscount(String orderId, String newPercentageDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        Double extraPaymentValue = userOrderPaymentRepository.getExtraPaymentValueForOrder(orderId);
        Double currentPercentageDiscountInCashValue = order.getPercentageDiscountCashValue();
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
        userOrderPayments.forEach(data -> {
            userOrderPaymentService.updatePercentageDiscountInUserOrderPaymentForUser(data, newPercentageDiscountString);
        });

    }

    @Transactional
    public void addCashDiscount(String orderId, String newCashDiscountString){
        Order order = orderRepository.findByOrderId(orderId);
        Double currentOrderValue = order.getValue();
        Double newCashDiscountForOrder = Double.parseDouble(newCashDiscountString);
        double currentCashDiscountForOrder = order.getCashDiscount();

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);

        if(order.getCashDiscount() > 0.01){
            order.setValue(currentOrderValue + currentCashDiscountForOrder - newCashDiscountForOrder);
            order.setCashDiscount(newCashDiscountForOrder);

            userOrderPayments.forEach(data -> {
                double currentCashDiscountForUser = data.getDiscountValueInCash();
                double newCashDiscountForUser = Math.round(((data.getBaseForPercentageDiscount() / order.getNetValue()) * newCashDiscountForOrder) * 100.0) / 100.0;
                double currentGeneralDiscountValue = data.getGeneralDiscountValue();
                double currentAmountToPay = data.getAmountToPay();
                data.setDiscountValueInCash(newCashDiscountForUser);
                data.setAmountToPayWithoutExtraPayment(data.getAmountToPayWithoutExtraPayment() + currentCashDiscountForUser - newCashDiscountForUser);
                data.setGeneralDiscountValue(currentGeneralDiscountValue - currentCashDiscountForUser + newCashDiscountForUser);
                data.setAmountToPay(currentAmountToPay + currentCashDiscountForUser - newCashDiscountForUser);
            });
        } else {
            Double newOrderValue = currentOrderValue - newCashDiscountForOrder;
            order.setValue(newOrderValue);
            order.setCashDiscount(newCashDiscountForOrder);
            orderRepository.save(order);

            userOrderPayments.forEach(data -> {
                double cashDiscountForUser = Math.round(((data.getBaseForPercentageDiscount() / order.getNetValue()) * newCashDiscountForOrder) * 100.0) / 100.0;
                LOG.warn("netValue: {}", order.getNetValue());
                data.setDiscountValueInCash(cashDiscountForUser);
                data.setAmountToPayWithoutExtraPayment(data.getAmountToPayWithoutExtraPayment() - cashDiscountForUser);
                data.setAmountToPay(data.getAmountToPay() - data.getDiscountValueInCash());
                data.setGeneralDiscountValue(data.getGeneralDiscountValue() + cashDiscountForUser);
            });

            orderRepository.save(order);
            userOrderPaymentRepository.saveAll(userOrderPayments);
        }
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
    public boolean updateOrderWithNewExtraPayment(String orderId, String extraPaymentId, String newExtraPaymentProduct, String newExtraPaymentPriceString) {
        Order order = orderRepository.findByOrderId(orderId);
        ExtraPayment extraPayment = extraPaymentRepository.findByExtraPaymentId(extraPaymentId);
        if(extraPayment == null || order == null) return false;

        double currentExtraPaymentPrice = extraPayment.getPrice();
        extraPayment.setProduct(newExtraPaymentProduct);
        double newExtraPaymentPrice = Math.round(Double.parseDouble(newExtraPaymentPriceString) * 100.0) / 100.0;
        extraPayment.setPrice(newExtraPaymentPrice);
        extraPaymentRepository.save(extraPayment);

        double currentOrderValue = order.getValue();
        double currentOrderValueWithoutGivenExtraPayment = currentOrderValue - currentExtraPaymentPrice;
        double newOrderValue = currentOrderValueWithoutGivenExtraPayment + newExtraPaymentPrice;
        order.setValue(newOrderValue);
        order.setExtraPaymentValue(extraPaymentRepository.getExtraPaymentsValueForOrder(orderId));

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        int howManyUsersInOrder = userOrderPayments.size();
        double currentGivenExtraPaymentForUser = Math.round((currentExtraPaymentPrice / howManyUsersInOrder) * 100.0) / 100.0;

        userOrderPayments.forEach(data -> {
            double currentExtraPaymentForUserWithoutGivenExtraPayment = data.getExtraPaymentValue() - Math.round(currentGivenExtraPaymentForUser * 100.0) / 100.0;
            double newExtraPaymentForUser = currentExtraPaymentForUserWithoutGivenExtraPayment + Math.round((newExtraPaymentPrice / howManyUsersInOrder) * 100.0 ) / 100.0;
            data.setExtraPaymentValue(newExtraPaymentForUser);
            double currentAmountToPayWithoutGivenExtraPayment = data.getAmountToPay() - currentGivenExtraPaymentForUser;
            double newAmountToPay = currentAmountToPayWithoutGivenExtraPayment + newExtraPaymentForUser;
            data.setAmountToPay(newAmountToPay);
        });
        return true;
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
