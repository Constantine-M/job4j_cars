CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    engine_id INT NOT NULL UNIQUE REFERENCES engine(id)
);