package dev.alvartaco.notifications.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.alvartaco.notifications.model.dto.MessageDTO;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageProducer {

    public static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public void send(String categoryId, String messageBody) {
        try (KafkaProducer<String, String> messageProducer = new KafkaProducer<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,broker:29092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        ))) {

            List<MessageDTO> data1 = List.of(
                    MessageDTO.builder()
                            .categoryId(categoryId)
                            .messageBody(messageBody)
                            .build()
            );

            data1.stream()
                    .map(t -> new ProducerRecord<>("messages", t.getCategoryId(), toJson(t)))
                    .forEach(record -> send(messageProducer, record));
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
