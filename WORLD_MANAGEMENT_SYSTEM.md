# World Management System - Verbesserte Implementierung

## Übersicht

Das verbesserte World Management System implementiert ein vollständiges Rolling-Restart-System für öffentliche Welten und On-Demand-Loading für private Inseln. Dies ermöglicht eine nahtlose Spielerfahrung ohne Unterbrechungen.

## Features

### 🔄 Rolling-Restart-System für öffentliche Welten
- **Automatische Welten-Swaps**: Alle 4 Stunden werden öffentliche Welten automatisch zurückgesetzt
- **Nahtlose Übergänge**: Spieler werden automatisch zur neuen Instanz teleportiert
- **Keine Downtime**: Immer eine Live-Instanz verfügbar während die andere zurückgesetzt wird
- **Unterstützte Welten**: Hub, Gold Mine, Deep Caverns, Spider's Den, Blazing Fortress, End, Park, Dwarven Mines, Crystal Hollows, Nether, Crimson Isle, Kuudra, Rift, Garden, Dungeon Hub, Catacombs, Master Mode, alle Dungeons (F1-F7, M1-M7)

### 🏝️ On-Demand-Loading für private Inseln
- **Lazy Loading**: Inseln werden nur geladen, wenn Spieler sie betreten
- **Automatisches Entladen**: Inseln werden entladen, wenn keine Spieler mehr anwesend sind
- **Speicher-Optimierung**: Reduziert RAM-Verbrauch durch intelligentes Laden/Entladen
- **Persistente Speicherung**: Inseln werden automatisch gespeichert beim Entladen

## Implementierte Klassen

### 1. `FileUtils.java`
- **Zweck**: Hilfsklasse für Dateioperationen
- **Features**:
  - Sichere ZIP-Entpackung mit Zip-Slip-Schutz
  - Rekursive Verzeichnislöschung
  - Fehlerbehandlung für I/O-Operationen

### 2. `EmptyWorldGenerator.java`
- **Zweck**: Custom World Generator für leere Welten
- **Features**:
  - Generiert leere Chunks ohne Standard-Terrain
  - Optimiert für Vorlagen-basierte Welten
  - Kompatibel mit modernen Bukkit-Versionen

### 3. `WorldManager.java`
- **Zweck**: Hauptklasse für World Management
- **Features**:
  - Rolling-Restart-Mechanismus
  - On-Demand-Loading für private Inseln
  - Asynchrone Welt-Operationen
  - Thread-sichere Implementierung
  - Automatische Task-Verwaltung

### 4. `HypixelSkyblockRecreation.java`
- **Zweck**: Hauptklasse des Plugins
- **Features**:
  - Initialisierung aller öffentlichen Welten
  - Command-Registrierung
  - Event-Listener-Registrierung
  - Graceful Shutdown

### 5. Command-Klassen
- **`HubCommand.java`**: Teleportation zum Hub mit Live-Instanz-Abfrage
- **`IslandCommand.java`**: Verwaltung privater Inseln mit On-Demand-Loading

## Verwendung

### Öffentliche Welten
```java
// Hole die aktuelle Live-Instanz einer Welt
World hub = plugin.getWorldManager().getLiveWorld("hub");
if (hub != null) {
    player.teleport(hub.getSpawnLocation());
}
```

### Private Inseln
```java
// Lade eine private Insel on-demand
World island = plugin.getWorldManager().loadPrivateIsland(playerUUID);

// Entlade eine private Insel
plugin.getWorldManager().unloadPrivateIsland(playerUUID);
```

## Commands

### `/hub`
- Teleportiert zum Hub
- Nutzt automatisch die aktuelle Live-Instanz
- Keine Downtime durch Rolling-Restart

### `/island [subcommand]`
- `home` / `go`: Teleportiert zur eigenen Insel (lädt sie on-demand)
- `unload`: Entlädt die eigene Insel
- `reload`: Lädt die Insel neu

## Konfiguration

### Vorlagen-Struktur
```
src/main/resources/
└── vorlagen/
    ├── oeffentlich/
    │   ├── hub.zip
    │   ├── gold_mine.zip
    │   └── ...
    └── privat/
        └── standard_insel.zip
```

### Welt-Struktur
```
server/
├── hub_a/          # Live-Instanz
├── hub_b/          # Standby-Instanz
├── gold_mine_a/    # Live-Instanz
├── gold_mine_b/    # Standby-Instanz
└── private_islands/
    ├── player-uuid-1/
    ├── player-uuid-2/
    └── ...
```

## Technische Details

### Rolling-Restart-Mechanismus
1. **Initialisierung**: Beide Instanzen (A und B) werden geladen
2. **Live-Instanz**: Eine Instanz ist als "live" markiert
3. **Swap-Zyklus**: Alle 4 Stunden wird zur anderen Instanz gewechselt
4. **Spieler-Transfer**: Alle Spieler werden zur neuen Live-Instanz teleportiert
5. **Reset**: Die alte Instanz wird asynchron zurückgesetzt
6. **Standby**: Die zurückgesetzte Instanz wird als Standby bereitgehalten

### On-Demand-Loading
1. **Anfrage**: Spieler betritt Insel
2. **Prüfung**: Existiert die Insel bereits?
3. **Erstellung**: Falls nicht, wird sie aus Vorlage erstellt
4. **Laden**: Welt wird mit EmptyWorldGenerator geladen
5. **Teleportation**: Spieler wird zur Insel teleportiert
6. **Überwachung**: System überwacht Spieler-Anwesenheit
7. **Entladen**: Bei leerer Insel wird sie gespeichert und entladen

## Vorteile

### Performance
- **Reduzierter RAM-Verbrauch**: Nur aktive Welten sind geladen
- **Optimierte Server-Ressourcen**: Intelligente Verteilung der Last
- **Skalierbarkeit**: System wächst mit der Spielerzahl

### Benutzerfreundlichkeit
- **Keine Downtime**: Nahtlose Übergänge zwischen Instanzen
- **Schnelle Ladezeiten**: On-Demand-Loading reduziert Wartezeiten
- **Automatische Verwaltung**: Keine manuellen Eingriffe erforderlich

### Wartbarkeit
- **Modulare Architektur**: Klare Trennung der Verantwortlichkeiten
- **Thread-Sicherheit**: Sichere asynchrone Operationen
- **Fehlerbehandlung**: Robuste Behandlung von Ausnahmefällen

## Sicherheit

- **Zip-Slip-Schutz**: Verhindert Directory-Traversal-Angriffe
- **Thread-Sicherheit**: ConcurrentHashMap für sichere Zugriffe
- **Validierung**: Überprüfung aller Eingaben und Zustände
- **Graceful Shutdown**: Sauberes Beenden aller Tasks

## Kompatibilität

- **Bukkit 1.21+**: Vollständige Kompatibilität mit modernen Versionen
- **Folia**: Unterstützt Folia-Server
- **Adventure API**: Moderne Text-Formatierung
- **Cross-Platform**: Funktioniert auf allen unterstützten Plattformen

Das System ist vollständig implementiert und einsatzbereit für Produktionsumgebungen.
