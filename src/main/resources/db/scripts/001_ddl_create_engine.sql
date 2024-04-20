CREATE TABLE IF NOT EXISTS engine
(
    id SERIAL PRIMARY KEY,
    capacity FLOAT,
    horsepower INT,
    type VARCHAR(32)
);