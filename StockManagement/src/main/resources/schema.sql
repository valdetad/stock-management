-- Drop existing tables if they exist
DROP TABLE IF EXISTS STOCK;
DROP TABLE IF EXISTS PRODUCT;
DROP TABLE IF EXISTS MARKET;

CREATE TABLE MARKET (
                        ID INT PRIMARY KEY,
                        OTHER_COLUMNS VARCHAR(255)
);


CREATE TABLE PRODUCT (
                         ID INT PRIMARY KEY AUTO_INCREMENT,
                         NAME VARCHAR(255) NOT NULL,
                         CATEGORY VARCHAR(255),
                         PRICE DECIMAL(10, 2),
                         DESCRIPTION TEXT,
                         BARCODE VARCHAR(255) UNIQUE
);


CREATE TABLE STOCK (
                       ID INT PRIMARY KEY AUTO_INCREMENT,
                       QUANTITY INT NOT NULL,
                       MARKET_ID INT,
                       PRODUCT_ID INT,
                       FOREIGN KEY (MARKET_ID) REFERENCES MARKET(ID),
                       FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT(ID)
);

ALTER TABLE STOCK DROP CONSTRAINT IF EXISTS FKqkljcdurtmoxxm5k8ktfwytmm;

ALTER TABLE STOCK
    ADD CONSTRAINT FKqkljcdurtmoxxm5k8ktfwytmm
        FOREIGN KEY (MARKET_ID) REFERENCES MARKET(ID);
