package com.zosh.controller;

import com.razorpay.PaymentLink;
//import com.razorpay.RazorpayException;
//import com.stripe.exception.StripeException;
import com.zosh.domain.PaymentMethod;
//import com.zosh.exception.UserException;
import com.zosh.model.*;
import com.zosh.repository.CartItemRepository;
import com.zosh.repository.CartRepository;
import com.zosh.response.ApiResponse;
import com.zosh.response.PaymentLinkResponse;
//import com.zosh.response.PaymentLinkResponse;
import com.zosh.service.*;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.ArrayList;
import java.util.HashSet;
//import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment/")
public class PaymentController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @PostMapping("{paymentMethod}/order/{orderId}")
    public ResponseEntity<PaymentLinkResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentOrder order= paymentService.getPaymentOrderById(orderId);
        PaymentLink paymentResponse;
        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
        	paymentResponse = paymentService.createRazorpayPaymentLink(user,
                    order.getAmount(),
                    order.getId());
        }
        else {
            // This prevents the "silent null" response
            throw new Exception("Payment method " + paymentMethod + " is not supported yet.");
        }
//        else{
//            paymentResponse=paymentService.createStripePaymentLink(user,
//                    order.getAmount(),
//                    order.getId());
//        }
        PaymentLinkResponse res = new PaymentLinkResponse();
		String paymentUrl=paymentResponse.get("short_url");
		String paymentUrlId=paymentResponse.get("id");
		

		res.setPayment_link_url(paymentUrl);
		res.setPayment_link_id(paymentUrlId);
        paymentResponse = null;
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("{paymentId}") // EndPoint should be in this way http://localhost:5454/api/payment/pay_SVPMoIuowzuK8b?razorpay_payment_link_id=plink_SVPLUvaOZa3N6s
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam(name = "razorpay_payment_link_id") String paymentLinkId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        //PaymentLinkResponse paymentResponse;

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentLinkId(paymentLinkId);

        boolean paymentSuccess = paymentService.ProceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);
        
        if(paymentSuccess){
            for(Order order:paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller=sellerService.getSellerById(order.getSellerId());
                SellerReport report=sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
            Cart cart=cartRepository.findByUserId(user.getId());
            cart.setCouponPrice(0);
            cart.setCouponCode(null);
            Set<CartItem> items=cart.getCartItems();
            cartItemRepository.deleteAll(items);
            cart.setCartItems(new HashSet<>());
            cartRepository.save(cart);
        }     
        ApiResponse res = new ApiResponse();
        res.setMessage("Payment successful");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}