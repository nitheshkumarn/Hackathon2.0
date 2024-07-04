package com.hackathon.ai.serviceImpl;

import org.springframework.http.ResponseEntity;

import com.hackathon.ai.requestdto.UserRequest;
import com.hackathon.ai.responsedto.UserResponse;
import com.hackathon.ai.service.UserService;
import com.hackathon.ai.util.ResponseStructure;

public class UserServiceImpl implements UserService {

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {
		
		
		
		return null;
	}

}
