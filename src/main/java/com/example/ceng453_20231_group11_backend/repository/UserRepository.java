package com.example.ceng453_20231_group11_backend.repository;


import com.example.ceng453_20231_group11_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
