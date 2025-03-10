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
import pl.dskimina.foodsy.repository.ExtraPaymentRepository;
import pl.dskimina.foodsy.repository.OrderRepository;
import pl.dskimina.foodsy.repository.UserOrderPaymentRepository;
import pl.dskimina.foodsy.repository.UserRepository;

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

    @Test
    public void createUserOrderPaymentTest(){

        User user = new User();
        user.setUserId("userIdTest");
        user.setFirstName("userFirstNameTest");
        user.setLastName("userLastNameTest");

        Order order = new Order();
        order.setOrderId("orderIdTest");
        order.setValue(100.0);

        Mockito.when(orderRepository.findByOrderId("orderIdTest")).thenReturn(order);
        Mockito.when(userRepository.findByUserId("userIdTest")).thenReturn(user);



        userOrderPaymentService.createUserOrderPayment("orderIdTest", "userIdTest");

        ArgumentCaptor<UserOrderPayment> paymentCaptor = ArgumentCaptor.forClass(UserOrderPayment.class);

        Mockito.verify(orderRepository, Mockito.times(1)).findByOrderId("orderIdTest");
        Mockito.verify(userRepository, Mockito.times(1)).findByUserId("userIdTest");
        Mockito.verify(userOrderPaymentRepository, Mockito.times(1)).save(paymentCaptor.capture());

        UserOrderPayment userOrderPayment = paymentCaptor.getValue();

        Assertions.assertEquals("userFirstNameTest", userOrderPayment.getUser().getFirstName());
        Assertions.assertEquals("userLastNameTest", userOrderPayment.getUser().getLastName());
        Assertions.assertEquals("userIdTest", userOrderPayment.getUser().getUserId());
        Assertions.assertEquals("orderIdTest", userOrderPayment.getOrder().getOrderId());
        Assertions.assertEquals(100.0, userOrderPayment.getOrder().getValue());
    }
}
