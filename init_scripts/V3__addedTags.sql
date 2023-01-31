CREATE TABLE IF NOT EXISTS product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    retired BIT(1) NOT NULL,
    price DECIMAL(12,2) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tag (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS tag_product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tag_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (tag_id) REFERENCES tag(id),
    CONSTRAINT FOREIGN KEY (product_id) REFERENCES product(id)
);