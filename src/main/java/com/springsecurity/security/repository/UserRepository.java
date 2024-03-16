package com.springsecurity.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springsecurity.security.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
}
