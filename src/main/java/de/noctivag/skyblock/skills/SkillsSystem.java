package de.noctivag.skyblock.skills;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Skills System - Basic implementation
 */
public class SkillsSystem {
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;

    public SkillsSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void addXP(Player player, String skill, int amount) {
        player.sendMessage("§aDu hast " + amount + " " + skill + " XP erhalten!");
    }
}
