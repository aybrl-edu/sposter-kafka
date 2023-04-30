package com.sportser.sportseruserprofileserver.controller;

import com.sportser.common.dto.CoachPresence;
import com.sportser.sportseruserprofileserver.config.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coach")
public class CoachController {

    @Autowired
    private KafkaTemplate<String, CoachPresence> kafkaTemplate;

    @Autowired
    private KafkaConfig kafkaConfig;


    @PostMapping("/present")
    public ResponseEntity<String> setCoachPresence(@RequestBody CoachPresence coachPresence){
        try {
            kafkaTemplate.send(kafkaConfig.getKafkaTopicCoachPresence(), coachPresence);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("ok", HttpStatus.OK);

    }
}
