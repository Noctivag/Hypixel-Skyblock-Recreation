package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class LeaderboardGUI extends CustomGUI {
    private final Player player;
    
    public LeaderboardGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§e§lRangliste", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.DIAMOND, "§b§lLevel Rangliste", "§7Zeige Level-Rangliste");
        setItem(12, Material.GOLD_INGOT, "§6§lCoins Rangliste", "§7Zeige Coins-Rangliste");
        setItem(14, Material.DIAMOND_SWORD, "§c§lKills Rangliste", "§7Zeige Kills-Rangliste");
        setItem(16, Material.GOLD_INGOT, "§e§lGesamt Rangliste", "§7Zeige Gesamt-Rangliste");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
