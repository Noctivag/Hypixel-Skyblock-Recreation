package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class JobsGUI extends CustomGUI {
    private final Player player;
    
    public JobsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, "§e§lJobs", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.DIAMOND_PICKAXE, "§6§lMiner", "§7Werde ein Miner");
        setItem(12, Material.DIAMOND_SWORD, "§c§lKrieger", "§7Werde ein Krieger");
        setItem(14, Material.FISHING_ROD, "§b§lFischer", "§7Werde ein Fischer");
        setItem(16, Material.DIAMOND_HOE, "§a§lBauer", "§7Werde ein Bauer");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
