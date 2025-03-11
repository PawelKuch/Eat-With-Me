package pl.dskimina.foodsy.service;

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

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);


        UserOrderInfo userOrderInfo = new UserOrderInfo("orderIdTest", "userIdTest", "userFirstNameTest", "userLastNameTest", 100.0);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(0.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest" )).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(orderRepository).getUsersAmountForOrder("orderIdTest");
        Mockito.verify(orderItemRepository).getUserOrderInfo("orderIdTest","userIdTest");
        Mockito.verify(extraPaymentRepository).getExtraPaymentsValueForOrder("orderIdTest");
        Mockito.verify(userOrderPaymentRepository).findByOrderOrderId("orderIdTest");
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

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        userOrderPaymentService.addUserOrderPaymentInfoForOrderIdAndUserId("orderIdTest", "userIdTest");

        Mockito.verify(orderRepository).getUsersAmountForOrder("orderIdTest");
        Mockito.verify(orderItemRepository).getUserOrderInfo("orderIdTest","userIdTest");
        Mockito.verify(extraPaymentRepository).getExtraPaymentsValueForOrder("orderIdTest");
        Mockito.verify(userOrderPaymentRepository).existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest");
        Mockito.verify(userOrderPaymentRepository).save(paymentCaptor.capture());

        UserOrderPayment capturedUserOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userIdTest" ,capturedUserOrderPayment.getUser().getUserId());
        Assertions.assertEquals("orderIdTest" ,capturedUserOrderPayment.getOrder().getOrderId());
        Assertions.assertEquals(0.0, capturedUserOrderPayment.getAmountToPay());
        Assertions.assertEquals(0.0, capturedUserOrderPayment.getExtraPaymentValue());
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
        userOrderPayment.setAmountToPay(50.0);
        userOrderPayment.setExtraPaymentValue(0.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
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

    @Test
    public void addUserOrderPaymentInfoForOrderIdAndUserIdWhenExistsWithWithCashDiscountAndExtraPaymentsTest(){
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


        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(userOrderPayment);

        Mockito.when(orderRepository.getUsersAmountForOrder("orderIdTest")).thenReturn(1);
        Mockito.when(orderItemRepository.getUserOrderInfo("orderIdTest", "userIdTest")).thenReturn(userOrderInfo);
        Mockito.when(extraPaymentRepository.getExtraPaymentsValueForOrder("orderIdTest")).thenReturn(10.0);
        Mockito.when(orderRepository.getCashDiscountForOrder("orderIdTest")).thenReturn(20.0);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(userOrderPayment);
        Mockito.when(userOrderPaymentRepository.existsByOrderOrderIdAndUserUserId("orderIdTest", "userIdTest")).thenReturn(true);

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
}
