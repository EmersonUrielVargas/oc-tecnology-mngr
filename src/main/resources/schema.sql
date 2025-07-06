CREATE TABLE IF NOT EXISTS technologies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS technology_capacity (
    id BIGSERIAL PRIMARY KEY,
    id_technology BIGINT NOT NULL,
    id_capacity BIGINT NOT NULL,

    CONSTRAINT uq_technology_capacity UNIQUE (id_technology, id_capacity),
    CONSTRAINT fk_technology
        FOREIGN KEY (id_technology)
        REFERENCES technologies(id)
        ON DELETE CASCADE
);


