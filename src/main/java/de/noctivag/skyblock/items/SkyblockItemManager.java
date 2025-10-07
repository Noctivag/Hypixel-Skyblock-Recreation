package de.noctivag.skyblock.items;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Skyblock Item Manager - Verwaltet alle Items aus dem Plugin
 */
public class SkyblockItemManager {
    
    private final SkyblockPlugin plugin;
    
    public SkyblockItemManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Erstellt ein Skyblock Item
     */
    public ItemStack createSkyblockItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(lore.stream().map(net.kyori.adventure.text.Component::text).toList());
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Erstellt ein verzaubertes Skyblock Item
     */
    public ItemStack createEnchantedSkyblockItem(Material material, String name, List<String> lore, 
                                               Enchantment enchantment, int level) {
        ItemStack item = createSkyblockItem(material, name, lore);
        item.addUnsafeEnchantment(enchantment, level);
        return item;
    }
    
    /**
     * Gibt alle verfügbaren Skyblock Items zurück
     */
    public List<ItemStack> getAllSkyblockItems() {
        List<ItemStack> items = new ArrayList<>();
        
        // Weapons
        items.addAll(getWeapons());
        
        // Armor
        items.addAll(getArmor());
        
        // Tools
        items.addAll(getTools());
        
        // Accessories
        items.addAll(getAccessories());
        
        // Special Items
        items.addAll(getSpecialItems());
        
        // Minion Items
        items.addAll(getMinionItems());
        
        // Pet Items
        items.addAll(getPetItems());
        
        // Enchanted Items
        items.addAll(getEnchantedItems());
        
        return items;
    }
    
    /**
     * Gibt alle Waffen zurück
     */
    public List<ItemStack> getWeapons() {
        List<ItemStack> weapons = new ArrayList<>();
        
        // Skyblock Sword
        weapons.add(createSkyblockItem(
            Material.DIAMOND_SWORD,
            "§a§lSkyblock Sword",
            Arrays.asList(
                "§7Das ultimative Schwert",
                "§eDamage: §c100",
                "§eStrength: §c50"
            )
        ));
        
        // Aspect of the End
        weapons.add(createEnchantedSkyblockItem(
            Material.DIAMOND_SWORD,
            "§d§lAspect of the End",
            Arrays.asList(
                "§7Teleportiert dich 8 Blöcke",
                "§eDamage: §c100",
                "§eStrength: §c100",
                "§eIntelligence: §b50"
            ),
            Enchantment.SHARPNESS, 5
        ));
        
        return weapons;
    }
    
    /**
     * Gibt alle Rüstungen zurück
     */
    public List<ItemStack> getArmor() {
        List<ItemStack> armor = new ArrayList<>();
        
        // Dragon Helmet
        armor.add(createSkyblockItem(
            Material.DRAGON_HEAD,
            "§5§lDragon Helmet",
            Arrays.asList(
                "§7Teil des Dragon Sets",
                "§eHealth: §c+100",
                "§eDefense: §a+50"
            )
        ));
        
        return armor;
    }
    
    /**
     * Gibt alle Werkzeuge zurück
     */
    public List<ItemStack> getTools() {
        List<ItemStack> tools = new ArrayList<>();
        
        // Treecapitator
        tools.add(createEnchantedSkyblockItem(
            Material.DIAMOND_AXE,
            "§a§lTreecapitator",
            Arrays.asList(
                "§7Fällt ganze Bäume",
                "§eEfficiency: §bX",
                "§eUnbreaking: §bIII"
            ),
            Enchantment.EFFICIENCY, 10
        ));
        
        return tools;
    }
    
    /**
     * Gibt alle Accessoires zurück
     */
    public List<ItemStack> getAccessories() {
        List<ItemStack> accessories = new ArrayList<>();
        
        // Talisman of Coins
        accessories.add(createSkyblockItem(
            Material.GOLD_INGOT,
            "§6§lTalisman of Coins",
            Arrays.asList(
                "§7Erhöht deine Münzen",
                "§eCoin Multiplier: §6+10%"
            )
        ));
        
        return accessories;
    }
    
    /**
     * Gibt alle speziellen Items zurück
     */
    public List<ItemStack> getSpecialItems() {
        List<ItemStack> specialItems = new ArrayList<>();
        
        // Booster Cookie
        specialItems.add(createSkyblockItem(
            Material.COOKIE,
            "§d§lBooster Cookie",
            Arrays.asList(
                "§7Gibt dir verschiedene Boni",
                "§eDuration: §a4 Tage",
                "§eEffects: §bMultiple"
            )
        ));
        
        return specialItems;
    }
    
    /**
     * Gibt alle Minion Items zurück
     */
    public List<ItemStack> getMinionItems() {
        List<ItemStack> minionItems = new ArrayList<>();
        
        // Minion Upgrade
        minionItems.add(createSkyblockItem(
            Material.EMERALD,
            "§a§lMinion Upgrade",
            Arrays.asList(
                "§7Upgraded deine Minions",
                "§eSpeed: §b+25%",
                "§eStorage: §b+50%"
            )
        ));
        
        return minionItems;
    }
    
    /**
     * Gibt alle Pet Items zurück
     */
    public List<ItemStack> getPetItems() {
        List<ItemStack> petItems = new ArrayList<>();
        
        // Pet Food
        petItems.add(createSkyblockItem(
            Material.BONE,
            "§e§lPet Food",
            Arrays.asList(
                "§7Füttert deine Pets",
                "§eXP Boost: §b+25%",
                "§eDuration: §a1h"
            )
        ));
        
        return petItems;
    }
    
    /**
     * Gibt alle verzauberten Items zurück
     */
    public List<ItemStack> getEnchantedItems() {
        List<ItemStack> enchantedItems = new ArrayList<>();
        
        // Enchanted Diamond
        enchantedItems.add(createSkyblockItem(
            Material.DIAMOND,
            "§b§lEnchanted Diamond",
            Arrays.asList(
                "§7Verzauberter Diamant",
                "§eAmount: §b160",
                "§eUsed for: §aCrafting"
            )
        ));
        
        return enchantedItems;
    }
}
