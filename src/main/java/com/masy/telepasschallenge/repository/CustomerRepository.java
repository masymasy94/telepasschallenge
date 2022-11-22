package com.masy.telepasschallenge.repository;

import com.masy.telepasschallenge.data.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
