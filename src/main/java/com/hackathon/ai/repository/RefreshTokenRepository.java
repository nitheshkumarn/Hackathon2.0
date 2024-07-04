package com.hackathon.ai.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.ai.entity.RefreshToken;
import com.hackathon.ai.entity.User;



public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	public Optional<RefreshToken> findByToken(String rt);

	public List<RefreshToken> findByExpirationBefore(LocalDateTime expiry);

	public List<RefreshToken> findByUserAndIsBlocked(User user, boolean b);

	public List<RefreshToken> findAllByUserAndIsBlockedAndTokenNot(User user, boolean b, String refreshToken);

}