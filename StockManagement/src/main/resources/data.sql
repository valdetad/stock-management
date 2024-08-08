-- Insert data into MARKET table
INSERT INTO MARKET (NAME) VALUES ('Town Market');
INSERT INTO MARKET (NAME) VALUES ('City Market');

-- Insert data into PRODUCT table
INSERT INTO PRODUCT (NAME, CATEGORY, PRICE, DESCRIPTION, BARCODE) VALUES
                                                                      ('Samsung Galaxy S21', 'Electronics', 799.99, 'Latest Samsung Galaxy phone', '1234567890'),
                                                                      ('iPhone 14 Pro', 'Electronics', 999.99, 'Latest Apple iPhone', '0987654321');

-- Insert statements for the STOCK table
INSERT INTO STOCK (QUANTITY, NAME, MARKET_ID, PRODUCT_ID, BARCODE)
VALUES
    (100, 'Samsung Galaxy S21', 1, 1, 1222223366),
    (200, 'iPhone 14 Pro', 1, 2, 1222223363),
    (150, 'Samsung Galaxy Note 5', 2, 1, 1222223364),
    (50, 'iPhone 13 Pro', 2, 2, 1222223365);
