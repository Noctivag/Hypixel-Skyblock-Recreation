# Kodierungsprobleme lösen - Anleitung

## Problem
Die Kompilierungsfehler deuten auf Zeichenkodierungsprobleme hin:
- `unmappable character (0xFF) for encoding UTF-8`
- `unmappable character (0xFE)`
- `unmappable character (0xA7)` (Minecraft §-Zeichen)
- `illegal character: '\ufffd'` (Ersatzzeichen)
- `illegal character: '\u0000'` (Null-Bytes)

## Ursachen
1. **Falsche Zeichenkodierung**: Dateien sind nicht in UTF-8 gespeichert
2. **Byte-Order-Marks (BOM)**: Dateien enthalten BOM-Zeichen
3. **Beschädigte Dateien**: Dateien enthalten Null-Bytes oder Binärdaten
4. **Minecraft-Farbcodes**: §-Zeichen verursachen Probleme

## Lösungen

### 1. Automatische Lösung (Empfohlen)
```bash
# Windows
fix-encoding.bat

# Linux/Mac
./fix-encoding.sh
```

### 2. Manuelle Maven-Kompilierung
```bash
# Mit expliziter UTF-8 Kodierung
mvn clean compile -Dfile.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8

# Komplettes Build
mvn clean package -Dfile.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8
```

### 3. IDE-Einstellungen

#### IntelliJ IDEA
1. File → Settings → Editor → File Encodings
2. Global Encoding: UTF-8
3. Project Encoding: UTF-8
4. Default encoding for properties files: UTF-8
5. Transparent native-to-ascii conversion: ✓

#### VS Code
1. Settings → Text Editor → Files
2. Encoding: utf8
3. Auto Guess Encoding: ✓

#### Eclipse
1. Window → Preferences → General → Workspace
2. Text file encoding: UTF-8

### 4. Dateien manuell reparieren

#### Problemdateien identifizieren
```bash
# Finde Dateien mit Kodierungsproblemen
grep -r -P "[\x00-\x08\x0E-\x1F\x7F-\xFF]" src/ --include="*.java"

# Oder mit PowerShell (Windows)
Get-ChildItem -Recurse -Include "*.java" | Select-String -Pattern "[\x00-\x08\x0E-\x1F\x7F-\xFF]"
```

#### Dateien konvertieren
1. Öffne die problematische Datei in einem Texteditor
2. Speichere sie als UTF-8 ohne BOM
3. Überprüfe, dass keine Null-Bytes enthalten sind

### 5. Minecraft-Farbcodes ersetzen

#### Problem
Minecraft-Farbcodes (§) können Kodierungsprobleme verursachen.

#### Lösung
```java
// Statt § verwenden Sie:
String message = "§aGrün"; // Kann Probleme verursachen
String message = "&aGrün"; // Besser, wird später ersetzt
String message = ChatColor.GREEN + "Grün"; // Am besten
```

### 6. System-Umgebungsvariablen setzen

#### Windows
```cmd
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
set MAVEN_OPTS=-Dfile.encoding=UTF-8
```

#### Linux/Mac
```bash
export JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
export MAVEN_OPTS="-Dfile.encoding=UTF-8"
```

## Prävention

### 1. .gitattributes verwenden
Die bereitgestellte `.gitattributes`-Datei stellt sicher, dass alle Java-Dateien als UTF-8 behandelt werden.

### 2. Maven-Konfiguration
Die `pom.xml` wurde aktualisiert mit:
```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

### 3. IDE-Konfiguration
Stellen Sie sicher, dass Ihre IDE standardmäßig UTF-8 verwendet.

## Verifikation

### Erfolgreiche Kompilierung prüfen
```bash
mvn clean compile
# Sollte ohne Kodierungsfehler durchlaufen
```

### JAR-Datei erstellen
```bash
mvn clean package
# Erstellt target/BasicsPlugin-1.0-SNAPSHOT.jar
```

## Häufige Fehler

### 1. "unmappable character"
- **Ursache**: Datei nicht in UTF-8
- **Lösung**: Datei als UTF-8 ohne BOM speichern

### 2. "illegal character: '\u0000'"
- **Ursache**: Null-Bytes in der Datei
- **Lösung**: Datei neu erstellen oder aus Git wiederherstellen

### 3. "class, interface, enum, or record expected"
- **Ursache**: Beschädigte Dateistruktur
- **Lösung**: Datei vollständig neu erstellen

## Support

Falls die Probleme weiterhin bestehen:
1. Überprüfen Sie die IDE-Einstellungen
2. Stellen Sie sicher, dass alle Dateien als UTF-8 gespeichert sind
3. Verwenden Sie die bereitgestellten Skripte
4. Bei Bedarf die problematischen Dateien einzeln neu erstellen
