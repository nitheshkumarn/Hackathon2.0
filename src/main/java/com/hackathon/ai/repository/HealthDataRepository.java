package com.hackathon.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.HealthData;

public interface HealthDataRepository extends JpaRepository<HealthData, Integer> {

}