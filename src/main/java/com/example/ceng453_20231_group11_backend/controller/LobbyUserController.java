package com.example.ceng453_20231_group11_backend.controller;

import com.example.ceng453_20231_group11_backend.dto.LobbyUserDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.service.LobbyUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/lobbyusers", headers = "Accept=application/json")
public class LobbyUserController {
    private final LobbyUserService lobbyUserService;

    public LobbyUserController(LobbyUserService lobbyUserService) {
        this.lobbyUserService = lobbyUserService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add User to Lobby",
            description = "Adds a user to a game lobby.")
    public ResponseEntity<ResponseDTO> addUserToLobby(@RequestBody LobbyUserDTO lobbyUserDTO) {
        try {
            Pair<HttpStatus, ResponseDTO> response = lobbyUserService.addUserToLobby(lobbyUserDTO);
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } catch (Exception e) {
            log.error("Error adding user to lobby: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error adding user to lobby: " + e.getMessage(), "fail"));
        }
    }

    @GetMapping("/{lobbyId}")
    @Operation(summary = "List Users in Lobby",
            description = "Retrieves a list of users in a specific game lobby.")
    public ResponseEntity<ResponseDTO> getUsersInLobby(@PathVariable Long lobbyId) {
        try {
            Pair<HttpStatus, ResponseDTO> response = lobbyUserService.getUsersInLobby(lobbyId);
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } catch (Exception e) {
            log.error("Error retrieving users in lobby: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error retrieving users in lobby: " + e.getMessage(), "fail"));
        }
    }

    @DeleteMapping("/{lobbyId}/remove/{userId}")
    @Operation(summary = "Remove User from Lobby",
            description = "Removes a user from a specific game lobby.")
    public ResponseEntity<ResponseDTO> removeUserFromLobby(@PathVariable Long lobbyId, @PathVariable Long userId) {
        try {
            Pair<HttpStatus, ResponseDTO> response = lobbyUserService.removeUserFromLobby(userId, lobbyId);
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } catch (Exception e) {
            log.error("Error removing user from lobby: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error removing user from lobby: " + e.getMessage(), "fail"));
        }
    }

}
