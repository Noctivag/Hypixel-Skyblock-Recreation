package de.noctivag.plugin.cosmetics;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleEffect {
    private final String name;
    private final ParticleEffectType effectType;
    private final Particle type;
    private final int cost;
    private final String permission;
    private final String description;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private final int count;
    private final double speed;

    // Constructor for FeatureManager compatibility
    public ParticleEffect(String name, ParticleEffectType effectType, Particle type, int cost, String permission, String description) {
        this.name = name;
        this.effectType = effectType;
        this.type = type;
        this.cost = cost;
        this.permission = permission;
        this.description = description;
        this.offsetX = 0.5;
        this.offsetY = 0.5;
        this.offsetZ = 0.5;
        this.count = 1;
        this.speed = 0;
    }

    // New simpler constructor to avoid requiring ParticleEffectType
    public ParticleEffect(String name, Particle type, int cost, String permission, String description) {
        this.name = name;
        this.effectType = ParticleEffectType.AURA;
        this.type = type;
        this.cost = cost;
        this.permission = permission;
        this.description = description;
        this.offsetX = 0.5;
        this.offsetY = 0.5;
        this.offsetZ = 0.5;
        this.count = 1;
        this.speed = 0;
    }

    // Constructor for CosmeticsManager compatibility
    public ParticleEffect(Particle type, double offsetX, double offsetY, double offsetZ, int count, double speed) {
        this.name = type.name().toLowerCase();
        this.effectType = ParticleEffectType.AURA;
        this.type = type;
        this.cost = 0;
        this.permission = "";
        this.description = "";
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.count = count;
        this.speed = speed;
    }

    public void spawn(Location location) {
        location.getWorld().spawnParticle(
            type,
            location.add(0, 1, 0), // Slightly above player
            count,
            offsetX,
            offsetY,
            offsetZ,
            speed
        );
    }

    public String getName() {
        return name;
    }

    public ParticleEffectType getEffectType() {
        return effectType;
    }

    public Particle getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public void playEffect(Player player) {
        spawn(player.getLocation());
    }

    public static ParticleEffect createDefault(Particle type) {
        return switch (type) {
            case HEART -> new ParticleEffect(type, 0.5, 0.5, 0.5, 1, 0);
            case FLAME -> new ParticleEffect(type, 0.2, 0.2, 0.2, 3, 0.02);
            case CLOUD -> new ParticleEffect(type, 0.2, 0.2, 0.2, 1, 0);
            case NOTE -> new ParticleEffect(type, 0.5, 0.5, 0.5, 1, 1);
            case PORTAL -> new ParticleEffect(type, 0.2, 0.2, 0.2, 5, 0.5);
            case WITCH -> new ParticleEffect(type, 0.5, 0.5, 0.5, 3, 0);
            // Additional helpful presets
            case SOUL -> new ParticleEffect(type, 0.3, 0.3, 0.3, 3, 0);
            default -> new ParticleEffect(type, 0.3, 0.3, 0.3, 1, 0);
        };
    }

    public enum ParticleEffectType {
        AURA,
        TRAIL,
        WING,
        CROWN,
        SPIRAL,
        SHIELD,
        VORTEX,
        RAINBOW,
        GALAXY
    }
}
