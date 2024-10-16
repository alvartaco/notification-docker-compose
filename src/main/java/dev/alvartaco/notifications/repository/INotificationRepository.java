package dev.alvartaco.notifications.repository;

import dev.alvartaco.notifications.model.dto.NotificationDTO;
import dev.alvartaco.notifications.exception.NotificationException;
import dev.alvartaco.notifications.model.Notification;

import java.util.List;

/**
 * Interface to handle different types of Notification Repositories
 */
public interface INotificationRepository {

        Integer create(Notification notification) throws NotificationException;
        List<NotificationDTO> findAllNotificationDTOsLiFo() throws NotificationException;
}
