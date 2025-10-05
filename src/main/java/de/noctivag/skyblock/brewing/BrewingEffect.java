package de.noctivag.skyblock.brewing;

import org.bukkit.potion.PotionEffectType;

/**
 * Brewing effect
 */
public class BrewingEffect {
    
    private final PotionEffectType effectType;
    private final int duration;
    private final int amplifier;
    private final boolean ambient;
    private final boolean particles;
    
    public BrewingEffect(PotionEffectType effectType, int duration, int amplifier, boolean ambient, boolean particles) {
        this.effectType = effectType;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.particles = particles;
    }
    
    // Getters
    public PotionEffectType getEffectType() { return effectType; }
    public int getDuration() { return duration; }
    public int getAmplifier() { return amplifier; }
    public boolean isAmbient() { return ambient; }
    public boolean hasParticles() { return particles; }
}
