package com.springsecurity.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springsecurity.security.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long>{
    PasswordResetToken findByToken(String token);
    
}
