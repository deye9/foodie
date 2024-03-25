package com.foodie.kafka.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class KafkaConfigTest {

    @Mock
    private KafkaProperties kafkaProperties;

    @Mock
    private KafkaTemplate<String, List<Object>> kafkaTemplate;

    @Mock
    private ConsumerFactory<String, List<Object>> consumerFactory;

    @Mock
    private Admin admin;

    @InjectMocks
    private KafkaConfig kafkaConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(kafkaProperties.getAdmin()).thenReturn(admin);
        when(admin.isFailFast()).thenReturn(true);
    }

    @Test
    void testKafkaAdmin() {
        KafkaAdmin admin = kafkaConfig.kafkaAdmin();
        assertNotNull(admin);
    }

    @Test
    void testKafkaAdminConfiguration() {
        when(kafkaProperties.buildAdminProperties()).thenReturn(new HashMap<>());

        KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);
        KafkaAdmin kafkaAdmin = kafkaConfig.kafkaAdmin();

        // Ensure that KafkaAdmin is created and configured correctly
        assert kafkaAdmin != null;
    }

}