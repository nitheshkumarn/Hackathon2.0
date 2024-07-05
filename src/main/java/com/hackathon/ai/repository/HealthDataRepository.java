package com.hackathon.ai.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.HealthData;
import com.hackathon.ai.entity.User;

public interface HealthDataRepository extends JpaRepository<HealthData, Integer> {

  boolean existsByDate(LocalDate date);



HealthData findByDateAndUser(LocalDate date, User user);



HealthData findByDate(LocalDate date);

}