Das hier funktioniert bei mir mit jdk 16 (Windows 10) und OpenJDK 11 (Linux)
(Java Version muss ggf. in pom.xml angepasst werden)
Für VS Code ist es sinnvoll, die Lombok-Extension zu installieren, damit VS Code
weiß, dass aufgrund der Annotations (z.B. @Getter / @Setter) die entsprechenden Methoden vorhanden sind.

Zugang H2-Datenbank:
URL: localhost/h2-console
JDBC URL: jdbc:h2:file:./database
User + Kennwort leer

Start Application: .\mvnw spring-boot:run
                   mvn spring-boot:run (für macOS)

Änderung Serverport auf 80 (in application.properties) hat unter Linux Probleme verursacht.
Kommando muss dort und vielleicht unter macOS jetzt mit root-Rechten gestartet werden:
    sudo mvn spring-boot:run

Zugriff über Webbrowser: localhost/login?name=Name | localhost/ | localhost/register
Abbruch: Strg+C im Terminal

spring initializr: https://start.spring.io/

Weiterführende Links:

Views mit Thymeleaf erstellen: https://www.dev-insider.de/views-mit-thymeleaf-erstellen-a-976811/

https://www.thymeleaf.org/documentation.html

@JENS CSS Präprozessor:
https://sass-lang.com/

siehe auch HELP.md

Mail ich habe mir die Mailadresse bierbestellen@gmail.com sichern 🥳
Mehr dazu findet ihr in dem verschlüsselten Verzeichnis, mit dem Schlüssel bierbestellen