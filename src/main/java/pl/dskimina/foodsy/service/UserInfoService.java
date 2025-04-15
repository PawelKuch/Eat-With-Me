package pl.dskimina.foodsy.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.data.UserInfo;
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

    public UserInfo getUserInfo(String userId, String orderId){
        Order order = orderRepository.findByOrderId(orderId);
        User user = userRepository.findByUserId(userId);
        int howManyUsersInOrder = orderRepository.getUsersAmountForOrder(orderId);

        UserInfo userInfo = new UserInfo();
        double generalOrderValue = orderItemRepository.getOrderItemsValueForOrder(orderId);
        LOG.warn("generalOrderValue: {}", generalOrderValue);
        double orderItemsValue = Math.round(orderItemRepository.getOrderItemsValueForUserAndOrder(orderId, userId) * 100.0) / 100.0;

        double percentageDiscountInDecimal = Math.round((order.getPercentageDiscount() / 100.0) * 100.0) / 100.0;
        double percentageDiscount = order.getPercentageDiscount();
        double cashDiscount = Math.round(((orderItemsValue / generalOrderValue) * order.getCashDiscount()) * 100.0) / 100.0;
        LOG.warn("orderItemsValue: {}", orderItemsValue);
        LOG.warn("generalOrderValue: {}", generalOrderValue);
        LOG.warn("order.getCashDiscount(): {}", order.getCashDiscount());
        double baseForPercentageDiscount = orderItemsValue - cashDiscount;
        double percentageDiscountInCash = Math.round((baseForPercentageDiscount * percentageDiscountInDecimal) * 100.0) / 100.0;
        double generalDiscountValue = percentageDiscountInCash + cashDiscount;
        LOG.warn("percentageDiscountInCash: {}", percentageDiscountInCash);
        LOG.warn("cashDiscount: {}", cashDiscount);
        double extraPayment = Math.round((order.getExtraPaymentValue() / howManyUsersInOrder) * 100.0) / 100.0;
        LOG.warn("order.getExtraPaymentValue(): {}", order.getExtraPaymentValue());
        LOG.warn("extraPayment: {}", extraPayment);
        LOG.warn("howManyUsers: {}", howManyUsersInOrder);
        double amountToPayWithoutExtraPayment = Math.round((orderItemsValue - generalDiscountValue) * 100.0) / 100.0;
        LOG.warn("amountToPayWithoutExtraPayment: {}", amountToPayWithoutExtraPayment);
        LOG.warn("orderItemsValue: {}", orderItemsValue);
        LOG.warn("generalDiscountValue: {}", generalDiscountValue);
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
