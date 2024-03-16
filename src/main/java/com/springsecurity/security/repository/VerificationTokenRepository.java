package com.springsecurity.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springsecurity.security.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long>{

    VerificationToken findByToken(String token);

}
