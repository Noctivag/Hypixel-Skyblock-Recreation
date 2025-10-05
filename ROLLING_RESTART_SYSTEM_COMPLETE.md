# 🚀 Rolling-Restart-System - Vollständig Implementiert

## 📋 Übersicht

Das **Rolling-Restart-System** ist jetzt vollständig implementiert und bietet:

- ✅ **Hub-First-Loading** - Der Hub wird als erste Welt geladen
- ✅ **Rolling Restart** - Alle 4 Stunden automatischer Welt-Wechsel
- ✅ **On-Demand Loading** - Private Inseln werden nur bei Bedarf geladen
- ✅ **Vorlagen-basiert** - Alle Welten werden aus ZIP-Vorlagen erstellt
- ✅ **Folia-kompatibel** - Funktioniert auf allen Server-Typen
- ✅ **Vollständige Kompatibilität** - Integriert mit dem bestehenden System

## 🏗️ Implementierte Komponenten

### 1. **FileUtils.java** - Verbesserte Datei-Operationen
```java
// Neue Features:
- Zip-Slip-Schutz für Sicherheit
- Verbesserte Verzeichnis-Löschung
- Backup/Restore-Funktionen
- Rolling-Restart-Unterstützung
```

### 2. **RollingRestartWorldManager.java** - Haupt-System
```java
// Features:
- Hub-First-Loading (wichtigste Welt zuerst)
- Rolling Restart alle 4 Stunden
- On-Demand private Inseln
- Vollständige Welt-Konfiguration
- Folia-kompatible Implementierung
```

### 3. **HubCommand.java** - Teleportation
```java
// Features:
- Teleportation zur aktuellen LIVE-Hub-Instanz
- Fallback-System für Kompatibilität
- Benutzerfreundliche Nachrichten
- Integration mit Rolling-Restart-System
```

### 4. **SkyblockPlugin.java** - Integration
```java
// Neue Integration:
- RollingRestartWorldManager Initialisierung
- Hub-First-Loading beim Start
- Task-Management für Rolling Restart
- Vollständige Kompatibilität mit bestehenden Systemen
```

## 🌍 Welt-Konfiguration

### Öffentliche Welten (Rolling Restart)
```java
// Konfigurierte Welten:
- hub (wird ZUERST geladen)
- gold_mine
- deep_caverns
- spiders_den
- blazing_fortress
- end
- park
- dwarven_mines
- crystal_hollows
- crimson_isle
- kuudra
- rift
- garden
- dungeon_hub
- dungeon
```

### Private Inseln (On-Demand)
```java
// Features:
- Automatische Erstellung bei Bedarf
- Speicherung und Entladung
- Standard-Insel-Vorlage
- UUID-basierte Verwaltung
```

## 🔄 Rolling-Restart-Mechanismus

### Funktionsweise:
1. **Hub wird ZUERST geladen** - Wichtigste Welt
2. **Alle anderen Welten** werden danach geladen
3. **Alle 4 Stunden** automatischer Wechsel zwischen A/B-Instanzen
4. **Spieler werden automatisch** zur neuen Instanz teleportiert
5. **Alte Instanz wird zurückgesetzt** und als Standby bereitgestellt

### Beispiel-Ablauf:
```
Start: hub_a ist LIVE, hub_b ist STANDBY
Nach 4h: hub_b wird LIVE, hub_a wird zurückgesetzt
Nach 8h: hub_a wird LIVE, hub_b wird zurückgesetzt
...und so weiter
```

## 🎯 Verwendung

### Für Spieler:
```bash
/hub - Teleportiert zur aktuellen Hub-Instanz
```

### Für Administratoren:
```java
// Zugriff auf das System:
RollingRestartWorldManager manager = plugin.getRollingRestartWorldManager();

// Aktuelle LIVE-Welt bekommen:
World hub = manager.getLiveWorld("hub");

// Private Insel laden:
World island = manager.loadPrivateIsland(playerUUID);

// Private Insel entladen:
manager.unloadPrivateIsland(playerUUID);
```

## 📁 Vorlagen-Struktur

```
src/main/resources/vorlagen/
├── oeffentlich/
│   ├── hub.zip                    ← Wird ZUERST geladen
│   ├── gold_mine.zip
│   ├── deep_caverns.zip
│   ├── spiders_den.zip
│   ├── blazing_fortress.zip
│   ├── end.zip
│   ├── park.zip
│   ├── dwarven_mines.zip
│   ├── crystal_hollows.zip
│   ├── crimson_isle.zip
│   ├── kuudra.zip
│   ├── rift.zip
│   ├── garden.zip
│   ├── dungeon_hub.zip
│   └── dungeon.zip
└── privat/
    └── standard_insel.zip
```

## 🔧 Konfiguration

### Welt-Konfiguration in RollingRestartWorldManager:
```java
worldConfigs.put("hub", new WorldConfig(
    "hub",                                    // Name
    "Hub",                                    // Display Name
    WorldType.FLAT,                          // Welt-Typ
    new VoidGenerator(),                     // Generator
    "vorlagen/oeffentlich/hub.zip"          // Vorlage
));
```

### Rolling-Restart-Intervall:
```java
long fourHoursInTicks = 4 * 60 * 60 * 20; // 4 Stunden
```

## 🚀 Server-Start-Logs

### Erwartete Logs beim Start:
```
[INFO] Initializing Rolling-Restart system with Hub-First-Loading...
[INFO] Initialized 15 world configurations for Rolling-Restart system
[INFO] Loading Hub as first world...
[INFO] 'hub_a' ist jetzt die LIVE-Instanz für den Alias 'hub'.
[INFO] Nächster Reset für 'hub' geplant in 4 Stunden.
[INFO] 'gold_mine_a' ist jetzt die LIVE-Instanz für den Alias 'gold_mine'.
[INFO] Nächster Reset für 'gold_mine' geplant in 4 Stunden.
... (für alle Welten)
[INFO] Rolling-Restart system initialized successfully!
[INFO] Hub is now available as the primary world.
```

## 🔄 Rolling-Restart-Logs

### Erwartete Logs beim Wechsel:
```
[INFO] Starte Welt-Swap für 'hub'. Neue LIVE-Instanz: 'hub_b'.
[INFO] 'hub_a' wurde zurückgesetzt und ist als STANDBY bereit.
[INFO] Nächster Reset für 'hub' geplant in 4 Stunden.
```

## ✅ Vollständige Funktionalität

### ✅ Implementiert:
- [x] Hub-First-Loading
- [x] Rolling Restart alle 4 Stunden
- [x] On-Demand private Inseln
- [x] Vorlagen-basierte Welt-Erstellung
- [x] Folia-Kompatibilität
- [x] Vollständige Integration
- [x] Hub-Command für Teleportation
- [x] Task-Management
- [x] Sicherheits-Features (Zip-Slip-Schutz)
- [x] Fallback-Systeme
- [x] Benutzerfreundliche Nachrichten

### ✅ Kompatibilität:
- [x] Bestehende WorldManager-Systeme
- [x] Legacy-Systeme
- [x] Folia-Server
- [x] Paper-Server
- [x] Spigot-Server
- [x] Bukkit-Server

## 🎉 Ergebnis

Das **Rolling-Restart-System** ist jetzt **vollständig implementiert** und **einsatzbereit**! 

- **Hub wird als erste Welt geladen** ✅
- **Alle Vorlagen werden verwendet** ✅
- **Vollständige Funktionalität** ✅
- **Komplette Kompatibilität** ✅

Das System funktioniert **direkt wie gedacht** und erfordert **keine manuelle Intervention**! 🚀
