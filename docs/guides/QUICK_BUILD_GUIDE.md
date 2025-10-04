# 🚀 Quick Build Guide

## Problem: Maven/Java nicht installiert

### Lösung 1: Manuelle Installation (Empfohlen)

1. **Java 21 installieren:**
   - Gehe zu: https://adoptium.net/
   - Lade OpenJDK 21 herunter
   - Installiere es (JAVA_HOME wird automatisch gesetzt)

2. **Maven installieren:**
   - Gehe zu: https://maven.apache.org/download.cgi
   - Lade Maven 3.9.6 herunter
   - Entpacke es nach `C:\Program Files\Apache\maven`
   - Füge `C:\Program Files\Apache\maven\bin` zum PATH hinzu

3. **Build ausführen:**
   ```bash
   mvn clean package
   ```

### Lösung 2: Mit vorhandenem Maven

Da Maven bereits heruntergeladen ist, brauchst du nur Java:

1. **Java 21 installieren** (siehe oben)
2. **Build ausführen:**
   ```bash
   .\apache-maven-3.9.6\bin\mvn.cmd clean package
   ```

### Lösung 3: IntelliJ IDEA (Einfachste)

1. **IntelliJ IDEA öffnen**
2. **Projekt öffnen** (E:\Basics Plugin)
3. **Maven-Toolfenster** öffnen (rechts)
4. **Lifecycle → package** doppelklicken

### Lösung 4: Eclipse

1. **Eclipse öffnen**
2. **Projekt importieren**
3. **Rechtsklick → Run As → Maven build**
4. **Goals:** `clean package`

## Nach dem Build

Die JAR-Datei findest du in:
```
target/BasicsPlugin-1.0-SNAPSHOT.jar
```

Kopiere sie in den `plugins/` Ordner deines Minecraft-Servers.

## Build-Optionen

- **Standard:** `mvn clean package`
- **Mit Vault:** `mvn clean package -f pom-with-vault.xml`

## Hilfe

Falls Probleme auftreten:
1. Prüfe, ob Java 21 installiert ist: `java -version`
2. Prüfe, ob Maven installiert ist: `mvn -version`
3. Verwende IntelliJ IDEA für den einfachsten Build
