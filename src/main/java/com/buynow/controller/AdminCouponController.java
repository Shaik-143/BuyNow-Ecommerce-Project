package com.buynow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.buynow.model.Cart;
import com.buynow.model.Coupon;
import com.buynow.model.User;
import com.buynow.service.CouponService;
import com.buynow.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;
    private final UserService userService;
    //private final CartService cartService; 

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(@RequestParam String apply,@RequestParam String code,
    		@RequestParam double orderValue,@RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Cart cart;

        if(apply.equals("true")){
            cart = couponService.applyCoupon(code,orderValue,user); // it can be used for applying 
        }
        else {
            cart = couponService.removeCoupon(code,user);// it can be used for removing the coupon
        }
        return ResponseEntity.ok(cart);
    }

    // Admin operations
    
    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupon);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }
}