# 🌍 Folia-Kompatible Welt-Erstellung

## ✅ **Was wurde implementiert:**

### **1. FoliaWorldCreator**
- **Automatische Welt-Erstellung** vor dem Server-Start
- **Vorlagen-basierte Welten** aus ZIP-Dateien
- **Folia-kompatible Generatoren** für alle Welt-Typen
- **Intelligente Fallback-Mechanismen**

### **2. Welt-Generatoren**
- **VoidGenerator** - Leere Welten (Hub)
- **IslandGenerator** - Skyblock-Inseln
- **DungeonGenerator** - Dungeon-Welten
- **ArenaGenerator** - PvP/Event-Arenen
- **MinigameGenerator** - Minigame-Welten

### **3. Automatische Konfiguration**
- **Game Rules** werden automatisch gesetzt
- **Spawn-Punkte** werden konfiguriert
- **Welt-spezifische Einstellungen** werden angewendet

## 🚀 **Verwendung:**

### **✅ Automatische Erstellung (Standard)**
Das Plugin erstellt die Welten **automatisch** beim Server-Start:

```bash
# Starte den Server - Welten werden automatisch erstellt
java -Xms8G -Xmx20G -jar folia-1.21.8-6.jar --nogui
```

**Das war's!** Keine manuellen Schritte erforderlich. Das Plugin:
1. **Erkennt** beim Start alle benötigten Welten
2. **Erstellt** sie automatisch mit den richtigen Generatoren
3. **Konfiguriert** sie mit den optimalen Einstellungen
4. **Lädt** sie in den WorldManager

### **Option 2: Manuelle Erstellung mit Multiverse-Core (Fallback)**
Falls die automatische Erstellung nicht funktioniert:

```bash
# Nach dem Server-Start:
/mv create skyblock_hub normal -g de.noctivag.skyblock.worlds.generators.VoidGenerator
/mv create skyblock_private normal -g de.noctivag.skyblock.worlds.generators.IslandGenerator
/mv create skyblock_public normal -g de.noctivag.skyblock.worlds.generators.IslandGenerator
/mv create skyblock_dungeons normal -g de.noctivag.skyblock.worlds.generators.DungeonGenerator
/mv create event_arenas flat -g de.noctivag.skyblock.worlds.generators.ArenaGenerator
/mv create pvp_arenas flat -g de.noctivag.skyblock.worlds.generators.ArenaGenerator
/mv create minigame_worlds normal -g de.noctivag.skyblock.worlds.generators.MinigameGenerator
```

## 📁 **Welt-Vorlagen (Optional):**

Falls du eigene Welt-Vorlagen verwenden möchtest, lege sie hier ab:

```
src/main/resources/vorlagen/
├── hub/
│   └── hub_template.zip
├── islands/
│   ├── private_template.zip
│   └── public_template.zip
├── dungeons/
│   └── dungeon_template.zip
├── arenas/
│   ├── event_template.zip
│   └── pvp_template.zip
└── minigames/
    └── minigame_template.zip
```

## 🔧 **Konfiguration:**

### **Welt-Einstellungen:**
- **Hub**: Kein Tageszyklus, keine Mobs, Mittag
- **Inseln**: Normaler Tageszyklus, Mobs aktiviert
- **Dungeons**: Kein Tageszyklus, Nacht, Mobs aktiviert
- **Arenen**: Kein Tageszyklus, keine Mobs, Mittag
- **Minigames**: Normaler Tageszyklus, Mobs aktiviert

### **Spawn-Punkte:**
- Alle Welten spawnen bei `(0, 100, 0)`
- Automatische Sicherheitsprüfung für Spawn-Punkte

## 🎯 **Vorteile der neuen Implementierung:**

### **✅ Folia-Kompatibilität:**
- **Keine Bukkit.createWorld()** zur Laufzeit
- **Virtual Threads** für alle Operationen
- **Asynchrone Welt-Erstellung**

### **✅ Performance:**
- **On-Demand Loading** für private Inseln
- **Intelligente Caching** von Welt-Referenzen
- **Optimierte Generatoren**

### **✅ Flexibilität:**
- **Vorlagen-basierte Welten** aus ZIP-Dateien
- **Konfigurierbare Generatoren**
- **Rolling-Restart-System** für öffentliche Welten

## 🚨 **Wichtige Hinweise:**

### **Folia-Limitationen:**
- **Keine Welt-Erstellung zur Laufzeit** (nur beim Server-Start)
- **Welten müssen vor dem Start existieren** oder automatisch erstellt werden
- **Multiverse-Core** kann als Alternative verwendet werden

### **Server-Hardware Empfehlungen:**
- **RAM**: 16-32GB (8GB Minimum)
- **CPU**: 8+ Kerne (4+ Minimum)
- **SSD**: Für bessere I/O-Performance
- **Java**: Java 21+ (für Virtual Threads)

## 📊 **Erwartete Logs:**

```
[INFO] [Skyblock] Initializing Skyblock worlds...
[INFO] [Skyblock] Initializing all worlds...
[INFO] [Skyblock] Initialized 7 world templates
[INFO] [Skyblock] Creating or loading all worlds...
[INFO] [Skyblock] Creating world: skyblock_hub with generator: VoidGenerator
[INFO] [Skyblock] Successfully created world: skyblock_hub
[INFO] [Skyblock] Creating world: skyblock_private with generator: IslandGenerator
[INFO] [Skyblock] Successfully created world: skyblock_private
[INFO] [Skyblock] Creating world: skyblock_public with generator: IslandGenerator
[INFO] [Skyblock] Successfully created world: skyblock_public
[INFO] [Skyblock] Creating world: skyblock_dungeons with generator: DungeonGenerator
[INFO] [Skyblock] Successfully created world: skyblock_dungeons
[INFO] [Skyblock] Creating world: event_arenas with generator: ArenaGenerator
[INFO] [Skyblock] Successfully created world: event_arenas
[INFO] [Skyblock] Creating world: pvp_arenas with generator: ArenaGenerator
[INFO] [Skyblock] Successfully created world: pvp_arenas
[INFO] [Skyblock] Creating world: minigame_worlds with generator: MinigameGenerator
[INFO] [Skyblock] Successfully created world: minigame_worlds
[INFO] [Skyblock] World creation/loading completed: 7/7 worlds ready
[INFO] [Skyblock] Successfully loaded world: skyblock_hub
[INFO] [Skyblock] Successfully loaded world: skyblock_private
[INFO] [Skyblock] Successfully loaded world: skyblock_public
[INFO] [Skyblock] Successfully loaded world: skyblock_dungeons
[INFO] [Skyblock] Successfully loaded world: event_arenas
[INFO] [Skyblock] Successfully loaded world: pvp_arenas
[INFO] [Skyblock] Successfully loaded world: minigame_worlds
[INFO] [Skyblock] World initialization completed: 7/7 worlds ready
[INFO] [Skyblock] Skyblock world initialization completed
```

## 🎉 **Fazit:**

Dein Plugin ist jetzt **vollständig Folia-kompatibel** und erstellt alle Welten **automatisch** beim Server-Start. Die neue Implementierung ist:

- ✅ **Folia-kompatibel**
- ✅ **Performance-optimiert**
- ✅ **Einfach zu verwenden**
- ✅ **Flexibel konfigurierbar**

**Starte einfach den Server und die Welten werden automatisch erstellt!** 🚀
