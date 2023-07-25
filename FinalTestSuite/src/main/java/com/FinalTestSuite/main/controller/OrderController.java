package com.FinalTestSuite.main.controller;

import java.util.List;

import javax.persistence.criteria.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinalTestSuite.main.entity.OrderEntity;
import com.FinalTestSuite.main.entity.User;
import com.FinalTestSuite.main.repository.OrderRepository;
import com.FinalTestSuite.main.repository.UserRepository;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<OrderEntity> createOrder(@PathVariable Long userId, @RequestBody OrderEntity order) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        order.setUser(user);
        OrderEntity savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }


 
}