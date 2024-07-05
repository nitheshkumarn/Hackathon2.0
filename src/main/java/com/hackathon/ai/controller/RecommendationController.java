package com.hackathon.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.ai.responsedto.RecommendationResponse;
import com.hackathon.ai.service.RecommendationService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5175/")

@RequestMapping("/api/v1/recommendation")
public class RecommendationController {
	
	private RecommendationService recService;
	
	@PostMapping("/generate")
	public ResponseEntity<ResponseStructure<RecommendationResponse>> generateRecommendation(){
		
		return recService.generateRecommendation();
		
	}
	
	@GetMapping("/get")
	public ResponseEntity<ResponseStructure<RecommendationResponse>> getRecommendation(){
		
		return recService.getRecommendation();
		
	}
	
	

}
