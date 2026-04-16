package com.buynow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
	PasswordResetToken findByToken(String token);
}
