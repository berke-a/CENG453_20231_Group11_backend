CREATE TABLE User
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(512)        NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    role     VARCHAR(255)        NOT NULL,
);