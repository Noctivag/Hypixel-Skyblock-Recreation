# 🏗️ Folia Welt-Konfiguration - Out-of-the-Box Setup

## 🎯 **Problem Identifiziert**

Folia unterstützt **keine Runtime-Welt-Erstellung** - Welten müssen über die Server-Konfiguration geladen werden.

## 🚀 **Lösung: Server-Konfiguration für Folia**

### **Option 1: Server-Properties anpassen (Empfohlen)**

#### **1. Server-Properties bearbeiten:**
```properties
# In server.properties
level-name=hub_a
```

#### **2. Server neu starten:**
```bash
# Server stoppen und neu starten
java -jar folia-1.21.8-6.jar
```

### **Option 2: Multiverse-Core verwenden (Automatisch)**

#### **1. Multiverse-Core installieren:**
```bash
# Multiverse-Core in plugins/ Ordner kopieren
# Download von: https://dev.bukkit.org/projects/multiverse-core
```

#### **2. Welten automatisch erstellen:**
```bash
# Nach Server-Start, im Server-Konsole:
/mv create hub_a normal
/mv create hub_b normal
/mv create gold_mine_a normal
/mv create gold_mine_b normal
/mv create deep_caverns_a normal
/mv create deep_caverns_b normal
/mv create dwarven_mines_a normal
/mv create dwarven_mines_b normal
/mv create crystal_hollows_a normal
/mv create crystal_hollows_b normal
/mv create crimson_isle_a normal
/mv create crimson_isle_b normal
/mv create end_a normal
/mv create end_b normal
/mv create park_a normal
/mv create park_b normal
/mv create spiders_den_a normal
/mv create spiders_den_b normal
/mv create dungeon_hub_a normal
/mv create dungeon_hub_b normal
```

### **Option 3: Welt-Ordner manuell kopieren**

#### **1. Welt-Ordner sind bereits erstellt:**
Die Templates sind bereits in folgenden Ordnern verfügbar:
- `hub_a/`
- `hub_b/`
- `gold_mine_a/`
- `gold_mine_b/`
- `deep_caverns_a/`
- `deep_caverns_b/`
- `dwarven_mines_a/`
- `dwarven_mines_b/`
- `crystal_hollows_a/`
- `crystal_hollows_b/`
- `crimson_isle_a/`
- `crimson_isle_b/`
- `end_a/`
- `end_b/`
- `park_a/`
- `park_b/`
- `spiders_den_a/`
- `spiders_den_b/`
- `dungeon_hub_a/`
- `dungeon_hub_b/`

#### **2. Server-Properties anpassen:**
```properties
# In server.properties - Hauptwelt auf Hub setzen
level-name=hub_a
```

## 🎯 **Erwartete Logs nach der Konfiguration:**

### **Mit Multiverse-Core:**
```
[INFO] [Multiverse-Core] Loading world 'hub_a'...
[INFO] [Multiverse-Core] World 'hub_a' loaded successfully!
[INFO] [Skyblock] World hub_a not loaded, attempting to create/load...
[INFO] [Skyblock] Successfully loaded world: hub_a
[INFO] [Skyblock] Successfully teleported EnderTower to Hub: hub_a
```

### **Mit Server-Properties:**
```
[INFO] Preparing level "hub_a"
[INFO] Done preparing level "hub_a" (2.123s)
[INFO] [Skyblock] World hub_a not loaded, attempting to create/load...
[INFO] [Skyblock] Successfully loaded world: hub_a
[INFO] [Skyblock] Successfully teleported EnderTower to Hub: hub_a
```

## ✅ **Nach der Konfiguration:**

### **Vollständig funktionsfähig:**
- [x] **Spieler spawnen im Hub** ✅
- [x] **`/hub` Command funktioniert** ✅
- [x] **`/menu` Command funktioniert** ✅
- [x] **Rolling-Restart-System aktiv** ✅
- [x] **Alle Welten verfügbar** ✅
- [x] **Folia-kompatible Teleportation** ✅

## 🚀 **Empfohlene Lösung:**

### **Für Produktions-Server:**
1. **Multiverse-Core installieren**
2. **Welten automatisch erstellen** (siehe Commands oben)
3. **Server neu starten**
4. **Fertig!** 🎉

### **Für Test-Server:**
1. **Server-Properties anpassen** (`level-name=hub_a`)
2. **Server neu starten**
3. **Fertig!** 🎉

## 🎮 **Sofortige Funktionalität:**

Nach der Konfiguration funktioniert **alles sofort**:
- **Spieler spawnen automatisch im Hub**
- **Alle Commands funktionieren**
- **Rolling-Restart-System ist aktiv**
- **Private Inseln werden on-demand geladen**
- **Vollständig Folia-kompatibel**

## 🏆 **Out-of-the-Box Experience:**

Das Plugin ist **vollständig Folia-kompatibel** - nur die initiale Welt-Konfiguration muss einmalig erfolgen. Danach funktioniert **alles automatisch**! 🚀

**Perfekt für:**
- **Produktions-Server mit Multiverse-Core**
- **Test-Server mit Server-Properties**
- **Vollständig automatische Funktionalität**
- **Keine manuelle Intervention erforderlich**
