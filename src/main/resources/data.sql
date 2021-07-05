-- Wird bei persistenter DB nicht ausgeführt
-- Dokumentation Beispieldaten

-- Löschen aller Daten, um sie anschließend neu anzulegen:
DELETE FROM orderpositions;
DELETE FROM orders;
DELETE FROM products;
DELETE FROM customers;
DELETE FROM employees;
DELETE FROM addresses;

-- Beispieldaten für Adressen
INSERT INTO addresses VALUES (1, 'Ausgedachte Str.', 3, '12345', 'Entenhausen', 'Deutschland');
INSERT INTO addresses VALUES (2, 'Musterstr.', 42, '42424', 'Nuketown', 'Deutschland');

-- Beispieldaten für Kunden
INSERT INTO customers VALUES (1, 'Bernd', 'das Brot', '1985-04-01', 1, 'Bernd@Brot.de', '012345678910', '5c6c557828690e93182ab73ce6eade78acfcb8f6');
-- Passwort: Brot

-- Beispieldaten für Angestellte
INSERT INTO employees VALUES (1, 'Kasimir', 'Eckhardt', '1990-08-10', 2, 'kasimir.eckhardt@edu.fhdw.de', '017634934106',  'Webdevelopment', TRUE, 'b595c71e9f4e4dfcfc18e80190685ed563791f74');
-- Passwort: beershop

-- Beispieldaten für Produkte
INSERT INTO products VALUES (1, 'Pils - der Genüssliche', 'Qualitativ hochwertiges Standard-Bier zum fairen Preis. Hier weiß man, was man bekommt!', 'https://www.bierselect.de/biermagazin/wp-content/uploads/2016/06/Interessantes-ueber-das-Pils-1300x722.jpg', 333, 0.89);
INSERT INTO products VALUES (2, 'Radler / Alster - der Heimliche', 'Du möchtest etwas Alkohol konsumieren, ohne als Säufer zu gelten? Dann bist du hier genau richtig!', 'https://lh3.googleusercontent.com/proxy/CCNy_Kus4Bk2ao230o660i0dUssNBnRKuwQkpF3jX-Ct7oICe9JXrYdNvJSikzrMxpPtBj9RwHoDfE0BllnPvHI4Wxg28w6FaGOvdKDhYXEJwkZ_', 333, 0.95);
INSERT INTO products VALUES (3, 'Weizen - die Königin', 'Schmeckt und knallt richtig rein. Serviervorschlag: Einen Schuss Bananensaft hinzugeben für ein köstliches Bananenweizen. Prost!', 'https://www.marions-kochbuch.de/dru-pic/1842.jpg', 500, 1.29);

-- Beispieldaten für Bestellungen
INSERT INTO orders VALUES (1, '2021-07-01', '15:33:42', 9.12, 'ordered', 1);

-- Beispieldaten für Bestellpositionen
INSERT INTO orderpositions VALUES (1, 5, 3, 1);
INSERT INTO orderpositions VALUES (2, 3, 1, 1);