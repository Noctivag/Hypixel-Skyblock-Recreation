# Hypixel Skyblock Menu Design System

## Übersicht

Das neue Hypixel Menu Design System ermöglicht es, alle Menüs im Plugin im authentischen Hypixel Skyblock-Stil zu gestalten. Das System bietet konsistente Farben, Layouts und Styling-Elemente, die dem originalen Hypixel Skyblock nachempfunden sind.

## Hauptkomponenten

### 1. HypixelMenuStyleSystem

Die zentrale Klasse für alle Styling-Funktionen:

```java
HypixelMenuStyleSystem styleSystem = new HypixelMenuStyleSystem(plugin);
```

#### Farbkonstanten
- `GOLD` (§6) - Haupttitel und wichtige Elemente
- `YELLOW` (§e) - Sekundäre Titel und Highlights
- `GREEN` (§a) - Erfolg, aktivierte Features
- `RED` (§c) - Fehler, deaktivierte Features
- `BLUE` (§b) - Information, soziale Features
- `PURPLE` (§d) - Spezielle Features
- `GRAY` (§7) - Beschreibungen und Standardtext

#### Icons und Symbole
- `PLAYER_ICON` (👤) - Spieler-bezogene Features
- `MONEY_ICON` (💰) - Wirtschaftliche Features
- `LEVEL_ICON` (⭐) - Level und Fortschritt
- `STATS_ICON` (📊) - Statistiken
- `SETTINGS_ICON` (⚙️) - Einstellungen
- `CLOSE_ICON` (❌) - Schließen
- `BACK_ICON` (⬅️) - Zurück
- `SUCCESS_ICON` (✓) - Erfolg
- `ERROR_ICON` (✗) - Fehler

### 2. Menü-Layout

#### Standard-Layout
- **Größe**: 54 Slots (6 Reihen)
- **Border**: Graue Glaspaneele mit schwarzen Ecken
- **Header**: Spieler-Info in Slot 4
- **Kategorien**: Organisiert in Reihen
- **Navigation**: Footer mit Standard-Buttons

#### Border-System
```java
styleSystem.setupHypixelBorder(inventory);
```

#### Spieler-Header
```java
styleSystem.setupPlayerHeader(inventory, player, slot);
```

### 3. Item-Typen

#### Kategorie-Header
```java
setItem(slot, styleSystem.createCategoryHeader("KATEGORIE", "Beschreibung"));
```

#### Feature-Items
```java
setItem(slot, styleSystem.createFeatureItem(Material.DIAMOND_SWORD, "⚔️ PVP ARENA", 
    "Beschreibung des Features", true,
    "• Feature 1",
    "• Feature 2",
    "• Feature 3"));
```

#### Status-Items
```java
setItem(slot, styleSystem.createStatusItem(Material.EMERALD, "STATUS", 
    "Beschreibung", true)); // true = aktiv, false = inaktiv
```

#### Fortschritts-Items
```java
setItem(slot, styleSystem.createProgressItem(Material.EXPERIENCE_BOTTLE, "LEVEL", 
    currentLevel, maxLevel, "levels"));
```

#### Navigation-Items
```java
// Navigation Footer
styleSystem.setupNavigationFooter(inventory, startSlot, showBack, showNext);

// Einzelne Navigation-Items
setItem(slot, styleSystem.createNavigationItem(NavigationType.CLOSE));
setItem(slot, styleSystem.createNavigationItem(NavigationType.BACK));
setItem(slot, styleSystem.createNavigationItem(NavigationType.REFRESH));
setItem(slot, styleSystem.createNavigationItem(NavigationType.HELP));
```

## Implementierung

### 1. Basis-Menü erstellen

```java
public class MeinHypixelMenu extends CustomGUI {
    private final HypixelMenuStyleSystem styleSystem;
    
    public MeinHypixelMenu(Plugin plugin, Player player) {
        super(54, Component.text("§6§l⚡ MEIN MENÜ ⚡"));
        this.styleSystem = new HypixelMenuStyleSystem(plugin);
        setupItems();
    }
    
    private void setupItems() {
        // Border setup
        styleSystem.setupHypixelBorder(getInventory());
        
        // Spieler-Header
        styleSystem.setupPlayerHeader(getInventory(), player, 4);
        
        // Kategorien
        setupKategorie1();
        setupKategorie2();
        
        // Navigation
        styleSystem.setupNavigationFooter(getInventory(), 50, false, false);
    }
}
```

### 2. Kategorien organisieren

```java
private void setupKategorie1() {
    // Kategorie-Header
    setItem(10, styleSystem.createCategoryHeader("KATEGORIE 1", "Beschreibung"));
    
    // Features
    setItem(11, styleSystem.createFeatureItem(Material.ITEM, "🎯 FEATURE 1", 
        "Beschreibung", true, "• Detail 1", "• Detail 2"));
    
    setItem(12, styleSystem.createFeatureItem(Material.ITEM, "🎯 FEATURE 2", 
        "Beschreibung", false, "• Detail 1", "• Detail 2"));
}
```

### 3. Click-Handling

```java
public void handleClick(int slot) {
    switch (slot) {
        case 11 -> {
            player.sendMessage(styleSystem.GREEN + "Feature 1 geöffnet!");
            // Feature 1 öffnen
        }
        case 12 -> {
            player.sendMessage(styleSystem.RED + "Feature 2 ist deaktiviert!");
        }
        case 50 -> player.closeInventory(); // Close
        case 51 -> setupItems(); // Refresh
    }
}
```

## Aktualisierte Menüs

### UltimateMainMenu
- Vollständig auf Hypixel-Stil umgestellt
- Kategorisierte Features
- Konsistente Farbgebung
- Verbesserte Navigation

### IntegratedMenuSystem
- Hypixel-Styling implementiert
- System-Status-Indikatoren
- Kategorisierte Systeme
- Verbesserte Benutzerfreundlichkeit

## Best Practices

### 1. Konsistente Farbverwendung
- Verwende die vordefinierten Farbkonstanten
- Gold für Haupttitel
- Grün für aktive Features
- Rot für deaktivierte Features
- Grau für Beschreibungen

### 2. Kategorisierung
- Organisiere Features in logische Kategorien
- Verwende Kategorie-Header für bessere Übersicht
- Gruppiere ähnliche Features zusammen

### 3. Navigation
- Immer Navigation-Footer verwenden
- Back-Button für hierarchische Menüs
- Refresh-Button für dynamische Inhalte
- Help-Button für Benutzerführung

### 4. Icons und Emojis
- Verwende konsistente Icons für ähnliche Features
- Emojis für bessere visuelle Erkennbarkeit
- Vermeide zu viele verschiedene Icons

### 5. Beschreibungen
- Kurze, prägnante Beschreibungen
- Bullet Points für Features
- Klare Aktionsaufforderungen

## Migration bestehender Menüs

### Schritt 1: Imports hinzufügen
```java
import de.noctivag.plugin.gui.HypixelMenuStyleSystem;
```

### Schritt 2: StyleSystem initialisieren
```java
private final HypixelMenuStyleSystem styleSystem;

public MeinMenu(Plugin plugin, Player player) {
    // ... existing code ...
    this.styleSystem = new HypixelMenuStyleSystem(plugin);
}
```

### Schritt 3: Border hinzufügen
```java
private void setupItems() {
    styleSystem.setupHypixelBorder(getInventory());
    // ... existing code ...
}
```

### Schritt 4: Items konvertieren
```java
// Alt
setItem(slot, Material.DIAMOND_SWORD, "§c§lPVP ARENA", "§7Beschreibung");

// Neu
setItem(slot, styleSystem.createFeatureItem(Material.DIAMOND_SWORD, "⚔️ PVP ARENA", 
    "Beschreibung", true, "• Feature 1", "• Feature 2"));
```

### Schritt 5: Navigation hinzufügen
```java
// Am Ende von setupItems()
styleSystem.setupNavigationFooter(getInventory(), 50, false, false);
```

## Beispiele

### Vollständiges Beispiel
Siehe `ExampleHypixelMenu.java` für ein vollständiges Beispiel der neuen Menü-Implementierung.

### Farbkombinationen
```java
// Haupttitel
styleSystem.GOLD + styleSystem.BOLD + "⚡ MENÜ ⚡"

// Feature-Titel (aktiv)
styleSystem.GREEN + styleSystem.BOLD + "✓ FEATURE"

// Feature-Titel (inaktiv)
styleSystem.GRAY + styleSystem.BOLD + "✗ FEATURE"

// Beschreibung
styleSystem.GRAY + "Beschreibungstext"

// Highlight
styleSystem.YELLOW + "Wichtige Information"
```

## Troubleshooting

### Häufige Probleme

1. **Border nicht sichtbar**
   - Stelle sicher, dass `setupHypixelBorder()` vor anderen Items aufgerufen wird

2. **Farben nicht korrekt**
   - Verwende die vordefinierten Konstanten aus `HypixelMenuStyleSystem`

3. **Navigation funktioniert nicht**
   - Implementiere `handleClick()` Methode
   - Verwende die korrekten Slot-Nummern

4. **Items überschreiben sich**
   - Plane das Layout vorher
   - Verwende Slot-Kalkulationen

### Debug-Tipps

1. Verwende `player.sendMessage()` für Click-Debugging
2. Teste mit verschiedenen Inventar-Größen
3. Überprüfe Slot-Zuordnungen
4. Verwende konsistente Namenskonventionen

## Fazit

Das neue Hypixel Menu Design System bietet:
- Konsistente, professionelle Optik
- Einfache Implementierung
- Wartbare Code-Struktur
- Erweiterbare Funktionalität
- Authentischen Hypixel Skyblock-Look

Alle bestehenden Menüs sollten schrittweise auf das neue System migriert werden, um eine einheitliche Benutzererfahrung zu gewährleisten.
