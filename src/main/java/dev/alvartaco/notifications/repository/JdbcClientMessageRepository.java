package dev.alvartaco.notifications.repository;

import dev.alvartaco.notifications.exception.MessageException;
import dev.alvartaco.notifications.exception.NotificationException;
import dev.alvartaco.notifications.model.Message;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Repository JDBC Client for Messages
 */
@Repository
public class JdbcClientMessageRepository implements IMessageRepository{

    private static final Logger log = LoggerFactory.getLogger(JdbcClientMessageRepository.class);
    private final JdbcClient jdbcClient;
    public JdbcClientMessageRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


//    public List<Message> findAllByMessageIdDesc() throws MessageException {
//        try {
//            return jdbcClient.sql("select * from message order by message_id desc")
//                    .query(Message.class)
//                    .list();
//        } catch (Exception e) {
//            log.error("#NOTIFICATIONS-D-C - List<Message> findAll() ");
//            throw new MessageException(e.toString());
//        }
//    }
//     public Optional<Message> findByMessageId(Integer messageId) throws MessageException {
//        try {
//            return jdbcClient.sql("SELECT message_id, category_id, message_body, message_created_on " +
//                            "FROM message WHERE message_id = :messageId" )
//                    .param("messageId", messageId)
//                    .query(Message.class)
//                    .optional();
//        } catch (Exception e) {
//            log.error("#NOTIFICATIONS-D-C - Optional<Message> findByMessageId() ");
//            throw new MessageException(e.toString());
//        }
//    }

    /**
     * It inserts a new message
     */
    public Integer create(@Valid Message message) throws MessageException{
        try {

            KeyHolder keyHolder = new GeneratedKeyHolder();

            var updated = jdbcClient.sql("INSERT INTO message(category_id, message_body, message_created_on) " +
                            "values (?,?,?)")
                    .params(List.of(message.category().getCategoryId(), message.messageBody(), message.createdOn()))
                    .update(keyHolder);
            Assert.state(updated == 1, "Failed to create Message, table is empty");

            log.info("#NOTIFICATIONS-D-C - END save message.");

            return Objects.requireNonNull(keyHolder.getKey()).intValue();

        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - create(Message message) ");
            throw new MessageException(e.toString());
        }
    }

    @Override
    public Message save(String categoryId, String messageBody) throws NotificationException, MessageException {
        return null;
    }

    /**
     * Number of Message Rows in the table
     *
    public Integer count() throws MessageException {
        try {
            return jdbcClient.sql("select message_id from message").query().listOfRows().size();
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - count() ");
            throw new MessageException(e.toString());
        }
    }
    */
}
