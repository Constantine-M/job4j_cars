CREATE TABLE IF NOT EXISTS engine
(
    id SERIAL PRIMARY KEY,
    capacity FLOAT,
    horse_power INT,
    fuel_type VARCHAR(32)
);