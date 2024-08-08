-- Drop existing tables if they exist
DROP TABLE IF EXISTS STOCK;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS MARKET;

-- Create MARKET table
CREATE TABLE MARKET (
                        ID INT PRIMARY KEY,
                        OTHER_COLUMNS VARCHAR(255)
);

-- Create PRODUCT table
CREATE TABLE PRODUCT (
                         ID INT PRIMARY KEY AUTO_INCREMENT,
                         NAME VARCHAR(255) NOT NULL,
                         CATEGORY VARCHAR(255),
                         PRICE DECIMAL(10, 2),
                         DESCRIPTION TEXT,
                         BARCODE VARCHAR(255) UNIQUE
);

-- Create STOCK table with NAME column
CREATE TABLE STOCK (
                       ID INT PRIMARY KEY AUTO_INCREMENT,
                       QUANTITY INT NOT NULL,
                       NAME VARCHAR(255),
                       MARKET_ID INT,
                       PRODUCT_ID INT,
                       FOREIGN KEY (MARKET_ID) REFERENCES MARKET(ID),
                       FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT(ID)
);

-- Drop existing constraints before adding new ones
ALTER TABLE STOCK DROP CONSTRAINT IF EXISTS FKqkljcdurtmoxxm5k8ktfwytmm;

-- Add new foreign key constraints
ALTER TABLE STOCK
    ADD CONSTRAINT FKqkljcdurtmoxxm5k8ktfwytmm
        FOREIGN KEY (MARKET_ID) REFERENCES MARKET(ID);
