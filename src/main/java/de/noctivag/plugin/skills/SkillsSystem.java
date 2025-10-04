package de.noctivag.plugin.skills;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Skills System - Basic implementation
 */
public class SkillsSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;

    public SkillsSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void addXP(Player player, String skill, int amount) {
        player.sendMessage("§aDu hast " + amount + " " + skill + " XP erhalten!");
    }
}
