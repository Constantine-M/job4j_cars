CREATE TABLE IF NOT EXISTS auto_passport
(
    id SERIAL PRIMARY KEY ,
    bought_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    having_passport VARCHAR(32),
    cleared boolean
)