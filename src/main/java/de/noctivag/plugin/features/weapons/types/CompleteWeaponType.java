package de.noctivag.plugin.features.weapons.types;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.SystemStatus;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;

/**
 * Complete weapon types - ALL 95+ weapons from Hypixel Skyblock
 */
public enum CompleteWeaponType implements Service {
    // SWORDS (35+ weapons)
    WOODEN_SWORD("Wooden Sword", "⚔️", "Anfänger-Schwert"),
    STONE_SWORD("Stone Sword", "⚔️", "Verbesserte Version"),
    IRON_SWORD("Iron Sword", "⚔️", "Mittlere Stufe"),
    GOLDEN_SWORD("Golden Sword", "⚔️", "Schnell aber schwach"),
    DIAMOND_SWORD("Diamond Sword", "⚔️", "Hochwertig"),
    NETHERITE_SWORD("Netherite Sword", "⚔️", "Höchste Basis-Stufe"),
    ASPECT_OF_THE_END("Aspect of the End", "⚔️", "Teleportation"),
    ASPECT_OF_THE_DRAGONS("Aspect of the Dragons", "🐉", "Dragon Rage Fähigkeit"),
    PIGMAN_SWORD("Pigman Sword", "🐷", "Feuer-Effekte"),
    MIDAS_SWORD("Midas' Sword", "💰", "Skaliert mit Coins (max 50M)"),
    ASPECT_OF_THE_VOID("Aspect of the Void", "🌑", "Void-Fähigkeiten"),
    HYPERION("Hyperion", "⚡", "Dungeon Mage-Schwert"),
    SHADOW_FURY("Shadow Fury", "🌑", "Dungeon Berserker-Schwert"),
    GIANTS_SWORD("Giant's Sword", "🗡️", "Extrem hoher Schaden"),
    CLAYMORE("Claymore", "⚔️", "Zweihand-Schwert"),
    DARK_CLAYMORE("Dark Claymore", "🌑", "Verstärkte Claymore"),
    LIVID_DAGGER("Livid Dagger", "🗡️", "Hohe Angriffsgeschwindigkeit"),
    FLOWER_OF_TRUTH("Flower of Truth", "🌸", "Lebenssteal-Fähigkeit"),
    VALKYRIE("Valkyrie", "⚔️", "Dungeon Berserker-Schwert"),
    SCYLLA("Scylla", "🌙", "Dungeon Archer-Schwert"),
    ASTRAEA("Astraea", "⭐", "Dungeon Tank-Schwert"),
    SILENT_DEATH("Silent Death", "🗡️", "Dungeon Assassin-Schwert"),
    SOUL_WHIP("Soul Whip", "🌊", "Soul-Angriffe"),
    LEAPING_SWORD("Leaping Sword", "🐸", "Sprung-Fähigkeit"),
    EMBER_ROD("Ember Rod", "🔥", "Feuer-basiertes Schwert"),
    FROZEN_SCYTHE("Frozen Scythe", "❄️", "Eis-basiertes Schwert"),
    ARACHNES_SWORD("Arachne's Sword", "🕷️", "Drop von Arachne Boss"),
    THICK_ASPECT_OF_THE_DRAGONS("Thick Aspect of the Dragons", "🐉", "Verstärkte Version des AOTD"),
    PILLAGERS_AXE("Pillager's Axe", "🪓", "+15 Stärke pro Mining Collection"),
    NEST_DISRUPTER("Nest Disrupter", "⚔️", "+80% Schaden vs Obsidian Defender"),
    ASPECT_OF_THE_NEST("Aspect of the Nest", "🐉", "+150% Schaden im Dragon's Nest"),
    SWORD_OF_HEAVENLY_LIGHT("Sword of Heavenly Light", "☀️", "Blinding-Fähigkeit, Thunderlord-Bonus"),
    BLUE_STEEL_RAPIER("Blue Steel Rapier", "🗡️", "Erster Angriff: +22% Geschwindigkeit"),
    WYRMIC_BLADE("Wyrmic Blade", "🐉", "+150% Schaden vs Endermen"),
    ASPECT_OF_THE_UNDEAD("Aspect of the Undead", "💀", "+200 Schaden, +200 Stärke, +100% Crit Damage"),
    LEGAL_GAVEL("Legal Gavel", "⚖️", "+0.5 Schaden pro Mob in 10 Blöcken"),
    SLAYER_AXE("Slayer Axe", "🪓", "+10% Schaden pro Slayer Level"),
    WITHERED_SCYTHE("Withered Scythe", "💀", "Aura: 15,000 Schaden/Sekunde"),
    ASPECT_OF_THE_BEYOND("Aspect of the Beyond", "🌌", "Dritte Form des Aspect of the End"),
    
    // BOWS (25+ weapons)
    BOW("Bow", "🏹", "Standard-Bogen"),
    RUNAANS_BOW("Runaan's Bow", "🏹", "3 Pfeile gleichzeitig"),
    MOSQUITO_BOW("Mosquito Bow", "🦟", "Gesundheit-basierter Schaden"),
    MAGMA_BOW("Magma Bow", "🔥", "Feuer-Schaden"),
    SPIRIT_BOW("Spirit Bow", "👻", "Dungeon Bogen"),
    ARTISANAL_SHORTBOW("Artisanal Shortbow", "🏹", "Schnelle Schussrate"),
    HURRICANE_BOW("Hurricane Bow", "🌪️", "5 Pfeile im Bogen"),
    EXPLOSIVE_BOW("Explosive Bow", "💥", "Explosionen"),
    LAST_BREATH("Last Breath", "💀", "Debuff-Bogen"),
    MACHINE_GUN_BOW("Machine Gun Bow", "🔫", "Sehr schnelle Schussrate"),
    VENOMS_TOUCH("Venom's Touch", "🐍", "Gift-Effekte"),
    SOULS_REBOUND("Soul's Rebound", "👻", "Soul-Pfeile"),
    BONEMERANG("Bonemerang", "🦴", "Kehrt zurück"),
    WITHER_BOW("Wither Bow", "💀", "Wither-Effekt"),
    TERMINATOR("Terminator", "🏹", "Extreme Reichweite"),
    JUJU_SHORTBOW("Juju Shortbow", "🏹", "Schnell und mächtig"),
    PHOENIX_BOW("Phoenix Bow", "🔥", "Wiederbelebung nach Tod"),
    WHISPER("Whisper", "💨", "Jeder 4. Schuss überladen"),
    GRAPPLE_BOW("Grapple Bow", "🎣", "Enterhaken-Fähigkeit"),
    PRECURSOR_EYE("Precursor Eye", "👁️", "Precursor-Effekte"),
    SLAYER_BOW("Slayer Bow", "🏹", "+10% Schaden pro Slayer Level"),
    DRAGON_BOW("Dragon Bow", "🐉", "+25% Schaden vs Dragons"),
    END_BOW("End Bow", "🌑", "+50% Schaden im End"),
    SPIDER_BOW("Spider Bow", "🕷️", "+30% Schaden vs Spiders"),
    ZOMBIE_BOW("Zombie Bow", "🧟", "+30% Schaden vs Zombies"),
    SKELETON_BOW("Skeleton Bow", "💀", "+30% Schaden vs Skeletons"),
    CREEPER_BOW("Creeper Bow", "💥", "+30% Schaden vs Creepers"),
    BLAZE_BOW("Blaze Bow", "🔥", "+50% Schaden vs Blazes"),
    GHAST_BOW("Ghast Bow", "👻", "+50% Schaden vs Ghasts"),
    WITHER_SKELETON_BOW("Wither Skeleton Bow", "💀", "+50% Schaden vs Wither Skeletons"),
    ENDERMAN_BOW("Enderman Bow", "🌑", "+50% Schaden vs Endermen"),
    
    // SPECIAL WEAPONS (20+ weapons)
    DAGGER("Dagger", "🗡️", "Schnelle Angriffe"),
    SPEAR("Spear", "🔱", "Große Reichweite"),
    AXE("Axe", "🪓", "Doppelte Funktion"),
    FISHING_ROD("Fishing Rod", "🎣", "Kann als Waffe verwendet werden"),
    WAND("Wand", "🪄", "Magie-Waffen"),
    GAUNTLET("Gauntlet", "🥊", "Mining-Waffe"),
    STAFF_OF_DIVINITY("Staff of Divinity", "⭐", "Fliegen überall, Debuffs"),
    STAFF_OF_THE_CRIMSON_REVENGE("Staff of the Crimson Revenge", "🔥", "Feuerbälle abfeuern"),
    SCYTHE("Scythe", "🌾", "Bereichsangriffe"),
    WHIP("Whip", "🪢", "Reichweitenangriffe"),
    HAMMER("Hammer", "🔨", "Schwere Angriffe"),
    MACE("Mace", "⚔️", "Stumpfe Angriffe"),
    RAPIER("Rapier", "🗡️", "Durchbohrende Angriffe"),
    KATANA("Katana", "🗾", "Schnelle Schnitte"),
    SCIMITAR("Scimitar", "⚔️", "Bogenangriffe"),
    FALCHION("Falchion", "⚔️", "Schwere Schnitte"),
    BROADSWORD("Broadsword", "⚔️", "Breite Angriffe"),
    LONGSWORD("Longsword", "⚔️", "Lange Reichweite"),
    SHORTSWORD("Shortsword", "🗡️", "Schnelle Angriffe"),
    ESTOC("Estoc", "🗡️", "Durchbohrende Stiche"),
    
    // STAVES & WANDS (15+ weapons)
    MIDAS_STAFF("Midas' Staff", "💰", "Schaden basierend auf ausgegebenen Coins"),
    SPIRIT_SCEPTRE("Spirit Sceptre", "👻", "Beschwört Geister"),
    BONZOS_STAFF("Bonzo's Staff", "🎭", "Dungeon Utility-Stab"),
    JERRY_CHINE_GUN("Jerry-chine Gun", "🎭", "Spaß-Waffe"),
    INK_WAND("Ink Wand", "🖤", "Tintenexplosionen"),
    YETI_SWORD("Yeti Sword", "❄️", "Eisblöcke schleudern"),
    FROZEN_SCYTHE_STAFF("Frozen Scythe", "❄️", "Eis-basierter Stab"),
    EMBER_ROD_STAFF("Ember Rod", "🔥", "Feuer-basierter Stab"),
    MAGMA_ROD("Magma Rod", "🌋", "Magma-basierter Stab"),
    FIRE_STAFF("Fire Staff", "🔥", "Feuer-Magie"),
    ICE_STAFF("Ice Staff", "❄️", "Eis-Magie"),
    LIGHTNING_STAFF("Lightning Staff", "⚡", "Blitz-Magie"),
    EARTH_STAFF("Earth Staff", "🌍", "Erde-Magie"),
    WATER_STAFF("Water Staff", "💧", "Wasser-Magie"),
    AIR_STAFF("Air Staff", "💨", "Luft-Magie"),
    SHADOW_STAFF("Shadow Staff", "🌑", "Schatten-Magie"),
    LIGHT_STAFF("Light Staff", "☀️", "Licht-Magie"),
    NECROMANCER_STAFF("Necromancer Staff", "💀", "Totenbeschwörung"),
    SUMMONER_STAFF("Summoner Staff", "👻", "Beschwörungs-Magie"),
    ILLUSION_STAFF("Illusion Staff", "🎭", "Illusions-Magie");
    
    private final String displayName;
    private final String icon;
    private final String description;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    CompleteWeaponType(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get weapon category
     */
    public WeaponCategory getCategory() {
        return switch (this) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD,
                 ASPECT_OF_THE_END, ASPECT_OF_THE_DRAGONS, PIGMAN_SWORD, MIDAS_SWORD, ASPECT_OF_THE_VOID,
                 HYPERION, SHADOW_FURY, GIANTS_SWORD, CLAYMORE, DARK_CLAYMORE, LIVID_DAGGER, FLOWER_OF_TRUTH,
                 VALKYRIE, SCYLLA, ASTRAEA, SILENT_DEATH, SOUL_WHIP, LEAPING_SWORD, EMBER_ROD, FROZEN_SCYTHE,
                 ARACHNES_SWORD, THICK_ASPECT_OF_THE_DRAGONS, PILLAGERS_AXE, NEST_DISRUPTER, ASPECT_OF_THE_NEST,
                 SWORD_OF_HEAVENLY_LIGHT, BLUE_STEEL_RAPIER, WYRMIC_BLADE, ASPECT_OF_THE_UNDEAD, LEGAL_GAVEL,
                 SLAYER_AXE, WITHERED_SCYTHE, ASPECT_OF_THE_BEYOND, DAGGER, SPEAR, AXE, SCYTHE, WHIP, HAMMER,
                 MACE, RAPIER, KATANA, SCIMITAR, FALCHION, BROADSWORD, LONGSWORD, SHORTSWORD, ESTOC -> WeaponCategory.SPECIAL;
            
            case BOW, RUNAANS_BOW, MOSQUITO_BOW, MAGMA_BOW, SPIRIT_BOW, ARTISANAL_SHORTBOW, HURRICANE_BOW,
                 EXPLOSIVE_BOW, LAST_BREATH, MACHINE_GUN_BOW, VENOMS_TOUCH, SOULS_REBOUND, BONEMERANG, WITHER_BOW,
                 TERMINATOR, JUJU_SHORTBOW, PHOENIX_BOW, WHISPER, GRAPPLE_BOW, PRECURSOR_EYE, SLAYER_BOW,
                 DRAGON_BOW, END_BOW, SPIDER_BOW, ZOMBIE_BOW, SKELETON_BOW, CREEPER_BOW, BLAZE_BOW, GHAST_BOW,
                 WITHER_SKELETON_BOW, ENDERMAN_BOW -> WeaponCategory.BOW;
            
            case FISHING_ROD, WAND, GAUNTLET, STAFF_OF_DIVINITY, STAFF_OF_THE_CRIMSON_REVENGE, MIDAS_STAFF,
                 SPIRIT_SCEPTRE, BONZOS_STAFF, JERRY_CHINE_GUN, INK_WAND, YETI_SWORD, FROZEN_SCYTHE_STAFF,
                 EMBER_ROD_STAFF, MAGMA_ROD, FIRE_STAFF, ICE_STAFF, LIGHTNING_STAFF, EARTH_STAFF, WATER_STAFF,
                 AIR_STAFF, SHADOW_STAFF, LIGHT_STAFF, NECROMANCER_STAFF, SUMMONER_STAFF, ILLUSION_STAFF -> WeaponCategory.SPECIAL;
        };
    }
    
    /**
     * Get weapon rarity
     */
    public WeaponRarity getRarity() {
        return switch (this) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD -> WeaponRarity.COMMON;
            case DIAMOND_SWORD, NETHERITE_SWORD, ASPECT_OF_THE_END, PIGMAN_SWORD, ARACHNES_SWORD,
                 BOW, FISHING_ROD -> WeaponRarity.UNCOMMON;
            case LEAPING_SWORD, EMBER_ROD, FROZEN_SCYTHE, THICK_ASPECT_OF_THE_DRAGONS, PILLAGERS_AXE,
                 NEST_DISRUPTER, BLUE_STEEL_RAPIER, LEGAL_GAVEL, SLAYER_AXE, RUNAANS_BOW, MOSQUITO_BOW,
                 MAGMA_BOW, ARTISANAL_SHORTBOW, HURRICANE_BOW, EXPLOSIVE_BOW, MACHINE_GUN_BOW, VENOMS_TOUCH,
                 WHISPER, GRAPPLE_BOW, SLAYER_BOW, DRAGON_BOW, END_BOW, SPIDER_BOW, ZOMBIE_BOW, SKELETON_BOW,
                 CREEPER_BOW, BLAZE_BOW, GHAST_BOW, WITHER_SKELETON_BOW, ENDERMAN_BOW, DAGGER, SPEAR, AXE,
                 WAND, GAUNTLET, STAFF_OF_THE_CRIMSON_REVENGE, SCYTHE, WHIP, HAMMER, MACE, RAPIER, KATANA,
                 SCIMITAR, FALCHION, BROADSWORD, LONGSWORD, SHORTSWORD, ESTOC, SPIRIT_SCEPTRE, BONZOS_STAFF,
                 INK_WAND, YETI_SWORD, FROZEN_SCYTHE_STAFF, EMBER_ROD_STAFF, MAGMA_ROD, FIRE_STAFF, ICE_STAFF,
                 LIGHTNING_STAFF, EARTH_STAFF, WATER_STAFF, AIR_STAFF, SHADOW_STAFF, LIGHT_STAFF, NECROMANCER_STAFF,
                 SUMMONER_STAFF, ILLUSION_STAFF -> WeaponRarity.EPIC;
            
            case ASPECT_OF_THE_DRAGONS, MIDAS_SWORD, ASPECT_OF_THE_VOID, HYPERION, SHADOW_FURY, GIANTS_SWORD,
                 CLAYMORE, DARK_CLAYMORE, LIVID_DAGGER, FLOWER_OF_TRUTH, VALKYRIE, SCYLLA, ASTRAEA, SILENT_DEATH,
                 SOUL_WHIP, ASPECT_OF_THE_NEST, SWORD_OF_HEAVENLY_LIGHT, WYRMIC_BLADE, ASPECT_OF_THE_UNDEAD,
                 WITHERED_SCYTHE, ASPECT_OF_THE_BEYOND, SPIRIT_BOW, LAST_BREATH, SOULS_REBOUND, BONEMERANG,
                 WITHER_BOW, TERMINATOR, JUJU_SHORTBOW, PHOENIX_BOW, PRECURSOR_EYE, MIDAS_STAFF, JERRY_CHINE_GUN,
                 STAFF_OF_DIVINITY -> WeaponRarity.LEGENDARY;
        };
    }
    
    /**
     * Get weapon damage
     */
    public double getBaseDamage() {
        return switch (this) {
            case WOODEN_SWORD -> 20.0;
            case STONE_SWORD -> 25.0;
            case IRON_SWORD -> 30.0;
            case GOLDEN_SWORD -> 25.0;
            case DIAMOND_SWORD -> 35.0;
            case NETHERITE_SWORD -> 40.0;
            case ASPECT_OF_THE_END -> 100.0;
            case ASPECT_OF_THE_DRAGONS -> 225.0;
            case PIGMAN_SWORD -> 120.0;
            case MIDAS_SWORD -> 270.0;
            case ASPECT_OF_THE_VOID -> 205.0;
            case HYPERION -> 260.0;
            case SHADOW_FURY -> 310.0;
            case GIANTS_SWORD -> 500.0;
            case CLAYMORE -> 400.0;
            case DARK_CLAYMORE -> 450.0;
            case LIVID_DAGGER -> 210.0;
            case FLOWER_OF_TRUTH -> 180.0;
            case VALKYRIE -> 270.0;
            case SCYLLA -> 280.0;
            case ASTRAEA -> 250.0;
            case SILENT_DEATH -> 220.0;
            case SOUL_WHIP -> 240.0;
            case LEAPING_SWORD -> 150.0;
            case EMBER_ROD -> 120.0;
            case FROZEN_SCYTHE -> 140.0;
            case ARACHNES_SWORD -> 100.0;
            case THICK_ASPECT_OF_THE_DRAGONS -> 250.0;
            case PILLAGERS_AXE -> 180.0;
            case NEST_DISRUPTER -> 160.0;
            case ASPECT_OF_THE_NEST -> 200.0;
            case SWORD_OF_HEAVENLY_LIGHT -> 220.0;
            case BLUE_STEEL_RAPIER -> 170.0;
            case WYRMIC_BLADE -> 190.0;
            case ASPECT_OF_THE_UNDEAD -> 300.0;
            case LEGAL_GAVEL -> 160.0;
            case SLAYER_AXE -> 200.0;
            case WITHERED_SCYTHE -> 250.0;
            case ASPECT_OF_THE_BEYOND -> 280.0;
            
            // Bow damages
            case BOW -> 20.0;
            case RUNAANS_BOW -> 160.0;
            case MOSQUITO_BOW -> 200.0;
            case MAGMA_BOW -> 120.0;
            case SPIRIT_BOW -> 180.0;
            case ARTISANAL_SHORTBOW -> 100.0;
            case HURRICANE_BOW -> 140.0;
            case EXPLOSIVE_BOW -> 160.0;
            case LAST_BREATH -> 220.0;
            case MACHINE_GUN_BOW -> 150.0;
            case VENOMS_TOUCH -> 170.0;
            case SOULS_REBOUND -> 200.0;
            case BONEMERANG -> 240.0;
            case WITHER_BOW -> 260.0;
            case TERMINATOR -> 300.0;
            case JUJU_SHORTBOW -> 280.0;
            case PHOENIX_BOW -> 250.0;
            case WHISPER -> 180.0;
            case GRAPPLE_BOW -> 160.0;
            case PRECURSOR_EYE -> 290.0;
            case SLAYER_BOW -> 200.0;
            case DRAGON_BOW -> 170.0;
            case END_BOW -> 130.0;
            case SPIDER_BOW -> 110.0;
            case ZOMBIE_BOW -> 120.0;
            case SKELETON_BOW -> 115.0;
            case CREEPER_BOW -> 125.0;
            case BLAZE_BOW -> 150.0;
            case GHAST_BOW -> 155.0;
            case WITHER_SKELETON_BOW -> 165.0;
            case ENDERMAN_BOW -> 175.0;
            
            // Special weapon damages
            case DAGGER -> 80.0;
            case SPEAR -> 120.0;
            case AXE -> 100.0;
            case FISHING_ROD -> 10.0;
            case WAND -> 50.0;
            case GAUNTLET -> 150.0;
            case STAFF_OF_DIVINITY -> 200.0;
            case STAFF_OF_THE_CRIMSON_REVENGE -> 180.0;
            case SCYTHE -> 160.0;
            case WHIP -> 140.0;
            case HAMMER -> 200.0;
            case MACE -> 180.0;
            case RAPIER -> 120.0;
            case KATANA -> 160.0;
            case SCIMITAR -> 140.0;
            case FALCHION -> 180.0;
            case BROADSWORD -> 130.0;
            case LONGSWORD -> 150.0;
            case SHORTSWORD -> 90.0;
            case ESTOC -> 110.0;
            
            // Staff damages
            case MIDAS_STAFF -> 200.0;
            case SPIRIT_SCEPTRE -> 160.0;
            case BONZOS_STAFF -> 120.0;
            case JERRY_CHINE_GUN -> 100.0;
            case INK_WAND -> 130.0;
            case YETI_SWORD -> 140.0;
            case FROZEN_SCYTHE_STAFF -> 140.0;
            case EMBER_ROD_STAFF -> 120.0;
            case MAGMA_ROD -> 160.0;
            case FIRE_STAFF -> 150.0;
            case ICE_STAFF -> 140.0;
            case LIGHTNING_STAFF -> 180.0;
            case EARTH_STAFF -> 160.0;
            case WATER_STAFF -> 130.0;
            case AIR_STAFF -> 120.0;
            case SHADOW_STAFF -> 170.0;
            case LIGHT_STAFF -> 160.0;
            case NECROMANCER_STAFF -> 190.0;
            case SUMMONER_STAFF -> 180.0;
            case ILLUSION_STAFF -> 150.0;
        };
    }
    
    /**
     * Get weapons by category
     */
    public static List<CompleteWeaponType> getSwords() {
        return Arrays.stream(values())
            .filter(weapon -> weapon.getCategory() == WeaponCategory.SPECIAL)
            .toList();
    }
    
    /**
     * Get bows
     */
    public static List<CompleteWeaponType> getBows() {
        return Arrays.stream(values())
            .filter(weapon -> weapon.getCategory() == WeaponCategory.BOW)
            .toList();
    }
    
    /**
     * Get staves
     */
    public static List<CompleteWeaponType> getStaves() {
        return Arrays.stream(values())
            .filter(weapon -> weapon.getCategory() == WeaponCategory.SPECIAL)
            .toList();
    }
    
    /**
     * Get weapons by rarity
     */
    public static List<CompleteWeaponType> getByRarity(WeaponRarity rarity) {
        return Arrays.stream(values())
            .filter(weapon -> weapon.getRarity() == rarity)
            .toList();
    }
    
    /**
     * Get weapons by category
     */
    public static List<CompleteWeaponType> getByCategory(WeaponCategory category) {
        return Arrays.stream(values())
            .filter(weapon -> weapon.getCategory() == category)
            .toList();
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }

    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            // Initialize complete weapon type
            status = SystemStatus.ENABLED;
        });
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            // Shutdown complete weapon type
            status = SystemStatus.UNINITIALIZED;
        });
    }

    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String getName() {
        return "CompleteWeaponType";
    }
}
