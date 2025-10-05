package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.CustomItemType;
import de.noctivag.skyblock.enums.Rarity;
import de.noctivag.skyblock.models.CustomItem;
import de.noctivag.skyblock.models.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service für die Verwaltung von Custom Items
 */
public class CustomItemService {

    private final SkyblockPluginRefactored plugin;

    public CustomItemService(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
    }

    /**
     * Erstellt ein Custom Item
     */
    public CompletableFuture<CustomItem> createCustomItem(CustomItemType itemType) {
        return CompletableFuture.supplyAsync(() -> {
            CustomItem item = new CustomItem(itemType);
            
            if (plugin.getSettingsConfig().isDebugMode()) {
                plugin.getLogger().info("Created custom item: " + itemType.getDisplayName());
            }
            
            return item;
        });
    }

    /**
     * Gibt ein Custom Item an einen Spieler
     */
    public CompletableFuture<Boolean> giveCustomItem(Player player, CustomItemType itemType) {
        return createCustomItem(itemType).thenApply(item -> {
            try {
                ItemStack itemStack = item.toItemStack();
                player.getInventory().addItem(itemStack);
                
                player.sendMessage("§aDu hast " + itemType.getDisplayName() + " erhalten!");
                
                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Gave " + itemType.getDisplayName() + " to player " + player.getName());
                }
                
                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error giving custom item to player: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Berechnet die Gesamt-Stats eines Spielers basierend auf seiner Ausrüstung
     */
    public Map<String, Double> calculatePlayerStats(Player player) {
        Map<String, Double> totalStats = new HashMap<>();
        
        // Base stats
        totalStats.put("health", 100.0);
        totalStats.put("defense", 0.0);
        totalStats.put("strength", 0.0);
        totalStats.put("crit_damage", 50.0);
        totalStats.put("crit_chance", 30.0);
        totalStats.put("intelligence", 0.0);
        totalStats.put("speed", 100.0);
        
        // Add stats from equipped items
        ItemStack[] armor = player.getInventory().getArmorContents();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        
        // Check armor
        for (ItemStack armorPiece : armor) {
            if (armorPiece != null && armorPiece.hasItemMeta()) {
                CustomItem customItem = getCustomItemFromItemStack(armorPiece);
                if (customItem != null) {
                    addStatsToTotal(totalStats, customItem.getStats());
                }
            }
        }
        
        // Check main hand weapon
        if (mainHand != null && mainHand.hasItemMeta()) {
            CustomItem customItem = getCustomItemFromItemStack(mainHand);
            if (customItem != null) {
                addStatsToTotal(totalStats, customItem.getStats());
            }
        }
        
        // Add pet stats
        PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        if (playerProfileService != null) {
            PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
            if (profile != null && profile.getPetBag() != null) {
                Map<String, Double> petStats = profile.getPetBag().getTotalStatBonuses();
                addStatsToTotal(totalStats, petStats);
            }
        }
        
        return totalStats;
    }

    /**
     * Fügt Stats zu den Gesamt-Stats hinzu
     */
    private void addStatsToTotal(Map<String, Double> totalStats, Map<String, Double> itemStats) {
        for (Map.Entry<String, Double> entry : itemStats.entrySet()) {
            String statKey = entry.getKey();
            double value = entry.getValue();
            totalStats.put(statKey, totalStats.getOrDefault(statKey, 0.0) + value);
        }
    }

    /**
     * Versucht ein Custom Item aus einem ItemStack zu extrahieren
     */
    private CustomItem getCustomItemFromItemStack(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }
        
        String displayName = itemStack.getItemMeta().getDisplayName();
        if (displayName == null) {
            return null;
        }
        
        // Find matching CustomItemType
        for (CustomItemType itemType : CustomItemType.values()) {
            if (displayName.equals(itemType.getDisplayName())) {
                return new CustomItem(itemType);
            }
        }
        
        return null;
    }

    /**
     * Prüft ob ein Spieler ein vollständiges Armor Set trägt
     */
    public boolean hasCompleteArmorSet(Player player, String setName) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        int setPieces = 0;
        
        for (ItemStack armorPiece : armor) {
            if (armorPiece != null && armorPiece.hasItemMeta()) {
                String displayName = armorPiece.getItemMeta().getDisplayName();
                if (displayName != null && displayName.contains(setName)) {
                    setPieces++;
                }
            }
        }
        
        return setPieces == 4; // Full set
    }

    /**
     * Gibt die Armor Set Boni zurück
     */
    public Map<String, Double> getArmorSetBonuses(Player player) {
        Map<String, Double> bonuses = new HashMap<>();
        
        // Shadow Assassin Set Bonus
        if (hasCompleteArmorSet(player, "Shadow Assassin")) {
            bonuses.put("crit_damage", 50.0);
            bonuses.put("intelligence", 100.0);
            bonuses.put("speed", 20.0);
        }
        
        // Necron Set Bonus
        if (hasCompleteArmorSet(player, "Necron")) {
            bonuses.put("strength", 80.0);
            bonuses.put("crit_damage", 60.0);
            bonuses.put("health", 100.0);
        }
        
        // Superior Dragon Set Bonus
        if (hasCompleteArmorSet(player, "Superior Dragon")) {
            bonuses.put("strength", 25.0);
            bonuses.put("crit_damage", 30.0);
            bonuses.put("crit_chance", 5.0);
            bonuses.put("intelligence", 50.0);
        }
        
        return bonuses;
    }

    /**
     * Aktiviert die Fähigkeit eines Items
     */
    public CompletableFuture<Boolean> activateItemAbility(Player player, CustomItemType itemType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                switch (itemType) {
                    case HYPERION:
                        return activateHyperionAbility(player);
                    case SCYLLA:
                        return activateScyllaAbility(player);
                    case VALKYRIE:
                        return activateValkyrieAbility(player);
                    case ASTRAEA:
                        return activateAstraeaAbility(player);
                    case SPIRIT_SCEPTRE:
                        return activateSpiritSceptreAbility(player);
                    case TERMINATOR:
                        return activateTerminatorAbility(player);
                    default:
                        return false;
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Error activating item ability: " + e.getMessage());
                return false;
            }
        });
    }

    private boolean activateHyperionAbility(Player player) {
        // Implosion ability - damage nearby enemies
        player.sendMessage("§6[HYPERION] §eImplosion aktiviert!");
        // TODO: Implement actual implosion effect
        return true;
    }

    private boolean activateScyllaAbility(Player player) {
        // Healing circle ability
        player.sendMessage("§6[SCYLLA] §aHeilkreis aktiviert!");
        // TODO: Implement healing circle effect
        return true;
    }

    private boolean activateValkyrieAbility(Player player) {
        // Shadow fury ability
        player.sendMessage("§6[VALKYRIE] §5Schatten-Furie aktiviert!");
        // TODO: Implement shadow fury effect
        return true;
    }

    private boolean activateAstraeaAbility(Player player) {
        // Lightning ability
        player.sendMessage("§6[ASTRAEA] §bBlitz aktiviert!");
        // TODO: Implement lightning effect
        return true;
    }

    private boolean activateSpiritSceptreAbility(Player player) {
        // Bat projectiles ability
        player.sendMessage("§5[SPIRIT SCEPTRE] §dBat-Projektile aktiviert!");
        // TODO: Implement bat projectiles
        return true;
    }

    private boolean activateTerminatorAbility(Player player) {
        // Salvation ability
        player.sendMessage("§6[TERMINATOR] §eSalvation aktiviert!");
        // TODO: Implement salvation effect
        return true;
    }

    /**
     * Gibt eine Liste aller verfügbaren Custom Items zurück
     */
    public List<CustomItemType> getAllCustomItems() {
        return List.of(CustomItemType.values());
    }

    /**
     * Gibt Custom Items nach Rarity zurück
     */
    public List<CustomItemType> getCustomItemsByRarity(Rarity rarity) {
        return List.of(CustomItemType.values()).stream()
                .filter(item -> item.getRarity() == rarity)
                .toList();
    }
}
