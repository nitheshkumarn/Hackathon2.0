package com.hackathon.ai.responsedto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthDataResponse {
	
	private Integer healthId;
	private LocalDate date;
	private String diet;
	private int exerciseMinutes;
	private int sleepHours;
	private int userId;

}
