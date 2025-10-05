package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.events.EventManager;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import java.util.Arrays;

public class EventMenu extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final EventManager eventManager;

    public EventMenu() {
        super("§c§lEvent Menü", 4);
        this.SkyblockPlugin = null;
        this.eventManager = null;
        initializeItems();
    }

    public EventMenu(SkyblockPlugin SkyblockPlugin) {
        super("§c§lEvent Menü", 4);
        this.SkyblockPlugin = SkyblockPlugin;
        this.eventManager = (EventManager) SkyblockPlugin.getEventManager(); // Cast from Object to EventManager
        initializeItems();
    }

    private void initializeItems() {
        // Aktive Events - Row 1
        setItem(10, Material.DRAGON_HEAD, "§5§lEnder Dragon Event",
            "§7Stelle dich dem Enderdrachen!",
            "§eKosten: §f100 Coins",
            "§eBelohnung: §f500 Coins",
            "§eLevel: §f10+",
            "§eKlicke zum Beitreten");

        setItem(11, Material.WITHER_SKELETON_SKULL, "§8§lWither Boss Event",
            "§7Bezwinge den Wither-Boss!",
            "§eKosten: §f200 Coins",
            "§eBelohnung: §f1000 Coins",
            "§eLevel: §f15+",
            "§eKlicke zum Beitreten");

        setItem(12, Material.ZOMBIE_HEAD, "§2§lTitan Event",
            "§7Kämpfe gegen den Titan!",
            "§eKosten: §f300 Coins",
            "§eBelohnung: §f1500 Coins",
            "§eLevel: §f20+",
            "§eKlicke zum Beitreten");

        setItem(13, Material.PRISMARINE_SHARD, "§b§lElder Guardian Event",
            "§7Bekämpfe den Elder Guardian!",
            "§eKosten: §f250 Coins",
            "§eBelohnung: §f1200 Coins",
            "§eLevel: §f18+",
            "§eKlicke zum Beitreten");

        setItem(14, Material.IRON_AXE, "§6§lRavager Event",
            "§7Stelle dich dem Ravager!",
            "§eKosten: §f180 Coins",
            "§eBelohnung: §f900 Coins",
            "§eLevel: §f12+",
            "§eKlicke zum Beitreten");

        // Row 2
        setItem(15, Material.PHANTOM_MEMBRANE, "§8§lPhantom King Event",
            "§7Bekämpfe den Phantom King!",
            "§eKosten: §f220 Coins",
            "§eBelohnung: §f1100 Coins",
            "§eLevel: §f16+",
            "§eKlicke zum Beitreten");

        setItem(16, Material.BLAZE_POWDER, "§c§lBlaze King Event",
            "§7Stelle dich dem Blaze King!",
            "§eKosten: §f280 Coins",
            "§eBelohnung: §f1400 Coins",
            "§eLevel: §f22+",
            "§eKlicke zum Beitreten");

        setItem(17, Material.ENDER_PEARL, "§5§lEnderman Lord Event",
            "§7Bekämpfe den Enderman Lord!",
            "§eKosten: §f350 Coins",
            "§eBelohnung: §f1800 Coins",
            "§eLevel: §f25+",
            "§eKlicke zum Beitreten");

        // Zeitplan
        setItem(28, Material.CLOCK, "§e§lNächste Events",
            "§7Drachen-Event: §aIn 30 Minuten",
            "§7Wither-Boss: §aIn 1 Stunde",
            "§7Zombie-Horde: §aIn 2 Stunden");

        // Statistiken
        setItem(30, Material.PAPER, "§b§lDeine Event-Stats",
            "§7Teilnahmen: §e12",
            "§7Siege: §a5",
            "§7Rang: §6#3");

        // Belohnungen
        setItem(32, Material.CHEST, "§6§lEvent-Shop",
            "§7Tausche deine Event-Punkte",
            "§7gegen tolle Belohnungen ein!",
            "§eDeine Punkte: §61250");

        // Zurück zum Hauptmenü
        setItem(35, Material.BARRIER, "§c§lZurück",
            "§7Zum Hauptmenü");
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        getInventory().setItem(slot, item);
    }
}
