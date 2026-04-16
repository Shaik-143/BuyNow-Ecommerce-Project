package com.buynow.controller;

import com.buynow.domain.PaymentMethod;
import com.buynow.exception.OrderException;
import com.buynow.exception.SellerException;
import com.buynow.exception.UserException;
import com.buynow.model.*;
import com.buynow.repository.PaymentOrderRepository;
import com.buynow.response.PaymentLinkResponse;
import com.buynow.service.*;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	private final OrderItemService orderItemService;
	private final CartService cartService;
	private final PaymentService paymentService;
	private final PaymentOrderRepository paymentOrderRepository;
	private final SellerReportService sellerReportService;
	private final SellerService sellerService;

	@PostMapping()
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(
			@RequestBody Address spippingAddress,
			@RequestParam PaymentMethod paymentMethod,
			@RequestHeader("Authorization")String jwt)
            throws UserException, RazorpayException, StripeException {
		
		User user = userService.findUserProfileByJwt(jwt);
		Cart cart = cartService.findUserCart(user);
		Set<Order> orders = orderService.createOrder(user, spippingAddress, cart);

		PaymentOrder paymentOrder=paymentService.createOrder(user,orders);

		PaymentLinkResponse res = new PaymentLinkResponse();

		if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
			PaymentLink payment = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
			String paymentUrl=payment.get("short_url");
			String paymentUrlId=payment.get("id");

			res.setPayment_link_url(paymentUrl);
			res.setPayment_link_id(paymentUrlId);
			paymentOrder.setPaymentLinkId(paymentUrlId);
			paymentOrderRepository.save(paymentOrder);
		}
		else{
			String paymentUrl=paymentService.createStripePaymentLink(user,
					paymentOrder.getAmount(),
					paymentOrder.getId());
			res.setPayment_link_url(paymentUrl);
		}
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/user")
	public ResponseEntity< List<Order>> usersOrderHistoryHandler(
			@RequestHeader("Authorization") String jwt) throws UserException{
		
		User user=userService.findUserProfileByJwt(jwt);
		List<Order> orders=orderService.usersOrderHistory(user.getId());
		
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity< Order> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) 
			throws OrderException, UserException{	
		//User user = userService.findUserProfileByJwt(jwt);
		Order orders=orderService.findOrderById(orderId);
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED); // http status 202 accepted
	}

	@GetMapping("/item/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId, @RequestHeader("Authorization")String jwt) 
			throws Exception {
		System.out.println("------- controller ");
		//User user = userService.findUserProfileByJwt(jwt);
		OrderItem orderItem=orderItemService.getOrderItemById(orderItemId);
		return new ResponseEntity<>(orderItem,HttpStatus.ACCEPTED); // http status 202 accepted
	}

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) 
			throws UserException, OrderException, SellerException {
		User user=userService.findUserProfileByJwt(jwt);
		Order order=orderService.cancelOrder(orderId,user);

		Seller seller= sellerService.getSellerById(order.getSellerId()); // repo comming
		SellerReport report=sellerReportService.getSellerReport(seller); // repo comming

		report.setCanceledOrders(report.getCanceledOrders()+1);
		report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(report);

		return ResponseEntity.ok(order); // http status 200 ok
	}
}