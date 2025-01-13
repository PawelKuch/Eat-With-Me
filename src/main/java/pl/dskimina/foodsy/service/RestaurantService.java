package pl.dskimina.foodsy.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dskimina.foodsy.entity.Restaurant;
import pl.dskimina.foodsy.entity.data.RestaurantData;
import pl.dskimina.foodsy.repository.RestaurantRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    public boolean setLogoForRestaurant(String restaurantId, String logoPath){
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if(logoPath == null) {LOG.error("logoPath is null");}
        if(restaurant == null){LOG.error("restaurant is null");}
        File file = new File(logoPath);
        try {
            byte[] logoBytes = Files.readAllBytes(file.toPath());
            restaurant.setImage(logoBytes);
            restaurantRepository.save(restaurant);
            return true;
        } catch (IOException ex){
            LOG.error("Error while converting file to bytes with the exception message: " + ex.getMessage());
            return false;
        }
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

    @Transactional
    @PostConstruct
    public void setLogoForStartingRestaurants(){
        String kfcPath = "/static/img/kfclogo.png";
        String mcdPath = "/static/img/mcdonaldlogo.png";
        String bklogo = "/static/img/burgerkinglogo.png";
        String pizzeriaRinoPath = "/static/img/pizzeriarinologo.png";
        String uPiotrusiaPath = "/static/img/upiotrusialogo.png";

        setLogoForRestaurant("123azxczc1qsa", kfcPath);
        setLogoForRestaurant("123azxcc1qsa", bklogo);
        setLogoForRestaurant("123aczc1qsa", mcdPath);
        setLogoForRestaurant("123azczcqsa", pizzeriaRinoPath);
        setLogoForRestaurant("12azczc1qsa", uPiotrusiaPath);
    }
}
