package com.sportser.sportserheartratesensordataworker.services;

import com.sportser.common.dto.HeartRateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KafkaConsumerService {

    private final Logger logger = LoggerFactory.getLogger("hr-data-analyzer-service");

    @Autowired
    private UsersService usersService;


    @KafkaListener(topics = "${spring.kafka.consumer.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(HeartRateUserDto heartRateUserDto)  {
        logger.info("message received from Kafka topic : {}", heartRateUserDto);
        usersService.analyseData(heartRateUserDto);
    }
}
