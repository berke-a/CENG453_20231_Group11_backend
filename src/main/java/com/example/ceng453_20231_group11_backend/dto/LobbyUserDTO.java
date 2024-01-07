package com.example.ceng453_20231_group11_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LobbyUserDTO {
    private Long lobbyId;
    private Long userId;
}
