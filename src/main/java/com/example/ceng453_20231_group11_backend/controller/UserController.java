package com.example.ceng453_20231_group11_backend.controller;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.LoginDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Operation(summary = "User Login",
            description = "Authenticates a user with the provided login credentials.")
    public ResponseEntity<ResponseDTO> login(LoginDTO loginDTO, HttpServletRequest request) {
        if (loginDTO != null) {
            Pair<HttpStatus, ResponseDTO> response = userService.handleLogin(loginDTO, request);
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } else {
            log.warn("BAD REQUEST on login - missing loginDTO");
            return ResponseEntity.badRequest().body(new ResponseDTO(null,
                    "Missing login credentials", APIConstants.RESPONSE_FAIL));
        }
    }

    @PostMapping(value = "/register")
    @Operation(summary = "Register New Player",
            description = "Registers a new player with the provided user details.")
    public ResponseEntity<ResponseDTO> register(UserDTO newUserDTO) {
        Pair<HttpStatus, ResponseDTO> response = userService.handleRegister(newUserDTO);
        return ResponseEntity.status(response.getFirst()).body(response.getSecond());
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Request Password Reset",
            description = "Sends a password reset token to the user's email.")
    public ResponseEntity<ResponseDTO> requestPasswordReset(@RequestParam("email") String email) {
        try {
            Pair<HttpStatus, ResponseDTO> response = userService.createPasswordResetToken(email);
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } catch (Exception e) {
            log.error("Error during password reset request: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error processing password reset request",
                            APIConstants.RESPONSE_FAIL));
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change Password",
            description = "Changes the user's password using the reset token.")
    public ResponseEntity<ResponseDTO> changePassword(@RequestParam("token") String token,
                                                      @RequestParam("newPassword") String newPassword) {
        try {
            Pair<HttpStatus, ResponseDTO> response = userService.changePassword(token, newPassword);
            return ResponseEntity.status(response.getFirst()).body(response.getSecond());
        } catch (Exception e) {
            log.error("Error during password change: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(null, "Error changing password",
                            APIConstants.RESPONSE_FAIL));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "User Logout",
            description = "Logs out the current user and invalidates the session.")
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request) {
        Pair<HttpStatus, ResponseDTO> response = userService.handleLogout(request);
        return ResponseEntity.status(response.getFirst()).body(response.getSecond());
    }
}
