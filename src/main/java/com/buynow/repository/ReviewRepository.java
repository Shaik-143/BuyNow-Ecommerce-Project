package com.buynow.repository;

//import com.zosh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findReviewsByUserId(Long userId);
    List<Review> findReviewsByProductId(Long productId);
}
