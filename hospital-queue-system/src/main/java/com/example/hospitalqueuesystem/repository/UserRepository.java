package com.example.hospitalqueuesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospitalqueuesystem.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByUsername(String username);

}