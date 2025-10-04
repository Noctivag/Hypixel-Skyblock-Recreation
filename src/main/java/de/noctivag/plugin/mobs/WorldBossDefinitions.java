package de.noctivag.plugin.mobs;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.EntityType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WorldBossDefinitions {
    
    public static Map<AdvancedMobSystem.MobType, AdvancedMobSystem.MobConfig> getWorldBossConfigs() {
        Map<AdvancedMobSystem.MobType, AdvancedMobSystem.MobConfig> mobConfigs = new HashMap<>();
        
        // === WORLD BOSSES ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.MAGMA_BOSS, new AdvancedMobSystem.MobConfig(
            "Magma Boss", "§6Magma Boss", EntityType.MAGMA_CUBE,
            "§7A powerful magma boss that spawns in the nether.",
            AdvancedMobSystem.MobCategory.WORLD_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 2000, 100, 0.01,
            Arrays.asList("§7- World Boss", "§7- Fire Immunity", "§7- Magma Power"),
            Arrays.asList("§7- Magma Bow", "§7- 500x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Nether Spawn", "§7- World Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.ARACHNE, new AdvancedMobSystem.MobConfig(
            "Arachne", "§8Arachne", EntityType.SPIDER,
            "§7A powerful spider boss found in the spider's den.",
            AdvancedMobSystem.MobCategory.WORLD_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 1500, 80, 0.01,
            Arrays.asList("§7- World Boss", "§7- Spider", "§7- Arachne Power"),
            Arrays.asList("§7- Arachne's Keeper", "§7- 400x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Spider's Den", "§7- World Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.DRAGON, new AdvancedMobSystem.MobConfig(
            "Dragon", "§5Dragon", EntityType.ENDER_DRAGON,
            "§7A powerful dragon that spawns in the end.",
            AdvancedMobSystem.MobCategory.WORLD_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 3000, 150, 0.005,
            Arrays.asList("§7- World Boss", "§7- Flight", "§7- Dragon Power"),
            Arrays.asList("§7- Dragon Armor", "§7- 1000x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- End Spawn", "§7- World Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.WITHER, new AdvancedMobSystem.MobConfig(
            "Wither", "§8Wither", EntityType.WITHER,
            "§7A powerful wither boss.",
            AdvancedMobSystem.MobCategory.WORLD_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 2500, 120, 0.005,
            Arrays.asList("§7- World Boss", "§7- Wither Effect", "§7- Wither Power"),
            Arrays.asList("§7- Wither Armor", "§7- 800x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- World Spawn", "§7- World Boss Spawn")
        ));
        
        // === SPECIAL BOSSES ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.INFERNO_DEMONLORD, new AdvancedMobSystem.MobConfig(
            "Inferno Demonlord", "§cInferno Demonlord", EntityType.BLAZE,
            "§7A powerful demon lord from the inferno.",
            AdvancedMobSystem.MobCategory.SPECIAL_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 5000, 200, 0.001,
            Arrays.asList("§7- Special Boss", "§7- Fire Immunity", "§7- Demon Power"),
            Arrays.asList("§7- Inferno Armor", "§7- 2000x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Inferno Spawn", "§7- Special Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.KUUDRA, new AdvancedMobSystem.MobConfig(
            "Kuudra", "§9Kuudra", EntityType.SQUID,
            "§7A powerful sea creature boss.",
            AdvancedMobSystem.MobCategory.SPECIAL_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 4000, 180, 0.001,
            Arrays.asList("§7- Special Boss", "§7- Water", "§7- Kuudra Power"),
            Arrays.asList("§7- Kuudra Armor", "§7- 1500x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Ocean Spawn", "§7- Special Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.NECRON, new AdvancedMobSystem.MobConfig(
            "Necron", "§8Necron", EntityType.WITHER_SKELETON,
            "§7A powerful necromancer boss.",
            AdvancedMobSystem.MobCategory.SPECIAL_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 6000, 250, 0.001,
            Arrays.asList("§7- Special Boss", "§7- Necromancy", "§7- Necron Power"),
            Arrays.asList("§7- Necron Armor", "§7- 3000x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Special Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.SADAN, new AdvancedMobSystem.MobConfig(
            "Sadan", "§6Sadan", EntityType.ZOMBIE,
            "§7A powerful zombie boss.",
            AdvancedMobSystem.MobCategory.SPECIAL_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 3500, 160, 0.001,
            Arrays.asList("§7- Special Boss", "§7- Undead", "§7- Sadan Power"),
            Arrays.asList("§7- Sadan Armor", "§7- 1200x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Special Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.LIVID, new AdvancedMobSystem.MobConfig(
            "Livid", "§5Livid", EntityType.ENDERMAN,
            "§7A powerful enderman boss.",
            AdvancedMobSystem.MobCategory.SPECIAL_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 2800, 140, 0.001,
            Arrays.asList("§7- Special Boss", "§7- Teleportation", "§7- Livid Power"),
            Arrays.asList("§7- Livid Armor", "§7- 1000x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Special Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.SCARF, new AdvancedMobSystem.MobConfig(
            "Scarf", "§fScarf", EntityType.SKELETON,
            "§7A powerful skeleton boss.",
            AdvancedMobSystem.MobCategory.SPECIAL_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 3200, 150, 0.001,
            Arrays.asList("§7- Special Boss", "§7- Ranged Attack", "§7- Scarf Power"),
            Arrays.asList("§7- Scarf Armor", "§7- 1100x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Special Boss Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.THORN, new AdvancedMobSystem.MobConfig(
            "Thorn", "§aThorn", EntityType.SPIDER,
            "§7A powerful spider boss.",
            AdvancedMobSystem.MobCategory.SPECIAL_BOSS, AdvancedMobSystem.MobRarity.LEGENDARY, 3000, 145, 0.001,
            Arrays.asList("§7- Special Boss", "§7- Spider", "§7- Thorn Power"),
            Arrays.asList("§7- Thorn Armor", "§7- 1050x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Special Boss Spawn")
        ));
        
        return mobConfigs;
    }
}
