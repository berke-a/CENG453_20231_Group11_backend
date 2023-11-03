CREATE TABLE Score
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL,
    score_value INT          NOT NULL,
    score_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES User (username)
        ON DELETE CASCADE
);