package de.noctivag.plugin.cosmetics;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum ParticleShape {
    CIRCLE("Kreis", "Ein perfekter Kreis um den Spieler"),
    SPIRAL("Spirale", "Eine sich drehende Spirale"),
    HELIX("Helix", "Eine 3D-Helix-Form"),
    WAVE("Welle", "Eine wellenförmige Bewegung"),
    CROWN("Krone", "Eine Krone über dem Kopf"),
    WINGS("Flügel", "Flügel-artige Partikel"),
    AURA("Aura", "Eine leuchtende Aura"),
    ORBIT("Orbit", "Partikel die um den Spieler kreisen"),
    PULSE("Puls", "Ein pulsierender Effekt"),
    RAIN("Regen", "Regen-artige Partikel"),
    SNOW("Schnee", "Schnee-artige Partikel"),
    FIRE("Feuer", "Feuer-artige Partikel"),
    LIGHTNING("Blitz", "Blitz-artige Partikel"),
    HEART("Herz", "Herz-Form"),
    STAR("Stern", "Stern-Form"),
    DIAMOND("Diamant", "Diamant-Form"),
    SQUARE("Quadrat", "Quadrat-Form"),
    TRIANGLE("Dreieck", "Dreieck-Form");

    private final String displayName;
    private final String description;

    ParticleShape(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public List<Location> generateParticleLocations(Player player, int particleCount, double time) {
        Location center = player.getLocation().add(0, 1, 0);
        List<Location> locations = new ArrayList<>();

        switch (this) {
            case CIRCLE -> {
                double radius = 1.5;
                for (int i = 0; i < particleCount; i++) {
                    double angle = (2 * Math.PI * i / particleCount) + time;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    locations.add(new Location(center.getWorld(), x, center.getY(), z));
                }
            }
            case SPIRAL -> {
                double radius = 1.0;
                double height = 2.0;
                for (int i = 0; i < particleCount; i++) {
                    double angle = (4 * Math.PI * i / particleCount) + time * 2;
                    double r = radius * (1 - (double) i / particleCount);
                    double x = center.getX() + r * Math.cos(angle);
                    double z = center.getZ() + r * Math.sin(angle);
                    double y = center.getY() + (height * i / particleCount);
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case HELIX -> {
                double radius = 1.2;
                double height = 2.5;
                for (int i = 0; i < particleCount; i++) {
                    double angle = (6 * Math.PI * i / particleCount) + time * 3;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    double y = center.getY() + (height * i / particleCount);
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case WAVE -> {
                double amplitude = 1.0;
                double frequency = 2.0;
                for (int i = 0; i < particleCount; i++) {
                    double x = center.getX() + (i - particleCount / 2.0) * 0.2;
                    double y = center.getY() + amplitude * Math.sin(frequency * (i * 0.3 + time));
                    double z = center.getZ();
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case CROWN -> {
                double radius = 0.8;
                for (int i = 0; i < particleCount; i++) {
                    double angle = (2 * Math.PI * i / particleCount) + time;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    double y = center.getY() + 2.2 + 0.3 * Math.sin(angle * 3);
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case WINGS -> {
                double wingSpan = 2.0;
                for (int i = 0; i < particleCount; i++) {
                    double side = (i < particleCount / 2) ? -1 : 1;
                    double x = center.getX() + side * wingSpan * (0.5 + 0.5 * Math.sin(time + i * 0.5));
                    double y = center.getY() + 1.5 + 0.5 * Math.sin(time * 2 + i * 0.3);
                    double z = center.getZ() + (i % 3 - 1) * 0.3;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case AURA -> {
                double radius = 2.0;
                for (int i = 0; i < particleCount; i++) {
                    double angle = (2 * Math.PI * i / particleCount) + time;
                    double r = radius * (0.8 + 0.2 * Math.sin(time * 3 + angle));
                    double x = center.getX() + r * Math.cos(angle);
                    double z = center.getZ() + r * Math.sin(angle);
                    double y = center.getY() + 0.5 + Math.sin(time * 2 + angle) * 0.5;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case ORBIT -> {
                double radius = 1.8;
                for (int i = 0; i < particleCount; i++) {
                    double angle = (2 * Math.PI * i / particleCount) + time * 2;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    double y = center.getY() + 1.0;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case PULSE -> {
                double radius = 1.0 + 0.5 * Math.sin(time * 4);
                for (int i = 0; i < particleCount; i++) {
                    double angle = (2 * Math.PI * i / particleCount);
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    double y = center.getY() + 1.0;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case RAIN -> {
                for (int i = 0; i < particleCount; i++) {
                    double x = center.getX() + (Math.random() - 0.5) * 4;
                    double y = center.getY() + 3 + Math.random() * 2;
                    double z = center.getZ() + (Math.random() - 0.5) * 4;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case SNOW -> {
                for (int i = 0; i < particleCount; i++) {
                    double x = center.getX() + (Math.random() - 0.5) * 3;
                    double y = center.getY() + 2 + Math.random() * 3;
                    double z = center.getZ() + (Math.random() - 0.5) * 3;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case FIRE -> {
                for (int i = 0; i < particleCount; i++) {
                    double x = center.getX() + (Math.random() - 0.5) * 2;
                    double y = center.getY() + Math.random() * 2;
                    double z = center.getZ() + (Math.random() - 0.5) * 2;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case LIGHTNING -> {
                for (int i = 0; i < particleCount; i++) {
                    double x = center.getX() + (Math.random() - 0.5) * 4;
                    double y = center.getY() + Math.random() * 4;
                    double z = center.getZ() + (Math.random() - 0.5) * 4;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case HEART -> {
                for (int i = 0; i < particleCount; i++) {
                    double t = (2 * Math.PI * i / particleCount) + time;
                    double x = center.getX() + 16 * Math.pow(Math.sin(t), 3);
                    double y = center.getY() + 2.5 - (13 * Math.cos(t) - 5 * Math.cos(2*t) - 2 * Math.cos(3*t) - Math.cos(4*t)) * 0.1;
                    double z = center.getZ();
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case STAR -> {
                double radius = 1.5;
                for (int i = 0; i < particleCount; i++) {
                    double angle = (2 * Math.PI * i / particleCount) + time;
                    double r = radius * (1 + 0.3 * Math.sin(5 * angle));
                    double x = center.getX() + r * Math.cos(angle);
                    double z = center.getZ() + r * Math.sin(angle);
                    double y = center.getY() + 1.0;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case DIAMOND -> {
                double radius = 1.2;
                for (int i = 0; i < particleCount; i++) {
                    double angle = (2 * Math.PI * i / particleCount) + time;
                    double r = radius * (1 + 0.2 * Math.sin(4 * angle));
                    double x = center.getX() + r * Math.cos(angle);
                    double z = center.getZ() + r * Math.sin(angle);
                    double y = center.getY() + 1.0 + 0.5 * Math.sin(2 * angle);
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case SQUARE -> {
                double size = 1.5;
                int pointsPerSide = particleCount / 4;
                for (int i = 0; i < particleCount; i++) {
                    double x, z;
                    if (i < pointsPerSide) {
                        // Top side
                        x = center.getX() + (i - pointsPerSide / 2.0) * (size / pointsPerSide);
                        z = center.getZ() + size / 2;
                    } else if (i < 2 * pointsPerSide) {
                        // Right side
                        x = center.getX() + size / 2;
                        z = center.getZ() + ((i - pointsPerSide) - pointsPerSide / 2.0) * (size / pointsPerSide);
                    } else if (i < 3 * pointsPerSide) {
                        // Bottom side
                        x = center.getX() + ((i - 2 * pointsPerSide) - pointsPerSide / 2.0) * (size / pointsPerSide);
                        z = center.getZ() - size / 2;
                    } else {
                        // Left side
                        x = center.getX() - size / 2;
                        z = center.getZ() + ((i - 3 * pointsPerSide) - pointsPerSide / 2.0) * (size / pointsPerSide);
                    }
                    double y = center.getY() + 1.0;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
            case TRIANGLE -> {
                double size = 1.5;
                int pointsPerSide = particleCount / 3;
                for (int i = 0; i < particleCount; i++) {
                    double x, z;
                    if (i < pointsPerSide) {
                        // Top side
                        x = center.getX() + (i - pointsPerSide / 2.0) * (size / pointsPerSide);
                        z = center.getZ() + size / 2;
                    } else if (i < 2 * pointsPerSide) {
                        // Right side
                        double t = (double) (i - pointsPerSide) / pointsPerSide;
                        x = center.getX() + size / 2 * (1 - t);
                        z = center.getZ() + size / 2 * (1 - t) - size / 2;
                    } else {
                        // Left side
                        double t = (double) (i - 2 * pointsPerSide) / pointsPerSide;
                        x = center.getX() - size / 2 * (1 - t);
                        z = center.getZ() + size / 2 * (1 - t) - size / 2;
                    }
                    double y = center.getY() + 1.0;
                    locations.add(new Location(center.getWorld(), x, y, z));
                }
            }
        }

        return locations;
    }

    public Particle getParticleType() {
        return switch (this) {
            case FIRE -> Particle.FLAME;
            case SNOW -> Particle.SNOWFLAKE;
            case RAIN -> Particle.SPLASH;
            case LIGHTNING -> Particle.ELECTRIC_SPARK;
            case HEART -> Particle.HEART;
            default -> Particle.END_ROD;
        };
    }
}
