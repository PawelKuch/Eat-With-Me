package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.exception.BadRequestException;
import pl.dskimina.foodsy.exception.RestaurantNotFoundException;
import pl.dskimina.foodsy.repository.RestaurantRepository;

import java.io.IOException;
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
    public void addRestaurant(String name, String phone, String email, String address, String tags, byte[] image){
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(UUID.randomUUID().toString());
        restaurant.setName(name);
        restaurant.setPhone(phone);
        restaurant.setEmail(email);
        restaurant.setAddress(address);
        restaurant.setTags(tags);
        restaurant.setImage(image);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void setLogoForRestaurant(String restaurantId, byte[] logoBytes)  {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(restaurant == null) throw new RestaurantNotFoundException("Nie znaleziono żądanej restauracji id: " + restaurantId);
        if(logoBytes == null) throw new BadRequestException("Tablica bajtów logo jest pusta!");
        restaurant.setImage(logoBytes);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public RestaurantData getRestaurantByRestaurantId(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(restaurant == null) throw new RestaurantNotFoundException("Nie znaleziono żądanej restauracji id: " + restaurantId);
        return toDataService.convert(restaurant);
    }

    @Transactional
    public List<RestaurantData> getRestaurants(){
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        return restaurantList.stream().map(toDataService::convert).collect(Collectors.toList());
    }

    @Transactional
    public byte[] getImageForRestaurantId(String restaurantId){
        return restaurantRepository.getImageByRestaurantId(restaurantId);
    }

    @Transactional
    public void updateRestaurant(String restaurantId, RestaurantData restaurantData) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(restaurant == null) throw new RestaurantNotFoundException("Nie znaleziono żądanej restauracji id: " + restaurantId);
        String name = restaurantData.getName();
        String tags = restaurantData.getTags();
        String email = restaurantData.getEmail();
        String phone = restaurantData.getPhone();
        String address = restaurantData.getAddress();

        if(name != null && !name.isEmpty()) restaurant.setName(name);
        if(tags != null && !tags.isEmpty()) restaurant.setTags(tags);
        if(email != null && !email.isEmpty()) restaurant.setEmail(email);
        if(address != null && !address.isEmpty()) restaurant.setAddress(address);
        if(phone != null && !phone.isEmpty()) restaurant.setPhone(phone);
        LOG.debug("restaurant has been updated");
        restaurantRepository.save(restaurant);

    }

    @Transactional
    public void  updateRestaurantLogo(String restaurantId, MultipartFile image) throws IOException{
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(restaurant == null) throw new RestaurantNotFoundException("Nie znaleziono żądanej restauracji id: " + restaurantId);
        if(image == null) throw new BadRequestException("image jest pusty");

        restaurant.setImage(image.getBytes());
        restaurantRepository.save(restaurant);
    }

    public void deleteRestaurantByRestaurantId(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(restaurant == null) throw new RestaurantNotFoundException("Nie znaleziono żądanej restauracji id: " + restaurantId);
        restaurantRepository.delete(restaurant);
    }

}
