package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class CosmeticShopGUI extends CustomGUI {
    private final Player player;
    
    public CosmeticShopGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§d§lCosmetic Shop", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.DIAMOND, "§b§lPartikel-Effekte", "§7Kaufe Partikel-Effekte");
        setItem(12, Material.ELYTRA, "§a§lFlügel", "§7Kaufe Flügel");
        setItem(14, Material.LEATHER_HELMET, "§6§lHüte", "§7Kaufe Hüte");
        setItem(16, Material.WOLF_SPAWN_EGG, "§e§lHaustiere", "§7Kaufe Haustiere");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
