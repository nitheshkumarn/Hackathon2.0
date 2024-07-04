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
public class GoalResponse {
	
	private Integer goalId;
	private String description;
	private LocalDate targetDate;
	private boolean achieved;
	private int userId;

}