package com.zosh.exception;

public class WishlistNotFoundException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WishlistNotFoundException(String message){
        super(message);
    }
}
