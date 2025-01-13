package pl.dskimina.foodsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class InitComponent {

    private static final Logger LOG = LoggerFactory.getLogger(InitComponent.class);

    private final RestaurantService restaurantService;

    @Autowired
    public InitComponent(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @EventListener(classes = ContextRefreshedEvent.class)
    public void setLogoForStartingRestaurants() throws IOException {
        byte[] kfcPath = readLogo("./logos/kfclogo.png");
        byte[] mcdPath = readLogo("./logos/mcdonaldlogo.png");
        byte[] bklogo = readLogo("./logos/burgerkinglogo.png");
        byte[] pizzeriaRinoPath = readLogo("./logos/pizzeriarinologo.png");
        byte[] uPiotrusiaPath = readLogo("./logos/upiotrusialogo.png");

        restaurantService.setLogoForRestaurant("123azxczc1qsa", kfcPath);
        restaurantService.setLogoForRestaurant("123azxcc1qsa", bklogo);
        restaurantService.setLogoForRestaurant("123aczc1qsa", mcdPath);
        restaurantService.setLogoForRestaurant("123azczcqsa", pizzeriaRinoPath);
        restaurantService.setLogoForRestaurant("12azczc1qsa", uPiotrusiaPath);
    }

    private byte[] readLogo(String path) throws IOException {
        File file = new File(path);
        return Files.readAllBytes(file.toPath());
    }

}
