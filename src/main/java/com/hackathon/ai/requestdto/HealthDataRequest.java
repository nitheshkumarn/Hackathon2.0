package com.hackathon.ai.requestdto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthDataRequest {
	
	private LocalDate date;
	private String diet;
	private int exerciseMinutes;
	private int sleepHours;

}
