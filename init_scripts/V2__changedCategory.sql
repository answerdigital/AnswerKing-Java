CREATE TABLE IF NOT EXISTS category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    description TIMESTAMP NOT NULL,
    createdOn TIMESTAMP NOT NULL,
    lastUpdated VARCHAR(255) NOT NULL,
    retired BIT(1) NOT NULL,

    PRIMARY KEY (id)
);