package com.hackathon.ai.exceptionHandler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hackathon.ai.exception.HealthDataAlreadyExists;
import com.hackathon.ai.exception.UserAlreadyLoggedException;
import com.hackathon.ai.exception.UserAlreadyRegisteredException;
import com.hackathon.ai.exception.UserNotFoundException;

@RestControllerAdvice
public class ApplicationHandler extends ResponseEntityExceptionHandler {
    private ResponseEntity<Object> structure(HttpStatus status, String message, Object rootCause) {
        return new ResponseEntity<Object>(Map.of("status", status.value(), "message", message, "rootCause", rootCause), status);
    }
    
    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<Object> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException exception) {
        return structure(HttpStatus.BAD_REQUEST, exception.getMessage(), "User Already Exists");
    }
    
    @ExceptionHandler(UserAlreadyLoggedException.class)
    public ResponseEntity<Object> handleUserAlreadyLoggedException(UserAlreadyLoggedException exception) {
    	return structure(HttpStatus.BAD_REQUEST,exception.getMessage(),"User Already Logged in");
    }
    
    @ExceptionHandler(HealthDataAlreadyExists.class)
    public ResponseEntity<Object> handleHealthDataAlreadyExistsException(HealthDataAlreadyExists exception){
    	return structure(HttpStatus.BAD_REQUEST,exception.getMessage(),"HealthData Already Exists");
    }
    
    
    @ExceptionHandler( UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException( UserNotFoundException exception){
    	return structure(HttpStatus.NOT_FOUND,exception.getMessage(),"User Not Found");
    }
    
}