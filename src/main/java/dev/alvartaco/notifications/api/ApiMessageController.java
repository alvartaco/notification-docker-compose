package dev.alvartaco.notifications.api;

import dev.alvartaco.notifications.kafka.KafkaHealthService;
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
    private final KafkaHealthService kafkaHealthService;

    public ApiMessageController(MessageProducer messageProducer, KafkaHealthService kafkaHealthService) {
        this.messageProducer = messageProducer;
        this.kafkaHealthService = kafkaHealthService;
    }

    /**
     * It can be tested with:
     * $ curl -X POST localhost:8088/api/messages -H 'Content-type:application/json' -d '{
     * "categoryId": "2", "messageBody": "the body", "messageCreatorId": "dsdqwe23eweqw"}'
     * @param messageDTO MessageDTO
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void send(@Valid @RequestBody MessageDTO messageDTO)  {

        if (kafkaHealthService.isKafkaUp()) {
            try {
                messageProducer.send(messageDTO.getCategoryId(),
                        messageDTO.getMessageBody(), messageDTO.getMessageCreatorId());
            } catch (Exception e) {
                log.error("#NOTIFICATIONS-D-C - Error /api/messages {}", e.getMessage());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } else {
            log.error("#NOTIFICATIONS-D-C - Error /api/messages {}", "Kafka not reachable.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Kafka not reachable.");
        }
    }
}
