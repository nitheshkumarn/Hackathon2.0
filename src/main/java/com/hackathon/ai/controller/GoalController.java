package com.hackathon.ai.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.ai.requestdto.GoalRequest;
import com.hackathon.ai.responsedto.GoalResponse;
import com.hackathon.ai.service.GoalService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5175/")

@RequestMapping("/api/v1/goal")
public class GoalController {
	
	private GoalService gs;
	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<GoalResponse>> addGoal(@RequestBody GoalRequest goalRequest) {
		
		return gs.addGoal(goalRequest);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseStructure<GoalResponse>> updateGoal(@PathVariable Integer id, @RequestBody GoalRequest goalRequest) {
		return gs.updateGoal(id, goalRequest);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseStructure<GoalResponse>> deleteGoal(@PathVariable Integer id) {
		return gs.deleteGoal(id);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<ResponseStructure<List<GoalResponse>>> getGoals() {
		return gs.getGoals();
	}
	

}
