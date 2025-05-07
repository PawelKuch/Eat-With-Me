package pl.dskimina.foodsy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.dskimina.foodsy.entity.data.OrderData;
import pl.dskimina.foodsy.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public RedirectView getOrderList(RedirectAttributes ra) {
        return new RedirectView("/orders");
    }
}
