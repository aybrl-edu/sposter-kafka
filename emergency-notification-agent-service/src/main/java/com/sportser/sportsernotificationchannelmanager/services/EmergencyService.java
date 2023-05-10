package com.sportser.sportsernotificationchannelmanager.services;

import com.sportser.common.dto.CoachDTO;
import com.sportser.common.dto.HeartRateUserDto;
import com.sportser.sportsernotificationchannelmanager.redis.model.Emergency;
import com.sportser.sportsernotificationchannelmanager.redis.model.Registration;
import com.sportser.sportsernotificationchannelmanager.redis.repo.EmergencyRepository;
import com.sportser.sportsernotificationchannelmanager.redis.repo.RegistrationRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class EmergencyService {

    @Autowired
    private WebClient webClient;

    private final EmergencyRepository emergencyRepository;
    private final RegistrationRepository registrationRepository;
    private final MQTTService mqttService;


    @Value("${address.user-profile}")
    private String ip;

    public EmergencyService(EmergencyRepository usersRepository, RegistrationRepository registrationRepository, MQTTService mqttService) {
        this.emergencyRepository= usersRepository;
        this.registrationRepository = registrationRepository;
        this.mqttService = mqttService;
    }

    public void registration(String email){
        Registration coachRegistration = new Registration(email, new Timestamp(System.currentTimeMillis()).toString());
        //Saving the registration in cache
        registrationRepository.save(coachRegistration);
        //Checking if there are messages directed to this given coach in cache and sending them
        Optional<Emergency> coachEmergency = emergencyRepository.findById(email);
        if(coachEmergency.isPresent()){
            System.out.println("Coach Present!");
            for(String message : coachEmergency.get().getMessages()) {
                mqttService.publishMessage("emergency-topic", message);
            }
            emergencyRepository.delete(coachEmergency.get());
        }
    }

    public CoachDTO getCoachFromUserEmail(String email) {
        return webClient
                .get()
                .uri(String.format("http://%s:8085/coach/by-user-email/%s", ip, email))
                .retrieve()
                .bodyToMono(CoachDTO.class)
                .block();
    }

    public void receiveEmergency(HeartRateUserDto emergencyDto){
        String messageStr = String.format("Le client %s  a atteint une fréquence cardiaque de %s à %s",
                emergencyDto.getUserEmail(),
                emergencyDto.getHeartRate(),
                emergencyDto.getTime());


        CoachDTO coach = getCoachFromUserEmail(emergencyDto.getUserEmail());
        String coachMail = coach.getEmail();

        String message = new MQTTMessage(coachMail, messageStr).toString();

        Optional<Registration> coachRegistration = registrationRepository.findById(coachMail);
        // The user is logged in and his token is in cache, sending the message
        if(coachRegistration.isPresent()){
            mqttService.publishMessage("emergency-topic", message);
        }
        // The user is not logged in, saving the message in cache
        else {
            Optional<Emergency> coachEmergency = emergencyRepository.findById(coachMail);
            //An instance of emergency already exists for this coach
            if(coachEmergency.isPresent()){
                coachEmergency.get().getMessages().add(message);
            }
            //Creating an emergency list for this coach
            else {
                System.out.println("Creating emergency in cache");
                Emergency e = new Emergency(coachMail);
                e.getMessages().add(message);
                emergencyRepository.save(e);
            }
            log.info("Saved message to emergency cache");
        }
    }

    public String unregister(String email){
        registrationRepository.deleteById(email);
        return "Successfully unregistered";
    }

    @Scheduled(fixedRate = 10000)
    public void checkTokenTimeout() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Iterable<Registration> registrationList = registrationRepository.findAll();
        for(Registration r : registrationList){
            if(Duration.between(Timestamp.valueOf(r.getLoginTime()).toLocalDateTime(),currentTime.toLocalDateTime()).getSeconds()>600){
                //The token has been added 10minutes ago, removing it from cache
                registrationRepository.delete(r);
            }
        }
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
class MQTTMessage {
    private String coachMail;
    private String message;
}


@Configuration
class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }
}