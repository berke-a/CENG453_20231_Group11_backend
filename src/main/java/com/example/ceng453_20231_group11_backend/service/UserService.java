package com.example.ceng453_20231_group11_backend.service;

import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.entity.User;
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
            return Pair.of(HttpStatus.OK, new ResponseDTO(userDTO, null, "success"));
        }
        log.warn("User not found with username:{}", principal.getUsername());
        return Pair.of(HttpStatus.NOT_FOUND, new ResponseDTO(null,
                String.format("User not found with username:%s", principal.getUsername()), "fail"));
    }

}
