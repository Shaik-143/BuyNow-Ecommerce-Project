package com.buynow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.model.Deal;

public interface DealRepository extends JpaRepository<Deal,Long> {

}
