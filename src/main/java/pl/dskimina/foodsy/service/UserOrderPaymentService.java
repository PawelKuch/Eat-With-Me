package pl.dskimina.foodsy.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.UserOrderPayment;

import pl.dskimina.foodsy.entity.data.UserOrderInfo;
import pl.dskimina.foodsy.entity.data.UserOrderPaymentData;
import pl.dskimina.foodsy.repository.ExtraPaymentRepository;
import pl.dskimina.foodsy.repository.UserOrderPaymentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class UserOrderPaymentService {

    private final UserOrderPaymentRepository userOrderPaymentRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final OrderItemService orderItemService;
    private final ExtraPaymentRepository extraPaymentRepository;
    private final ToDataService toDataService;

    public UserOrderPaymentService(UserOrderPaymentRepository userOrderPaymentRepository, OrderService orderService, UserService userService,
                                   OrderItemService orderItemService, ExtraPaymentRepository extraPaymentRepository, ToDataService toDataService) {
        this.userOrderPaymentRepository = userOrderPaymentRepository;
        this.orderService = orderService;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.extraPaymentRepository = extraPaymentRepository;
        this.toDataService = toDataService;
    }


    public UserOrderPayment createUserOrderPayment(String orderId, String userId) {
        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId(UUID.randomUUID().toString());
        Order order = orderService.findOrderByOrderId(orderId);
        User user = userService.getUserInstanceById(userId);
        userOrderPayment.setOrder(order);
        userOrderPayment.setUser(user);
        userOrderPayment.setAmountToPay(0.0);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setIsPaid(false);
        userOrderPaymentRepository.save(userOrderPayment);
        return userOrderPayment;
    }

    @Transactional
    public void addUserOrderPaymentInfoForOrderIdAndUserId(String orderId, String userId) {
        int howManyUsersInOrder = orderService.getUsersAmountForOrder(orderId);
        UserOrderInfo userOrderInfoDTO = orderItemService.getUserOrderInfoDTO(userId, orderId); //user ,Order, OrderItemsValueForUser
        Double extraPaymentValueForOrder = extraPaymentRepository.getExtraPaymentsValueForOrder(orderId);
        Double amountToPay = userOrderInfoDTO.getMenuItemsValue();

        if(howManyUsersInOrder > 0 && extraPaymentValueForOrder != null) {
            double extraPaymentValueToPayForUser = Math.round(extraPaymentValueForOrder / (howManyUsersInOrder) * 100.0) / 100.0;
            amountToPay += extraPaymentValueToPayForUser;
        }

        if(!userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId(orderId, userId)){
            UserOrderPayment userOrderPayment = createUserOrderPayment(orderId, userId);
            userOrderPayment.setAmountToPay(amountToPay);
            userOrderPaymentRepository.save(userOrderPayment);
        } else {
            UserOrderPayment userOrderPayment = userOrderPaymentRepository.findByOrderOrderIdAndUserUserId(orderId, userId);
            userOrderPayment.setAmountToPay(amountToPay);
            userOrderPaymentRepository.save(userOrderPayment);
        }
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
