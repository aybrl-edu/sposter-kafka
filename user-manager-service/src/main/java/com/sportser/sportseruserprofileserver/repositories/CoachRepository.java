package com.sportser.sportseruserprofileserver.repositories;

import com.sportser.sportseruserprofileserver.entities.CoachEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CoachRepository extends CrudRepository<CoachEntity, Integer> {

    @Query(value = "SELECT c.* FROM coaches c JOIN coaching co ON c.id_coach = co.id_coach JOIN users u ON co.id_user = u.id_user WHERE u.email = :userEmail", nativeQuery = true)
    CoachEntity findCoachByUserEmail(@Param("userEmail") String userEmail);
}
