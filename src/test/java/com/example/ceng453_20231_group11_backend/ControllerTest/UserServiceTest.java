package com.example.ceng453_20231_group11_backend.ControllerTest;

import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.mapper.UserMapper;
import com.example.ceng453_20231_group11_backend.repository.UserRepository;
import com.example.ceng453_20231_group11_backend.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

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

    @Test
    public void registerPlayerTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPass");
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        Assert.assertEquals("User is successfully created with username:" + userDTO.getUsername(),
                userService.registerUser(userDTO).getSecond().getMessage());
    }
}
