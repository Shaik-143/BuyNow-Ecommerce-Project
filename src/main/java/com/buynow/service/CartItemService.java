package com.buynow.service;

import com.buynow.exception.CartItemException;
import com.buynow.exception.UserException;
import com.buynow.model.CartItem;


public interface CartItemService {
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException;
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
	
}
