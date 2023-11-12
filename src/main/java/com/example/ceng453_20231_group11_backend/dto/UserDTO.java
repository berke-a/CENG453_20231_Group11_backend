package com.example.ceng453_20231_group11_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String email;

    @Override
    public String toString() {
        return String.format("UserDTO with username %s and password %s", username, password);
    }
}
