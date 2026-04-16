package com.buynow.service;

import javax.naming.AuthenticationException;

import com.buynow.exception.ReviewNotFoundException;
import com.buynow.model.Product;
import com.buynow.model.Review;
import com.buynow.model.User;
import com.buynow.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest req,
                        User user,
                        Product product);

    List<Review> getReviewsByProductId(Long productId);

    Review updateReview(Long reviewId,
                        String reviewText,
                        double rating,
                        Long userId) throws ReviewNotFoundException, AuthenticationException;


    void deleteReview(Long reviewId, Long userId) throws ReviewNotFoundException, AuthenticationException;

}
