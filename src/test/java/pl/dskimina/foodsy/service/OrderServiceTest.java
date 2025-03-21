package pl.dskimina.foodsy.service;


import ch.qos.logback.core.testUtil.MockInitialContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.UserOrderPayment;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ToDataService toDataService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private UserService userService;

    @Mock
    private UserOrderPaymentRepository userOrderPaymentRepository;

    @Mock
    private UserOrderPaymentService userOrderPaymentService;

    @Mock
    private ExtraPaymentRepository extraPaymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    public void createOrderTest(){
        String restaurantId = "restaurantIdTest";
        String userId = "userIdTest";
        String closingDate = "1999-01-17T22:22";
        String minOrderValue = "100";
        String description = "descriptionTest";

        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId("restaurantIdTest");
        restaurant.setName("restaurantNameTest");
        restaurant.setEmail("restaurantEmailTest");

        Mockito.when(userRepository.findByUserId("userIdTest")).thenReturn(user);
        Mockito.when(restaurantRepository.findByRestaurantId("restaurantIdTest")).thenReturn(restaurant);

        orderService.createOrder(restaurantId, userId, closingDate, minOrderValue, description);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Mockito.verify(userRepository, Mockito.times(1)).findByUserId("userIdTest");
        verify(restaurantRepository, Mockito.times(1)).findByRestaurantId("restaurantIdTest");
        verify(orderRepository, Mockito.times(1)).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals("userIdTest", capturedOrder.getOwner().getUserId());
        Assertions.assertEquals("userFirstNameTest", capturedOrder.getOwner().getFirstName());
        Assertions.assertEquals("userLastNameTest", capturedOrder.getOwner().getLastName());

        Assertions.assertEquals("restaurantIdTest", capturedOrder.getRestaurant().getRestaurantId());
        Assertions.assertEquals("restaurantNameTest", capturedOrder.getRestaurant().getName());
        Assertions.assertEquals("restaurantEmailTest", capturedOrder.getRestaurant().getEmail());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime closingDateTime = LocalDateTime.parse(closingDate, formatter);

        Assertions.assertEquals(closingDateTime, capturedOrder.getClosingDate());
        Assertions.assertEquals(100.0, capturedOrder.getMinValue());
        Assertions.assertEquals("descriptionTest", capturedOrder.getDescription());
    }

    @Test
    public void addPercentageDiscountWithoutAnyDiscountTest(){
        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setValue(50.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(0.0);
        order.setPercentageDiscountCashValue(0.0);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        orderService.addPercentageDiscount("orderIdTest", "10");

        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals("orderIdTest", capturedOrder.getOrderId());
        Assertions.assertEquals(10.0, capturedOrder.getPercentageDiscount());
        Assertions.assertEquals(5.0, capturedOrder.getPercentageDiscountCashValue());
        Assertions.assertEquals(45.0, capturedOrder.getValue());
    }

    @Test
    public void addPercentageDiscountWithCurrentCashDiscountTest(){
        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setNetValue(100.0);
        order.setCashDiscount(20.0);
        order.setPercentageDiscount(0.0);
        order.setPercentageDiscountCashValue(0.0);
        order.setExtraPaymentValue(0.0);
        order.setValue(80.0);

        UserOrderPayment uop = new UserOrderPayment();
        uop.setUserOrderPaymentId("uopIdTest");
        uop.setExtraPaymentValue(0.0);
        uop.setDiscountValueInCash(20.0);
        uop.setDiscountInPercentageInCash(0.0);
        uop.setBaseForPercentageDiscount(80.0);
        uop.setGeneralDiscountValue(20.0);
        uop.setAmountToPay(80.0);
        uop.setAmountToPayWithoutExtraPayment(80.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(uop);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);

        ArgumentCaptor<List<UserOrderPayment>> userOrderPaymentCaptor = ArgumentCaptor.forClass(List.class);

        orderService.addPercentageDiscount("orderIdTest", "10");

        Mockito.verify(userOrderPaymentRepository).saveAll(userOrderPaymentCaptor.capture());
        Mockito.verify(orderRepository).save(order);

        List<UserOrderPayment> capturedList = userOrderPaymentCaptor.getValue();
        Assertions.assertEquals(72.0, capturedList.get(0).getAmountToPay());
        Assertions.assertEquals(72.0, capturedList.get(0).getAmountToPayWithoutExtraPayment());
    }


    @Test
    public void addCashDiscountWithoutAnyDiscount(){
        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setNetValue(100.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(0.0);
        order.setPercentageDiscountCashValue(0.0);
        order.setExtraPaymentValue(0.0);
        order.setValue(100.0);

        UserOrderPayment uop = new UserOrderPayment();
        uop.setUserOrderPaymentId("uopIdTest");
        uop.setMenuItemsValue(100.0);
        uop.setExtraPaymentValue(0.0);
        uop.setDiscountValueInCash(0.0);
        uop.setGeneralDiscountValue(0.0);
        uop.setAmountToPay(100.0);
        uop.setAmountToPayWithoutExtraPayment(100.0);
        uop.setBaseForPercentageDiscount(100.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(uop);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);

        ArgumentCaptor<List<UserOrderPayment>> userOrderPaymentCaptor = ArgumentCaptor.forClass(List.class);

        orderService.addCashDiscount("orderIdTest", "20");

        Mockito.verify(userOrderPaymentRepository).saveAll(userOrderPaymentCaptor.capture());

        List<UserOrderPayment> capturedList = userOrderPaymentCaptor.getValue();

        Assertions.assertEquals(20.0, capturedList.get(0).getDiscountValueInCash());
        Assertions.assertEquals(20.0, capturedList.get(0).getGeneralDiscountValue());
        Assertions.assertEquals(0.0, capturedList.get(0).getExtraPaymentValue());
        Assertions.assertEquals(80.0, capturedList.get(0).getAmountToPayWithoutExtraPayment());
        Assertions.assertEquals(80.0, capturedList.get(0).getAmountToPay());
    }

    @Test
    public void addCashDiscountDiscountWithPercentageDiscountTest(){
        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setNetValue(100.0);
        order.setCashDiscount(0.0);
        order.setPercentageDiscount(10.0);
        order.setPercentageDiscountCashValue(10.0);
        order.setExtraPaymentValue(0.0);
        order.setValue(90.0);

        UserOrderPayment uop = new UserOrderPayment();
        uop.setUserOrderPaymentId("uopIdTest");
        uop.setMenuItemsValue(100.0);
        uop.setExtraPaymentValue(0.0);
        uop.setDiscountValueInCash(0.0);
        uop.setDiscountInPercentageInCash(10.0);
        uop.setBaseForPercentageDiscount(100.0);
        uop.setGeneralDiscountValue(10.0);
        uop.setAmountToPay(90.0);
        uop.setAmountToPayWithoutExtraPayment(90.0);

        List<UserOrderPayment> userOrderPayments = new ArrayList<>();
        userOrderPayments.add(uop);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(userOrderPaymentRepository.findByOrderOrderId("orderIdTest")).thenReturn(userOrderPayments);

        ArgumentCaptor<List<UserOrderPayment>> userOrderPaymentCaptor = ArgumentCaptor.forClass(List.class);

        orderService.addCashDiscount("orderIdTest", "10");

        Mockito.verify(userOrderPaymentRepository).saveAll(userOrderPaymentCaptor.capture());
        Mockito.verify(orderRepository).save(order);

        List<UserOrderPayment> capturedList = userOrderPaymentCaptor.getValue();
        Assertions.assertEquals(80.0, capturedList.get(0).getAmountToPay());
        Assertions.assertEquals(80.0, capturedList.get(0).getAmountToPayWithoutExtraPayment());
    }

}
