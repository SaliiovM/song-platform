CREATE TABLE IF NOT EXISTS resource (
    id bigserial PRIMARY KEY,
    "key" VARCHAR(255),
    state VARCHAR(255)
);