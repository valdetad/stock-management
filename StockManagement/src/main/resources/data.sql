-- MARKET table (Remove duplicates)
INSERT INTO MARKET (NAME) VALUES ('Town Market');
INSERT INTO MARKET (NAME) VALUES ('City Market');

-- PRODUCT table (Remove duplicates)
INSERT INTO PRODUCT (NAME, CATEGORY, PRICE, DESCRIPTION, BARCODE) VALUES
                                                                      ('Samsung Galaxy S21', 'Electronics', 799.99, 'Latest Samsung Galaxy phone', '1234567890'),
                                                                      ('iPhone 14 Pro', 'Electronics', 999.99, 'Latest Apple iPhone', '0987654321');

-- STOCK table (Remove duplicates)
INSERT INTO STOCK (QUANTITY, NAME, MARKET_ID, PRODUCT_ID, BARCODE)
VALUES
    (100, 'Samsung Galaxy S21', 1, 1, 1222223366),
    (200, 'iPhone 14 Pro', 1, 2, 1222223363),
    (150, 'Samsung Galaxy Note 5', 2, 1, 1222223364),
    (50, 'iPhone 13 Pro', 2, 2, 1222223365);

-- PURCHASE table (Remove duplicates)
INSERT INTO PURCHASE (MARKET_ID, PRODUCT_ID, PURCHASE_DATE, QUANTITY, PRICE, TOTAL, STATUS)
VALUES
    (1, 1, '2024-08-01', 10, 799.99, 7999.90, 'Completed'),
    (1, 2, '2024-08-02', 5, 999.99, 4999.95, 'Completed'),
    (2, 1, '2024-08-03', 15, 799.99, 11999.85, 'Pending'),
    (2, 2, '2024-08-04', 8, 999.99, 7999.92, 'Completed');
