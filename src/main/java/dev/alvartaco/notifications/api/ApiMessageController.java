package dev.alvartaco.notifications.api;

import dev.alvartaco.notifications.kafka.MessageProducer;
import dev.alvartaco.notifications.model.dto.MessageDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * Rest Service to accomplish
 * "It is required to create a system capable of receiving messages"
 */
@RestController
@RequestMapping("/api/messages")
public class ApiMessageController {

    private static final Logger log = LoggerFactory.getLogger(ApiMessageController.class);
    private final MessageProducer messageProducer;

    public ApiMessageController(MessageProducer messageProducer) {
          this.messageProducer = messageProducer;
    }

    /**
     * It can be tested with:
     * $ curl -X POST localhost:8088/api/messages -H 'Content-type:application/json' -d '{"categoryId": "2", "messageBody": "the body"}'
     * @param messageDTO MessageDTO
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void send(@Valid @RequestBody MessageDTO messageDTO)  {
        try {
            messageProducer.send(messageDTO.getCategoryId(), messageDTO.getMessageBody());
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - Error /api/messages {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
