Das hier funktioniert bei mir mit jdk 16 (Windows 10) und OpenJDK 11 (Linux)
(Java Version muss ggf. in pom.xml angepasst werden)
Für VS Code ist es sinnvoll, die Lombok-Extension zu installieren, damit VS Code
weiß, dass aufgrund der Annotations (z.B. @Data) die entsprechenden Methoden vorhanden sind.

Zugang H2-Datenbank: jdbc:h2:file:./database
User + Kennwort leer

Schreiben in Datenbank funktioniert jetzt, ist aber anfällig für SQL-Injections.
Beispiel:
    Eingabe Vorname: Fieser
    Eingabe Nachname: Hacker'); DELETE FROM PERSON WHERE ID = 2; INSERT INTO PERSON VALUES (5, 'Noch ein', 'Hacker
Ergebnis: 2 Personen hinzugefügt und eine gelöscht. Ein Hacker könnte so natürlich auch die ganze Datenbank löschen.

Bisher implementiert:

Spring Framework, Anleitung Quickstart: https://spring.io/quickstart
Start Application: .\mvnw spring-boot:run
                   mvn spring-boot:run (für macOS)
Zugriff über Webbrowser: localhost/login?name=Name / localhost/ / localhost/register
Abbruch: Strg+C im Terminal

spring initializr: https://start.spring.io/

Views mit Thymeleaf erstellen: https://www.dev-insider.de/views-mit-thymeleaf-erstellen-a-976811/

Weiterführende Links:

https://www.thymeleaf.org/documentation.html

https://www.dev-insider.de/datenbankabfragen-mit-dem-jdbc-template-a-1022878/
https://spring.io/guides/gs/relational-data-access/

@JENS CSS Präprozessor:
https://sass-lang.com/

siehe auch HELP.md