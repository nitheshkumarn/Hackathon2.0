package com.hackathon.ai.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAlreadyLoggedException extends RuntimeException{
	
	private String message;

}
