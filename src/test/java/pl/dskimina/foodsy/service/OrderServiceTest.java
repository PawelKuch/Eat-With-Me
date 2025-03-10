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
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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




}
