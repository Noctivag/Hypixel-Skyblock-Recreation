package de.noctivag.skyblock.cosmetics.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.CosmeticsMenu;
import de.noctivag.skyblock.gui.CustomGUI;
import org.bukkit.entity.Player;

public class CosmeticsGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final de.noctivag.skyblock.cosmetics.CosmeticsManager cosmeticsManager;

    public CosmeticsGUI(SkyblockPlugin SkyblockPlugin, de.noctivag.skyblock.cosmetics.CosmeticsManager cosmeticsManager) {
        super("§6✧ Kosmetik-Menü ✧", 6);
        this.SkyblockPlugin = SkyblockPlugin;
        this.cosmeticsManager = cosmeticsManager;
    }

    public void openMainMenu(Player player) {
        // Delegate to the unified CosmeticsMenu implementation
        new CosmeticsMenu(SkyblockPlugin, cosmeticsManager).open(player);
    }
    
    @Override
    public void setupItems() {
        // Placeholder implementation
    }
}
