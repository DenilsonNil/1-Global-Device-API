CREATE TABLE IF NOT EXISTS devices (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    brand         VARCHAR(100) NOT NULL,
    state         VARCHAR(80) NOT NULL,
    created_at    Date NOT NULL DEFAULT NOW(),
    CHECK (state IN ('AVAILABLE', 'IN_USE', 'INACTIVE'))
);