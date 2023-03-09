package pl.dskimina.foodsy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

public class JWTUtils {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);


    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return "";
    }

    public String getUserNameFromJwtToken(String token) {
        return "";
    }

    public boolean validateJwtToken(String authToken) {

        return false;
    }
}
