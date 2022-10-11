CREATE TABLE IF NOT EXISTS item (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    available BIT(1) NOT NULL,
    price DECIMAL(12,2) NOT NULL,

    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS category (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    description VARCHAR(255) NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS item_category (
    id INT NOT NULL AUTO_INCREMENT,
    item_id INT NOT NULL,
    category_id INT NOT NULL,

    PRIMARY KEY (Id),
    FOREIGN KEY (item_id) REFERENCES item(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS order_status (
    id INT NOT NULL AUTO_INCREMENT,
    status VARCHAR(255),

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS `order` (
    id INT NOT NULL AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    order_status_id INT NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (order_status_id) REFERENCES order_status(id)
);

CREATE TABLE IF NOT EXISTS order_item (
    order_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,

    PRIMARY KEY (order_id, item_id),

    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (item_id) REFERENCES item(id)

);


