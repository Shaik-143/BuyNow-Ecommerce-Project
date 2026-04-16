package com.buynow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.domain.PayoutsStatus;
import com.buynow.model.Payouts;

import java.util.List;

public interface PayoutsRepository extends JpaRepository<Payouts,Long> {

    List<Payouts> findPayoutsBySellerId(Long sellerId);
    List<Payouts> findAllByStatus(PayoutsStatus status);
}
