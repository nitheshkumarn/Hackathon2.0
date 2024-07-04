package com.hackathon.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
