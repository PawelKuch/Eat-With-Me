package pl.dskimina.foodsy.service;


import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.data.UserData;

@Service
public class SessionService {
    UserService userService;
    UserData currentUser;

    public SessionService(UserService userService){
        this.userService = userService;
    }

    public UserData getCurrentUser() {return currentUser;}

    public void setCurrentUser(String userId) {
        this.currentUser = userService.getUserById(userId);
    }
    public void logOut(){this.currentUser = null;}
}
