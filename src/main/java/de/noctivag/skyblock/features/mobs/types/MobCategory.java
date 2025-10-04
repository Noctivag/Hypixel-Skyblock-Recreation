package de.noctivag.skyblock.features.mobs.types;
import org.bukkit.inventory.ItemStack;

public enum MobCategory {
    PRIVATE_ISLAND("Private Island"),
    HUB("Hub"),
    DEEP_CAVERNS("Deep Caverns"),
    SPIDERS_DEN("Spider's Den"),
    THE_END("The End"),
    DWARVEN_MINES("Dwarven Mines"),
    CRYSTAL_HOLLOWS("Crystal Hollows"),
    CRIMSON_ISLE("Crimson Isle"),
    SLAYER_BOSS("Slayer Boss"),
    WORLD_BOSS("World Boss"),
    SPECIAL_BOSS("Special Boss"),
    DUNGEON("Dungeon"),
    NETHER("Nether"),
    FISHING("Fishing"),
    EVENT("Event");

    private final String displayName;

    MobCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
