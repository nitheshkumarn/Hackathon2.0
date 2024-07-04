package com.hackathon.ai.service;

import org.springframework.http.ResponseEntity;

import com.hackathon.ai.requestdto.GoalRequest;
import com.hackathon.ai.responsedto.GoalResponse;
import com.hackathon.ai.util.ResponseStructure;

public interface GoalService {

	ResponseEntity<ResponseStructure<GoalResponse>> addGoal(GoalRequest goalRequest);

	ResponseEntity<ResponseStructure<GoalResponse>> updateGoal(Integer id, GoalRequest goalRequest);

	ResponseEntity<ResponseStructure<GoalResponse>> deleteGoal(Integer id);

}

