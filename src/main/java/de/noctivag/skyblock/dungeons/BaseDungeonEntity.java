package de.noctivag.skyblock.dungeons;

import java.util.UUID;

/**
 * Basisschnittstelle für alle Dungeon-Entitäten (Spieler, Mobs, Bosse)
 */
public interface BaseDungeonEntity {
    UUID getEntityId();
    String getName();
    String getType();
    int getFloor();
}
