package pl.dskimina.foodsy.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.data.UserInfo;
import pl.dskimina.foodsy.exception.OrderNotFoundException;
import pl.dskimina.foodsy.exception.UserNotFoundException;
import pl.dskimina.foodsy.repository.OrderItemRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.UserRepository;

import java.util.List;

@Service
public class UserInfoService {
    private final static Logger LOG = LoggerFactory.getLogger(UserInfoService.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public UserInfoService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<UserInfo> getUserInfoListForOrder(String orderId) {
        List<String> userIds = orderRepository.getUsersIdForOrder(orderId);
        return userIds.stream().map( userId -> getUserInfo(userId, orderId)).toList();
    }

    public UserInfo getUserInfo(String userId, String orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        User user = userRepository.findByUserId(userId);

        if(order == null) throw new OrderNotFoundException("Nie znaleziono żądanego zamówienia id: " + orderId);
        if(user == null) throw new UserNotFoundException("Nie znaleziono żądanego użytkownika id: " + userId);

        int howManyUsersInOrder = orderRepository.getUsersAmountForOrder(orderId);

        UserInfo userInfo = new UserInfo();
        double generalOrderValue = orderItemRepository.getOrderItemsValueForOrder(orderId);
        LOG.debug("generalOrderValue: {}", generalOrderValue);
        double orderItemsValue = Math.round(orderItemRepository.getOrderItemsValueForUserAndOrder(orderId, userId) * 100.0) / 100.0;

        double percentageDiscountInDecimal = Math.round((order.getPercentageDiscount() / 100.0) * 100.0) / 100.0;
        double percentageDiscount = order.getPercentageDiscount();
        double cashDiscount = Math.round(((orderItemsValue / generalOrderValue) * order.getCashDiscount()) * 100.0) / 100.0;
        LOG.debug("orderItemsValue: {}", orderItemsValue);
        LOG.debug("generalOrderValue: {}", generalOrderValue);
        LOG.debug("order.getCashDiscount(): {}", order.getCashDiscount());
        double baseForPercentageDiscount = orderItemsValue - cashDiscount;
        double percentageDiscountInCash = Math.round((baseForPercentageDiscount * percentageDiscountInDecimal) * 100.0) / 100.0;
        double generalDiscountValue = percentageDiscountInCash + cashDiscount;
        LOG.debug("percentageDiscountInCash: {}", percentageDiscountInCash);
        LOG.debug("cashDiscount: {}", cashDiscount);
        double extraPayment = Math.round((order.getExtraPaymentValue() / howManyUsersInOrder) * 100.0) / 100.0;
        LOG.debug("order.getExtraPaymentValue(): {}", order.getExtraPaymentValue());
        LOG.debug("extraPayment: {}", extraPayment);
        LOG.debug("howManyUsers: {}", howManyUsersInOrder);
        double amountToPayWithoutExtraPayment = Math.round((orderItemsValue - generalDiscountValue) * 100.0) / 100.0;
        LOG.debug("amountToPayWithoutExtraPayment: {}", amountToPayWithoutExtraPayment);
        LOG.debug("orderItemsValue: {}", orderItemsValue);
        LOG.debug("generalDiscountValue: {}", generalDiscountValue);
        double amountToPay = amountToPayWithoutExtraPayment + extraPayment;

        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setMenuItemsValue(orderItemsValue);
        userInfo.setCashDiscount(cashDiscount);
        userInfo.setPercentageDiscount(percentageDiscount);
        userInfo.setPercentageDiscountInCash(percentageDiscountInCash);
        userInfo.setGeneralDiscount(generalDiscountValue);
        userInfo.setExtraPayment(extraPayment);
        userInfo.setAmountToPayWithoutExtraPayment(amountToPayWithoutExtraPayment);
        userInfo.setAmountToPay(amountToPay);
        return userInfo;
    }
}
