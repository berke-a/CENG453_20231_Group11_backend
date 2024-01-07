package com.example.ceng453_20231_group11_backend.repository;

import com.example.ceng453_20231_group11_backend.entity.GameLobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameLobbyRepository extends JpaRepository<GameLobby, Long> {
}
