package de.noctivag.skyblock.items;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pet Item System - Hypixel Skyblock Style
 * 
 * Features:
 * - Pet Items (Pet Candy, Pet Food, Pet Upgrades)
 * - Pet Stats Bonuses
 * - Pet Abilities
 * - Pet Evolution
 * - Pet Skins
 * - Pet Accessories
 */
public class PetItemSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerPetItems> playerPetItems = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> petItemTasks = new ConcurrentHashMap<>();
    
    public PetItemSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        startPetItemUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void startPetItemUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerPetItems> entry : playerPetItems.entrySet()) {
                    PlayerPetItems petItems = entry.getValue();
                    petItems.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Pet") && (displayName.contains("Candy") || displayName.contains("Food") || displayName.contains("Upgrade"))) {
            openPetItemGUI(player);
        }
    }
    
    private void openPetItemGUI(Player player) {
        player.sendMessage(Component.text("§aPet Item GUI geöffnet!"));
    }
    
    public boolean applyPetItem(Player player, PetItemType itemType) {
        // Check if player has the pet item
        if (!hasPetItem(player, itemType)) {
            player.sendMessage("§cDu hast kein " + itemType.getDisplayName() + "!");
            return false;
        }
        
        // Apply pet item effects
        PetItemConfig config = getPetItemConfig(itemType);
        if (config == null) {
            player.sendMessage(Component.text("§cPet Item Konfiguration nicht gefunden!"));
            return false;
        }
        
        // Remove pet item from player
        removePetItem(player, itemType);
        
        // Update player stats
        PlayerPetItems playerPetItems = getPlayerPetItems(player.getUniqueId());
        playerPetItems.addPetItem(itemType);
        
        player.sendMessage("§a" + config.getDisplayName() + " erfolgreich angewendet!");
        player.sendMessage("§7Effekt: " + config.getDescription());
        
        // Update database
        databaseManager.executeUpdate("""
            INSERT INTO player_pet_items (uuid, pet_item_type, success, timestamp)
            VALUES (?, ?, true, NOW())
        """, player.getUniqueId(), itemType.name());
        
        return true;
    }
    
    private boolean hasPetItem(Player player, PetItemType itemType) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().getDisplayName().contains(itemType.getDisplayName())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void removePetItem(Player player, PetItemType itemType) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().getDisplayName().contains(itemType.getDisplayName())) {
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.getInventory().remove(item);
                    }
                    return;
                }
            }
        }
    }
    
    private PetItemConfig getPetItemConfig(PetItemType itemType) {
        return switch (itemType) {
            case PET_CANDY -> new PetItemConfig(
                "Pet Candy", "§dPet Candy", Material.SUGAR,
                "§7Increases pet experience gain by 25%",
                PetItemCategory.EXPERIENCE, 100, Arrays.asList("§7+25% Pet XP Gain")
            );
            case PET_FOOD -> new PetItemConfig(
                "Pet Food", "§6Pet Food", Material.BREAD,
                "§7Increases pet stats by 10%",
                PetItemCategory.STATS, 200, Arrays.asList("§7+10% Pet Stats")
            );
            case PET_UPGRADE -> new PetItemConfig(
                "Pet Upgrade", "§bPet Upgrade", Material.ENCHANTED_BOOK,
                "§7Upgrades pet rarity by one level",
                PetItemCategory.UPGRADE, 500, Arrays.asList("§7+1 Pet Rarity Level")
            );
            case PET_SKIN -> new PetItemConfig(
                "Pet Skin", "§ePet Skin", Material.LEATHER,
                "§7Changes pet appearance",
                PetItemCategory.COSMETIC, 50, Arrays.asList("§7Custom Pet Skin")
            );
            case PET_ACCESSORY -> new PetItemConfig(
                "Pet Accessory", "§cPet Accessory", Material.GOLD_NUGGET,
                "§7Adds special pet abilities",
                PetItemCategory.ABILITY, 300, Arrays.asList("§7Special Pet Ability")
            );
        };
    }
    
    public ItemStack createPetItem(PetItemType itemType) {
        PetItemConfig config = getPetItemConfig(itemType);
        if (config == null) return null;
        
        ItemStack item = new ItemStack(config.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(Component.text(config.getDisplayName()));
        List<String> lore = new ArrayList<>();
        lore.add(config.getDescription());
        lore.add("");
        lore.addAll(config.getEffects());
        lore.add("");
        lore.add("§7Category: " + config.getCategory().getColor() + config.getCategory().getName());
        lore.add("§7Value: §a" + config.getValue() + " coins");
        lore.add("");
        lore.add("§6Right-click to use!");
        
        meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
        
        return item;
    }
    
    public PlayerPetItems getPlayerPetItems(UUID playerId) {
        return playerPetItems.computeIfAbsent(playerId, k -> new PlayerPetItems(playerId));
    }
    
    public enum PetItemType {
        PET_CANDY, PET_FOOD, PET_UPGRADE, PET_SKIN, PET_ACCESSORY;
        
        public String getDisplayName() {
            return switch (this) {
                case PET_CANDY -> "§dPet Candy";
                case PET_FOOD -> "§6Pet Food";
                case PET_UPGRADE -> "§bPet Upgrade";
                case PET_SKIN -> "§ePet Skin";
                case PET_ACCESSORY -> "§cPet Accessory";
            };
        }
    }
    
    public enum PetItemCategory {
        EXPERIENCE("§aExperience", "§7Items that boost pet experience"),
        STATS("§cStats", "§7Items that boost pet stats"),
        UPGRADE("§bUpgrade", "§7Items that upgrade pet rarity"),
        COSMETIC("§eCosmetic", "§7Items that change pet appearance"),
        ABILITY("§dAbility", "§7Items that add pet abilities");
        
        private final String name;
        private final String description;
        
        PetItemCategory(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getColor() { return name.substring(0, 2); }
    }
    
    public static class PetItemConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final PetItemCategory category;
        private final int value;
        private final List<String> effects;
        
        public PetItemConfig(String name, String displayName, Material material, String description,
                           PetItemCategory category, int value, List<String> effects) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.category = category;
            this.value = value;
            this.effects = effects;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public PetItemCategory getCategory() { return category; }
        public int getValue() { return value; }
        public List<String> getEffects() { return effects; }
    }
    
    public static class PlayerPetItems {
        private final UUID playerId;
        private final Map<PetItemType, Integer> petItemCounts = new ConcurrentHashMap<>();
        private int totalPetItems = 0;
        private long lastUpdate;
        
        public PlayerPetItems(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = java.lang.System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save pet item data to database
        }
        
        public void addPetItem(PetItemType itemType) {
            petItemCounts.put(itemType, petItemCounts.getOrDefault(itemType, 0) + 1);
            totalPetItems++;
        }
        
        public int getPetItemCount(PetItemType itemType) {
            return petItemCounts.getOrDefault(itemType, 0);
        }
        
        public int getTotalPetItems() {
            return totalPetItems;
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<PetItemType, Integer> getPetItemCounts() { return petItemCounts; }
    }
}
