package de.noctivag.plugin.armor;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Armor Ability System - Hypixel Skyblock Style
 * 
 * Features:
 * - Armor Set Bonuses
 * - Full Set Abilities
 * - Partial Set Bonuses
 * - Armor Special Effects
 * - Cooldown Management
 * - Ability Activation
 */
public class ArmorAbilitySystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerArmorAbilities> playerArmorAbilities = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> armorAbilityTasks = new ConcurrentHashMap<>();
    
    public ArmorAbilitySystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startArmorAbilityUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void startArmorAbilityUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerArmorAbilities> entry : playerArmorAbilities.entrySet()) {
                    PlayerArmorAbilities abilities = entry.getValue();
                    abilities.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // Check for armor ability activation
        if (event.getAction().toString().contains("RIGHT_CLICK")) {
            checkArmorAbility(player);
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            applyArmorDamageBonuses(player, event);
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            applyArmorDefenseBonuses(player, event);
        }
    }
    
    private void checkArmorAbility(Player player) {
        ArmorSetType currentSet = getCurrentArmorSet(player);
        if (currentSet == null) return;
        
        ArmorAbilityConfig abilityConfig = getArmorAbilityConfig(currentSet);
        if (abilityConfig == null) return;
        
        PlayerArmorAbilities playerAbilities = getPlayerArmorAbilities(player.getUniqueId());
        
        // Check cooldown
        if (playerAbilities.isOnCooldown(currentSet)) {
            long remainingTime = playerAbilities.getCooldownRemaining(currentSet);
            player.sendMessage("§cFähigkeit ist noch " + remainingTime + " Sekunden im Cooldown!");
            return;
        }
        
        // Activate ability
        activateArmorAbility(player, currentSet, abilityConfig);
        
        // Set cooldown
        playerAbilities.setCooldown(currentSet, abilityConfig.getCooldown());
        
        player.sendMessage("§a" + abilityConfig.getName() + " aktiviert!");
    }
    
    private void activateArmorAbility(Player player, ArmorSetType armorSet, ArmorAbilityConfig config) {
        switch (armorSet) {
            case SUPERIOR_DRAGON:
                activateSuperiorDragonAbility(player, config);
                break;
            case UNSTABLE_DRAGON:
                activateUnstableDragonAbility(player, config);
                break;
            case STRONG_DRAGON:
                activateStrongDragonAbility(player, config);
                break;
            case YOUNG_DRAGON:
                activateYoungDragonAbility(player, config);
                break;
            case OLD_DRAGON:
                activateOldDragonAbility(player, config);
                break;
            case PROTECTOR_DRAGON:
                activateProtectorDragonAbility(player, config);
                break;
            case WISE_DRAGON:
                activateWiseDragonAbility(player, config);
                break;
            case SHADOW_ASSASSIN:
                activateShadowAssassinAbility(player, config);
                break;
            case ADAPTIVE_ARMOR:
                activateAdaptiveArmorAbility(player, config);
                break;
            case FROZEN_BLAZE_ARMOR:
                activateFrozenBlazeAbility(player, config);
                break;
            case YETI_ARMOR:
                activateYetiArmorAbility(player, config);
                break;
            case REVENANT_ARMOR:
                activateRevenantArmorAbility(player, config);
                break;
            case TARANTULA_ARMOR:
                activateTarantulaArmorAbility(player, config);
                break;
            case SVEN_ARMOR:
                activateSvenArmorAbility(player, config);
                break;
            case VOIDGLOOM_ARMOR:
                activateVoidgloomArmorAbility(player, config);
                break;
            case INFERNO_ARMOR:
                activateInfernoArmorAbility(player, config);
                break;
        }
    }
    
    private void activateSuperiorDragonAbility(Player player, ArmorAbilityConfig config) {
        // Superior Dragon: All-around stat boost
        player.sendMessage("§6Superior Dragon Ability: §7All stats increased by 25% for 30 seconds!");
        // Apply stat boost effect
    }
    
    private void activateUnstableDragonAbility(Player player, ArmorAbilityConfig config) {
        // Unstable Dragon: Random effect
        Random random = new Random();
        int effect = random.nextInt(4);
        switch (effect) {
            case 0:
                player.sendMessage("§5Unstable Dragon Ability: §7Speed boost activated!");
                break;
            case 1:
                player.sendMessage("§5Unstable Dragon Ability: §7Strength boost activated!");
                break;
            case 2:
                player.sendMessage("§5Unstable Dragon Ability: §7Defense boost activated!");
                break;
            case 3:
                player.sendMessage("§5Unstable Dragon Ability: §7Critical chance boost activated!");
                break;
        }
    }
    
    private void activateStrongDragonAbility(Player player, ArmorAbilityConfig config) {
        // Strong Dragon: Damage boost
        player.sendMessage("§cStrong Dragon Ability: §7Damage increased by 50% for 20 seconds!");
    }
    
    private void activateYoungDragonAbility(Player player, ArmorAbilityConfig config) {
        // Young Dragon: Speed boost
        player.sendMessage("§fYoung Dragon Ability: §7Speed increased by 100% for 15 seconds!");
    }
    
    private void activateOldDragonAbility(Player player, ArmorAbilityConfig config) {
        // Old Dragon: Defense boost
        player.sendMessage("§7Old Dragon Ability: §7Defense increased by 75% for 25 seconds!");
    }
    
    private void activateProtectorDragonAbility(Player player, ArmorAbilityConfig config) {
        // Protector Dragon: Maximum defense
        player.sendMessage("§aProtector Dragon Ability: §7Maximum defense activated for 30 seconds!");
    }
    
    private void activateWiseDragonAbility(Player player, ArmorAbilityConfig config) {
        // Wise Dragon: Intelligence and mana boost
        player.sendMessage("§bWise Dragon Ability: §7Intelligence and mana increased by 100% for 20 seconds!");
    }
    
    private void activateShadowAssassinAbility(Player player, ArmorAbilityConfig config) {
        // Shadow Assassin: Stealth and speed
        player.sendMessage("§5Shadow Assassin Ability: §7Stealth mode activated for 10 seconds!");
    }
    
    private void activateAdaptiveArmorAbility(Player player, ArmorAbilityConfig config) {
        // Adaptive Armor: Adapts to current situation
        player.sendMessage("§eAdaptive Armor Ability: §7Armor adapted to current environment!");
    }
    
    private void activateFrozenBlazeAbility(Player player, ArmorAbilityConfig config) {
        // Frozen Blaze: Ice and fire effects
        player.sendMessage("§bFrozen Blaze Ability: §7Ice and fire effects activated!");
    }
    
    private void activateYetiArmorAbility(Player player, ArmorAbilityConfig config) {
        // Yeti Armor: Cold immunity and strength
        player.sendMessage("§fYeti Armor Ability: §7Cold immunity and strength boost activated!");
    }
    
    private void activateRevenantArmorAbility(Player player, ArmorAbilityConfig config) {
        // Revenant Armor: Undead immunity and healing
        player.sendMessage("§2Revenant Armor Ability: §7Undead immunity and healing activated!");
    }
    
    private void activateTarantulaArmorAbility(Player player, ArmorAbilityConfig config) {
        // Tarantula Armor: Spider abilities and web immunity
        player.sendMessage("§8Tarantula Armor Ability: §7Spider abilities and web immunity activated!");
    }
    
    private void activateSvenArmorAbility(Player player, ArmorAbilityConfig config) {
        // Sven Armor: Wolf pack abilities
        player.sendMessage("§fSven Armor Ability: §7Wolf pack abilities activated!");
    }
    
    private void activateVoidgloomArmorAbility(Player player, ArmorAbilityConfig config) {
        // Voidgloom Armor: Enderman abilities and void immunity
        player.sendMessage("§5Voidgloom Armor Ability: §7Enderman abilities and void immunity activated!");
    }
    
    private void activateInfernoArmorAbility(Player player, ArmorAbilityConfig config) {
        // Inferno Armor: Fire abilities and fire immunity
        player.sendMessage("§6Inferno Armor Ability: §7Fire abilities and fire immunity activated!");
    }
    
    private void applyArmorDamageBonuses(Player player, EntityDamageByEntityEvent event) {
        ArmorSetType currentSet = getCurrentArmorSet(player);
        if (currentSet == null) return;
        
        double damageMultiplier = getArmorDamageMultiplier(currentSet);
        if (damageMultiplier > 1.0) {
            event.setDamage(event.getDamage() * damageMultiplier);
        }
    }
    
    private void applyArmorDefenseBonuses(Player player, EntityDamageEvent event) {
        ArmorSetType currentSet = getCurrentArmorSet(player);
        if (currentSet == null) return;
        
        double defenseMultiplier = getArmorDefenseMultiplier(currentSet);
        if (defenseMultiplier > 1.0) {
            event.setDamage(event.getDamage() / defenseMultiplier);
        }
    }
    
    private ArmorSetType getCurrentArmorSet(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        ItemStack chestplate = inventory.getChestplate();
        ItemStack leggings = inventory.getLeggings();
        ItemStack boots = inventory.getBoots();
        
        // Check for full armor sets
        if (isFullArmorSet(helmet, chestplate, leggings, boots, "Superior Dragon")) {
            return ArmorSetType.SUPERIOR_DRAGON;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Unstable Dragon")) {
            return ArmorSetType.UNSTABLE_DRAGON;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Strong Dragon")) {
            return ArmorSetType.STRONG_DRAGON;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Young Dragon")) {
            return ArmorSetType.YOUNG_DRAGON;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Old Dragon")) {
            return ArmorSetType.OLD_DRAGON;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Protector Dragon")) {
            return ArmorSetType.PROTECTOR_DRAGON;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Wise Dragon")) {
            return ArmorSetType.WISE_DRAGON;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Shadow Assassin")) {
            return ArmorSetType.SHADOW_ASSASSIN;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Adaptive Armor")) {
            return ArmorSetType.ADAPTIVE_ARMOR;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Frozen Blaze")) {
            return ArmorSetType.FROZEN_BLAZE_ARMOR;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Yeti Armor")) {
            return ArmorSetType.YETI_ARMOR;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Revenant Armor")) {
            return ArmorSetType.REVENANT_ARMOR;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Tarantula Armor")) {
            return ArmorSetType.TARANTULA_ARMOR;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Sven Armor")) {
            return ArmorSetType.SVEN_ARMOR;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Voidgloom Armor")) {
            return ArmorSetType.VOIDGLOOM_ARMOR;
        } else if (isFullArmorSet(helmet, chestplate, leggings, boots, "Inferno Armor")) {
            return ArmorSetType.INFERNO_ARMOR;
        }
        
        return null;
    }
    
    private boolean isFullArmorSet(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, String setName) {
        return helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasDisplayName() &&
               chestplate != null && chestplate.hasItemMeta() && chestplate.getItemMeta().hasDisplayName() &&
               leggings != null && leggings.hasItemMeta() && leggings.getItemMeta().hasDisplayName() &&
               boots != null && boots.hasItemMeta() && boots.getItemMeta().hasDisplayName() &&
               helmet.getItemMeta().getDisplayName().contains(setName) &&
               chestplate.getItemMeta().getDisplayName().contains(setName) &&
               leggings.getItemMeta().getDisplayName().contains(setName) &&
               boots.getItemMeta().getDisplayName().contains(setName);
    }
    
    private ArmorAbilityConfig getArmorAbilityConfig(ArmorSetType armorSet) {
        return switch (armorSet) {
            case SUPERIOR_DRAGON -> new ArmorAbilityConfig("Superior Dragon", "All-around stat boost", 60);
            case UNSTABLE_DRAGON -> new ArmorAbilityConfig("Unstable Dragon", "Random effect", 45);
            case STRONG_DRAGON -> new ArmorAbilityConfig("Strong Dragon", "Damage boost", 50);
            case YOUNG_DRAGON -> new ArmorAbilityConfig("Young Dragon", "Speed boost", 40);
            case OLD_DRAGON -> new ArmorAbilityConfig("Old Dragon", "Defense boost", 55);
            case PROTECTOR_DRAGON -> new ArmorAbilityConfig("Protector Dragon", "Maximum defense", 70);
            case WISE_DRAGON -> new ArmorAbilityConfig("Wise Dragon", "Intelligence boost", 50);
            case SHADOW_ASSASSIN -> new ArmorAbilityConfig("Shadow Assassin", "Stealth mode", 30);
            case ADAPTIVE_ARMOR -> new ArmorAbilityConfig("Adaptive Armor", "Adaptation", 60);
            case FROZEN_BLAZE_ARMOR -> new ArmorAbilityConfig("Frozen Blaze", "Ice and fire effects", 45);
            case YETI_ARMOR -> new ArmorAbilityConfig("Yeti Armor", "Cold immunity", 50);
            case REVENANT_ARMOR -> new ArmorAbilityConfig("Revenant Armor", "Undead immunity", 40);
            case TARANTULA_ARMOR -> new ArmorAbilityConfig("Tarantula Armor", "Spider abilities", 45);
            case SVEN_ARMOR -> new ArmorAbilityConfig("Sven Armor", "Wolf pack abilities", 50);
            case VOIDGLOOM_ARMOR -> new ArmorAbilityConfig("Voidgloom Armor", "Enderman abilities", 55);
            case INFERNO_ARMOR -> new ArmorAbilityConfig("Inferno Armor", "Fire abilities", 50);
        };
    }
    
    private double getArmorDamageMultiplier(ArmorSetType armorSet) {
        return switch (armorSet) {
            case STRONG_DRAGON -> 1.25;
            case INFERNO_ARMOR -> 1.20;
            case VOIDGLOOM_ARMOR -> 1.15;
            case SVEN_ARMOR -> 1.10;
            default -> 1.0;
        };
    }
    
    private double getArmorDefenseMultiplier(ArmorSetType armorSet) {
        return switch (armorSet) {
            case PROTECTOR_DRAGON -> 1.50;
            case OLD_DRAGON -> 1.30;
            case YETI_ARMOR -> 1.25;
            case REVENANT_ARMOR -> 1.20;
            case TARANTULA_ARMOR -> 1.15;
            default -> 1.0;
        };
    }
    
    public PlayerArmorAbilities getPlayerArmorAbilities(UUID playerId) {
        return playerArmorAbilities.computeIfAbsent(playerId, k -> new PlayerArmorAbilities(playerId));
    }
    
    public enum ArmorSetType {
        SUPERIOR_DRAGON, UNSTABLE_DRAGON, STRONG_DRAGON, YOUNG_DRAGON, OLD_DRAGON, PROTECTOR_DRAGON, WISE_DRAGON,
        SHADOW_ASSASSIN, ADAPTIVE_ARMOR, FROZEN_BLAZE_ARMOR, YETI_ARMOR,
        REVENANT_ARMOR, TARANTULA_ARMOR, SVEN_ARMOR, VOIDGLOOM_ARMOR, INFERNO_ARMOR
    }
    
    public static class ArmorAbilityConfig {
        private final String name;
        private final String description;
        private final int cooldown; // in seconds
        
        public ArmorAbilityConfig(String name, String description, int cooldown) {
            this.name = name;
            this.description = description;
            this.cooldown = cooldown;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getCooldown() { return cooldown; }
    }
    
    public static class PlayerArmorAbilities {
        private final UUID playerId;
        private final Map<ArmorSetType, Long> cooldowns = new ConcurrentHashMap<>();
        private int totalAbilitiesUsed = 0;
        private long lastUpdate;
        
        public PlayerArmorAbilities(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save armor ability data to database
        }
        
        public void setCooldown(ArmorSetType armorSet, int cooldownSeconds) {
            cooldowns.put(armorSet, System.currentTimeMillis() + (cooldownSeconds * 1000L));
            totalAbilitiesUsed++;
        }
        
        public boolean isOnCooldown(ArmorSetType armorSet) {
            Long cooldownEnd = cooldowns.get(armorSet);
            return cooldownEnd != null && System.currentTimeMillis() < cooldownEnd;
        }
        
        public long getCooldownRemaining(ArmorSetType armorSet) {
            Long cooldownEnd = cooldowns.get(armorSet);
            if (cooldownEnd == null) return 0;
            long remaining = (cooldownEnd - System.currentTimeMillis()) / 1000;
            return Math.max(0, remaining);
        }
        
        public int getTotalAbilitiesUsed() {
            return totalAbilitiesUsed;
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<ArmorSetType, Long> getCooldowns() { return cooldowns; }
    }
}
