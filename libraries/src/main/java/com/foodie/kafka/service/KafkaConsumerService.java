package com.foodie.kafka.service;

import java.time.Duration;
import java.util.*;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    final KafkaConsumer<String, List<Object>> consumer;

    public KafkaConsumerService(KafkaConsumer<String, List<Object>> consumer) {
        this.consumer = consumer;
    }

    public List<Object> getTopicMessages(String topic) {

        consumer.subscribe(Collections.singletonList(topic));
        List<Object> messageList = new ArrayList<>();

        do {
            ConsumerRecords<String, List<Object>> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, List<Object>> record : records) {

                if (!records.isEmpty()) {
                    
                    for (ConsumerRecord<String, List<Object>> stringMessageObjectConsumerRecord : records) {
                        messageList.addAll(stringMessageObjectConsumerRecord.value());
                    }
                }
            }
            break;
        } while (true);

        return messageList;
    }

}