package com.example.ceng453_20231_group11_backend.service;

import com.example.ceng453_20231_group11_backend.dto.LobbyUserDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserInLobbyDTO;
import com.example.ceng453_20231_group11_backend.entity.GameLobby;
import com.example.ceng453_20231_group11_backend.entity.LobbyUser;
import com.example.ceng453_20231_group11_backend.entity.User;
import com.example.ceng453_20231_group11_backend.repository.GameLobbyRepository;
import com.example.ceng453_20231_group11_backend.repository.LobbyUserRepository;
import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LobbyUserService {
    private final LobbyUserRepository lobbyUserRepository;
    private final GameLobbyRepository gameLobbyRepository;
    private final UserRepository userRepository;

    public LobbyUserService(LobbyUserRepository lobbyUserRepository, GameLobbyRepository gameLobbyRepository, UserRepository userRepository) {
        this.lobbyUserRepository = lobbyUserRepository;
        this.gameLobbyRepository = gameLobbyRepository;
        this.userRepository = userRepository;
    }

    public Pair<HttpStatus, ResponseDTO> addUserToLobby(LobbyUserDTO lobbyUserDTO) {
        try {
            Long userId = lobbyUserDTO.getUserId();
            Long lobbyId = lobbyUserDTO.getLobbyId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("User not found"));
            GameLobby lobby = gameLobbyRepository.findById(lobbyId)
                    .orElseThrow(() -> new Exception("Lobby not found"));

            // Check if lobby already has 4 players
            if (lobby.getPlayerCount() >= 4) {
                return Pair.of(HttpStatus.BAD_REQUEST, new ResponseDTO(null, "Lobby is full.", "fail"));
            }

            lobby.setPlayerCount(lobby.getPlayerCount() + 1);
            gameLobbyRepository.save(lobby);

            LobbyUser lobbyUser = LobbyUser.builder()
                    .user(user)
                    .lobby(lobby)
                    .build();

            lobbyUserRepository.save(lobbyUser);
            return Pair.of(HttpStatus.OK, new ResponseDTO(lobbyUser, "User added to lobby successfully.", "success"));
        } catch (Exception e) {
            return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseDTO(null, "Error adding user to lobby: " + e.getMessage(), "error"));
        }
    }

    public Pair<HttpStatus, ResponseDTO> removeUserFromLobby(Long userId, Long lobbyId) {
        try {
            Optional<LobbyUser> lobbyUser = lobbyUserRepository.findByUserIdAndLobbyId(userId, lobbyId);

            if (lobbyUser.isPresent()) {
                lobbyUserRepository.delete(lobbyUser.get());

                GameLobby lobby = gameLobbyRepository.findById(lobbyId)
                        .orElseThrow(() -> new Exception("Lobby not found"));
                lobby.setPlayerCount(lobby.getPlayerCount() - 1);
                gameLobbyRepository.save(lobby);

                return Pair.of(HttpStatus.OK, new ResponseDTO(null, "User removed from lobby successfully.", "success"));
            } else {
                return Pair.of(HttpStatus.NOT_FOUND, new ResponseDTO(null, "User or Lobby not found.", "fail"));
            }
        } catch (Exception e) {
            return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseDTO(null, "Error removing user from lobby: " + e.getMessage(), "error"));
        }
    }

    public Pair<HttpStatus, ResponseDTO> getUsersInLobby(Long lobbyId) {
        try {
            List<LobbyUser> lobbyUsers = lobbyUserRepository.findAllByLobbyId(lobbyId);
            List<UserInLobbyDTO> userInLobbyDTOs = lobbyUsers.stream()
                    .map(lobbyUser -> new UserInLobbyDTO(
                            lobbyUser.getUser().getId(),
                            lobbyUser.getUser().getUsername()
                    ))
                    .collect(Collectors.toList());

            return Pair.of(HttpStatus.OK, new ResponseDTO(userInLobbyDTOs, "Users in lobby retrieved successfully.", "success"));

        } catch (Exception e) {
            return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseDTO(null, "Error retrieving users in lobby: " + e.getMessage(), "error"));
        }
    }

}
