package com.foodie;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.GenericContainer;

@Testcontainers
public class TestSetupKafka implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    // Define a Kafka container with specific environment variables
    @Container
    public static GenericContainer<?> kafkaContainer = new GenericContainer<>(
            ("bitnami/kafka:3.6"))
            .withEnv("KAFKA_ENABLE_KRAFT", "yes")
            .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true")
            .withEnv("KAFKA_CREATE_TOPICS", "foodie-exception:foodie-users:foodie-payment:input-topic:output-topic");

    @Override
    public void close() throws Throwable {
        // Stop the kafka container
        kafkaContainer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        if (started)
            return;

        // Fire up the kafka Container
        kafkaContainer.start();

        // Set system properties for the Spring datasource to use the Kafka container
        System.setProperty("spring.kafka.trusted-packages", "*");
        System.setProperty("spring.kafka.group-id-config", "test");
        System.setProperty("spring.kafka.reset-config", "earliest");
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getHost() + ":" + kafkaContainer.getMappedPort(9040));

        // Set system properties for the Spring Consumer Config
        System.setProperty("spring.kafka.consumer.group-id", "test");
        System.setProperty("spring.kafka.consumer.enable-auto-commit", "false");
        System.setProperty("spring.kafka.consumer.auto-offset-reset", "earliest");
        System.setProperty("spring.kafka.consumer.bootstrap-servers", kafkaContainer.getHost() + ":" + kafkaContainer.getMappedPort(9040));
        System.setProperty("spring.kafka.consumer.properties.spring.json.trusted.packages", "*");
        System.setProperty("spring.kafka.consumer.key-deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        System.setProperty("spring.kafka.consumer.value-deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer");

        // Set system properties for the Spring Producer Config
        System.setProperty("spring.kafka.producer.bootstrap-servers", kafkaContainer.getHost() + ":" + kafkaContainer.getMappedPort(9040));
        System.setProperty("spring.kafka.producer.key-serializer", "org.apache.kafka.common.serialization.StringSerializer");
        System.setProperty("spring.kafka.producer.value-serializer", "org.springframework.kafka.support.serializer.JsonSerializer");

        // Set system properties for the Spring Streams Config
        System.setProperty("spring.kafka.streams.application-id", "test-stream");
        System.setProperty("spring.kafka.streams.properties[\"default.key.serde\"]", "org.apache.kafka.common.serialization.Serdes$StringSerde");
        System.setProperty("spring.cloud.stream.kafka.binder.brokers", kafkaContainer.getHost() + ":" + kafkaContainer.getMappedPort(9040));

        // Store this instance in the global context store
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("TestSetupKafka", this);
    }

    @Test
    void contextLoads() {
        assertEquals(2, 1 + 1);
    }
}