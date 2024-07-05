package com.hackathon.ai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.Goal;
import com.hackathon.ai.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	boolean existsByUserEmail(String userEmail);

	Optional<User> findByUserName(String username);

	

	List<Goal> findAllGoalsByUserId(Integer userId);

}
