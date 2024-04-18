CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    model VARCHAR(64) NOT NULL,
    color VARCHAR(32) NOT NULL ,
    mileage INT,
    engine_id INT NOT NULL REFERENCES engine(id),
    body_id INT NOT NULL REFERENCES body(id),
    auto_passport_id INT NOT NULL UNIQUE REFERENCES auto_passport(id)
);