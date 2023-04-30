package com.sportser.sportserheartratesensordatacollector.controller;

import com.sportser.common.dto.HeartRateUserDto;
import com.sportser.sportserheartratesensordatacollector.services.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@Slf4j
@RequestMapping("")
public class HeartRateSensorController {

    @Autowired
    KafkaProducerService kafkaProducerService;

    @PostMapping
    public void addHeartRate(@RequestBody HeartRateUserDto heartRateUserDto) {
        heartRateUserDto.setTime(new Timestamp(System.currentTimeMillis()));
        kafkaProducerService.sendMessage(heartRateUserDto);
    }
}

