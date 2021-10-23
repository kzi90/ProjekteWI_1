Das hier funktioniert bei mir mit jdk 16 (Windows 10) und OpenJDK 11 (Linux)
Java Version muss ggf. in pom.xml angepasst werden

pom.xml in .gitignore aufgenommen, damit die Java-Version nicht ständig
angepasst werden muss. Es werden vermutlich keine neuen dependencies
mehr hinzugefügt werden müssen.

Zugang H2-Datenbank:
URL: localhost/h2-console
JDBC URL: jdbc:h2:file:./database
User + Kennwort leer

Start Application:
Windows: .\mvnw spring-boot:run
macOS: mvn spring-boot:run
Linux: sudo mvn spring-boot:run

Änderung Serverport auf 80 (in application.properties) hat unter Linux bewirkt,
dass das Kommando dort jetzt mit root-Rechten gestartet werden muss (sudo)

Falls beim Startversuch eine Fehlermeldung erscheint, kann es helfen, vor dem Start den target-Ordner zu löschen.
Das geht mit folgender Befehlszeile unter Windows in der Powershell:
rm -R .\target; .\mvnw spring-boot:run

Zugriff über Webbrowser: localhost/
Abbruch: Strg+C im Terminal, in dem das Programm gestartet wurde

Login zum Mitarbeiterbereich: localhost/s3cr3tl0g1n
Mitarbeiterbereich: localhost/products_edit