Das hier funktioniert bei mir mit jdk 16 (Windows 10) und OpenJDK 11 (Linux)
(Java Version muss ggf. in pom.xml angepasst werden)
Für VS Code ist es sinnvoll, die Lombok-Extension zu installieren, damit VS Code
weiß, dass aufgrund der Annotations (z.B. @Data) die entsprechenden Methoden vorhanden sind.

Zugang H2-Datenbank:
URL: localhost/h2-console
JDBC URL: jdbc:h2:file:./database
User + Kennwort leer

Bisher implementiert:

Spring Framework, Anleitung Quickstart: https://spring.io/quickstart
Start Application: .\mvnw spring-boot:run
                   mvn spring-boot:run (für macOS)
Zugriff über Webbrowser: localhost/login?name=Name | localhost/ | localhost/register
Abbruch: Strg+C im Terminal

Änderung Serverport auf 80 (in application.properties) hat unter Linux Probleme verursacht.
Kommando muss dort und vielleicht unter macOS jetzt mit root-Rechten gestartet werden:
    sudo mvn spring-boot:run

spring initializr: https://start.spring.io/

Views mit Thymeleaf erstellen: https://www.dev-insider.de/views-mit-thymeleaf-erstellen-a-976811/

Weiterführende Links:

https://www.thymeleaf.org/documentation.html
https://www.dev-insider.de/datenbankabfragen-mit-dem-jdbc-template-a-1022878/
https://spring.io/guides/gs/relational-data-access/

@JENS CSS Präprozessor:
https://sass-lang.com/

siehe auch HELP.md