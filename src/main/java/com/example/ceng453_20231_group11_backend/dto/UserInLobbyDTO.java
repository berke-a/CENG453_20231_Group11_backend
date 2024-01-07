package com.example.ceng453_20231_group11_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInLobbyDTO {
    private Long userId;
    private String username;
}
