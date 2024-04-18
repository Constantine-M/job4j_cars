create table auto_post
(
    id           serial PRIMARY KEY,
    title        VARCHAR(96),
    description  TEXT,
    created      TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    sold         BOOLEAN,
    price        BIGINT,
    car_id       INT REFERENCES cars(id)
);