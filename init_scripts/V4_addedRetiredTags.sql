CREATE TABLE IF NOT EXISTS tag (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    retired BIT(1) NOT NULL,

    PRIMARY KEY(id)
);