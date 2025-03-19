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
        //userOrderPaymentRepository.save(userOrderPayment);
        return userOrderPayment;
    }
    Logger LOG = LoggerFactory.getLogger(UserOrderPaymentService.class);
    @Transactional
    public void addUserOrderPaymentInfoForOrderIdAndUserId(String orderId, String userId) {
        UserOrderInfo orderInfoForUserAfterAddingOrderItem = orderItemRepository.getUserOrderInfo(orderId, userId);
        Order order = orderRepository.findByOrderId(orderId);
        double menuItemsValueForUserAfterAddingOrderItem = orderInfoForUserAfterAddingOrderItem.getMenuItemsValue();
        double currentPercentageDiscountForOrderNoDecimalValue = orderRepository.getPercentageDiscountForOrder(orderId);
        double currentPercentageDiscountForOrder = currentPercentageDiscountForOrderNoDecimalValue / 100.0; //procenty np. 23% = 0.23
        int howManyUsersInOrder = orderRepository.getUsersAmountForOrder(orderId);
        double currentCashDiscountForOrder = orderRepository.getCashDiscountForOrder(orderId);
        double currentExtraPaymentForOrder = Objects.requireNonNullElse(extraPaymentRepository.getExtraPaymentsValueForOrder(orderId), 0.0);
        double orderItemsGeneralValueForOrder = order.getNetValue();

        double currentCashDiscountForUser = (menuItemsValueForUserAfterAddingOrderItem / orderItemsGeneralValueForOrder) * currentCashDiscountForOrder;
        LOG.warn("menuItemsValueAfterAddingOrderItem: {}", menuItemsValueForUserAfterAddingOrderItem);
        LOG.warn("generalOrderItemsValueForOrder: {}", orderItemsGeneralValueForOrder);
        LOG.warn("currentCashDiscountForOrder: {}", currentCashDiscountForOrder);
        LOG.warn("currentCashDiscountForUser: {}", currentCashDiscountForUser);
        double currentExtraPaymentForUser = howManyUsersInOrder > 0 ? currentExtraPaymentForOrder / howManyUsersInOrder : currentExtraPaymentForOrder;
        double currentPercentageDiscountInCashForUser = menuItemsValueForUserAfterAddingOrderItem * currentPercentageDiscountForOrder;


        //czy użytkownik na zamówieniu istnieje
        if(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId(orderId, userId)){
           UserOrderPayment uop = userOrderPaymentRepository.findByOrderOrderIdAndUserUserId(orderId, userId);
           uop.setAmountToPayWithoutExtraPayment(menuItemsValueForUserAfterAddingOrderItem - (currentCashDiscountForUser + currentPercentageDiscountInCashForUser));
           uop.setMenuItemsValue(menuItemsValueForUserAfterAddingOrderItem);
           uop.setAmountToPay(menuItemsValueForUserAfterAddingOrderItem - (currentCashDiscountForUser + currentPercentageDiscountInCashForUser) + currentExtraPaymentForUser);
           uop.setDiscountInPercentageInCash(currentPercentageDiscountInCashForUser);
           uop.setGeneralDiscountValue((uop.getGeneralDiscountValue() - uop.getDiscountValueInCash()) + currentPercentageDiscountInCashForUser);
           uop.setDiscountValueInCash(currentCashDiscountForUser);
           uop.setDiscountInPercentage(Math.round(currentPercentageDiscountForOrderNoDecimalValue * 100.0) / 100.0);
           uop.setBaseForPercentageDiscount(menuItemsValueForUserAfterAddingOrderItem);
           uop.setExtraPaymentValue(currentExtraPaymentForUser);


           List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
           userOrderPayments.forEach(data -> {
               if(!data.getUser().getUserId().equals(userId)){
                   UserOrderInfo uoi = orderItemRepository.getUserOrderInfo(orderId, data.getUser().getUserId());
                   double cashDiscountForUser = (data.getMenuItemsValue() / order.getNetValue()) * order.getCashDiscount();
                   double percentageDiscountInCashForUser = Math.round((uoi.getMenuItemsValue() * currentPercentageDiscountForOrder) * 100.0) / 100.0;
                   data.setDiscountValueInCash(cashDiscountForUser);
                   data.setAmountToPayWithoutExtraPayment(uoi.getMenuItemsValue() - (cashDiscountForUser + percentageDiscountInCashForUser));
                   data.setAmountToPay(uoi.getMenuItemsValue() - (cashDiscountForUser + percentageDiscountInCashForUser) + currentExtraPaymentForUser);
               }
           });
           userOrderPayments.add(uop);
           userOrderPaymentRepository.saveAll(userOrderPayments);

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
            userOrderPayment.setMenuItemsValue(menuItemsValueForUserAfterAddingOrderItem);
            userOrderPayment.setAmountToPay(amountToPay);
            userOrderPayment.setAmountToPayWithoutExtraPayment(amountToPay - currentExtraPaymentForUser);
            userOrderPayment.setDiscountValueInCash(currentCashDiscountForUser);
            userOrderPayment.setDiscountInPercentage(currentPercentageDiscountForOrderNoDecimalValue);
            LOG.warn("currentPercentageDiscountForOrderNoDecimalValue: " + currentPercentageDiscountForOrderNoDecimalValue);
            userOrderPayment.setDiscountInPercentageInCash(currentPercentageDiscountInCashForUser);
            userOrderPayment.setBaseForPercentageDiscount(menuItemsValueForUserAfterAddingOrderItem);
            userOrderPayment.setGeneralDiscountValue(currentPercentageDiscountInCashForUser + currentCashDiscountForUser);


            //Akutalizacja reszty usoerOrderPayment ze wzgledu na powstanie nowego UserORderPayment, przez co wartość howManyUsers jest inna, wiec wartość rabatu kwotowego
            // oraz extrapayment jest rozkładana na większą liczbę userów
            List<UserOrderPayment> userOrderPayments = userOrderPaymentRepository.findByOrderOrderId(orderId);
            for(UserOrderPayment uop : userOrderPayments){
                UserOrderInfo userOrderInfo = orderItemRepository.getUserOrderInfo(orderId, uop.getUser().getUserId());
                double baseForPercentageDiscount = userOrderInfo.getMenuItemsValue();
                double cashDiscountForUser = (uop.getMenuItemsValue() / order.getNetValue()) * order.getCashDiscount();
                uop.setAmountToPay(uop.getAmountToPay() - uop.getExtraPaymentValue() + uop.getDiscountValueInCash() + currentExtraPaymentForUser - cashDiscountForUser);
                uop.setGeneralDiscountValue((uop.getGeneralDiscountValue() - uop.getDiscountValueInCash()) + cashDiscountForUser);
                uop.setExtraPaymentValue(currentExtraPaymentForUser);
                uop.setDiscountValueInCash(currentCashDiscountForUser);
                uop.setDiscountInPercentageInCash(uop.getGeneralDiscountValue() - cashDiscountForUser);
                uop.setBaseForPercentageDiscount(baseForPercentageDiscount);
            }
            userOrderPayments.add(userOrderPayment);
            userOrderPaymentRepository.saveAll(userOrderPayments);
        }
    }

    public void addPercentageDiscountInUserOrderPaymentForOrder(List<UserOrderPayment> userOrderPayments, double percentageDiscountDouble){
        userOrderPayments.forEach(data -> {
            data.setDiscountInPercentage(percentageDiscountDouble);
            double baseForPercentageDiscount = Math.round((data.getAmountToPay() - data.getExtraPaymentValue()) * 100.0) / 100.0;
            Double percentageDiscountValue = Math.round((baseForPercentageDiscount * percentageDiscountDouble)*100.0) / 100.0;

            data.setBaseForPercentageDiscount(baseForPercentageDiscount);
            data.setDiscountInPercentageInCash(percentageDiscountValue);
            data.setGeneralDiscountValue(data.getGeneralDiscountValue() + percentageDiscountValue);
            double amountToPay = Math.round((data.getAmountToPay() - percentageDiscountValue) * 100.0) / 100.0;
            data.setAmountToPay(amountToPay);
            data.setAmountToPayWithoutExtraPayment(data.getAmountToPay() - data.getExtraPaymentValue());
            userOrderPaymentRepository.save(data);
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
