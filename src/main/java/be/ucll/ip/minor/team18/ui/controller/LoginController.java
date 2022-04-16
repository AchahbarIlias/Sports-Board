package be.ucll.ip.minor.team18.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "user-login";
    }

    @GetMapping("/user_logout")
    public String logoutPage() {
        return "user-logout";
    }

}
