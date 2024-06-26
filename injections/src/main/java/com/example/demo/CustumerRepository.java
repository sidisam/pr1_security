package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustumerRepository extends JpaRepository<Customer,Long> {
    public List<Customer> findCustomerByName(String name);
}
