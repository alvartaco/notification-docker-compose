package dev.alvartaco.notifications.notificationengine.factories;

import dev.alvartaco.notifications.notificationengine.INotificationEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * A factory class for creating instances of
 * INotificationEngineService based on a provided notification type.
 */
@Component
public class NotificationEngineFactory {

    private static final Logger log = LoggerFactory.getLogger(NotificationEngineFactory.class);

    /**
     * A map that contains INotificationEngineService instances
     * mapped to their corresponding notification types.
     */
    private final Map<String, INotificationEngineService> iNotificationServiceMap;

    /**
     * Uses constructor-autowiring to create a new NotificationEngineFactory instance
     * with the provided map of INotificationEngineService instances.
     *
     * @param iNotificationServices a map of INotificationEngineService instances
     */
    public NotificationEngineFactory(Map<String, INotificationEngineService> iNotificationServices) {
        this.iNotificationServiceMap = iNotificationServices;
    }

    /**
     * Returns the INotificationEngineService instance
     * corresponding to the provided notification type.
     *
     * @param notificationType the type of notification
     * @return the INotificationEngineService instance corresponding to the provided notification type
     * @throws RuntimeException if the provided notification type is not supported
     */
    public INotificationEngineService getNotificationService(String notificationType) {
        INotificationEngineService iNotificationEngineService = iNotificationServiceMap.get(notificationType);
        if (iNotificationEngineService == null) {
            log.error("#NOTIFICATIONS-D-C - Error getting notification services.");
            throw new RuntimeException("Unsupported notification type");
        }
        return iNotificationEngineService;
    }

    /**
     * Executes the sendNotification() method on the NotificationService
     * instance corresponding to the provided notification type.
     * @param notificationType the type of notification to execute
     * @throws RuntimeException if the provided notification type is not supported
     */
    public void execute(String notificationType) {
        INotificationEngineService notificationService = getNotificationService(notificationType);
        notificationService.sendNotification();
    }
}