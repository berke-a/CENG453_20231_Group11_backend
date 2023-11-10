package com.example.ceng453_20231_group11_backend.service;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.entity.User;
import com.example.ceng453_20231_group11_backend.enums.Role;
import com.example.ceng453_20231_group11_backend.mapper.UserMapper;
import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Pair<HttpStatus, ResponseDTO> handleLogin(Authentication authRequest) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authRequest.getPrincipal();
        Optional<User> user = userRepository.findByUsername(principal.getUsername());
        if (user.isPresent()) {
            UserDTO userDTO = userMapper.map(user.get());
            return Pair.of(HttpStatus.OK, new ResponseDTO(userDTO, null, APIConstants.RESPONSE_SUCCESS));
        }
        log.warn("User not found with username:{}", principal.getUsername());
        return Pair.of(HttpStatus.NOT_FOUND, new ResponseDTO(null,
                String.format("User not found with username:%s", principal.getUsername()), APIConstants.RESPONSE_FAIL));
    }

    public Pair<HttpStatus, ResponseDTO> registerUser(UserDTO userDTO) {
        String validationResult = validateRegisterFields(userDTO);
        if (validationResult.isEmpty()) {
            if (userRepository.findByUsername(userDTO.getUsername()).isEmpty()) {
                // User user = userMapper.toUser(userDTO); // TODO: Mapper not working
                User user = new User();
                user.setUsername(userDTO.getUsername());
                user.setPassword(userDTO.getPassword());
                user.setRole(Role.USER);
                userRepository.save(user);
                UserDTO userDTOResponse = new UserDTO();
                userDTOResponse.setUsername(user.getUsername());
                userDTOResponse.setPassword(user.getPassword());
                // UserDTO userDTOResponse = userMapper.map(user); // TODO: Mapper not working
                return Pair.of(HttpStatus.OK, new ResponseDTO(userDTOResponse,
                        String.format("Player is successfully created with username:%s", user.getUsername()), APIConstants.RESPONSE_SUCCESS));
            } else {
                log.warn("Username exists, could not complete user registration for username:{}", userDTO.getUsername());
                return Pair.of(HttpStatus.OK, new ResponseDTO(null,
                        String.format("Username already exists.\nPlease choose another and try again",
                                userDTO.getUsername()), APIConstants.RESPONSE_FAIL));
            }
        } else {
            return Pair.of(HttpStatus.OK, new ResponseDTO(null,
                    String.format("Validation Error on registation.\nFollowing constraints must be met: %s", validationResult),
                    APIConstants.RESPONSE_FAIL));
        }
    }

    private String validateRegisterFields(UserDTO userDTO) {
        if (userDTO == null) {
            return "Missing player register credentials";
        }

        String result = "";
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            result += "Username cannot be empty.\n";
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            result += "Password cannot be empty.\n";
        }
        return result;
    }

}
