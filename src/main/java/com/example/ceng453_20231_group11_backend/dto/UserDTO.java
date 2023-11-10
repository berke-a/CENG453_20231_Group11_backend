package com.example.ceng453_20231_group11_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;

    @Override
    public String toString() {
        return String.format("UserDTO with id %d and username %s and password %s", id, username, password);
    }
}
