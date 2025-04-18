package pl.dskimina.foodsy.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.data.UserInfo;
import pl.dskimina.foodsy.repository.OrderItemRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    private Order createTestOrder(double netValue, double percentageDiscount, double percentageDiscountInCash, double cashDiscount, double extraPaymentValue, double value){
        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setNetValue(netValue);
        order.setPercentageDiscount(percentageDiscount);
        order.setPercentageDiscountCashValue(percentageDiscountInCash);
        order.setCashDiscount(cashDiscount);
        order.setExtraPaymentValue(extraPaymentValue);
        order.setValue(value);
        return order;
    }

    public User createUserTest(){
        User user = new User();
        user.setFirstName("firstNameTest");
        user.setLastName("lastNameTest");
        user.setUserId("userIdTest");
        return user;
    }

    @Test
    public void getUserInfoTest(){
        Order order = createTestOrder(24.0, 10.0, 2.0, 4.0, 6.0, 18.0);
        User user = createUserTest();

        Mockito.when(userRepository.findByUserId("userIdTest")).thenReturn(user);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(24.0);
        Mockito.when(orderItemRepository.getOrderItemsValueForUserAndOrder("orderIdTest", "userIdTest")).thenReturn(24.0);

        UserInfo result = userInfoService.getUserInfo("userIdTest", "orderIdTest");

        Assertions.assertEquals(24.0, result.getAmountToPay());
    }


    @Test
    public void addCashAndPercentageDiscountsInOne() {
        String orderId = "orderIdTest";
        Order order = createTestOrder(24.0, 0.0, 0.0, 0.0, 0.0, 24.0);
        order.setOrderId(orderId);

        User user = createUserTest();

        Mockito.when(orderRepository.findByOrderId(orderId)).thenReturn(order);

        orderService.addCashDiscount(orderId, "4");

        Mockito.verify(orderRepository).save(orderCaptor.capture());
        Order savedAfterCash = orderCaptor.getValue();

        Assertions.assertEquals(24.0, savedAfterCash.getNetValue());
        Assertions.assertEquals(20.0, savedAfterCash.getBaseValue());
        Assertions.assertEquals(4.0, savedAfterCash.getCashDiscount());
        Assertions.assertEquals(20.0, savedAfterCash.getValue());


        Mockito.reset(orderRepository);
        String updatedOrderId = savedAfterCash.getOrderId();
        Mockito.when(orderRepository.findByOrderId(updatedOrderId)).thenReturn(savedAfterCash);

        orderService.addPercentageDiscount(updatedOrderId, "10");

        Mockito.verify(orderRepository).save(orderCaptor.capture());
        Order savedAfterPercentage = orderCaptor.getValue();

        Assertions.assertEquals(10.0, savedAfterPercentage.getPercentageDiscount());
        Assertions.assertEquals(2.0, savedAfterPercentage.getPercentageDiscountCashValue());
        Assertions.assertEquals(4.0, savedAfterPercentage.getCashDiscount());
        Assertions.assertEquals(20.0, savedAfterPercentage.getBaseValue());
        Assertions.assertEquals(18.0, savedAfterPercentage.getValue());
        Assertions.assertEquals(0.0, savedAfterPercentage.getExtraPaymentValue());

        Mockito.reset(orderRepository);
        Mockito.when(orderRepository.findByOrderId(savedAfterPercentage.getOrderId())).thenReturn(savedAfterPercentage);


        orderService.addExtraPayment("orderIdTest", "6");

        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order savedAfterExtraPayment = orderCaptor.getValue();

        Assertions.assertEquals("orderIdTest", savedAfterExtraPayment.getOrderId());
        Assertions.assertEquals(6.0, savedAfterExtraPayment.getExtraPaymentValue());
        Assertions.assertEquals(24.0, savedAfterExtraPayment.getValue());

        Mockito.reset(orderRepository);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(savedAfterExtraPayment);

        //CashDiscount2 - cashDiscount = 0
        orderService.addCashDiscount("orderIdTest", "0");

        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order savedAfterCashDiscount2 = orderCaptor.getValue();

        Assertions.assertEquals(6.0, savedAfterCashDiscount2.getExtraPaymentValue());
        Assertions.assertEquals(0.0, savedAfterCashDiscount2.getCashDiscount());
        Assertions.assertEquals(2.4, savedAfterCashDiscount2.getPercentageDiscountCashValue());
        Assertions.assertEquals(27.6, savedAfterCashDiscount2.getValue());

        Mockito.reset(orderRepository);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(savedAfterCashDiscount2);
        Mockito.when(userRepository.findByUserId("userIdTest")).thenReturn(user);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(24.0);
        Mockito.when(orderItemRepository.getOrderItemsValueForUserAndOrder("orderIdTest", "userIdTest")).thenReturn(24.0);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        UserInfo userInfo = userInfoService.getUserInfo("userIdTest", "orderIdTest");

        Assertions.assertEquals(27.6, userInfo.getAmountToPay());
    }
}
