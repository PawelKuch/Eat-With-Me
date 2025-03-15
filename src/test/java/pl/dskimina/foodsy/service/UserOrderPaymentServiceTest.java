package pl.dskimina.foodsy.service;

import org.assertj.core.api.AbstractAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.UserOrderPayment;
import pl.dskimina.foodsy.entity.data.UserOrderInfo;
import pl.dskimina.foodsy.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@ExtendWith(MockitoExtension.class)
public class UserOrderPaymentServiceTest {

    @InjectMocks
    private UserOrderPaymentService userOrderPaymentService;

    @Mock
    private UserService userService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private ExtraPaymentRepository extraPaymentRepository;

    @Mock
    private ToDataService toDataService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserOrderPaymentRepository userOrderPaymentRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithNoExtraPaymentsTest(){
        UserOrderPayment userOrderPayment = getUserOrderPayment();
        userOrderPayment.setDiscountValueInCash(0.0);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 100.0);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest" )).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(orderRepository).getUsersAmountForOrder("orderIdTest");
        Mockito.verify(orderItemRepository).getUserOrderInfo("orderIdTest","userIdTest");
        Mockito.verify(extraPaymentRepository).getExtraPaymentsValueForOrder("orderIdTest");
        Mockito.verify(userOrderPaymentRepository).existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest");
        Mockito.verify(userOrderPaymentRepository).findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest");
        Mockito.verify(userOrderPaymentRepository).save(paymentCaptor.capture());

        UserOrderPayment capturedUserOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userOrderPaymentIdTest" ,capturedUserOrderPayment.getUserOrderPaymentId());
        Assertions.assertEquals(100.0, capturedUserOrderPayment.getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayment.getExtraPaymentValue());

    }

    //addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithExtraPaymentsTest
    //addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithCashDiscountTest
    //addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithPercentageDiscountTest


    private static UserOrderPayment getUserOrderPayment() {
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setValue(100.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(100.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        return userOrderPayment;
    }


    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenNotExistsWithNoExtraPaymentsTest(){
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setValue(0.0);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 0.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(userRepository.findByUserId("userIdTest")).thenReturn(user);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(false);

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(orderRepository).getUsersAmountForOrder("orderIdTest");
        Mockito.verify(orderItemRepository).getUserOrderInfo("orderIdTest","userIdTest");
        Mockito.verify(extraPaymentRepository).getExtraPaymentsValueForOrder("orderIdTest");
        Mockito.verify(userOrderPaymentRepository).existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest");
        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithExtraPaymentsTest(){
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setValue(50.0);


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 85.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setAmountToPay(50.0);
        userOrderPayment.setExtraPaymentValue(0.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).save(paymentCaptor.capture());

        UserOrderPayment capturedUserOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayment.getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayment.getOrder().getOrderId());
        Assertions.assertEquals(95.0, capturedUserOrderPayment.getAmountToPay());
        Assertions.assertEquals(10.0, capturedUserOrderPayment.getExtraPaymentValue());
    }



    //add orderItem, add extrapayment, add discount and add orderItem
    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithCashDiscountAndExtraPaymentsTest(){
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        //order.setValue(50.0);
        //50 - 20 = 30
        order.setValue(30.0);
        order.setCashDiscount(20.0);



        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 85.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(50.0);
        userOrderPayment.setDiscountValueInCash(20.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(20.0);


        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(20.0);


        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).save(paymentCaptor.capture());

        UserOrderPayment capturedUserOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayment.getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayment.getOrder().getOrderId());
        Assertions.assertEquals(75.0, capturedUserOrderPayment.getAmountToPay());
        Assertions.assertEquals(10.0, capturedUserOrderPayment.getExtraPaymentValue());
        Assertions.assertEquals(20.0, capturedUserOrderPayment.getDiscountValueInCash());
        Assertions.assertEquals(20.0, capturedUserOrderPayment.getGeneralDiscountValue());
    }

    //add orderItem, add extrapayment, add discount and add orderItem
    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithPercentageDiscountAndExtraPaymentsTest(){
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        //order.setValue(50.0);
        //50
        order.setValue(50.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(10.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(50.0);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(10.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(0.0);////@@@@@


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 85.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.getPercentageDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).save(paymentCaptor.capture());

        UserOrderPayment capturedUserOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayment.getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayment.getOrder().getOrderId());
        Assertions.assertEquals(86.5, capturedUserOrderPayment.getAmountToPay());
        Assertions.assertEquals(10.0, capturedUserOrderPayment.getExtraPaymentValue());
        Assertions.assertEquals(0.0, capturedUserOrderPayment.getDiscountValueInCash());
        Assertions.assertEquals(85.0, capturedUserOrderPayment.getBaseForPercentageDiscount());
        Assertions.assertEquals(8.5, capturedUserOrderPayment.getDiscountInPercentageInCash());
        Assertions.assertEquals(8.5, capturedUserOrderPayment.getGeneralDiscountValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithPercentageDiscountTest(){
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        //order.setValue(50.0);
        //50
        order.setValue(50.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(10.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(50.0);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(10.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(0.0);////@@@@@


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 85.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu


        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.getPercentageDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).save(paymentCaptor.capture());

        UserOrderPayment capturedUserOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayment.getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayment.getOrder().getOrderId());
        Assertions.assertEquals(76.5, capturedUserOrderPayment.getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayment.getDiscountValueInCash());
        Assertions.assertEquals(85.0, capturedUserOrderPayment.getBaseForPercentageDiscount());
        Assertions.assertEquals(8.5, capturedUserOrderPayment.getDiscountInPercentageInCash());
        Assertions.assertEquals(8.5, capturedUserOrderPayment.getGeneralDiscountValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithPercentageDiscountAndCashDiscountTest(){
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        //order.setValue(50.0);
        //50
        order.setValue(50.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(10.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(50.0);
        userOrderPayment.setDiscountValueInCash(10.0);
        userOrderPayment.setDiscountInPercentage(10.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(0.0);////@@@@@


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 85.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu


        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(orderRepository.getPercentageDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).save(paymentCaptor.capture());

        UserOrderPayment capturedUserOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayment.getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayment.getOrder().getOrderId());
        Assertions.assertEquals(66.5, capturedUserOrderPayment.getAmountToPay());
        Assertions.assertEquals(10.0, capturedUserOrderPayment.getDiscountValueInCash());
        Assertions.assertEquals(85.0, capturedUserOrderPayment.getBaseForPercentageDiscount());
        Assertions.assertEquals(8.5, capturedUserOrderPayment.getDiscountInPercentageInCash());
        Assertions.assertEquals(18.5, capturedUserOrderPayment.getGeneralDiscountValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithCashDiscountTest() {
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        //order.setValue(50.0);
        //50
        order.setValue(50.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(10.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(50.0);
        userOrderPayment.setDiscountValueInCash(10.0);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(0.0);////@@@@@


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 85.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu


        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(orderRepository.getPercentageDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).save(paymentCaptor.capture());

        UserOrderPayment capturedUserOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest", capturedUserOrderPayment.getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayment.getOrder().getOrderId());
        Assertions.assertEquals(75.0, capturedUserOrderPayment.getAmountToPay());
        Assertions.assertEquals(10.0, capturedUserOrderPayment.getDiscountValueInCash());
        Assertions.assertEquals(85.0, capturedUserOrderPayment.getBaseForPercentageDiscount());
        Assertions.assertEquals(0.0, capturedUserOrderPayment.getDiscountInPercentageInCash());
        Assertions.assertEquals(10.0, capturedUserOrderPayment.getGeneralDiscountValue());
    }


    //2 users

    //adding item A of user A, adding percentage discount and adding item B of user B
    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithPercentageDiscountFor2UsersTest(){
        User user1 = new User();
        user1.setUserId("userIdTest");
        user1.setFirstName("userFirstNameTest");
        user1.setLastName("userLastNameTest");

        User user2 = new User();
        user2.setUserId("userIdTest2");
        user2.setFirstName("userFirstNameTest2");
        user2.setLastName("userLastNameTest2");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setValue(50.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(10.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user1);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(45.0);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(10.0);
        userOrderPayment.setDiscountInPercentageInCash(5.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(5.0);////@@@@@


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 50.0);
        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 35.0);

        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest2")).thenReturn(userOrderInfo2);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.getPercentageDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest2")).thenReturn(false);
        Mockito.when(userRepository.findByUserId("userIdTest2")).thenReturn(user2);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        /*UserOrderPayment userPayment2 = capturedUserOrderPayments.stream()
                .filter(uop -> uop.getUser().getUserId().equals("userIdTest2"))
                .findFirst().orElseThrow();*/

        //Assertions.assertEquals("userIdTest2", userPayment2.getUser().getUserId());

        Assertions.assertEquals(2, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest2" ,capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(31.5, capturedUserOrderPayments.get(1).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
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
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithCashDiscountFor2UsersTest(){
        User user1 = new User();
        user1.setUserId("userIdTest");
        user1.setFirstName("userFirstNameTest");
        user1.setLastName("userLastNameTest");

        User user2 = new User();
        user2.setUserId("userIdTest2");
        user2.setFirstName("userFirstNameTest2");
        user2.setLastName("userLastNameTest2");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setValue(50.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(0.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user1);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(40.0);
        userOrderPayment.setDiscountValueInCash(10.0);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setDiscountInPercentageInCash(0.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(10.0);////@@@@@


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 50.0);
        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 35.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(2);

        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest2")).thenReturn(userOrderInfo2);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(orderRepository.getPercentageDiscountForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest2")).thenReturn(false);
        Mockito.when(userRepository.findByUserId("userIdTest2")).thenReturn(user2);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(2, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest2" ,capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(30.0, capturedUserOrderPayments.get(1).getAmountToPay());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(35.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(1).getGeneralDiscountValue());

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(45.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(50.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());

    }
}
