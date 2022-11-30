CREATE TABLE IF NOT EXISTS product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    retired BIT(1) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    category_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    KEY `category_id` (`category_id`),
    CONSTRAINT `products_fk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
);

CREATE TABLE IF NOT EXISTS category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    description VARCHAR(255) NOT NULL,
    createdOn VARCHAR(255) NOT NULL,
    lastUpdated VARCHAR(255) NOT NULL,
    retired BIT(1) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product_category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    order_status VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS order_product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);
