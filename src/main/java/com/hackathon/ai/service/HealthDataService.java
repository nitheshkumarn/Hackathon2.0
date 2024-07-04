package com.hackathon.ai.service;

import org.springframework.http.ResponseEntity;

import com.hackathon.ai.requestdto.HealthDataRequest;
import com.hackathon.ai.responsedto.HealthDataResponse;
import com.hackathon.ai.util.ResponseStructure;

public interface HealthDataService {

	ResponseEntity<ResponseStructure<HealthDataResponse>> addHealthData(HealthDataRequest healthDataRequest);

	ResponseEntity<ResponseStructure<HealthDataResponse>> updateHealthData(Integer healthId,
			HealthDataRequest healthDataRequest);

	ResponseEntity<ResponseStructure<HealthDataResponse>> deleteHealthData(Integer healthId);

}
