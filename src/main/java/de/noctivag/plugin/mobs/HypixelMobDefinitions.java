package de.noctivag.plugin.mobs;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.EntityType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HypixelMobDefinitions {
    
    public static Map<AdvancedMobSystem.MobType, AdvancedMobSystem.MobConfig> getHypixelMobConfigs() {
        Map<AdvancedMobSystem.MobType, AdvancedMobSystem.MobConfig> mobConfigs = new HashMap<>();
        
        // === HUB AREA MOBS ===
        
        // Graveyard Zombies
        mobConfigs.put(de.noctivag.plugin.mobs.AdvancedMobSystem.MobType.GRAVEYARD_ZOMBIE, new AdvancedMobSystem.MobConfig(
            "Graveyard Zombie", "§2Graveyard Zombie", EntityType.ZOMBIE,
            "§7A zombie found in the graveyard area.",
            de.noctivag.plugin.mobs.AdvancedMobSystem.MobCategory.HUB, de.noctivag.plugin.mobs.AdvancedMobSystem.MobRarity.COMMON, 25, 5, 0.2,
            Arrays.asList("§7- Basic Attack", "§7- Undead", "§7- Graveyard Spawn"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 2x XP", "§7- 5% Rotten Flesh"),
            Arrays.asList("§7- Graveyard Area", "§7- Hub World")
        ));
        
        mobConfigs.put(de.noctivag.plugin.mobs.AdvancedMobSystem.MobType.ZOMBIE_VILLAGER, new AdvancedMobSystem.MobConfig(
            "Zombie Villager", "§2Zombie Villager", EntityType.ZOMBIE_VILLAGER,
            "§7A zombified villager from the graveyard.",
            AdvancedMobSystem.MobCategory.HUB, AdvancedMobSystem.MobRarity.UNCOMMON, 30, 6, 0.18,
            Arrays.asList("§7- Basic Attack", "§7- Undead", "§7- Villager Traits"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 3x XP", "§7- 10% Rotten Flesh"),
            Arrays.asList("§7- Graveyard Area", "§7- Hub World")
        ));
        
        // Crypt Ghouls
        mobConfigs.put(AdvancedMobSystem.MobType.CRYPT_GHOUL, new AdvancedMobSystem.MobConfig(
            "Crypt Ghoul", "§8Crypt Ghoul", EntityType.ZOMBIE,
            "§7A stronger zombie found in the crypts.",
            AdvancedMobSystem.MobCategory.HUB, AdvancedMobSystem.MobRarity.UNCOMMON, 50, 8, 0.15,
            Arrays.asList("§7- Strong Attack", "§7- Undead", "§7- Crypt Spawn"),
            Arrays.asList("§7- 2x Rotten Flesh", "§7- 5x XP", "§7- 15% Rotten Flesh"),
            Arrays.asList("§7- Crypt Area", "§7- Hub World")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.CRYPT_LURKER, new AdvancedMobSystem.MobConfig(
            "Crypt Lurker", "§8Crypt Lurker", EntityType.ZOMBIE,
            "§7A stealthy zombie that lurks in the crypts.",
            AdvancedMobSystem.MobCategory.HUB, AdvancedMobSystem.MobRarity.RARE, 75, 12, 0.12,
            Arrays.asList("§7- Stealth Attack", "§7- Undead", "§7- Crypt Spawn"),
            Arrays.asList("§7- 3x Rotten Flesh", "§7- 8x XP", "§7- 20% Rotten Flesh"),
            Arrays.asList("§7- Crypt Area", "§7- Hub World")
        ));
        
        // === END DIMENSION MOBS ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.ZEALOT, new AdvancedMobSystem.MobConfig(
            "Zealot", "§5Zealot", EntityType.ENDERMAN,
            "§7A special Enderman that can drop special items.",
            AdvancedMobSystem.MobCategory.END, AdvancedMobSystem.MobRarity.RARE, 100, 15, 0.1,
            Arrays.asList("§7- Teleportation", "§7- End Power", "§7- Special Drops"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 10x XP", "§7- 0.1% Special Drop"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension", "§7- Special Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.SPECIAL_ZEALOT, new AdvancedMobSystem.MobConfig(
            "Special Zealot", "§dSpecial Zealot", EntityType.ENDERMAN,
            "§7A very rare Zealot with guaranteed special drops.",
            AdvancedMobSystem.MobCategory.END, AdvancedMobSystem.MobRarity.EPIC, 200, 25, 0.05,
            Arrays.asList("§7- Teleportation", "§7- End Power", "§7- Guaranteed Drops"),
            Arrays.asList("§7- 2x Ender Pearl", "§7- 25x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension", "§7- Very Rare Spawn")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.ENDERMITE, new AdvancedMobSystem.MobConfig(
            "Endermite", "§5Endermite", EntityType.ENDERMITE,
            "§7A small end creature.",
            AdvancedMobSystem.MobCategory.END, AdvancedMobSystem.MobRarity.COMMON, 8, 2, 0.4,
            Arrays.asList("§7- Small Size", "§7- End Power"),
            Arrays.asList("§7- 1x XP"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension")
        ));
        
        // === NETHER DIMENSION MOBS ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.MAGMA_CUBE, new AdvancedMobSystem.MobConfig(
            "Magma Cube", "§6Magma Cube", EntityType.MAGMA_CUBE,
            "§7A bouncing fire creature.",
            AdvancedMobSystem.MobCategory.NETHER, AdvancedMobSystem.MobRarity.COMMON, 16, 3, 0.3,
            Arrays.asList("§7- Bouncing", "§7- Fire Immunity", "§7- Size Variation"),
            Arrays.asList("§7- 1x Magma Cream", "§7- 3x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.GHAST, new AdvancedMobSystem.MobConfig(
            "Ghast", "§fGhast", EntityType.GHAST,
            "§7A floating fire creature.",
            AdvancedMobSystem.MobCategory.NETHER, AdvancedMobSystem.MobRarity.UNCOMMON, 10, 5, 0.2,
            Arrays.asList("§7- Flight", "§7- Fireball Attack", "§7- Fire Immunity"),
            Arrays.asList("§7- 1x Ghast Tear", "§7- 5x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.PIGLIN, new AdvancedMobSystem.MobConfig(
            "Piglin", "§6Piglin", EntityType.PIGLIN,
            "§7A nether humanoid creature.",
            AdvancedMobSystem.MobCategory.NETHER, AdvancedMobSystem.MobRarity.COMMON, 16, 5, 0.25,
            Arrays.asList("§7- Gold Attraction", "§7- Nether Spawn"),
            Arrays.asList("§7- 1x Gold Nugget", "§7- 3x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.PIGLIN_BRUTE, new AdvancedMobSystem.MobConfig(
            "Piglin Brute", "§cPiglin Brute", EntityType.PIGLIN_BRUTE,
            "§7A stronger, more aggressive Piglin.",
            AdvancedMobSystem.MobCategory.NETHER, AdvancedMobSystem.MobRarity.UNCOMMON, 50, 8, 0.15,
            Arrays.asList("§7- Strong Attack", "§7- Gold Attraction", "§7- Aggressive"),
            Arrays.asList("§7- 2x Gold Nugget", "§7- 8x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.HOGLIN, new AdvancedMobSystem.MobConfig(
            "Hoglin", "§6Hoglin", EntityType.HOGLIN,
            "§7A large nether beast.",
            AdvancedMobSystem.MobCategory.NETHER, AdvancedMobSystem.MobRarity.UNCOMMON, 40, 9, 0.2,
            Arrays.asList("§7- Charge Attack", "§7- Nether Spawn"),
            Arrays.asList("§7- 1x Raw Porkchop", "§7- 6x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.ZOGLIN, new AdvancedMobSystem.MobConfig(
            "Zoglin", "§cZoglin", EntityType.ZOGLIN,
            "§7An undead nether beast.",
            AdvancedMobSystem.MobCategory.NETHER, AdvancedMobSystem.MobRarity.RARE, 60, 12, 0.12,
            Arrays.asList("§7- Charge Attack", "§7- Undead", "§7- Aggressive"),
            Arrays.asList("§7- 2x Raw Porkchop", "§7- 10x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.STRIDER, new AdvancedMobSystem.MobConfig(
            "Strider", "§6Strider", EntityType.STRIDER,
            "§7A lava-walking creature.",
            AdvancedMobSystem.MobCategory.NETHER, AdvancedMobSystem.MobRarity.COMMON, 20, 3, 0.3,
            Arrays.asList("§7- Lava Walking", "§7- Nether Spawn"),
            Arrays.asList("§7- 1x String", "§7- 2x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));
        
        // === DUNGEON MOBS ===
        
        mobConfigs.put(AdvancedMobSystem.MobType.DUNGEON_ZOMBIE, new AdvancedMobSystem.MobConfig(
            "Dungeon Zombie", "§2Dungeon Zombie", EntityType.ZOMBIE,
            "§7A zombie found in dungeons.",
            AdvancedMobSystem.MobCategory.DUNGEON, AdvancedMobSystem.MobRarity.COMMON, 100, 15, 0.2,
            Arrays.asList("§7- Dungeon Spawn", "§7- Undead", "§7- Dungeon Power"),
            Arrays.asList("§7- 2x Rotten Flesh", "§7- 10x XP", "§7- 5% Dungeon Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Areas")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.DUNGEON_SKELETON, new AdvancedMobSystem.MobConfig(
            "Dungeon Skeleton", "§fDungeon Skeleton", EntityType.SKELETON,
            "§7A skeleton found in dungeons.",
            AdvancedMobSystem.MobCategory.DUNGEON, AdvancedMobSystem.MobRarity.COMMON, 80, 12, 0.25,
            Arrays.asList("§7- Dungeon Spawn", "§7- Ranged Attack", "§7- Dungeon Power"),
            Arrays.asList("§7- 2x Bone", "§7- 8x XP", "§7- 5% Dungeon Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Areas")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.DUNGEON_SPIDER, new AdvancedMobSystem.MobConfig(
            "Dungeon Spider", "§8Dungeon Spider", EntityType.SPIDER,
            "§7A spider found in dungeons.",
            AdvancedMobSystem.MobCategory.DUNGEON, AdvancedMobSystem.MobRarity.COMMON, 60, 10, 0.3,
            Arrays.asList("§7- Dungeon Spawn", "§7- Climbing", "§7- Dungeon Power"),
            Arrays.asList("§7- 2x String", "§7- 6x XP", "§7- 5% Dungeon Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Areas")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.DUNGEON_CREEPER, new AdvancedMobSystem.MobConfig(
            "Dungeon Creeper", "§aDungeon Creeper", EntityType.CREEPER,
            "§7A creeper found in dungeons.",
            AdvancedMobSystem.MobCategory.DUNGEON, AdvancedMobSystem.MobRarity.UNCOMMON, 120, 18, 0.15,
            Arrays.asList("§7- Dungeon Spawn", "§7- Explosion", "§7- Dungeon Power"),
            Arrays.asList("§7- 2x Gunpowder", "§7- 12x XP", "§7- 10% Dungeon Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Areas")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.DUNGEON_ENDERMAN, new AdvancedMobSystem.MobConfig(
            "Dungeon Enderman", "§5Dungeon Enderman", EntityType.ENDERMAN,
            "§7An enderman found in dungeons.",
            AdvancedMobSystem.MobCategory.DUNGEON, AdvancedMobSystem.MobRarity.UNCOMMON, 150, 20, 0.1,
            Arrays.asList("§7- Dungeon Spawn", "§7- Teleportation", "§7- Dungeon Power"),
            Arrays.asList("§7- 2x Ender Pearl", "§7- 15x XP", "§7- 10% Dungeon Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Areas")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.DUNGEON_BLAZE, new AdvancedMobSystem.MobConfig(
            "Dungeon Blaze", "§6Dungeon Blaze", EntityType.BLAZE,
            "§7A blaze found in dungeons.",
            AdvancedMobSystem.MobCategory.DUNGEON, AdvancedMobSystem.MobRarity.UNCOMMON, 100, 16, 0.2,
            Arrays.asList("§7- Dungeon Spawn", "§7- Fire Attack", "§7- Dungeon Power"),
            Arrays.asList("§7- 2x Blaze Rod", "§7- 12x XP", "§7- 10% Dungeon Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Areas")
        ));
        
        mobConfigs.put(AdvancedMobSystem.MobType.DUNGEON_WITHER_SKELETON, new AdvancedMobSystem.MobConfig(
            "Dungeon Wither Skeleton", "§8Dungeon Wither Skeleton", EntityType.WITHER_SKELETON,
            "§7A wither skeleton found in dungeons.",
            AdvancedMobSystem.MobCategory.DUNGEON, AdvancedMobSystem.MobRarity.RARE, 200, 25, 0.08,
            Arrays.asList("§7- Dungeon Spawn", "§7- Wither Effect", "§7- Dungeon Power"),
            Arrays.asList("§7- 2x Wither Skeleton Skull", "§7- 20x XP", "§7- 15% Dungeon Drop"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Areas")
        ));
        
        return mobConfigs;
    }
}
