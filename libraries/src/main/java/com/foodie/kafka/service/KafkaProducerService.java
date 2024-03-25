package com.foodie.kafka.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import jakarta.validation.Valid;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaAdmin kafkaAdmin;

    private final KafkaConsumerService kafkaConsumerService;

    private final KafkaConsumer<String, List<Object>> kafkaConsumer;

    private final KafkaTemplate<String, List<Object>> kafkaTemplate;

    public KafkaProducerService(KafkaAdmin kafkaAdmin, KafkaConsumerService kafkaConsumerService,
            KafkaConsumer<String, List<Object>> kafkaConsumer, KafkaTemplate<String, List<Object>> kafkaTemplate) {
        this.kafkaAdmin = kafkaAdmin;
        this.kafkaConsumer = kafkaConsumer;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    public boolean topicExists(String topicName) {

        log.info("Checking if topic exists: {}", topicName);

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaAdmin.getConfigurationProperties().get(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG));

        try (AdminClient adminClient = AdminClient.create(props)) {
            return adminClient.listTopics().names().get().contains(topicName);
        } catch (InterruptedException | ExecutionException e) {

            log.error("Error while checking if topic exists: {}", e.getMessage());

            return false;
        }
    }

    public void saveMessageToTopic(String topicName, @Valid List<Object> value) {

        log.info("Saving message {} to topic: {}", value, topicName);

        var future = kafkaTemplate.send(topicName, value);

        future.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                future.completeExceptionally(exception);
            } else {
                future.complete(sendResult);
            }
            log.info("Message sent to Kafka topic : " + value);
        });
    }

    public List<String> getAllTopics() throws ExecutionException, InterruptedException {

        log.info("Retrieving all topics");

        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {

            ListTopicsResult topicsResult = adminClient.listTopics();
            Set<String> topicNames = topicsResult.names().get();
            return new ArrayList<>(topicNames);
        }
    }

    public void createTopic(String topicName) throws ExecutionException, InterruptedException {

        log.info("Creating topic: {}", topicName);

        Map<String, String> topicConfig = new HashMap<>();

        topicConfig.put(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(24 * 60 * 60 * 1000)); // 24 hours retention
        NewTopic newTopic = new NewTopic(topicName, 1, (short) 1).configs(topicConfig);

        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        }
        kafkaConsumer.subscribe(Collections.singletonList(topicName));
    }

    public void deleteTopic(String topicName) throws ExecutionException, InterruptedException {

        log.info("Deleting topic: {}", topicName);

        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            adminClient.deleteTopics(Collections.singletonList(topicName)).all().get();
        }
    }

    public List<Object> getTopicMessages(String topicName) throws ExecutionException, InterruptedException {

        log.info("Retrieving messages from topic: {}", topicName);

        return kafkaConsumerService.getTopicMessages(topicName);
    }
}