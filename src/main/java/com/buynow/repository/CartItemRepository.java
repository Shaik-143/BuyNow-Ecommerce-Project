package com.buynow.repository;

//import com.zosh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.model.Cart;
import com.buynow.model.CartItem;
import com.buynow.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);


}
