Das hier funktioniert bei mir mit jdk 16 (Windows 10) und OpenJDK 11 (Linux)
(Java Version muss ggf. in pom.xml angepasst werden)

Zugang H2-Datenbank:
URL: localhost/h2-console
JDBC URL: jdbc:h2:file:./database
User + Kennwort leer

Start Application: .\mvnw spring-boot:run
mvn spring-boot:run (für macOS)

Änderung Serverport auf 80 (in application.properties) hat unter Linux Probleme verursacht.
Kommando muss dort jetzt mit root-Rechten gestartet werden:
sudo mvn spring-boot:run

Falls beim Startversuch eine Fehlermeldung erscheint, kann es helfen, vor dem Start den target-Ordner zu löschen.
Das geht mit folgender Befehlszeile unter Windows in der Powershell:
rm -R .\target; .\mvnw spring-boot:run

Zugriff über Webbrowser: localhost/
Abbruch: Strg+C im Terminal

Login zum Mitarbeiterbereich: localhost/s3cr3tl0g1n
Mitarbeiterbereich: localhost/employee_area