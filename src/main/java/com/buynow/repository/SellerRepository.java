package com.buynow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.domain.AccountStatus;
import com.buynow.model.Seller;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}
