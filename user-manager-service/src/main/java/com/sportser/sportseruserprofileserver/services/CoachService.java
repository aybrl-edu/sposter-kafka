package com.sportser.sportseruserprofileserver.services;

import com.sportser.common.dto.CoachDTO;
import com.sportser.sportseruserprofileserver.entities.CoachEntity;
import com.sportser.sportseruserprofileserver.repositories.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoachService {
    @Autowired
    private CoachRepository coachRepository;

    public CoachEntity getCoachByUserEmail(String userEmail) {
        return coachRepository.findCoachByUserEmail(userEmail);
    }
}
