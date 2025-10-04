package de.noctivag.skyblock.features.npcs.quests;
import org.bukkit.inventory.ItemStack;

public enum QuestType {
    KILL_MOBS("Kill Mobs", "⚔️", "Kill specific mobs"),
    MINE_BLOCKS("Mine Blocks", "⛏️", "Mine specific blocks"),
    COLLECT_ITEMS("Collect Items", "📦", "Collect specific items"),
    BUILD("Build", "🏗️", "Build structures"),
    KILL_BOSS("Kill Boss", "👑", "Defeat bosses"),
    FISH("Fish", "🎣", "Catch fish"),
    FARM("Farm", "🌾", "Farm crops"),
    FORAGE("Forage", "🌳", "Forage resources"),
    EXPLORE("Explore", "🗺️", "Explore areas"),
    TRADE("Trade", "💰", "Trade items"),
    CRAFT("Craft", "🔨", "Craft items"),
    ENCHANT("Enchant", "✨", "Enchant items"),
    BREW("Brew", "🧪", "Brew potions"),
    TAME("Tame", "🐾", "Tame pets"),
    COMPLETE_DUNGEON("Complete Dungeon", "🏰", "Complete dungeons");

    private final String displayName;
    private final String icon;
    private final String description;

    QuestType(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedName() {
        return icon + " " + displayName;
    }

    @Override
    public String toString() {
        return getFormattedName();
    }
}
