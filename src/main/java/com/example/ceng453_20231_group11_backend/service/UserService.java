package com.example.ceng453_20231_group11_backend.service;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.LoginDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.entity.User;
import com.example.ceng453_20231_group11_backend.enums.Role;
import com.example.ceng453_20231_group11_backend.mapper.UserMapper;
import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Pair<HttpStatus, ResponseDTO> handleLogin(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );

            User user = userRepository.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDTO userDTO = userMapper.map(user);

            return Pair.of(HttpStatus.OK, new ResponseDTO(userDTO, "User login successfully!", APIConstants.RESPONSE_SUCCESS));
        } catch (AuthenticationException e) {
            log.warn("Login failed for username: {}", loginDTO.getUsername());
            return Pair.of(HttpStatus.UNAUTHORIZED, new ResponseDTO(null, "Login failed: Invalid username or password", APIConstants.RESPONSE_FAIL));
        }
    }


    public Pair<HttpStatus, ResponseDTO> registerUser(UserDTO userDTO) {
        String validationResult = validateRegisterFields(userDTO);
        if (validationResult.isEmpty()) {
            if (userRepository.findByUsername(userDTO.getUsername()).isEmpty()) {
                User user = userMapper.toUser(userDTO);
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                user.setRole(Role.USER);
                userRepository.save(user);
                UserDTO userDTOResponse = userMapper.map(user); // TODO: Mapper not working
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
