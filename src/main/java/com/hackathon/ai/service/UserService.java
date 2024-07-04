package com.hackathon.ai.service;


import org.springframework.http.ResponseEntity;

import com.hackathon.ai.requestdto.UserRequest;
import com.hackathon.ai.responsedto.UserResponse;
import com.hackathon.ai.util.ResponseStructure;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest);
}
