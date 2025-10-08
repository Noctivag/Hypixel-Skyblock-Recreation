package de.noctivag.skyblock.dungeons;

import java.util.UUID;

/**
 * Basisklasse für alle Dungeon-Entitäten (Spieler, Mobs, Bosse)
 */
public abstract class BaseDungeonEntity {
    protected UUID entityId;
    protected String name;
    protected String type;
    protected int floor;

    public BaseDungeonEntity(UUID entityId, String name, String type, int floor) {
        this.entityId = entityId;
        this.name = name;
        this.type = type;
        this.floor = floor;
    }

    public UUID getEntityId() { return entityId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getFloor() { return floor; }
}
