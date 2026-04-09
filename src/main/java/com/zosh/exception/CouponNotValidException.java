package com.zosh.exception;

public class CouponNotValidException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CouponNotValidException(String message) {
        super(message);
    }
}
