CREATE TABLE owners
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(64),
    history_id INT NOT NULL REFERENCES history(id)
);