package de.noctivag.skyblock.cosmetics.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.gui.CosmeticsMenu;
import de.noctivag.skyblock.gui.CustomGUI;
import org.bukkit.entity.Player;

public class CosmeticsGUI extends CustomGUI {
    private final SkyblockPlugin plugin;
    private final de.noctivag.plugin.cosmetics.CosmeticsManager cosmeticsManager;

    public CosmeticsGUI(SkyblockPlugin plugin, de.noctivag.plugin.cosmetics.CosmeticsManager cosmeticsManager) {
        super("§6✧ Kosmetik-Menü ✧", 6);
        this.plugin = plugin;
        this.cosmeticsManager = cosmeticsManager;
    }

    public void openMainMenu(Player player) {
        // Delegate to the unified CosmeticsMenu implementation
        new CosmeticsMenu(plugin, cosmeticsManager).open(player);
    }
}
