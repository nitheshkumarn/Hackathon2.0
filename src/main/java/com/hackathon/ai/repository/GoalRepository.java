package com.hackathon.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.Goal;
import com.hackathon.ai.entity.User;

public interface GoalRepository extends JpaRepository<Goal, Integer> {

	List<Goal> findAllGoalsByUser(User user);

	

}

