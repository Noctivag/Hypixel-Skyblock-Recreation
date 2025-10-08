package de.noctivag.skyblock.features.weapons.types;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;

/**
 * Complete weapon types - ALL 95+ weapons from Hypixel Skyblock
 */
public enum CompleteWeaponType implements Service {
    // SWORDS (35+ weapons)
    WOODEN_SWORD("Wooden Sword", "⚔️", "Anfänger-Schwert", 20, 0, 0, 0, 0, null),
    STONE_SWORD("Stone Sword", "⚔️", "Verbesserte Version", 25, 0, 0, 0, 0, null),
    IRON_SWORD("Iron Sword", "⚔️", "Mittlere Stufe", 30, 0, 0, 0, 0, null),
    GOLDEN_SWORD("Golden Sword", "⚔️", "Schnell aber schwach", 25, 0, 0, 0, 0, null),
    DIAMOND_SWORD("Diamond Sword", "⚔️", "Hochwertig", 35, 0, 0, 0, 0, null),
    NETHERITE_SWORD("Netherite Sword", "⚔️", "Höchste Basis-Stufe", 40, 0, 0, 0, 0, null),
    ASPECT_OF_THE_END("Aspect of the End", "⚔️", "Teleportation", 100, 0, 0, 100, 0, new WeaponAbility("Instant Transmission", "Teleportiert dich 8 Blöcke nach vorne.", 50, 0, false)),
    ASPECT_OF_THE_DRAGONS("Aspect of the Dragons", "🐉", "Dragon Rage Fähigkeit", 225, 100, 0, 0, 0, new WeaponAbility("Dragon Rage", "Fügt Gegnern in der Nähe Schaden zu.", 0, 0, false)),
    DUNGEON_ASPECT_OF_THE_DRAGONS("Dungeon Aspect of the Dragons", "🐉", "Dungeonized Version, Dragon Rage Fähigkeit", 225, 100, 0, 0, 0, new WeaponAbility("Dragon Rage", "Fügt Gegnern in der Nähe Schaden zu.", 0, 0, false)),
    STARRED_ASPECT_OF_THE_DRAGONS("Starred Aspect of the Dragons", "⭐🐉", "Starred Dungeon Version, Dragon Rage Fähigkeit", 225, 100, 0, 0, 0, new WeaponAbility("Dragon Rage", "Fügt Gegnern in der Nähe Schaden zu.", 0, 0, false)),
    RECOMBOBULATED_ASPECT_OF_THE_DRAGONS("Recombobulated Aspect of the Dragons", "🔄🐉", "Recombobulated Version, Dragon Rage Fähigkeit", 225, 100, 0, 0, 0, new WeaponAbility("Dragon Rage", "Fügt Gegnern in der Nähe Schaden zu.", 0, 0, false)),
    PIGMAN_SWORD("Pigman Sword", "🐷", "Feuer-Effekte", 120, 100, 0, 0, 0, new WeaponAbility("Burning Souls", "Feuer-AoE um dich herum.", 150, 30, false)),
    MIDAS_SWORD("Midas' Sword", "💰", "Skaliert mit Coins (max 50M)", 270, 120, 0, 0, 0, null),
    GILDED_MIDAS_SWORD("Gilded Midas' Sword", "🥇💰", "Gilded Upgrade, max Damage", 270, 120, 0, 0, 0, null),
    DUNGEON_MIDAS_SWORD("Dungeon Midas' Sword", "💰", "Dungeonized Version, max Damage", 270, 120, 0, 0, 0, null),
    STARRED_MIDAS_SWORD("Starred Midas' Sword", "⭐💰", "Starred Dungeon Version, max Damage", 270, 120, 0, 0, 0, null),
    RECOMBOBULATED_MIDAS_SWORD("Recombobulated Midas' Sword", "🔄💰", "Recombobulated Version, max Damage", 270, 120, 0, 0, 0, null),
    ASPECT_OF_THE_VOID("Aspect of the Void", "🌑", "Void-Fähigkeiten", 205, 100, 0, 0, 0, new WeaponAbility("Void Transmission", "Teleportiert dich 10 Blöcke nach vorne.", 45, 0, false)),
    HYPERION("Hyperion", "⚡", "Dungeon Mage-Schwert", 260, 150, 0, 350, 0, new WeaponAbility("Wither Impact", "Explosion, Teleport, Heilung.", 300, 0, false)),
    WITHERED_HYPERION("Withered Hyperion", "⚡", "Withered Upgrade, Dungeon Mage-Schwert", 260, 150, 0, 350, 0, new WeaponAbility("Wither Impact", "Explosion, Teleport, Heilung.", 300, 0, false)),
    FABLED_HYPERION("Fabled Hyperion", "✨⚡", "Fabled Upgrade, Dungeon Mage-Schwert", 260, 150, 0, 350, 0, new WeaponAbility("Wither Impact", "Explosion, Teleport, Heilung.", 300, 0, false)),
    STARRED_HYPERION("Starred Hyperion", "⭐⚡", "Starred Dungeon Version, Mage-Schwert", 260, 150, 0, 350, 0, new WeaponAbility("Wither Impact", "Explosion, Teleport, Heilung.", 300, 0, false)),
    RECOMBOBULATED_HYPERION("Recombobulated Hyperion", "🔄⚡", "Recombobulated Version, Mage-Schwert", 260, 150, 0, 350, 0, new WeaponAbility("Wither Impact", "Explosion, Teleport, Heilung.", 300, 0, false)),
    SHADOW_FURY("Shadow Fury", "🌑", "Dungeon Berserker-Schwert", 310, 100, 0, 0, 0, new WeaponAbility("Shadow Fury", "Teleportiert zu Gegnern und greift sie an.", 150, 0, false)),
    GIANTS_SWORD("Giant's Sword", "🗡️", "Extrem hoher Schaden", 500, 150, 0, 0, 0, null),
    WITHERED_GIANTS_SWORD("Withered Giant's Sword", "🗡️", "Withered Upgrade, hoher Schaden", 500, 150, 0, 0, 0, null),
    FABLED_GIANTS_SWORD("Fabled Giant's Sword", "✨🗡️", "Fabled Upgrade, hoher Schaden", 500, 150, 0, 0, 0, null),
    STARRED_GIANTS_SWORD("Starred Giant's Sword", "⭐🗡️", "Starred Dungeon Version, hoher Schaden", 500, 150, 0, 0, 0, null),
    RECOMBOBULATED_GIANTS_SWORD("Recombobulated Giant's Sword", "🔄🗡️", "Recombobulated Version, hoher Schaden", 500, 150, 0, 0, 0, null),
    CLAYMORE("Claymore", "⚔️", "Zweihand-Schwert", 400, 0, 0, 0, 0, null),
    DARK_CLAYMORE("Dark Claymore", "🌑", "Verstärkte Claymore", 450, 0, 0, 0, 0, null),
    LIVID_DAGGER("Livid Dagger", "🗡️", "Hohe Angriffsgeschwindigkeit", 210, 60, 0, 0, 0, new WeaponAbility("Backstab", "100% Crit Chance von hinten.", 0, 0, true)),
    FABLED_LIVID_DAGGER("Fabled Livid Dagger", "✨🗡️", "Fabled Upgrade, hohe Angriffsgeschwindigkeit", 210, 60, 0, 0, 0, new WeaponAbility("Backstab", "100% Crit Chance von hinten.", 0, 0, true)),
    STARRED_LIVID_DAGGER("Starred Livid Dagger", "⭐🗡️", "Starred Dungeon Version, hohe Angriffsgeschwindigkeit", 210, 60, 0, 0, 0, new WeaponAbility("Backstab", "100% Crit Chance von hinten.", 0, 0, true)),
    RECOMBOBULATED_LIVID_DAGGER("Recombobulated Livid Dagger", "🔄🗡️", "Recombobulated Version, hohe Angriffsgeschwindigkeit", 210, 60, 0, 0, 0, new WeaponAbility("Backstab", "100% Crit Chance von hinten.", 0, 0, true)),
    FLOWER_OF_TRUTH("Flower of Truth", "🌸", "Lebenssteal-Fähigkeit", 180, 100, 0, 0, 0, new WeaponAbility("Flying Flower", "Wirft eine Blume, AoE Damage.", 70, 0, false)),
    VALKYRIE("Valkyrie", "⚔️", "Dungeon Berserker-Schwert", 270, 60, 0, 0, 60, null),
    SCYLLA("Scylla", "🌙", "Dungeon Archer-Schwert", 280, 0, 0, 350, 0, null),
    ASTRAEA("Astraea", "⭐", "Dungeon Tank-Schwert", 250, 0, 0, 350, 0, null),
    SILENT_DEATH("Silent Death", "🗡️", "Dungeon Assassin-Schwert", 220, 60, 0, 0, 0, null),
    SOUL_WHIP("Soul Whip", "🌊", "Soul-Angriffe", 240, 100, 0, 0, 0, null),
    LEAPING_SWORD("Leaping Sword", "🐸", "Sprung-Fähigkeit", 150, 100, 0, 0, 0, new WeaponAbility("Leap", "Springt zu Gegnern.", 50, 0, false)),
    EMBER_ROD("Ember Rod", "🔥", "Feuer-basiertes Schwert", 120, 0, 0, 0, 0, new WeaponAbility("Ember Rain", "Feuerbälle regnen lassen.", 150, 30, false)),
    FROZEN_SCYTHE("Frozen Scythe", "❄️", "Eis-basiertes Schwert", 140, 0, 0, 0, 0, new WeaponAbility("Ice Bolt", "Schießt Eissplitter.", 50, 0, false)),
    ARACHNES_SWORD("Arachne's Sword", "🕷️", "Drop von Arachne Boss", 100, 0, 0, 0, 0, null),
    THICK_ASPECT_OF_THE_DRAGONS("Thick Aspect of the Dragons", "🐉", "Verstärkte Version des AOTD", 250, 100, 0, 0, 0, null),
    PILLAGERS_AXE("Pillager's Axe", "🪓", "+15 Stärke pro Mining Collection", 180, 0, 0, 0, 0, null),
    NEST_DISRUPTER("Nest Disrupter", "⚔️", "+80% Schaden vs Obsidian Defender", 160, 0, 0, 0, 0, null),
    ASPECT_OF_THE_NEST("Aspect of the Nest", "🐉", "+150% Schaden im Dragon's Nest", 200, 0, 0, 0, 0, null),
    SWORD_OF_HEAVENLY_LIGHT("Sword of Heavenly Light", "☀️", "Blinding-Fähigkeit, Thunderlord-Bonus", 220, 0, 0, 0, 0, null),
    BLUE_STEEL_RAPIER("Blue Steel Rapier", "🗡️", "Erster Angriff: +22% Geschwindigkeit", 170, 0, 0, 0, 0, null),
    WYRMIC_BLADE("Wyrmic Blade", "🐉", "+150% Schaden vs Endermen", 190, 0, 0, 0, 0, null),
    ASPECT_OF_THE_UNDEAD("Aspect of the Undead", "💀", "+200 Schaden, +200 Stärke, +100% Crit Damage", 300, 200, 100, 0, 0, null),
    LEGAL_GAVEL("Legal Gavel", "⚖️", "+0.5 Schaden pro Mob in 10 Blöcken", 160, 0, 0, 0, 0, null),
    SLAYER_AXE("Slayer Axe", "🪓", "+10% Schaden pro Slayer Level", 200, 0, 0, 0, 0, null),
    WITHERED_SCYTHE("Withered Scythe", "💀", "Aura: 15,000 Schaden/Sekunde", 250, 0, 0, 0, 0, null),
    ASPECT_OF_THE_BEYOND("Aspect of the Beyond", "🌌", "Dritte Form des Aspect of the End", 280, 0, 0, 0, 0, null),
    
    // BOWS (25+ weapons)
    BOW("Bow", "🏹", "Standard-Bogen", 20, 0, 0, 0, 0, null),
    RUNAANS_BOW("Runaan's Bow", "🏹", "3 Pfeile gleichzeitig", 160, 0, 0, 0, 0, null),
    SPIRITUAL_RUNAANS_BOW("Spiritual Runaan's Bow", "✨🏹", "Spiritual Upgrade, 3 Pfeile gleichzeitig", 160, 0, 0, 0, 0, null),
    DUNGEON_RUNAANS_BOW("Dungeon Runaan's Bow", "🏹", "Dungeonized Version, 3 Pfeile gleichzeitig", 160, 0, 0, 0, 0, null),
    STARRED_RUNAANS_BOW("Starred Runaan's Bow", "⭐🏹", "Starred Dungeon Version, 3 Pfeile gleichzeitig", 160, 0, 0, 0, 0, null),
    RECOMBOBULATED_RUNAANS_BOW("Recombobulated Runaan's Bow", "🔄🏹", "Recombobulated Version, 3 Pfeile gleichzeitig", 160, 0, 0, 0, 0, null),
    MOSQUITO_BOW("Mosquito Bow", "🦟", "Gesundheit-basierter Schaden", 200, 0, 0, 0, 0, null),
    MAGMA_BOW("Magma Bow", "🔥", "Feuer-Schaden", 120, 0, 0, 0, 0, null),
    SPIRIT_BOW("Spirit Bow", "👻", "Dungeon Bogen", 180, 0, 0, 0, 0, null),
    ARTISANAL_SHORTBOW("Artisanal Shortbow", "🏹", "Schnelle Schussrate", 100, 0, 0, 0, 0, null),
    HURRICANE_BOW("Hurricane Bow", "🌪️", "5 Pfeile im Bogen", 140, 0, 0, 0, 0, null),
    EXPLOSIVE_BOW("Explosive Bow", "💥", "Explosionen", 160, 0, 0, 0, 0, null),
    LAST_BREATH("Last Breath", "💀", "Debuff-Bogen", 220, 0, 0, 0, 0, null),
    MACHINE_GUN_BOW("Machine Gun Bow", "🔫", "Sehr schnelle Schussrate", 150, 0, 0, 0, 0, null),
    VENOMS_TOUCH("Venom's Touch", "🐍", "Gift-Effekte", 170, 0, 0, 0, 0, null),
    SOULS_REBOUND("Soul's Rebound", "👻", "Soul-Pfeile", 200, 0, 0, 0, 0, null),
    BONEMERANG("Bonemerang", "🦴", "Kehrt zurück", 240, 0, 0, 0, 0, null),
    WITHER_BOW("Wither Bow", "💀", "Wither-Effekt", 260, 0, 0, 0, 0, null),
    TERMINATOR("Terminator", "🏹", "Extreme Reichweite", 300, 0, 0, 0, 0, null),
    JUJU_SHORTBOW("Juju Shortbow", "🏹", "Schnell und mächtig", 280, 0, 0, 0, 0, null),
    SPIRITUAL_JUJU_SHORTBOW("Spiritual Juju Shortbow", "✨🏹", "Spiritual Upgrade, schnell und mächtig", 280, 0, 0, 0, 0, null),
    STARRED_JUJU_SHORTBOW("Starred Juju Shortbow", "⭐🏹", "Starred Dungeon Version, schnell und mächtig", 280, 0, 0, 0, 0, null),
    RECOMBOBULATED_JUJU_SHORTBOW("Recombobulated Juju Shortbow", "🔄🏹", "Recombobulated Version, schnell und mächtig", 280, 0, 0, 0, 0, null),
    PHOENIX_BOW("Phoenix Bow", "🔥", "Wiederbelebung nach Tod", 250, 0, 0, 0, 0, null),
    WHISPER("Whisper", "💨", "Jeder 4. Schuss überladen", 180, 0, 0, 0, 0, null),
    GRAPPLE_BOW("Grapple Bow", "🎣", "Enterhaken-Fähigkeit", 160, 0, 0, 0, 0, null),
    PRECURSOR_EYE("Precursor Eye", "👁️", "Precursor-Effekte", 290, 0, 0, 0, 0, null),
    SLAYER_BOW("Slayer Bow", "🏹", "+10% Schaden pro Slayer Level", 200, 0, 0, 0, 0, null),
    DRAGON_BOW("Dragon Bow", "🐉", "+25% Schaden vs Dragons", 170, 0, 0, 0, 0, null),
    END_BOW("End Bow", "🌑", "+50% Schaden im End", 130, 0, 0, 0, 0, null),
    SPIDER_BOW("Spider Bow", "🕷️", "+30% Schaden vs Spiders", 110, 0, 0, 0, 0, null),
    ZOMBIE_BOW("Zombie Bow", "🧟", "+30% Schaden vs Zombies", 120, 0, 0, 0, 0, null),
    SKELETON_BOW("Skeleton Bow", "💀", "+30% Schaden vs Skeletons", 115, 0, 0, 0, 0, null),
    CREEPER_BOW("Creeper Bow", "💥", "+30% Schaden vs Creepers", 125, 0, 0, 0, 0, null),
    BLAZE_BOW("Blaze Bow", "🔥", "+50% Schaden vs Blazes", 150, 0, 0, 0, 0, null),
    GHAST_BOW("Ghast Bow", "👻", "+50% Schaden vs Ghasts", 155, 0, 0, 0, 0, null),
    WITHER_SKELETON_BOW("Wither Skeleton Bow", "💀", "+50% Schaden vs Wither Skeletons", 165, 0, 0, 0, 0, null),
    ENDERMAN_BOW("Enderman Bow", "🌑", "+50% Schaden vs Endermen", 175, 0, 0, 0, 0, null),
    
    // SPECIAL WEAPONS (20+ weapons)
    DAGGER("Dagger", "🗡️", "Schnelle Angriffe", 80, 0, 0, 0, 0, null),
    SPEAR("Spear", "🔱", "Große Reichweite", 120, 0, 0, 0, 0, null),
    AXE("Axe", "🪓", "Doppelte Funktion", 100, 0, 0, 0, 0, null),
    FISHING_ROD("Fishing Rod", "🎣", "Kann als Waffe verwendet werden", 10, 0, 0, 0, 0, null),
    WAND("Wand", "🪄", "Magie-Waffen", 50, 0, 0, 0, 0, null),
    GAUNTLET("Gauntlet", "🥊", "Mining-Waffe", 150, 0, 0, 0, 0, null),
    STAFF_OF_DIVINITY("Staff of Divinity", "⭐", "Fliegen überall, Debuffs", 200, 0, 0, 0, 0, null),
    STAFF_OF_THE_CRIMSON_REVENGE("Staff of the Crimson Revenge", "🔥", "Feuerbälle abfeuern", 180, 0, 0, 0, 0, null),
    SCYTHE("Scythe", "🌾", "Bereichsangriffe", 160, 0, 0, 0, 0, null),
    WHIP("Whip", "🪢", "Reichweitenangriffe", 140, 0, 0, 0, 0, null),
    HAMMER("Hammer", "🔨", "Schwere Angriffe", 200, 0, 0, 0, 0, null),
    MACE("Mace", "⚔️", "Stumpfe Angriffe", 180, 0, 0, 0, 0, null),
    RAPIER("Rapier", "🗡️", "Durchbohrende Angriffe", 120, 0, 0, 0, 0, null),
    KATANA("Katana", "🗾", "Schnelle Schnitte", 160, 0, 0, 0, 0, null),
    SCIMITAR("Scimitar", "⚔️", "Bogenangriffe", 140, 0, 0, 0, 0, null),
    FALCHION("Falchion", "⚔️", "Schwere Schnitte", 180, 0, 0, 0, 0, null),
    BROADSWORD("Broadsword", "⚔️", "Breite Angriffe", 130, 0, 0, 0, 0, null),
    LONGSWORD("Longsword", "⚔️", "Lange Reichweite", 150, 0, 0, 0, 0, null),
    SHORTSWORD("Shortsword", "🗡️", "Schnelle Angriffe", 90, 0, 0, 0, 0, null),
    ESTOC("Estoc", "🗡️", "Durchbohrende Stiche", 110, 0, 0, 0, 0, null),
    
    // STAVES & WANDS (15+ weapons)
    MIDAS_STAFF("Midas' Staff", "💰", "Schaden basierend auf ausgegebenen Coins", 200, 0, 0, 0, 0, null),
    SPIRIT_SCEPTRE("Spirit Sceptre", "👻", "Beschwört Geister", 160, 0, 0, 0, 0, null),
    BONZOS_STAFF("Bonzo's Staff", "🎭", "Dungeon Utility-Stab", 120, 0, 0, 0, 0, null),
    JERRY_CHINE_GUN("Jerry-chine Gun", "🎭", "Spaß-Waffe", 100, 0, 0, 0, 0, null),
    INK_WAND("Ink Wand", "🖤", "Tintenexplosionen", 130, 0, 0, 0, 0, null),
    YETI_SWORD("Yeti Sword", "❄️", "Eisblöcke schleudern", 140, 0, 0, 0, 0, null),
    FROZEN_SCYTHE_STAFF("Frozen Scythe", "❄️", "Eis-basierter Stab", 140, 0, 0, 0, 0, null),
    EMBER_ROD_STAFF("Ember Rod", "🔥", "Feuer-basierter Stab", 120, 0, 0, 0, 0, null),
    MAGMA_ROD("Magma Rod", "🌋", "Magma-basierter Stab", 160, 0, 0, 0, 0, null),
    FIRE_STAFF("Fire Staff", "🔥", "Feuer-Magie", 150, 0, 0, 0, 0, null),
    ICE_STAFF("Ice Staff", "❄️", "Eis-Magie", 140, 0, 0, 0, 0, null),
    LIGHTNING_STAFF("Lightning Staff", "⚡", "Blitz-Magie", 180, 0, 0, 0, 0, null),
    EARTH_STAFF("Earth Staff", "🌍", "Erde-Magie", 160, 0, 0, 0, 0, null),
    WATER_STAFF("Water Staff", "💧", "Wasser-Magie", 130, 0, 0, 0, 0, null),
    AIR_STAFF("Air Staff", "💨", "Luft-Magie", 120, 0, 0, 0, 0, null),
    SHADOW_STAFF("Shadow Staff", "🌑", "Schatten-Magie", 170, 0, 0, 0, 0, null),
    LIGHT_STAFF("Light Staff", "☀️", "Licht-Magie", 160, 0, 0, 0, 0, null),
    NECROMANCER_STAFF("Necromancer Staff", "💀", "Totenbeschwörung", 190, 0, 0, 0, 0, null),
    SUMMONER_STAFF("Summoner Staff", "👻", "Beschwörungs-Magie", 180, 0, 0, 0, 0, null),
    ILLUSION_STAFF("Illusion Staff", "🎭", "Illusions-Magie", 150, 0, 0, 0, 0, null);
    
    private final String displayName;
    private final String icon;
    private final String description;
    private final int baseDamage;
    private final int strength;
    private final int critDamage;
    private final int intelligence;
    private final int ferocity;
    private final WeaponAbility ability;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    CompleteWeaponType(String displayName, String icon, String description, int baseDamage, int strength, int critDamage, int intelligence, int ferocity, WeaponAbility ability) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.baseDamage = baseDamage;
        this.strength = strength;
        this.critDamage = critDamage;
        this.intelligence = intelligence;
        this.ferocity = ferocity;
        this.ability = ability;
    }
    
    public String getDisplayName() { return displayName; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
    public int getBaseDamageStat() { return baseDamage; }
    public int getStrength() { return strength; }
    public int getCritDamage() { return critDamage; }
    public int getIntelligence() { return intelligence; }
    public int getFerocity() { return ferocity; }
    public WeaponAbility getAbility() { return ability; }
    
    /**
     * Get weapon category
     */
    public WeaponCategory getCategory() {
    switch (this) {
            // Varianten auf Basistyp mappen
            case DUNGEON_ASPECT_OF_THE_DRAGONS:
            case STARRED_ASPECT_OF_THE_DRAGONS:
            case RECOMBOBULATED_ASPECT_OF_THE_DRAGONS:
                return ASPECT_OF_THE_DRAGONS.getCategory();
            case GILDED_MIDAS_SWORD:
            case DUNGEON_MIDAS_SWORD:
            case STARRED_MIDAS_SWORD:
            case RECOMBOBULATED_MIDAS_SWORD:
                return MIDAS_SWORD.getCategory();
            case WITHERED_HYPERION:
            case FABLED_HYPERION:
            case STARRED_HYPERION:
            case RECOMBOBULATED_HYPERION:
                return HYPERION.getCategory();
            case WITHERED_GIANTS_SWORD:
            case FABLED_GIANTS_SWORD:
            case STARRED_GIANTS_SWORD:
            case RECOMBOBULATED_GIANTS_SWORD:
                return GIANTS_SWORD.getCategory();
            case FABLED_LIVID_DAGGER:
            case STARRED_LIVID_DAGGER:
            case RECOMBOBULATED_LIVID_DAGGER:
                return LIVID_DAGGER.getCategory();
            case SPIRITUAL_RUNAANS_BOW:
            case DUNGEON_RUNAANS_BOW:
            case STARRED_RUNAANS_BOW:
            case RECOMBOBULATED_RUNAANS_BOW:
                return RUNAANS_BOW.getCategory();
            case SPIRITUAL_JUJU_SHORTBOW:
            case STARRED_JUJU_SHORTBOW:
            case RECOMBOBULATED_JUJU_SHORTBOW:
                return JUJU_SHORTBOW.getCategory();
            // Standard Mapping
            case WOODEN_SWORD: case STONE_SWORD: case IRON_SWORD: case GOLDEN_SWORD: case DIAMOND_SWORD: case NETHERITE_SWORD:
            case ASPECT_OF_THE_END: case ASPECT_OF_THE_DRAGONS: case PIGMAN_SWORD: case MIDAS_SWORD: case ASPECT_OF_THE_VOID:
            case HYPERION: case SHADOW_FURY: case GIANTS_SWORD: case CLAYMORE: case DARK_CLAYMORE: case LIVID_DAGGER: case FLOWER_OF_TRUTH:
            case VALKYRIE: case SCYLLA: case ASTRAEA: case SILENT_DEATH: case SOUL_WHIP: case LEAPING_SWORD: case EMBER_ROD: case FROZEN_SCYTHE:
            case ARACHNES_SWORD: case THICK_ASPECT_OF_THE_DRAGONS: case PILLAGERS_AXE: case NEST_DISRUPTER: case ASPECT_OF_THE_NEST:
            case SWORD_OF_HEAVENLY_LIGHT: case BLUE_STEEL_RAPIER: case WYRMIC_BLADE: case ASPECT_OF_THE_UNDEAD: case LEGAL_GAVEL:
            case SLAYER_AXE: case WITHERED_SCYTHE: case ASPECT_OF_THE_BEYOND: case DAGGER: case SPEAR: case AXE: case SCYTHE: case WHIP: case HAMMER:
            case MACE: case RAPIER: case KATANA: case SCIMITAR: case FALCHION: case BROADSWORD: case LONGSWORD: case SHORTSWORD: case ESTOC:
                return WeaponCategory.SPECIAL;
            case BOW: case RUNAANS_BOW: case MOSQUITO_BOW: case MAGMA_BOW: case SPIRIT_BOW: case ARTISANAL_SHORTBOW: case HURRICANE_BOW:
            case EXPLOSIVE_BOW: case LAST_BREATH: case MACHINE_GUN_BOW: case VENOMS_TOUCH: case SOULS_REBOUND: case BONEMERANG: case WITHER_BOW:
            case TERMINATOR: case JUJU_SHORTBOW: case PHOENIX_BOW: case WHISPER: case GRAPPLE_BOW: case PRECURSOR_EYE: case SLAYER_BOW:
            case DRAGON_BOW: case END_BOW: case SPIDER_BOW: case ZOMBIE_BOW: case SKELETON_BOW: case CREEPER_BOW: case BLAZE_BOW: case GHAST_BOW:
            case WITHER_SKELETON_BOW: case ENDERMAN_BOW:
                return WeaponCategory.BOW;
            case FISHING_ROD: case WAND: case GAUNTLET: case STAFF_OF_DIVINITY: case STAFF_OF_THE_CRIMSON_REVENGE: case MIDAS_STAFF:
            case SPIRIT_SCEPTRE: case BONZOS_STAFF: case JERRY_CHINE_GUN: case INK_WAND: case YETI_SWORD: case FROZEN_SCYTHE_STAFF:
            case EMBER_ROD_STAFF: case MAGMA_ROD: case FIRE_STAFF: case ICE_STAFF: case LIGHTNING_STAFF: case EARTH_STAFF: case WATER_STAFF:
            case AIR_STAFF: case SHADOW_STAFF: case LIGHT_STAFF: case NECROMANCER_STAFF: case SUMMONER_STAFF: case ILLUSION_STAFF:
                return WeaponCategory.SPECIAL;
            default:
                throw new IllegalStateException("Unbekannter Waffentyp: " + this);
        }
    }
    
    /**
     * Get weapon rarity
     */
    public WeaponRarity getRarity() {
        switch (this) {
            case DUNGEON_ASPECT_OF_THE_DRAGONS:
            case STARRED_ASPECT_OF_THE_DRAGONS:
            case RECOMBOBULATED_ASPECT_OF_THE_DRAGONS:
                return ASPECT_OF_THE_DRAGONS.getRarity();
            case GILDED_MIDAS_SWORD:
            case DUNGEON_MIDAS_SWORD:
            case STARRED_MIDAS_SWORD:
            case RECOMBOBULATED_MIDAS_SWORD:
                return MIDAS_SWORD.getRarity();
            case WITHERED_HYPERION:
            case FABLED_HYPERION:
            case STARRED_HYPERION:
            case RECOMBOBULATED_HYPERION:
                return HYPERION.getRarity();
            case WITHERED_GIANTS_SWORD:
            case FABLED_GIANTS_SWORD:
            case STARRED_GIANTS_SWORD:
            case RECOMBOBULATED_GIANTS_SWORD:
                return GIANTS_SWORD.getRarity();
            case FABLED_LIVID_DAGGER:
            case STARRED_LIVID_DAGGER:
            case RECOMBOBULATED_LIVID_DAGGER:
                return LIVID_DAGGER.getRarity();
            case SPIRITUAL_RUNAANS_BOW:
            case DUNGEON_RUNAANS_BOW:
            case STARRED_RUNAANS_BOW:
            case RECOMBOBULATED_RUNAANS_BOW:
                return RUNAANS_BOW.getRarity();
            case SPIRITUAL_JUJU_SHORTBOW:
            case STARRED_JUJU_SHORTBOW:
            case RECOMBOBULATED_JUJU_SHORTBOW:
                return JUJU_SHORTBOW.getRarity();
            // Standard Mapping
            case WOODEN_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case GOLDEN_SWORD:
                return WeaponRarity.COMMON;
            case DIAMOND_SWORD:
            case NETHERITE_SWORD:
            case ASPECT_OF_THE_END:
            case PIGMAN_SWORD:
            case ARACHNES_SWORD:
            case BOW:
            case FISHING_ROD:
                return WeaponRarity.UNCOMMON;
            case LEAPING_SWORD:
            case EMBER_ROD:
            case FROZEN_SCYTHE:
            case THICK_ASPECT_OF_THE_DRAGONS:
            case PILLAGERS_AXE:
            case NEST_DISRUPTER:
            case BLUE_STEEL_RAPIER:
            case LEGAL_GAVEL:
            case SLAYER_AXE:
            case RUNAANS_BOW:
            case MOSQUITO_BOW:
            case MAGMA_BOW:
            case ARTISANAL_SHORTBOW:
            case HURRICANE_BOW:
            case EXPLOSIVE_BOW:
            case MACHINE_GUN_BOW:
            case VENOMS_TOUCH:
            case WHISPER:
            case GRAPPLE_BOW:
            case SLAYER_BOW:
            case DRAGON_BOW:
            case END_BOW:
            case SPIDER_BOW:
            case ZOMBIE_BOW:
            case SKELETON_BOW:
            case CREEPER_BOW:
            case BLAZE_BOW:
            case GHAST_BOW:
            case WITHER_SKELETON_BOW:
            case ENDERMAN_BOW:
            case DAGGER:
            case SPEAR:
            case AXE:
            case WAND:
            case GAUNTLET:
            case STAFF_OF_THE_CRIMSON_REVENGE:
            case SCYTHE:
            case WHIP:
            case HAMMER:
            case MACE:
            case RAPIER:
            case KATANA:
            case SCIMITAR:
            case FALCHION:
            case BROADSWORD:
            case LONGSWORD:
            case SHORTSWORD:
            case ESTOC:
            case SPIRIT_SCEPTRE:
            case BONZOS_STAFF:
            case INK_WAND:
            case YETI_SWORD:
            case FROZEN_SCYTHE_STAFF:
            case EMBER_ROD_STAFF:
            case MAGMA_ROD:
            case FIRE_STAFF:
            case ICE_STAFF:
            case LIGHTNING_STAFF:
            case EARTH_STAFF:
            case WATER_STAFF:
            case AIR_STAFF:
            case SHADOW_STAFF:
            case LIGHT_STAFF:
            case NECROMANCER_STAFF:
            case SUMMONER_STAFF:
            case ILLUSION_STAFF:
                return WeaponRarity.EPIC;
            case ASPECT_OF_THE_DRAGONS:
            case MIDAS_SWORD:
            case ASPECT_OF_THE_VOID:
            case HYPERION:
            case SHADOW_FURY:
            case GIANTS_SWORD:
            case CLAYMORE:
            case DARK_CLAYMORE:
            case LIVID_DAGGER:
            case FLOWER_OF_TRUTH:
            case VALKYRIE:
            case SCYLLA:
            case ASTRAEA:
            case SILENT_DEATH:
            case SOUL_WHIP:
            case ASPECT_OF_THE_NEST:
            case SWORD_OF_HEAVENLY_LIGHT:
            case WYRMIC_BLADE:
            case ASPECT_OF_THE_UNDEAD:
            case WITHERED_SCYTHE:
            case ASPECT_OF_THE_BEYOND:
            case SPIRIT_BOW:
            case LAST_BREATH:
            case SOULS_REBOUND:
            case BONEMERANG:
            case WITHER_BOW:
            case TERMINATOR:
            case JUJU_SHORTBOW:
            case PHOENIX_BOW:
            case PRECURSOR_EYE:
            case MIDAS_STAFF:
            case JERRY_CHINE_GUN:
            case STAFF_OF_DIVINITY:
                return WeaponRarity.LEGENDARY;
            default:
                throw new IllegalStateException("Unbekannter Waffentyp: " + this);
        }
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
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        // Initialize complete weapon type
        status = SystemStatus.RUNNING;
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        // Shutdown complete weapon type
        status = SystemStatus.DISABLED;
    }

    @Override
    public String getName() {
        return "CompleteWeaponType";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }

    public int getPriority() {
        return 50;
    }

    public boolean isRequired() {
        return false;
    }
}
