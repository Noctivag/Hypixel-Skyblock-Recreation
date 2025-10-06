package de.noctivag.skyblock.pets;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Pet Items System - Hypixel Skyblock Style
 * 
 * Features:
 * - Equipable pet items
 * - Pet item effects and bonuses
 * - Pet item rarity system
 * - Pet item crafting recipes
 * - Pet item GUI interface
 */
public class PetItemsSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final PetSystem petSystem;
    private final Map<String, PetItem> petItems = new HashMap<>();
    private final Map<UUID, Map<String, PetItem>> equippedPetItems = new ConcurrentHashMap<>();
    
    public PetItemsSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform, PetSystem petSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        this.petSystem = petSystem;
        
        initializePetItems();
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializePetItems() {
        // Pet XP Boost Items
        petItems.put("XP_BOOST_1", new PetItem(
            "XP_BOOST_1", "XP Boost I", "§aXP Boost I", Material.EXPERIENCE_BOTTLE,
            PetItemRarity.COMMON, PetItemType.XP_BOOST,
            "§7Increases pet XP gain by 10%",
            Arrays.asList("§7+10% Pet XP Gain"),
            Arrays.asList("§7- 1x Experience Bottle", "§7- 1x Gold Ingot"),
            10.0, "XP_GAIN"
        ));
        
        petItems.put("XP_BOOST_2", new PetItem(
            "XP_BOOST_2", "XP Boost II", "§aXP Boost II", Material.EXPERIENCE_BOTTLE,
            PetItemRarity.UNCOMMON, PetItemType.XP_BOOST,
            "§7Increases pet XP gain by 25%",
            Arrays.asList("§7+25% Pet XP Gain"),
            Arrays.asList("§7- 1x XP Boost I", "§7- 1x Lapis Lazuli"),
            25.0, "XP_GAIN"
        ));
        
        petItems.put("XP_BOOST_3", new PetItem(
            "XP_BOOST_3", "XP Boost III", "§aXP Boost III", Material.EXPERIENCE_BOTTLE,
            PetItemRarity.RARE, PetItemType.XP_BOOST,
            "§7Increases pet XP gain by 50%",
            Arrays.asList("§7+50% Pet XP Gain"),
            Arrays.asList("§7- 1x XP Boost II", "§7- 1x Diamond"),
            50.0, "XP_GAIN"
        ));
        
        // Pet Stat Boost Items
        petItems.put("STRENGTH_BOOST", new PetItem(
            "STRENGTH_BOOST", "Strength Boost", "§cStrength Boost", Material.IRON_INGOT,
            PetItemRarity.UNCOMMON, PetItemType.STAT_BOOST,
            "§7Increases pet strength by 20%",
            Arrays.asList("§7+20% Pet Strength"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Redstone"),
            20.0, "STRENGTH"
        ));
        
        petItems.put("SPEED_BOOST", new PetItem(
            "SPEED_BOOST", "Speed Boost", "§aSpeed Boost", Material.FEATHER,
            PetItemRarity.UNCOMMON, PetItemType.STAT_BOOST,
            "§7Increases pet speed by 20%",
            Arrays.asList("§7+20% Pet Speed"),
            Arrays.asList("§7- 1x Feather", "§7- 1x Sugar"),
            20.0, "SPEED"
        ));
        
        petItems.put("LUCK_BOOST", new PetItem(
            "LUCK_BOOST", "Luck Boost", "§6Luck Boost", Material.GOLD_INGOT,
            PetItemRarity.RARE, PetItemType.STAT_BOOST,
            "§7Increases pet luck by 30%",
            Arrays.asList("§7+30% Pet Luck"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Emerald"),
            30.0, "LUCK"
        ));
        
        // Pet Ability Items
        petItems.put("ABILITY_BOOST", new PetItem(
            "ABILITY_BOOST", "Ability Boost", "§dAbility Boost", Material.NETHER_STAR,
            PetItemRarity.EPIC, PetItemType.ABILITY_BOOST,
            "§7Increases pet ability effectiveness by 40%",
            Arrays.asList("§7+40% Pet Ability Effectiveness"),
            Arrays.asList("§7- 1x Nether Star", "§7- 1x Ender Pearl"),
            40.0, "ABILITY_EFFECTIVENESS"
        ));
        
        petItems.put("COOLDOWN_REDUCTION", new PetItem(
            "COOLDOWN_REDUCTION", "Cooldown Reduction", "§bCooldown Reduction", Material.CLOCK,
            PetItemRarity.RARE, PetItemType.ABILITY_BOOST,
            "§7Reduces pet ability cooldowns by 25%",
            Arrays.asList("§7-25% Pet Ability Cooldowns"),
            Arrays.asList("§7- 1x Clock", "§7- 1x Redstone"),
            25.0, "COOLDOWN_REDUCTION"
        ));
        
        // Pet Special Items
        petItems.put("PET_SKIN", new PetItem(
            "PET_SKIN", "Pet Skin", "§ePet Skin", Material.PLAYER_HEAD,
            PetItemRarity.LEGENDARY, PetItemType.SPECIAL,
            "§7Changes your pet's appearance",
            Arrays.asList("§7Custom Pet Appearance"),
            Arrays.asList("§7- 1x Player Head", "§7- 1x Dye"),
            0.0, "APPEARANCE"
        ));
        
        petItems.put("PET_NAME_TAG", new PetItem(
            "PET_NAME_TAG", "Pet Name Tag", "§fPet Name Tag", Material.NAME_TAG,
            PetItemRarity.COMMON, PetItemType.SPECIAL,
            "§7Allows you to rename your pet",
            Arrays.asList("§7Custom Pet Name"),
            Arrays.asList("§7- 1x Name Tag", "§7- 1x Ink Sac"),
            0.0, "CUSTOM_NAME"
        ));
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.contains("Pet Items")) {
            event.setCancelled(true);
            handlePetItemsGUIClick(player, event.getSlot());
        }
    }
    
    public void openPetItemsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§e§lPet Items"));
        
        // Pet item categories
        addGUIItem(gui, 10, Material.EXPERIENCE_BOTTLE, "§a§lXP Boost Items", 
            Arrays.asList("§7Items that boost pet XP gain", "", "§eClick to view"));
        
        addGUIItem(gui, 11, Material.IRON_INGOT, "§c§lStat Boost Items", 
            Arrays.asList("§7Items that boost pet stats", "", "§eClick to view"));
        
        addGUIItem(gui, 12, Material.NETHER_STAR, "§d§lAbility Items", 
            Arrays.asList("§7Items that enhance pet abilities", "", "§eClick to view"));
        
        addGUIItem(gui, 13, Material.PLAYER_HEAD, "§e§lSpecial Items", 
            Arrays.asList("§7Special pet items and cosmetics", "", "§eClick to view"));
        
        // Equipped items
        Map<String, PetItem> equipped = equippedPetItems.getOrDefault(player.getUniqueId(), new HashMap<>());
        addGUIItem(gui, 19, Material.CHEST, "§6§lEquipped Items", 
            Arrays.asList("§7View your equipped pet items", "§7• " + equipped.size() + " items equipped", "", "§eClick to view"));
        
        // Crafting
        addGUIItem(gui, 20, Material.CRAFTING_TABLE, "§9§lCraft Pet Items", 
            Arrays.asList("§7Craft new pet items", "§7• View recipes", "§7• Create items", "", "§eClick to craft"));
        
        // Close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            Arrays.asList("§7Close pet items", "", "§eClick to close"));
        
        player.openInventory(gui);
    }
    
    public void openPetItemCategoryGUI(Player player, PetItemType type) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lPet Items - " + type.getDisplayName());
        
        List<PetItem> categoryItems = petItems.values().stream()
            .filter(item -> item.getType() == type)
            .toList();
        
        int slot = 10;
        for (PetItem item : categoryItems) {
            if (slot >= 44) break;
            
            ItemStack itemStack = createPetItemStack(item);
            gui.setItem(slot, itemStack);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to pet items", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public void openEquippedItemsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lEquipped Pet Items"));
        
        Map<String, PetItem> equipped = equippedPetItems.getOrDefault(player.getUniqueId(), new HashMap<>());
        
        int slot = 10;
        for (Map.Entry<String, PetItem> entry : equipped.entrySet()) {
            if (slot >= 44) break;
            
            ItemStack itemStack = createPetItemStack(entry.getValue());
            gui.setItem(slot, itemStack);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to pet items", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public boolean equipPetItem(Player player, Pet pet, PetItem petItem) {
        Map<String, PetItem> equipped = equippedPetItems.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        
        // Check if item is compatible with pet
        if (!isItemCompatibleWithPet(petItem, pet)) {
            player.sendMessage("§cThis item is not compatible with " + "Pet" + "!");
            return false;
        }
        
        // Check if player has the item
        if (!player.getInventory().contains(petItem.getMaterial())) {
            player.sendMessage(Component.text("§cYou don't have this item!"));
            return false;
        }
        
        // Equip the item
        equipped.put(petItem.getId(), petItem);
        
        // Remove item from inventory
        player.getInventory().removeItem(new ItemStack(petItem.getMaterial(), 1));
        
        player.sendMessage(Component.text("§a§lPET ITEM EQUIPPED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        player.sendMessage("§7Item: " + petItem.getDisplayName());
        
        return true;
    }
    
    public boolean unequipPetItem(Player player, Pet pet, PetItem petItem) {
        Map<String, PetItem> equipped = equippedPetItems.get(player.getUniqueId());
        if (equipped == null) return false;
        
        if (equipped.remove(petItem.getId()) != null) {
            // Return item to inventory
            player.getInventory().addItem(new ItemStack(petItem.getMaterial(), 1));
            
            player.sendMessage(Component.text("§c§lPET ITEM UNEQUIPPED!"));
            player.sendMessage("§7Pet: §e" + "Pet");
            player.sendMessage("§7Item: " + petItem.getDisplayName());
            
            return true;
        }
        
        return false;
    }
    
    private boolean isItemCompatibleWithPet(PetItem petItem, Pet pet) {
        // Check if the pet item is compatible with the pet type
        // This could be based on pet category, rarity, or specific pet types
        return true; // Simplified for now
    }
    
    public ItemStack createPetItemStack(PetItem petItem) {
        ItemStack item = new ItemStack(petItem.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(petItem.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Type: §ePet Item");
            lore.add("§7Rarity: " + petItem.getRarity().getDisplayName());
            lore.add("§7Category: " + petItem.getType().getDisplayName());
            lore.add("");
            lore.add("§7Description:");
            lore.add(petItem.getDescription());
            lore.add("");
            lore.add("§7Effects:");
            lore.addAll(petItem.getEffects());
            lore.add("");
            lore.add("§7Crafting:");
            lore.addAll(petItem.getCraftingMaterials());
            lore.add("");
            lore.add("§eRight-click on a pet to equip!");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private void handlePetItemsGUIClick(Player player, int slot) {
        switch (slot) {
            case 10: // XP Boost Items
                openPetItemCategoryGUI(player, PetItemType.XP_BOOST);
                break;
            case 11: // Stat Boost Items
                openPetItemCategoryGUI(player, PetItemType.STAT_BOOST);
                break;
            case 12: // Ability Items
                openPetItemCategoryGUI(player, PetItemType.ABILITY_BOOST);
                break;
            case 13: // Special Items
                openPetItemCategoryGUI(player, PetItemType.SPECIAL);
                break;
            case 19: // Equipped Items
                openEquippedItemsGUI(player);
                break;
            case 20: // Crafting
                player.sendMessage(Component.text("§ePet Item Crafting coming soon!"));
                break;
            case 49: // Close
                player.closeInventory();
                break;
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(description.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PetItem getPetItem(String itemId) {
        return petItems.get(itemId);
    }
    
    public Map<String, PetItem> getAllPetItems() {
        return new HashMap<>(petItems);
    }
    
    public Map<String, PetItem> getEquippedPetItems(UUID playerId) {
        return equippedPetItems.getOrDefault(playerId, new HashMap<>());
    }
    
    // Pet Item Classes
    public static class PetItem {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final PetItemRarity rarity;
        private final PetItemType type;
        private final String description;
        private final List<String> effects;
        private final List<String> craftingMaterials;
        private final double value;
        private final String effectType;
        
        public PetItem(String id, String name, String displayName, Material material, PetItemRarity rarity,
                      PetItemType type, String description, List<String> effects, List<String> craftingMaterials,
                      double value, String effectType) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.rarity = rarity;
            this.type = type;
            this.description = description;
            this.effects = effects;
            this.craftingMaterials = craftingMaterials;
            this.value = value;
            this.effectType = effectType;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public PetItemRarity getRarity() { return rarity; }
        public PetItemType getType() { return type; }
        public String getDescription() { return description; }
        public List<String> getEffects() { return effects; }
        public List<String> getCraftingMaterials() { return craftingMaterials; }
        public double getValue() { return value; }
        public String getEffectType() { return effectType; }
    }
    
    public enum PetItemRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        PetItemRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public enum PetItemType {
        XP_BOOST("§aXP Boost", "Items that boost pet XP gain"),
        STAT_BOOST("§cStat Boost", "Items that boost pet statistics"),
        ABILITY_BOOST("§dAbility Boost", "Items that enhance pet abilities"),
        SPECIAL("§eSpecial", "Special pet items and cosmetics");
        
        private final String displayName;
        private final String description;
        
        PetItemType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
}
