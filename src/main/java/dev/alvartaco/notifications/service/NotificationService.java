package dev.alvartaco.notifications.service;

import dev.alvartaco.notifications.model.dto.NotificationDTO;
import dev.alvartaco.notifications.model.dto.NotificationDisplayDTO;
import dev.alvartaco.notifications.exception.CategoryException;
import dev.alvartaco.notifications.exception.NotificationException;
import dev.alvartaco.notifications.model.ConstNotificationStatus;
import dev.alvartaco.notifications.model.Message;
import dev.alvartaco.notifications.model.Notification;
import dev.alvartaco.notifications.model.User;
import dev.alvartaco.notifications.notificationengine.factories.NotificationEngineFactory;
import dev.alvartaco.notifications.repository.INotificationRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final INotificationRepository iNotificationRepository;
    private final UserService userService;
    private final NotificationEngineFactory notificationEngineFactory;
    private final CategoryService categoryService;

    public NotificationService(@Qualifier("jdbcClientNotificationRepository")
                               INotificationRepository iNotificationRepository,
                               NotificationEngineFactory notificationEngineFactory,
                               CategoryService categoryService,
                               UserService userService) {
        this.userService = userService;
        this.iNotificationRepository = iNotificationRepository;
        this.notificationEngineFactory = notificationEngineFactory;
        this.categoryService = categoryService;
    }

    /**
     * Method that is fired to notify de Subscribers
     * after a subject changes, a new message was created
     * @param message
     * @throws NotificationException
     */
    public void notify(Message message) throws NotificationException {

        Notification notification;
        Integer notificationId;
        LocalDateTime now;

        List<User> users = userService.getUsersByCategoryId(message.category().getCategoryId());
        log.info("#NOTIFICATIONS-D-C - Users {}",  users);

        if (users != null) {

            List<String> userChannelTypes;

            // Loop the users having the message category of the message
            // Finance, sport, movies
            for(User user : users) {

                // Retrieves User's chanel of notification
                // SMS, Email, Push notifications
                userChannelTypes = userService.getUserChannelTypes(user);

                if (userChannelTypes != null) {

                    // Creates the corresponding notifications in the DB
                    for(String channelType : userChannelTypes) {

                        now = LocalDateTime.now();

                        log.info("#NOTIFICATIONS-D-C - CREATE NOTIFICATION");
                        notification = new Notification(
                                0,
                                message,
                                user,
                                channelType,
                                ConstNotificationStatus.UNKNOWN,
                                now,
                                now,
                                Short.parseShort("0"));

                        log.info("#NOTIFICATIONS-D-C - notification {}", notification);

                        notificationId = create(notification);

                        // If the notification was created in the DB
                        // The corresponding notification message is sent to the user
                        if (notificationId > 0) {
                             log.info("#NOTIFICATIONS-D-C - Start sending corresponding dummy notification to the message " );
                                 notificationEngineFactory.execute(channelType);
                             log.info("#NOTIFICATIONS-D-C - End Sending corresponding dummy notification to the message " );
                        }
                    }
                }
            }
        }
    }

    /**
     * It saves a Notification
     */
    public Integer create(@Valid Notification notification) throws NotificationException {
           return iNotificationRepository.create(notification);
    }

    /**
     * Used for Displaying the List of Notifications
     * Log history. A list of all data records in the log, sorted from newest to oldest.
     * @return
     * @throws NotificationException
     */
    public List<NotificationDisplayDTO> getAllNotificationsDisplayDTOsLiFo() throws NotificationException {
        try {
            List<NotificationDisplayDTO> notificationDisplayDTOS = new ArrayList<>();
            for (NotificationDTO notificationDTO : iNotificationRepository.getAllNotificationDTOsLiFo()) {
                notificationDisplayDTOS.add(mapDTOToDisplayDTO(notificationDTO));
            }
            log.info("#NOTIFICATIONS-D-C - OK getAllNotificationsDisplayDTOsLiFo().");
            return notificationDisplayDTOS;
        } catch ( Exception e) {
            log.error("#NOTIFICATIONS-D-C - ERROR getAllNotificationsDisplayDTOsLiFo().");
            throw new NotificationException("Notification not found");
        }
    }

    /**
     * Used for Displaying the List of Notifications of the loggeduser
     * Log history. A list of all data records in the log, sorted from newest to oldest.
     * @return
     * @throws NotificationException
     */
    public List<NotificationDisplayDTO> getAllNotificationDTOsLiFoByMessageCreatorId() throws NotificationException {
        try {
            List<NotificationDisplayDTO> notificationDisplayDTOS = new ArrayList<>();
            for (NotificationDTO notificationDTO : iNotificationRepository.getAllNotificationDTOsLiFoByMessageCreatorId()) {
                notificationDisplayDTOS.add(mapDTOToDisplayDTO(notificationDTO));
            }
            log.info("#NOTIFICATIONS-D-C - OK getAllNotificationDTOsLiFoByMessageCreatorId().");
            return notificationDisplayDTOS;
        } catch ( Exception e) {
            log.error("#NOTIFICATIONS-D-C - ERROR getAllNotificationDTOsLiFoByMessageCreatorId().");
            throw new NotificationException("Notification not found");
        }
    }

    /**
     * Fill the DTO with data formated por the Front
     * @param
     * @return
     */
    public NotificationDisplayDTO mapDTOToDisplayDTO(NotificationDTO notificationDTO) throws CategoryException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Integer notificationId = notificationDTO.getNotificationId();
        Integer messageId = notificationDTO.getMessageId();
        String messageCategoryName = categoryService.getCategoryByCategoryId(notificationDTO.getMessageCategoryId()).getCategoryName();
        Short categoryId = notificationDTO.getMessageCategoryId();
        String userName = userService.getUsersByUserId(notificationDTO.getUserId()).userName();
        Integer userId = notificationDTO.getUserId();
        String channelType = notificationDTO.getNotificationChannelType();
        String status = notificationDTO.getNotificationStatus();
        String createdOn = (notificationDTO.getNotificationCreatedOn()).format(formatter);
        String updatedOn = (notificationDTO.getNotificationUpdatedOn()).format(formatter);;
        Short retryNumber = notificationDTO.getNotificationRetryNumber();

        return new NotificationDisplayDTO(
                         notificationId,
                         messageId,
                         messageCategoryName,
                         categoryId,
                         userName,
                         userId,
                         channelType,
                         status,
                         createdOn,
                         updatedOn,
                         retryNumber
                );
    }
}