package com.sportser.sportseruserprofileserver.controller;

import com.sportser.common.dto.CoachDTO;
import com.sportser.common.dto.CoachPresence;
import com.sportser.sportseruserprofileserver.config.KafkaConfig;
import com.sportser.sportseruserprofileserver.entities.CoachEntity;
import com.sportser.sportseruserprofileserver.services.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coach")
public class CoachController {

    @Autowired
    private KafkaTemplate<String, CoachPresence> kafkaTemplate;

    @Autowired
    private KafkaConfig kafkaConfig;

    @Autowired
    private CoachService coachService;


    @PostMapping("/present")
    public ResponseEntity<String> setCoachPresence(@RequestBody CoachPresence coachPresence){
        try {
            kafkaTemplate.send(kafkaConfig.getKafkaTopicCoachPresence(), coachPresence);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("ok", HttpStatus.OK);

    }

    @GetMapping("/by-user-email/{userEmail}")
    public ResponseEntity<CoachDTO> getCoachFromUserEmail(@PathVariable("userEmail") String userEmail){
        CoachEntity coach = coachService.getCoachByUserEmail(userEmail);
        CoachDTO coachDTO = new CoachDTO();
        if (coach != null) {
            coachDTO.setEmail(coach.getEmail());
            coachDTO.setIdCoach(coach.getIdCoach());
            coachDTO.setFirstName(coach.getFirstName());
            coachDTO.setLastName(coach.getLastName());

            return ResponseEntity.ok(coachDTO);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
