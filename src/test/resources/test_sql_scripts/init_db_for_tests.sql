
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
    `description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
    `created_on` TIMESTAMP DEFAULT NOT NULL,
    `retired` bit(1) NOT NULL,
    `last_updated` TIMESTAMP DEFAULT NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

INSERT INTO category (name, description, created_on, retired, last_updated)
VALUES('CatOne', 'CatOne description', '', 0, '');

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `address` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
    `order_status` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1, 'Leeds', 'IN_PROGRESS');
INSERT INTO `order` VALUES (2, 'Manchester', 'IN_PROGRESS');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
    `description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
    `retired` bit(1) NOT NULL,
    `price` decimal(12, 2) NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,

    KEY `category_id` (`category_id`),
    CONSTRAINT `products_fk_1` FOREIGN KEY (`category_id`) REFERENCES category (`id`)

) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------

INSERT INTO product (name, description, price, retired, category_id)
VALUES('Burger', '300g Beef', 6.69, 0, 1);
INSERT INTO product (name, description, price, retired, category_id)
VALUES('Cola', '500ml', 2.99, 0, 1);
INSERT INTO product (name, description, price, retired, category_id)
VALUES('Fries', 'Large Fries', 2.99, 0, 1);

-- ----------------------------
-- Table structure for order_product
-- ----------------------------
DROP TABLE IF EXISTS `order_product`;
CREATE TABLE `order_product`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `order_id` bigint(20) NOT NULL,
    `product_id` bigint(20) NOT NULL,
    `quantity` int(11) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `order_id`(`order_id`) USING BTREE,
    INDEX `product_id`(`product_id`) USING BTREE,
    CONSTRAINT `order_product_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `order_product_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_product
-- ----------------------------
INSERT INTO `order_product` VALUES (1, 1, 1, 5);
INSERT INTO `order_product` VALUES (2, 1, 3, 47);
INSERT INTO `order_product` VALUES (3, 2, 1, 1);
INSERT INTO `order_product` VALUES (4, 2, 3, 1);

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`  (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `product_id` bigint(20) NOT NULL,
    `category_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `product_id`(`product_id`) USING BTREE,
    INDEX `category_id`(`category_id`) USING BTREE,
    CONSTRAINT `product_category_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `product_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_category
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;