package de.noctivag.skyblock.items;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
// import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

// import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ItemAbilitySystem - Manages all item abilities and special effects
 * 
 * Features:
 * - Dragon Weapon Abilities
 * - Dungeon Weapon Abilities
 * - Slayer Weapon Abilities
 * - Mining Tool Abilities
 * - Fishing Rod Abilities
 * - Magic Weapon Abilities
 * - Bow Abilities
 * - Special Item Abilities
 * - Cooldown Management
 * - Ability Activation
 */
public class ItemAbilitySystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, Long> abilityCooldowns = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> activeAbilities = new ConcurrentHashMap<>();
    
    public ItemAbilitySystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        SkyblockPlugin.getServer().getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    /**
     * Activate item ability for player
     */
    public void activateAbility(Player player, ItemType itemType) {
        UUID playerId = player.getUniqueId();
        String cooldownKey = playerId.toString() + "_" + itemType.name();
        
        // Check cooldown
        if (abilityCooldowns.containsKey(playerId)) {
            long lastUsed = abilityCooldowns.get(playerId);
            long cooldownTime = getAbilityCooldown(itemType);
            if (java.lang.System.currentTimeMillis() - lastUsed < cooldownTime) {
                long remaining = (cooldownTime - (java.lang.System.currentTimeMillis() - lastUsed)) / 1000;
                player.sendMessage("§cAbility on cooldown for " + remaining + " seconds!");
                return;
            }
        }
        
        // Activate ability based on item type
        switch (itemType) {
            // Dragon Weapon Abilities
            case ASPECT_OF_THE_DRAGONS:
                activateDragonBreath(player);
                break;
            case ASPECT_OF_THE_END:
                activateInstantTransmission(player);
                break;
            case ASPECT_OF_THE_VOID:
                activateVoidStrike(player);
                break;
                
            // Dungeon Weapon Abilities
            case HYPERION:
                activateWitherImpact(player);
                break;
            case SCYLLA:
                activateWitherImpact(player);
                break;
            case ASTRAEA:
                activateWitherImpact(player);
                break;
            case VALKYRIE:
                activateWitherImpact(player);
                break;
            case BONEMERANG:
                activateBoneToss(player);
                break;
            case SPIRIT_BOW:
                activateSpiritShot(player);
                break;
            case SPIRIT_SCEPTER:
                activateSpiritBurst(player);
                break;
            case SPIRIT_SWORD:
                activateSpiritStrike(player);
                break;
            case ADAPTIVE_BLADE:
                activateAdaptiveStrike(player);
                break;
            case SHADOW_FURY:
                activateShadowStrike(player);
                break;
            case LIVID_DAGGER:
                activateBackstab(player);
                break;
                
            // Slayer Weapon Abilities
            case REVENANT_FALCHION:
                activateUndeadSlayer(player);
                break;
            case REAPER_FALCHION:
                activateReaperStrike(player);
                break;
            case REAPER_SCYTHE:
                activateReaperHarvest(player);
                break;
            case AXE_OF_THE_SHREDDED:
                activateWebShredder(player);
                break;
            case VOIDEDGE_KATANA:
                activateVoidStrike(player);
                break;
            case VOIDWALKER_KATANA:
                activateVoidWalker(player);
                break;
            case VOIDLING_KATANA:
                activateVoidMastery(player);
                break;
                
            // Mining Tool Abilities
            case DIAMOND_PICKAXE:
                activateBasicMining(player);
                break;
            case STONK:
                activateEnhancedMining(player);
                break;
            case GOLDEN_PICKAXE:
                activateFastMining(player);
                break;
            case MOLTEN_PICKAXE:
                activateMoltenMining(player);
                break;
            case TITANIUM_PICKAXE:
                activateTitaniumMining(player);
                break;
            case DRILL_ENGINE:
                activateMechanicalMining(player);
                break;
            case TITANIUM_DRILL_DR_X355:
                activateAdvancedMining(player);
                break;
            case TITANIUM_DRILL_DR_X455:
                activateHighEndMining(player);
                break;
            case TITANIUM_DRILL_DR_X555:
                activateUltimateMining(player);
                break;
            case GAUNTLET:
                activateGauntletPower(player);
                break;
                
            // Fishing Rod Abilities
            case ROD_OF_THE_SEA:
                activateSeaCreatureBoost(player);
                break;
            case CHALLENGING_ROD:
                activateChallengeBoost(player);
                break;
            case ROD_OF_LEGENDS:
                activateLegendaryFishing(player);
                break;
            case SHARK_BAIT:
                activateSharkSpecialist(player);
                break;
            case SHARK_SCALE_ROD:
                activateSharkScalePower(player);
                break;
            case AUGER_ROD:
                activateMechanicalFishing(player);
                break;
                
            // Magic Weapon Abilities
            case FIRE_VEIL_WAND:
                activateFireVeil(player);
                break;
            case FROZEN_SCYTHE:
                activateFrozenSlash(player);
                break;
            case VOODOO_DOLL:
                activateVoodooCurse(player);
                break;
            case BONZO_STAFF:
                activateBonzoBalloon(player);
                break;
            case SCARF_STUDIES:
                activateStudy(player);
                break;
            case PROFESSOR_SCARF_STAFF:
                activateAcademicPower(player);
                break;
            case THORN_BOW:
                activateThornShot(player);
                break;
            case LAST_BREATH:
                activateLastBreath(player);
                break;
                
            // Bow Abilities
            case JUJU_SHORTBOW:
                activateRapidFire(player);
                break;
            case TERMINATOR:
                activateTerminate(player);
                break;
            case ARTISANAL_SHORTBOW:
                activatePreciseShot(player);
                break;
            case MAGMA_BOW:
                activateMagmaShot(player);
                break;
            case VENOM_TOUCH:
                activateVenomShot(player);
                break;
                
            // Special Item Abilities
            case GRAPPLING_HOOK:
                activateGrapple(player);
                break;
            case ENDER_PEARL:
                activateTeleport(player);
                break;
            case AOTE:
                activateInstantTransmission(player);
                break;
            case AOTV:
                activateVoidStrike(player);
                break;
            case PIGMAN_SWORD:
                activatePigmanRage(player);
                break;
            case GOLDEN_APPLE:
                activateHealing(player);
                break;
            case ENCHANTED_GOLDEN_APPLE:
                activateEnhancedHealing(player);
                break;
            case POTATO_WAR_ARMOR:
                activatePotatoWarPower(player);
                break;
                
            default:
                player.sendMessage(Component.text("§cNo ability available for this item!"));
                return;
        }
        
        // Set cooldown
        abilityCooldowns.put(playerId, java.lang.System.currentTimeMillis());
        player.sendMessage("§a" + itemType.getDisplayName() + " ability activated!");
    }
    
    /**
     * Dragon Weapon Abilities
     */
    private void activateDragonBreath(Player player) {
        player.sendMessage(Component.text("§aDragon Breath activated! Devastating dragon energy for 30 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 600, 4)); // 30 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 0));
    }
    
    private void activateInstantTransmission(Player player) {
        player.sendMessage(Component.text("§aInstant Transmission activated! Teleport 8 blocks ahead!"));
        // Teleport logic would be implemented here
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 2)); // 15 seconds
    }
    
    private void activateVoidStrike(Player player) {
        player.sendMessage(Component.text("§aVoid Strike activated! Channeling void power for 45 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 900, 5)); // 45 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 900, 0));
    }
    
    /**
     * Dungeon Weapon Abilities
     */
    private void activateWitherImpact(Player player) {
        player.sendMessage(Component.text("§aWither Impact activated! Devastating explosion!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 600, 6)); // 30 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 2));
    }
    
    private void activateBoneToss(Player player) {
        player.sendMessage(Component.text("§aBone Toss activated! Throwing bone that returns!"));
        // Bone toss logic would be implemented here
    }
    
    private void activateSpiritShot(Player player) {
        player.sendMessage(Component.text("§aSpirit Shot activated! Channeling spirit energy for 30 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 600, 4)); // 30 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0));
    }
    
    private void activateSpiritBurst(Player player) {
        player.sendMessage(Component.text("§aSpirit Burst activated! Explosive spirit magic for 45 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 900, 5)); // 45 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 1));
    }
    
    private void activateSpiritStrike(Player player) {
        player.sendMessage(Component.text("§aSpirit Strike activated! Ghostly strikes for 30 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 600, 4)); // 30 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0));
    }
    
    private void activateAdaptiveStrike(Player player) {
        player.sendMessage(Component.text("§aAdaptive Strike activated! Adapting to combat for 60 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1200, 3)); // 60 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 1200, 1));
    }
    
    private void activateShadowStrike(Player player) {
        player.sendMessage(Component.text("§aShadow Strike activated! Shadow energy for 45 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 900, 5)); // 45 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 900, 0));
    }
    
    private void activateBackstab(Player player) {
        player.sendMessage(Component.text("§aBackstab activated! Fury of a livid for 60 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1200, 6)); // 60 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 3));
    }
    
    /**
     * Slayer Weapon Abilities
     */
    private void activateUndeadSlayer(Player player) {
        player.sendMessage(Component.text("§aUndead Slayer activated! Extra damage to zombies for 90 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1800, 4)); // 90 seconds
    }
    
    private void activateReaperStrike(Player player) {
        player.sendMessage(Component.text("§aReaper Strike activated! Enhanced zombie slaying for 120 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 2400, 5)); // 120 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2400, 2));
    }
    
    private void activateReaperHarvest(Player player) {
        player.sendMessage(Component.text("§aReaper's Harvest activated! Ultimate zombie slaying for 150 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 3000, 6)); // 150 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3000, 3));
    }
    
    private void activateWebShredder(Player player) {
        player.sendMessage(Component.text("§aWeb Shredder activated! Massive spider damage for 90 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1800, 5)); // 90 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1800, 2));
    }
    
    private void activateVoidWalker(Player player) {
        player.sendMessage(Component.text("§aVoid Walker activated! Enhanced enderman slaying for 120 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 2400, 5)); // 120 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2400, 0));
    }
    
    private void activateVoidMastery(Player player) {
        player.sendMessage(Component.text("§aVoid Mastery activated! Ultimate enderman slaying for 150 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 3000, 6)); // 150 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 3000, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 3000, 0));
    }
    
    /**
     * Mining Tool Abilities
     */
    private void activateBasicMining(Player player) {
        player.sendMessage(Component.text("§aBasic Mining activated! Enhanced mining for 60 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 1200, 2)); // 60 seconds
    }
    
    private void activateEnhancedMining(Player player) {
        player.sendMessage(Component.text("§aEnhanced Mining activated! Superior mining for 90 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 1800, 3)); // 90 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 1800, 1));
    }
    
    private void activateFastMining(Player player) {
        player.sendMessage(Component.text("§aFast Mining activated! Rapid mining for 75 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 1500, 4)); // 75 seconds
    }
    
    private void activateMoltenMining(Player player) {
        player.sendMessage(Component.text("§aMolten Mining activated! Fire-resistant mining for 120 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 2400, 4)); // 120 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2400, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 2400, 2));
    }
    
    private void activateTitaniumMining(Player player) {
        player.sendMessage(Component.text("§aTitanium Mining activated! Ultimate mining power for 150 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 3000, 5)); // 150 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 3000, 3));
    }
    
    private void activateMechanicalMining(Player player) {
        player.sendMessage(Component.text("§aMechanical Mining activated! Auto-repair mining for 180 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 3600, 5)); // 180 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 3600, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3600, 1));
    }
    
    private void activateAdvancedMining(Player player) {
        player.sendMessage(Component.text("§aAdvanced Mining activated! High-tech mining for 200 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 4000, 6)); // 200 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 4000, 4));
    }
    
    private void activateHighEndMining(Player player) {
        player.sendMessage(Component.text("§aHigh-End Mining activated! Premium mining for 250 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 5000, 6)); // 250 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 5000, 5));
    }
    
    private void activateUltimateMining(Player player) {
        player.sendMessage(Component.text("§aUltimate Mining activated! Maximum mining efficiency for 300 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 6000, 7)); // 300 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 6000, 6));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 6000, 2));
    }
    
    private void activateGauntletPower(Player player) {
        player.sendMessage(Component.text("§aGauntlet Power activated! All-in-one tool power for 240 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 4800, 6)); // 240 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 4800, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 4800, 3));
    }
    
    /**
     * Fishing Rod Abilities
     */
    private void activateSeaCreatureBoost(Player player) {
        player.sendMessage(Component.text("§aSea Creature Boost activated! +10% Sea Creature Chance for 300 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 6000, 2)); // 300 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 0));
    }
    
    private void activateChallengeBoost(Player player) {
        player.sendMessage(Component.text("§aChallenge Boost activated! Enhanced fishing for 360 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 7200, 3)); // 360 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 7200, 0));
    }
    
    private void activateLegendaryFishing(Player player) {
        player.sendMessage(Component.text("§aLegendary Fishing activated! Incredible fishing power for 420 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 8400, 4)); // 420 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 8400, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 8400, 0));
    }
    
    private void activateSharkSpecialist(Player player) {
        player.sendMessage(Component.text("§aShark Specialist activated! +25% Shark Chance for 480 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 9600, 5)); // 480 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 9600, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 9600, 0));
    }
    
    private void activateSharkScalePower(Player player) {
        player.sendMessage(Component.text("§aShark Scale Power activated! Enhanced shark hunting for 540 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 10800, 6)); // 540 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10800, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 10800, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 10800, 0));
    }
    
    private void activateMechanicalFishing(Player player) {
        player.sendMessage(Component.text("§aMechanical Fishing activated! Auto-reel fishing for 600 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 12000, 7)); // 600 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 12000, 0));
    }
    
    /**
     * Magic Weapon Abilities
     */
    private void activateFireVeil(Player player) {
        player.sendMessage(Component.text("§aFire Veil activated! Veil of fire for 60 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0)); // 60 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1200, 4));
    }
    
    private void activateFrozenSlash(Player player) {
        player.sendMessage(Component.text("§aFrozen Slash activated! Ice and frost power for 75 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1500, 5)); // 75 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1500, 0)); // Ice effect
    }
    
    private void activateVoodooCurse(Player player) {
        player.sendMessage(Component.text("§aVoodoo Curse activated! Dark magic for 90 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1800, 5)); // 90 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1800, 0));
    }
    
    private void activateBonzoBalloon(Player player) {
        player.sendMessage(Component.text("§aBonzo Balloon activated! Clown magic for 60 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1200, 4)); // 60 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 1200, 2));
    }
    
    private void activateStudy(Player player) {
        player.sendMessage(Component.text("§aStudy activated! Academic knowledge for 90 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1800, 4)); // 90 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1800, 0));
    }
    
    private void activateAcademicPower(Player player) {
        player.sendMessage(Component.text("§aAcademic Power activated! Professor's knowledge for 120 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 2400, 5)); // 120 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2400, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 2400, 2));
    }
    
    private void activateThornShot(Player player) {
        player.sendMessage(Component.text("§aThorn Shot activated! Piercing thorn arrows for 45 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 900, 4)); // 45 seconds
    }
    
    private void activateLastBreath(Player player) {
        player.sendMessage(Component.text("§aLast Breath activated! Final blow power for 60 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1200, 6)); // 60 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 3));
    }
    
    /**
     * Bow Abilities
     */
    private void activateRapidFire(Player player) {
        player.sendMessage(Component.text("§aRapid Fire activated! High-speed arrows for 90 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1800, 5)); // 90 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1800, 3));
    }
    
    private void activateTerminate(Player player) {
        player.sendMessage(Component.text("§aTerminate activated! Devastating arrows for 120 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 2400, 7)); // 120 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2400, 4));
    }
    
    private void activatePreciseShot(Player player) {
        player.sendMessage(Component.text("§aPrecise Shot activated! Precision arrows for 60 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1200, 3)); // 60 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 1200, 2));
    }
    
    private void activateMagmaShot(Player player) {
        player.sendMessage(Component.text("§aMagma Shot activated! Fire arrows for 75 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1500, 4)); // 75 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1500, 0));
    }
    
    private void activateVenomShot(Player player) {
        player.sendMessage(Component.text("§aVenom Shot activated! Poison arrows for 75 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1500, 4)); // 75 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1500, 0)); // Venom effect
    }
    
    /**
     * Special Item Abilities
     */
    private void activateGrapple(Player player) {
        player.sendMessage(Component.text("§aGrapple activated! Long-range grappling!"));
        // Grapple logic would be implemented here
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 300, 0)); // 15 seconds
    }
    
    private void activateTeleport(Player player) {
        player.sendMessage(Component.text("§aTeleport activated! Instant teleportation!"));
        // Teleport logic would be implemented here
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 300, 0)); // 15 seconds
    }
    
    private void activatePigmanRage(Player player) {
        player.sendMessage(Component.text("§aPigman Rage activated! Pigman power for 90 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1800, 4)); // 90 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1800, 0));
    }
    
    private void activateHealing(Player player) {
        player.sendMessage(Component.text("§aHealing activated! Instant healing and resistance!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 4)); // 30 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 600, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 0));
    }
    
    private void activateEnhancedHealing(Player player) {
        player.sendMessage(Component.text("§aEnhanced Healing activated! Massive healing and multiple resistances!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 6)); // 60 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 1200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 3));
    }
    
    private void activatePotatoWarPower(Player player) {
        player.sendMessage(Component.text("§aPotato War Power activated! War damage resistance for 180 seconds!"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 3600, 3)); // 180 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 3600, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3600, 2));
    }
    
    /**
     * Get ability cooldown time
     */
    private long getAbilityCooldown(ItemType itemType) {
        switch (itemType) {
            // Dragon weapons - 60 seconds
            case ASPECT_OF_THE_DRAGONS, ASPECT_OF_THE_END, ASPECT_OF_THE_VOID:
                return 60000; // 60 seconds
                
            // Dungeon weapons - 30 seconds
            case HYPERION, SCYLLA, ASTRAEA, VALKYRIE, BONEMERANG, SPIRIT_BOW, SPIRIT_SCEPTER, SPIRIT_SWORD,
                 ADAPTIVE_BLADE, SHADOW_FURY, LIVID_DAGGER:
                return 30000; // 30 seconds
                
            // Slayer weapons - 45 seconds
            case REVENANT_FALCHION, REAPER_FALCHION, REAPER_SCYTHE, AXE_OF_THE_SHREDDED, VOIDEDGE_KATANA, VOIDWALKER_KATANA, VOIDLING_KATANA:
                return 45000; // 45 seconds
                
            // Mining tools - 120 seconds
            case DIAMOND_PICKAXE, STONK, GOLDEN_PICKAXE, MOLTEN_PICKAXE, TITANIUM_PICKAXE, DRILL_ENGINE,
                 TITANIUM_DRILL_DR_X355, TITANIUM_DRILL_DR_X455, TITANIUM_DRILL_DR_X555, GAUNTLET:
                return 120000; // 120 seconds
                
            // Fishing rods - 180 seconds
            case ROD_OF_THE_SEA, CHALLENGING_ROD, ROD_OF_LEGENDS, SHARK_BAIT, SHARK_SCALE_ROD, AUGER_ROD:
                return 180000; // 180 seconds
                
            // Magic weapons - 60 seconds
            case FIRE_VEIL_WAND, FROZEN_SCYTHE, VOODOO_DOLL, BONZO_STAFF, SCARF_STUDIES, PROFESSOR_SCARF_STAFF, THORN_BOW, LAST_BREATH:
                return 60000; // 60 seconds
                
            // Bows - 45 seconds
            case JUJU_SHORTBOW, TERMINATOR, ARTISANAL_SHORTBOW, MAGMA_BOW, VENOM_TOUCH:
                return 45000; // 45 seconds
                
            // Special items - 90 seconds
            case GRAPPLING_HOOK, ENDER_PEARL, AOTE, AOTV, PIGMAN_SWORD, GOLDEN_APPLE, ENCHANTED_GOLDEN_APPLE, POTATO_WAR_ARMOR:
                return 90000; // 90 seconds
                
            default:
                return 60000; // Default 60 seconds
        }
    }
    
    /**
     * Check if player has ability active
     */
    public boolean hasAbilityActive(Player player) {
        return activeAbilities.containsKey(player.getUniqueId());
    }
    
    /**
     * Deactivate all abilities for player
     */
    public void deactivateAllAbilities(Player player) {
        BukkitTask task = activeAbilities.remove(player.getUniqueId());
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
        player.sendMessage(Component.text("§7All item abilities deactivated!"));
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Handle item ability activation through item interaction
        // This would check if the player is holding an item and right-clicked to activate ability
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Handle item ability effects during combat
        // This would apply item-specific damage modifications
    }
}
