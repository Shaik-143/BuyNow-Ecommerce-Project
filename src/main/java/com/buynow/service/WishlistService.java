package com.buynow.service;


import com.buynow.exception.WishlistNotFoundException;
import com.buynow.model.Product;
import com.buynow.model.User;
import com.buynow.model.Wishlist;

//import java.util.Optional;

public interface WishlistService {

    Wishlist createWishlist(User user);

    Wishlist getWishlistByUserId(User user);

    Wishlist addProductToWishlist(User user, Product product) throws WishlistNotFoundException;

}

