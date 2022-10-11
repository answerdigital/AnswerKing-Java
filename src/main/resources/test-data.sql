DELETE FROM item_category;
DELETE FROM order_item;
DELETE FROM `order`;
DELETE FROM order_status;
DELETE FROM item;
DELETE FROM category;

ALTER TABLE item_category AUTO_INCREMENT = 1;
ALTER TABLE item AUTO_INCREMENT = 1;
ALTER TABLE category AUTO_INCREMENT = 1;
ALTER TABLE `order` AUTO_INCREMENT = 1;
ALTER TABLE order_status AUTO_INCREMENT = 1;

INSERT INTO category (id, name, description) values
    (1, 'Burgers', 'Bread and filling'),
    (2, 'Chicken', 'Contains chicken'),
    (3, 'Cheese', 'contains cheese')
    ON DUPLICATE KEY UPDATE id = id,
                            name = name,
                            description = description;

INSERT INTO item (id, name, description, available, price) values
    (1, 'Answer Whopper', 'The biggest burger', true, '5.99'),
    (2, 'Chicken Burger', 'Standard chicken burger', true, '3.99'),
    (3, 'Cheese Burger', 'standard cheese burger', true, '2.99')
    ON DUPLICATE KEY UPDATE id = id,
                            name = name,
                            description = description,
                            available = available,
                            price = price;

INSERT INTO item_category (id, item_id, category_id) values
    (1, 1, 1),
    (2, 2, 1),
    (3, 2, 2),
    (4, 3, 1),
    (5, 3, 3)
    ON DUPLICATE KEY UPDATE id = id,
                            item_id = item_id,
                            category_id =category_id;
