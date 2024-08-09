-- Drop existing tables if they exist
DROP TABLE IF EXISTS STOCK;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS MARKET;

-- Create MARKET table
CREATE TABLE MARKET (
                        ID INT PRIMARY KEY AUTO_INCREMENT,
                        NAME VARCHAR(255) NOT NULL
);

-- PRODUCT table
CREATE TABLE PRODUCT (
                         ID INT PRIMARY KEY AUTO_INCREMENT,
                         NAME VARCHAR(255) NOT NULL,
                         CATEGORY VARCHAR(255),
                         PRICE DECIMAL(10, 2),
                         DESCRIPTION TEXT,
                         BARCODE VARCHAR(255) UNIQUE
);

-- STOCK table
CREATE TABLE STOCK (
                       ID INT PRIMARY KEY AUTO_INCREMENT,
                       QUANTITY INT NOT NULL,
                       NAME VARCHAR(255),
                       MARKET_ID INT,
                       PRODUCT_ID INT,
                       BARCODE VARCHAR(255),
                       FOREIGN KEY (MARKET_ID) REFERENCES MARKET(ID),
                       FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT(ID)
);

-- Purchase table