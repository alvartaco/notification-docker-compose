package dev.alvartaco.notifications.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.alvartaco.notifications.model.dto.MessageDTO;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageProducer {

    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);
    public static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public void send(String categoryId, String messageBody, String messageCreatorId) {
        try (KafkaProducer<String, String> messageProducer = new KafkaProducer<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,broker:29092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        ))) {

            MessageDTO messageDTO = MessageDTO.builder()
                    .categoryId(categoryId)
                    .messageBody(messageBody)
                    .messageCreatorId(messageCreatorId)
                    .build();

            messageProducer.send(new ProducerRecord<>("messages",
                    messageDTO.getCategoryId(), toJson(messageDTO))).get();
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - Error sending message{}", e.getMessage());
        }
    }

    @SneakyThrows
    private static void send(KafkaProducer<String, String> messageProducer, ProducerRecord<String, String> record) {
        messageProducer.send(record).get();
    }

    @SneakyThrows
    private static String toJson(MessageDTO t) {
        return OBJECT_MAPPER.writeValueAsString(t);
    }
}
