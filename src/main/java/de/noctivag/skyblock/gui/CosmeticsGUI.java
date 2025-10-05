package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * Cosmetics GUI - Kosmetik und Aussehen
 */
public class CosmeticsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public CosmeticsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void open(Player player) {
        openCosmeticsGUI(player);
    }
    
    public void openCosmeticsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lCosmetics"));
        
        // Particle Effects
        setItem(gui, 10, Material.FIREWORK_ROCKET, "§d§lParticle Effects",
            "§7Partikel-Effekte",
            "§7• Trail Effects",
            "§7• Aura Effects",
            "§7• Special Effects",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.NOTE_BLOCK, "§b§lSound Effects",
            "§7Sound-Effekte",
            "§7• Join Sounds",
            "§7• Chat Sounds",
            "§7• Action Sounds",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.ELYTRA, "§6§lWings",
            "§7Flügel und Cape",
            "§7• Angel Wings",
            "§7• Demon Wings",
            "§7• Dragon Wings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.NETHER_STAR, "§e§lHalo Effects",
            "§7Halo-Effekte",
            "§7• Angel Halo",
            "§7• Demon Halo",
            "§7• Special Halo",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.LEAD, "§a§lTrail Effects",
            "§7Trail-Effekte",
            "§7• Rainbow Trail",
            "§7• Fire Trail",
            "§7• Ice Trail",
            "",
            "§eKlicke zum Öffnen");
            
        // Chat Cosmetics
        setItem(gui, 19, Material.WRITABLE_BOOK, "§b§lChat Cosmetics",
            "§7Chat-Kosmetik",
            "§7• Chat Colors",
            "§7• Chat Formats",
            "§7• Chat Emojis",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.NAME_TAG, "§e§lName Tags",
            "§7Namens-Tags",
            "§7• Colored Names",
            "§7• Special Names",
            "§7• Custom Names",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.PLAYER_HEAD, "§d§lSkins",
            "§7Spieler-Skins",
            "§7• Custom Skins",
            "§7• Special Skins",
            "§7• Animated Skins",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.ARMOR_STAND, "§f§lArmor Cosmetics",
            "§7Rüstungs-Kosmetik",
            "§7• Invisible Armor",
            "§7• Colored Armor",
            "§7• Special Armor",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.ITEM_FRAME, "§6§lItem Cosmetics",
            "§7Item-Kosmetik",
            "§7• Custom Items",
            "§7• Special Items",
            "§7• Animated Items",
            "",
            "§eKlicke zum Öffnen");
            
        // Special Cosmetics
        setItem(gui, 28, Material.TOTEM_OF_UNDYING, "§5§lSpecial Cosmetics",
            "§7Besondere Kosmetik",
            "§7• Rare Effects",
            "§7• Limited Edition",
            "§7• Event Cosmetics",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.GOLDEN_APPLE, "§6§lPremium Cosmetics",
            "§7Premium Kosmetik",
            "§7• VIP Effects",
            "§7• Premium Skins",
            "§7• Exclusive Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.EMERALD, "§a§lCosmetic Shop",
            "§7Kaufe Kosmetik",
            "§7• New Cosmetics",
            "§7• Special Offers",
            "§7• Bundles",
            "",
            "§eKlicke zum Öffnen");
            
        // Cosmetic Management
        setItem(gui, 37, Material.BOOK, "§b§lMy Cosmetics",
            "§7Deine Kosmetik",
            "§7• Active Cosmetics",
            "§7• Cosmetic History",
            "§7• Favorites",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lCosmetic Settings",
            "§7Kosmetik-Einstellungen",
            "§7• Show/Hide Effects",
            "§7• Performance Settings",
            "§7• Auto-Equip",
            "",
            "§eKlicke zum Öffnen");
            
        // Navigation
        setItem(gui, 45, Material.ARROW, "§7« Back",
            "§7Zurück zum Hauptmenü");
            
        setItem(gui, 49, Material.BARRIER, "§c§lClose",
            "§7GUI schließen");
            
        player.openInventory(gui);
    }
    
    private void setItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
