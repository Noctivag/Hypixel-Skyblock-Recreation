package de.noctivag.plugin.kit;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KitShop {
    private final Plugin plugin;
    private final Map<String, KitInfo> kitShopItems;

    public KitShop(Plugin plugin) {
        this.plugin = plugin;
        this.kitShopItems = new HashMap<>();
        initializeKits();
    }

    private void initializeKits() {
        // Starter Kit - Free for everyone
        kitShopItems.put("starter", new KitInfo(
            "Starter",
            0,
            1800, // 30 minutes Cooldown
            Material.WOODEN_SWORD,
            "§7Ein Grundausrüstungs-Kit für neue Spieler",
            "§a✓ Kostenlos verfügbar",
            Arrays.asList(
                new ItemStack(Material.WOODEN_SWORD),
                new ItemStack(Material.LEATHER_CHESTPLATE),
                new ItemStack(Material.BREAD, 16),
                new ItemStack(Material.TORCH, 32)
            )
        ));

        // PVP Kit - Free with longer cooldown
        kitShopItems.put("pvp", new KitInfo(
            "PVP",
            0,
            5400, // 1.5 hours Cooldown
            Material.IRON_SWORD,
            "§7Ein Kit für PVP-Kämpfer",
            "§c⚔ Kampf-Ausrüstung",
            Arrays.asList(
                createEnchantedItem(Material.IRON_SWORD, Enchantment.SHARPNESS, 2),
                createEnchantedItem(Material.IRON_CHESTPLATE, Enchantment.PROTECTION, 2),
                createEnchantedItem(Material.IRON_LEGGINGS, Enchantment.PROTECTION, 1),
                createEnchantedItem(Material.IRON_BOOTS, Enchantment.PROTECTION, 1),
                new ItemStack(Material.GOLDEN_APPLE, 3),
                new ItemStack(Material.COOKED_BEEF, 16)
            )
        ));

        // Mining Kit - Free with moderate cooldown
        kitShopItems.put("mining", new KitInfo(
            "Mining",
            0,
            3600, // 1 hour Cooldown
            Material.DIAMOND_PICKAXE,
            "§7Perfekt zum Abbauen von Ressourcen",
            "§e⛏ Bergbau-Ausrüstung",
            Arrays.asList(
                createEnchantedItem(Material.IRON_PICKAXE, Enchantment.EFFICIENCY, 3),
                createEnchantedItem(Material.IRON_PICKAXE, Enchantment.FORTUNE, 2),
                new ItemStack(Material.TORCH, 64),
                new ItemStack(Material.COOKED_BEEF, 16),
                new ItemStack(Material.WATER_BUCKET, 1)
            )
        ));

        // Builder Kit - New free kit
        kitShopItems.put("builder", new KitInfo(
            "Builder",
            0,
            2700, // 45 minutes Cooldown
            Material.GOLDEN_AXE,
            "§7Ideal für Baumeister und Architekten",
            "§6🏗 Bau-Ausrüstung",
            Arrays.asList(
                createEnchantedItem(Material.GOLDEN_AXE, Enchantment.EFFICIENCY, 5),
                new ItemStack(Material.COBBLESTONE, 64),
                new ItemStack(Material.OAK_PLANKS, 64),
                new ItemStack(Material.GLASS, 32),
                new ItemStack(Material.TORCH, 64),
                new ItemStack(Material.LADDER, 16)
            )
        ));

        // Explorer Kit - New free kit
        kitShopItems.put("explorer", new KitInfo(
            "Explorer",
            0,
            4500, // 1.25 hours Cooldown
            Material.COMPASS,
            "§7Für Abenteurer und Entdecker",
            "§b🧭 Entdecker-Ausrüstung",
            Arrays.asList(
                new ItemStack(Material.COMPASS, 1),
                new ItemStack(Material.MAP, 3),
                createEnchantedItem(Material.LEATHER_BOOTS, Enchantment.FEATHER_FALLING, 3),
                new ItemStack(Material.OAK_BOAT, 1),
                new ItemStack(Material.COOKED_BEEF, 32),
                new ItemStack(Material.TORCH, 64),
                new ItemStack(Material.ENDER_PEARL, 2)
            )
        ));

        // VIP Kit - Free for VIPs with longer cooldown
        kitShopItems.put("vip", new KitInfo(
            "VIP",
            0,
            21600, // 6 hours Cooldown
            Material.DIAMOND_SWORD,
            "§6Exklusiv für VIP-Spieler",
            "§6★ VIP Exklusiv",
            Arrays.asList(
                createEnchantedItem(Material.DIAMOND_SWORD, Enchantment.SHARPNESS, 3),
                createEnchantedItem(Material.DIAMOND_CHESTPLATE, Enchantment.PROTECTION, 3),
                createEnchantedItem(Material.DIAMOND_LEGGINGS, Enchantment.PROTECTION, 2),
                createEnchantedItem(Material.DIAMOND_BOOTS, Enchantment.PROTECTION, 2),
                new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2),
                new ItemStack(Material.ENDER_PEARL, 4),
                createEnchantedItem(Material.BOW, Enchantment.POWER, 3),
                new ItemStack(Material.ARROW, 64)
            )
        ));
    }

    private ItemStack createEnchantedItem(Material material, Enchantment enchantment, int level) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public KitInfo getKitInfo(String kitName) {
        return kitShopItems.get(kitName.toLowerCase());
    }

    public Set<String> getAvailableKits() {
        return kitShopItems.keySet();
    }

    public static class KitInfo {
        private final String name;
        private final int price;
        private final long cooldown;
        private final Material icon;
        private final String description;
        private final String subtitle;
        private final List<ItemStack> items;

        public KitInfo(String name, int price, long cooldown, Material icon, String description, String subtitle, List<ItemStack> items) {
            this.name = name;
            this.price = price;
            this.cooldown = cooldown;
            this.icon = icon;
            this.description = description;
            this.subtitle = subtitle;
            this.items = items;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public long getCooldown() {
            return cooldown;
        }

        public Material getIcon() {
            return icon;
        }

        public String getDescription() {
            return description;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public List<ItemStack> getItems() {
            return items;
        }
    }
}
