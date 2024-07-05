package com.hackathon.ai.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.Recommendation;
import com.hackathon.ai.entity.User;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {



Recommendation findByDateTimeAndUser(LocalDate date, User user);

}
