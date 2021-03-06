-- Wird bei persistenter DB nicht ausgeführt
-- Dokumentation Beispieldaten

-- Löschen aller Daten, um sie anschließend neu anzulegen:
DELETE FROM orderpositions;
DELETE FROM orders;
DELETE FROM products;
DELETE FROM customers;
DELETE FROM employees;
DELETE FROM addresses;
DELETE FROM newsletter_emails;

-- Beispieldaten für Adressen
INSERT INTO addresses VALUES (1, 'Ausgedachte Str.', '3', '12345', 'Entenhausen', 'Deutschland');
INSERT INTO addresses VALUES (2, 'Musterstr.', '42', '42424', 'Nuketown', 'Deutschland');
INSERT INTO addresses VALUES (3, 'Braker Mitte', '9', '32657', 'Lemgo', 'Deutschland');
INSERT INTO addresses VALUES (4, 'Breslauer Straße', '18c', '33813', 'Oerlinghausen', 'Deutschland');

-- Beispieldaten für Kunden
-- Passwort Bernd das Brot: Brot
INSERT INTO customers VALUES (1, 'Bernd', 'das Brot', '1985-04-01', 1, 'Bernd@Brot.de', '012345678910', 'ef53751671e5219919b139afb43865b325192727a2f1ca216b018d4509abb57f', TRUE, '');
INSERT INTO customers VALUES (2, 'Kasimir', 'Eckhardt', '1990-08-10', 3, 'kzi-eckhardt@web.de', '0176/34934106', '15c7772149be0dce3d0c6352acb5cdf0578365de1ab8aa3ea2efc97467c3ea59', TRUE, '');
Insert into customers values (3, 'Lukas',	'Kröker',	'1997-04-19',	1,	'lukas.kroeker@edu.fhdw.de',	'015785724772',	'09597712a6fe5e3ea54e9ca35f496e65c672d1eb6f85e68b0de8bfa46b72a722', TRUE, '');
INSERT INTO customers VALUES (4, 'Sven', 'Hädrich', '1995-06-04', 4, 'sven.haedrich@edu.fhdw.de', '01707781756', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', TRUE, '');

-- Beispieldaten für Angestellte
INSERT INTO employees VALUES (1, 'Kasimir', 'Eckhardt', '1990-08-10', 3, 'kasimir.eckhardt@edu.fhdw.de', '017634934106',  'Webdevelopment', TRUE, '15c7772149be0dce3d0c6352acb5cdf0578365de1ab8aa3ea2efc97467c3ea59');
Insert into employees values (3, 'Lukas',	'Kröker',	'1997-04-19',	1,	'lukas.kroeker@edu.fhdw.de',	'015785724772', 'Webdevelopment', TRUE, '09597712a6fe5e3ea54e9ca35f496e65c672d1eb6f85e68b0de8bfa46b72a722');

-- Beispieldaten für Produkte
INSERT INTO products VALUES (1, 'Pils', 'Sparrenpils', 'Die Burg, die nicht fällt - Genau das verkörpert unser Sparrenpils. Ein Vergleich der Burg mit unserem Gerstenmalz und dem Hopfen ist leicht gezogen, unsere Zutaten haben eine Reife hinter sich, welche dem Alter der Burg ebenbürtig ist. Genau diese führt zu dem einzigartig herben Geschmack unseres Bieres; mit dem wohlschmeckend hopfigen und malzigen Abgang spürst du deine Geschmacksnerven ohne Widerstand kapitulieren und es wird der volle Genuss entfaltet. Nicht umsonst erreichst du mit unserem Sparrenpils eine Pegelhöhe und das Gefühl der Uneinnehmbarkeit, aber sei gewarnt, der Graben drum herum ist tief und auch der beste Reiter landet mal im Graben.', 5.2, 'Wasser, Pilsner Malz, Weizenmalz, Hopfen, Hefe', './images/bier-pils.png', 333, 0.89, TRUE);
INSERT INTO products VALUES (2, 'Radler/Alster', 'Studentenbrause', 'Die Mimose unter den Getränken? Nicht mit uns, das versichern wir dir! Unsere Studentenbrause überzeugt mit den penibelsten Studien zu dem perfekten Bier – Limo – Gemisch. Entgegen den politischen Gepflogenheiten, ist bei uns kein Kopieren angesagt, unsere Brause ist in langwierigen Forschungen entstanden und wir behaupten daher nicht umsonst, dass dieses Radler selbst den härtesten Ritter aus der Festung holen und ihn von dem Geschmack überzeigen wird. Du glaubst lieber einem Lyriker? Bitte. Wer läuft so spät durch die Stadt und singt? Es ist der Student, vor Rausch fast blind. Er hält es sicher, er hält es warm, die Studentenbrause in seinem Arm – Rudolf-August Oetker (frei zitiert)', 2.9, 'Wasser, Pilsner Malz, Weizenmalz, Hopfen, Hefe, Zitronen-Limetten Limo-Gemisch (Hausrezept)', './images/bier-radler.png', 333, 0.95, TRUE);
INSERT INTO products VALUES (3, 'Weizen', 'Almjubler', 'Ein Schuss, ein Schrei, ein Tor, Bielefeld vor! Nostalgisch schweifen wir ab, Dammeier, Kauf, Vata, Porcello, Hain, Owomoyela, Buckley und am Ende der legendäre Endstand von 3 zu 1. Wo das passiert ist? Auf der unbezwingbaren Alm, hier bei uns in Ostwestfalen. Noch heute hört man in den Tiefen der Katakomben die Alm nach diesem Spiel beben. Wir haben den Bayern die Lederhosen ausgezogen, ebenso haben wir hier das Weizen, was es wirklich zu feiern verdient. Almjubler sind die Zeiten, wo man pure Freude verspürt, wo die Gier nach mehr gewinnt, die Gänsehaut in schmerzhafte Höhe steigt. Wenn dieser Tropfen flüssiger Brillanz deine Kehle hinabläuft, dann wirst du jubeln. Lass den 13.02.2005 auch in dir leben, spüre die geballte Macht der Ostwestfalen, des großartigsten Stadions und der jubelnden Fans.', 5.3, 'Wasser, Weizenmalz, Gerstenmalz, Hefe, Hopfen', './images/bier-weizen.png', 500, 1.29, TRUE);
INSERT INTO products VALUES (4, 'Bananenweizen', 'Ravensberger Spinnerei', 'Die Spinnen doch die Paderborner“ – Obelix (Stadtbekannter Bananenweizenliebhaber). Wir befinden uns im Jahre 2021 n.Chr. Ganz Ostwestfalen ist von Paderborner besetzt… Ganz Ostwestfalen? Nein! Eine von unbeugsamen Arminen bevölkerte Stadt hört nicht auf, dem Abwasser von Bier Widerstand zu leisten. Und das Leben ist nicht leicht für die paderborner Bierbrauer, denn nun besitzen die Arminen einen Zaubertrank – die Ravensberger Spinnerei, DAS Bananenweizen. Die Fusion von sämigen, leicht säuerlichem Almjubler mit der süße von ostasiatischer Banane führt zu übermenschlichen Kräften. Trotze den Paderbornern und zeige deine Treue der glorreichen Bielefelder-Braukunst.', 4.5, 'Wasser, Weizenmalz, Gerstenmalz, Hefe, Hopfen, Bananensaft', './images/bier-bananenweizen.png', 500, 1.29, TRUE);


-- Beispieldaten für Bestellungen
INSERT INTO orders VALUES (1, '2021-07-01', '15:33:42', 9.12, 'ordered', 1);
INSERT INTO orders VALUES (2, '2021-08-27', '20:38:03', 47.16, 'ordered', 2);
INSERT INTO orders VALUES (3, '2021-09-01', '13:24:07', 48.60, 'ordered', 3);
INSERT INTO orders VALUES (4, '2021-09-01', '13:53:23', 4.42, 'ordered', 4);
INSERT INTO orders VALUES (5, '2021-09-05',	'13:54:25', 61.68, 'ordered', 4);
INSERT INTO orders VALUES (6, '2021-09-05', '14:11:57', 8.35, 'ordered', 4);

-- Beispieldaten für Bestellpositionen
INSERT INTO orderpositions VALUES (1, 5, 3, 1);
INSERT INTO orderpositions VALUES (2, 3, 1, 1);
INSERT INTO orderpositions VALUES (1, 24, 1, 2);
INSERT INTO orderpositions VALUES (2, 20, 4, 2);
INSERT INTO orderpositions VALUES (1, 24, 2, 3);
INSERT INTO orderpositions VALUES (2, 20, 3, 3);
INSERT INTO orderpositions VALUES (1, 1, 4, 4);
INSERT INTO orderpositions VALUES (2, 1, 3, 4);
INSERT INTO orderpositions VALUES (3, 1, 2, 4);
INSERT INTO orderpositions VALUES (4, 1, 1, 4);
INSERT INTO orderpositions VALUES (1, 5, 1, 5);
INSERT INTO orderpositions VALUES (2, 10, 2, 5);
INSERT INTO orderpositions VALUES (3, 15, 3, 5);
INSERT INTO orderpositions VALUES (4, 22, 4, 5);
INSERT INTO orderpositions VALUES (1, 5, 3, 6);
INSERT INTO orderpositions VALUES (2, 2, 2, 6);