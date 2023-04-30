package com.sportser.sportserheartratesensordataworker.repositories;

import com.sportser.sportserheartratesensordataworker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
}
