# Multi-Server-System Setup Guide

## 📋 Inhaltsverzeichnis
1. [Überblick](#überblick)
2. [System-Anforderungen](#system-anforderungen)
3. [Installation](#installation)
4. [Konfiguration](#konfiguration)
5. [Server-Typen](#server-typen)
6. [Commands](#commands)
7. [Troubleshooting](#troubleshooting)
8. [Erweiterte Konfiguration](#erweiterte-konfiguration)

## 🎯 Überblick

Das Multi-Server-System ermöglicht es, mehrere Minecraft-Server für verschiedene Zwecke zu verwalten:
- **Hub Server** - Hauptserver für Lobby und Navigation
- **Island Server** - Private Inseln für Spieler
- **Dungeon Server** - Dungeon-Instanzen
- **Event Server** - Event-basierte Inseln
- **Auction Server** - Auktionshaus
- **Bank Server** - Bank-System
- **Minigame Server** - Minigames
- **PvP Server** - PvP-Arenen

## 🔧 System-Anforderungen

### Mindestanforderungen:
- **Java 17+**
- **Spigot/Paper 1.20+**
- **RAM**: 2GB pro Server
- **CPU**: 2 Kerne pro Server
- **Speicher**: 10GB freier Speicher

### Empfohlene Anforderungen:
- **Java 21**
- **Paper 1.20.4**
- **RAM**: 4GB pro Server
- **CPU**: 4 Kerne pro Server
- **SSD**: 50GB freier Speicher

### Optionale Dependencies:
- **Redis** (für erweiterte Features)
- **MySQL** (für persistente Daten)

## 📦 Installation

### Schritt 1: Plugin-Datei herunterladen
```bash
# Lade die Basics Plugin JAR-Datei herunter
wget https://github.com/your-repo/basics-plugin.jar
```

### Schritt 2: Server-Verzeichnisse erstellen
```bash
# Erstelle Verzeichnisse für verschiedene Server-Typen
mkdir -p /opt/minecraft/servers/{hub,island,dungeon,event,auction,bank,minigame,pvp}
```

### Schritt 3: Plugin in alle Server installieren
```bash
# Kopiere das Plugin in alle Server-Verzeichnisse
for server in hub island dungeon event auction bank minigame pvp; do
    cp basics-plugin.jar /opt/minecraft/servers/$server/plugins/
done
```

### Schritt 4: Server-Properties konfigurieren
```bash
# Für jeden Server eine server.properties erstellen
cat > /opt/minecraft/servers/hub/server.properties << EOF
server-port=25565
server-name=Hub Server
motd=§6§lBasics Plugin §7- §eHub Server
online-mode=false
white-list=false
EOF

cat > /opt/minecraft/servers/island/server.properties << EOF
server-port=25566
server-name=Island Server
motd=§6§lBasics Plugin §7- §aIsland Server
online-mode=false
white-list=false
EOF

# Wiederhole für alle anderen Server mit entsprechenden Ports
```

## ⚙️ Konfiguration

### Schritt 1: Hauptkonfiguration (config.yml)
```yaml
# /opt/minecraft/servers/hub/config.yml
network:
  enabled: true
  server-type: HUB
  hub-address: "localhost"
  hub-port: 25565
  
  # Redis-Konfiguration (optional)
  redis:
    enabled: false
    host: "localhost"
    port: 6379
    password: ""
  
  # MySQL-Konfiguration (optional)
  mysql:
    enabled: false
    host: "localhost"
    port: 3306
    database: "basics_network"
    username: "root"
    password: "password"
  
  # Server-Limits
  max-players-per-server: 100
  max-islands-per-server: 50
  
  # Timing-Konfiguration
  server-heartbeat-interval: 1000  # ms
  data-sync-interval: 5000         # ms
```

### Schritt 2: Island-Server-Konfiguration
```yaml
# /opt/minecraft/servers/island/config.yml
network:
  enabled: true
  server-type: ISLAND
  hub-address: "localhost"
  hub-port: 25565
  
  # Island-spezifische Einstellungen
  island:
    size: 100
    spacing: 200
    max-members: 10
    auto-save-interval: 300  # 5 Minuten
```

### Schritt 3: Dungeon-Server-Konfiguration
```yaml
# /opt/minecraft/servers/dungeon/config.yml
network:
  enabled: true
  server-type: DUNGEON
  hub-address: "localhost"
  hub-port: 25565
  
  # Dungeon-spezifische Einstellungen
  dungeon:
    max-instances: 50
    instance-timeout: 3600  # 1 Stunde
    auto-cleanup: true
```

## 🖥️ Server-Typen

### Hub Server (Port 25565)
- **Zweck**: Hauptserver für Lobby und Navigation
- **Features**: 
  - Player-Spawn
  - Server-Navigation
  - Warp-System
  - Lobby-Features

### Island Server (Port 25566)
- **Zweck**: Private Inseln für Spieler
- **Features**:
  - Island-Erstellung
  - Island-Management
  - Member-System
  - Island-Upgrades

### Dungeon Server (Port 25567)
- **Zweck**: Dungeon-Instanzen
- **Features**:
  - Dungeon-Instanzen
  - Boss-Fights
  - Loot-System
  - Progress-Tracking

### Event Server (Port 25568)
- **Zweck**: Event-basierte Inseln
- **Features**:
  - Temporäre Events
  - Event-Inseln
  - Event-Rewards
  - Event-Tracking

### Auction Server (Port 25569)
- **Zweck**: Auktionshaus
- **Features**:
  - Item-Auktionen
  - Bidding-System
  - Auction-History
  - Fee-System

### Bank Server (Port 25570)
- **Zweck**: Bank-System
- **Features**:
  - Player-Banking
  - Interest-System
  - Loan-System
  - Transaction-History

### Minigame Server (Port 25571)
- **Zweck**: Minigames
- **Features**:
  - Verschiedene Minigames
  - Leaderboards
  - Rewards
  - Tournament-System

### PvP Server (Port 25572)
- **Zweck**: PvP-Arenen
- **Features**:
  - PvP-Arenen
  - Ranking-System
  - Tournament-Mode
  - Spectator-Mode

## 💻 Commands

### Network Commands
```bash
# Netzwerk-Status anzeigen
/network status

# Alle Server anzeigen
/network servers

# Netzwerk-Informationen
/network info

# Hilfe anzeigen
/network help
```

### Transfer Commands
```bash
# Zu Hub-Server transferieren
/transfer hub

# Zu Island-Server transferieren
/transfer island

# Zu Dungeon-Server transferieren
/transfer dungeon

# Mit spezifischem Grund transferieren
/transfer island ISLAND_ACCESS
```

### Island Commands
```bash
# Insel erstellen
/island create

# Insel mit spezifischem Typ erstellen
/island create PRIVATE

# Insel eines Spielers besuchen
/island visit <player>

# Insel-Mitglieder anzeigen
/island members

# Spieler zur Insel einladen
/island invite <player>

# Spieler von Insel entfernen
/island kick <player>

# Insel-Informationen
/island info

# Hilfe anzeigen
/island help
```

## 🚀 Server starten

### Schritt 1: Start-Skripte erstellen
```bash
# Erstelle Start-Skript für Hub
cat > /opt/minecraft/servers/hub/start.sh << EOF
#!/bin/bash
cd /opt/minecraft/servers/hub
java -Xmx4G -Xms2G -jar paper-1.20.4.jar nogui
EOF

# Erstelle Start-Skript für Island
cat > /opt/minecraft/servers/island/start.sh << EOF
#!/bin/bash
cd /opt/minecraft/servers/island
java -Xmx4G -Xms2G -jar paper-1.20.4.jar nogui
EOF

# Wiederhole für alle anderen Server
```

### Schritt 2: Skripte ausführbar machen
```bash
chmod +x /opt/minecraft/servers/*/start.sh
```

### Schritt 3: Server starten
```bash
# Starte Hub-Server
/opt/minecraft/servers/hub/start.sh &

# Starte Island-Server
/opt/minecraft/servers/island/start.sh &

# Starte alle anderen Server
for server in dungeon event auction bank minigame pvp; do
    /opt/minecraft/servers/$server/start.sh &
done
```

### Schritt 4: Systemd-Services erstellen (optional)
```bash
# Erstelle systemd-Service für Hub
cat > /etc/systemd/system/minecraft-hub.service << EOF
[Unit]
Description=Minecraft Hub Server
After=network.target

[Service]
Type=simple
User=minecraft
WorkingDirectory=/opt/minecraft/servers/hub
ExecStart=/opt/minecraft/servers/hub/start.sh
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# Aktiviere und starte Service
systemctl enable minecraft-hub
systemctl start minecraft-hub
```

## 🔧 Troubleshooting

### Häufige Probleme

#### Problem: Server startet nicht
```bash
# Überprüfe Logs
tail -f /opt/minecraft/servers/hub/logs/latest.log

# Überprüfe Java-Version
java -version

# Überprüfe Port-Verfügbarkeit
netstat -tulpn | grep :25565
```

#### Problem: Plugin lädt nicht
```bash
# Überprüfe Plugin-Ordner
ls -la /opt/minecraft/servers/hub/plugins/

# Überprüfe Plugin-Kompatibilität
# Stelle sicher, dass die Plugin-Version mit der Server-Version kompatibel ist
```

#### Problem: Server können nicht kommunizieren
```bash
# Überprüfe Firewall-Einstellungen
ufw status

# Überprüfe Netzwerk-Konnektivität
ping localhost
telnet localhost 25565
```

#### Problem: Player-Transfer funktioniert nicht
```bash
# Überprüfe Server-Status
/network status

# Überprüfe Server-Verbindung
/network servers

# Überprüfe Logs für Fehler
tail -f /opt/minecraft/servers/hub/logs/latest.log
```

### Debug-Commands
```bash
# Detaillierte Logs aktivieren
# In server.properties:
debug=true

# Plugin-Debug aktivieren
# In config.yml:
debug: true
```

## 🔧 Erweiterte Konfiguration

### Redis-Integration (optional)
```yaml
# config.yml
network:
  redis:
    enabled: true
    host: "redis.example.com"
    port: 6379
    password: "your-redis-password"
    database: 0
    timeout: 2000
    pool:
      max-total: 20
      max-idle: 10
      min-idle: 5
```

### MySQL-Integration (optional)
```yaml
# config.yml
network:
  mysql:
    enabled: true
    host: "mysql.example.com"
    port: 3306
    database: "basics_network"
    username: "basics_user"
    password: "your-mysql-password"
    ssl: false
    pool:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
```

### Load-Balancing
```yaml
# config.yml
network:
  load-balancing:
    enabled: true
    algorithm: "round-robin"  # oder "least-connections"
    health-check-interval: 5000
    failover-timeout: 30000
```

### Monitoring
```yaml
# config.yml
network:
  monitoring:
    enabled: true
    metrics:
      - player-count
      - server-load
      - transfer-stats
      - island-count
    alerts:
      - high-cpu-usage
      - low-memory
      - transfer-failures
```

## 📊 Performance-Optimierung

### JVM-Optimierung
```bash
# Optimierte JVM-Parameter
java -Xmx4G -Xms2G \
     -XX:+UseG1GC \
     -XX:+ParallelRefProcEnabled \
     -XX:MaxGCPauseMillis=200 \
     -XX:+UnlockExperimentalVMOptions \
     -XX:+DisableExplicitGC \
     -XX:+AlwaysPreTouch \
     -XX:G1NewSizePercent=30 \
     -XX:G1MaxNewSizePercent=40 \
     -XX:G1HeapRegionSize=8M \
     -XX:G1ReservePercent=20 \
     -XX:G1HeapWastePercent=5 \
     -XX:G1MixedGCCountTarget=4 \
     -XX:InitiatingHeapOccupancyPercent=15 \
     -XX:G1MixedGCLiveThresholdPercent=90 \
     -XX:G1RSetUpdatingPauseTimePercent=5 \
     -XX:SurvivorRatio=32 \
     -XX:+PerfDisableSharedMem \
     -XX:MaxTenuringThreshold=1 \
     -jar paper-1.20.4.jar nogui
```

### Server-Optimierung
```yaml
# paper.yml
chunk-loading:
  autoconfig-send-distance: true
  max-concurrent-sends: 2
  max-concurrent-sends-per-world: 1
  min-load-radius: 2
  max-load-radius: 8
  target-player-chunk-send-rate: 100.0
  global-max-chunk-load-rate: -1.0
  global-max-concurrent-loads: 500.0
  player-max-concurrent-loads: 20.0
  chunk-loading-thread-count: -1
```

## 🔒 Sicherheit

### Firewall-Konfiguration
```bash
# UFW-Firewall konfigurieren
ufw allow 25565/tcp  # Hub
ufw allow 25566/tcp  # Island
ufw allow 25567/tcp  # Dungeon
ufw allow 25568/tcp  # Event
ufw allow 25569/tcp  # Auction
ufw allow 25570/tcp  # Bank
ufw allow 25571/tcp  # Minigame
ufw allow 25572/tcp  # PvP

# SSH-Zugang erlauben
ufw allow ssh

# Firewall aktivieren
ufw enable
```

### SSL/TLS-Konfiguration (optional)
```yaml
# config.yml
network:
  ssl:
    enabled: true
    keystore: "/path/to/keystore.jks"
    keystore-password: "your-keystore-password"
    truststore: "/path/to/truststore.jks"
    truststore-password: "your-truststore-password"
```

## 📈 Monitoring und Wartung

### Log-Rotation
```bash
# Logrotate-Konfiguration
cat > /etc/logrotate.d/minecraft << EOF
/opt/minecraft/servers/*/logs/*.log {
    daily
    missingok
    rotate 7
    compress
    delaycompress
    notifempty
    create 644 minecraft minecraft
    postrotate
        systemctl reload minecraft-hub
        systemctl reload minecraft-island
        # Wiederhole für alle anderen Server
    endscript
}
EOF
```

### Backup-System
```bash
# Backup-Skript erstellen
cat > /opt/minecraft/backup.sh << EOF
#!/bin/bash
BACKUP_DIR="/opt/minecraft/backups"
DATE=$(date +%Y%m%d_%H%M%S)

# Erstelle Backup-Verzeichnis
mkdir -p $BACKUP_DIR

# Backup für jeden Server
for server in hub island dungeon event auction bank minigame pvp; do
    tar -czf $BACKUP_DIR/${server}_${DATE}.tar.gz \
        -C /opt/minecraft/servers $server
done

# Lösche alte Backups (älter als 7 Tage)
find $BACKUP_DIR -name "*.tar.gz" -mtime +7 -delete
EOF

# Cron-Job für tägliche Backups
echo "0 2 * * * /opt/minecraft/backup.sh" | crontab -
```

## 🎯 Fazit

Das Multi-Server-System bietet eine robuste und skalierbare Lösung für Minecraft-Server-Netzwerke. Mit dieser Anleitung sollten Sie in der Lage sein, das System erfolgreich einzurichten und zu betreiben.

### Nächste Schritte:
1. **Testen**: Starten Sie mit einem Hub- und Island-Server
2. **Erweitern**: Fügen Sie nach Bedarf weitere Server-Typen hinzu
3. **Optimieren**: Passen Sie die Konfiguration an Ihre Bedürfnisse an
4. **Überwachen**: Implementieren Sie Monitoring und Alerting
5. **Warten**: Führen Sie regelmäßige Backups und Updates durch

### Support:
- **GitHub Issues**: Für Bug-Reports und Feature-Requests
- **Discord**: Für Community-Support
- **Wiki**: Für detaillierte Dokumentation

Viel Erfolg mit Ihrem Multi-Server-System! 🚀
