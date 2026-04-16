package com.buynow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.model.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {

    PaymentOrder findByPaymentLinkId(String paymentId);
}
