package com.FinalTestSuite.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FinalTestSuite.main.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
   
}