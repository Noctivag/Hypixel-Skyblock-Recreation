package de.noctivag.skyblock.magic;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Advanced Magic System for Hypixel Skyblock-style magic
 * Includes spells, mana, and magical abilities
 */
public class AdvancedMagicSystem {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerMagicData> playerMagicData = new ConcurrentHashMap<>();
    private final Map<String, SpellType> spellTypes = new HashMap<>();
    private final Map<UUID, List<ActiveSpell>> activeSpells = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> playerMana = new ConcurrentHashMap<>();
    
    public AdvancedMagicSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeSpellTypes();
        startMagicTask();
    }
    
    /**
     * Initialize all spell types
     */
    private void initializeSpellTypes() {
        // Combat Spells
        spellTypes.put("fireball", new SpellType(
            "Fireball", 
            Material.FIRE_CHARGE, 
            SpellCategory.COMBAT,
            Arrays.asList("§7Launches a fireball", "§7Deals fire damage to enemies"),
            50,
            5,
            100
        ));
        
        spellTypes.put("lightning_bolt", new SpellType(
            "Lightning Bolt", 
            Material.LIGHTNING_ROD, 
            SpellCategory.COMBAT,
            Arrays.asList("§7Summons lightning", "§7Deals massive damage"),
            100,
            10,
            200
        ));
        
        spellTypes.put("ice_shard", new SpellType(
            "Ice Shard", 
            Material.SNOWBALL, 
            SpellCategory.COMBAT,
            Arrays.asList("§7Launches ice shards", "§7Slows enemies"),
            75,
            7,
            150
        ));
        
        spellTypes.put("earth_spike", new SpellType(
            "Earth Spike", 
            Material.STONE, 
            SpellCategory.COMBAT,
            Arrays.asList("§7Creates earth spikes", "§7Deals physical damage"),
            80,
            8,
            120
        ));
        
        spellTypes.put("wind_blade", new SpellType(
            "Wind Blade", 
            Material.FEATHER, 
            SpellCategory.COMBAT,
            Arrays.asList("§7Creates wind blades", "§7Deals cutting damage"),
            90,
            9,
            180
        ));
        
        // Healing Spells
        spellTypes.put("heal", new SpellType(
            "Heal", 
            Material.GOLDEN_APPLE, 
            SpellCategory.HEALING,
            Arrays.asList("§7Restores health", "§7Heals over time"),
            60,
            6,
            80
        ));
        
        spellTypes.put("regeneration", new SpellType(
            "Regeneration", 
            Material.GHAST_TEAR, 
            SpellCategory.HEALING,
            Arrays.asList("§7Provides regeneration", "§7Heals over time"),
            80,
            8,
            100
        ));
        
        spellTypes.put("cure", new SpellType(
            "Cure", 
            Material.MILK_BUCKET, 
            SpellCategory.HEALING,
            Arrays.asList("§7Removes negative effects", "§7Cures status ailments"),
            40,
            4,
            60
        ));
        
        spellTypes.put("purify", new SpellType(
            "Purify", 
            Material.GLOWSTONE_DUST, 
            SpellCategory.HEALING,
            Arrays.asList("§7Purifies the area", "§7Removes all negative effects"),
            120,
            12,
            150
        ));
        
        // Utility Spells
        spellTypes.put("teleport", new SpellType(
            "Teleport", 
            Material.ENDER_PEARL, 
            SpellCategory.UTILITY,
            Arrays.asList("§7Teleports to target location", "§7Short range teleportation"),
            100,
            10,
            200
        ));
        
        spellTypes.put("invisibility", new SpellType(
            "Invisibility", 
            Material.GLASS, 
            SpellCategory.UTILITY,
            Arrays.asList("§7Makes you invisible", "§7Hides from enemies"),
            150,
            15,
            300
        ));
        
        spellTypes.put("speed_boost", new SpellType(
            "Speed Boost", 
            Material.SUGAR, 
            SpellCategory.UTILITY,
            Arrays.asList("§7Increases movement speed", "§7Faster movement"),
            70,
            7,
            100
        ));
        
        spellTypes.put("jump_boost", new SpellType(
            "Jump Boost", 
            Material.RABBIT_FOOT, 
            SpellCategory.UTILITY,
            Arrays.asList("§7Increases jump height", "§7Higher jumps"),
            60,
            6,
            90
        ));
        
        spellTypes.put("water_breathing", new SpellType(
            "Water Breathing", 
            Material.PUFFERFISH, 
            SpellCategory.UTILITY,
            Arrays.asList("§7Allows underwater breathing", "§7No drowning damage"),
            80,
            8,
            120
        ));
        
        spellTypes.put("night_vision", new SpellType(
            "Night Vision", 
            Material.GOLDEN_CARROT, 
            SpellCategory.UTILITY,
            Arrays.asList("§7Provides night vision", "§7See in the dark"),
            50,
            5,
            80
        ));
        
        // Protection Spells
        spellTypes.put("shield", new SpellType(
            "Shield", 
            Material.SHIELD, 
            SpellCategory.PROTECTION,
            Arrays.asList("§7Creates a protective shield", "§7Reduces incoming damage"),
            120,
            12,
            200
        ));
        
        spellTypes.put("fire_resistance", new SpellType(
            "Fire Resistance", 
            Material.FIRE_CHARGE, 
            SpellCategory.PROTECTION,
            Arrays.asList("§7Provides fire resistance", "§7Immune to fire damage"),
            100,
            10,
            150
        ));
        
        spellTypes.put("poison_resistance", new SpellType(
            "Poison Resistance", 
            Material.SPIDER_EYE, 
            SpellCategory.PROTECTION,
            Arrays.asList("§7Provides poison resistance", "§7Immune to poison"),
            90,
            9,
            130
        ));
        
        spellTypes.put("fall_protection", new SpellType(
            "Fall Protection", 
            Material.FEATHER, 
            SpellCategory.PROTECTION,
            Arrays.asList("§7Reduces fall damage", "§7Safe landing"),
            80,
            8,
            120
        ));
        
        // Special Spells
        spellTypes.put("summon_familiar", new SpellType(
            "Summon Familiar", 
            Material.BONE, 
            SpellCategory.SPECIAL,
            Arrays.asList("§7Summons a magical familiar", "§7Combat companion"),
            200,
            20,
            500
        ));
        
        spellTypes.put("time_stop", new SpellType(
            "Time Stop", 
            Material.CLOCK, 
            SpellCategory.SPECIAL,
            Arrays.asList("§7Stops time briefly", "§7Freezes enemies"),
            300,
            30,
            1000
        ));
        
        spellTypes.put("meteor", new SpellType(
            "Meteor", 
            Material.FIRE_CHARGE, 
            SpellCategory.SPECIAL,
            Arrays.asList("§7Summons a meteor", "§7Massive area damage"),
            400,
            40,
            1500
        ));
        
        spellTypes.put("blizzard", new SpellType(
            "Blizzard", 
            Material.SNOWBALL, 
            SpellCategory.SPECIAL,
            Arrays.asList("§7Creates a blizzard", "§7Area damage and slow"),
            350,
            35,
            1200
        ));
        
        spellTypes.put("earthquake", new SpellType(
            "Earthquake", 
            Material.STONE, 
            SpellCategory.SPECIAL,
            Arrays.asList("§7Creates an earthquake", "§7Massive area damage"),
            450,
            45,
            2000
        ));
    }
    
    /**
     * Start the magic task
     */
    private void startMagicTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveSpells();
                updateManaRegeneration();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active spells
     */
    private void updateActiveSpells() {
        for (Map.Entry<UUID, List<ActiveSpell>> entry : activeSpells.entrySet()) {
            UUID playerId = entry.getKey();
            List<ActiveSpell> spells = entry.getValue();
            
            // Remove expired spells
            spells.removeIf(ActiveSpell::isExpired);
            
            // Apply active spells
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                applySpells(player, spells);
            }
        }
    }
    
    /**
     * Update mana regeneration for all players
     */
    private void updateManaRegeneration() {
        for (Map.Entry<UUID, Integer> entry : playerMana.entrySet()) {
            UUID playerId = entry.getKey();
            int currentMana = entry.getValue();
            
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                int maxMana = getMaxMana(playerId);
                if (currentMana < maxMana) {
                    int regeneration = getManaRegeneration(playerId);
                    playerMana.put(playerId, Math.min(maxMana, currentMana + regeneration));
                }
            }
        }
    }
    
    /**
     * Apply spells to a player
     */
    private void applySpells(Player player, List<ActiveSpell> spells) {
        for (ActiveSpell spell : spells) {
            spell.apply(player);
        }
    }
    
    /**
     * Get player magic data
     */
    public PlayerMagicData getPlayerMagicData(UUID playerId) {
        return playerMagicData.computeIfAbsent(playerId, k -> new PlayerMagicData());
    }
    
    /**
     * Get spell type by ID
     */
    public SpellType getSpellType(String spellId) {
        return spellTypes.get(spellId);
    }
    
    /**
     * Get all spell types
     */
    public Map<String, SpellType> getAllSpellTypes() {
        return new HashMap<>(spellTypes);
    }
    
    /**
     * Get spell types by category
     */
    public Map<String, SpellType> getSpellTypesByCategory(SpellCategory category) {
        Map<String, SpellType> categorySpells = new HashMap<>();
        for (Map.Entry<String, SpellType> entry : spellTypes.entrySet()) {
            if (entry.getValue().getCategory() == category) {
                categorySpells.put(entry.getKey(), entry.getValue());
            }
        }
        return categorySpells;
    }
    
    /**
     * Check if player can cast a spell
     */
    public boolean canCastSpell(Player player, String spellId) {
        SpellType spell = getSpellType(spellId);
        if (spell == null) return false;
        
        // Check if player has enough mana
        int currentMana = getCurrentMana(player.getUniqueId());
        if (currentMana < spell.getManaCost()) {
            return false;
        }
        
        // Check if player has required level
        PlayerMagicData data = getPlayerMagicData(player.getUniqueId());
        if (data.getLevel() < getRequiredLevel(spellId)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Cast a spell
     */
    public boolean castSpell(Player player, String spellId) {
        if (!canCastSpell(player, spellId)) {
            return false;
        }
        
        SpellType spell = getSpellType(spellId);
        PlayerMagicData data = getPlayerMagicData(player.getUniqueId());
        
        // Remove mana
        int currentMana = getCurrentMana(player.getUniqueId());
        playerMana.put(player.getUniqueId(), currentMana - spell.getManaCost());
        
        // Create active spell
        ActiveSpell activeSpell = new ActiveSpell(spellId, player.getUniqueId(), spell.getDuration());
        List<ActiveSpell> playerSpells = activeSpells.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        playerSpells.add(activeSpell);
        
        // Update statistics
        data.incrementCastedSpells();
        data.addExperience(spell.getExperienceValue());
        
        return true;
    }
    
    /**
     * Get required level for a spell
     */
    private int getRequiredLevel(String spellId) {
        SpellType spell = getSpellType(spellId);
        if (spell == null) return 1;
        
        switch (spell.getCategory()) {
            case COMBAT:
            case HEALING:
            case UTILITY:
            case PROTECTION:
                return 1;
            case SPECIAL:
                return 10;
            default:
                return 1;
        }
    }
    
    /**
     * Get current mana for a player
     */
    public int getCurrentMana(UUID playerId) {
        return playerMana.getOrDefault(playerId, getMaxMana(playerId));
    }
    
    /**
     * Get max mana for a player
     */
    public int getMaxMana(UUID playerId) {
        PlayerMagicData data = getPlayerMagicData(playerId);
        return 100 + (data.getLevel() * 10);
    }
    
    /**
     * Get mana regeneration for a player
     */
    public int getManaRegeneration(UUID playerId) {
        PlayerMagicData data = getPlayerMagicData(playerId);
        return 1 + (data.getLevel() / 5);
    }
    
    /**
     * Set mana for a player
     */
    public void setMana(UUID playerId, int mana) {
        int maxMana = getMaxMana(playerId);
        playerMana.put(playerId, Math.min(maxMana, Math.max(0, mana)));
    }
    
    /**
     * Add mana to a player
     */
    public void addMana(UUID playerId, int mana) {
        int currentMana = getCurrentMana(playerId);
        setMana(playerId, currentMana + mana);
    }
    
    /**
     * Remove mana from a player
     */
    public void removeMana(UUID playerId, int mana) {
        int currentMana = getCurrentMana(playerId);
        setMana(playerId, currentMana - mana);
    }
    
    /**
     * Create a spell item
     */
    public ItemStack createSpellItem(String spellId) {
        SpellType spell = getSpellType(spellId);
        if (spell == null) return null;
        
        ItemStack item = new ItemStack(spell.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6" + spell.getName() + " Spell"));
            List<String> lore = new ArrayList<>(spell.getDescription());
            lore.add("");
            lore.add("§7Category: " + spell.getCategory().getDisplayName());
            lore.add("§7Mana Cost: §a" + spell.getManaCost());
            lore.add("§7Duration: §a" + spell.getDuration() + " seconds");
            lore.add("§7Experience: §a" + spell.getExperienceValue() + " XP");
            lore.add("");
            lore.add("§7Right-click to cast this spell");
            lore.add("§7and unleash magical power!");
            lore.add("");
            lore.add("§8A magical spell");
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Get player's magic level
     */
    public int getMagicLevel(UUID playerId) {
        PlayerMagicData data = getPlayerMagicData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's magic experience
     */
    public int getMagicExperience(UUID playerId) {
        PlayerMagicData data = getPlayerMagicData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add magic experience to player
     */
    public void addMagicExperience(UUID playerId, int experience) {
        PlayerMagicData data = getPlayerMagicData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's magic statistics
     */
    public Map<String, Integer> getMagicStatistics(UUID playerId) {
        PlayerMagicData data = getPlayerMagicData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("casted_spells", data.getCastedSpells());
        stats.put("total_experience", data.getTotalExperience());
        stats.put("current_mana", getCurrentMana(playerId));
        stats.put("max_mana", getMaxMana(playerId));
        
        return stats;
    }
    
    /**
     * Get active spells for a player
     */
    public List<ActiveSpell> getActiveSpells(UUID playerId) {
        return activeSpells.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Reset player's magic data
     */
    public void resetMagicData(UUID playerId) {
        playerMagicData.remove(playerId);
        activeSpells.remove(playerId);
        playerMana.remove(playerId);
    }
    
    /**
     * Save player's magic data
     */
    public void saveMagicData(UUID playerId) {
        PlayerMagicData data = getPlayerMagicData(playerId);
        // Save to database
        databaseManager.savePlayerMagicData(playerId, data);
    }
    
    /**
     * Load player's magic data
     */
    public void loadMagicData(UUID playerId) {
        try {
            CompletableFuture<Object> future = databaseManager.loadPlayerMagicData(playerId);
            PlayerMagicData data = (PlayerMagicData) future.get();
            if (data != null) {
                playerMagicData.put(playerId, data);
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Failed to load magic data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the magic system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerMagicData.keySet()) {
            saveMagicData(playerId);
        }
        
        // Clear data
        playerMagicData.clear();
        activeSpells.clear();
        playerMana.clear();
    }
}
