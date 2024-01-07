package com.example.ceng453_20231_group11_backend.entity;

import com.example.ceng453_20231_group11_backend.enums.GameState;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "GameLobby")
@Table(name = "game_lobby")
@EntityListeners(AuditingEntityListener.class)
public class GameLobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state", nullable = false)
    private GameState gameState;

    @Column(name = "player_count", nullable = false)
    private Integer playerCount;
}
