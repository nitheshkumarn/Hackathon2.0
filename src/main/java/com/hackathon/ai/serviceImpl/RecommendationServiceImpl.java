package com.hackathon.ai.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hackathon.ai.entity.HealthData;
import com.hackathon.ai.entity.Recommendation;
import com.hackathon.ai.entity.User;
import com.hackathon.ai.exception.UserNotFoundException;
import com.hackathon.ai.repository.HealthDataRepository;
import com.hackathon.ai.repository.RecommendationRepository;
import com.hackathon.ai.repository.UserRepository;
import com.hackathon.ai.responsedto.RecommendationResponse;
import com.hackathon.ai.service.RecommendationService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

	HealthDataRepository healthDataRepo;
	UserRepository userRepo;

	RecommendationRepository recoRepo;
	ResponseStructure<RecommendationResponse> structure;

	@Override
	public ResponseEntity<ResponseStructure<RecommendationResponse>> generateRecommendation() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		User user = userRepo.findByUserName(userName).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		LocalDate date = LocalDate.now();
		HealthData healthData = healthDataRepo.findByDateAndUser(date, user);

		String recommendationText = generateRecommendationText(healthData);

		Recommendation recommendation = Recommendation.builder().text(recommendationText).dateTime(LocalDateTime.now())
				.user(user).healthData(healthData).build();

		recommendation = recoRepo.save(recommendation);

		RecommendationResponse recommendationResponse = RecommendationResponse.builder()
				.recId(recommendation.getRecId()).text(recommendationText).dateTime(recommendation.getDateTime())
				.build();

		structure.setData(recommendationResponse);
		structure.setMessage("Recommendation Created SuccessFully");
		structure.setStatus(HttpStatus.CREATED.value());

		return new ResponseEntity<ResponseStructure<RecommendationResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<RecommendationResponse>> getRecommendation() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		User user = userRepo.findByUserName(userName).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		LocalDate date = LocalDate.now();

		Recommendation recommendation = recoRepo.findByDateTimeAndUser(date, user);

		RecommendationResponse recommendationResponse = RecommendationResponse.builder()
				.recId(recommendation.getRecId()).text(recommendation.getText()).dateTime(recommendation.getDateTime())
				.build();
		
		structure.setData(recommendationResponse);
		structure.setMessage("Recommendation Created SuccessFully");
		structure.setStatus(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure<RecommendationResponse>>(structure, HttpStatus.CREATED);
	}

	private String generateRecommendationText(HealthData healthData) {
		StringBuilder recommendationText = new StringBuilder();

		if (healthData.getExerciseMinutes() < 30) {
			recommendationText.append("Try to exercise for at least 30 minutes a day. ");
		} else {
			recommendationText.append("Great job on your exercise routine! ");
		}

		if (healthData.getSleepHours() < 7) {
			recommendationText.append("Ensure you get at least 7 hours of sleep. ");
		} else {
			recommendationText.append("Your sleep duration is adequate. ");
		}

		if (!healthData.getDiet().toLowerCase().contains("vegetables")) {
			recommendationText.append("Include more vegetables in your diet. ");
		} else {
			recommendationText.append("Your diet looks balanced. ");
		}

		return recommendationText.toString();
	}
}
