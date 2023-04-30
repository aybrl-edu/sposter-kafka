package com.sportser.sportseruserprofileserver.repositories;

import com.sportser.sportseruserprofileserver.entities.CoachEntity;
import com.sportser.sportseruserprofileserver.entities.CoachingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachingRepository extends JpaRepository<Integer, CoachingEntity> {
}
