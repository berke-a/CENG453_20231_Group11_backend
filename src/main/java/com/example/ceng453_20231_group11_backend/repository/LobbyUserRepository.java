package com.example.ceng453_20231_group11_backend.repository;

import com.example.ceng453_20231_group11_backend.entity.LobbyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LobbyUserRepository extends JpaRepository<LobbyUser, Long> {
    Optional<LobbyUser> findByUserIdAndLobbyId(Long userId, Long lobbyId);

    List<LobbyUser> findAllByLobbyId(Long lobbyId);
}
