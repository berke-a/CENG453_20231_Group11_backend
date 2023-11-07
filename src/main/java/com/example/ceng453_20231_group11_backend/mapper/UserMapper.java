package com.example.ceng453_20231_group11_backend.mapper;

import com.example.ceng453_20231_group11_backend.dto.UserDTO;
import com.example.ceng453_20231_group11_backend.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    List<UserDTO> toUserDTOList(List<User> userList);

    User toUser(UserDTO userDTO);

    UserDTO map(User user);
}
