package de.noctivag.skyblock.mobs.abilities;

import de.noctivag.skyblock.mobs.AbilityTrigger;
import de.noctivag.skyblock.mobs.MobAbility;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Ability that creates an explosion when triggered
 * Commonly used for mobs that explode on death
 */
public class ExplodeAbility implements MobAbility {
    
    private final float explosionPower;
    private final boolean breakBlocks;
    private final boolean damageEntities;
    private long lastExecution = 0;
    private final long cooldown;
    
    public ExplodeAbility(float explosionPower, boolean breakBlocks, boolean damageEntities, long cooldown) {
        this.explosionPower = explosionPower;
        this.breakBlocks = breakBlocks;
        this.damageEntities = damageEntities;
        this.cooldown = cooldown;
    }
    
    public ExplodeAbility(float explosionPower) {
        this(explosionPower, false, true, 0);
    }
    
    @Override
    public void execute(Entity self, Player target) {
        if (!canExecute()) {
            return;
        }
        
        Location location = self.getLocation();
        
        // Create explosion
        self.getWorld().createExplosion(
            location.getX(),
            location.getY(),
            location.getZ(),
            explosionPower,
            breakBlocks,
            damageEntities
        );
        
        lastExecution = System.currentTimeMillis();
    }
    
    @Override
    public AbilityTrigger getTrigger() {
        return AbilityTrigger.ON_DEATH;
    }
    
    @Override
    public long getCooldown() {
        return cooldown;
    }
    
    @Override
    public boolean canExecute() {
        return System.currentTimeMillis() - lastExecution >= cooldown;
    }
}
