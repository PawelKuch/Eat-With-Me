package pl.dskimina.foodsy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class InitComponent {

    private final RestaurantService restaurantService;

    @Autowired
    public InitComponent(RestaurantService restaurantService) {this.restaurantService = restaurantService;}


    @EventListener(classes = ContextRefreshedEvent.class)
    public void setLogoForStartingRestaurants() throws IOException {
        byte[] kfcBytes = getBytesImage("./logos/kfclogo.png");
        byte[] mcdBytes = getBytesImage("./logos/mcdonaldlogo.png");
        byte[] bkBytes = getBytesImage("./logos/burgerkinglogo.png");
        byte[] pizzeriaRinoBytes = getBytesImage("./logos/pizzeriarinologo.png");
        byte[] uPiotrusiaBytes = getBytesImage("./logos/upiotrusialogo.png");

        restaurantService.setLogoForRestaurant("123azxczc1qsa", kfcBytes);
        restaurantService.setLogoForRestaurant("123azxcc1qsa", bkBytes);
        restaurantService.setLogoForRestaurant("123aczc1qsa", mcdBytes);
        restaurantService.setLogoForRestaurant("123azczcqsa", pizzeriaRinoBytes);
        restaurantService.setLogoForRestaurant("12azczc1qsa", uPiotrusiaBytes);
    }

    public byte[] getBytesImage(String path) throws IOException {
        File file = new File(path);
        return Files.readAllBytes(file.toPath());
    }
}
