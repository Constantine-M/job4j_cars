create table auto_post
(
    id           serial PRIMARY KEY,
    description  VARCHAR NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    auto_user_id INT REFERENCES auto_user (id)  NOT NULL
);