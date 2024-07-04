package com.hackathon.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.Goal;

public interface GoalRepository extends JpaRepository<Goal, Integer> {

}

