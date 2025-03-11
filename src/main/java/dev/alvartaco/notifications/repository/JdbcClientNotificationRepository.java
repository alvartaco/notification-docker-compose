package dev.alvartaco.notifications.repository;

import dev.alvartaco.notifications.exception.NotificationException;
import dev.alvartaco.notifications.model.Notification;
import dev.alvartaco.notifications.model.dto.NotificationDTO;
import dev.alvartaco.notifications.service.secure.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Repository of DataBase Notification Records
 * JDBC Client
 */
@Repository
public class JdbcClientNotificationRepository implements INotificationRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcClientNotificationRepository.class);
    private final JdbcClient jdbcClient;
    private final IUserService iUserService;

    public JdbcClientNotificationRepository(JdbcClient jdbcClient, IUserService iUserService) {
        this.jdbcClient = jdbcClient;
        this.iUserService = iUserService;
    }

    @Override
    public Integer create(Notification notification) throws NotificationException {
        try {

            KeyHolder keyHolder = new GeneratedKeyHolder();

            var updated = jdbcClient.sql("INSERT INTO notification(" +
                            "message_id, " +
                            "message_category_id, " +
                            "user_id, " +
                            "notification_channel_type, " +
                            "notification_status, " +
                            "notification_created_on, " +
                            "notification_updated_on, " +
                            "notification_retry_number)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
                    .params(List.of(
                            notification.getMessage().messageId(),
                            notification.getMessage().category().getCategoryId(),
                            notification.getUser().userId(),
                            notification.getChannelType(),
                            notification.getStatus(),
                            notification.getCreatedOn(),
                            notification.getUpdatedOn(),
                            notification.getRetryNumber()
                    ))
                    .update(keyHolder);

            Assert.state(updated == 1, "Failed to create Notification, table is empty");

            log.info("#NOTIFICATIONS-D-C - END save Notification.");

            return Objects.requireNonNull(keyHolder.getKey()).intValue();

        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - create(Notification notification) ");
            throw new NotificationException(e.toString());
        }
    }

    /*
    @Override
    public Integer count() throws NotificationException {
        try {
            return jdbcClient.sql("select notification_id from notification").query().listOfRows().size();
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - count() ");
            throw new NotificationException(e.toString());
        }
    }*/

    @Override
    public List<NotificationDTO> getAllNotificationDTOsLiFoByMessageCreatorId() throws NotificationException {
        try {
            // Get the Authentication object from SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedUserEmail = authentication.getName(); // Get email from authentication
            String messageCreatorId = iUserService.findUserByEmail(loggedUserEmail).getId();

            return jdbcClient.sql("SELECT n.* " +
                            "FROM notification n " +
                            "JOIN message m ON n.message_id = m.message_id " +
                            "WHERE m.message_creator_id = :messageCreatorId " +
                            "ORDER BY n.notification_id DESC")
                    .param("messageCreatorId", messageCreatorId)
                    .query(NotificationDTO.class)
                    .list();
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - List<Notification> getAllNotificationDTOsLiFoByMessageCreatorId() ");
            throw new NotificationException(e.toString());
        }
    }

    @Override
    public List<NotificationDTO> getAllNotificationDTOsLiFo() throws NotificationException {
        try {
            return jdbcClient.sql("select * from notification order by notification_id desc")
                    .query(NotificationDTO.class)
                    .list();
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - List<Notification> findAllNotificationDTOsLiFo() ");
            throw new NotificationException(e.toString());
        }
    }
}

