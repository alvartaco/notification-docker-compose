package dev.alvartaco.notifications.notificationengine.strategies;

import dev.alvartaco.notifications.model.ConstChannelTypes;
import dev.alvartaco.notifications.notificationengine.INotificationEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service(ConstChannelTypes.EMAIL)
public class EmailNotificationEngineService implements INotificationEngineService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationEngineService.class);

    @Override
    public void sendNotification() {
        log.info("#NOTIFICATIONS-D-C - NOTIFICATION - Sent by EMAIL");
    }
}
