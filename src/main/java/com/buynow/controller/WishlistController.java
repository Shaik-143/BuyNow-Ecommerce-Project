package com.buynow.controller;

import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.buynow.exception.ProductException;
import com.buynow.exception.UserException;
import com.buynow.exception.WishlistNotFoundException;
import com.buynow.model.Product;
import com.buynow.model.User;
import com.buynow.model.Wishlist;
import com.buynow.service.ProductService;
import com.buynow.service.UserService;
import com.buynow.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Wishlist> createWishlist(@RequestBody User user) {
        Wishlist wishlist = wishlistService.createWishlist(user);
        return ResponseEntity.ok(wishlist);
    }
    
    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}") // for removing also u can use it
    public ResponseEntity<Wishlist> addProductToWishlist(@PathVariable Long productId,@RequestHeader("Authorization") String jwt)
    		throws WishlistNotFoundException, ProductException, UserException {

        Product product = productService.findProductById(productId);
        User user=userService.findUserProfileByJwt(jwt);
        Wishlist updatedWishlist = wishlistService.addProductToWishlist(user,product);
        return ResponseEntity.ok(updatedWishlist);
    }
}