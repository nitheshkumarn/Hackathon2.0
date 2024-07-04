package com.hackathon.ai.service;


import org.springframework.http.ResponseEntity;

import com.hackathon.ai.requestdto.AuthRequest;
import com.hackathon.ai.requestdto.OTPModel;
import com.hackathon.ai.requestdto.UserRequest;
import com.hackathon.ai.responsedto.AuthResponse;
import com.hackathon.ai.responsedto.UserResponse;
import com.hackathon.ai.util.ResponseStructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPModel otpModel);

	ResponseEntity<ResponseStructure<AuthResponse>> login(String accessToken, String refreshToken,
			AuthRequest authRequest, HttpServletResponse response);

	ResponseEntity<ResponseStructure<HttpServletResponse>> logout(HttpServletRequest req, HttpServletResponse resp);
}
