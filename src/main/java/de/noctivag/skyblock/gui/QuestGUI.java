package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class QuestGUI extends CustomGUI {
    private final Player player;
    
    public QuestGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§a§lQuest System", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.BOOK, "§a§lAktive Quests", "§7Zeige aktive Quests");
        setItem(12, Material.ENCHANTED_BOOK, "§6§lVerfügbare Quests", "§7Zeige verfügbare Quests");
        setItem(14, Material.GOLD_INGOT, "§e§lAbgeschlossene Quests", "§7Zeige abgeschlossene Quests");
        setItem(16, Material.EMERALD, "§b§lQuest Belohnungen", "§7Zeige Quest-Belohnungen");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
