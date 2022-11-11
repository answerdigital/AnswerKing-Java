DELETE FROM product_category;
DELETE FROM order_product;
DELETE FROM `order`;
DELETE FROM order_status;
DELETE FROM product;
DELETE FROM category;

ALTER TABLE product_category AUTO_INCREMENT = 1;
ALTER TABLE product AUTO_INCREMENT = 1;
ALTER TABLE category AUTO_INCREMENT = 1;
ALTER TABLE `order` AUTO_INCREMENT = 1;
ALTER TABLE order_status AUTO_INCREMENT = 1;