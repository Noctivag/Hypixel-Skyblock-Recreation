package de.noctivag.skyblock.pets;

import org.bukkit.Material;
import java.util.UUID;

/**
 * Basisklasse für alle Pet-Objekte
 */
public abstract class BasePet {
    protected String petId;
    protected UUID ownerId;
    protected String name;
    protected String displayName;
    protected Material material;
    protected int level;
    protected boolean active;

    public BasePet(String petId, UUID ownerId, String name, String displayName, Material material, int level, boolean active) {
        this.petId = petId;
        this.ownerId = ownerId;
        this.name = name;
        this.displayName = displayName;
        this.material = material;
        this.level = level;
        this.active = active;
    }

    public String getPetId() { return petId; }
    public UUID getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public Material getMaterial() { return material; }
    public int getLevel() { return level; }
    public boolean isActive() { return active; }
}
