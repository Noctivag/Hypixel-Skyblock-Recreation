package de.noctivag.skyblock.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

/**
 * Beispiel-Ability: AI Beam (Laserstrahl nach vorne)
 */
public class AILaserAbility implements ItemAbility {
    @Override
    public String getName() { return "AI Beam"; }
    @Override
    public String getDescription() { return "Schießt einen Laserstrahl nach vorne."; }
    @Override
    public int getManaCost() { return 50; }
    @Override
    public int getCooldownTicks() { return 40; }
    @Override
    public void onUse(Player player, PlayerInteractEvent event) {
        // Dummy: Rückstoß nach vorne
        Vector dir = player.getLocation().getDirection().normalize();
        player.setVelocity(dir.multiply(2));
        player.sendMessage("§bAI Beam aktiviert!");
    }
}
