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
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setExtraPaymentValue(0.0);
        order.setNetValue(24.0);
        order.setValue(24.0);
        order.setPercentageDiscount(0.0);
        order.setCashDiscount(0.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setBaseForPercentageDiscount(24.0);
        userOrderPayment.setAmountToPay(24.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setDiscountValueInCash(0.0);

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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userOrderPaymentIdTest" ,capturedUserOrderPayments.get(0).getUserOrderPaymentId());
        Assertions.assertEquals(48.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());

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
        order.setExtraPaymentValue(0.0);
        order.setNetValue(24.0);
        order.setValue(24.0);
        order.setPercentageDiscount(0.0);
        order.setCashDiscount(0.0);

        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();


        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(24.0);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(userRepository.findByUserId("userIdTest")).thenReturn(user);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(false);

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        //order.setValue(50.0);
        //50
        order.setNetValue(24.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(0.0);
        order.setExtraPaymentValue(10.0);
        order.setPercentageDiscountCashValue(0.0);
        order.setValue(34.0);



        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setExtraPaymentValue(10.0);
        userOrderPayment.setAmountToPay(34.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(48.0);

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(58.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());
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
        order.setNetValue(24.0);
        order.setExtraPaymentValue(10.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(0.0);
        order.setPercentageDiscountCashValue(0.0);
        order.setValue(31.6);



        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountValueInCash(10.0);
        userOrderPayment.setExtraPaymentValue(10.0);
        userOrderPayment.setGeneralDiscountValue(2.4);
        userOrderPayment.setAmountToPay(31.6);
        userOrderPayment.setBaseForPercentageDiscount(24.0);

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


        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getExtraPaymentValue());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(48.0, capturedUserOrderPayments.get(0).getAmountToPay());
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
        order.setNetValue(24.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(10.0);
        order.setPercentageDiscountCashValue(2.4);
        order.setExtraPaymentValue(5.0);
        order.setValue(26.6);


        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(10.0);
        userOrderPayment.setDiscountInPercentageInCash(2.4);
        userOrderPayment.setExtraPaymentValue(5.0);
        userOrderPayment.setGeneralDiscountValue(2.4);
        userOrderPayment.setBaseForPercentageDiscount(24.0);
        userOrderPayment.setMenuItemsValue(24.0);
        userOrderPayment.setAmountToPay(26.6);

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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");

        order.setNetValue(24.0);
        order.setCashDiscount(0.0);
        order.setExtraPaymentValue(0.0);
        order.setPercentageDiscount(10.0);
        order.setValue(21.6);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(21.6);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(10.0);
        userOrderPayment.setDiscountInPercentageInCash(2.4);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(2.4);
        userOrderPayment.setBaseForPercentageDiscount(24.0);


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

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


        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        //order.setValue(50.0);
        //50
        order.setNetValue(24.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(10.0);
        order.setExtraPaymentValue(0.0);
        order.setPercentageDiscountCashValue(1.4);
        order.setValue(12.6);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountValueInCash(10.0);
        userOrderPayment.setDiscountInPercentage(1.4);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(11.4);////@@@@@
        userOrderPayment.setAmountToPay(12.6);
        userOrderPayment.setBaseForPercentageDiscount(38.0);


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu
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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");

        order.setNetValue(24.0);
        order.setCashDiscount(4.0);
        order.setExtraPaymentValue(0.0);
        order.setPercentageDiscount(0.0);
        order.setValue(20.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountValueInCash(4.0);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(4.0);////@@@@@
        userOrderPayment.setBaseForPercentageDiscount(24.0);
        userOrderPayment.setAmountToPay(20.0);


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 48.0);
        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

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


        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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


    //2 users

    //adding item A of user A, adding percentage discount and adding item B of user B
    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithPercentageDiscountFor2UsersTest(){
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
        userOrderPayment.setMenuItemsValue(50.0);
        userOrderPayment.setGeneralDiscountValue(5.0);////@@@@@

        UserOrderInfo userOrderInfo2 = new UserOrderInfo("orderIdTest", "userIdTest2", "userFirstNameTest2", "userLastNameTest2", 35.0);

        //50 - to wartość z orderValue,
        // a 35 - wartość nowododanego produktu

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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        order.setNetValue(24.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(0.0);
        order.setValue(14.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user1);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setDiscountInPercentageInCash(0.0);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setDiscountValueInCash(10.0);
        userOrderPayment.setMenuItemsValue(24.0);
        userOrderPayment.setGeneralDiscountValue(10.0);////@@@@@
        userOrderPayment.setAmountToPay(14.0);

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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        order.setNetValue(24.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(10.0);
        order.setExtraPaymentValue(0.0);
        order.setValue(12.6);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user1);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountValueInCash(10.0);
        userOrderPayment.setDiscountInPercentage(10.0);
        userOrderPayment.setDiscountInPercentageInCash(1.4);
        userOrderPayment.setExtraPaymentValue(0.0);
        userOrderPayment.setGeneralDiscountValue(11.4);
        userOrderPayment.setMenuItemsValue(24.0);
        userOrderPayment.setAmountToPay(12.6);

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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        order.setExtraPaymentValue(20.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user1);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(65.0);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setDiscountInPercentage(10.0);
        userOrderPayment.setDiscountInPercentageInCash(5.0);
        userOrderPayment.setExtraPaymentValue(20.0);
        userOrderPayment.setGeneralDiscountValue(5.0);
        userOrderPayment.setMenuItemsValue(50.0);

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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest2");

        Mockito.verify(userOrderPaymentRepository).saveAll(paymentCaptor.capture());

        List<UserOrderPayment> capturedUserOrderPayments = paymentCaptor.getValue();

        Assertions.assertEquals(2, capturedUserOrderPayments.size());

        Assertions.assertEquals("userIdTest2", capturedUserOrderPayments.get(1).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(1).getOrder().getOrderId());
        Assertions.assertEquals(41.5, capturedUserOrderPayments.get(1).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(1).getDiscountValueInCash());
        Assertions.assertEquals(35.0, capturedUserOrderPayments.get(1).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(1).getDiscountInPercentage());
        Assertions.assertEquals(3.50, capturedUserOrderPayments.get(1).getDiscountInPercentageInCash());
        Assertions.assertEquals(3.5, capturedUserOrderPayments.get(1).getGeneralDiscountValue());
        Assertions.assertEquals(10.0, userOrderPayments.get(1).getExtraPaymentValue());

        Assertions.assertEquals("userIdTest", capturedUserOrderPayments.get(0).getUser().getUserId());
        Assertions.assertEquals("orderIdTest", capturedUserOrderPayments.get(0).getOrder().getOrderId());
        Assertions.assertEquals(55.0, capturedUserOrderPayments.get(0).getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayments.get(0).getDiscountValueInCash());
        Assertions.assertEquals(50.0, capturedUserOrderPayments.get(0).getBaseForPercentageDiscount());
        Assertions.assertEquals(10.0, capturedUserOrderPayments.get(0).getDiscountInPercentage());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getDiscountInPercentageInCash());
        Assertions.assertEquals(5.0, capturedUserOrderPayments.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(10.0, userOrderPayments.get(0).getExtraPaymentValue());
    }

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenFirstExistsWithCashDiscountAndExtraPaymentFor2UsersTest() {
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
        order.setExtraPaymentValue(8.0);
        order.setCashDiscount(10.0);
        order.setPercentageDiscount(0.0);
        order.setValue(22.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user1);
        userOrderPayment.setOrder(order);
        userOrderPayment.setDiscountValueInCash(10.0);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setDiscountInPercentageInCash(0.0);
        userOrderPayment.setExtraPaymentValue(8.0);
        userOrderPayment.setMenuItemsValue(24.0);
        userOrderPayment.setGeneralDiscountValue(10.0);
        userOrderPayment.setAmountToPay(22.0);

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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        order.setNetValue(50.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(0.0);
        order.setExtraPaymentValue(10.0);
        order.setValue(60.0);

        UserOrderPayment userOrderPayment = new UserOrderPayment();
        userOrderPayment.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment.setUser(user1);
        userOrderPayment.setOrder(order);
        userOrderPayment.setAmountToPay(60.0);
        userOrderPayment.setDiscountValueInCash(0.0);
        userOrderPayment.setBaseForPercentageDiscount(50.0);
        userOrderPayment.setDiscountInPercentage(0.0);
        userOrderPayment.setDiscountInPercentageInCash(0.0);
        userOrderPayment.setExtraPaymentValue(10.0);
        userOrderPayment.setMenuItemsValue(50.0);
        userOrderPayment.setGeneralDiscountValue(0.0);

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

        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
        User user1 = new User();
        user1.setUserId("userIdTest1");
        user1.setFirstName("userFirstNameTest1");
        user1.setLastName("userLastNameTest1");

        User user2 = new User();
        user2.setUserId("userIdTest2");
        user2.setFirstName("userFirstNameTest2");
        user2.setLastName("userLastNameTest2");

        User user3 = new User();
        user3.setUserId("userIdTest3");
        user3.setFirstName("userFirstNameTest3");
        user3.setLastName("userLastNameTest3");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setValue(50.40);
        order.setNetValue(48.0);
        order.setCashDiscount(10.0);
        //order.setCashDiscount(12.0);
        order.setPercentageDiscount(10.0);
        order.setExtraPaymentValue(18.0);

        UserOrderPayment userOrderPayment1 = new UserOrderPayment();
        userOrderPayment1.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment1.setUser(user1);
        userOrderPayment1.setOrder(order);
        userOrderPayment1.setAmountToPay(25.20);
        userOrderPayment1.setDiscountValueInCash(6.0);
        userOrderPayment1.setDiscountInPercentage(10.0);
        userOrderPayment1.setDiscountInPercentageInCash(1.8);
        userOrderPayment1.setExtraPaymentValue(9.0);
        //userOrderPayment1.setBaseForPercentageDiscount(19.0);
        userOrderPayment1.setGeneralDiscountValue(7.80);////@@@@@
        userOrderPayment1.setMenuItemsValue(24.0);

        UserOrderPayment userOrderPayment2 = new UserOrderPayment();
        userOrderPayment2.setUserOrderPaymentId("userOrderPaymentIdTest");
        userOrderPayment2.setUser(user2);
        userOrderPayment2.setOrder(order);
        userOrderPayment2.setAmountToPay(25.20);
        userOrderPayment2.setDiscountValueInCash(6.0);
        userOrderPayment2.setDiscountInPercentage(10.0);
        userOrderPayment2.setBaseForPercentageDiscount(19.0);
        userOrderPayment2.setDiscountInPercentageInCash(1.8);
        userOrderPayment2.setExtraPaymentValue(9.0);
        userOrderPayment2.setGeneralDiscountValue(7.80);////@@@@@
        userOrderPayment2.setMenuItemsValue(24.0);

        UserOrderInfo userOrderInfo3 = new UserOrderInfo("orderIdTest", "userIdTest3", "userFirstNameTest3", "userLastNameTest3", 24.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment1);
        userOrderPayments.add(userOrderPayment2);


        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest3")).thenReturn(userOrderInfo3);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(3);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(10.0);
        //Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(12.0);
        Mockito.when(orderItemRepository.getOrderItemsValueForOrder("orderIdTest")).thenReturn(72.0);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest3")).thenReturn(false);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userRepository.findByUserId("userIdTest3")).thenReturn(user3);


        ArgumentCaptor<List<UserOrderPayment>> paymentCaptor = ArgumentCaptor.forClass(List.class);

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
}
