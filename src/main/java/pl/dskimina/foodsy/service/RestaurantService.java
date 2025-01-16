package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.repository.RestaurantRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ToDataService toDataService;

    private final static Logger LOG = LoggerFactory.getLogger(RestaurantService.class);
    public RestaurantService(RestaurantRepository restaurantRepository, ToDataService toDataService) {
        this.restaurantRepository = restaurantRepository;
        this.toDataService = toDataService;
    }

    @Transactional
    public void addRestaurant(String name, String phone, String email, String address, String tags){
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(UUID.randomUUID().toString());
        restaurant.setName(name);
        restaurant.setPhone(phone);
        restaurant.setEmail(email);
        restaurant.setAddress(address);
        restaurant.setTags(tags);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void setLogoForRestaurant(String restaurantId, byte[] logoBytes){
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(logoBytes == null || restaurant == null){
            LOG.error("restaurant or logo bytes is null");
            return;
        }
        restaurant.setImage(logoBytes);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public RestaurantData getRestaurantByRestaurantId(String restaurantId){
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        return toDataService.convert(restaurant);
    }

    @Transactional
    public List<RestaurantData> getRestaurants(){
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        return restaurantList.stream().map(toDataService::convert).collect(Collectors.toList());
    }


}
