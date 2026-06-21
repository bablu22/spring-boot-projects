-- =========================
-- USERS
-- =========================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- =========================
-- CATEGORIES
-- =========================
CREATE TABLE categories (
    id TINYINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- =========================
-- PROFILES (One-to-One with User)
-- =========================
CREATE TABLE profiles (
    id BIGINT PRIMARY KEY,
    bio LONGTEXT,
    phone_number VARCHAR(15),
    date_of_birth DATE,
    loyalty_points INT UNSIGNED DEFAULT 0,
    CONSTRAINT fk_profile_user FOREIGN KEY (id) REFERENCES users(id)
);

-- =========================
-- ADDRESSES (Many-to-One with User)
-- =========================
CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_address_user_id ON addresses(user_id);

-- =========================
-- PRODUCTS (Many-to-One with Category)
-- =========================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description LONGTEXT NOT NULL,
    category_id TINYINT,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_product_category_id ON products(category_id);

-- =========================
-- WISHLIST (Many-to-Many User <-> Product)
-- =========================
CREATE TABLE wishlist (
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, product_id),
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE INDEX idx_wishlist_product_id ON wishlist(product_id);
