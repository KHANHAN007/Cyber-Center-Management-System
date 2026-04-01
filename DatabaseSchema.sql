-- ============================================
-- CYBER CENTER MANAGEMENT SYSTEM DATABASE
-- Database: PRJ_CYBER_JAVA
-- ============================================

DROP DATABASE IF EXISTS PRJ_CYBER_JAVA;
CREATE DATABASE PRJ_CYBER_JAVA;
USE PRJ_CYBER_JAVA;

-- ============================================
-- 1. ROLE TABLE
-- ============================================
CREATE TABLE role
(
    role_id   INT AUTO_INCREMENT PRIMARY KEY,
    role_code VARCHAR(10) UNIQUE,
    role_name VARCHAR(50),
    is_deleted BOOLEAN DEFAULT FALSE
);

INSERT INTO role (role_code, role_name)
VALUES ('AD001', 'ADMIN'),
       ('ST001', 'STAFF'),
       ('CU001', 'CUSTOMER');

-- ============================================
-- 2. USER & USER PROFILE
-- ============================================
CREATE TABLE user
(
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_code  VARCHAR(10) UNIQUE,
    username   VARCHAR(50) UNIQUE,
    password   VARCHAR(255),
    email      VARCHAR(100),
    role_id    INT,
    status     ENUM ('ACTIVE','INACTIVE','BLOCKED') DEFAULT 'ACTIVE',
    is_deleted BOOLEAN                              DEFAULT FALSE,
    created_at DATETIME                             DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role (role_id)
);

-- Insert test users with proper bcrypt hashed passwords
-- Password: admin123 -> hash: $2a$10$slYQmyNdGzin7olVN3qu2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm
INSERT INTO user (user_code, username, password, email, role_id, status)
VALUES ('USER001', 'admin', '$2a$10$mU708.5ZIbZJnVBhQDq2Yen5yMd1AhAAv72Q69CBke4Q3yK5mzM5y', 'admin@cyber.com', 1,
        'ACTIVE'),
       ('USER002', 'staff1', '$2a$10$slYQmyNdGzin7olVN3qu2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'staff@cyber.com', 2,
        'ACTIVE'),
       ('USER003', 'customer1', '$2a$10$slYQmyNdGzin7olVN3qu2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'customer@cyber.com', 3,
        'ACTIVE');
CREATE TABLE user_profile
(
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT UNIQUE,
    full_name  VARCHAR(100),
    phone      VARCHAR(20),
    address    VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
);

INSERT INTO user_profile (user_id, full_name, phone, address)
VALUES (1, 'Admin User', '0901234567', '123 Admin Street'),
       (2, 'Staff User', '0902345678', '456 Staff Avenue'),
       (3, 'Customer User', '0903456789', '789 Customer Road');

-- ============================================
-- 3. ZONE & PC CONFIG & PC
-- ============================================
CREATE TABLE zone
(
    zone_id          INT AUTO_INCREMENT PRIMARY KEY,
    zone_code        VARCHAR(10) UNIQUE,
    zone_name        VARCHAR(50),
    price_multiplier DECIMAL(3, 2) DEFAULT 1.0
);

INSERT INTO zone (zone_code, zone_name, price_multiplier)
VALUES ('Z001', 'VIP Zone', 1.5),
       ('Z002', 'Standard Zone', 1.0),
       ('Z003', 'Economy Zone', 0.8);

CREATE TABLE pc_config
(
    config_id      INT AUTO_INCREMENT PRIMARY KEY,
    config_code    VARCHAR(10) UNIQUE,
    cpu            VARCHAR(50),
    ram            INT,
    gpu            VARCHAR(50),
    price_per_hour DECIMAL(10, 2)
);

INSERT INTO pc_config (config_code, cpu, ram, gpu, price_per_hour)
VALUES ('CFG001', 'Intel i9', 32, 'RTX 4090', 150000.00),
       ('CFG002', 'Intel i7', 16, 'RTX 3080', 100000.00),
       ('CFG003', 'Intel i5', 8, 'RTX 3060', 50000.00);

CREATE TABLE pc
(
    pc_id      INT AUTO_INCREMENT PRIMARY KEY,
    pc_code    VARCHAR(10) UNIQUE,
    pc_name    VARCHAR(50),
    zone_id    INT,
    config_id  INT,
    status     ENUM ('AVAILABLE','BOOKED','IN_USE','MAINTENANCE') DEFAULT 'AVAILABLE',
    is_deleted BOOLEAN                                            DEFAULT FALSE,
    FOREIGN KEY (zone_id) REFERENCES zone (zone_id),
    FOREIGN KEY (config_id) REFERENCES pc_config (config_id)
);

INSERT INTO pc (pc_code, pc_name, zone_id, config_id, status)
VALUES ('PC001', 'Gaming PC 1', 1, 1, 'AVAILABLE'),
       ('PC002', 'Gaming PC 2', 1, 1, 'AVAILABLE'),
       ('PC003', 'Pro PC 1', 2, 2, 'AVAILABLE'),
       ('PC004', 'Standard PC 1', 3, 3, 'AVAILABLE');

-- ============================================
-- 4. BOOKING
-- ============================================
CREATE TABLE booking_slot
(
    slot_id   INT AUTO_INCREMENT PRIMARY KEY,
    slot_code VARCHAR(10) UNIQUE,
    slot_time TIME,
    slot_date DATE
);

CREATE TABLE booking
(
    booking_id      INT AUTO_INCREMENT PRIMARY KEY,
    booking_code    VARCHAR(10) UNIQUE,
    user_id         INT,
    pc_id           INT,
    slot_id         INT,
    start_time      DATETIME,
    end_time        DATETIME,
    total_price     DECIMAL(12, 2),
    voucher_id      INT,
    discount_amount DECIMAL(10, 2),
    status          ENUM ('PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED'),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (pc_id) REFERENCES pc (pc_id),
    FOREIGN KEY (slot_id) REFERENCES booking_slot (slot_id)
);

CREATE TABLE check_in_out
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    booking_id     INT UNIQUE,
    check_in_time  DATETIME,
    check_out_time DATETIME,
    FOREIGN KEY (booking_id) REFERENCES booking (booking_id)
);

-- ============================================
-- 5. FOOD MANAGEMENT - WITH MIN/MAX PRICES & SIZES/TOPPINGS AS STRINGS
-- ============================================
CREATE TABLE food_category
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(10) UNIQUE,
    name          VARCHAR(50),
    status        VARCHAR(20) DEFAULT 'ACTIVE'
);

INSERT INTO food_category (category_code, name, status)
VALUES ('CAT001', 'Pizza', 'ACTIVE'),
       ('CAT002', 'Burger', 'ACTIVE'),
       ('CAT003', 'Drink', 'ACTIVE'),
       ('CAT004', 'Dessert', 'ACTIVE');

-- ✅ MAIN FOOD TABLE - WITH ALL REQUIRED COLUMNS
CREATE TABLE food_item
(
    item_id            INT AUTO_INCREMENT PRIMARY KEY,
    item_code          VARCHAR(10) UNIQUE,
    name               VARCHAR(100),
    category_id        INT,
    description        VARCHAR(255),
    min_price          DECIMAL(10, 0)                       DEFAULT 0,
    max_price          DECIMAL(10, 0)                       DEFAULT 0,
    available_sizes    VARCHAR(255),
    available_toppings VARCHAR(255),
    status             ENUM ('ACTIVE','SOLD_OUT','DELETED') DEFAULT 'ACTIVE',
    is_deleted         BOOLEAN                              DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES food_category (category_id)
);

-- ✅ SAMPLE FOOD DATA
INSERT INTO food_item (item_code, name, category_id, description, min_price, max_price, available_sizes,
                       available_toppings, status)
VALUES ('FD001', 'Margherita Pizza', 1, 'Classic pizza with tomato, mozzarella, and fresh basil', 150000, 250000,
        'Small, Medium, Large', 'Extra Cheese, Pepperoni, Mushroom', 'ACTIVE'),
       ('FD002', 'Spicy Burger', 2, 'Juicy burger with spicy sauce and crispy fries', 80000, 120000, 'Regular, Large',
        'Cheese, Bacon, Jalapeño', 'ACTIVE'),
       ('FD003', 'Cola', 3, 'Cold soft drink', 20000, 35000, 'Small, Medium, Large', '', 'ACTIVE'),
       ('FD004', 'Chocolate Cake', 4, 'Delicious chocolate cake', 60000, 80000, 'Slice, Whole', '', 'ACTIVE');

-- ============================================
-- 6. SIZE & TOPPING (Optional - for future use)
-- ============================================
CREATE TABLE size
(
    size_id INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(20)
);

INSERT INTO size (name)
VALUES ('Small'),
       ('Medium'),
       ('Large');

CREATE TABLE food_item_size
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT,
    size_id INT,
    price   DECIMAL(10, 2),
    FOREIGN KEY (item_id) REFERENCES food_item (item_id),
    FOREIGN KEY (size_id) REFERENCES size (size_id)
);

CREATE TABLE topping
(
    topping_id INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50),
    price      DECIMAL(10, 2)
);

INSERT INTO topping (name, price)
VALUES ('Extra Cheese', 20000),
       ('Pepperoni', 25000),
       ('Mushroom', 15000),
       ('Bacon', 20000),
       ('Jalapeño', 10000);

CREATE TABLE food_item_topping
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    item_id    INT,
    topping_id INT,
    FOREIGN KEY (item_id) REFERENCES food_item (item_id),
    FOREIGN KEY (topping_id) REFERENCES topping (topping_id)
);

-- ============================================
-- 7. COMBO
-- ============================================
CREATE TABLE combo
(
    combo_id    INT AUTO_INCREMENT PRIMARY KEY,
    combo_code  VARCHAR(10) UNIQUE,
    name        VARCHAR(100),
    price       DECIMAL(10, 2),
    combo_items VARCHAR(500),
    status      ENUM ('ACTIVE','SOLD_OUT','DELETED') DEFAULT 'ACTIVE',
    is_deleted  BOOLEAN                              DEFAULT FALSE
);

INSERT INTO combo (combo_code, name, price, combo_items, status)
VALUES ('CMB001', 'Family Pizza Combo', 500000.00, 'Margherita Pizza x2, Cola x2', 'ACTIVE'),
       ('CMB002', 'Burger Combo', 250000.00, 'Spicy Burger x2, Cola x1', 'ACTIVE');

CREATE TABLE combo_item
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    combo_id INT,
    item_id  INT,
    quantity INT,
    FOREIGN KEY (combo_id) REFERENCES combo (combo_id),
    FOREIGN KEY (item_id) REFERENCES food_item (item_id)
);

INSERT INTO combo_item (combo_id, item_id, quantity)
VALUES (1, 1, 2),
       (1, 3, 2),
       (2, 2, 2),
       (2, 3, 1);

CREATE TABLE inventory
(
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id      INT,
    quantity     INT,
    FOREIGN KEY (item_id) REFERENCES food_item (item_id)
);

-- ============================================
-- 8. ORDERS
-- ============================================
CREATE TABLE fb_order
(
    order_id    INT AUTO_INCREMENT PRIMARY KEY,
    order_code  VARCHAR(15) UNIQUE,
    booking_id  INT,
    total_price DECIMAL(12, 2),
    status      VARCHAR(20),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking (booking_id)
);

CREATE TABLE order_detail
(
    detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id  INT,
    item_id   INT,
    combo_id  INT,
    size_id   INT,
    quantity  INT,
    price     DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES fb_order (order_id),
    FOREIGN KEY (item_id) REFERENCES food_item (item_id),
    FOREIGN KEY (combo_id) REFERENCES combo (combo_id),
    FOREIGN KEY (size_id) REFERENCES size (size_id)
);

-- ============================================
-- 9. WALLET & PAYMENT
-- ============================================
CREATE TABLE wallet
(
    wallet_id   INT AUTO_INCREMENT PRIMARY KEY,
    wallet_code VARCHAR(10) UNIQUE,
    user_id     INT UNIQUE,
    balance     DECIMAL(12, 2) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

INSERT INTO wallet (wallet_code, user_id, balance)
VALUES ('W001', 1, 5000000),
       ('W002', 2, 10000000),
       ('W003', 3, 500000);

CREATE TABLE wallet_transaction
(
    trans_id   INT AUTO_INCREMENT PRIMARY KEY,
    wallet_id  INT,
    amount     DECIMAL(12, 2),
    type       VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wallet_id) REFERENCES wallet (wallet_id)
);

CREATE TABLE payment
(
    payment_id   INT AUTO_INCREMENT PRIMARY KEY,
    payment_code VARCHAR(10) UNIQUE,
    booking_id   INT,
    wallet_id    INT,
    amount       DECIMAL(12, 2),
    method       VARCHAR(20),
    status       ENUM ('PENDING','PAID','FAILED'),
    FOREIGN KEY (booking_id) REFERENCES booking (booking_id),
    FOREIGN KEY (wallet_id) REFERENCES wallet (wallet_id)
);

-- ============================================
-- 10. LOYALTY
-- ============================================
CREATE TABLE loyalty_points
(
    loyalty_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT UNIQUE,
    points     INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

INSERT INTO loyalty_points (user_id, points)
VALUES (1, 1000),
       (2, 2000),
       (3, 500);

CREATE TABLE point_transaction
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT,
    points     INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

-- ============================================
-- 11. OTP & EMAIL
-- ============================================
CREATE TABLE otp
(
    otp_id      INT AUTO_INCREMENT PRIMARY KEY,
    booking_id  INT,
    code        VARCHAR(10),
    expiry_time DATETIME,
    status      VARCHAR(20),
    FOREIGN KEY (booking_id) REFERENCES booking (booking_id)
);

CREATE TABLE email_log
(
    email_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id  INT,
    email    VARCHAR(100),
    content  TEXT,
    sent_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

-- ============================================
-- 12. PROMOTION
-- ============================================
CREATE TABLE promotion
(
    promotion_id     INT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(100),
    discount_percent INT,
    valid_from       DATETIME,
    valid_to         DATETIME
);

CREATE TABLE voucher_code
(
    voucher_id       INT AUTO_INCREMENT PRIMARY KEY,
    code             VARCHAR(50) UNIQUE,
    description      VARCHAR(255),
    promotion_id     INT,
    discount_type    VARCHAR(20),
    discount_value   DECIMAL(10, 2),
    min_order_value  DECIMAL(10, 2) DEFAULT 0,
    max_uses         INT DEFAULT 0,
    used_count       INT DEFAULT 0,
    status           VARCHAR(20) DEFAULT 'ACTIVE',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at       DATETIME,
    FOREIGN KEY (promotion_id) REFERENCES promotion (promotion_id)
);

INSERT INTO voucher_code (code, description, discount_type, discount_value, status)
VALUES ('WELCOME10', 'Welcome discount 10%', 'PERCENT', 10, 'ACTIVE'),
       ('SUMMER50', 'Summer promotion 50k discount', 'FIXED', 50000, 'ACTIVE');

-- ============================================
-- STORED PROCEDURES
-- ============================================
DELIMITER //

CREATE PROCEDURE GetUserDetailsById(IN p_user_id INT)
BEGIN
    SELECT
        u.user_id, u.user_code, u.username, u.password, u.email, u.role_id, u.status,
        u.is_deleted, u.created_at, u.updated_at,
        r.role_name,
        up.full_name, up.phone, up.address
    FROM user u
             LEFT JOIN role r ON u.role_id = r.role_id
             LEFT JOIN user_profile up ON u.user_id = up.user_id
    WHERE u.user_id = p_user_id AND u.is_deleted = FALSE;
END //

CREATE PROCEDURE GetUserWithProfile(IN p_user_id INT)
BEGIN
    SELECT
        u.user_id, u.user_code, u.username, u.password, u.email, u.role_id, u.status,
        u.is_deleted, u.created_at, u.updated_at,
        up.profile_id, up.full_name, up.phone, up.address,
        IFNULL(lp.points, 0) AS points
    FROM user u
             LEFT JOIN user_profile up ON u.user_id = up.user_id
             LEFT JOIN loyalty_points lp ON u.user_id = lp.user_id
    WHERE u.user_id = p_user_id AND u.is_deleted = FALSE;
END //

CREATE PROCEDURE GetPCDetailWithConfig(IN p_pc_id INT)
BEGIN
    SELECT
        p.pc_id, p.pc_code, p.pc_name, p.zone_id, p.status, p.config_id,
        c.config_code, c.cpu, c.ram, c.gpu, c.price_per_hour
    FROM pc p
             LEFT JOIN pc_config c ON p.config_id = c.config_id
    WHERE p.pc_id = p_pc_id AND p.is_deleted = FALSE;
END //

-- ✅ FIXED: Read directly from food_item columns
CREATE PROCEDURE GetFoodItemsWithCategoryAndToppings(IN p_show_deleted BOOLEAN)
BEGIN
    SELECT
        fi.item_id, fi.item_code, fi.name AS item_name,
        fc.category_id, fc.name AS category_name,
        fi.description, fi.status, fi.is_deleted,
        fi.min_price, fi.max_price,
        fi.available_sizes, fi.available_toppings
    FROM food_item fi
             LEFT JOIN food_category fc ON fi.category_id = fc.category_id
    WHERE (p_show_deleted = TRUE) OR (p_show_deleted = FALSE AND fi.is_deleted = FALSE)
    ORDER BY fc.category_id, fi.item_id;
END //

-- ✅ FIXED: Read directly from combo table
CREATE PROCEDURE GetCombosWithItems(IN p_show_deleted BOOLEAN)
BEGIN
    SELECT
        c.combo_id, c.combo_code, c.name AS combo_name, c.price AS combo_price,
        c.status, c.is_deleted, c.combo_items,
        ci.item_id, ci.quantity,
        fi.item_code, fi.name AS item_name, fi.min_price, fi.max_price,
        fc.category_id, fc.name AS category_name
    FROM combo c
    LEFT JOIN combo_item ci ON c.combo_id = ci.combo_id
    LEFT JOIN food_item fi ON ci.item_id = fi.item_id
    LEFT JOIN food_category fc ON fi.category_id = fc.category_id
    WHERE (p_show_deleted = TRUE) OR (p_show_deleted = FALSE AND c.is_deleted = FALSE)
    ORDER BY c.combo_id, ci.id;
END //

-- ✅ GET ALL USERS WITH ROLE
CREATE PROCEDURE GetAllUsersWithRole()
BEGIN
    SELECT
        u.user_id, u.user_code, u.username, u.password, u.email, u.role_id, u.status,
        u.is_deleted, u.created_at, u.updated_at,
        r.role_name
    FROM user u
             LEFT JOIN role r ON u.role_id = r.role_id
    WHERE u.is_deleted = FALSE
    ORDER BY u.user_id;
END //

-- ✅ GET PENDING BOOKINGS
CREATE PROCEDURE GetPendingBookings()
BEGIN
    SELECT
        b.booking_id, b.booking_code, b.user_id, b.pc_id, b.slot_id,
        b.start_time, b.end_time, b.total_price, b.voucher_id, b.discount_amount,
        b.status, b.created_at, b.updated_at,
        u.username, p.pc_name, z.zone_name
    FROM booking b
             LEFT JOIN user u ON b.user_id = u.user_id
             LEFT JOIN pc p ON b.pc_id = p.pc_id
             LEFT JOIN zone z ON p.zone_id = z.zone_id
    WHERE b.status = 'PENDING'
    ORDER BY b.created_at;
END //

-- ✅ GET BOOKING DETAIL WITH JOIN
CREATE PROCEDURE GetBookingDetailWithJoin(IN p_booking_id INT)
BEGIN
    SELECT
        b.booking_id, b.booking_code, b.user_id, b.pc_id, b.slot_id,
        b.start_time, b.end_time, b.total_price, b.voucher_id, b.discount_amount,
        b.status, b.created_at, b.updated_at,
        u.username, u.email,
        p.pc_name, p.pc_code,
        c.config_code, c.cpu, c.ram, c.gpu, c.price_per_hour,
        z.zone_name,
        up.full_name, up.phone,
        co.check_in_time, co.check_out_time
    FROM booking b
             LEFT JOIN user u ON b.user_id = u.user_id
             LEFT JOIN user_profile up ON u.user_id = up.user_id
             LEFT JOIN pc p ON b.pc_id = p.pc_id
             LEFT JOIN pc_config c ON p.config_id = c.config_id
             LEFT JOIN zone z ON p.zone_id = z.zone_id
             LEFT JOIN check_in_out co ON b.booking_id = co.booking_id
    WHERE b.booking_id = p_booking_id;
END //
DELIMITER ;
