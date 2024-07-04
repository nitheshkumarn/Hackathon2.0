package com.hackathon.ai.util;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component
public class ResponseStructure <T> {

	private Integer status;
	private String message;
	private T data;
	
	public Integer getStatus() {
		return status;
	}
	public ResponseStructure<T> setStatus(Integer status) {
		this.status = status;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public ResponseStructure<T> setMessage(String message) {
		this.message = message;
		return this;
	}
	public T getData() {
		return data;
	}
	public ResponseStructure<T> setData(T data) {
		this.data = data;
		return this;
	}
	
}
