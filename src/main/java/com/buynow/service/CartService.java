package com.buynow.service;

import com.buynow.exception.ProductException;
import com.buynow.model.Cart;
import com.buynow.model.CartItem;
import com.buynow.model.Product;
import com.buynow.model.User;

public interface CartService {
	
	public CartItem addCartItem(User user, Product product, String size, int quantity) throws ProductException;
	
	public Cart findUserCart(User user);

}
