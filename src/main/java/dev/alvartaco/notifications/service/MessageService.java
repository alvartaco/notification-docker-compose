package dev.alvartaco.notifications.service;

import dev.alvartaco.notifications.exception.CategoryException;
import dev.alvartaco.notifications.exception.MessageException;
import dev.alvartaco.notifications.exception.NotificationException;
import dev.alvartaco.notifications.model.Message;
import dev.alvartaco.notifications.repository.IMessageRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
//public class MessageService implements IMessageService{
public class MessageService implements IMessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final IMessageRepository iMessageRepository;
    private final NotificationService notificationService;
    private final CategoryService categoryService;
    public MessageService(@Qualifier("jdbcClientMessageRepository")
                          IMessageRepository iMessageRepository,
                          CategoryService categoryService,
                          NotificationService notificationService) {
        this.iMessageRepository = iMessageRepository;
        this.notificationService = notificationService;
        this.categoryService = categoryService;
    }

    /**
     * Main entry point to save messages
     */
    private Integer create(@Valid Message message) throws MessageException {
        log.info("#NOTIFICATIONS-D-C - Going to iMessageRepository.create(message).");
        return iMessageRepository.create(message);
    }

    @Override
    public void notify(String categoryId, String messageBody) throws NotificationException {

        try {

            log.info("#NOTIFICATIONS-D-C - notify(String categoryId, String messageBody)");
            notificationService.notify(
                    save(categoryId, messageBody)
            );

        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - Error notify(String categoryId, String messageBody)");
            throw new NotificationException(e.toString());
        }
    }

    private Message save(String categoryId, String messageBody) throws NotificationException, MessageException {

        /*
         * Validation for existing in Database categoryId
         * Validating messageBody
         */
        try {
            if (categoryService.getAllCategoryDTOsByCategoryNameAsc().stream().noneMatch(dto -> dto.getCategoryId() == Short.parseShort(categoryId))) {
                log.error("#NOTIFICATIONS-D-C - Error categoryId");
                throw new MessageException("#NOTIFICATIONS-D-C - Error categoryId");
            }
            if (messageBody == null || messageBody.isEmpty() || messageBody.isBlank()) {
                log.error("#NOTIFICATIONS-D-C - Error messageBody");
                throw new MessageException("#NOTIFICATIONS-D-C - Error messageBody");
            }
        } catch (CategoryException e) {
            log.error("#NOTIFICATIONS-D-C - Error save(String categoryId, String messageBody)");
            throw new MessageException(e.toString());
        }

        try {
            log.info("#NOTIFICATIONS-D-C - START to save message.");

            Message message = new Message(
                    null,
                    categoryService.getCategoryByCategoryId(Short.valueOf(categoryId)),
                    messageBody.trim(),
                    LocalDateTime.now());

            log.info("#NOTIFICATIONS-D-C - Message {}",  message);

            message = new Message(
                    create(message),
                    message.category(),
                    message.messageBody(),
                    message.createdOn()
            );

            return message;

        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - Error saving message.");
            throw new NotificationException(e.toString());
        }
    }
}