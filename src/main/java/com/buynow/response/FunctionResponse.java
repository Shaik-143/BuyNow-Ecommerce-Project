package com.buynow.response;

import com.buynow.dto.OrderHistory;
import com.buynow.model.Cart;
import com.buynow.model.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionResponse {
    private String functionName;
    private Cart userCart;
    private OrderHistory orderHistory;
    private Product product;
}
