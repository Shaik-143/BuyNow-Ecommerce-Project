package com.buynow.exception;

public class CategoryNotFoundException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoryNotFoundException(String categoryNotFound) {
        super(categoryNotFound);
    }
}
