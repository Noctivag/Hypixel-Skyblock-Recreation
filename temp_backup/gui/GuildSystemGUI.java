package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class GuildSystemGUI extends CustomGUI {
    private final Player player;
    
    public GuildSystemGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§6§lGuild System", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.GOLD_INGOT, "§6§lGuild erstellen", "§7Erstelle eine neue Guild");
        setItem(12, Material.BOOK, "§a§lGuild beitreten", "§7Tritt einer Guild bei");
        setItem(14, Material.GOLD_INGOT, "§e§lGuild verwalten", "§7Verwalte deine Guild");
        setItem(16, Material.EMERALD, "§b§lGuild verlassen", "§7Verlasse deine Guild");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
