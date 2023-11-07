CREATE TABLE Score
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id   INT  NOT NULL,
    score_value INT  NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User (id)
        ON DELETE CASCADE
);
