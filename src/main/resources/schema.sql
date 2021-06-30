-- Das wird nicht ausgeführt, da eine persistente DB verwendet wird (database.mv.db)
-- Trotzdem kann hier dokumentiert werden, welche Tabellen wie erstellt wurden.
CREATE TABLE person (
   id INTEGER NOT NULL AUTO_INCREMENT,
   firstname VARCHAR(128) NOT NULL,
   lastname VARCHAR(128) NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE addresses (
   
)

CREATE TABLE customers (
   id INTEGER NOT NULL AUTO_INCREMENT,
   firstname VARCHAR(128) NOT NULL,
   lastname VARCHAR(128) NOT NULL,
   address_id INTEGER NOT NULL,
   email VARCHAR(128) NOT NULL,
   passhash VARCHAR(128) NOT NULL,
   FOREIGN KEY (address_id) REFERENCES ADDRESSES(id),
   PRIMARY KEY (id),
   UNIQUE (email)
);