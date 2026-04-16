package com.buynow.service;

import com.buynow.exception.UserException;
import com.buynow.model.User;

public interface UserService{

	public User findUserProfileByJwt(String jwt) throws UserException;
	
	public User findUserByEmail(String email) throws UserException;

}
