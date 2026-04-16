package com.buynow.service.impl;


import lombok.RequiredArgsConstructor;

//import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.buynow.config.JwtProvider;
import com.buynow.exception.UserException;
import com.buynow.model.User;
import com.buynow.repository.PasswordResetTokenRepository;
import com.buynow.repository.UserRepository;
import com.buynow.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {


	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final JavaMailSender javaMailSender;
	
//	public UserServiceImplementation(
//			UserRepository userRepository,
//			JwtProvider jwtProvider,
//			PasswordEncoder passwordEncoder,
//			PasswordResetTokenRepository passwordResetTokenRepository,
//			JavaMailSender javaMailSender) {
//		
//		this.userRepository=userRepository;
//		this.jwtProvider=jwtProvider;
//		this.passwordEncoder=passwordEncoder;
//		this.passwordResetTokenRepository=passwordResetTokenRepository;
//		this.javaMailSender=javaMailSender;
//		
//	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		String email=jwtProvider.getEmailFromJwtToken(jwt);
		
		User user = userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UserException("user not exist with email "+email);
		}
		return user;
	}
	
	@Override
	public User findUserByEmail(String username) throws UserException {
		
		User user=userRepository.findByEmail(username);
		
		if(user!=null) {
			return user;
		}
		
		throw new UserException("user not exist with username "+username);
	}
 // edited here and constructor modified
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public PasswordResetTokenRepository getPasswordResetTokenRepository() {
		return passwordResetTokenRepository;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}
}