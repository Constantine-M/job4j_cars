CREATE TABLE IF NOT EXISTS cars
(
    id SERIAL PRIMARY KEY,
    model VARCHAR(64) NOT NULL,
    mileage INT,
    engine_id INT NOT NULL REFERENCES engine(id),
    car_brand_id INT NOT NULL REFERENCES car_brand(id),
    color_id INT NOT NULL REFERENCES color(id),
    body_id INT NOT NULL REFERENCES body(id),
    auto_passport_id INT NOT NULL UNIQUE REFERENCES auto_passport(id)
);