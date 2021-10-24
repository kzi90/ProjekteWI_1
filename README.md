Das hier funktioniert bei mir mit jdk 16 (Windows 10) und OpenJDK 11 (Linux)
Java Version muss ggf. in pom.xml angepasst werden

Falls eine andere Java-Version als 16 verwendet wird, im Terminal zuerst den
Befehl "git update-index --skip-worktree pom.xml" ausführen.
Danach kann die Version z.B. auf 11 geändert werden, ohne dass die Änderung
beim nächsten Commit im Repository übernommen wird.

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