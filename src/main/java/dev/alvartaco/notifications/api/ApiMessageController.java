package dev.alvartaco.notifications.api;

import dev.alvartaco.notifications.dto.MessageDTO;
import dev.alvartaco.notifications.service.CategoryService;
import dev.alvartaco.notifications.service.MessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Rest Service to accomplish
 * "It is required to create a system capable of receiving messages"
 */
@RestController
@RequestMapping("/api/messages")
public class ApiMessageController {

    private static final Logger log = LoggerFactory.getLogger(ApiMessageController.class);
    private final MessageService messageService;

    public ApiMessageController(CategoryService categoryService,
                             MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * It can be tested with:
     * $ curl -X POST localhost:8088/api/messages -H 'Content-type:application/json' -d '{"categoryId": "2", "messageBody": "the boddy"}'
     * @param messageDTO
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void create(@Valid @RequestBody MessageDTO messageDTO)  {

        try {
            messageService.notify(messageDTO.getCategoryId(), messageDTO.getMessageBody());
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - Error /api/messages {}", e.getMessage());
        }

    }
}
