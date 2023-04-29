package com.sportser.sportserheartratesensordatacollector.services;

import com.sportser.sportserheartratesensordatacollector.config.KafkaConfig;
import com.sportser.sportserheartratesensordatacollector.dto.HeartRateUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaConfig kafkaConfig;

    private final Logger logger = LoggerFactory.getLogger("hr-data-collector-service");

    @Autowired
    public KafkaProducerService(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public void sendMessage(HeartRateUserDto heartRateUserDto) {
        kafkaConfig.kafkaTemplate().send(kafkaConfig.getKafkaTopicConsume(), heartRateUserDto);
        logger.info(String.format("heart rates data sent to Kafka %s", heartRateUserDto.toString()));
    }
}
