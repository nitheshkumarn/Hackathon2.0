package com.hackathon.ai.service;

import org.springframework.http.ResponseEntity;

import com.hackathon.ai.responsedto.RecommendationResponse;
import com.hackathon.ai.util.ResponseStructure;

public interface RecommendationService {

	ResponseEntity<ResponseStructure<RecommendationResponse>> generateRecommendation();

	ResponseEntity<ResponseStructure<RecommendationResponse>> getRecommendation();

}
