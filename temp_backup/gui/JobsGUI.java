package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class JobsGUI extends CustomGUI {
    private final Player player;
    
    public JobsGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§e§lJobs", 54);
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
