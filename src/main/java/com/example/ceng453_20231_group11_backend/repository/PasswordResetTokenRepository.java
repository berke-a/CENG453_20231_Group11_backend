package com.example.ceng453_20231_group11_backend.repository;

import com.example.ceng453_20231_group11_backend.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
