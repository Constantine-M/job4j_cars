CREATE TABLE IF NOT EXISTS owners
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(64),
    start_ownership TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    end_ownership TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    auto_passport_id INT REFERENCES auto_passport(id)
);