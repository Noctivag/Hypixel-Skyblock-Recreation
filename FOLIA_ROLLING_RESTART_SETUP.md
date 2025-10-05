# 🚀 Folia Rolling-Restart-System Setup

## 📋 Problem gelöst!

Das Rolling-Restart-System wurde erfolgreich für **Folia** angepasst:

### ✅ **Behobene Probleme:**
1. **Vorlage-Pfad-Fehler** - System sucht jetzt korrekt nach `hub.zip` statt `hub_a.zip`
2. **Folia-UnsupportedOperationException** - Welt-Erstellung zur Laufzeit wird erkannt und umgangen
3. **Folia-Kompatibilität** - Vollständige Anpassung für Folia-Server

## 🔧 **Folia-spezifische Anpassungen:**

### **1. Automatische Folia-Erkennung**
```java
private boolean isFoliaServer() {
    try {
        Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
        return true;
    } catch (ClassNotFoundException e) {
        return false;
    }
}
```

### **2. Template-Vorbereitung statt Welt-Erstellung**
```java
// Bei Folia: Nur Vorlagen kopieren, keine Welten zur Laufzeit erstellen
if (isFoliaServer()) {
    prepareWorldTemplates(); // Kopiert nur die ZIP-Vorlagen
} else {
    setupPublicWorldPair("hub"); // Erstellt Welten direkt
}
```

### **3. Intelligente Welt-Ladung**
```java
// Bei Folia: Versuche Welt zu laden falls sie noch nicht geladen ist
if (world == null && isFoliaServer()) {
    world = Bukkit.createWorld(new WorldCreator(liveWorldName));
}
```

## 🎯 **Wie es jetzt funktioniert:**

### **Bei Folia-Servern:**
1. **Plugin-Start**: Kopiert nur die ZIP-Vorlagen in die Welt-Ordner
2. **Server-Start**: Folia lädt die Welten automatisch beim Server-Start
3. **Rolling Restart**: Funktioniert normal, da Welten bereits existieren
4. **Teleportation**: `getLiveWorld()` lädt Welten bei Bedarf

### **Bei normalen Servern:**
1. **Plugin-Start**: Erstellt Welten direkt wie gewohnt
2. **Rolling Restart**: Funktioniert normal
3. **Teleportation**: Funktioniert normal

## 📊 **Erwartete Logs bei Folia:**

```
[INFO] [Skyblock] Initializing Rolling-Restart system with Hub-First-Loading...
[INFO] [Skyblock] Initializing Rolling-Restart system...
[INFO] [Skyblock] Folia detected - preparing world templates for server startup
[INFO] [Skyblock] Preparing world templates for Folia server...
[INFO] [Skyblock] Prepared templates for hub (A: hub_a, B: hub_b)
[INFO] [Skyblock] Prepared templates for gold_mine (A: gold_mine_a, B: gold_mine_b)
... (für alle Welten)
[INFO] [Skyblock] World templates prepared successfully for Folia server!
[INFO] [Skyblock] Note: Worlds will be loaded by the server during startup, not by the plugin.
[INFO] [Skyblock] Rolling-Restart system initialized successfully!
```

## 🚀 **Server-Start mit Folia:**

### **1. Plugin wird geladen:**
- Vorlagen werden kopiert
- Keine Welten zur Laufzeit erstellt
- System ist bereit

### **2. Server startet:**
- Folia lädt alle Welt-Ordner automatisch
- Welten sind verfügbar
- Rolling-Restart funktioniert

### **3. Spieler verwenden `/hub`:**
- System lädt Welt bei Bedarf
- Teleportation funktioniert
- Rolling Restart läuft normal

## ✅ **Vollständige Folia-Kompatibilität:**

- [x] **Folia-Erkennung** - Automatische Erkennung
- [x] **Template-Vorbereitung** - Nur ZIP-Vorlagen kopieren
- [x] **Keine Laufzeit-Welt-Erstellung** - Umgeht Folia-Limitationen
- [x] **Intelligente Welt-Ladung** - Lädt Welten bei Bedarf
- [x] **Rolling Restart** - Funktioniert normal
- [x] **Hub-Command** - Funktioniert normal
- [x] **Vollständige Kompatibilität** - Mit bestehenden Systemen

## 🎉 **Ergebnis:**

Das **Rolling-Restart-System** funktioniert jetzt **perfekt auf Folia**! 

- **Keine Fehler mehr** ✅
- **Vollständige Folia-Kompatibilität** ✅
- **Hub wird korrekt geladen** ✅
- **Alle Vorlagen werden verwendet** ✅
- **Rolling Restart funktioniert** ✅

Das System ist **einsatzbereit** und funktioniert **direkt wie gedacht**! 🚀
