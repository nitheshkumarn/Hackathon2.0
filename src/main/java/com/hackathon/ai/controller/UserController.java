package com.hackathon.ai.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.ai.requestdto.AuthRequest;
import com.hackathon.ai.requestdto.OTPModel;
import com.hackathon.ai.requestdto.UserRequest;
import com.hackathon.ai.responsedto.AuthResponse;
import com.hackathon.ai.responsedto.UserResponse;
import com.hackathon.ai.service.UserService;
import com.hackathon.ai.util.ResponseStructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

	@PostMapping("/verify-otp")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(@RequestBody OTPModel otpModel) {
		return us.verifyOTP(otpModel);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> login(@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refreshToken, @RequestBody AuthRequest authRequest,
			HttpServletResponse response) {
		return us.login(accessToken, refreshToken, authRequest, response);
	}

	@PostMapping("/logout")
	public ResponseEntity<ResponseStructure<HttpServletResponse>> logout(HttpServletResponse resp,
			HttpServletRequest req) {
		return us.logout(req, resp);
	}

}



