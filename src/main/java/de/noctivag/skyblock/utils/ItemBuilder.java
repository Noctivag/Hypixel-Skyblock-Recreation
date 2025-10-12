package de.noctivag.skyblock.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Advanced ItemBuilder utility for creating complex ItemStacks with fluent API
 * Supports Hypixel Skyblock-style items with custom colors, enchantments, and NBT data
 */
public class ItemBuilder {
    
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private final Plugin plugin;
    
    // Color patterns for automatic color parsing
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    
    // Hypixel Skyblock color constants
    public static final String HYPIXEL_GREEN = "&a";
    public static final String HYPIXEL_RED = "&c";
    public static final String HYPIXEL_YELLOW = "&e";
    public static final String HYPIXEL_BLUE = "&9";
    public static final String HYPIXEL_PURPLE = "&5";
    public static final String HYPIXEL_GOLD = "&6";
    public static final String HYPIXEL_GRAY = "&7";
    public static final String HYPIXEL_DARK_GRAY = "&8";
    public static final String HYPIXEL_WHITE = "&f";
    public static final String HYPIXEL_BLACK = "&0";
    
    public ItemBuilder(Material material) {
        this(material, 1);
    }
    
    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = itemStack.getItemMeta();
        this.plugin = null;
    }
    
    public ItemBuilder(Material material, Plugin plugin) {
        this(material, 1, plugin);
    }
    
    public ItemBuilder(Material material, int amount, Plugin plugin) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = itemStack.getItemMeta();
        this.plugin = plugin;
    }
    
    /**
     * Set the display name of the item
     */
    public ItemBuilder name(String name) {
        if (itemMeta != null) {
            itemMeta.displayName(Component.text(parseColors(name)));
        }
        return this;
    }
    
    /**
     * Set the lore of the item
     */
    public ItemBuilder lore(String... lore) {
        if (itemMeta != null) {
            List<Component> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(Component.text(parseColors(line)));
            }
            itemMeta.lore(loreList);
        }
        return this;
    }
    
    /**
     * Set the lore of the item from a list
     */
    public ItemBuilder lore(List<String> lore) {
        if (itemMeta != null) {
            List<Component> loreList = lore.stream()
                .map(line -> Component.text(parseColors(line)))
                .collect(Collectors.toList());
            itemMeta.lore(loreList);
        }
        return this;
    }
    
    /**
     * Add a line to the lore
     */
    public ItemBuilder addLore(String line) {
        if (itemMeta != null) {
            List<Component> lore = itemMeta.lore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.add(Component.text(parseColors(line)));
            itemMeta.lore(lore);
        }
        return this;
    }
    
    /**
     * Add multiple lines to the lore
     */
    public ItemBuilder addLore(String... lines) {
        if (itemMeta != null) {
            List<Component> lore = itemMeta.lore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            for (String line : lines) {
                lore.add(Component.text(parseColors(line)));
            }
            itemMeta.lore(lore);
        }
        return this;
    }
    
    /**
     * Add an enchantment to the item
     */
    public ItemBuilder enchant(Enchantment enchantment, int level) {
        if (itemMeta != null) {
            itemMeta.addEnchant(enchantment, level, true);
        }
        return this;
    }
    
    /**
     * Add multiple enchantments to the item
     */
    public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
        if (itemMeta != null) {
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
        return this;
    }
    
    /**
     * Add enchantment glow effect without actual enchantment
     */
    public ItemBuilder glow() {
        if (itemMeta != null) {
            itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }
    
    /**
     * Set the amount of the item
     */
    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }
    
    /**
     * Set the durability of the item
     */
    public ItemBuilder durability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }
    
    /**
     * Set custom model data
     */
    public ItemBuilder customModelData(int customModelData) {
        if (itemMeta != null) {
            itemMeta.setCustomModelData(customModelData);
        }
        return this;
    }
    
    /**
     * Set the item as unbreakable
     */
    public ItemBuilder unbreakable(boolean unbreakable) {
        if (itemMeta != null) {
            itemMeta.setUnbreakable(unbreakable);
        }
        return this;
    }
    
    /**
     * Hide item flags
     */
    public ItemBuilder hideFlags(ItemFlag... flags) {
        if (itemMeta != null) {
            itemMeta.addItemFlags(flags);
        }
        return this;
    }
    
    /**
     * Hide all item flags
     */
    public ItemBuilder hideAllFlags() {
        if (itemMeta != null) {
            itemMeta.addItemFlags(ItemFlag.values());
        }
        return this;
    }
    
    /**
     * Set the color of leather armor
     */
    public ItemBuilder color(Color color) {
        if (itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemMeta).setColor(color);
        }
        return this;
    }
    
    /**
     * Set the color of leather armor from RGB values
     */
    public ItemBuilder color(int red, int green, int blue) {
        return color(Color.fromRGB(red, green, blue));
    }
    
    /**
     * Set the skull owner for player heads
     */
    public ItemBuilder skull(String owner) {
        if (itemMeta instanceof SkullMeta) {
            ((SkullMeta) itemMeta).setOwner(owner);
        }
        return this;
    }
    
    /**
     * Add persistent data to the item
     */
    public ItemBuilder persistentData(String key, String value) {
        if (itemMeta != null && plugin != null) {
            NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
        }
        return this;
    }
    
    /**
     * Add persistent data to the item with integer value
     */
    public ItemBuilder persistentData(String key, int value) {
        if (itemMeta != null && plugin != null) {
            NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, value);
        }
        return this;
    }
    
    /**
     * Add persistent data to the item with boolean value
     */
    public ItemBuilder persistentData(String key, boolean value) {
        if (itemMeta != null && plugin != null) {
            NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, value);
        }
        return this;
    }
    
    /**
     * Create a Hypixel-style item with common formatting
     */
    public ItemBuilder hypixelStyle(String name, String... lore) {
        return name(HYPIXEL_GREEN + name)
               .lore(lore)
               .glow();
    }
    
    /**
     * Create a common item with Hypixel formatting
     */
    public ItemBuilder common(String name, String... lore) {
        return name(HYPIXEL_WHITE + name)
               .lore(lore);
    }
    
    /**
     * Create an uncommon item with Hypixel formatting
     */
    public ItemBuilder uncommon(String name, String... lore) {
        return name(HYPIXEL_GREEN + name)
               .lore(lore);
    }
    
    /**
     * Create a rare item with Hypixel formatting
     */
    public ItemBuilder rare(String name, String... lore) {
        return name(HYPIXEL_BLUE + name)
               .lore(lore);
    }
    
    /**
     * Create an epic item with Hypixel formatting
     */
    public ItemBuilder epic(String name, String... lore) {
        return name(HYPIXEL_PURPLE + name)
               .lore(lore);
    }
    
    /**
     * Create a legendary item with Hypixel formatting
     */
    public ItemBuilder legendary(String name, String... lore) {
        return name(HYPIXEL_GOLD + name)
               .lore(lore);
    }
    
    /**
     * Create a mythic item with Hypixel formatting
     */
    public ItemBuilder mythic(String name, String... lore) {
        return name(HYPIXEL_RED + name)
               .lore(lore);
    }
    
    /**
     * Create a special item with Hypixel formatting
     */
    public ItemBuilder special(String name, String... lore) {
        return name(HYPIXEL_YELLOW + name)
               .lore(lore)
               .glow();
    }
    
    /**
     * Build the final ItemStack
     */
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    /**
     * Parse color codes in text
     */
    private String parseColors(String text) {
        if (text == null) return null;
        
        // Parse hex colors
        Matcher hexMatcher = HEX_PATTERN.matcher(text);
        while (hexMatcher.find()) {
            String hex = hexMatcher.group(1);
            text = text.replace(hexMatcher.group(0), net.md_5.bungee.api.ChatColor.of("#" + hex).toString());
        }
        
        // Parse standard color codes
        text = text.replace("&", "§");
        
        return text;
    }
    
    /**
     * Create a new ItemBuilder instance
     */
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }
    
    /**
     * Create a new ItemBuilder instance with amount
     */
    public static ItemBuilder of(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }
    
    /**
     * Create a new ItemBuilder instance with plugin
     */
    public static ItemBuilder of(Material material, Plugin plugin) {
        return new ItemBuilder(material, plugin);
    }
    
    /**
     * Create a new ItemBuilder instance with amount and plugin
     */
    public static ItemBuilder of(Material material, int amount, Plugin plugin) {
        return new ItemBuilder(material, amount, plugin);
    }
    
    /**
     * Create a border item for GUI frames
     */
    public static ItemBuilder border() {
        return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name(" ");
    }
    
    /**
     * Create a filler item for empty GUI slots
     */
    public static ItemBuilder filler() {
        return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ");
    }
    
    /**
     * Create a close button for GUIs
     */
    public static ItemBuilder closeButton() {
        return new ItemBuilder(Material.BARRIER)
                .name(HYPIXEL_RED + "Schließen")
                .lore(HYPIXEL_GRAY + "Klicke zum Schließen");
    }
    
    /**
     * Create a back button for GUIs
     */
    public static ItemBuilder backButton() {
        return new ItemBuilder(Material.ARROW)
                .name(HYPIXEL_YELLOW + "Zurück")
                .lore(HYPIXEL_GRAY + "Klicke zum Zurückgehen");
    }
    
    /**
     * Create a next button for GUIs
     */
    public static ItemBuilder nextButton() {
        return new ItemBuilder(Material.ARROW)
                .name(HYPIXEL_YELLOW + "Weiter")
                .lore(HYPIXEL_GRAY + "Klicke zum Weitergehen");
    }
}
