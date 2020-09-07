CREATE TABLE user_profile
(
    id         UUID         NOT NULL PRIMARY KEY,
    username   VARCHAR(100) NOT NULL,
    image_link VARCHAR(200)
)