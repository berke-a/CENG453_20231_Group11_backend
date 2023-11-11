package com.example.ceng453_20231_group11_backend.ControllerTest;

import com.example.ceng453_20231_group11_backend.dto.LoginDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.entity.User;
import com.example.ceng453_20231_group11_backend.enums.Role;
import com.example.ceng453_20231_group11_backend.mapper.UserMapper;
import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import com.example.ceng453_20231_group11_backend.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void registerPlayerTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPass");
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        Assert.assertEquals("User is successfully created with username:" + userDTO.getUsername(),
                userService.registerUser(userDTO).getSecond().getMessage());
    }

    @Test
    public void loginUserTest() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPass");

        Authentication mockAuthentication = mock(Authentication.class);
        given(authenticationManager.authenticate(ArgumentMatchers.any()))
                .willReturn(mockAuthentication);
        given(mockAuthentication.isAuthenticated()).willReturn(true);

        when(userRepository.findByUsername(loginDTO.getUsername()))
                .thenReturn(Optional.of(new User(
                        1L,
                        loginDTO.getUsername(),
                        loginDTO.getPassword(),
                        Role.USER
                )));

        ResponseDTO response = userService.handleLogin(loginDTO).getSecond();
        Assert.assertEquals("User login successfully!", response.getMessage());
    }

}
