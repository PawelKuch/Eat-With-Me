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
        UserOrderInfo orderInfoForUserAfterAddingOrderItem = orderItemRepository.getUserOrderInfo(orderId, userId);
        double menuItemsValueForUserAfterAddingOrderItem = orderInfoForUserAfterAddingOrderItem.getMenuItemsValue();
        double currentPercentageDiscountForOrderNoDecimalValue = orderRepository.getPercentageDiscountForOrder(orderId);
        double currentPercentageDiscountForOrder = currentPercentageDiscountForOrderNoDecimalValue / 100.0; //procenty np. 23% = 0.23
        int howManyUsersInOrder = orderRepository.getUsersAmountForOrder(orderId);
        double currentCashDiscountForOrder = orderRepository.getCashDiscountForOrder(orderId);
        double currentExtraPaymentForOrder = Objects.requireNonNullElse(extraPaymentRepository.getExtraPaymentsValueForOrder(orderId), 0.0);

        double currentCashDiscountForUser = howManyUsersInOrder > 0 ? currentCashDiscountForOrder / howManyUsersInOrder : currentCashDiscountForOrder;
        double currentExtraPaymentForUser = howManyUsersInOrder > 0 ? currentExtraPaymentForOrder / howManyUsersInOrder : currentExtraPaymentForOrder;
        double currentPercentageDiscountInCashForUser = menuItemsValueForUserAfterAddingOrderItem * currentPercentageDiscountForOrder;


        //czy użytkownik na zamówieniu istnieje
        if(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId(orderId, userId)){
           UserOrderPayment uop = userOrderPaymentRepository.findByOrderOrderIdAndUserUserId(orderId, userId);
           double newAmountToPay = menuItemsValueForUserAfterAddingOrderItem + currentExtraPaymentForUser - (uop.getDiscountValueInCash() + currentPercentageDiscountInCashForUser) ;
           uop.setAmountToPay(newAmountToPay);
           uop.setDiscountInPercentageInCash(currentPercentageDiscountInCashForUser);
           uop.setGeneralDiscountValue(uop.getDiscountValueInCash() + currentPercentageDiscountInCashForUser);
           uop.setDiscountInPercentage(currentPercentageDiscountForOrderNoDecimalValue);
           uop.setBaseForPercentageDiscount(menuItemsValueForUserAfterAddingOrderItem);
           uop.setExtraPaymentValue(currentExtraPaymentForUser);
           userOrderPaymentRepository.save(uop);
        }else {
            //przypisywanie do tworzonego userORderPayment odpowiednich wartości
            UserOrderPayment userOrderPayment = createUserOrderPayment(orderId, userId);

            Double amountToPay = menuItemsValueForUserAfterAddingOrderItem - currentPercentageDiscountInCashForUser + currentExtraPaymentForUser - currentCashDiscountForUser;
            LOG.warn("currentExtraPaymentForUser: " + currentExtraPaymentForUser + " currentExtraPaymentValueForUser: " + currentExtraPaymentForUser);
            LOG.warn("percentageInCash: " + currentPercentageDiscountInCashForUser);
            LOG.warn("currentPercentageDiscountInCashForUser: " + currentPercentageDiscountInCashForUser);
            LOG.warn("currentExtraPaymentForUser: " + currentCashDiscountForUser);
            LOG.warn("amountToPay: " + amountToPay);

            userOrderPayment.setExtraPaymentValue(currentExtraPaymentForUser);
            userOrderPayment.setAmountToPay(amountToPay);
            userOrderPayment.setDiscountValueInCash(currentCashDiscountForUser);
            userOrderPayment.setDiscountInPercentage(currentPercentageDiscountForOrderNoDecimalValue);
            userOrderPayment.setDiscountInPercentageInCash(currentPercentageDiscountInCashForUser);
            userOrderPayment.setBaseForPercentageDiscount(menuItemsValueForUserAfterAddingOrderItem);
            userOrderPayment.setGeneralDiscountValue(currentPercentageDiscountInCashForUser + currentCashDiscountForUser);


            //Akutalizacja reszty usoerOrderPayment ze wzgledu na powstanie nowego UserORderPayment, przez co wartość howManyUsers jest inna, wiec wartość rabatu kwotowego
            // oraz extrapayment jest rozkładana na większą liczbę userów
            List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
            for(UserOrderPayment uop : userOrderPayments){
                UserOrderInfo userOrderInfo = orderItemRepository.getUserOrderInfo(orderId, uop.getUser().getUserId());
                double baseForPercentageDiscount = userOrderInfo.getMenuItemsValue();
                uop.setAmountToPay(uop.getAmountToPay() - uop.getExtraPaymentValue() + uop.getDiscountValueInCash() + currentExtraPaymentForUser - currentCashDiscountForUser);
                uop.setGeneralDiscountValue((uop.getGeneralDiscountValue() - uop.getDiscountValueInCash()) + currentCashDiscountForUser);
                uop.setExtraPaymentValue(currentExtraPaymentForUser);
                uop.setDiscountValueInCash(currentCashDiscountForUser);
                uop.setDiscountInPercentageInCash(uop.getGeneralDiscountValue() - currentCashDiscountForUser);
                uop.setBaseForPercentageDiscount(baseForPercentageDiscount);
            }
            userOrderPayments.add(userOrderPayment);
            userOrderPaymentRepository.saveAll(userOrderPayments);
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
