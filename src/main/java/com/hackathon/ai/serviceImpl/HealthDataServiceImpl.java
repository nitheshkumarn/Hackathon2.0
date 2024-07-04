package com.hackathon.ai.serviceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hackathon.ai.entity.HealthData;
import com.hackathon.ai.entity.User;
import com.hackathon.ai.repository.HealthDataRepository;
import com.hackathon.ai.repository.UserRepository;
import com.hackathon.ai.requestdto.HealthDataRequest;
import com.hackathon.ai.responsedto.HealthDataResponse;
import com.hackathon.ai.service.HealthDataService;
import com.hackathon.ai.service.NotificationService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HealthDataServiceImpl implements HealthDataService {

	ResponseStructure<HealthDataResponse> structure;

	private HealthDataRepository healthDataRepo;
	private UserRepository userRepo;
	private NotificationService notiService;

	private HealthData mapToHealthData(HealthDataRequest healthDataRequest, User user) {
		return HealthData.builder().date(healthDataRequest.getDate()).diet(healthDataRequest.getDiet())
				.exerciseMinutes(healthDataRequest.getExerciseMinutes()).sleepHours(healthDataRequest.getSleepHours())
				.user(user).build();
	}

	private HealthDataResponse mapToHealthDataResponse(HealthData healthData) {
		return HealthDataResponse.builder().healthId(healthData.getHealthId()).date(healthData.getDate())
				.diet(healthData.getDiet()).exerciseMinutes(healthData.getExerciseMinutes())
				.sleepHours(healthData.getSleepHours()).userId(healthData.getUser().getUserId()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<HealthDataResponse>> addHealthData(HealthDataRequest healthDataRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		return userRepo.findByUserName(userName).map(user -> {
			HealthData healthData = mapToHealthData(healthDataRequest,user);
			healthData = healthDataRepo.save(healthData);
			
			user.getSetOfhHealthData().add(healthData);
			user = userRepo.save(user);
			healthData.setUser(user);
			
			healthData = healthDataRepo.save(healthData);
			
			notiService.createNotification("Health Data for the day created", userName);
			
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Health Data for the Day Created");
			structure.setData(mapToHealthDataResponse(healthData));
			
			
			return new ResponseEntity<ResponseStructure<HealthDataResponse>>(structure, HttpStatus.CREATED);
			
		}).orElseThrow(() -> new RuntimeException("User not Found"));

		/*User user = userRepo.findByUserName(userName).orElseThrow(() -> new RuntimeException(" user not found"));
		HealthData healthData = mapToHealthData(healthDataRequest,user);
		healthData = healthDataRepo.save(healthData);
		user.getSetOfhHealthData().add(healthData);
		userRepo.save(user);
		

		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Health Data for the Day Created");
		structure.setData(mapToHealthDataResponse(healthData));

		return new ResponseEntity<ResponseStructure<HealthDataResponse>>(structure, HttpStatus.CREATED);*/
		
		
	}
	
	@Override
    public ResponseEntity<ResponseStructure<HealthDataResponse>> updateHealthData(Integer healthId, HealthDataRequest healthDataRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return userRepo.findByUserName(userName).map(user -> {
            HealthData existingHealthData = healthDataRepo.findById(healthId)
                .orElseThrow(() -> new RuntimeException("Health data not found"));

            if (!existingHealthData.getUser().equals(user)) {
                throw new RuntimeException("Unauthorized access");
            }

            existingHealthData.setDate(healthDataRequest.getDate());
            existingHealthData.setDiet(healthDataRequest.getDiet());
            existingHealthData.setExerciseMinutes(healthDataRequest.getExerciseMinutes());
            existingHealthData.setSleepHours(healthDataRequest.getSleepHours());

            HealthData updatedHealthData = healthDataRepo.save(existingHealthData);
            
            notiService.createNotification("Health Data has been updated", userName);

            structure.setStatus(HttpStatus.OK.value());
            structure.setMessage("Health Data updated successfully");
            structure.setData(mapToHealthDataResponse(updatedHealthData));

            return new ResponseEntity<>(structure, HttpStatus.OK);
        }).orElseThrow(() -> new RuntimeException("User not Found"));
    }
	
	@Override
	public ResponseEntity<ResponseStructure<HealthDataResponse>> deleteHealthData(Integer healthId) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String userName = authentication.getName();


	    System.out.println("Username from authentication: " + userName);

	 
	    return userRepo.findByUserName(userName).map(user -> {
	        HealthData healthData = healthDataRepo.findById(healthId)
	                .orElseThrow(() -> new RuntimeException("Health data not found"));
	     
	        if (!healthData.getUser().equals(user)) {
	            throw new RuntimeException("Unauthorized access");
	        }

	   
	        healthDataRepo.deleteById(healthId);
	        
	        notiService.createNotification("Health Data has been Deleted", userName);

	  
	        structure.setStatus(HttpStatus.OK.value());
            structure.setMessage("Health Data deleted successfully");
            structure.setData(mapToHealthDataResponse(healthData));

	        return new ResponseEntity<>(structure, HttpStatus.OK);
	    }).orElseThrow(() -> new RuntimeException("User not found"));
	}

	}


