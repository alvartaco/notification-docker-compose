package dev.alvartaco.notifications.controller;

import dev.alvartaco.notifications.kafka.KafkaHealthService;
import dev.alvartaco.notifications.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the Home page
 */
@Controller
@RequestMapping("/web")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final KafkaHealthService kafkaHealthService;

    public HomeController(KafkaHealthService kafkaHealthService, CategoryService categoryService) {
        this.kafkaHealthService = kafkaHealthService;
    }

    public String home(Model model) {
        log.info("#NOTIFICATIONS-D-C - INSIDE /");

        boolean isKafkaUp = kafkaHealthService.isKafkaUp();

        model.addAttribute("isNotKafkaUp", !isKafkaUp);

        return "index";
    }
}