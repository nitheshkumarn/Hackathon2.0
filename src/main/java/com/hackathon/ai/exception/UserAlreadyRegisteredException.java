package com.hackathon.ai.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAlreadyRegisteredException extends RuntimeException {
	
	private String message;

}