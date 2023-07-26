package com.FinalTestSuite.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalTestSuite.main.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    
}
