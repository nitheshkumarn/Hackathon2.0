package com.hackathon.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.ai.requestdto.UserRequest;
import com.hackathon.ai.responsedto.UserResponse;
import com.hackathon.ai.service.UserService;
import com.hackathon.ai.util.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5175/")

@RequestMapping("/api/v1/users")
public class UserController {
	
	private UserService us;

	@PostMapping("/register")
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(@RequestBody UserRequest userRequest) {
		return us.registerUser(userRequest);
	}


}
