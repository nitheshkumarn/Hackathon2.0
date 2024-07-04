package com.hackathon.ai.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.AccessToken;
import com.hackathon.ai.entity.User;


public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {

	public Optional<AccessToken> findByToken(String at);

	public List<AccessToken> findByExpirationBefore(LocalDateTime expiry);

	public Optional<AccessToken> findByTokenAndIsBlocked(String at, boolean b);

	public List<AccessToken> findByUserAndIsBlocked(User user, boolean b);

	public List<AccessToken> findAllByUserAndIsBlockedAndTokenNot(User user, boolean b, String accessToken);

}
