CREATE TABLE PasswordResetToken
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT,
    token           VARCHAR(512) UNIQUE NOT NULL,
    creation_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiration_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User (id)
        ON DELETE CASCADE
);
