package com.sportser.sportsernotificationchannelmanager.services;

import com.sportser.common.dto.CoachPresence;
import com.sportser.common.dto.EmergencyDto;
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
    private final Logger logger = LoggerFactory.getLogger("emergency-notification-agent");

    @Autowired
    private EmergencyService emergencyService;

    @KafkaListener(topics = "${app.kafka.topic.coach-presence-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactoryCoach")
    public void receiveMessage(CoachPresence coachPresence)  {
        logger.info("message received from Kafka topic : {}", coachPresence);
        if(coachPresence.getIsPresent()) {
            emergencyService.registration(coachPresence.getCoachMail());
        } else emergencyService.unregister(coachPresence.getCoachMail());
    }

    @KafkaListener(topics = "${app.kafka.topic.emergency-topic}",
                    groupId = "${spring.kafka.consumer.group-id}",
                    containerFactory = "kafkaListenerContainerFactoryEmergency")
    public void receiveMessageEmergency(HeartRateUserDto emergencyDto)  {
        logger.info("message received from Kafka topic : {}", emergencyDto);
        emergencyService.receiveEmergency(emergencyDto);
    }
}
