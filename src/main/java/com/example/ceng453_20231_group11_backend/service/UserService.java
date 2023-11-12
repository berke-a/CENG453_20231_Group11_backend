package com.example.ceng453_20231_group11_backend.service;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.dto.LoginDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.entity.PasswordResetToken;
import com.example.ceng453_20231_group11_backend.entity.User;
import com.example.ceng453_20231_group11_backend.enums.Role;
import com.example.ceng453_20231_group11_backend.mapper.UserMapper;
import com.example.ceng453_20231_group11_backend.repository.PasswordResetTokenRepository;
import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    /**
     * Handles the login process for a user.
     *
     * @param loginDTO Data transfer object containing login credentials.
     * @param request HttpServletRequest to manage session.
     * @return A Pair containing the HttpStatus and a ResponseDTO with login results.
     */
    public Pair<HttpStatus, ResponseDTO> handleLogin(LoginDTO loginDTO, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            User user = userRepository.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDTO userDTO = userMapper.map(user);

            return Pair.of(HttpStatus.OK, new ResponseDTO(userDTO, "User login successfully!",
                    APIConstants.RESPONSE_SUCCESS));
        } catch (AuthenticationException e) {
            log.warn("Login failed for username: {}", loginDTO.getUsername());
            return Pair.of(HttpStatus.UNAUTHORIZED, new ResponseDTO(null,
                    "Login failed: Invalid username or password", APIConstants.RESPONSE_FAIL));
        }
    }

    /**
     * Registers a new user in the system.
     *
     * @param userDTO Data transfer object containing user registration data.
     * @return A Pair containing the HttpStatus and a ResponseDTO with registration results.
     */
    public Pair<HttpStatus, ResponseDTO> handleRegister(UserDTO userDTO) {
        String validationResult = validateRegisterFields(userDTO);
        if (validationResult.isEmpty()) {
            if (userRepository.findByUsername(userDTO.getUsername()).isEmpty()) {
                User user = userMapper.toUser(userDTO);
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                user.setRole(Role.USER);
                userRepository.save(user);
                UserDTO userDTOResponse = userMapper.map(user);
                return Pair.of(HttpStatus.OK, new ResponseDTO(userDTOResponse,
                        String.format("User is successfully created with username:%s", user.getUsername()),
                        APIConstants.RESPONSE_SUCCESS));
            } else {
                log.warn("Username exists, could not complete user registration for username:{}",
                        userDTO.getUsername());
                return Pair.of(HttpStatus.OK, new ResponseDTO(null,
                        String.format("Username '%s' already exists.\nPlease choose another and try again",
                                userDTO.getUsername()), APIConstants.RESPONSE_FAIL));
            }
        } else {
            return Pair.of(HttpStatus.OK, new ResponseDTO(null,
                    String.format("Validation Error on registration.\nFollowing constraints must be met: %s",
                            validationResult),
                    APIConstants.RESPONSE_FAIL));
        }
    }

    /**
     * Validates the fields of a UserDTO for registration.
     *
     * @param userDTO Data transfer object containing user registration data.
     * @return A string containing validation errors; empty if no errors are found.
     */
    private String validateRegisterFields(UserDTO userDTO) {
        if (userDTO == null) {
            return "Missing user register credentials";
        }

        String result = "";
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            result += "Username cannot be empty.\n";
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            result += "Password cannot be empty.\n";
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            result += "Password cannot be empty.\n";
        }
        return result;
    }

    /**
     * Creates and sends a password reset token to the user's email.
     *
     * @param email The email address of the user who requests a password reset.
     * @return Pair of HttpStatus and ResponseDTO with the result of the password reset request.
     */
    public Pair<HttpStatus, ResponseDTO> createPasswordResetToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return Pair.of(HttpStatus.NOT_FOUND, new ResponseDTO(null, "Email address not found",
                    APIConstants.RESPONSE_FAIL));
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpirationDate(new Timestamp(System.currentTimeMillis() + 3600000)); // 1-hour validity
        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetToken(user.getEmail(), token);
        return Pair.of(HttpStatus.OK, new ResponseDTO(token, "Password reset token sent",
                APIConstants.RESPONSE_SUCCESS));
    }

    /**
     * Validates the password reset token.
     *
     * @param token The password reset token to be validated.
     * @return Pair of HttpStatus and ResponseDTO indicating if the token is valid.
     */
    public Pair<HttpStatus, ResponseDTO> validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpirationDate().before(new Timestamp(System.currentTimeMillis()))) {
            return Pair.of(HttpStatus.BAD_REQUEST, new ResponseDTO(null, "Invalid or expired token",
                    APIConstants.RESPONSE_FAIL));
        }
        return Pair.of(HttpStatus.OK, new ResponseDTO(resetToken.getUser(), "Token valid",
                APIConstants.RESPONSE_SUCCESS));
    }

    /**
     * Changes the password of a user based on a valid password reset token.
     *
     * @param token The password reset token.
     * @param newPassword The new password to set for the user.
     * @return Pair of HttpStatus and ResponseDTO indicating the result of the password change process.
     */
    public Pair<HttpStatus, ResponseDTO> changePassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpirationDate().before(new Timestamp(System.currentTimeMillis()))) {
            return Pair.of(HttpStatus.BAD_REQUEST, new ResponseDTO(null, "Invalid or expired token",
                    APIConstants.RESPONSE_FAIL));
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return Pair.of(HttpStatus.OK, new ResponseDTO(null, "Password changed successfully",
                APIConstants.RESPONSE_SUCCESS));
    }

    /**
     * Handles the process of logging out a user.
     *
     * @param request The HttpServletRequest, used to access the user's session.
     * @return A Pair containing the HttpStatus and a ResponseDTO indicating the outcome of the logout process.
     */
    public Pair<HttpStatus, ResponseDTO> handleLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            return Pair.of(HttpStatus.OK, new ResponseDTO(null, "Logged out successfully",
                    APIConstants.RESPONSE_SUCCESS));
        }
        return Pair.of(HttpStatus.OK, new ResponseDTO(null,
                "No active session found or already logged out", APIConstants.RESPONSE_SUCCESS));
    }
}
