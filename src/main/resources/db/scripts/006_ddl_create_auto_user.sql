CREATE TABLE IF NOT EXISTS auto_user
(
    id          serial primary key,
    login       varchar             not null unique,
    password    varchar             not null,
    auto_post_id INT REFERENCES auto_post (id)
);