package com.foodie.kafka.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class KafkaConsumerServiceTest {

    @Mock
    KafkaConsumer<String, List<Object>> mockConsumer;

    KafkaConsumerService consumerService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        consumerService = new KafkaConsumerService(mockConsumer);
    }

    @Test
    public void testGetTopicMessages() {
        String topic = "testTopic";
        ConsumerRecords<String, List<Object>> records = new ConsumerRecords<>(Collections.emptyMap());
        when(mockConsumer.poll(Duration.ofMillis(100))).thenReturn(records);

        List<Object> result = consumerService.getTopicMessages(topic);

        assertEquals(Collections.emptyList(), result);
        verify(mockConsumer).subscribe(Collections.singletonList(topic));
        verify(mockConsumer).poll(Duration.ofMillis(100));
    }

    @Test
    public void testGetTopicMessagesWithData() {
        String topic = "testTopic";
        List<Object> messages = Arrays.asList("message1", "message2");
        ConsumerRecord<String, List<Object>> record = new ConsumerRecord<>(topic, 0, 0, null, messages);
        ConsumerRecords<String, List<Object>> records = new ConsumerRecords<>(
                Collections.singletonMap(new TopicPartition(topic, 0), Collections.singletonList(record)));

        when(mockConsumer.poll(Duration.ofMillis(100))).thenReturn(records);

        List<Object> result = consumerService.getTopicMessages(topic);

        verify(mockConsumer, times(1)).subscribe(Collections.singletonList(topic));
        assertEquals(messages, result);
    }

}
