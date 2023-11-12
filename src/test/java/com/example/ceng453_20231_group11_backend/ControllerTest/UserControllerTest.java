package com.example.ceng453_20231_group11_backend.ControllerTest;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.controller.UserController;
import com.example.ceng453_20231_group11_backend.dto.LoginDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void testRegisterUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPass");
        Pair<HttpStatus, ResponseDTO> response = Pair.of(HttpStatus.OK, new ResponseDTO(null,
                String.format("Player is successfully created with username: %s",
                        userDTO.getUsername()), APIConstants.RESPONSE_FAIL));
        when(userService.handleRegister(userDTO)).thenReturn(response);
        Assert.assertEquals(response.getFirst(), userController.register(userDTO).getStatusCode());
    }

    @Test
    public void testLoginUser() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("testPass");

        Pair<HttpStatus, ResponseDTO> response = Pair.of(HttpStatus.OK, new ResponseDTO(null, "User login successfully!",
                APIConstants.RESPONSE_FAIL));
        when(userService.handleLogin(loginDTO)).thenReturn(response);
        Assert.assertEquals(response.getFirst(), userController.login(loginDTO).getStatusCode());
    }
}
