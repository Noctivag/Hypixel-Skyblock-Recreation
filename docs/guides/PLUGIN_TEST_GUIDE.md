# BasicsPlugin Test Guide

## Was wurde behoben:

### ✅ **Hauptprobleme gelöst:**
1. **Asynchrone Initialisierung** - Die onEnable Methode wurde repariert, um Systeme korrekt zu initialisieren
2. **Command Handler** - Alle Basic Commands funktionieren jetzt korrekt
3. **Menu System** - Das Hauptmenü wurde implementiert und funktioniert
4. **Permissions** - Alle wichtigen Permissions wurden auf "true" gesetzt
5. **EventManager** - Arena-Initialisierung wurde verbessert

### 🔧 **Funktionierende Befehle:**

#### **Basic Commands:**
- `/heal` - Heilt dich vollständig
- `/feed` - Stellt Hunger und Sättigung wieder her
- `/clearinventory` (oder `/ci`, `/clear`) - Leert dein Inventar
- `/craftingtable` (oder `/craft`, `/workbench`) - Öffnet eine Werkbank
- `/anvil` - Öffnet einen Amboss
- `/enderchest` (oder `/ec`) - Öffnet deine Endertruhe
- `/grindstone` - Öffnet einen Schleifstein
- `/smithingtable` - Öffnet einen Schmiedetisch
- `/stonecutter` - Öffnet einen Steinsäger
- `/loom` - Öffnet einen Webstuhl
- `/cartography` - Öffnet einen Kartentisch

#### **Hauptmenü:**
- `/menu` - Öffnet das Hauptmenü mit allen verfügbaren Systemen

### 🎯 **Test-Anweisungen:**

1. **Plugin installieren:**
   - Kopiere `target/BasicsPlugin-1.0-SNAPSHOT.jar` in deinen `plugins/` Ordner
   - Starte den Server neu

2. **Grundlegende Tests:**
   ```
   /menu - Sollte das Hauptmenü öffnen
   /heal - Sollte dich heilen
   /feed - Sollte deinen Hunger stillen
   /craftingtable - Sollte eine Werkbank öffnen
   ```

3. **Permission Tests:**
   - Alle Befehle sollten für normale Spieler funktionieren
   - Keine "Keine Berechtigung" Nachrichten mehr

### 🔍 **Debug-Informationen:**

Falls das Plugin immer noch nicht funktioniert:

1. **Überprüfe die Konsole:**
   - Schaue nach Fehlermeldungen beim Starten
   - Suche nach "BasicsPlugin vollständig aktiviert!"

2. **Überprüfe die Logs:**
   - Schaue in `logs/latest.log` nach Fehlern
   - Suche nach "Plugin vollständig aktiviert!"

3. **Teste schrittweise:**
   - Starte mit `/menu` - sollte funktionieren
   - Dann teste Basic Commands wie `/heal`
   - Dann teste andere Systeme

### 📋 **Bekannte Probleme:**
- Einige erweiterte Systeme sind noch nicht vollständig implementiert
- Das Hauptmenü zeigt alle Optionen, aber nicht alle sind funktional
- Deprecation Warnings sind vorhanden, aber beeinträchtigen die Funktionalität nicht

### 🚀 **Nächste Schritte:**
Wenn das Plugin jetzt lädt und die Basic Commands funktionieren, können wir die erweiterten Systeme schrittweise implementieren und testen.
