package com.hackathon.ai.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.hackathon.ai.entity.Notification;
import com.hackathon.ai.entity.User;
import com.hackathon.ai.repository.NotificationRepository;
import com.hackathon.ai.repository.UserRepository;
import com.hackathon.ai.service.NotificationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	
	private UserRepository userRepo;
	private NotificationRepository notiRepo;

	Notification buildNotification(String message, User user) {
		return Notification.builder().message(message).dateTime(LocalDateTime.now()).isRead(false).user(user).build();
	}

	@Override
	public void createNotification(String message, String userName) {

		
		
		
		User users = userRepo.findByUserName(userName).orElseThrow(()-> new RuntimeException("User not found"));
		
		Notification notification = buildNotification(message, users);
		notiRepo.save(notification);
		users.getSetOfNotifications().add(notification);
		userRepo.save(users);
	
		
	}

	}


