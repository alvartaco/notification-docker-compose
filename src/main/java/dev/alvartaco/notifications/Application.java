package dev.alvartaco.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * System capable of receiving messages, which will have a
 * category and the body of the message. These messages will need to be forwarded to
 * the system's users, who will already be pre-populated. In addition to being subscribed
 * to message categories, these users will have specified the channels through which they
 * would like to be notified, such as SMS, Email or Push Notification.
 *
 * @author alvartaco
 *
 */
@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("#NOTIFICATIONS-D-C - Application Started.");
    }

}
