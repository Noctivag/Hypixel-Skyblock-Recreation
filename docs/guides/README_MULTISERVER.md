# 🚀 Multi-Server-System für das Basics Plugin

## 📋 Überblick

Das Multi-Server-System ermöglicht es, mehrere Minecraft-Server für verschiedene Zwecke zu verwalten und zu koordinieren. Es bietet eine nahtlose Integration zwischen verschiedenen Server-Typen und ermöglicht es Spielern, zwischen Servern zu wechseln, ohne ihre Daten zu verlieren.

## ✨ Features

### 🏠 Server-Typen
- **Hub Server** - Hauptserver für Lobby und Navigation
- **Island Server** - Private Inseln für Spieler
- **Dungeon Server** - Dungeon-Instanzen
- **Event Server** - Event-basierte Inseln
- **Auction Server** - Auktionshaus
- **Bank Server** - Bank-System
- **Minigame Server** - Minigames
- **PvP Server** - PvP-Arenen

### 🔄 Player-Transfer
- Nahtloser Transfer zwischen Servern
- Automatische Daten-Synchronisation
- Transfer-Status-Tracking
- Fehlerbehandlung und Timeouts

### 🏝️ Island-System
- Automatische Insel-Erstellung
- Verschiedene Insel-Typen (Private, Coop, Guild, Public, Event, Dungeon)
- Mitglieder-Verwaltung
- Zugriffskontrolle

### 📊 Daten-Synchronisation
- Echtzeit-Daten-Synchronisation zwischen Servern
- Player-Daten-Sync
- Island-Daten-Sync
- Collection-Daten-Sync
- Minion-Daten-Sync

### 💻 Commands
- `/network` - Netzwerk-Verwaltung
- `/island` - Island-Management
- `/transfer` - Server-Transfer

## 🛠️ Installation

### Schnellstart
```bash
# 1. Repository klonen
git clone https://github.com/your-repo/basics-plugin.git
cd basics-plugin

# 2. Setup ausführen
./scripts/start-multiserver.sh setup

# 3. Paper JAR herunterladen und platzieren
# Für jeden Server: paper-1.20.4.jar in das entsprechende Verzeichnis

# 4. Plugin kopieren
cp target/basics-plugin.jar servers/*/plugins/

# 5. Server starten
./scripts/start-multiserver.sh start
```

### Docker-Installation
```bash
# 1. Repository klonen
git clone https://github.com/your-repo/basics-plugin.git
cd basics-plugin

# 2. Docker Compose starten
docker-compose up -d

# 3. Status überprüfen
docker-compose ps
```

### Systemd-Installation
```bash
# 1. Service-Datei kopieren
sudo cp scripts/minecraft-multiserver.service /etc/systemd/system/

# 2. Service aktivieren
sudo systemctl enable minecraft-multiserver

# 3. Service starten
sudo systemctl start minecraft-multiserver

# 4. Status überprüfen
sudo systemctl status minecraft-multiserver
```

## ⚙️ Konfiguration

### Grundlegende Konfiguration
```yaml
# config.yml
network:
  enabled: true
  server-type: HUB  # HUB, ISLAND, DUNGEON, etc.
  hub-address: "localhost"
  hub-port: 25565
```

### Erweiterte Konfiguration
```yaml
# config.yml
network:
  redis:
    enabled: true
    host: "localhost"
    port: 6379
  
  mysql:
    enabled: true
    host: "localhost"
    port: 3306
    database: "basics_network"
```

## 🎮 Verwendung

### Commands

#### Network Commands
```bash
/network status          # Zeigt Netzwerk-Status
/network servers         # Zeigt alle Server
/network info           # Zeigt Netzwerk-Informationen
/network help           # Zeigt Hilfe
```

#### Transfer Commands
```bash
/transfer hub           # Transfer zum Hub-Server
/transfer island        # Transfer zum Island-Server
/transfer dungeon       # Transfer zum Dungeon-Server
```

#### Island Commands
```bash
/island create          # Erstellt eine Insel
/island visit <player>  # Besucht Spieler-Insel
/island members         # Zeigt Insel-Mitglieder
/island invite <player> # Lädt Spieler ein
/island kick <player>   # Entfernt Spieler
```

### Server-Verwaltung

#### Start-Skript verwenden
```bash
# Alle Server starten
./scripts/start-multiserver.sh start

# Spezifischen Server starten
./scripts/start-multiserver.sh start hub

# Server-Status anzeigen
./scripts/start-multiserver.sh status

# Logs anzeigen
./scripts/start-multiserver.sh logs hub
```

#### Docker verwenden
```bash
# Alle Services starten
docker-compose up -d

# Spezifischen Service starten
docker-compose up -d hub

# Logs anzeigen
docker-compose logs -f hub

# Services stoppen
docker-compose down
```

## 🔧 Troubleshooting

### Häufige Probleme

#### Server startet nicht
```bash
# Logs überprüfen
tail -f servers/hub/logs/latest.log

# Java-Version überprüfen
java -version

# Port-Verfügbarkeit überprüfen
netstat -tulpn | grep :25565
```

#### Plugin lädt nicht
```bash
# Plugin-Ordner überprüfen
ls -la servers/hub/plugins/

# Kompatibilität überprüfen
# Stelle sicher, dass die Plugin-Version mit der Server-Version kompatibel ist
```

#### Player-Transfer funktioniert nicht
```bash
# Server-Status überprüfen
/network status

# Server-Verbindung überprüfen
/network servers

# Logs für Fehler überprüfen
tail -f servers/hub/logs/latest.log
```

### Debug-Modus aktivieren
```yaml
# config.yml
network:
  debug:
    enabled: true
    log-level: "DEBUG"
```

## 📊 Monitoring

### Prometheus + Grafana
```bash
# Monitoring starten
docker-compose up -d prometheus grafana

# Grafana öffnen
open http://localhost:3000
# Login: admin/admin
```

### Logs überwachen
```bash
# Alle Server-Logs
tail -f servers/*/logs/latest.log

# Spezifischen Server-Log
tail -f servers/hub/logs/latest.log
```

## 🔒 Sicherheit

### Firewall konfigurieren
```bash
# UFW-Firewall
ufw allow 25565:25572/tcp
ufw allow ssh
ufw enable
```

### SSL/TLS aktivieren
```yaml
# config.yml
network:
  security:
    ssl:
      enabled: true
      keystore: "/path/to/keystore.jks"
```

## 📈 Performance-Optimierung

### JVM-Optimierung
```bash
# Optimierte JVM-Parameter
java -Xmx4G -Xms2G \
     -XX:+UseG1GC \
     -XX:+ParallelRefProcEnabled \
     -XX:MaxGCPauseMillis=200 \
     -jar paper-1.20.4.jar nogui
```

### Server-Optimierung
```yaml
# paper.yml
chunk-loading:
  autoconfig-send-distance: true
  max-concurrent-sends: 2
```

## 🔄 Updates

### Plugin aktualisieren
```bash
# 1. Backup erstellen
./scripts/start-multiserver.sh stop
cp -r servers servers_backup_$(date +%Y%m%d)

# 2. Plugin aktualisieren
cp new-basics-plugin.jar servers/*/plugins/

# 3. Server neu starten
./scripts/start-multiserver.sh start
```

### Docker aktualisieren
```bash
# 1. Images aktualisieren
docker-compose pull

# 2. Services neu starten
docker-compose up -d
```

## 📚 Dokumentation

### Weitere Ressourcen
- [Setup Guide](MULTISERVER_SETUP_GUIDE.md) - Detaillierte Installationsanleitung
- [API Dokumentation](docs/API.md) - Entwickler-API
- [Plugin Development](docs/PLUGIN_DEVELOPMENT.md) - Plugin-Entwicklung
- [FAQ](docs/FAQ.md) - Häufig gestellte Fragen

### Support
- **GitHub Issues**: [Bug-Reports und Feature-Requests](https://github.com/your-repo/basics-plugin/issues)
- **Discord**: [Community-Support](https://discord.gg/your-discord)
- **Wiki**: [Detaillierte Dokumentation](https://github.com/your-repo/basics-plugin/wiki)

## 🤝 Beitragen

### Entwicklung
```bash
# Repository forken und klonen
git clone https://github.com/your-username/basics-plugin.git
cd basics-plugin

# Entwicklungsumgebung einrichten
./gradlew build

# Tests ausführen
./gradlew test

# Pull Request erstellen
```

### Bug-Reports
Bitte verwenden Sie das [GitHub Issue System](https://github.com/your-repo/basics-plugin/issues) für Bug-Reports.

### Feature-Requests
Feature-Requests sind willkommen! Bitte erstellen Sie ein Issue mit dem Label "enhancement".

## 📄 Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert. Siehe [LICENSE](LICENSE) für Details.

## 🙏 Danksagungen

- **PaperMC** - Für das großartige Paper-Server-Software
- **Spigot** - Für die Bukkit/Spigot-API
- **Community** - Für Feedback und Beiträge

---

**Viel Spaß mit dem Multi-Server-System! 🎮**
