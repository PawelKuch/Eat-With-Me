package pl.dskimina.foodsy.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.Entity.Restaurant;
import pl.dskimina.foodsy.Repository.RestaurantRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {
    RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public void addRestaurant(String name, String phone, String Description){
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(UUID.randomUUID().toString());
        restaurant.setName(name);
        restaurant.setPhone(phone);
        restaurant.setDescription(Description);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public List<Restaurant> getRestaurants(){
        return restaurantRepository.findAll();
    }
}
