-- Wird bei persistenter DB nicht ausgeführt
-- Dokumentation Beispieldaten

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
INSERT INTO products VALUES (1, 'Pils', 'Standard-Pils, kein Schnickschnack', 333, 0.89);
INSERT INTO products VALUES (2, 'Bananenweizen', 'fruchtig herb', 500, 1.29);

-- Beispieldaten für Bestellungen
INSERT INTO orders VALUES (1, '2021-07-01', '15:33:42', 9.12, 'ordered', 1);

-- Beispieldaten für Bestellpositionen
INSERT INTO orderpositions VALUES (1, 5, 2, 1);
INSERT INTO orderpositions VALUES (2, 3, 1, 1);