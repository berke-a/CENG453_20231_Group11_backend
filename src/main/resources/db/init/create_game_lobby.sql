CREATE TABLE game_lobby
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_state VARCHAR(255) NOT NULL,
    player_count INT NOT NULL
);
