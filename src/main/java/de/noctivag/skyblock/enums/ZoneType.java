package de.noctivag.skyblock.enums;

/**
 * Enum für verschiedene Zonen im Skyblock
 */
public enum ZoneType {
    
    HUB("§aHub", "Das zentrale Gebiet des Servers"),
    PRIVATE_ISLAND("§ePrivate Insel", "Deine persönliche Insel"),
    DWARVEN_MINES("§6Dwarven Mines", "Tiefe Minen mit wertvollen Erzen"),
    CRYSTAL_HOLLOWS("§bCrystal Hollows", "Kristallhöhlen mit seltenen Ressourcen"),
    CRIMSON_ISLE("§cCrimson Isle", "Feurige Insel mit mächtigen Gegnern"),
    END("§5The End", "Das Ende mit Endermen und Drachen"),
    SPIDERS_DEN("§8Spider's Den", "Spinnennetz mit giftigen Kreaturen"),
    BLAZING_FORTRESS("§6Blazing Fortress", "Feurige Festung mit Nether-Mobs"),
    DUNGEON("§dDungeon", "Gefährliche Dungeons mit Bossen"),
    RIFT("§5Rift", "Mystische Dimension mit einzigartigen Mobs");
    
    private final String displayName;
    private final String description;
    
    ZoneType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}
