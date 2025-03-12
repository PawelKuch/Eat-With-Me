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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserOrderPaymentService {

    private final UserOrderPaymentRepository userOrderPaymentRepository;
    private final UserService userService;
    private final OrderItemService orderItemService;
    private final ExtraPaymentRepository extraPaymentRepository;
    private final ToDataService toDataService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public UserOrderPaymentService(UserOrderPaymentRepository userOrderPaymentRepository, UserService userService,
                                   OrderItemService orderItemService, ExtraPaymentRepository extraPaymentRepository, ToDataService toDataService,
                                   OrderRepository orderRepository, UserRepository userRepository, OrderItemRepository orderItemRepository) {
        this.userOrderPaymentRepository = userOrderPaymentRepository;
        this.userService = userService;
        this.orderItemService = orderItemService;
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
        userOrderPayment.setAmountToPay(0.0);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setGeneralDiscountValue(0.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setDiscountInPercentageInCash(0.0);
        userOrderPayment.setBaseForPercentageDiscount(0.0);
        userOrderPayment.setIsPaid(false);
        //userOrderPaymentRepository.save(userOrderPayment);
        return userOrderPayment;
    }
    Logger LOG = LoggerFactory.getLogger(UserOrderPaymentService.class);
    @Transactional
    public void addUserOrderPaymentInfoForOrderIdAndUserId(String orderId, String userId) {
        int howManyUsersInOrder = orderRepository.getUsersAmountForOrder(orderId);
        UserOrderInfo userOrderInfoDTO = orderItemRepository.getUserOrderInfo(orderId, userId);//user ,Order, OrderItemsValueForUser
        Double extraPaymentValueForOrder = extraPaymentRepository.getExtraPaymentsValueForOrder(orderId); //Objects.requireNonNullElse(extraPaymentRepository.getExtraPaymentsValueForOrder(orderId), 0.0);
        Double cashDiscountForOrder = orderRepository.getCashDiscountForOrder(orderId);
        Double percentageDiscount = orderRepository.getPercentageDiscountForOrder(orderId);
        LOG.warn("percentageDiscount: " + percentageDiscount);

        LOG.warn("cashDiscountForOrder: " + cashDiscountForOrder);
        Double amountToPay = userOrderInfoDTO.getMenuItemsValue();
        LOG.warn("amountToPay: " + amountToPay);
        double extraPaymentValueToPayForUser = 0.0;
        double cashDiscountForUser = 0.0;
        double percentageDiscountInCashForUser = Math.round((amountToPay * (percentageDiscount / 100.0)) * 100.0) / 100.0;
        LOG.warn("percentageDiscountInCashForUser: " + percentageDiscountInCashForUser);
        double baseForPercentageDiscount = amountToPay;
        LOG.warn("baseForPercentageDiscount: " + baseForPercentageDiscount);

        if(howManyUsersInOrder > 0 && extraPaymentValueForOrder != null) {
            extraPaymentValueToPayForUser = Math.round(extraPaymentValueForOrder / (howManyUsersInOrder) * 100.0) / 100.0;
            cashDiscountForUser = Math.round((cashDiscountForOrder / howManyUsersInOrder * 100.0)) / 100.0;
            LOG.warn("CashDiscountForUser: " + cashDiscountForUser);
            amountToPay = amountToPay + extraPaymentValueToPayForUser - cashDiscountForUser - percentageDiscountInCashForUser;
            LOG.warn("amountToPay calculating! NewAmountToPay: "  + amountToPay);

        }

        List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
        if(userOrderPayments != null && !userOrderPayments.isEmpty()){
            for (UserOrderPayment userOrderPayment : userOrderPayments) {
                userOrderPayment.setAmountToPay((userOrderPayment.getAmountToPay() - userOrderPayment.getExtraPaymentValue()) + extraPaymentValueToPayForUser - cashDiscountForUser);
                userOrderPayment.setExtraPaymentValue(extraPaymentValueToPayForUser);
                userOrderPayment.setGeneralDiscountValue(cashDiscountForUser + percentageDiscountInCashForUser);
                userOrderPayment.setDiscountInPercentageInCash(percentageDiscountInCashForUser);
                userOrderPayment.setBaseForPercentageDiscount(baseForPercentageDiscount);
            }
        }


        if(!userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId(orderId, userId)){
            UserOrderPayment userOrderPayment = createUserOrderPayment(orderId, userId);
            userOrderPayment.setExtraPaymentValue(extraPaymentValueToPayForUser);
            userOrderPayment.setAmountToPay(amountToPay);
            userOrderPaymentRepository.save(userOrderPayment);

        } else {
            UserOrderPayment userOrderPayment = userOrderPaymentRepository.findByOrderOrderIdAndUserUserId(orderId, userId);
            userOrderPayment.setAmountToPay(amountToPay);
            userOrderPaymentRepository.save(userOrderPayment);
        }
    }

    @Transactional
    public void updatePercentageDiscountInUserOrderPaymentForUser(String orderId, String userId, String newPercentageDiscountString){
        UserOrderPayment userOrderPayment = userOrderPaymentRepository.findByOrderOrderIdAndUserUserId(orderId, userId);
        LOG.info("newPercentageDiscountString: " + newPercentageDiscountString);
        double newPercentageDiscount = Math.round((Double.parseDouble(newPercentageDiscountString) / 100.0) * 100.0) / 100.0;
        LOG.info("newPercentageDiscount: " + newPercentageDiscount);
        userOrderPayment.setDiscountInPercentage(newPercentageDiscount);
        double amountToPay = userOrderPayment.getAmountToPay() + userOrderPayment.getDiscountInPercentageInCash();
        LOG.info("amountToPay: " + amountToPay);
        double newPercentageDiscountValueInCash = Math.round((userOrderPayment.getBaseForPercentageDiscount() * newPercentageDiscount) * 100.0) / 100.0;
        LOG.info("newPercentageDiscountValueInCash: " + newPercentageDiscountValueInCash);
        double newAmountToPay = (Math.round(userOrderPayment.getBaseForPercentageDiscount() * 100.0) / 100.0) - newPercentageDiscountValueInCash;
        LOG.info("newAmountToPay: " + newAmountToPay);


        userOrderPayment.setDiscountInPercentageInCash(newPercentageDiscountValueInCash);
        userOrderPayment.setAmountToPay(newAmountToPay);

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
