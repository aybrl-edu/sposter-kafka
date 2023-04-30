package com.sportser.sportserheartratesensordataworker.services;


import com.sportser.sportserheartratesensordataworker.config.KafkaConfig;
import com.sportser.common.dto.HeartRateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {

    private final Logger logger = LoggerFactory.getLogger("hr-data-analyzer-service");

    @Autowired
    private KafkaTemplate<String, HeartRateUserDto> kafkaTemplate;

    @Autowired
    private KafkaConfig kafkaConfig;

    public void sendMessage(HeartRateUserDto heartRateUserDto) {
        String kafkaTopicEmergency = kafkaConfig.getKafkaTopicEmergency();
        kafkaTemplate.send(kafkaTopicEmergency, heartRateUserDto);
        logger.info("Message sent to Kafka topic {} message : {}", kafkaTopicEmergency, heartRateUserDto);
    }
}
