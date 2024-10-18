package dev.alvartaco.notifications.kafka;

import dev.alvartaco.notifications.exception.MessageException;
import dev.alvartaco.notifications.exception.NotificationException;
import dev.alvartaco.notifications.model.dto.MessageDTO;
import dev.alvartaco.notifications.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaTransactionListener {

  private final MessageService messageService;

  @KafkaListener(topics = "messages", groupId = "message-group", containerFactory = "kafkaListenerContainerFactory")
  @Transactional
  public void listen(MessageDTO messageDTO) throws NotificationException {

    log.info("#NOTIFICATIONS-D-C - Message Received: {}", messageDTO);

          messageService.notify(
                  messageDTO.getCategoryId(),
                  messageDTO.getMessageBody());

    log.info("#NOTIFICATIONS-D-C - Message saved, notification sent");

  }
}
