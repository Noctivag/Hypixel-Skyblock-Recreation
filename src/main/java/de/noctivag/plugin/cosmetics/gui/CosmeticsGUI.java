package de.noctivag.plugin.cosmetics.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.gui.CosmeticsMenu;
import de.noctivag.plugin.gui.CustomGUI;
import org.bukkit.entity.Player;

public class CosmeticsGUI extends CustomGUI {
    private final Plugin plugin;
    private final de.noctivag.plugin.cosmetics.CosmeticsManager cosmeticsManager;

    public CosmeticsGUI(Plugin plugin, de.noctivag.plugin.cosmetics.CosmeticsManager cosmeticsManager) {
        super("§6✧ Kosmetik-Menü ✧", 6);
        this.plugin = plugin;
        this.cosmeticsManager = cosmeticsManager;
    }

    public void openMainMenu(Player player) {
        // Delegate to the unified CosmeticsMenu implementation
        new CosmeticsMenu(plugin, cosmeticsManager).open(player);
    }
}
