CREATE TABLE Score (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       player_id INT NOT NULL,
                       score_value INT NOT NULL,
                       created_at DATE NOT NULL,
                       FOREIGN KEY (player_id) REFERENCES User(id)
                           ON DELETE CASCADE
);
