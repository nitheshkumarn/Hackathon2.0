package com.hackathon.ai.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hackathon.ai.entity.Goal;
import com.hackathon.ai.entity.User;
import com.hackathon.ai.repository.GoalRepository;
import com.hackathon.ai.repository.NotificationRepository;
import com.hackathon.ai.repository.UserRepository;
import com.hackathon.ai.requestdto.GoalRequest;
import com.hackathon.ai.responsedto.GoalResponse;
import com.hackathon.ai.service.GoalService;
import com.hackathon.ai.service.NotificationService;
import com.hackathon.ai.util.ResponseStructure;


import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GoalServiceImpl implements GoalService {

	ResponseStructure<GoalResponse> structure;
	private ResponseStructure<List<GoalResponse>> listStructure;

	private UserRepository userRepo;
	private GoalRepository goalRepo;
	private NotificationService notiService;

	private Goal mapToGoal(GoalRequest goalRequest, User user) {
		return Goal.builder().description(goalRequest.getDescription()).targetDate(goalRequest.getTargetDate())
				.achieved(false).user(user).build();

	}

	private GoalResponse mapToGoalResponse(Goal goal) {
		return GoalResponse.builder().goalId(goal.getGoalId()).description(goal.getDescription())
				.targetDate(goal.getTargetDate()).achieved(false).userId(goal.getUser().getUserId()).build();
	}
	
	private List<GoalResponse> mapTOListOfGoalResponse(List<Goal> listOfGoals) {
		List<GoalResponse> listOfUserResponse = new ArrayList<>();

		listOfGoals.forEach(goal -> {
			GoalResponse gr = new GoalResponse();
			gr.setGoalId(goal.getGoalId());
			gr.setDescription(goal.getDescription());
			gr.setTargetDate(goal.getTargetDate());
			gr.setAchieved(false);
			gr.setUserId(goal.getUser().getUserId());
			listOfUserResponse.add(gr);
		});

		return listOfUserResponse;
	}

	@Override
	public ResponseEntity<ResponseStructure<GoalResponse>> addGoal(GoalRequest goalRequest) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();

		return userRepo.findByUserName(userName).map(user -> {
			Goal goal = mapToGoal(goalRequest, user);
			goal = goalRepo.save(goal);

			user.getSetOfGoals().add(goal);
			user = userRepo.save(user);
			goal.setUser(user);

			goal = goalRepo.save(goal);
			
			notiService.createNotification("Goal Added make sure you achieve it", userName);

			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Goal Has been set");
			structure.setData(mapToGoalResponse(goal));

			return new ResponseEntity<ResponseStructure<GoalResponse>>(structure, HttpStatus.CREATED);

		}).orElseThrow(() -> new RuntimeException("User not Found"));
	}

	
	@Override
	public ResponseEntity<ResponseStructure<GoalResponse>> updateGoal(Integer id, GoalRequest goalRequest) {
	    return goalRepo.findById(id).map(goal -> {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String userName = authentication.getName();

	        User user = userRepo.findByUserName(userName).orElseThrow(() -> new RuntimeException("User not Found"));

	        if (!goal.getUser().getUserId().equals(user.getUserId())) {
	            throw new RuntimeException("Unauthorized access");
	        }

	        goal.setDescription(goalRequest.getDescription());
	        goal.setTargetDate(goalRequest.getTargetDate());

	        goal = goalRepo.save(goal);

	        notiService.createNotification("Goal updated ", userName);
	        
	        structure.setStatus(HttpStatus.OK.value());
	        structure.setMessage("Goal updated successfully");
	        structure.setData(mapToGoalResponse(goal));

	        return new ResponseEntity<ResponseStructure<GoalResponse>>(structure, HttpStatus.OK);

	    }).orElseThrow(() -> new RuntimeException("Goal not Found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<GoalResponse>> deleteGoal(Integer id) {
	    return goalRepo.findById(id).map(goal -> {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String userName = authentication.getName();

	        User user = userRepo.findByUserName(userName).orElseThrow(() -> new RuntimeException("User not Found"));

	        if (!goal.getUser().getUserId().equals(user.getUserId())) {
	            throw new RuntimeException("Unauthorized access");
	        }

	        goalRepo.deleteById(id);
	        
	        notiService.createNotification("Goal deleted ", userName);

	        structure.setStatus(HttpStatus.OK.value());
	        structure.setMessage("Goal deleted successfully");
	        structure.setData(mapToGoalResponse(goal));

	        return new ResponseEntity<ResponseStructure<GoalResponse>>(structure, HttpStatus.OK);

	    }).orElseThrow(() -> new RuntimeException("Goal not Found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<GoalResponse>>> getGoals() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String userName = authentication.getName();
	        User user = userRepo.findByUserName(userName).orElseThrow(() -> new RuntimeException("User not Found"));
	        
	        List<Goal> goals = goalRepo.findAllGoalsByUser(user);
		
	        if (goals.isEmpty()) {
				listStructure.setStatus(HttpStatus.NOT_FOUND.value());
				listStructure.setMessage("No Goals found");
				listStructure.setData(mapTOListOfGoalResponse(goals));

				return new ResponseEntity<ResponseStructure<List<GoalResponse>>>(listStructure, HttpStatus.NOT_FOUND);
			} else {
				listStructure.setStatus(HttpStatus.FOUND.value());
				listStructure.setMessage("list of Goals found");
				listStructure.setData(mapTOListOfGoalResponse(goals));

				return new ResponseEntity<ResponseStructure<List<GoalResponse>>>(listStructure, HttpStatus.FOUND);
			}
	}
	
	
}




