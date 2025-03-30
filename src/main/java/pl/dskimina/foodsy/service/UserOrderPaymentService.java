package pl.dskimina.foodsy.service;

import jakarta.transaction.Transactional;
import org.apache.juli.logging.Log;
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
import java.util.Objects;
import java.util.UUID;

@Service
public class UserOrderPaymentService {

    private final UserOrderPaymentRepository userOrderPaymentRepository;
    private final ExtraPaymentRepository extraPaymentRepository;
    private final ToDataService toDataService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public UserOrderPaymentService(UserOrderPaymentRepository userOrderPaymentRepository,
                                   ExtraPaymentRepository extraPaymentRepository, ToDataService toDataService,
                                   OrderRepository orderRepository, UserRepository userRepository, OrderItemRepository orderItemRepository) {
        this.userOrderPaymentRepository = userOrderPaymentRepository;
        this.extraPaymentRepository = extraPaymentRepository;
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
    Logger LOG = LoggerFactory.getLogger(UserOrderPaymentService.class);
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
        double extraPaymentForUser = howManyUsersInOrder > 0 ? currentExtraPaymentForOrder / howManyUsersInOrder : currentExtraPaymentForOrder;
        double currentPercentageDiscountInCashForUser = Math.round(currentBaseForPercentageDiscount * currentPercentageDiscountForOrder * 100.0) / 100.0;


        //czy użytkownik na zamówieniu istnieje
        if(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId(orderId, userId)){
           UserOrderPayment uop = userOrderPaymentRepository.findByOrderOrderIdAndUserUserId(orderId, userId);
           List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId) != null ? userOrderPaymentRepository.findByOrderOrderId(orderId) : new ArrayList<>();

           userOrderPayments.forEach(data -> {
               if(!data.getUser().getUserId().equals(userId)){
                   LOG.warn("data.getUser().getUserId().equals(userId): {}", false);
                   UserOrderInfo uoi = orderItemRepository.getUserOrderInfo(orderId, data.getUser().getUserId());
                   double cashDiscountForUser = (data.getMenuItemsValue() / order.getNetValue()) * order.getCashDiscount();
                   double percentageDiscountInCashForUser = Math.round((uoi.getMenuItemsValue() * currentPercentageDiscountForOrder) * 100.0) / 100.0;
                   data.setDiscountValueInCash(cashDiscountForUser);
                   data.setAmountToPayWithoutExtraPayment(uoi.getMenuItemsValue() - (cashDiscountForUser + percentageDiscountInCashForUser));
                   data.setAmountToPay(uoi.getMenuItemsValue() - (cashDiscountForUser + percentageDiscountInCashForUser) + extraPaymentForUser);
               }else {
                   LOG.warn("data.getUser().getUserId().equals(userId): {}", true);
                   uop.setAmountToPayWithoutExtraPayment(menuItemsValueForUserAfterAddingOrderItem - (currentCashDiscountForUser + currentPercentageDiscountInCashForUser));
                   LOG.warn("40. menuItemsValueForUserAfterAddingOrderItem: {}", menuItemsValueForUserAfterAddingOrderItem);
                   LOG.warn("50. currentCashDiscountForUser: {}", currentCashDiscountForUser);
                   LOG.warn("60. currentPercentageDiscountInCashForUser: {}", currentPercentageDiscountInCashForUser);
                   uop.setMenuItemsValue(menuItemsValueForUserAfterAddingOrderItem);
                   uop.setBaseForPercentageDiscount(menuItemsValueForUserAfterAddingOrderItem - currentCashDiscountForUser);
                   double percentageDiscountInCashForUser = Math.round((uop.getBaseForPercentageDiscount() * currentPercentageDiscountForOrder) * 100.0) / 100.0;
                   uop.setAmountToPay(menuItemsValueForUserAfterAddingOrderItem - (currentCashDiscountForUser + percentageDiscountInCashForUser) + extraPaymentForUser);
                   LOG.warn("____menuItemsValueForUserAfterAddingOrderItem {}", menuItemsValueForUserAfterAddingOrderItem);
                   LOG.warn("currentCashDiscountForUser {}", currentCashDiscountForUser);
                   LOG.warn("currentPercentageDiscountInCashForUser {}",  currentPercentageDiscountInCashForUser);
                   LOG.warn("extraPaymentForUser: {}", extraPaymentForUser);
                   LOG.warn("uop.setAmountToPay: {}", uop.getAmountToPay());
                   uop.setDiscountInPercentageInCash(percentageDiscountInCashForUser);
                   uop.setGeneralDiscountValue(percentageDiscountInCashForUser + currentCashDiscountForUser);
                   uop.setDiscountValueInCash(currentCashDiscountForUser);
                   LOG.warn("1. currentCashDiscountForUser: {}", currentCashDiscountForUser);
                   uop.setDiscountInPercentage(Math.round(currentPercentageDiscountForOrderNoDecimalValue * 100.0) / 100.0);
                   uop.setExtraPaymentValue(extraPaymentForUser);
               }
           });
           LOG.warn("_____userOrderPaymentsSize!!!: {}", userOrderPayments.size());
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
                    LOG.warn("28. uop.getMenuItemsValue(): {}", uop.getMenuItemsValue());
                    LOG.warn("27. cashDiscountForUser: {}", cashDiscountForUser);
                    double percentageDiscountInCashForUser = Math.round((baseForPercentageDiscountForUser * (uop.getDiscountInPercentage() / 100)) * 100.0) / 100.0;
                    LOG.warn("31. baseForPercnetage * uop.getDiscountInPercentage: {}", baseForPercentageDiscountForUser * uop.getDiscountInPercentage());
                    LOG.warn("30. percentageDiscountInCashForUser: {}", percentageDiscountInCashForUser);
                    LOG.warn("29. uop.getDiscountInPercentage(): {}", Math.round((uop.getDiscountInPercentage() / 100) * 100.0) / 100.0);
                    LOG.warn("26. baseForPercentageDiscountForUser: {}", baseForPercentageDiscountForUser);
                    uop.setBaseForPercentageDiscount(Math.round(baseForPercentageDiscountForUser * 100.0) / 100.0);
                    uop.setDiscountInPercentageInCash(percentageDiscountInCashForUser);
                    double amountToPayForUser = uop.getMenuItemsValue() - percentageDiscountInCashForUser - cashDiscountForUser + extraPaymentForUser;
                    LOG.warn("24. amountToPayForUser: {}", amountToPayForUser);
                    LOG.warn("20. uop.getMenuItemsValue(): {}", uop.getMenuItemsValue());
                    LOG.warn("21. percentageDiscountInCashForUser: {}", percentageDiscountInCashForUser);
                    LOG.warn("22. cashDiscountForUser: {}", cashDiscountForUser);
                    LOG.warn("23. extraPaymentForUser: {}", extraPaymentForUser);
                    uop.setAmountToPay(Math.round(amountToPayForUser * 100.0) / 100.0);
                    uop.setAmountToPayWithoutExtraPayment(Math.round((amountToPayForUser - extraPaymentForUser) * 100.0) / 100.0);
                    uop.setGeneralDiscountValue(uop.getDiscountValueInCash() + uop.getDiscountInPercentageInCash());
                }
            }
                    UserOrderPayment userOrderPayment = createUserOrderPayment(orderId, userId);
                    double baseForPercentageDiscount = menuItemsValueForUserAfterAddingOrderItem - currentCashDiscountForUser;
                    double percentageDiscountInCash = Math.round((baseForPercentageDiscount * currentPercentageDiscountForOrder) * 100.0) / 100.0;

                    Double amountToPay = Math.round((menuItemsValueForUserAfterAddingOrderItem - percentageDiscountInCash + extraPaymentForUser - currentCashDiscountForUser) * 100.0) / 100.0;
                    LOG.warn("4. menuItemsValueForUserAfterAddingOrderItem: {}", menuItemsValueForUserAfterAddingOrderItem);
                    LOG.warn("6. percentageDiscountInCash: {}", percentageDiscountInCash);
                    LOG.warn("7. extraPaymentForUser: {}", extraPaymentForUser);
                    LOG.warn("8. currentCashDiscountForUser: {}", Math.round(currentCashDiscountForUser * 100.0) / 100.0);
                    LOG.warn("9. amountToPa without rounding: {}", menuItemsValueForUserAfterAddingOrderItem - percentageDiscountInCash + extraPaymentForUser - currentCashDiscountForUser);
                    userOrderPayment.setExtraPaymentValue(extraPaymentForUser);
                    userOrderPayment.setMenuItemsValue(menuItemsValueForUserAfterAddingOrderItem);
                    userOrderPayment.setAmountToPay(Math.round(amountToPay * 100.0) / 100.0);
                    LOG.warn("5. amountToPay: {}", amountToPay);
                    userOrderPayment.setAmountToPayWithoutExtraPayment(Math.round((amountToPay - extraPaymentForUser) * 100.0) / 100.0);
                    userOrderPayment.setDiscountValueInCash(Math.round(currentCashDiscountForUser * 100.0) / 100.0);
                    userOrderPayment.setDiscountInPercentage(currentPercentageDiscountForOrderNoDecimalValue);
                    userOrderPayment.setDiscountInPercentageInCash(Math.round(percentageDiscountInCash * 100.0) / 100.0);

                    userOrderPayment.setBaseForPercentageDiscount(Math.round(baseForPercentageDiscount * 100.0) / 100.0);
                    double generalDiscountValue = percentageDiscountInCash + currentCashDiscountForUser;
                    userOrderPayment.setGeneralDiscountValue(Math.round(generalDiscountValue * 100.0) / 100.0);
                    LOG.warn("created userOrderPayment!");
                    userOrderPayments.add(userOrderPayment);


            LOG.warn("!!!!When not exists size of userPaymentsList: {}", userOrderPayments.size());
            userOrderPaymentRepository.saveAll(userOrderPayments);
        }
    }


    @Transactional
    public void updateUserOrderPaymentsForDiscounts(String orderId, String newCashDiscountString, String newPercentageDiscountString, String newExtraPaymentValueString){
        double newCashDiscount = Double.parseDouble(newCashDiscountString);
        double newPercentageDiscount = Double.parseDouble(newPercentageDiscountString);
        double newPercentageDiscountDouble = Math.round((newPercentageDiscount / 100) * 100.0) / 100.0;
        double newExtraPaymentValue = Double.parseDouble(newExtraPaymentValueString);
        int howManyUsersInOrder = orderRepository.getUsersAmountForOrder(orderId);

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        userOrderPayments.forEach(data -> {
            double currentCashDiscount = data.getDiscountValueInCash();
            double currentPercentageDiscount = data.getDiscountInPercentage();
            double currentPercentageDiscountInCash = data.getDiscountInPercentageInCash();
            double currentExtraPaymentValue = data.getExtraPaymentValue();



        });
    }



    @Transactional
    public void updatePercentageDiscountInUserOrderPaymentForUser(UserOrderPayment userOrderPayment, String newPercentageDiscountString){
        LOG.info("newPercentageDiscountString: " + newPercentageDiscountString);
        double newPercentageDiscount = Math.round((Double.parseDouble(newPercentageDiscountString) / 100.0) * 100.0) / 100.0;
        LOG.info("newPercentageDiscount: " + newPercentageDiscount);
        userOrderPayment.setDiscountInPercentage(newPercentageDiscount);

        double newPercentageDiscountValueInCash = Math.round((userOrderPayment.getBaseForPercentageDiscount() * newPercentageDiscount) * 100.0) / 100.0;
        LOG.info("newPercentageDiscountValueInCash: " + newPercentageDiscountValueInCash);
        double newAmountToPay = (Math.round(userOrderPayment.getBaseForPercentageDiscount() * 100.0) / 100.0) - newPercentageDiscountValueInCash + userOrderPayment.getExtraPaymentValue();

        LOG.info("newAmountToPay: " + newAmountToPay);


        userOrderPayment.setDiscountInPercentageInCash(newPercentageDiscountValueInCash);
        userOrderPayment.setAmountToPay(newAmountToPay);
        userOrderPayment.setAmountToPayWithoutExtraPayment(userOrderPayment.getAmountToPay() - userOrderPayment.getExtraPaymentValue());

        LOG.info("getGeneralDiscountValue: " + userOrderPayment.getGeneralDiscountValue());
        userOrderPayment.setGeneralDiscountValue(newPercentageDiscountValueInCash + userOrderPayment.getDiscountValueInCash());
        LOG.info("userOrderPayment.getGeneralDiscountValue() + percentageDiscountValue: " + newPercentageDiscountValueInCash + userOrderPayment.getDiscountValueInCash());

        userOrderPaymentRepository.save(userOrderPayment);
    }


    @Transactional
    public List<UserOrderPayment> findUserOrderPaymentsForOrderId(String orderId) {
        return userOrderPaymentRepository.findByOrderOrderId(orderId);
    }

    @Transactional
    public List<UserOrderPaymentData> getUserOrderPaymentsForOrderId(String orderId){
        return userOrderPaymentRepository.findByOrderOrderId(orderId)
                .stream()
                .map(toDataService::convert)
                .toList();
    }

    @Transactional
    public void saveUserOrderPayment(UserOrderPayment userOrderPayment) {
        userOrderPaymentRepository.save(userOrderPayment);
    }

}
