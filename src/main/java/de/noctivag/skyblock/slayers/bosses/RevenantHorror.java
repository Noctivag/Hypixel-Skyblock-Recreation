package de.noctivag.skyblock.slayers.bosses;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Revenant Horror - Undead slayer boss
 */
public class RevenantHorror extends SlayerBoss {
    
    public RevenantHorror(Location spawnLocation, int tier) {
        super("REVENANT_HORROR", spawnLocation, tier, "REVENANT");
        
        // Set tier-specific stats
        setTierStats(tier);
    }
    
    @Override
    public String getName() {
        return "§cRevenant Horror §7[Tier " + getTier() + "]";
    }
    
    @Override
    public String getLootTableId() {
        return "revenant_horror_tier_" + getTier();
    }
    
    /**
     * Set stats based on tier
     */
    private void setTierStats(int tier) {
        switch (tier) {
            case 1:
                setMaxHealth(1000.0);
                setDamage(50.0);
                setDefense(10.0);
                setCombatXP(100.0);
                break;
            case 2:
                setMaxHealth(5000.0);
                setDamage(100.0);
                setDefense(20.0);
                setCombatXP(250.0);
                break;
            case 3:
                setMaxHealth(25000.0);
                setDamage(200.0);
                setDefense(40.0);
                setCombatXP(500.0);
                break;
            case 4:
                setMaxHealth(100000.0);
                setDamage(400.0);
                setDefense(80.0);
                setCombatXP(1000.0);
                break;
            default:
                setMaxHealth(1000.0);
                setDamage(50.0);
                setDefense(10.0);
                setCombatXP(100.0);
                break;
        }
    }
}

