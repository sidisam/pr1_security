package com.example.demo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustumerRepository customerRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/search")
    public List<Customer> search(@RequestParam String name) {
        String sql = "SELECT u FROM Customer u WHERE u.name = '" + name + "'";
        Query query = entityManager.createQuery(sql);
        return query.getResultList();
    }
    // Solution number 1
    @GetMapping("/searchSecure1")
    public List<Customer> searchSecure(@RequestParam String name) {
        System.out.println(name);
        String sql = "SELECT u FROM Customer u WHERE u.name = :name";
        TypedQuery<Customer> query = entityManager.createQuery(sql, Customer.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
    // Solition number 2 (Best praxis) using JpaRepository
    @GetMapping("/searchSecure2")
    public List<Customer> searchSecure2(@RequestParam String name) {
            return this.customerRepository.findCustomerByName(name);
    }

    @PostConstruct
    public void addingTestDataToTheDb(){
        for (int i = 1; i <= 5; i++) {
            Customer customer =new Customer((long) i,"user"+i,"passwordOfUserNumber"+i);
            Customer save = customerRepository.save(customer);
        }
    }
}