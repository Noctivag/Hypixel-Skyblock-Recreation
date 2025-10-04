package de.noctivag.plugin.network;
import org.bukkit.inventory.ItemStack;

/**
 * Extended Server Types - Erweiterte Server-Typen für das Multi-Server-System
 * 
 * Definiert alle verfügbaren Server-Typen mit erweiterten Konfigurationen
 */
public class ExtendedServerTypes {
    
    /**
     * Erweiterte Server-Typen
     */
    public enum ExtendedServerType {
        // Basis Server-Typen
        HUB("Hub Server", "Hauptserver für Lobby und Navigation", 200, 25565, true),
        ISLAND("Island Server", "Private Inseln für Spieler", 100, 25566, true),
        DUNGEON("Dungeon Server", "Dungeon-Instanzen", 20, 25567, false),
        EVENT("Event Server", "Event-basierte Inseln", 150, 25568, true),
        AUCTION("Auction Server", "Auktionshaus", 50, 25569, true),
        BANK("Bank Server", "Bank-System", 30, 25570, true),
        MINIGAME("Minigame Server", "Minigames", 80, 25571, true),
        PVP("PvP Server", "PvP-Arenen", 60, 25572, true),
        
        // Erweiterte Server-Typen
        CREATIVE("Creative Server", "Creative-Modus Welten", 100, 25573, true),
        SURVIVAL("Survival Server", "Survival-Modus Welten", 150, 25574, true),
        HARDCORE("Hardcore Server", "Hardcore-Modus Welten", 50, 25575, true),
        ADVENTURE("Adventure Server", "Adventure-Modus Welten", 80, 25576, true),
        SPECTATOR("Spectator Server", "Spectator-Modus Welten", 200, 25577, true),
        TEST("Test Server", "Test- und Entwicklungs-Welten", 20, 25578, false),
        RESOURCE("Resource Server", "Resource-Sammel-Welten", 100, 25579, true),
        NETHER("Nether Server", "Nether-Dimension Welten", 80, 25580, true),
        END("End Server", "End-Dimension Welten", 60, 25581, true),
        
        // Spezialisierte Server-Typen
        SKYBLOCK("Skyblock Server", "Skyblock-spezifische Welten", 120, 25582, true),
        BEDWARS("Bedwars Server", "Bedwars-Minigames", 100, 25583, true),
        SKYWARS("Skywars Server", "Skywars-Minigames", 100, 25584, true),
        UHC("UHC Server", "Ultra Hardcore Games", 50, 25585, true),
        KITPVP("KitPvP Server", "Kit-basierte PvP-Arenen", 80, 25586, true),
        PRISON("Prison Server", "Prison-Spielmodus", 100, 25587, true),
        FACTIONS("Factions Server", "Factions-Spielmodus", 120, 25588, true),
        TOWNY("Towny Server", "Towny-Spielmodus", 100, 25589, true),
        
        // Technische Server-Typen
        PROXY("Proxy Server", "BungeeCord/Velocity Proxy", 1000, 25590, true),
        LOBBY("Lobby Server", "Hauptlobby für alle Spieler", 500, 25591, true),
        QUEUE("Queue Server", "Warteschlangen-Server", 200, 25592, true),
        MAINTENANCE("Maintenance Server", "Wartungs-Server", 10, 25593, false);
        
        private final String displayName;
        private final String description;
        private final int maxPlayers;
        private final int defaultPort;
        private final boolean isPublic;
        
        ExtendedServerType(String displayName, String description, int maxPlayers, int defaultPort, boolean isPublic) {
            this.displayName = displayName;
            this.description = description;
            this.maxPlayers = maxPlayers;
            this.defaultPort = defaultPort;
            this.isPublic = isPublic;
        }
        
        // Getters
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public int getMaxPlayers() { return maxPlayers; }
        public int getDefaultPort() { return defaultPort; }
        public boolean isPublic() { return isPublic; }
        
        /**
         * Gibt den Server-Typ basierend auf dem Namen zurück
         */
        public static ExtendedServerType fromName(String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        
        /**
         * Gibt alle öffentlichen Server-Typen zurück
         */
        public static ExtendedServerType[] getPublicTypes() {
            return java.util.Arrays.stream(values())
                .filter(ExtendedServerType::isPublic)
                .toArray(ExtendedServerType[]::new);
        }
        
        /**
         * Gibt alle privaten Server-Typen zurück
         */
        public static ExtendedServerType[] getPrivateTypes() {
            return java.util.Arrays.stream(values())
                .filter(type -> !type.isPublic())
                .toArray(ExtendedServerType[]::new);
        }
    }
    
    /**
     * Server-Konfiguration für erweiterte Typen
     */
    public static class ServerTypeConfig {
        private final ExtendedServerType serverType;
        private final String worldName;
        private final String[] additionalWorlds;
        private final boolean allowCustomWorlds;
        private final boolean allowWorldUpload;
        private final int maxCustomWorlds;
        private final String[] allowedWorldTypes;
        private final boolean enableMultithreading;
        private final int threadPoolSize;
        
        public ServerTypeConfig(ExtendedServerType serverType, String worldName, String[] additionalWorlds,
                               boolean allowCustomWorlds, boolean allowWorldUpload, int maxCustomWorlds,
                               String[] allowedWorldTypes, boolean enableMultithreading, int threadPoolSize) {
            this.serverType = serverType;
            this.worldName = worldName;
            this.additionalWorlds = additionalWorlds != null ? additionalWorlds.clone() : new String[0];
            this.allowCustomWorlds = allowCustomWorlds;
            this.allowWorldUpload = allowWorldUpload;
            this.maxCustomWorlds = maxCustomWorlds;
            this.allowedWorldTypes = allowedWorldTypes != null ? allowedWorldTypes.clone() : new String[0];
            this.enableMultithreading = enableMultithreading;
            this.threadPoolSize = threadPoolSize;
        }
        
        // Getters
        public ExtendedServerType getServerType() { return serverType; }
        public String getWorldName() { return worldName; }
        public String[] getAdditionalWorlds() { return additionalWorlds.clone(); }
        public boolean isAllowCustomWorlds() { return allowCustomWorlds; }
        public boolean isAllowWorldUpload() { return allowWorldUpload; }
        public int getMaxCustomWorlds() { return maxCustomWorlds; }
        public String[] getAllowedWorldTypes() { return allowedWorldTypes.clone(); }
        public boolean isEnableMultithreading() { return enableMultithreading; }
        public int getThreadPoolSize() { return threadPoolSize; }
    }
    
    /**
     * Gibt die Standard-Konfiguration für einen Server-Typ zurück
     */
    public static ServerTypeConfig getDefaultConfig(ExtendedServerType serverType) {
        switch (serverType) {
            case HUB:
                return new ServerTypeConfig(serverType, "hub_world", 
                    new String[]{"lobby_world", "spawn_world"}, 
                    false, false, 0, new String[0], true, 4);
                    
            case ISLAND:
                return new ServerTypeConfig(serverType, "skyblock_private", 
                    new String[]{"skyblock_public", "skyblock_coop"}, 
                    true, true, 10, new String[]{"NORMAL", "FLAT"}, true, 6);
                    
            case DUNGEON:
                return new ServerTypeConfig(serverType, "skyblock_dungeons", 
                    new String[0], 
                    true, true, 50, new String[]{"NORMAL", "NETHER", "THE_END"}, true, 8);
                    
            case EVENT:
                return new ServerTypeConfig(serverType, "event_arenas", 
                    new String[]{"event_lobby", "event_spectator"}, 
                    true, true, 20, new String[]{"FLAT", "NORMAL"}, true, 4);
                    
            case AUCTION:
                return new ServerTypeConfig(serverType, "auction_house", 
                    new String[0], 
                    false, false, 0, new String[0], true, 2);
                    
            case BANK:
                return new ServerTypeConfig(serverType, "bank_world", 
                    new String[0], 
                    false, false, 0, new String[0], true, 2);
                    
            case MINIGAME:
                return new ServerTypeConfig(serverType, "minigame_worlds", 
                    new String[]{"bedwars_world", "skywars_world", "uhc_world"}, 
                    true, true, 30, new String[]{"FLAT", "NORMAL"}, true, 6);
                    
            case PVP:
                return new ServerTypeConfig(serverType, "pvp_arenas", 
                    new String[]{"kitpvp_world", "factions_world"}, 
                    true, true, 25, new String[]{"FLAT", "NORMAL"}, true, 4);
                    
            case CREATIVE:
                return new ServerTypeConfig(serverType, "creative_worlds", 
                    new String[]{"creative_flat", "creative_amplified"}, 
                    true, true, 50, new String[]{"NORMAL", "FLAT", "AMPLIFIED"}, true, 8);
                    
            case SURVIVAL:
                return new ServerTypeConfig(serverType, "survival_worlds", 
                    new String[]{"survival_nether", "survival_end"}, 
                    true, true, 20, new String[]{"NORMAL", "NETHER", "THE_END"}, true, 6);
                    
            case HARDCORE:
                return new ServerTypeConfig(serverType, "hardcore_worlds", 
                    new String[]{"hardcore_nether", "hardcore_end"}, 
                    true, true, 10, new String[]{"NORMAL", "NETHER", "THE_END"}, true, 4);
                    
            case ADVENTURE:
                return new ServerTypeConfig(serverType, "adventure_worlds", 
                    new String[0], 
                    true, true, 15, new String[]{"NORMAL", "NETHER", "THE_END"}, true, 4);
                    
            case SPECTATOR:
                return new ServerTypeConfig(serverType, "spectator_worlds", 
                    new String[0], 
                    true, true, 5, new String[]{"FLAT", "VOID"}, true, 2);
                    
            case TEST:
                return new ServerTypeConfig(serverType, "test_worlds", 
                    new String[0], 
                    true, true, 100, new String[]{"NORMAL", "FLAT", "NETHER", "THE_END", "AMPLIFIED"}, true, 10);
                    
            case RESOURCE:
                return new ServerTypeConfig(serverType, "resource_worlds", 
                    new String[]{"resource_nether", "resource_end"}, 
                    true, true, 20, new String[]{"NORMAL", "NETHER", "THE_END"}, true, 6);
                    
            case NETHER:
                return new ServerTypeConfig(serverType, "nether_worlds", 
                    new String[0], 
                    true, true, 15, new String[]{"NETHER"}, true, 4);
                    
            case END:
                return new ServerTypeConfig(serverType, "end_worlds", 
                    new String[0], 
                    true, true, 10, new String[]{"THE_END"}, true, 4);
                    
            case SKYBLOCK:
                return new ServerTypeConfig(serverType, "skyblock_worlds", 
                    new String[]{"skyblock_private", "skyblock_public", "skyblock_coop"}, 
                    true, true, 30, new String[]{"NORMAL", "FLAT"}, true, 6);
                    
            case BEDWARS:
                return new ServerTypeConfig(serverType, "bedwars_worlds", 
                    new String[]{"bedwars_lobby", "bedwars_spectator"}, 
                    true, true, 50, new String[]{"FLAT"}, true, 8);
                    
            case SKYWARS:
                return new ServerTypeConfig(serverType, "skywars_worlds", 
                    new String[]{"skywars_lobby", "skywars_spectator"}, 
                    true, true, 50, new String[]{"FLAT"}, true, 8);
                    
            case UHC:
                return new ServerTypeConfig(serverType, "uhc_worlds", 
                    new String[]{"uhc_lobby", "uhc_spectator"}, 
                    true, true, 20, new String[]{"NORMAL", "AMPLIFIED"}, true, 4);
                    
            case KITPVP:
                return new ServerTypeConfig(serverType, "kitpvp_worlds", 
                    new String[]{"kitpvp_lobby", "kitpvp_spectator"}, 
                    true, true, 30, new String[]{"FLAT", "NORMAL"}, true, 4);
                    
            case PRISON:
                return new ServerTypeConfig(serverType, "prison_worlds", 
                    new String[]{"prison_mines", "prison_shop"}, 
                    true, true, 10, new String[]{"NORMAL", "FLAT"}, true, 4);
                    
            case FACTIONS:
                return new ServerTypeConfig(serverType, "factions_worlds", 
                    new String[]{"factions_nether", "factions_end"}, 
                    true, true, 5, new String[]{"NORMAL", "NETHER", "THE_END"}, true, 4);
                    
            case TOWNY:
                return new ServerTypeConfig(serverType, "towny_worlds", 
                    new String[]{"towny_nether", "towny_end"}, 
                    true, true, 5, new String[]{"NORMAL", "NETHER", "THE_END"}, true, 4);
                    
            case PROXY:
                return new ServerTypeConfig(serverType, "proxy_world", 
                    new String[0], 
                    false, false, 0, new String[0], true, 2);
                    
            case LOBBY:
                return new ServerTypeConfig(serverType, "lobby_world", 
                    new String[]{"lobby_parkour", "lobby_shop"}, 
                    false, false, 0, new String[0], true, 2);
                    
            case QUEUE:
                return new ServerTypeConfig(serverType, "queue_world", 
                    new String[0], 
                    false, false, 0, new String[0], true, 2);
                    
            case MAINTENANCE:
                return new ServerTypeConfig(serverType, "maintenance_world", 
                    new String[0], 
                    false, false, 0, new String[0], true, 1);
                    
            default:
                return new ServerTypeConfig(serverType, "default_world", 
                    new String[0], 
                    false, false, 0, new String[0], true, 2);
        }
    }
    
    /**
     * Gibt alle verfügbaren Server-Typen zurück
     */
    public static ExtendedServerType[] getAllServerTypes() {
        return ExtendedServerType.values();
    }
    
    /**
     * Gibt alle Server-Typen zurück, die Custom Worlds unterstützen
     */
    public static ExtendedServerType[] getCustomWorldSupportedTypes() {
        return java.util.Arrays.stream(ExtendedServerType.values())
            .filter(type -> getDefaultConfig(type).isAllowCustomWorlds())
            .toArray(ExtendedServerType[]::new);
    }
    
    /**
     * Gibt alle Server-Typen zurück, die World Upload unterstützen
     */
    public static ExtendedServerType[] getWorldUploadSupportedTypes() {
        return java.util.Arrays.stream(ExtendedServerType.values())
            .filter(type -> getDefaultConfig(type).isAllowWorldUpload())
            .toArray(ExtendedServerType[]::new);
    }
    
    /**
     * Gibt alle Server-Typen zurück, die Multithreading unterstützen
     */
    public static ExtendedServerType[] getMultithreadingSupportedTypes() {
        return java.util.Arrays.stream(ExtendedServerType.values())
            .filter(type -> getDefaultConfig(type).isEnableMultithreading())
            .toArray(ExtendedServerType[]::new);
    }
}
