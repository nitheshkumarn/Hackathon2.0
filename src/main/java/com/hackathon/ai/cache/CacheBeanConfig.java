package com.hackathon.ai.cache;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hackathon.ai.entity.User;



@Configuration
public class CacheBeanConfig {

	@Bean
	CacheStore<User> userCacheStore() {

		return new CacheStore<User>(Duration.ofMinutes(5));
		
	}
	
	@Bean
	CacheStore<String> OTPCacheStore(){
		
		return new CacheStore<>(Duration.ofMinutes(5));
	}

}