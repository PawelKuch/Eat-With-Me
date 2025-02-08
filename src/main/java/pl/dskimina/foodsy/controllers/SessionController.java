package pl.dskimina.foodsy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.service.SessionService;

@Controller
public class SessionController {

    private final SessionService sessionService;
    public SessionController(SessionService sessionService) {this.sessionService = sessionService;}

    @GetMapping("/sign-out")
    public RedirectView logout(){
        sessionService.logOut();
        return new RedirectView("/");
    }

    @PostMapping("/sign-in")
    public RedirectView signIn(@RequestParam("userId") String userId){
        sessionService.setCurrentUser(userId);
        return new RedirectView("/");
    }
}
