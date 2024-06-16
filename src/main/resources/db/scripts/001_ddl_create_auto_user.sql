CREATE TABLE IF NOT EXISTS auto_user
(
    id          serial primary key,
    username        varchar             not null,
    phone_number        varchar     not null,
    login       varchar             not null unique,
    password    varchar             not null,
    user_zone VARCHAR(128)
);