package dev.alvartaco.notifications.controller;

import dev.alvartaco.notifications.exception.CategoryException;
import dev.alvartaco.notifications.kafka.KafkaHealthService;
import dev.alvartaco.notifications.kafka.MessageProducer;
import dev.alvartaco.notifications.model.dto.CategoryDTO;
import dev.alvartaco.notifications.service.CategoryService;
import dev.alvartaco.notifications.service.secure.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller for message creation handling Page
 */
@Controller
public class MessageController {

    static final String MESSAGE = "message";
    static final String ERROR = "error";

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final CategoryService categoryService;
    private final MessageProducer messageProducer;
    private final KafkaHealthService kafkaHealthService;
    private final IUserService iUserService;

    public MessageController(CategoryService categoryService,
                             MessageProducer messageProducer, KafkaHealthService kafkaHealthService, IUserService iUserService) {
        this.categoryService = categoryService;
        this.messageProducer = messageProducer;
        this.kafkaHealthService = kafkaHealthService;
        this.iUserService = iUserService;
    }

    /**
     * Entry point for the message creation Form
     */
    @GetMapping("/web/message")
    public String message(@RequestParam(defaultValue = "") String error,
                          @RequestParam(defaultValue = "") String message,
                          Model model) {

        log.info("#NOTIFICATIONS-D-C - START /message");

        if (!message.isEmpty())
            model.addAttribute(MESSAGE, message);
        if (!error.isEmpty())
            model.addAttribute(ERROR, error);

        List<CategoryDTO> categories;
        boolean isKafkaUp = kafkaHealthService.isKafkaUp();

        try {
            categories = categoryService.getAllCategoryDTOsByCategoryNameAsc();
        } catch (CategoryException e) {
            // TESTED //
            log.error("#NOTIFICATIONS-D-C - Error getting categories /message, fwd to index.");
            return "index";
        }

        model.addAttribute("categorySelect", categories);
        model.addAttribute("isNotKafkaUp", !isKafkaUp);
        log.info("#NOTIFICATIONS-D-C - END /message");
        return "message/index";
    }

    /**
     * Method that calls the service to store the message in the DB
     */
    @PostMapping("/web/message/create")
    String createMessage(@RequestParam String categoryId,
                         @RequestParam String messageBody,
                         Model model, Authentication authentication) {

        log.info("#NOTIFICATIONS-D-C - START /web/message/create");

        /*
         * Validation for existing in Database categoryId
         * Validating messageBody
         */
        try {
            if (categoryService.getAllCategoryDTOsByCategoryNameAsc().stream().noneMatch(dto -> dto.getCategoryId() == Short.parseShort(categoryId))) {
                log.error("#NOTIFICATIONS-D-C - Error with received categoryID /web/message/create");
                return message("ERROR with received Message Category!!!", "", model);
            }
            if (messageBody.isEmpty()) {
                log.error("#NOTIFICATIONS-D-C - Error with received messageBody /web/message/create");
                return message("ERROR with received Message Body!!!", "", model);
            }
        } catch (CategoryException e) {
            log.error("#NOTIFICATIONS-D-C - Error getting categories /web/message/create, fwd to index.");
            return "index";
        }

        if (kafkaHealthService.isKafkaUp()) {
            log.info("#NOTIFICATIONS-D-C - Sending the Notification of message creation");
            try { // TODO GET ME messageCreatorId
                String loggedUserEmail = authentication.getName(); // Get email from authentication
                String messageCreatorId = iUserService.findUserByEmail(loggedUserEmail).getId();

                messageProducer.send(categoryId, messageBody, messageCreatorId);
            } catch (Exception e) {
                log.error("#NOTIFICATIONS-D-C - Error - messageProducer.send(categoryId, messageBody); ");
                return message("Message ERROR NOT Saved..!", "", model);
            }
        } else {
            log.error("#NOTIFICATIONS-D-C - Error - messageProducer.send(categoryId, messageBody); No Kafka Service ");
            return message("Message ERROR NOT Saved..!", "", model);
        }

        log.info("#NOTIFICATIONS-D-C - END /web/message/create");
        return message("", "Message Saved..!", model);
    }
}

