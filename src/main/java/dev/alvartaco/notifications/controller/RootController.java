package dev.alvartaco.notifications.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/")
    public String redirectToApp() {
        return "redirect:http://localhost:3000";
    }
}
