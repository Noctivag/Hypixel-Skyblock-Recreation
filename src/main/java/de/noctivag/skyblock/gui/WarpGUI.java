package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WarpGUI extends CustomGUI {
    private final Player player;
    
    public WarpGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§b§lWarp Menu", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        // Warp locations
        setItem(10, Material.GRASS_BLOCK, "§a§lSpawn",
            "§7Teleportiere zum Spawn",
            "§eKosten: §fKostenlos");
            
        setItem(12, Material.DIAMOND_ORE, "§6§lMining World",
            "§7Teleportiere zur Mining-Welt",
            "§eKosten: §f50 Coins");
            
        setItem(14, Material.NETHER_BRICK, "§c§lNether",
            "§7Teleportiere zum Nether",
            "§eKosten: §f100 Coins");
            
        setItem(16, Material.END_STONE, "§5§lEnd",
            "§7Teleportiere zum End",
            "§eKosten: §f200 Coins");
            
        setItem(28, Material.VILLAGER_SPAWN_EGG, "§e§lVillage",
            "§7Teleportiere zum Dorf",
            "§eKosten: §f25 Coins");
            
        setItem(30, Material.SKELETON_SKULL, "§8§lDungeon",
            "§7Teleportiere zum Dungeon",
            "§eKosten: §f150 Coins");
            
        // Back button
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
