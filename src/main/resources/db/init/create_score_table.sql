CREATE TABLE score
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id   BIGINT  NOT NULL,
    score_value INT  NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
        ON DELETE CASCADE
);
