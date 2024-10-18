package dev.alvartaco.notifications.notificationengine.strategies;

import dev.alvartaco.notifications.model.ConstChannelTypes;
import dev.alvartaco.notifications.notificationengine.INotificationEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(ConstChannelTypes.PUSH_NOTIFICATION)
public class PushNotificationEngineService implements INotificationEngineService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationEngineService.class);

    @Override
    public void sendNotification() {
        log.info("#NOTIFICATIONS-D-C - NOTIFICATION - Sent by Push Notification");
    }
}
