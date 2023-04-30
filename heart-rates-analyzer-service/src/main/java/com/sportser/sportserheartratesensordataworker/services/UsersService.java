package com.sportser.sportserheartratesensordataworker.services;

import com.sportser.common.dto.HeartRateUserDto;
import com.sportser.sportserheartratesensordataworker.dto.UserDto;
import com.sportser.sportserheartratesensordataworker.model.Users;
import com.sportser.sportserheartratesensordataworker.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WebClient webClient;
    @Value("${address.user-profile}")
    private String ip;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Boolean checkSubscribing(String email) {
        return webClient
                .get()
                .uri(String.format("http://%s:8085/user/checkSubscribing?email=%s", ip, email))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    public Boolean checkEmergency(Integer value, Integer age) {
        // in sportive activity
        Integer limit = 222 - age;
        return value > limit;
    }

    public UserDto getUser(String email) {
        return webClient
                .get()
                .uri(String.format("http://%s:8085/user/%s", ip, email))
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public Users analyseData(HeartRateUserDto heartRateUserDto) {
        if (heartRateUserDto.getUserEmail() != null && heartRateUserDto.getUserEmail().length() > 0) {
            if (checkSubscribing(heartRateUserDto.getUserEmail()) != null
                && checkSubscribing(heartRateUserDto.getUserEmail())) {

                UserDto userDto = getUser(heartRateUserDto.getUserEmail());
                if (checkEmergency(heartRateUserDto.getHeartRate(), userDto.getAge())) {
                    kafkaProducerService.sendMessage(heartRateUserDto);
                }
            }
            Users user = new Users();
            user.setEmail(heartRateUserDto.getUserEmail());
            user.setHeartRate(heartRateUserDto.getHeartRate());
            user.setTime(heartRateUserDto.getTime());
            return usersRepository.save(user);
        }
        return null;
    }

}
