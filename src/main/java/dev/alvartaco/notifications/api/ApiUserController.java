package dev.alvartaco.notifications.api;

import dev.alvartaco.notifications.model.User;
import dev.alvartaco.notifications.repository.InMemoryUserRepository;
import dev.alvartaco.notifications.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest Controller intended to test/view in the browser the List of loaded users
 */
@RestController
public class ApiUserController {

    private static final Logger log = LoggerFactory.getLogger(ApiUserController.class);

    private final UserService userService;
    public ApiUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    List<User> findAll() {
        log.info("#NOTIFICATIONS-D-C - INSIDE /users");
        return userService.findAll();
    }

}
