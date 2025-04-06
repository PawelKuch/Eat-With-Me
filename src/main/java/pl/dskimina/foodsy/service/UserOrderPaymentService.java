package pl.dskimina.foodsy.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.UserOrderPayment;

import pl.dskimina.foodsy.entity.data.UserOrderInfo;
import pl.dskimina.foodsy.entity.data.UserOrderPaymentData;
import pl.dskimina.foodsy.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserOrderPaymentService {
    private final static Logger LOG = LoggerFactory.getLogger(UserOrderPaymentService.class);
    private final UserOrderPaymentRepository userOrderPaymentRepository;
    private final ToDataService toDataService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public UserOrderPaymentService(UserOrderPaymentRepository userOrderPaymentRepository,
                                   ToDataService toDataService,
                                   OrderRepository orderRepository, UserRepository userRepository, OrderItemRepository orderItemRepository) {
        this.userOrderPaymentRepository = userOrderPaymentRepository;
        this.toDataService = toDataService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public UserOrderPayment createUserOrderPayment(String orderId, String userId) {
        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId(UUID.randomUUID().toString());
        Order order = orderRepository.findByOrderId(orderId);
        User user = userRepository.findByUserId(userId);
        userOrderPayment.setOrder(order);
        userOrderPayment.setUser(user);
        userOrderPayment.setMenuItemsValue(0.0);
        userOrderPayment.setAmountToPayWithoutExtraPayment(0.0);
        userOrderPayment.setAmountToPay(0.0);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setGeneralDiscountValue(0.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setDiscountInPercentageInCash(0.0);
        userOrderPayment.setBaseForPercentageDiscount(0.0);
        userOrderPayment.setIsPaid(false);
        return userOrderPayment;
    }

    @Transactional
    public void addUserOrderPaymentInfoForOrderIdAndUserId(String orderId, String userId) {
        UserOrderInfo orderInfoForUserAfterAddingOrderItem = orderItemRepository.getUserOrderInfo(orderId, userId);
        Order order = orderRepository.findByOrderId(orderId);
        double menuItemsValueForUserAfterAddingOrderItem = orderInfoForUserAfterAddingOrderItem.getMenuItemsValue();
        double currentPercentageDiscountForOrderNoDecimalValue = order.getPercentageDiscount();
        double currentPercentageDiscountForOrder = currentPercentageDiscountForOrderNoDecimalValue / 100.0; //procenty np. 23% = 0.23
        int howManyUsersInOrder = orderRepository.getUsersAmountForOrder(orderId);
        double currentCashDiscountForOrder = orderRepository.getCashDiscountForOrder(orderId);
        double currentExtraPaymentForOrder = order.getExtraPaymentValue();
        double orderItemsGeneralValueForOrder = orderItemRepository.getOrderItemsValueForOrder(orderId);

        double currentBaseForPercentageDiscount = menuItemsValueForUserAfterAddingOrderItem - order.getCashDiscount();
        double currentCashDiscountForUser = Math.round(((menuItemsValueForUserAfterAddingOrderItem / orderItemsGeneralValueForOrder) * currentCashDiscountForOrder) * 100.0) / 100.0;
        double extraPaymentForUser = howManyUsersInOrder > 0 ? Math.round((currentExtraPaymentForOrder / howManyUsersInOrder) * 100.0) / 100.0 : currentExtraPaymentForOrder;
        double currentPercentageDiscountInCashForUser = Math.round(currentBaseForPercentageDiscount * currentPercentageDiscountForOrder * 100.0) / 100.0;


        //czy użytkownik na zamówieniu istnieje
        if(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId(orderId, userId)){
           UserOrderPayment uop = userOrderPaymentRepository.findByOrderOrderIdAndUserUserId(orderId, userId);
           List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId) != null ? userOrderPaymentRepository.findByOrderOrderId(orderId) : new ArrayList<>();

           userOrderPayments.forEach(data -> {
               if(!data.getUser().getUserId().equals(userId)){
                   UserOrderInfo uoi = orderItemRepository.getUserOrderInfo(orderId, data.getUser().getUserId());
                   double cashDiscountForUser = (data.getMenuItemsValue() / order.getNetValue()) * order.getCashDiscount();
                   double percentageDiscountInCashForUser = Math.round((uoi.getMenuItemsValue() * currentPercentageDiscountForOrder) * 100.0) / 100.0;
                   data.setDiscountValueInCash(Math.round(cashDiscountForUser * 100.0) / 100.0);
                   double amountToPayWithoutExtraPayment = uoi.getMenuItemsValue() - (cashDiscountForUser + percentageDiscountInCashForUser);
                   data.setAmountToPayWithoutExtraPayment(Math.round(amountToPayWithoutExtraPayment * 100.0) / 100.0);
                   double amountToPay = uoi.getMenuItemsValue() - (cashDiscountForUser + percentageDiscountInCashForUser) + extraPaymentForUser;
                   data.setAmountToPay(Math.round(amountToPay * 100.0) / 100.0);
               }else {
                   double amountToPayWithoutExtraPayment = menuItemsValueForUserAfterAddingOrderItem - (currentCashDiscountForUser + currentPercentageDiscountInCashForUser);
                   uop.setAmountToPayWithoutExtraPayment(Math.round(amountToPayWithoutExtraPayment * 100.0) / 100.0);
                   uop.setMenuItemsValue(menuItemsValueForUserAfterAddingOrderItem);
                   uop.setBaseForPercentageDiscount(menuItemsValueForUserAfterAddingOrderItem - currentCashDiscountForUser);
                   double percentageDiscountInCashForUser = Math.round((uop.getBaseForPercentageDiscount() * currentPercentageDiscountForOrder) * 100.0) / 100.0;
                   double amountToPay = menuItemsValueForUserAfterAddingOrderItem - (currentCashDiscountForUser + percentageDiscountInCashForUser) + extraPaymentForUser;
                   uop.setAmountToPay(Math.round(amountToPay * 100.0) / 100.0);
                   uop.setDiscountInPercentageInCash(percentageDiscountInCashForUser);
                   uop.setGeneralDiscountValue(Math.round((percentageDiscountInCashForUser + currentCashDiscountForUser) * 100.0) / 100.0);
                   uop.setDiscountValueInCash(currentCashDiscountForUser);
                   uop.setDiscountInPercentage(Math.round(currentPercentageDiscountForOrderNoDecimalValue * 100.0) / 100.0);
                   uop.setExtraPaymentValue(extraPaymentForUser);
               }
           });
           userOrderPaymentRepository.saveAll(userOrderPayments);
        } else {
            List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);

            double orderNetValue = orderItemRepository.getOrderItemsValueForOrder(orderId);
            for(UserOrderPayment uop : userOrderPayments) {
                if (!uop.getUser().getUserId().equals(userId)) {
                    double cashDiscountForUser = (uop.getMenuItemsValue() / orderNetValue) * order.getCashDiscount();
                    uop.setDiscountValueInCash(Math.round(cashDiscountForUser * 100.0) / 100.0);
                    uop.setExtraPaymentValue(extraPaymentForUser);

                    double baseForPercentageDiscountForUser = uop.getMenuItemsValue() - cashDiscountForUser;
                    double percentageDiscountInCashForUser = Math.round((baseForPercentageDiscountForUser * (uop.getDiscountInPercentage() / 100)) * 100.0) / 100.0;
                    uop.setBaseForPercentageDiscount(Math.round(baseForPercentageDiscountForUser * 100.0) / 100.0);
                    uop.setDiscountInPercentageInCash(percentageDiscountInCashForUser);
                    double amountToPayForUser = Math.round((uop.getMenuItemsValue() - percentageDiscountInCashForUser - cashDiscountForUser + extraPaymentForUser) * 100.0) / 100.0;
                    LOG.warn("uop.getMenuItemsValue(): {}", uop.getMenuItemsValue());
                    LOG.warn("percentageDiscountInCashForUser: {}", percentageDiscountInCashForUser);
                    LOG.warn("cashDiscountForUser: {}", cashDiscountForUser);
                    LOG.warn("extraPaymentForUser: {}", extraPaymentForUser);
                    LOG.warn("amountToPayForUser: {}", amountToPayForUser);
                    uop.setAmountToPay(amountToPayForUser);
                    uop.setAmountToPayWithoutExtraPayment(Math.round((amountToPayForUser - extraPaymentForUser) * 100.0) / 100.0);
                    uop.setGeneralDiscountValue(Math.round((uop.getDiscountValueInCash() + uop.getDiscountInPercentageInCash()) * 100.0) / 100.0);
                }
            }
                    UserOrderPayment userOrderPayment = createUserOrderPayment(orderId, userId);
                    double baseForPercentageDiscount = menuItemsValueForUserAfterAddingOrderItem - currentCashDiscountForUser;
                    double percentageDiscountInCash = Math.round((baseForPercentageDiscount * currentPercentageDiscountForOrder) * 100.0) / 100.0;

                    double amountToPay = Math.round((menuItemsValueForUserAfterAddingOrderItem - percentageDiscountInCash + extraPaymentForUser - currentCashDiscountForUser) * 100.0) / 100.0;
                    userOrderPayment.setExtraPaymentValue(extraPaymentForUser);
                    userOrderPayment.setMenuItemsValue(Math.round(menuItemsValueForUserAfterAddingOrderItem * 100.0) / 100.0);
                    userOrderPayment.setAmountToPay(Math.round(amountToPay * 100.0) / 100.0);
                    userOrderPayment.setAmountToPayWithoutExtraPayment(Math.round((amountToPay - extraPaymentForUser) * 100.0) / 100.0);
                    userOrderPayment.setDiscountValueInCash(Math.round(currentCashDiscountForUser * 100.0) / 100.0);
                    userOrderPayment.setDiscountInPercentage(currentPercentageDiscountForOrderNoDecimalValue);
                    userOrderPayment.setDiscountInPercentageInCash(Math.round(percentageDiscountInCash * 100.0) / 100.0);

                    userOrderPayment.setBaseForPercentageDiscount(Math.round(baseForPercentageDiscount * 100.0) / 100.0);
                    double generalDiscountValue = percentageDiscountInCash + currentCashDiscountForUser;
                    userOrderPayment.setGeneralDiscountValue(Math.round(generalDiscountValue * 100.0) / 100.0);
                    userOrderPayments.add(userOrderPayment);
            userOrderPaymentRepository.saveAll(userOrderPayments);
        }
    }

    @Transactional
    public List<UserOrderPaymentData> getUserOrderPaymentsForOrderId(String orderId){
        return userOrderPaymentRepository.findByOrderOrderId(orderId)
                .stream()
                .map(toDataService::convert)
                .toList();
    }
}
