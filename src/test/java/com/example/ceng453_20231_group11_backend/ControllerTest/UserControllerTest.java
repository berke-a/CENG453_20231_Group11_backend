package com.example.ceng453_20231_group11_backend.ControllerTest;

import com.example.ceng453_20231_group11_backend.constants.APIConstants;
import com.example.ceng453_20231_group11_backend.controller.UserController;
import com.example.ceng453_20231_group11_backend.dto.LoginDTO;
import com.example.ceng453_20231_group11_backend.dto.ResponseDTO;
import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
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

        Pair<HttpStatus, ResponseDTO> response = Pair.of(HttpStatus.OK,
                new ResponseDTO(null, "User login successfully!", APIConstants.RESPONSE_SUCCESS));
        when(userService.handleLogin(ArgumentMatchers.eq(loginDTO), ArgumentMatchers.any(HttpServletRequest.class)))
                .thenReturn(response);
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        Assert.assertEquals(response.getFirst(), userController.login(loginDTO, mockRequest).getStatusCode());
    }

    @Test
    public void testRequestPasswordReset() {
        String email = "test@example.com";
        Pair<HttpStatus, ResponseDTO> response = Pair.of(HttpStatus.OK,
                new ResponseDTO("Token", "Password reset token sent", APIConstants.RESPONSE_SUCCESS));

        when(userService.createPasswordResetToken(email)).thenReturn(response);

        ResponseEntity<ResponseDTO> result = userController.requestPasswordReset(email);
        Assert.assertEquals(response.getFirst(), result.getStatusCode());
        Assert.assertEquals(response.getSecond(), result.getBody());
    }

    @Test
    public void testChangePassword() {
        String token = "validToken";
        String newPassword = "newPassword";
        Pair<HttpStatus, ResponseDTO> response = Pair.of(HttpStatus.OK,
                new ResponseDTO(null, "Password changed successfully", APIConstants.RESPONSE_SUCCESS));

        when(userService.changePassword(token, newPassword)).thenReturn(response);

        ResponseEntity<ResponseDTO> result = userController.changePassword(token, newPassword);
        Assert.assertEquals(response.getFirst(), result.getStatusCode());
        Assert.assertEquals(response.getSecond(), result.getBody());
    }

    @Test
    public void testLogoutUser() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        Pair<HttpStatus, ResponseDTO> serviceResponse = Pair.of(HttpStatus.OK,
                new ResponseDTO(null, "Logged out successfully", APIConstants.RESPONSE_SUCCESS));

        when(userService.handleLogout(mockRequest)).thenReturn(serviceResponse);

        ResponseEntity<ResponseDTO> responseEntity = userController.logout(mockRequest);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals("Logged out successfully", responseEntity.getBody().getMessage());
    }
}
