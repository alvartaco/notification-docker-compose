package dev.alvartaco.notifications.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KafkaHealthService {

    private final KafkaConfig kafkaConfig;

    public KafkaHealthService(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public boolean isKafkaUp() {
        try {

            final AdminClient client = AdminClient.create(kafkaConfig.consumerConfigs());
            final String topic = "messages";
            final DescribeTopicsResult describeTopicsResult = client.describeTopics(Collections.singleton(topic));
            final KafkaFuture<TopicDescription> future = describeTopicsResult.values().get(topic);

            // for healthcheck purposes we're fetching the topic description
            future.get(1, TimeUnit.SECONDS);
            return true;
        } catch (final InterruptedException | ExecutionException | TimeoutException |
                       java.util.concurrent.TimeoutException e) {
            log.error("Kafka is not running {}", e.getMessage());
            return false;
        }
    }
}
