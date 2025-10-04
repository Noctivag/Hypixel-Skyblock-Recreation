# 🌐 Multi-Server Setup - Hypixel Skyblock Recreation

## 📋 **Übersicht**

Das Hypixel Skyblock Recreation Plugin unterstützt eine vollständige Multi-Server-Architektur im Hypixel-Stil.

## 🏗️ **Server-Architektur**

### **Server-Typen**
- **HUB Server** - Hauptserver für Lobby und Navigation
- **ISLAND Server** - Private Inseln für Spieler
- **DUNGEON Server** - Dungeon-Instanzen
- **EVENT Server** - Event-basierte Inseln
- **AUCTION Server** - Auktionshaus
- **BANK Server** - Bank-System
- **MINIGAME Server** - Minigame-Server
- **PVP Server** - PvP-Arena

### **Kommunikation**
- **Redis** für Echtzeit-Kommunikation
- **MySQL** für persistente Daten
- **WebSocket** für Player-Transfers
- **Message Queue** für asynchrone Operationen

## 🚀 **Setup-Anleitung**

### **1. Voraussetzungen**

#### **Server-Anforderungen**
- **Minimum**: 4GB RAM, 2 CPU Cores
- **Empfohlen**: 8GB RAM, 4 CPU Cores
- **Netzwerk**: Stabile Verbindung zwischen Servern

#### **Software-Anforderungen**
- **Java 21+**
- **Minecraft Server 1.21+**
- **Redis Server 6.0+**
- **MySQL 8.0+**

### **2. Installation**

#### **HUB Server Setup**
```bash
# Server-Dateien herunterladen
wget https://papermc.io/api/v2/projects/paper/versions/1.21/builds/latest/downloads/paper-1.21-latest.jar

# Server starten
java -Xmx4G -Xms4G -jar paper-1.21-latest.jar

# Plugin installieren
cp BasicsPlugin-1.0-SNAPSHOT.jar plugins/
```

#### **Konfiguration**
```yaml
# config.yml
network:
  type: HUB
  redis:
    host: localhost
    port: 6379
    password: ""
  mysql:
    host: localhost
    port: 3306
    database: skyblock_network
    username: root
    password: password
```

### **3. Redis Setup**

#### **Redis Installation**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install redis-server

# CentOS/RHEL
sudo yum install redis
sudo systemctl start redis
sudo systemctl enable redis
```

#### **Redis Konfiguration**
```conf
# /etc/redis/redis.conf
bind 0.0.0.0
port 6379
requirepass your_redis_password
maxmemory 2gb
maxmemory-policy allkeys-lru
```

### **4. MySQL Setup**

#### **MySQL Installation**
```bash
# Ubuntu/Debian
sudo apt install mysql-server

# CentOS/RHEL
sudo yum install mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld
```

#### **Datenbank erstellen**
```sql
CREATE DATABASE skyblock_network;
CREATE USER 'skyblock'@'%' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON skyblock_network.* TO 'skyblock'@'%';
FLUSH PRIVILEGES;
```

## 🔧 **Server-Konfiguration**

### **HUB Server**
```yaml
# server.properties
server-port=25565
max-players=100
online-mode=false
white-list=false

# plugins/BasicsPlugin/config.yml
network:
  type: HUB
  max-players: 100
  heartbeat-interval: 1000
```

### **ISLAND Server**
```yaml
# server.properties
server-port=25566
max-players=50
online-mode=false

# plugins/BasicsPlugin/config.yml
network:
  type: ISLAND
  max-islands: 50
  island-size: 100
```

### **DUNGEON Server**
```yaml
# server.properties
server-port=25567
max-players=20
online-mode=false

# plugins/BasicsPlugin/config.yml
network:
  type: DUNGEON
  max-instances: 10
  instance-timeout: 3600
```

## 🌐 **Netzwerk-Konfiguration**

### **Firewall-Einstellungen**
```bash
# UFW (Ubuntu)
sudo ufw allow 25565:25575/tcp
sudo ufw allow 6379/tcp
sudo ufw allow 3306/tcp

# iptables
iptables -A INPUT -p tcp --dport 25565:25575 -j ACCEPT
iptables -A INPUT -p tcp --dport 6379 -j ACCEPT
iptables -A INPUT -p tcp --dport 3306 -j ACCEPT
```

### **Load Balancer (Optional)**
```nginx
# nginx.conf
upstream minecraft_servers {
    server 127.0.0.1:25565 weight=1;
    server 127.0.0.1:25566 weight=1;
    server 127.0.0.1:25567 weight=1;
}

server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        proxy_pass http://minecraft_servers;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 📊 **Monitoring & Logging**

### **Performance Monitoring**
```yaml
# config.yml
monitoring:
  enabled: true
  metrics-interval: 30
  memory-threshold: 80
  cpu-threshold: 70
  
logging:
  level: INFO
  file: logs/network.log
  max-size: 100MB
  max-files: 10
```

### **Health Checks**
```bash
# Redis Health Check
redis-cli ping

# MySQL Health Check
mysql -u skyblock -p -e "SELECT 1"

# Server Health Check
curl http://localhost:25565/health
```

## 🔄 **Player Transfer**

### **Automatischer Transfer**
```yaml
# config.yml
player-transfer:
  enabled: true
  auto-balance: true
  transfer-delay: 1000
  backup-servers:
    - "island-2:25566"
    - "island-3:25567"
```

### **Manueller Transfer**
```bash
# Command: /transfer <player> <server>
/transfer PlayerName island-server
/transfer PlayerName dungeon-server
```

## 🛡️ **Sicherheit**

### **Server-Sicherheit**
```yaml
# config.yml
security:
  enabled: true
  encryption: true
  token-timeout: 300
  max-connections-per-ip: 5
```

### **Datenbank-Sicherheit**
```sql
-- Sichere Benutzer erstellen
CREATE USER 'skyblock_readonly'@'%' IDENTIFIED BY 'readonly_password';
GRANT SELECT ON skyblock_network.* TO 'skyblock_readonly'@'%';

-- Firewall-Regeln
iptables -A INPUT -s 192.168.1.0/24 -p tcp --dport 3306 -j ACCEPT
iptables -A INPUT -p tcp --dport 3306 -j DROP
```

## 📈 **Skalierung**

### **Horizontale Skalierung**
```yaml
# Auto-Scaling Konfiguration
auto-scaling:
  enabled: true
  min-servers: 2
  max-servers: 10
  scale-up-threshold: 80
  scale-down-threshold: 20
```

### **Vertikale Skalierung**
```bash
# Server-Ressourcen erhöhen
java -Xmx8G -Xms8G -XX:+UseG1GC -jar paper-1.21-latest.jar
```

## 🔧 **Troubleshooting**

### **Häufige Probleme**

#### **Verbindungsfehler**
```bash
# Problem: Server können nicht kommunizieren
# Lösung: Firewall und Netzwerk prüfen
telnet server-ip 6379
telnet server-ip 3306
```

#### **Datenbankfehler**
```bash
# Problem: MySQL-Verbindung fehlgeschlagen
# Lösung: Benutzerberechtigungen prüfen
mysql -u skyblock -p -e "SHOW GRANTS FOR 'skyblock'@'%'"
```

#### **Redis-Fehler**
```bash
# Problem: Redis-Verbindung fehlgeschlagen
# Lösung: Redis-Status prüfen
redis-cli ping
redis-cli info memory
```

### **Debug-Modus**
```yaml
# config.yml
debug:
  enabled: true
  network-debug: true
  database-debug: true
  transfer-debug: true
```

## 📋 **Deployment-Checkliste**

### **Vor dem Deployment**
- [ ] Alle Server konfiguriert
- [ ] Redis läuft und ist erreichbar
- [ ] MySQL läuft und ist erreichbar
- [ ] Firewall-Regeln konfiguriert
- [ ] Plugin auf allen Servern installiert

### **Nach dem Deployment**
- [ ] Server-Status überprüft
- [ ] Player-Transfer getestet
- [ ] Datenbank-Synchronisation überprüft
- [ ] Performance-Metriken überwacht
- [ ] Logs überprüft

## 🎯 **Best Practices**

### **Performance**
- **Connection Pooling** für Datenbankverbindungen
- **Redis-Clustering** für hohe Verfügbarkeit
- **Load Balancing** für gleichmäßige Verteilung
- **Monitoring** für proaktive Überwachung

### **Sicherheit**
- **VPN** für Server-zu-Server-Kommunikation
- **SSL/TLS** für verschlüsselte Verbindungen
- **Rate Limiting** für API-Aufrufe
- **Backup-Strategien** für Datenintegrität

### **Wartung**
- **Regelmäßige Updates** für alle Komponenten
- **Monitoring-Alerts** für kritische Ereignisse
- **Backup-Routinen** für alle Daten
- **Dokumentation** für alle Änderungen
