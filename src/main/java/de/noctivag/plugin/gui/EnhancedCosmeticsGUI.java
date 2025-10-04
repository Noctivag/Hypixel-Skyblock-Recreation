package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.cosmetics.AdvancedCosmeticsSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

public class EnhancedCosmeticsGUI extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final AdvancedCosmeticsSystem cosmeticsSystem;
    private String currentCategory = "particles";

    public EnhancedCosmeticsGUI(Plugin plugin, Player player) {
        super(54, Component.text("§d§lEnhanced Cosmetics"));
        this.plugin = plugin;
        this.player = player;
        // Placeholder - method not implemented
        this.cosmeticsSystem = null;
        setupItems();
    }

    private void setupItems() {
        // Header
        setItem(4, Material.NETHER_STAR, "§d§lCosmetics System", 
            "§7Kaufe und aktiviere verschiedene Cosmetics",
            "§7Kategorie: §e" + getCategoryDisplayName(currentCategory));
        
        // Category buttons
        setItem(10, Material.NETHER_STAR, "§d§lPartikel-Effekte", 
            "§7Verschiedene Partikel-Effekte",
            "§eKlicke zum Anzeigen");
        
        setItem(11, Material.ELYTRA, "§f§lFlügel", 
            "§7Verschiedene Flügel-Typen",
            "§eKlicke zum Anzeigen");
        
        setItem(12, Material.GOLD_BLOCK, "§e§lHeiligenschein", 
            "§7Verschiedene Heiligenscheine",
            "§eKlicke zum Anzeigen");
        
        setItem(13, Material.FIREWORK_ROCKET, "§7§lSpuren", 
            "§7Verschiedene Spuren-Effekte",
            "§eKlicke zum Anzeigen");
        
        setItem(14, Material.NOTE_BLOCK, "§a§lSound-Effekte", 
            "§7Verschiedene Sound-Effekte",
            "§eKlicke zum Anzeigen");
        
        // Show cosmetics based on current category
        showCosmetics();
        
        // Action buttons
        setItem(28, Material.EMERALD, "§a§lAlle Deaktivieren",
            "§7Deaktiviere alle aktiven Cosmetics",
            "§eKlicke zum Deaktivieren");
        
        setItem(30, Material.BOOK, "§b§lMeine Cosmetics",
            "§7Zeige deine gekauften Cosmetics",
            "§eKlicke zum Anzeigen");
        
        setItem(32, Material.CHEST, "§6§lCosmetic Shop",
            "§7Kaufe neue Cosmetics",
            "§eKlicke zum Öffnen");
        
        // Navigation
        setItem(45, Material.ARROW, "§7§lZurück", "§7Zurück zum Hauptmenü");
        setItem(49, Material.BARRIER, "§c§lSchließen", "§7Schließe das Menü");
        
        // Decorative items
        for (int i = 0; i < 9; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 36; i < 45; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 46; i < 49; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 50; i < 54; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
    }

    private void showCosmetics() {
        AdvancedCosmeticsSystem.CosmeticCategory category = cosmeticsSystem.getCategory(currentCategory);
        if (category == null) return;
        
        int slot = 19;
        for (AdvancedCosmeticsSystem.CosmeticItem cosmetic : category.getCosmetics()) {
            if (slot >= 27) break; // Don't overflow
            
            // Placeholder - method not implemented
            boolean owned = false;
            boolean active = isCosmeticActive(cosmetic.getId());
            
            Material icon = cosmetic.getIcon();
            String name = cosmetic.getName();
            String status = "";
            
            if (active) {
                name = "§a§l✓ " + name;
                status = "§a§lAktiv";
            } else if (owned) {
                name = "§e§l" + name;
                status = "§e§lKlicke zum Aktivieren";
            } else {
                name = "§7§l" + name;
                status = "§c§lNicht gekauft";
            }
            
            setItem(slot, icon, name,
                "§7" + cosmetic.getDescription(),
                "",
                "§ePreis: §f" + plugin.getEconomyManager().formatMoney(cosmetic.getPrice()),
                "§7Typ: §f" + getTypeDisplayName(cosmetic.getType()),
                "",
                status);
            
            slot++;
        }
        
        // Fill remaining slots with glass panes
        while (slot < 27) {
            setItem(slot, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
            slot++;
        }
    }

    private boolean isCosmeticActive(String cosmeticId) {
        // Check if this cosmetic is currently active
        // This would need to be implemented in the cosmetics system
        return false; // Placeholder
    }

    private String getCategoryDisplayName(String categoryId) {
        return switch (categoryId) {
            case "particles" -> "Partikel-Effekte";
            case "wings" -> "Flügel";
            case "halos" -> "Heiligenschein";
            case "trails" -> "Spuren";
            case "sounds" -> "Sound-Effekte";
            default -> categoryId;
        };
    }

    private String getTypeDisplayName(AdvancedCosmeticsSystem.CosmeticType type) {
        return switch (type) {
            case PARTICLE_EFFECT -> "Partikel-Effekt";
            case WINGS -> "Flügel";
            case HAT -> "Hut";
            case PET -> "Haustier";
            case EMOTE -> "Emote";
        };
    }

    public void setCategory(String category) {
        this.currentCategory = category;
        setupItems();
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
