package de.noctivag.skyblock.mobs.impl;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class ZombieVillager extends CustomMob {

    public ZombieVillager(SkyblockPlugin plugin, Location spawnLocation, double maxHealth, double damage, double defense, double combatXP) {
        super(plugin, spawnLocation, maxHealth, damage, defense, combatXP);
    }

    @Override
    public String getName() {
        return "Zombie Villager";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ZOMBIE_VILLAGER;
    }

    @Override
    public String getLootTableId() {
        return "zombie_villager";
    }
}
