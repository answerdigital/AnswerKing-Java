INSERT INTO category (name, description, created_on, retired, last_updated)
VALUES('CatOne', 'CatOne description', '2022-11-22 02:22:22', 0, '2022-11-22 02:22:22');

INSERT INTO category (name, description, created_on, retired, last_updated)
VALUES('CatTwo', 'CatTwo description', '2022-11-23 03:33:33', 0, '2023-11-23 03:33:33');

INSERT INTO product (name, description, price, retired, category_id)
VALUES('Burger', '300g Beef', 6.69, 0, 1);
INSERT INTO product (name, description, price, retired, category_id)
VALUES('Cola', '500ml', 2.99, 0, 2);
INSERT INTO product (name, description, price, retired, category_id)
VALUES('Fries', 'Large Fries', 2.99, 0, 1);

INSERT INTO product_category (product_id, category_id) VALUES(1, 1);
INSERT INTO product_category (product_id, category_id) VALUES(3, 1);