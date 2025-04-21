package pl.dskimina.foodsy.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    public void addRestaurantTest(){
        String name = "Test Restaurant";
        String phone = "123456789";
        String email = "test@example.com";
        String address = "Test Address 123";
        String tags = "#tag2 #tag2";
        byte[] image = new byte[]{1, 2, 3};
        restaurantService.addRestaurant(name, phone, email, address, tags, image);

        ArgumentCaptor<Restaurant> restaurantArgumentCaptor = ArgumentCaptor.forClass(Restaurant.class);

        Mockito.verify(restaurantRepository).save(restaurantArgumentCaptor.capture());
        Restaurant capturedRestaurant = restaurantArgumentCaptor.getValue();

        Assertions.assertEquals(name, capturedRestaurant.getName());
        Assertions.assertEquals(phone, capturedRestaurant.getPhone());
        Assertions.assertEquals(email, capturedRestaurant.getEmail());
        Assertions.assertEquals(address, capturedRestaurant.getAddress());
        Assertions.assertEquals(tags, capturedRestaurant.getTags());
        Assertions.assertEquals(image, capturedRestaurant.getImage());
    }

}
