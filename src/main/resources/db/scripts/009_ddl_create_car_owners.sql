CREATE TABLE car_owners
(
    id SERIAL PRIMARY KEY,
    car_id INT NOT NULL REFERENCES cars(id),
    owner_id INT NOT NULL REFERENCES owners(id),
    UNIQUE (car_id, owner_id)
);