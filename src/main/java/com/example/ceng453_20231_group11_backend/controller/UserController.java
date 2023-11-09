package com.example.ceng453_20231_group11_backend.controller;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/users", headers = "Accept=application/json")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDTO> login(Authentication authRequest) {
        if (authRequest != null) {
            Pair<HttpStatus, ResponseDTO> response = userService.handleLogin(authRequest);
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } else {
            log.warn("BAD REQUEST on login - missing Authorization Header in the request.");
            return ResponseEntity.badRequest().body(new ResponseDTO(null,
                    "Missing Authorization Header in the request.", APIConstants.RESPONSE_FAIL));
        }
    }

}
