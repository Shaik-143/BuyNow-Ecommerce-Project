package com.buynow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.model.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByUserId(Long userId);
}
