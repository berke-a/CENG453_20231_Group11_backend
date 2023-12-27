package com.example.ceng453_20231_group11_backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewScoreDTO {
    private String username;
    private Integer score;
}
