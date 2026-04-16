package com.buynow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buynow.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
