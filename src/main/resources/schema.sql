-- Das wird nicht ausgeführt, da eine persistente DB verwendet wird (database.mv.db)
-- Trotzdem kann hier dokumentiert werden, welche Tabellen wie erstellt wurden.

-- Löschen der Tabellen, um sie anschließend neu anzulegen:
DROP TABLE orderpositions, orders, products, customers, employees, addresses;

-- Tabellen:
CREATE TABLE addresses (
    id INTEGER NOT NULL AUTO_INCREMENT,
    street VARCHAR(128) NOT NULL,
    housenr VARCHAR(128) NOT NULL,
    postcode VARCHAR(128) NOT NULL, -- VARCHAR wegen evtl. führender Nullen
    city VARCHAR(128) NOT NULL,
    country VARCHAR(128) NOT NULL DEFAULT 'Deutschland',
    PRIMARY KEY (id)
);

CREATE TABLE employees (
    id INTEGER NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    birthdate DATE NOT NULL,
    address_id INTEGER NOT NULL,
    email VARCHAR(128) NOT NULL,
    phonenumber VARCHAR(128), -- VARCHAR wegen führender Null
    department VARCHAR(128) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    pass_hash VARCHAR(128) NOT NULL,
    FOREIGN KEY (address_id) REFERENCES ADDRESSES(id),
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE customers (
    id INTEGER NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    birthdate DATE NOT NULL,
    address_id INTEGER NOT NULL,
    email VARCHAR(128) NOT NULL,
    phonenumber VARCHAR(128),
    pass_hash VARCHAR(128) NOT NULL,
    FOREIGN KEY (address_id) REFERENCES ADDRESSES(id),
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE products (
    id INTEGER NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(128) NOT NULL,
    product_description VARCHAR(512),
    image_url VARCHAR(512),
    amount_ml INTEGER NOT NULL,
    price_eur DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE orders (
    id INTEGER NOT NULL AUTO_INCREMENT,
    order_date DATE NOT NULL,
    order_time TIME NOT NULL,
    total_price DECIMAL (10,2) NOT NULL,
    order_status VARCHAR(128) NOT NULL,
    cust_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cust_id) REFERENCES customers(id)
);

CREATE TABLE orderpositions (
    pos_nr INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    order_id INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    PRIMARY KEY (pos_nr, order_id)
);