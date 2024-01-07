package com.example.ceng453_20231_group11_backend.controller;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.service.GameLobbyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/gamelobbies", headers = "Accept=application/json")
public class GameLobbyController {
    private final GameLobbyService gameLobbyService;

    public GameLobbyController(GameLobbyService gameLobbyService) {
        this.gameLobbyService = gameLobbyService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create Game Lobby",
            description = "Creates a new game lobby.")
    public ResponseEntity<ResponseDTO> createLobby() {
        try {
            Pair<HttpStatus, ResponseDTO> response = gameLobbyService.createLobby();
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } catch (Exception e) {
            log.error("Error during game lobby creation: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error during game lobby creation: " + e.getMessage(), APIConstants.RESPONSE_FAIL));
        }
    }

    @GetMapping
    @Operation(summary = "List Game Lobbies",
            description = "Retrieves a list of all available game lobbies.")
    public ResponseEntity<ResponseDTO> getAllLobbies() {
        try {
            Pair<HttpStatus, ResponseDTO> response = gameLobbyService.getAllLobbies();
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } catch (Exception e) {
            log.error("Error retrieving game lobbies: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error retrieving game lobbies: " + e.getMessage(), APIConstants.RESPONSE_FAIL));
        }
    }

    @DeleteMapping("/{lobbyId}")
    @Operation(summary = "Delete Game Lobby",
            description = "Deletes a specific game lobby based on the provided lobby ID.")
    public ResponseEntity<ResponseDTO> deleteLobby(@PathVariable Long lobbyId) {
        try {
            Pair<HttpStatus, ResponseDTO> response = gameLobbyService.deleteLobby(lobbyId);
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } catch (Exception e) {
            log.error("Error deleting game lobby: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error deleting game lobby: " + e.getMessage(), APIConstants.RESPONSE_FAIL));
        }
    }
}
