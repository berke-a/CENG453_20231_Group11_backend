package com.example.ceng453_20231_group11_backend.ServiceTest;

import com.example.ceng453_20231_group11_backend.dto.LoginDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.entity.PasswordResetToken;
import com.example.ceng453_20231_group11_backend.entity.User;
import com.example.ceng453_20231_group11_backend.enums.Role;
import com.example.ceng453_20231_group11_backend.mapper.UserMapper;
import com.example.ceng453_20231_group11_backend.repository.PasswordResetTokenRepository;
import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import com.example.ceng453_20231_group11_backend.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    PasswordResetTokenRepository passwordResetTokenRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    public void testRegisterUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPass");
        userDTO.setEmail("test@example.com");

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        Pair<HttpStatus, ResponseDTO> result = userService.handleRegister(userDTO);

        Assert.assertEquals(HttpStatus.OK, result.getFirst());
        Assert.assertEquals("User is successfully created with username:testUser", result.getSecond().getMessage());
    }


    @Test
    public void testLoginUser() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPass");
        String email = "test@example.com";

        Authentication mockAuthentication = mock(Authentication.class);
        given(authenticationManager.authenticate(ArgumentMatchers.any()))
                .willReturn(mockAuthentication);
        given(mockAuthentication.isAuthenticated()).willReturn(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());

        when(userRepository.findByUsername(loginDTO.getUsername()))
                .thenReturn(Optional.of(new User(
                        1L,
                        loginDTO.getUsername(),
                        loginDTO.getPassword(),
                        email,
                        Role.USER
                )));

        ResponseDTO response = userService.handleLogin(loginDTO, request).getSecond();
        Assert.assertEquals("User login successfully!", response.getMessage());
    }

    @Test
    public void testCreatePasswordResetToken() {
        String email = "test@example.com";
        User user = new User(1L, "testUser", "testPass", email, Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseDTO response = userService.createPasswordResetToken(email).getSecond();
        Assert.assertEquals("Password reset token sent", response.getMessage());
    }

    @Test
    public void testValidatePasswordResetToken() {
        String token = "testToken";
        User user = new User(1L, "testUser", "testPass", "test@example.com", Role.USER);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpirationDate(new Timestamp(System.currentTimeMillis() + 3600000));

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(resetToken);

        ResponseDTO response = userService.validatePasswordResetToken(token).getSecond();
        Assert.assertEquals("Token valid", response.getMessage());
    }

    @Test
    public void testChangePassword() {
        String token = "validToken";
        String newPassword = "newPassword";
        User user = new User(1L, "testUser", "oldPassword", "test@example.com", Role.USER);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpirationDate(new Timestamp(System.currentTimeMillis() + 3600000));

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(resetToken);

        ResponseDTO response = userService.changePassword(token, newPassword).getSecond();
        Assert.assertEquals("Password changed successfully", response.getMessage());
    }

    @Test
    public void testHandleLogout() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpSession mockSession = new MockHttpSession();
        mockRequest.setSession(mockSession);

        mockSession.setNew(false);
        mockSession.setAttribute("someAttribute", "someValue");

        Pair<HttpStatus, ResponseDTO> response = userService.handleLogout(mockRequest);

        Assert.assertEquals(HttpStatus.OK, response.getFirst());
        Assert.assertEquals("Logged out successfully", response.getSecond().getMessage());
    }
}
