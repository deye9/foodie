package com.foodie.kafka.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafKaTopicListeners {
    @KafkaListener(topics = "foodie-exception", groupId = "exception-group")
    public void listenUserTopic(Object message) {
        log.info("Received message in foodie-exception: {}", message);
    }

    @KafkaListener(topics = "foodie-users", groupId = "foodie-users-group")
    public void listenUsersTopic(Object message) {
        log.info("Received message in foodie-users: {}", message);
    }

    @KafkaListener(topics = "foodie-payment", groupId = "foodie-payment-group")
    public void listenPaymentTopic(Object message) {
        log.info("Received message in foodie-payment: {}", message);
    }
}