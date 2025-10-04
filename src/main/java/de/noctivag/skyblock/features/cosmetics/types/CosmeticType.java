package de.noctivag.skyblock.features.cosmetics.types;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Arrays;

/**
 * All cosmetic types from Hypixel Skyblock
 */
public enum CosmeticType {
    // Hats
    RAINBOW_HAT("Rainbow Hat", "🌈", "A colorful rainbow hat"),
    DIAMOND_HAT("Diamond Hat", "💎", "A shiny diamond hat"),
    GOLDEN_HAT("Golden Hat", "🥇", "A golden hat"),
    EMERALD_HAT("Emerald Hat", "💚", "A green emerald hat"),
    RUBY_HAT("Ruby Hat", "❤️", "A red ruby hat"),
    SAPPHIRE_HAT("Sapphire Hat", "💙", "A blue sapphire hat"),
    AMETHYST_HAT("Amethyst Hat", "💜", "A purple amethyst hat"),
    TOPAZ_HAT("Topaz Hat", "💛", "A yellow topaz hat"),
    JADE_HAT("Jade Hat", "🟢", "A jade hat"),
    OPAL_HAT("Opal Hat", "⚪", "An opal hat"),
    
    // Cloaks
    RAINBOW_CLOAK("Rainbow Cloak", "🌈", "A colorful rainbow cloak"),
    DIAMOND_CLOAK("Diamond Cloak", "💎", "A shiny diamond cloak"),
    GOLDEN_CLOAK("Golden Cloak", "🥇", "A golden cloak"),
    EMERALD_CLOAK("Emerald Cloak", "💚", "A green emerald cloak"),
    RUBY_CLOAK("Ruby Cloak", "❤️", "A red ruby cloak"),
    SAPPHIRE_CLOAK("Sapphire Cloak", "💙", "A blue sapphire cloak"),
    AMETHYST_CLOAK("Amethyst Cloak", "💜", "A purple amethyst cloak"),
    TOPAZ_CLOAK("Topaz Cloak", "💛", "A yellow topaz cloak"),
    JADE_CLOAK("Jade Cloak", "🟢", "A jade cloak"),
    OPAL_CLOAK("Opal Cloak", "⚪", "An opal cloak"),
    
    // Wings
    DRAGON_WINGS("Dragon Wings", "🐉", "Mystical dragon wings"),
    ANGEL_WINGS("Angel Wings", "👼", "Divine angel wings"),
    DEMON_WINGS("Demon Wings", "👹", "Dark demon wings"),
    PHOENIX_WINGS("Phoenix Wings", "🔥", "Burning phoenix wings"),
    ICE_WINGS("Ice Wings", "❄️", "Frozen ice wings"),
    SHADOW_WINGS("Shadow Wings", "🌑", "Mysterious shadow wings"),
    LIGHTNING_WINGS("Lightning Wings", "⚡", "Electric lightning wings"),
    NATURE_WINGS("Nature Wings", "🌿", "Natural nature wings"),
    WATER_WINGS("Water Wings", "💧", "Flowing water wings"),
    FIRE_WINGS("Fire Wings", "🔥", "Burning fire wings"),
    
    // Auras
    RAINBOW_AURA("Rainbow Aura", "🌈", "A colorful rainbow aura"),
    DIAMOND_AURA("Diamond Aura", "💎", "A shiny diamond aura"),
    GOLDEN_AURA("Golden Aura", "🥇", "A golden aura"),
    EMERALD_AURA("Emerald Aura", "💚", "A green emerald aura"),
    RUBY_AURA("Ruby Aura", "❤️", "A red ruby aura"),
    SAPPHIRE_AURA("Sapphire Aura", "💙", "A blue sapphire aura"),
    AMETHYST_AURA("Amethyst Aura", "💜", "A purple amethyst aura"),
    TOPAZ_AURA("Topaz Aura", "💛", "A yellow topaz aura"),
    JADE_AURA("Jade Aura", "🟢", "A jade aura"),
    OPAL_AURA("Opal Aura", "⚪", "An opal aura"),
    
    // Trails
    RAINBOW_TRAIL("Rainbow Trail", "🌈", "A colorful rainbow trail"),
    DIAMOND_TRAIL("Diamond Trail", "💎", "A shiny diamond trail"),
    GOLDEN_TRAIL("Golden Trail", "🥇", "A golden trail"),
    EMERALD_TRAIL("Emerald Trail", "💚", "A green emerald trail"),
    RUBY_TRAIL("Ruby Trail", "❤️", "A red ruby trail"),
    SAPPHIRE_TRAIL("Sapphire Trail", "💙", "A blue sapphire trail"),
    AMETHYST_TRAIL("Amethyst Trail", "💜", "A purple amethyst trail"),
    TOPAZ_TRAIL("Topaz Trail", "💛", "A yellow topaz trail"),
    JADE_TRAIL("Jade Trail", "🟢", "A jade trail"),
    OPAL_TRAIL("Opal Trail", "⚪", "An opal trail"),
    
    // Special Effects
    PARTICLE_EFFECT("Particle Effect", "✨", "Special particle effects"),
    SOUND_EFFECT("Sound Effect", "🎵", "Special sound effects"),
    LIGHTNING_EFFECT("Lightning Effect", "⚡", "Lightning strike effects"),
    FIRE_EFFECT("Fire Effect", "🔥", "Fire particle effects"),
    WATER_EFFECT("Water Effect", "💧", "Water particle effects"),
    EARTH_EFFECT("Earth Effect", "🌍", "Earth particle effects"),
    AIR_EFFECT("Air Effect", "💨", "Air particle effects"),
    SPIRIT_EFFECT("Spirit Effect", "👻", "Spirit particle effects"),
    MAGIC_EFFECT("Magic Effect", "🔮", "Magic particle effects"),
    DIVINE_EFFECT("Divine Effect", "✨", "Divine particle effects");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    CosmeticType(String displayName, String icon, String description) {
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
     * Get cosmetics by category
     */
    public static List<CosmeticType> getHats() {
        return Arrays.asList(
            RAINBOW_HAT, DIAMOND_HAT, GOLDEN_HAT, EMERALD_HAT, RUBY_HAT,
            SAPPHIRE_HAT, AMETHYST_HAT, TOPAZ_HAT, JADE_HAT, OPAL_HAT
        );
    }
    
    /**
     * Get cloaks
     */
    public static List<CosmeticType> getCloaks() {
        return Arrays.asList(
            RAINBOW_CLOAK, DIAMOND_CLOAK, GOLDEN_CLOAK, EMERALD_CLOAK, RUBY_CLOAK,
            SAPPHIRE_CLOAK, AMETHYST_CLOAK, TOPAZ_CLOAK, JADE_CLOAK, OPAL_CLOAK
        );
    }
    
    /**
     * Get wings
     */
    public static List<CosmeticType> getWings() {
        return Arrays.asList(
            DRAGON_WINGS, ANGEL_WINGS, DEMON_WINGS, PHOENIX_WINGS, ICE_WINGS,
            SHADOW_WINGS, LIGHTNING_WINGS, NATURE_WINGS, WATER_WINGS, FIRE_WINGS
        );
    }
    
    /**
     * Get auras
     */
    public static List<CosmeticType> getAuras() {
        return Arrays.asList(
            RAINBOW_AURA, DIAMOND_AURA, GOLDEN_AURA, EMERALD_AURA, RUBY_AURA,
            SAPPHIRE_AURA, AMETHYST_AURA, TOPAZ_AURA, JADE_AURA, OPAL_AURA
        );
    }
    
    /**
     * Get trails
     */
    public static List<CosmeticType> getTrails() {
        return Arrays.asList(
            RAINBOW_TRAIL, DIAMOND_TRAIL, GOLDEN_TRAIL, EMERALD_TRAIL, RUBY_TRAIL,
            SAPPHIRE_TRAIL, AMETHYST_TRAIL, TOPAZ_TRAIL, JADE_TRAIL, OPAL_TRAIL
        );
    }
    
    /**
     * Get special effects
     */
    public static List<CosmeticType> getSpecialEffects() {
        return Arrays.asList(
            PARTICLE_EFFECT, SOUND_EFFECT, LIGHTNING_EFFECT, FIRE_EFFECT, WATER_EFFECT,
            EARTH_EFFECT, AIR_EFFECT, SPIRIT_EFFECT, MAGIC_EFFECT, DIVINE_EFFECT
        );
    }
    
    /**
     * Get cosmetics by category
     */
    public static List<CosmeticType> getByCategory(CosmeticCategory category) {
        return switch (category) {
            case HAT -> getHats();
            case CLOAK -> getCloaks();
            case WINGS -> getWings();
            case AURA -> getAuras();
            case TRAIL -> getTrails();
            case SPECIAL_EFFECT -> getSpecialEffects();
        };
    }
    
    /**
     * Get cosmetics by rarity
     */
    public static List<CosmeticType> getByRarity(CosmeticRarity rarity) {
        return Arrays.stream(values())
            .filter(cosmetic -> {
                // Simple rarity mapping - would be more sophisticated in real implementation
                return switch (rarity) {
                    case COMMON -> false; // No common cosmetics in this implementation
                    case RARE -> getHats().contains(cosmetic) || getCloaks().contains(cosmetic) || 
                               getTrails().contains(cosmetic) || getAuras().contains(cosmetic);
                    case EPIC -> getHats().contains(cosmetic) || getCloaks().contains(cosmetic) || 
                               getTrails().contains(cosmetic) || getAuras().contains(cosmetic);
                    case LEGENDARY -> getWings().contains(cosmetic) || getSpecialEffects().contains(cosmetic);
                    case MYTHIC -> false; // No mythic cosmetics in this implementation
                };
            })
            .toList();
    }
    
    /**
     * Get cosmetic category
     */
    public CosmeticCategory getCategory() {
        if (getHats().contains(this)) return CosmeticCategory.HAT;
        if (getCloaks().contains(this)) return CosmeticCategory.CLOAK;
        if (getWings().contains(this)) return CosmeticCategory.WINGS;
        if (getAuras().contains(this)) return CosmeticCategory.AURA;
        if (getTrails().contains(this)) return CosmeticCategory.TRAIL;
        if (getSpecialEffects().contains(this)) return CosmeticCategory.SPECIAL_EFFECT;
        
        return CosmeticCategory.HAT; // Default fallback
    }
    
    /**
     * Get cosmetic rarity
     */
    public CosmeticRarity getRarity() {
        if (getWings().contains(this) || getSpecialEffects().contains(this)) {
            return CosmeticRarity.LEGENDARY;
        } else if (getHats().contains(this) || getCloaks().contains(this) || 
                   getTrails().contains(this) || getAuras().contains(this)) {
            return CosmeticRarity.EPIC;
        } else {
            return CosmeticRarity.RARE;
        }
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
