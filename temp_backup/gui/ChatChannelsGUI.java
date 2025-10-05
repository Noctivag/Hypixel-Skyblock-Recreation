package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class ChatChannelsGUI extends CustomGUI {
    private final Player player;
    
    public ChatChannelsGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§b§lChat Channels", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§a§lGlobal Chat", "§7Wechsle zum Global Chat");
        setItem(12, Material.PLAYER_HEAD, "§6§lParty Chat", "§7Wechsle zum Party Chat");
        setItem(14, Material.GOLD_INGOT, "§e§lGuild Chat", "§7Wechsle zum Guild Chat");
        setItem(16, Material.EMERALD, "§b§lTrade Chat", "§7Wechsle zum Trade Chat");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
