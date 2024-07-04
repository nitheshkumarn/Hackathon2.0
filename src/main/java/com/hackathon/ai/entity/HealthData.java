package com.hackathon.ai.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HealthData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer healthId;
	
	private LocalDate date;
	private String diet;
	private int exerciseMinutes;
	private int sleepHours;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	

}

