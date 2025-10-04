# Plugin Build Anweisungen

## Voraussetzungen

Um das Plugin zu builden, benötigst du:

1. **Java Development Kit (JDK) 21 oder höher**
   - Download: https://adoptium.net/
   - Installation: Installiere das JDK und setze die JAVA_HOME Umgebungsvariable

2. **Apache Maven 3.9.6 oder höher**
   - Download: https://maven.apache.org/download.cgi
   - Installation: Entpacke Maven und füge es zum PATH hinzu

## Build-Prozess

### Option 1: Mit Maven (Empfohlen)

```bash
# Im Projektverzeichnis
mvn clean package
```

Die kompilierte JAR-Datei findest du dann in: `target/BasicsPlugin-1.0.0.jar`

### Option 1b: Mit Vault-Unterstützung

Falls du Vault-Integration benötigst:

```bash
# Verwende die pom-with-vault.xml
mvn clean package -f pom-with-vault.xml
```

**Hinweis:** Das Plugin funktioniert auch ohne Vault! Die Vault-Integration ist optional.

### Option 2: Mit IntelliJ IDEA

1. Öffne das Projekt in IntelliJ IDEA
2. Warte, bis Maven die Abhängigkeiten heruntergeladen hat
3. Öffne das Maven-Toolfenster (rechts)
4. Erweitere: `Lifecycle` → `package`
5. Doppelklick auf `package`

### Option 3: Mit Eclipse

1. Öffne das Projekt in Eclipse
2. Rechtsklick auf das Projekt → `Run As` → `Maven build...`
3. Goals: `clean package`
4. Klicke `Run`

## Nach dem Build

1. Kopiere die JAR-Datei aus dem `target`-Ordner
2. Füge sie in den `plugins`-Ordner deines Minecraft-Servers ein
3. Starte den Server neu

## Plugin-Features

Das Plugin enthält folgende Hauptfunktionen:

### ✅ Vollständig implementiert:
- **Economy System** mit Vault-Integration
- **Rank System** mit Permission-Management
- **Command Management** mit Cooldowns und Kosten
- **Basic Commands GUI** (Nickname, Prefix, Workbenches)
- **Join/Leave Messages GUI** mit Presets
- **Feature Book GUI** mit vollständiger Plugin-Übersicht
- **Party System** (create, invite, accept, leave, list)
- **Friends System** (add, accept, remove)
- **Teleportation** (spawn, back, rtp, tpa, warps, homes)
- **Cosmetics** (particles, sounds, wings, halos, trails)
- **Achievements** mit Tiers und Progress
- **Kit System** mit Shop
- **Daily Rewards** mit Streak-System
- **Events System**
- **Scoreboard** mit dynamischen Inhalten
- **Admin Tools** (vanish, invsee, rank management)

### 🔧 Konfiguration:
- Alle Features sind über `config.yml` konfigurierbar
- Economy-Einstellungen in `economy.yml`
- Rank-Permissions in `ranks.yml`
- Command-Settings in `commands.yml`

### 🎮 GUI-System:
- Vollständig ausgebautes Hauptmenü
- Intuitive Navigation zwischen allen Features
- Permission-basierte Zugriffe
- Real-time Updates

## Support

Bei Problemen:
1. Prüfe die Server-Logs auf Fehler
2. Stelle sicher, dass alle Abhängigkeiten installiert sind
3. Überprüfe die Konfigurationsdateien
4. Teste mit einem frischen Server-Setup

## Dependencies

Das Plugin benötigt:
- **Spigot/Paper 1.21+**
- **Vault** (optional, für Economy-Integration mit anderen Plugins)
- **Java 21+**

## Vault-Integration

Das Plugin unterstützt Vault-Integration für Economy-Systeme:
- **Mit Vault:** Vollständige Integration mit anderen Economy-Plugins
- **Ohne Vault:** Eigenes Economy-System funktioniert trotzdem
- **Automatische Erkennung:** Plugin erkennt Vault zur Laufzeit

Viel Erfolg beim Builden! 🚀
