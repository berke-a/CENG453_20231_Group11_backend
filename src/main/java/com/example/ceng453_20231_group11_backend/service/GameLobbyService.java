package com.example.ceng453_20231_group11_backend.service;

import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.entity.GameLobby;
import com.example.ceng453_20231_group11_backend.enums.GameState;
import com.example.ceng453_20231_group11_backend.repository.GameLobbyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GameLobbyService {
    private final GameLobbyRepository gameLobbyRepository;

    public GameLobbyService(GameLobbyRepository gameLobbyRepository) {
        this.gameLobbyRepository = gameLobbyRepository;
    }

    public Pair<HttpStatus, ResponseDTO> createLobby() {
        try {
            GameLobby newLobby = GameLobby.builder()
                    .gameState(GameState.WAITING)
                    .playerCount(0)
                    .build();
            GameLobby savedLobby = gameLobbyRepository.save(newLobby);
            return Pair.of(HttpStatus.OK, new ResponseDTO(savedLobby, "Lobby created successfully.", "success"));
        } catch (Exception e) {
            return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseDTO(null, "Error creating the lobby: " + e.getMessage(), "error"));
        }
    }

    public Pair<HttpStatus, ResponseDTO> getAllLobbies() {
        try {
            List<GameLobby> lobbies = gameLobbyRepository.findAll();
            return Pair.of(HttpStatus.OK, new ResponseDTO(lobbies, "Lobbies retrieved successfully.", "success"));
        } catch (Exception e) {
            return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseDTO(null, "Error retrieving lobbies: " + e.getMessage(), "error"));
        }
    }

    public Pair<HttpStatus, ResponseDTO> deleteLobby(Long lobbyId) {
        try {
            Optional<GameLobby> lobby = gameLobbyRepository.findById(lobbyId);
            if (lobby.isPresent()) {
                gameLobbyRepository.delete(lobby.get());
                return Pair.of(HttpStatus.OK, new ResponseDTO(null, "Lobby deleted successfully.", "success"));
            } else {
                return Pair.of(HttpStatus.NOT_FOUND, new ResponseDTO(null, "Lobby not found.", "fail"));
            }
        } catch (Exception e) {
            return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseDTO(null, "Error deleting the lobby: " + e.getMessage(), "error"));
        }
    }

}
