-- Das wird nicht ausgeführt, da eine persistente DB verwendet wird (database.mv.db)
-- Trotzdem kann hier dokumentiert werden, welche Tabellen wie erstellt wurden.
CREATE TABLE person (
    id INTEGER NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE addresses (
    id INTEGER NOT NULL AUTO_INCREMENT,
    street VARCHAR(128) NOT NULL,
    housenr VARCHAR(128) NOT NULL,
    postcode INTEGER NOT NULL,
    city VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE customers (
    id INTEGER NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    address_id INTEGER NOT NULL,
    email VARCHAR(128) NOT NULL,
    pass_hash VARCHAR(128) NOT NULL,
    FOREIGN KEY (address_id) REFERENCES ADDRESSES(id),
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE orders (
    id INTEGER NOT NULL AUTO_INCREMENT,
    order_date DATE NOT NULL,
    order_time TIME NOT NULL,
    cust_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cust_id) REFERENCES customers(id)
);

CREATE TABLE products (
    id INTEGER NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(128) NOT NULL,
    product_description VARCHAR(512),
    amount_ml INTEGER NOT NULL,
    price_eur DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE orderpositions (
    pos_nr INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    cust_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    FOREIGN KEY (cust_id) REFERENCES customers(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    PRIMARY KEY (pos_nr, cust_id, product_id)
);