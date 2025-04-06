package pl.dskimina.foodsy.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.UserOrderPayment;
import pl.dskimina.foodsy.entity.data.UserOrderInfo;
import pl.dskimina.foodsy.repository.*;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class UserOrderPaymentServiceTest {

    @InjectMocks
    private UserOrderPaymentService userOrderPaymentService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserOrderPaymentRepository userOrderPaymentRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Captor
    private ArgumentCaptor<List<UserOrderPayment>> paymentCaptor;

    private User createTestUser(String userId, String firstName, String lastName){
        User user = new User();
        user.setUserId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }
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

    private UserOrderPayment createTestUserOrderPayment(String userOrderPaymentId, User user, Order order, double menuItemsValue,
                                                        double discountValueInCash, double discountInPercentage,
                                                        double discountInPercentageInCash, double baseForPercentageDiscount,
                                                        double extraPaymentValue, double generalDiscountValue, double amountToPay){
        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId(userOrderPaymentId);
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(amountToPay);
        userOrderPayment.setDiscountValueInCash(discountValueInCash);
        userOrderPayment.setDiscountInPercentage(discountInPercentage);
        userOrderPayment.setBaseForPercentageDiscount(baseForPercentageDiscount);
        userOrderPayment.setDiscountInPercentageInCash(discountInPercentageInCash);
        userOrderPayment.setExtraPaymentValue(extraPaymentValue);
        userOrderPayment.setGeneralDiscountValue(generalDiscountValue);
        userOrderPayment.setMenuItemsValue(menuItemsValue);
        return userOrderPayment;
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithNoExtraPaymentsTest(){
        User user = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        Order order = createTestOrder(24.0, 0.0, 0.0, 0.0, 0.0, 24.0);


        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user, order,
                24.0, 0.0, 0.0, 0.0, 24.0, 0.0, 0.0, 24.0);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest" )).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(24.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userOrderPaymentIdTest" ,capturedUserOrderPayments.get(0).getUserOrderPaymentId());
        Assertions.assertEquals(48.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());

    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenNotExistsWithNoExtraPaymentsTest(){
        User user = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        Order order = createTestOrder(24.0, 0.0, 0.0, 0.0, 0.0, 24.0);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(24.0);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(userRepository.findByUserId("userIdTest")).thenReturn(user);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(false);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(24.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());
    }


    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithExtraPaymentsTest(){
        User user = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        Order order = createTestOrder(24.0, 0.0, 0.0, 0.0, 10.0, 34.0);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);

        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user, order,
                24.0, 0.0, 0.0, 0.0, 24.0, 10.0, 0.0, 34.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(58.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithCashDiscountAndExtraPaymentsTest(){
        User user = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");

        Order order = createTestOrder(24.0, 0.0, 0.0, 10.0, 10.0, 31.6);
        order.setOrderId("orderIdTest");
        order.setNetValue(24.0);
        order.setExtraPaymentValue(10.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(0.0);
        order.setPercentageDiscountCashValue(0.0);
        order.setValue(24.0);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0); //48 - value for user after adding the second item

        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user, order,
                24.0, 10.0, 0.0, 0.0, 14.0, 10.0, 10.0, 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(38.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(48.0, capturedUserOrderPayments.get(0).getAmountToPay());
    }


    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithPercentageDiscountAndExtraPaymentsTest(){
        User user = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        Order order = createTestOrder(24.0, 10.0, 2.4, 0.0, 5.0, 26.6);

        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user, order,
                24.0, 0.0, 10.0, 2.4, 24.0, 5.0, 2.4, 26.4);


        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(48.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(4.8, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(4.8, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(48.2, capturedUserOrderPayments.get(0).getAmountToPay());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithPercentageDiscountTest(){
        User user = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        Order order = createTestOrder(24.0, 10.0, 2.4, 0.0, 0.0, 21.6);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user, order,
                24.0, 0.0, 10.0, 2.4, 24.0, 0.0, 2.4, 21.6);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(4.8, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(43.2, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(48.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(4.8, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithPercentageDiscountAndCashDiscountTest(){
        User user = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        Order order = createTestOrder(24.0, 10.0, 1.4, 10.0, 0.0, 12.6);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user, order,
                24.0, 10.0, 10.0, 1.4, 14.0, 0.0, 11.4, 12.6);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);

        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(1, capturedUserOrderPayments.size());
        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(34.2, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(38.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(3.8, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(34.2, capturedUserOrderPayments.get(0).getAmountToPayWithoutExtraPayment());
        Assertions.assertEquals(13.8, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithCashDiscountTest() {
        User user = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        Order order = createTestOrder(24.0, 0.0, 0.0, 4.0, 0.0, 20.0);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user, order,
                24.0, 4.0, 0.0, 0.0, 20.0, 0.0, 4.0, 20.0);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(4.0);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(44.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(4.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(44.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(4.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithPercentageDiscountFor2UsersTest(){
        User user1 = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        User user2 = createTestUser("userIdTest2", "userFirstNameTest2", "userLastNameTest2");
        Order order = createTestOrder(50.0, 10.0, 5.0, 0.0, 0.0, 45.0);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user1, order,
                50.0, 0.0, 10.0, 5.0, 50.0, 5.0, 5.0, 45.0);

        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 35.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest2")).thenReturn(userOrderInfo2);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest2")).thenReturn(false);
        Mockito.when(userRepository.findByUserId("userIdTest2")).thenReturn(user2);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(85.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(2, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest2" ,capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(31.5, capturedUserOrderPayments.get(1).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(1).getDiscountInPercentage());
        Assertions.assertEquals(35.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(3.5, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(3.5, capturedUserOrderPayments.get(1).getGeneralDiscountValue());

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(45.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(50.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
    }


    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithCashDiscountFor2UsersTest(){
        User user1 = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        User user2 = createTestUser("userIdTest2", "userFirstNameTest2", "userLastNameTest2");
        Order order = createTestOrder( 24.0, 0.0, 0.0, 10.0, 0.0, 14.0);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user1, order,
                24.0, 10.0, 0.0, 0.0, 14.0, 0.0, 10.0, 14.0);

        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest2")).thenReturn(userOrderInfo2);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest2")).thenReturn(false);
        Mockito.when(userRepository.findByUserId("userIdTest2")).thenReturn(user2);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(2, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest2" ,capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(19.0, capturedUserOrderPayments.get(1).getAmountToPay());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(19.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(1).getGeneralDiscountValue());

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(19.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(19.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());

    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithPercentageDiscountAndCashDiscountFor2UsersTest() {
        User user1 = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        User user2 = createTestUser("userIdTest2", "userFirstNameTest2", "userLastNameTest2");
        Order order = createTestOrder(24.0, 10.0, 1.4, 10.0, 0.0, 12.6);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user1, order,
                24.0, 10.0, 10.0, 1.4, 14.0, 5.0, 11.4, 12.6);

        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest2")).thenReturn(userOrderInfo2);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest2")).thenReturn(false);
        Mockito.when(userRepository.findByUserId("userIdTest2")).thenReturn(user2);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(2, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest2", capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(19.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(1).getDiscountInPercentage());
        Assertions.assertEquals(1.9, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(6.9, capturedUserOrderPayments.get(1).getGeneralDiscountValue());
        Assertions.assertEquals(17.1, capturedUserOrderPayments.get(1).getAmountToPay());

        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(19.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getDiscountInPercentage());
        Assertions.assertEquals(1.9, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(6.9, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(17.1, capturedUserOrderPayments.get(0).getAmountToPay());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithPercentageDiscountAndExtraPaymentFor2UsersTest() {
        User user1 = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        User user2 = createTestUser("userIdTest2", "userFirstNameTest2", "userLastNameTest2");
        Order order = createTestOrder(50.0, 10.0, 5.0, 0.0, 20.0, 65.0);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user1, order,
                50.0, 0.0, 10.0, 5.0, 50.0, 20.0, 5.0, 65.0);
        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 35.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest2")).thenReturn(userOrderInfo2);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest2")).thenReturn(false);
        Mockito.when(userRepository.findByUserId("userIdTest2")).thenReturn(user2);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(85.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(2, capturedUserOrderPayments.size());


        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(55.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(50.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getDiscountInPercentage());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(10.0, userOrderPayments.get(0).getExtraPaymentValue());

        Assertions.assertEquals("userIdTest2", capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(41.5, capturedUserOrderPayments.get(1).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(35.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(1).getDiscountInPercentage());
        Assertions.assertEquals(3.50, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(3.5, capturedUserOrderPayments.get(1).getGeneralDiscountValue());
        Assertions.assertEquals(10.0, userOrderPayments.get(1).getExtraPaymentValue());

    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithCashDiscountAndExtraPaymentFor2UsersTest() {
        User user1 = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        User user2 = createTestUser("userIdTest2", "userFirstNameTest2", "userLastNameTest2");
        Order order = createTestOrder(24.0, 0.0, 0.0, 10.0, 8.0, 22.0);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user1, order,
                24.0, 10.0, 0.0, 0.0, 14.0, 8.0, 10.0, 22.0);
        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest2")).thenReturn(userOrderInfo2);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest2")).thenReturn(false);
        Mockito.when(userRepository.findByUserId("userIdTest2")).thenReturn(user2);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(2, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(19.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountInPercentage());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(4.0, userOrderPayments.get(0).getExtraPaymentValue());
        Assertions.assertEquals(23.0, capturedUserOrderPayments.get(0).getAmountToPay());

        Assertions.assertEquals("userIdTest2", capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(19.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountInPercentage());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(1).getGeneralDiscountValue());
        Assertions.assertEquals(4.0, userOrderPayments.get(1).getExtraPaymentValue());
        Assertions.assertEquals(23.0, capturedUserOrderPayments.get(1).getAmountToPay());


    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithExtraPaymentFor2UsersTest() {
        User user1 = createTestUser("userIdTest", "userFirstNameTest", "userLastNameTest");
        User user2 = createTestUser("userIdTest2", "userFirstNameTest2", "userLastNameTest2");
        Order order = createTestOrder(50.0, 0.0, 0.0, 0.0, 10.0, 60.0);
        UserOrderPayment userOrderPayment = createTestUserOrderPayment("userOrderPaymentIdTest", user1, order,
                50.0, 0.0, 0.0, 0.0, 50.0, 10.0, 0.0, 60.0);
        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 35.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest2")).thenReturn(userOrderInfo2);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest2")).thenReturn(false);
        Mockito.when(userRepository.findByUserId("userIdTest2")).thenReturn(user2);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(85.0);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(2, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest2", capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(35.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountInPercentage());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getGeneralDiscountValue());
        Assertions.assertEquals(5.0, userOrderPayments.get(1).getExtraPaymentValue());
        Assertions.assertEquals(40.0, capturedUserOrderPayments.get(1).getAmountToPay());

        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(50.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountInPercentage());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(5.0, userOrderPayments.get(0).getExtraPaymentValue());
        Assertions.assertEquals(55.0, capturedUserOrderPayments.get(0).getAmountToPay());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithPercentageDiscountAndCashDiscountAndExtraPaymentFor3UsersTest() {
        User user1 = createTestUser("userIdTest1", "userFirstNameTest1", "userLastNameTest1");
        User user2 = createTestUser("userIdTest2", "userFirstNameTest2", "userLastNameTest2");
        User user3 = createTestUser("userIdTest3", "userFirstNameTest3", "userLastNameTest3");
        Order order = createTestOrder(48.0, 10.0, 3.8, 10.0, 18.0, 52.2);
        UserOrderPayment userOrderPayment1 = createTestUserOrderPayment("userOrderPaymentIdTest", user1, order,
                24.0, 5.0, 10.0, 1.9, 19.0, 9.0, 6.9, 26.1);
        UserOrderPayment userOrderPayment2 = createTestUserOrderPayment("userOrderPaymentIdTest2", user2, order,
                24.0, 5.0, 10.0, 1.9, 19.0, 9.0, 6.9, 26.1);
        UserOrderInfo userOrderInfo3 = new UserOrderInfo("orderIdTest", "userIdTest3", "userFirstNameTest3", "userLastNameTest3", 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment1);
        userOrderPayments.add(userOrderPayment2);


        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest3")).thenReturn(userOrderInfo3);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(3);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(72.0);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest3")).thenReturn(false);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userRepository.findByUserId("userIdTest3")).thenReturn(user3);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest3");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(3, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest1", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(3.33, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(18.6, capturedUserOrderPayments.get(0).getAmountToPayWithoutExtraPayment());
        Assertions.assertEquals(24.6, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(20.67, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getDiscountInPercentage());
        Assertions.assertEquals(2.07, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.4, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(6.0, userOrderPayments.get(0).getExtraPaymentValue());

        Assertions.assertEquals("userIdTest3", capturedUserOrderPayments.get(2).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(2).getOrder().getOrderId());
        Assertions.assertEquals(24.6, capturedUserOrderPayments.get(2).getAmountToPay());
        Assertions.assertEquals(3.33 , capturedUserOrderPayments.get(2).getDiscountValueInCash());
        Assertions.assertEquals(20.67, capturedUserOrderPayments.get(2).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(2).getDiscountInPercentage());
        Assertions.assertEquals(2.07, capturedUserOrderPayments.get(2).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.4, capturedUserOrderPayments.get(2).getGeneralDiscountValue());
        Assertions.assertEquals(6.0, userOrderPayments.get(2).getExtraPaymentValue());

        Assertions.assertEquals("userIdTest2", capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(24.6, capturedUserOrderPayments.get(1).getAmountToPay());
        Assertions.assertEquals(3.33, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(20.67, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(1).getDiscountInPercentage());
        Assertions.assertEquals(2.07, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.4, capturedUserOrderPayments.get(1).getGeneralDiscountValue());
        Assertions.assertEquals(6.0, userOrderPayments.get(1).getExtraPaymentValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithPercentageDiscountAndExtraPaymentFor3UsersTest() {
        User user1 = createTestUser("userIdTest1", "userFirstNameTest1", "userLastNameTest1");
        User user2 = createTestUser("userIdTest2", "userFirstNameTest2", "userLastNameTest2");
        User user3 = createTestUser("userIdTest3", "userFirstNameTest3", "userLastNameTest3");
        Order order = createTestOrder(48.0, 15.0, 7.2, 0.0, 19.0, 52.2);
        UserOrderPayment userOrderPayment1 = createTestUserOrderPayment("userOrderPaymentIdTest", user1, order,
                24.0, 0.0, 15.0, 3.6, 24.0, 9.5, 3.6, 29.9);
        UserOrderPayment userOrderPayment2 = createTestUserOrderPayment("userOrderPaymentIdTest2", user2, order,
                24.0, 0.0, 15.0, 3.6, 24.0, 9.5, 3.6, 29.9);
        UserOrderInfo userOrderInfo3 = new UserOrderInfo("orderIdTest", "userIdTest3", "userFirstNameTest3", "userLastNameTest3", 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment1);
        userOrderPayments.add(userOrderPayment2);


        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest3")).thenReturn(userOrderInfo3);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(3);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(72.0);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest3")).thenReturn(false);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userRepository.findByUserId("userIdTest3")).thenReturn(user3);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest3");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(3, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest1", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(15.0, capturedUserOrderPayments.get(0).getDiscountInPercentage());
        Assertions.assertEquals(24.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(3.6, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(3.6, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(20.4, capturedUserOrderPayments.get(0).getAmountToPayWithoutExtraPayment());
        Assertions.assertEquals(26.73, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(6.33, userOrderPayments.get(0).getExtraPaymentValue());

        Assertions.assertEquals("userIdTest3", capturedUserOrderPayments.get(2).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(2).getOrder().getOrderId());
        Assertions.assertEquals(0.0 , capturedUserOrderPayments.get(2).getDiscountValueInCash());
        Assertions.assertEquals(24.0, capturedUserOrderPayments.get(2).getBaseForPercentageDiscount());
        Assertions.assertEquals(15.0, capturedUserOrderPayments.get(2).getDiscountInPercentage());
        Assertions.assertEquals(3.6, capturedUserOrderPayments.get(2).getDiscountInPercentageInCash());
        Assertions.assertEquals(3.6, capturedUserOrderPayments.get(2).getGeneralDiscountValue());
        Assertions.assertEquals(26.73, capturedUserOrderPayments.get(2).getAmountToPay());
        Assertions.assertEquals(6.33, userOrderPayments.get(2).getExtraPaymentValue());

        Assertions.assertEquals("userIdTest2", capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(26.73, capturedUserOrderPayments.get(1).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(24.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(15.0, capturedUserOrderPayments.get(1).getDiscountInPercentage());
        Assertions.assertEquals(3.6, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(3.6, capturedUserOrderPayments.get(1).getGeneralDiscountValue());
        Assertions.assertEquals(6.33, userOrderPayments.get(1).getExtraPaymentValue());
    }
}
