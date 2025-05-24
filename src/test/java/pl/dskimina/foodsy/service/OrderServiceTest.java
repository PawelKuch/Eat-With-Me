package pl.dskimina.foodsy.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dskimina.foodsy.entity.Order;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.exception.OrderNotFoundException;
import pl.dskimina.foodsy.exception.RestaurantNotFoundException;
import pl.dskimina.foodsy.exception.UserNotFoundException;
import pl.dskimina.foodsy.repository.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;


    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    ToDataService toDataService;

    private OrderData createTestOrderData(){
        OrderData orderData = new OrderData();
        String closingDateString = "1999-01-17T22:22";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime closingDate = LocalDateTime.parse(closingDateString, formatter);
        orderData.setOrderId("orderIdTest");
        orderData.setDescription("descriptionTest");
        orderData.setClosingDateTime(Date.from(closingDate.toInstant(ZoneOffset.ofHours(+1))));
        return orderData;
    }

    private Order createTestOrder(double percentageDiscount, double percentageDiscountInCash, double cashDiscount, double extraPaymentValue){
        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setPercentageDiscount(percentageDiscount);
        order.setPercentageDiscountCashValue(percentageDiscountInCash);
        order.setCashDiscount(cashDiscount);
        order.setExtraPaymentValue(extraPaymentValue);
        return order;
    }


    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @Test
    public void createOrderTest() throws UserNotFoundException, RestaurantNotFoundException {
        String restaurantId = "restaurantIdTest";
        String userId = "userIdTest";
        String closingDateString = "1999-01-17T22:22";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime closingDate = LocalDateTime.parse(closingDateString, formatter);

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

        OrderData orderData = createTestOrderData();

        Mockito.when(userRepository.findByUserId("userIdTest")).thenReturn(user);
        Mockito.when(restaurantRepository.findByRestaurantId("restaurantIdTest")).thenReturn(restaurant);
        Mockito.when(toDataService.convert(any(Order.class))).thenReturn(orderData);

        OrderData convertedOrderData = orderService.createOrder(restaurantId, userId, closingDateString, minOrderValue, description);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals("userIdTest", capturedOrder.getOwner().getUserId());
        Assertions.assertEquals("userFirstNameTest", capturedOrder.getOwner().getFirstName());
        Assertions.assertEquals("userLastNameTest", capturedOrder.getOwner().getLastName());

        Assertions.assertEquals("restaurantIdTest", capturedOrder.getRestaurant().getRestaurantId());
        Assertions.assertEquals("restaurantNameTest", capturedOrder.getRestaurant().getName());
        Assertions.assertEquals("restaurantEmailTest", capturedOrder.getRestaurant().getEmail());

        Assertions.assertEquals(closingDate, capturedOrder.getClosingDate());
        Assertions.assertEquals(100.0, capturedOrder.getMinValue());
        Assertions.assertEquals("descriptionTest", capturedOrder.getDescription());

        Assertions.assertEquals("orderIdTest", convertedOrderData.getOrderId());
    }

    @Test
    public void addPercentageDiscountWithoutAnyDiscountTest() throws OrderNotFoundException {
        Order order = createTestOrder( 0.0, 0.0, 0.0, 0.0);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        orderService.updateOrderPrice("orderIdTest", "0","10", "0");

        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals("orderIdTest", capturedOrder.getOrderId());
        Assertions.assertEquals(10.0, capturedOrder.getPercentageDiscount());
    }

    @Test
    public void addPercentageDiscountWithCurrentCashDiscountTest() throws OrderNotFoundException{
        Order order = createTestOrder( 0.0, 0.0, 20.0, 0.0);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        orderService.updateOrderPrice("orderIdTest", "0","10", "20");

        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals(10.0, capturedOrder.getPercentageDiscount());
        Assertions.assertEquals(20.0, capturedOrder.getCashDiscount());
    }


    @Test
    public void addCashDiscountWithoutAnyDiscount() throws OrderNotFoundException{
        Order order = createTestOrder(0.0, 0.0, 0.0, 0.0);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        orderService.updateOrderPrice("orderIdTest", "0","0", "20");

        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals(20.0, capturedOrder.getCashDiscount());
    }

    @Test
    public void addCashDiscountWithPercentageDiscountTest() throws OrderNotFoundException{
        Order order = createTestOrder( 10.0, 10.0, 0.0, 0.0);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        orderService.updateOrderPrice("orderIdTest", "0","10", "10");

        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals(10.0, capturedOrder.getCashDiscount());
        Assertions.assertEquals(10.0, capturedOrder.getPercentageDiscount());
    }

    @Test
    public void addExtraPaymentWithOneUserWithPercentageDiscount() throws OrderNotFoundException {
        Order order = createTestOrder( 20.0, 20.0, 0.0, 0.0);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        orderService.updateOrderPrice("orderIdTest", "10","20", "0");

        Mockito.verify(orderRepository).save(orderCaptor.capture());


        Order capturedOrder = orderCaptor.getValue();


        Assertions.assertEquals("orderIdTest", capturedOrder.getOrderId());
        Assertions.assertEquals(10.0, capturedOrder.getExtraPaymentValue());
        Assertions.assertEquals(20.0, capturedOrder.getPercentageDiscount());
    }

    //add extraPayment with current extraPayment
    @Test
    public void addExtraPaymentWithCurrentExtraPaymentTest() throws OrderNotFoundException{
        Order order = createTestOrder( 0.0, 0.0, 0.0, 10.0);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        orderService.updateOrderPrice("orderIdTest", "25","0", "0");
        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals("orderIdTest", capturedOrder.getOrderId());
        Assertions.assertEquals(25.0, capturedOrder.getExtraPaymentValue());
    }

    @Test
    public void addExtraPaymentWithCurrentCashDiscountAndPercentageDiscount() throws OrderNotFoundException{
        Order order = createTestOrder(10.0, 2.0, 4.0, 0.0);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);

        orderService.updateOrderPrice("orderIdTest", "6","10", "4");
        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertEquals("orderIdTest", capturedOrder.getOrderId());
        Assertions.assertEquals(6.0, capturedOrder.getExtraPaymentValue());
    }

    @Test
    public void addCashAndPercentageDiscountsInOne() throws OrderNotFoundException{
        String orderId = "orderIdTest";
        Order order = createTestOrder( 0.0, 0.0, 0.0, 0.0);
        order.setOrderId(orderId);

        Mockito.when(orderRepository.findByOrderId(orderId)).thenReturn(order);

        orderService.updateOrderPrice("orderIdTest", "0","0", "4");
        Mockito.verify(orderRepository).save(orderCaptor.capture());
        Order savedAfterCash = orderCaptor.getValue();

        Assertions.assertEquals(4.0, savedAfterCash.getCashDiscount());


        Mockito.reset(orderRepository);
        String updatedOrderId = savedAfterCash.getOrderId();
        Mockito.when(orderRepository.findByOrderId(updatedOrderId)).thenReturn(savedAfterCash);


        orderService.updateOrderPrice("orderIdTest", "0","10", "4");
        Mockito.verify(orderRepository).save(orderCaptor.capture());
        Order savedAfterPercentage = orderCaptor.getValue();

        Assertions.assertEquals(10.0, savedAfterPercentage.getPercentageDiscount());
        Assertions.assertEquals(4.0, savedAfterPercentage.getCashDiscount());
        Assertions.assertEquals(0.0, savedAfterPercentage.getExtraPaymentValue());

        Mockito.reset(orderRepository);
        Mockito.when(orderRepository.findByOrderId(savedAfterPercentage.getOrderId())).thenReturn(savedAfterPercentage);

        orderService.updateOrderPrice("orderIdTest", "6","10", "4");
        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order savedAfterExtraPayment = orderCaptor.getValue();

        Assertions.assertEquals("orderIdTest", savedAfterExtraPayment.getOrderId());
        Assertions.assertEquals(6.0, savedAfterExtraPayment.getExtraPaymentValue());

        Mockito.reset(orderRepository);
        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(savedAfterExtraPayment);

        //CashDiscount2 - cashDiscount = 0
        orderService.updateOrderPrice("orderIdTest", "6","10", "0");
        Mockito.verify(orderRepository).save(orderCaptor.capture());

        Order savedAfterCashDiscount2 = orderCaptor.getValue();

        Assertions.assertEquals(6.0, savedAfterCashDiscount2.getExtraPaymentValue());
        Assertions.assertEquals(0.0, savedAfterCashDiscount2.getCashDiscount());
    }

}
