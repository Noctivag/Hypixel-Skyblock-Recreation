# 🚀 Umfassende Multi-Server-System Anleitung

## 📋 Inhaltsverzeichnis
1. [System-Überblick](#system-überblick)
2. [Erweiterte Features](#erweiterte-features)
3. [Multithreading & Performance](#multithreading--performance)
4. [Installation & Setup](#installation--setup)
5. [Server-Typen & Konfiguration](#server-typen--konfiguration)
6. [Welt-Management](#welt-management)
7. [Commands & Verwaltung](#commands--verwaltung)
8. [Monitoring & Analytics](#monitoring--analytics)
9. [Troubleshooting](#troubleshooting)
10. [Erweiterte Konfiguration](#erweiterte-konfiguration)

---

## 🎯 System-Überblick

Das erweiterte Multi-Server-System ist eine **enterprise-grade** Lösung für komplexe Minecraft-Server-Netzwerke mit:

### ✨ Kern-Features
- **25+ Server-Typen** für verschiedene Spielmodi
- **Multithreaded Architektur** für maximale Performance
- **Thread-sichere Welt-Verwaltung** mit asynchronen Operationen
- **Custom World Loading** für eigene Welten
- **Erweiterte Server-Kommunikation** mit Load Balancing
- **Echtzeit-Monitoring** und Performance-Analytics
- **Cross-Server Data Synchronization** in Echtzeit

### 🏗️ Architektur-Komponenten
- **ThreadSafeWorldManager** - Multithreaded Welt-Verwaltung
- **AdvancedNetworkManager** - Erweiterte Server-Kommunikation
- **ServerCommunicationManager** - Echtzeit-Server-Kommunikation
- **AdvancedPlayerTransferSystem** - Intelligente Player-Transfers
- **ServerLoadBalancer** - 5 verschiedene Load Balancing-Strategien
- **CrossServerDataSync** - Echtzeit-Daten-Synchronisation
- **ServerMonitoringSystem** - Umfassendes Monitoring

---

## 🧵 Multithreading & Performance

### Thread-Safe Architektur
```java
// ExecutorService-basierte Architektur
private final ExecutorService worldExecutor = Executors.newFixedThreadPool(4);
private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

// Thread-sichere Datenstrukturen
private final Map<String, World> managedWorlds = new ConcurrentHashMap<>();
private final AtomicBoolean isInitialized = new AtomicBoolean(false);
```

### Performance-Optimierungen
- **Parallele Welt-Initialisierung** mit CompletableFuture
- **Asynchrone Welt-Operationen** ohne Server-Blockierung
- **Concurrent World Access** mit Lock-Mechanismen
- **Memory-Efficient** Datenstrukturen
- **Optimized Chunk Generation** mit Custom Generators

### Monitoring & Analytics
- **Echtzeit-Metriken** für alle Welten
- **Performance-Tracking** mit historischen Daten
- **Thread Pool Monitoring** für aktive Operationen
- **Resource Usage Tracking** (Player Count, Chunks, etc.)

---

## 🛠️ Installation & Setup

### System-Anforderungen
```bash
# Mindestanforderungen
Java 17+
Paper 1.20.4+
RAM: 4GB pro Server
CPU: 4 Kerne pro Server
SSD: 50GB freier Speicher

# Empfohlene Anforderungen
Java 21
Paper 1.20.4
RAM: 8GB pro Server
CPU: 8 Kerne pro Server
SSD: 100GB freier Speicher
```

### Schnellstart-Installation
```bash
# 1. Repository klonen
git clone https://github.com/your-repo/basics-plugin.git
cd basics-plugin

# 2. Plugin kompilieren
./gradlew build

# 3. Server-Verzeichnisse erstellen
mkdir -p servers/{hub,island,dungeon,event,auction,bank,minigame,pvp,creative,survival,hardcore,adventure,spectator,test,resource,nether,end,skyblock,bedwars,skywars,uhc,kitpvp,prison,factions,towny,proxy,lobby,queue,maintenance}

# 4. Plugin in alle Server installieren
cp target/basics-plugin.jar servers/*/plugins/

# 5. Server starten
./scripts/start-multiserver.sh start
```

### Docker-Installation
```bash
# Docker Compose starten
docker-compose up -d

# Status überprüfen
docker-compose ps

# Logs anzeigen
docker-compose logs -f
```

---

## 🖥️ Server-Typen & Konfiguration

### Basis Server-Typen
| Server-Typ | Port | Beschreibung | Max Players |
|------------|------|--------------|-------------|
| **HUB** | 25565 | Hauptserver für Lobby und Navigation | 200 |
| **ISLAND** | 25566 | Private Inseln für Spieler | 100 |
| **DUNGEON** | 25567 | Dungeon-Instanzen | 20 |
| **EVENT** | 25568 | Event-basierte Inseln | 150 |
| **AUCTION** | 25569 | Auktionshaus | 50 |
| **BANK** | 25570 | Bank-System | 30 |
| **MINIGAME** | 25571 | Minigames | 80 |
| **PVP** | 25572 | PvP-Arenen | 60 |

### Erweiterte Server-Typen
| Server-Typ | Port | Beschreibung | Max Players |
|------------|------|--------------|-------------|
| **CREATIVE** | 25573 | Creative-Modus Welten | 100 |
| **SURVIVAL** | 25574 | Survival-Modus Welten | 150 |
| **HARDCORE** | 25575 | Hardcore-Modus Welten | 50 |
| **ADVENTURE** | 25576 | Adventure-Modus Welten | 80 |
| **SPECTATOR** | 25577 | Spectator-Modus Welten | 200 |
| **TEST** | 25578 | Test- und Entwicklungs-Welten | 20 |
| **RESOURCE** | 25579 | Resource-Sammel-Welten | 100 |
| **NETHER** | 25580 | Nether-Dimension Welten | 80 |
| **END** | 25581 | End-Dimension Welten | 60 |

### Spezialisierte Server-Typen
| Server-Typ | Port | Beschreibung | Max Players |
|------------|------|--------------|-------------|
| **SKYBLOCK** | 25582 | Skyblock-spezifische Welten | 120 |
| **BEDWARS** | 25583 | Bedwars-Minigames | 100 |
| **SKYWARS** | 25584 | Skywars-Minigames | 100 |
| **UHC** | 25585 | Ultra Hardcore Games | 50 |
| **KITPVP** | 25586 | Kit-basierte PvP-Arenen | 80 |
| **PRISON** | 25587 | Prison-Spielmodus | 100 |
| **FACTIONS** | 25588 | Factions-Spielmodus | 120 |
| **TOWNY** | 25589 | Towny-Spielmodus | 100 |

### Technische Server-Typen
| Server-Typ | Port | Beschreibung | Max Players |
|------------|------|--------------|-------------|
| **PROXY** | 25590 | BungeeCord/Velocity Proxy | 1000 |
| **LOBBY** | 25591 | Hauptlobby für alle Spieler | 500 |
| **QUEUE** | 25592 | Warteschlangen-Server | 200 |
| **MAINTENANCE** | 25593 | Wartungs-Server | 10 |

---

## 🌍 Welt-Management

### Thread-Safe Welt-Verwaltung
```java
// Asynchrone Welt-Initialisierung
public CompletableFuture<Boolean> initializeAllWorlds() {
    // Alle Welten werden parallel erstellt
    List<CompletableFuture<World>> worldFutures = new ArrayList<>();
    for (Map.Entry<String, WorldConfig> entry : worldConfigs.entrySet()) {
        CompletableFuture<World> worldFuture = initializeWorldAsync(worldName, config);
        worldFutures.add(worldFuture);
    }
    return CompletableFuture.allOf(worldFutures.toArray(new CompletableFuture[0]));
}
```

### Custom World Loading
```java
// Custom World Generator mit Konfiguration
CustomWorldGenerator.WorldGenerationConfig genConfig = new CustomWorldGenerator.WorldGenerationConfig();
genConfig.setTerrainType(CustomWorldGenerator.TerrainType.AMPLIFIED);
genConfig.setBaseHeight(80);
genConfig.setHeightVariation(32);

CustomWorldGenerator generator = new CustomWorldGenerator(genConfig);
```

### Verfügbare Terrain-Typen
- **FLAT** - Flaches Terrain mit konfigurierbarer Höhe
- **NORMAL** - Standard-Minecraft-Terrain
- **AMPLIFIED** - Verstärktes Terrain mit höheren Bergen
- **VOID** - Leeres Terrain (nur Bedrock)
- **CUSTOM** - Benutzerdefiniertes Terrain

---

## 💻 Commands & Verwaltung

### Advanced Network Commands (`/advancednetwork`)
```bash
/advancednetwork status          # Zeigt detaillierten Netzwerk-Status
/advancednetwork servers         # Zeigt alle Server mit Metriken
/advancednetwork transfer <type> # Intelligente Server-Transfer
/advancednetwork sync <type>     # Manuelle Daten-Synchronisation
/advancednetwork monitor         # Echtzeit-Monitoring-Informationen
/advancednetwork alerts          # Zeigt aktive Alerts und Warnungen
/advancednetwork stats           # Detaillierte Statistiken und Analytics
/advancednetwork loadbalance     # Manuelles Load Balancing
/advancednetwork autoscale       # Auto-Scaling-Operationen
```

### Advanced World Commands (`/advancedworlds`)
```bash
/advancedworlds list                    # Zeigt alle Welten mit Metriken
/advancedworlds create <name> <type>    # Erstellt neue Welten
/advancedworlds load <path>             # Lädt eigene Welten
/advancedworlds unload <world>          # Entlädt Welten
/advancedworlds teleport <world>        # Teleportiert zu Welten
/advancedworlds metrics                 # Zeigt Performance-Metriken
/advancedworlds custom <name> <config>  # Erstellt Custom-Welten
/advancedworlds upload <file>           # Lädt Welt-Dateien hoch
/advancedworlds info <world>            # Zeigt detaillierte Welt-Info
/advancedworlds performance             # Zeigt Performance-Statistiken
```

### Standard Commands
```bash
# Network Commands
/network status          # Zeigt Netzwerk-Status
/network servers         # Zeigt alle Server
/network info           # Zeigt Netzwerk-Informationen

# Transfer Commands
/transfer hub           # Transfer zum Hub-Server
/transfer island        # Transfer zum Island-Server
/transfer dungeon       # Transfer zum Dungeon-Server

# Island Commands
/island create          # Erstellt eine Insel
/island visit <player>  # Besucht Spieler-Insel
/island members         # Zeigt Insel-Mitglieder
/island invite <player> # Lädt Spieler ein
/island kick <player>   # Entfernt Spieler

# World Commands
/worlds list            # Zeigt alle Welten
/worlds info <world>    # Zeigt Welt-Informationen
/worlds load <world>    # Lädt eine Welt
/worlds unload <world>  # Entlädt eine Welt
/worlds teleport <world> # Teleportiert zu einer Welt
```

---

## 📊 Monitoring & Analytics

### Echtzeit-Monitoring
```yaml
# Server-Metriken
- Player Count pro Server
- TPS (Ticks per Second)
- Memory Usage
- CPU Usage
- Server Load
- Server Health Score

# Welt-Metriken
- Geladene Chunks
- Aktive Spieler
- Performance-Trends
- Resource Usage
```

### Alert-System
```yaml
# Automatische Alerts bei:
- TPS < 15.0
- Memory Usage > 85%
- CPU Usage > 80%
- Player Count > Threshold
- Server Offline
- Transfer Failures
```

### Performance-Reports
- **Load Balancing Statistiken** mit Server-Verteilung
- **Transfer Statistiken** mit Success-Rate
- **Welt-Performance** mit historischen Daten
- **Resource Usage** mit Trend-Analyse

---

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
/advancednetwork status

# Server-Verbindung überprüfen
/advancednetwork servers

# Logs für Fehler überprüfen
tail -f servers/hub/logs/latest.log
```

#### Multithreading-Probleme
```bash
# Thread-Status überprüfen
/advancedworlds performance

# Aktive Operationen überprüfen
# Sollte normalerweise < 10 sein

# Memory-Usage überprüfen
# Sollte < 80% sein
```

### Debug-Modus aktivieren
```yaml
# config.yml
network:
  debug:
    enabled: true
    log-level: "DEBUG"
    thread-monitoring: true
    performance-tracking: true
```

---

## ⚙️ Erweiterte Konfiguration

### Multithreading-Konfiguration
```yaml
# config.yml
multithreading:
  enabled: true
  world-executor-threads: 4
  scheduler-threads: 2
  max-concurrent-operations: 10
  operation-timeout: 30000
```

### Load Balancing-Konfiguration
```yaml
# config.yml
load-balancing:
  enabled: true
  strategy: "resource-based"  # round-robin, least-connections, weighted-round-robin, least-response-time, resource-based
  health-threshold: 0.7
  load-threshold: 0.8
  auto-scaling: true
  max-servers-per-type: 10
```

### Custom World-Konfiguration
```yaml
# config.yml
custom-worlds:
  enabled: true
  max-custom-worlds: 50
  allowed-world-types:
    - "NORMAL"
    - "FLAT"
    - "NETHER"
    - "THE_END"
  upload-limit: 100MB
  auto-backup: true
```

### Performance-Optimierung
```yaml
# config.yml
performance:
  metrics-interval: 5000
  cleanup-interval: 300000
  cache-size: 1000
  async-operations: true
  chunk-preloading: true
```

---

## 🚀 JVM-Optimierung

### Optimierte JVM-Parameter
```bash
java -Xmx8G -Xms4G \
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

### Paper-Optimierung
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

---

## 🔒 Sicherheit

### Firewall-Konfiguration
```bash
# UFW-Firewall konfigurieren
ufw allow 25565:25593/tcp  # Alle Server-Ports
ufw allow ssh              # SSH-Zugang
ufw enable                 # Firewall aktivieren
```

### SSL/TLS-Konfiguration
```yaml
# config.yml
network:
  security:
    ssl:
      enabled: true
      keystore: "/path/to/keystore.jks"
      keystore-password: "your-keystore-password"
      truststore: "/path/to/truststore.jks"
      truststore-password: "your-truststore-password"
```

---

## 📈 Monitoring & Wartung

### Backup-System
```bash
# Automatische Backups
#!/bin/bash
BACKUP_DIR="/opt/minecraft/backups"
DATE=$(date +%Y%m%d_%H%M%S)

for server in hub island dungeon event auction bank minigame pvp creative survival hardcore adventure spectator test resource nether end skyblock bedwars skywars uhc kitpvp prison factions towny proxy lobby queue maintenance; do
    tar -czf $BACKUP_DIR/${server}_${DATE}.tar.gz \
        -C /opt/minecraft/servers $server
done

# Cron-Job für tägliche Backups
echo "0 2 * * * /opt/minecraft/backup.sh" | crontab -
```

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
}
EOF
```

---

## 🎯 Fazit

Das erweiterte Multi-Server-System bietet eine **vollständig multithreaded** und **enterprise-grade** Lösung für komplexe Minecraft-Server-Netzwerke mit:

### ✅ Implementierte Features
- **25+ Server-Typen** für verschiedene Spielmodi
- **Thread-sichere Architektur** mit asynchronen Operationen
- **Custom World Loading** für eigene Welten
- **Erweiterte Server-Kommunikation** mit Load Balancing
- **Echtzeit-Monitoring** und Performance-Analytics
- **Cross-Server Data Synchronization** in Echtzeit
- **Umfassende Admin-Befehle** für vollständige Verwaltung

### 🚀 Nächste Schritte
1. **Testen**: Starten Sie mit einem Hub- und Island-Server
2. **Erweitern**: Fügen Sie nach Bedarf weitere Server-Typen hinzu
3. **Optimieren**: Passen Sie die Multithreading-Konfiguration an
4. **Überwachen**: Implementieren Sie das Monitoring-System
5. **Warten**: Führen Sie regelmäßige Backups und Updates durch

### 📞 Support
- **GitHub Issues**: Für Bug-Reports und Feature-Requests
- **Discord**: Für Community-Support
- **Wiki**: Für detaillierte Dokumentation

**Viel Erfolg mit Ihrem erweiterten Multi-Server-System! 🎮✨**
