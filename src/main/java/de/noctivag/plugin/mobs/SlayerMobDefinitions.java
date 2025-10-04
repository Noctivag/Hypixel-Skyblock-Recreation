package de.noctivag.plugin.mobs;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.EntityType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SlayerMobDefinitions {
    
    public static Map<AdvancedMobSystem.MobType, AdvancedMobSystem.MobConfig> getSlayerMobConfigs() {
        Map<AdvancedMobSystem.MobType, AdvancedMobSystem.MobConfig> mobConfigs = new HashMap<>();
        
        // === ZOMBIE SLAYER MOBS ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.REVENANT_HORROR_I, new AdvancedMobSystem.MobConfig(
            "Revenant Horror I", "§2Revenant Horror I", EntityType.ZOMBIE,
            "§7A basic revenant horror.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.UNCOMMON, 100, 20, 0.1,
            Arrays.asList("§7- Slayer Spawn", "§7- Undead", "§7- Revenant Power"),
            Arrays.asList("§7- 1x Revenant Flesh", "§7- 20x XP", "§7- 10% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Zombie Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.REVENANT_HORROR_II, new AdvancedMobSystem.MobConfig(
            "Revenant Horror II", "§2Revenant Horror II", EntityType.ZOMBIE,
            "§7A stronger revenant horror.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.RARE, 200, 35, 0.08,
            Arrays.asList("§7- Slayer Spawn", "§7- Undead", "§7- Revenant Power"),
            Arrays.asList("§7- 2x Revenant Flesh", "§7- 35x XP", "§7- 15% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Zombie Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.REVENANT_HORROR_III, new AdvancedMobSystem.MobConfig(
            "Revenant Horror III", "§2Revenant Horror III", EntityType.ZOMBIE,
            "§7A powerful revenant horror.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.EPIC, 400, 60, 0.05,
            Arrays.asList("§7- Slayer Spawn", "§7- Undead", "§7- Revenant Power"),
            Arrays.asList("§7- 3x Revenant Flesh", "§7- 60x XP", "§7- 25% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Zombie Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.REVENANT_HORROR_IV, new AdvancedMobSystem.MobConfig(
            "Revenant Horror IV", "§2Revenant Horror IV", EntityType.ZOMBIE,
            "§7An extremely powerful revenant horror.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.LEGENDARY, 800, 100, 0.03,
            Arrays.asList("§7- Slayer Spawn", "§7- Undead", "§7- Revenant Power"),
            Arrays.asList("§7- 5x Revenant Flesh", "§7- 100x XP", "§7- 40% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Zombie Slayer Quest")
        ));
        
        // === SPIDER SLAYER MOBS ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.TARANTULA_BROODFATHER_I, new AdvancedMobSystem.MobConfig(
            "Tarantula Broodfather I", "§8Tarantula Broodfather I", EntityType.SPIDER,
            "§7A basic tarantula broodfather.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.UNCOMMON, 120, 25, 0.1,
            Arrays.asList("§7- Slayer Spawn", "§7- Spider", "§7- Tarantula Power"),
            Arrays.asList("§7- 1x Tarantula Web", "§7- 25x XP", "§7- 10% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Spider Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.TARANTULA_BROODFATHER_II, new AdvancedMobSystem.MobConfig(
            "Tarantula Broodfather II", "§8Tarantula Broodfather II", EntityType.SPIDER,
            "§7A stronger tarantula broodfather.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.RARE, 250, 45, 0.08,
            Arrays.asList("§7- Slayer Spawn", "§7- Spider", "§7- Tarantula Power"),
            Arrays.asList("§7- 2x Tarantula Web", "§7- 45x XP", "§7- 15% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Spider Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.TARANTULA_BROODFATHER_III, new AdvancedMobSystem.MobConfig(
            "Tarantula Broodfather III", "§8Tarantula Broodfather III", EntityType.SPIDER,
            "§7A powerful tarantula broodfather.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.EPIC, 500, 80, 0.05,
            Arrays.asList("§7- Slayer Spawn", "§7- Spider", "§7- Tarantula Power"),
            Arrays.asList("§7- 3x Tarantula Web", "§7- 80x XP", "§7- 25% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Spider Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.TARANTULA_BROODFATHER_IV, new AdvancedMobSystem.MobConfig(
            "Tarantula Broodfather IV", "§8Tarantula Broodfather IV", EntityType.SPIDER,
            "§7An extremely powerful tarantula broodfather.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.LEGENDARY, 1000, 150, 0.03,
            Arrays.asList("§7- Slayer Spawn", "§7- Spider", "§7- Tarantula Power"),
            Arrays.asList("§7- 5x Tarantula Web", "§7- 150x XP", "§7- 40% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Spider Slayer Quest")
        ));
        
        // === WOLF SLAYER MOBS ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.SVEN_PACKMASTER_I, new AdvancedMobSystem.MobConfig(
            "Sven Packmaster I", "§bSven Packmaster I", EntityType.WOLF,
            "§7A basic sven packmaster.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.UNCOMMON, 150, 30, 0.1,
            Arrays.asList("§7- Slayer Spawn", "§7- Wolf", "§7- Sven Power"),
            Arrays.asList("§7- 1x Sven Fur", "§7- 30x XP", "§7- 10% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Wolf Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.SVEN_PACKMASTER_II, new AdvancedMobSystem.MobConfig(
            "Sven Packmaster II", "§bSven Packmaster II", EntityType.WOLF,
            "§7A stronger sven packmaster.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.RARE, 300, 55, 0.08,
            Arrays.asList("§7- Slayer Spawn", "§7- Wolf", "§7- Sven Power"),
            Arrays.asList("§7- 2x Sven Fur", "§7- 55x XP", "§7- 15% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Wolf Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.SVEN_PACKMASTER_III, new AdvancedMobSystem.MobConfig(
            "Sven Packmaster III", "§bSven Packmaster III", EntityType.WOLF,
            "§7A powerful sven packmaster.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.EPIC, 600, 100, 0.05,
            Arrays.asList("§7- Slayer Spawn", "§7- Wolf", "§7- Sven Power"),
            Arrays.asList("§7- 3x Sven Fur", "§7- 100x XP", "§7- 25% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Wolf Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.SVEN_PACKMASTER_IV, new AdvancedMobSystem.MobConfig(
            "Sven Packmaster IV", "§bSven Packmaster IV", EntityType.WOLF,
            "§7An extremely powerful sven packmaster.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.LEGENDARY, 1200, 200, 0.03,
            Arrays.asList("§7- Slayer Spawn", "§7- Wolf", "§7- Sven Power"),
            Arrays.asList("§7- 5x Sven Fur", "§7- 200x XP", "§7- 40% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Wolf Slayer Quest")
        ));
        
        // === ENDERMAN SLAYER MOBS ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.VOIDGLOOM_SERAPH_I, new AdvancedMobSystem.MobConfig(
            "Voidgloom Seraph I", "§5Voidgloom Seraph I", EntityType.ENDERMAN,
            "§7A basic voidgloom seraph.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.UNCOMMON, 200, 40, 0.1,
            Arrays.asList("§7- Slayer Spawn", "§7- Enderman", "§7- Voidgloom Power"),
            Arrays.asList("§7- 1x Void Fragment", "§7- 40x XP", "§7- 10% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Enderman Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.VOIDGLOOM_SERAPH_II, new AdvancedMobSystem.MobConfig(
            "Voidgloom Seraph II", "§5Voidgloom Seraph II", EntityType.ENDERMAN,
            "§7A stronger voidgloom seraph.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.RARE, 400, 75, 0.08,
            Arrays.asList("§7- Slayer Spawn", "§7- Enderman", "§7- Voidgloom Power"),
            Arrays.asList("§7- 2x Void Fragment", "§7- 75x XP", "§7- 15% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Enderman Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.VOIDGLOOM_SERAPH_III, new AdvancedMobSystem.MobConfig(
            "Voidgloom Seraph III", "§5Voidgloom Seraph III", EntityType.ENDERMAN,
            "§7A powerful voidgloom seraph.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.EPIC, 800, 150, 0.05,
            Arrays.asList("§7- Slayer Spawn", "§7- Enderman", "§7- Voidgloom Power"),
            Arrays.asList("§7- 3x Void Fragment", "§7- 150x XP", "§7- 25% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Enderman Slayer Quest")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.VOIDGLOOM_SERAPH_IV, new AdvancedMobSystem.MobConfig(
            "Voidgloom Seraph IV", "§5Voidgloom Seraph IV", EntityType.ENDERMAN,
            "§7An extremely powerful voidgloom seraph.",
            AdvancedMobSystem.MobCategory.SLAYER, AdvancedMobSystem.MobRarity.LEGENDARY, 1600, 300, 0.03,
            Arrays.asList("§7- Slayer Spawn", "§7- Enderman", "§7- Voidgloom Power"),
            Arrays.asList("§7- 5x Void Fragment", "§7- 300x XP", "§7- 40% Slayer Drop"),
            Arrays.asList("§7- Slayer Spawn", "§7- Enderman Slayer Quest")
        ));
        
        return mobConfigs;
    }
}
