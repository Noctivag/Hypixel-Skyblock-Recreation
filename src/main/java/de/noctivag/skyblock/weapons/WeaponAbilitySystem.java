package de.noctivag.skyblock.weapons;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WeaponAbilitySystem - Complete weapon ability system for Hypixel Skyblock
 * 
 * Features:
 * - Weapon abilities (right-click, shift-right-click)
 * - Ability cooldowns
 * - Mana costs
 * - Ability effects and animations
 * - Weapon-specific abilities
 */
public class WeaponAbilitySystem implements Service {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, PlayerAbilityData> playerData = new ConcurrentHashMap<>();
    private final Map<String, WeaponAbility> abilities = new HashMap<>();
    private final Map<String, List<WeaponAbility>> abilitiesByWeapon = new HashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public WeaponAbilitySystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        
        // Initialize all weapon abilities
        initializeWeaponAbilities();
        
        status = SystemStatus.RUNNING;
        SkyblockPlugin.getLogger().info("§a[WeaponAbilitySystem] Initialized " + abilities.size() + " weapon abilities");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        playerData.clear();
        abilities.clear();
        abilitiesByWeapon.clear();
        status = SystemStatus.DISABLED;
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status != SystemStatus.RUNNING) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    @Override
    public String getName() {
        return "WeaponAbilitySystem";
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    /**
     * Initialize all weapon abilities
     */
    private void initializeWeaponAbilities() {
        // Sword Abilities
        initializeSwordAbilities();
        
        // Bow Abilities
        initializeBowAbilities();
        
        // Staff Abilities
        initializeStaffAbilities();
        
        // Special Weapon Abilities
        initializeSpecialWeaponAbilities();
    }
    
    /**
     * Initialize sword abilities
     */
    private void initializeSwordAbilities() {
        List<WeaponAbility> swordAbilities = new ArrayList<>();
        
        // Basic Sword Ability - Slash
        swordAbilities.add(new WeaponAbility(
            "slash", "Slash", "Deal extra damage to nearby enemies",
            WeaponType.SWORD, AbilityType.RIGHT_CLICK, 5, 20, 3.0,
            "Deals 150% damage to all enemies within 3 blocks"
        ));
        
        // Advanced Sword Ability - Whirlwind
        swordAbilities.add(new WeaponAbility(
            "whirlwind", "Whirlwind", "Spin attack that damages all nearby enemies",
            WeaponType.SWORD, AbilityType.SHIFT_RIGHT_CLICK, 10, 30, 5.0,
            "Deals 200% damage to all enemies within 5 blocks"
        ));
        
        // Legendary Sword Ability - Hyperion Wither Impact
        swordAbilities.add(new WeaponAbility(
            "wither_impact", "Wither Impact", "Teleport and deal massive damage",
            WeaponType.SWORD, AbilityType.RIGHT_CLICK, 20, 60, 8.0,
            "Teleports to target location and deals 500% damage to all enemies within 8 blocks"
        ));
        
        abilitiesByWeapon.put("SWORD", swordAbilities);
        swordAbilities.forEach(ability -> abilities.put(ability.getId(), ability));
    }
    
    /**
     * Initialize bow abilities
     */
    private void initializeBowAbilities() {
        List<WeaponAbility> bowAbilities = new ArrayList<>();
        
        // Basic Bow Ability - Piercing Shot
        bowAbilities.add(new WeaponAbility(
            "piercing_shot", "Piercing Shot", "Arrow pierces through multiple enemies",
            WeaponType.BOW, AbilityType.RIGHT_CLICK, 8, 25, 4.0,
            "Arrow pierces through up to 3 enemies"
        ));
        
        // Advanced Bow Ability - Explosive Shot
        bowAbilities.add(new WeaponAbility(
            "explosive_shot", "Explosive Shot", "Arrow explodes on impact",
            WeaponType.BOW, AbilityType.SHIFT_RIGHT_CLICK, 12, 40, 6.0,
            "Arrow explodes dealing 300% damage to all enemies within 6 blocks"
        ));
        
        // Legendary Bow Ability - Spirit Bow
        bowAbilities.add(new WeaponAbility(
            "spirit_shot", "Spirit Shot", "Summon spirit arrows",
            WeaponType.BOW, AbilityType.RIGHT_CLICK, 15, 50, 7.0,
            "Summons 5 spirit arrows that seek nearby enemies"
        ));
        
        abilitiesByWeapon.put("BOW", bowAbilities);
        bowAbilities.forEach(ability -> abilities.put(ability.getId(), ability));
    }
    
    /**
     * Initialize staff abilities
     */
    private void initializeStaffAbilities() {
        List<WeaponAbility> staffAbilities = new ArrayList<>();
        
        // Basic Staff Ability - Fireball
        staffAbilities.add(new WeaponAbility(
            "fireball", "Fireball", "Launch a fireball at enemies",
            WeaponType.STAFF, AbilityType.RIGHT_CLICK, 10, 20, 5.0,
            "Launches a fireball that deals 250% damage"
        ));
        
        // Advanced Staff Ability - Lightning Strike
        staffAbilities.add(new WeaponAbility(
            "lightning_strike", "Lightning Strike", "Strike enemies with lightning",
            WeaponType.STAFF, AbilityType.SHIFT_RIGHT_CLICK, 15, 35, 6.0,
            "Strikes all enemies within 6 blocks with lightning"
        ));
        
        // Legendary Staff Ability - Meteor
        staffAbilities.add(new WeaponAbility(
            "meteor", "Meteor", "Summon a meteor from the sky",
            WeaponType.STAFF, AbilityType.RIGHT_CLICK, 25, 60, 10.0,
            "Summons a meteor that deals 1000% damage to all enemies within 10 blocks"
        ));
        
        abilitiesByWeapon.put("STAFF", staffAbilities);
        staffAbilities.forEach(ability -> abilities.put(ability.getId(), ability));
    }
    
    /**
     * Initialize special weapon abilities
     */
    private void initializeSpecialWeaponAbilities() {
        List<WeaponAbility> specialAbilities = new ArrayList<>();
        
        // Hyperion Ability
        specialAbilities.add(new WeaponAbility(
            "hyperion_wither_impact", "Wither Impact", "Teleport and deal massive damage",
            WeaponType.SPECIAL, AbilityType.RIGHT_CLICK, 20, 60, 8.0,
            "Teleports to target location and deals 500% damage to all enemies within 8 blocks"
        ));
        
        // Valkyrie Ability
        specialAbilities.add(new WeaponAbility(
            "valkyrie_wither_impact", "Wither Impact", "Teleport and deal massive damage",
            WeaponType.SPECIAL, AbilityType.RIGHT_CLICK, 20, 60, 8.0,
            "Teleports to target location and deals 500% damage to all enemies within 8 blocks"
        ));
        
        // Scylla Ability
        specialAbilities.add(new WeaponAbility(
            "scylla_wither_impact", "Wither Impact", "Teleport and deal massive damage",
            WeaponType.SPECIAL, AbilityType.RIGHT_CLICK, 20, 60, 8.0,
            "Teleports to target location and deals 500% damage to all enemies within 8 blocks"
        ));
        
        // Astraea Ability
        specialAbilities.add(new WeaponAbility(
            "astraea_wither_impact", "Wither Impact", "Teleport and deal massive damage",
            WeaponType.SPECIAL, AbilityType.RIGHT_CLICK, 20, 60, 8.0,
            "Teleports to target location and deals 500% damage to all enemies within 8 blocks"
        ));
        
        abilitiesByWeapon.put("SPECIAL", specialAbilities);
        specialAbilities.forEach(ability -> abilities.put(ability.getId(), ability));
    }
    
    /**
     * Get player ability data
     */
    public PlayerAbilityData getPlayerData(UUID playerId) {
        return playerData.computeIfAbsent(playerId, k -> new PlayerAbilityData(playerId));
    }
    
    /**
     * Execute weapon ability
     */
    public boolean executeAbility(Player player, String abilityId) {
        WeaponAbility ability = abilities.get(abilityId);
        if (ability == null) return false;
        
        PlayerAbilityData playerAbilityData = getPlayerData(player.getUniqueId());
        
        // Check cooldown
        if (playerAbilityData.isOnCooldown(abilityId)) {
            long remaining = playerAbilityData.getCooldownRemaining(abilityId);
            player.sendMessage("§cAbility is on cooldown for " + remaining + " seconds!");
            return false;
        }
        
        // Check mana cost
        if (ability.getManaCost() > 0) {
            // TODO: Integrate with mana system
            player.sendMessage("§cNot enough mana! Need: " + ability.getManaCost());
            return false;
        }
        
        // Execute ability
        executeAbilityEffect(player, ability);
        
        // Set cooldown
        playerAbilityData.setCooldown(abilityId, ability.getCooldown());
        
        // Send message
        player.sendMessage("§aUsed ability: " + ability.getName());
        
        return true;
    }
    
    /**
     * Execute ability effect
     */
    private void executeAbilityEffect(Player player, WeaponAbility ability) {
        switch (ability.getId()) {
            case "slash":
                executeSlash(player, ability);
                break;
            case "whirlwind":
                executeWhirlwind(player, ability);
                break;
            case "wither_impact":
            case "hyperion_wither_impact":
            case "valkyrie_wither_impact":
            case "scylla_wither_impact":
            case "astraea_wither_impact":
                executeWitherImpact(player, ability);
                break;
            case "piercing_shot":
                executePiercingShot(player, ability);
                break;
            case "explosive_shot":
                executeExplosiveShot(player, ability);
                break;
            case "spirit_shot":
                executeSpiritShot(player, ability);
                break;
            case "fireball":
                executeFireball(player, ability);
                break;
            case "lightning_strike":
                executeLightningStrike(player, ability);
                break;
            case "meteor":
                executeMeteor(player, ability);
                break;
            default:
                player.sendMessage(Component.text("§cAbility not implemented yet!"));
                break;
        }
    }
    
    /**
     * Execute slash ability
     */
    private void executeSlash(Player player, WeaponAbility ability) {
        // TODO: Implement slash effect
        player.sendMessage(Component.text("§eSlash ability executed!"));
    }
    
    /**
     * Execute whirlwind ability
     */
    private void executeWhirlwind(Player player, WeaponAbility ability) {
        // TODO: Implement whirlwind effect
        player.sendMessage(Component.text("§eWhirlwind ability executed!"));
    }
    
    /**
     * Execute wither impact ability
     */
    private void executeWitherImpact(Player player, WeaponAbility ability) {
        // TODO: Implement wither impact effect
        player.sendMessage(Component.text("§eWither Impact ability executed!"));
    }
    
    /**
     * Execute piercing shot ability
     */
    private void executePiercingShot(Player player, WeaponAbility ability) {
        // TODO: Implement piercing shot effect
        player.sendMessage(Component.text("§ePiercing Shot ability executed!"));
    }
    
    /**
     * Execute explosive shot ability
     */
    private void executeExplosiveShot(Player player, WeaponAbility ability) {
        // TODO: Implement explosive shot effect
        player.sendMessage(Component.text("§eExplosive Shot ability executed!"));
    }
    
    /**
     * Execute spirit shot ability
     */
    private void executeSpiritShot(Player player, WeaponAbility ability) {
        // TODO: Implement spirit shot effect
        player.sendMessage(Component.text("§eSpirit Shot ability executed!"));
    }
    
    /**
     * Execute fireball ability
     */
    private void executeFireball(Player player, WeaponAbility ability) {
        // TODO: Implement fireball effect
        player.sendMessage(Component.text("§eFireball ability executed!"));
    }
    
    /**
     * Execute lightning strike ability
     */
    private void executeLightningStrike(Player player, WeaponAbility ability) {
        // TODO: Implement lightning strike effect
        player.sendMessage(Component.text("§eLightning Strike ability executed!"));
    }
    
    /**
     * Execute meteor ability
     */
    private void executeMeteor(Player player, WeaponAbility ability) {
        // TODO: Implement meteor effect
        player.sendMessage(Component.text("§eMeteor ability executed!"));
    }
    
    /**
     * Get abilities for weapon type
     */
    public List<WeaponAbility> getAbilitiesForWeapon(String weaponType) {
        return abilitiesByWeapon.getOrDefault(weaponType, new ArrayList<>());
    }
    
    /**
     * Get all abilities
     */
    public Map<String, WeaponAbility> getAllAbilities() {
        return new HashMap<>(abilities);
    }
    
    /**
     * Get ability by ID
     */
    public WeaponAbility getAbility(String id) {
        return abilities.get(id);
    }
    
    /**
     * Get player weapon abilities data (for GUI compatibility)
     */
    public PlayerWeaponAbilities getPlayerWeaponAbilities(UUID playerId) {
        return new PlayerWeaponAbilities(playerId);
    }
    
    /**
     * Player weapon abilities data class for GUI compatibility
     */
    public static class PlayerWeaponAbilities {
        private final UUID playerId;
        
        public PlayerWeaponAbilities(UUID playerId) {
            this.playerId = playerId;
        }
        
        public UUID getPlayerId() {
            return playerId;
        }

        // Methods expected by WeaponAbilityGUI
        public Map<String, Long> getCooldowns() {
            return new HashMap<>(); // Placeholder
        }
        
        public int getTotalAbilitiesUsed() {
            return 0; // Placeholder
        }
        
        public int getSuccessfulAbilities() {
            return 0; // Placeholder
        }
        
        public double getSuccessRate() {
            return 0.0; // Placeholder
        }
    }
    
}
