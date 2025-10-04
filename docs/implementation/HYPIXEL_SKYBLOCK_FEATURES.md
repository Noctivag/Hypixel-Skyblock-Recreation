# Hypixel Skyblock Features - Erweiterte Plugin-Funktionen

## Übersicht
Das Plugin wurde umfassend erweitert und enthält jetzt viele Features aus Hypixel Skyblock mit einer Multi-Server-kompatiblen Datenbank.

## 🗄️ Multi-Server Datenbank
- **MultiServerDatabaseManager**: Zentrale Datenbank-Verwaltung für mehrere Server
- **MySQL/MariaDB Support**: Professionelle Datenbank-Unterstützung
- **Connection Pooling**: HikariCP für optimale Performance
- **Auto-Reconnection**: Automatische Wiederverbindung bei Verbindungsabbrüchen
- **Data Synchronization**: Synchronisation zwischen Servern

### Datenbank-Tabellen:
- `server_info` - Server-Status und Informationen
- `player_profiles` - Spielerprofile mit Coins, Level, XP
- `skyblock_islands` - Insel-Verwaltung
- `island_members` - Insel-Mitglieder und Rollen
- `player_skills` - Skill-Level und XP
- `player_collections` - Collection-Fortschritt
- `player_slayers` - Slayer-Level und XP
- `player_minions` - Minion-Verwaltung
- `player_pets` - Haustier-Verwaltung
- `auction_house` - Auktionshaus
- `bazaar_orders` - Bazaar-Orders
- `player_dungeons` - Dungeon-Fortschritt
- `server_events` - Server-Events
- `guilds` - Gilden-Verwaltung
- `guild_members` - Gilden-Mitglieder
- `leaderboards` - Bestenlisten

## 🎯 Skills System (12 Skills)
### Erweiterte Skills:
1. **Farming** - Landwirtschaft mit doppelten Ernten
2. **Mining** - Bergbau mit seltenen Erzen
3. **Combat** - Kampf mit kritischen Treffern
4. **Foraging** - Holzfällen mit seltenen Hölzern
5. **Fishing** - Angeln mit seltenen Fischen
6. **Enchanting** - Verzauberungen mit reduzierten Kosten
7. **Alchemy** - Brauen mit verstärkten Tränken
8. **Taming** - Zähmung mit verbesserten Haustieren
9. **Carpentry** - Handwerk mit erweiterten Rezepten
10. **Runecrafting** - Runen erstellen und verbessern
11. **Social** - Soziale Aktivitäten mit Belohnungen
12. **Dungeoneering** - Dungeons mit verbesserter Leistung

## 📦 Collections System (50+ Items)
### Kategorien:
- **Farming**: Wheat, Carrot, Potato, Pumpkin, Melon, Sugar Cane, Cocoa, Mushrooms, Cactus, Nether Wart
- **Mining**: Cobblestone, Coal, Iron, Gold, Diamond, Lapis, Emerald, Redstone, Quartz, Obsidian, Glowstone, Gravel, Sand, End Stone, Netherrack, Mycelium
- **Foraging**: Oak, Spruce, Birch, Jungle, Acacia, Dark Oak, Crimson, Warped
- **Fishing**: Cod, Salmon, Pufferfish, Tropical Fish, Prismarine, Sponge, Lily Pad, Ink Sac
- **Combat**: Rotten Flesh, Bone, String, Spider Eye, Gunpowder, Ender Pearl, Ghast Tear, Slime Ball, Blaze Rod, Magma Cream, Nether Star, Skulls
- **Special**: Ender Eye, Dragon Egg, Elytra, Totem, Shulker Shell, Phantom Membrane, Heart of the Sea, Nautilus Shell, Conduit, Trident

## 🤖 Minions System (50+ Minion Types)
### Kategorien:
- **Farming Minions**: Wheat, Carrot, Potato, Pumpkin, Melon, Sugar Cane, Cocoa, Mushroom, Cactus, Nether Wart
- **Mining Minions**: Cobblestone, Coal, Iron, Gold, Diamond, Lapis, Emerald, Redstone, Quartz, Obsidian, Glowstone, Gravel, Sand, End Stone, Netherrack, Mycelium
- **Foraging Minions**: Oak, Spruce, Birch, Jungle, Acacia, Dark Oak, Crimson, Warped
- **Fishing Minions**: Fishing
- **Combat Minions**: Zombie, Skeleton, Spider, Creeper, Enderman, Ghast, Slime, Blaze, Magma Cube, Wither Skeleton

### Minion Features:
- Auto Collection
- Minion Upgrades
- Minion Skins
- Minion Storage
- Minion Fuel
- Minion Compactors
- Minion Super Compactors
- Minion Auto Smelters
- Minion Diamond Spreading
- Minion Budget Hoppers

## 🐾 Pets System (50+ Pet Types)
### Kategorien:
- **Combat Pets**: Zombie, Skeleton, Spider, Enderman, Blaze, Wither Skeleton, Dragon
- **Mining Pets**: Silverfish, Rock, Golem, Bal
- **Farming Pets**: Rabbit, Elephant, Mooshroom, Bee
- **Foraging Pets**: Ocelot, Monkey, Lion, Giraffe
- **Fishing Pets**: Squid, Dolphin, Guardian, Megalodon
- **Enchanting Pets**: Guardian, Sheep
- **Alchemy Pets**: Blaze, Ghast
- **Taming Pets**: Wolf, Cat, Horse, Parrot
- **Special Pets**: Phoenix, Ender Dragon, Wither

### Pet Features:
- **Rarities**: Common, Uncommon, Rare, Epic, Legendary, Mythic
- **Pet Abilities**: Spezielle Fähigkeiten je nach Pet-Typ
- **Pet Stats**: Level, XP, Rarity-basierte Multiplikatoren
- **Pet Evolution**: Aufstiegssystem
- **Pet Skins**: Verschiedene Aussehen
- **Pet Items**: Spezielle Pet-Items

## 🏰 Dungeons System
### Catacombs (F1-F7):
- **F1**: Bonzo - 5 Spieler, 5 Minuten
- **F2**: Scarf - 10 Spieler, 10 Minuten
- **F3**: The Professor - 15 Spieler, 15 Minuten
- **F4**: Thorn - 20 Spieler, 20 Minuten
- **F5**: Livid - 25 Spieler, 25 Minuten
- **F6**: Sadan - 30 Spieler, 30 Minuten
- **F7**: Necron - 35 Spieler, 35 Minuten

### Master Mode (M1-M7):
- **M1-M7**: Elite Versionen aller Catacombs Floors
- **Erhöhte Schwierigkeit**: Stärkere Bosse und Mobs
- **Bessere Belohnungen**: Mehr Coins, XP und Items

### Dungeon Classes:
- **Berserker**: +20% Nahkampfschaden
- **Archer**: +25% Fernkampfschaden
- **Mage**: +30% Magieschaden
- **Tank**: +50% Gesundheit
- **Healer**: +40% Heilung

## 🏛️ Guilds System
### Features:
- **Guild Creation**: Gilden erstellen und verwalten
- **Guild Levels**: Level-System mit XP
- **Guild Coins**: Gilden-Währung
- **Guild Members**: Mitglieder und Rollen (Owner, Officer, Member)
- **Guild Permissions**: Rollenbasierte Berechtigungen
- **Guild Chat**: Gilden-Chat
- **Guild Island**: Gilden-Insel
- **Guild Wars**: Gilden-Kriege
- **Guild Events**: Gilden-Events
- **Guild Shop**: Gilden-Shop
- **Guild Bank**: Gilden-Bank
- **Guild Upgrades**: Gilden-Upgrades
- **Guild Achievements**: Gilden-Erfolge
- **Guild Leaderboards**: Gilden-Bestenlisten

## 🏪 Auction House
### Features:
- **Multi-Server Support**: Auktionen zwischen Servern
- **Auction Creation**: Auktionen erstellen
- **Bidding System**: Bieten auf Auktionen
- **BIN (Buy It Now)**: Sofortkauf-Option
- **Auction Timer**: Zeitbasierte Auktionen
- **Fee System**: Gebühren-System (5% Auktion, 2% BIN)
- **Auction History**: Auktions-Historie
- **Search Functions**: Suchfunktionen
- **Market Depth**: Markttiefe
- **Price History**: Preis-Historie

## 🛒 Bazaar System
### Features:
- **Instant Trading**: Sofortiger Handel
- **Buy/Sell Orders**: Kauf-/Verkaufs-Orders
- **Market Depth**: Markttiefe
- **Price History**: Preis-Historie
- **Order Book Management**: Order-Buch-Verwaltung
- **Trading Volume Tracking**: Handelsvolumen-Tracking
- **Market Manipulation Protection**: Marktmanipulations-Schutz

## 🔧 Technische Verbesserungen
### Dependencies:
- **HikariCP 5.1.0**: Connection Pooling
- **MySQL Connector 8.0.33**: MySQL Support
- **MariaDB Client 3.3.2**: MariaDB Support

### Performance:
- **Async Operations**: Asynchrone Datenbank-Operationen
- **Connection Pooling**: Optimierte Datenbankverbindungen
- **Caching**: Intelligentes Caching
- **Batch Operations**: Batch-Datenbank-Operationen

### Sicherheit:
- **SQL Injection Protection**: Schutz vor SQL-Injection
- **Connection Security**: Sichere Datenbankverbindungen
- **Data Validation**: Datenvalidierung
- **Access Control**: Zugriffskontrolle

## 🚀 Installation und Setup
1. **Datenbank konfigurieren**: MySQL/MariaDB Server einrichten
2. **Plugin installieren**: JAR-Datei in plugins-Ordner
3. **Konfiguration**: config.yml anpassen
4. **Datenbank-Tabellen**: Werden automatisch erstellt
5. **Multi-Server Setup**: Server-ID in config.yml setzen

## 📊 Monitoring und Wartung
- **Server Status**: Automatische Server-Status-Updates
- **Performance Monitoring**: Datenbank-Performance-Überwachung
- **Error Logging**: Umfassende Fehlerprotokollierung
- **Backup System**: Automatische Backups
- **Maintenance Mode**: Wartungsmodus

## 🎮 Spieler-Features
- **Cross-Server Progress**: Fortschritt zwischen Servern
- **Unified Economy**: Einheitliche Wirtschaft
- **Global Leaderboards**: Globale Bestenlisten
- **Cross-Server Chat**: Server-übergreifender Chat
- **Unified Inventory**: Einheitliches Inventar
- **Global Events**: Globale Events

## 🔮 Zukünftige Erweiterungen
- **Skyblock Islands**: Erweiterte Insel-Features
- **Slayers System**: Verbessertes Slayer-System
- **Fishing System**: Erweiterte Angel-Features
- **Foraging System**: Verbessertes Foraging
- **Mining System**: Erweiterte Mining-Features
- **Combat System**: Verbessertes Kampf-System
- **Enchanting System**: Erweiterte Verzauberungen
- **Alchemy System**: Verbessertes Brauen
- **Taming System**: Erweiterte Zähmung
- **Carpentry System**: Handwerk-System
- **Runecrafting System**: Runen-System
- **Social System**: Soziale Features
- **Banking System**: Erweiterte Bank-Features
- **Quests System**: Quest-System
- **Events System**: Event-System
- **Cosmetics System**: Kosmetik-Features
- **Achievements System**: Erfolgs-System
- **Leaderboards System**: Bestenlisten-System
- **API System**: API für externe Integration
- **Web Interface**: Web-Interface

Das Plugin ist jetzt ein vollständiges Hypixel Skyblock-ähnliches System mit Multi-Server-Unterstützung und professioneller Datenbank-Integration!
