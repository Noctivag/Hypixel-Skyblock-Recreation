package de.noctivag.skyblock.cosmetics.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.gui.CosmeticsMenu;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CosmeticsGUIListener implements Listener {
    private final SkyblockPlugin plugin;

    public CosmeticsGUIListener(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof CosmeticsMenu cosmeticsMenu)) return;

        event.setCancelled(true);

        int slot = event.getSlot();

        // Wing slot
        if (slot == 21) {
            handleWingToggle(player);
            return;
        }

        // Check for particle effects
        Particle part = cosmeticsMenu.getParticleAtSlot(slot);
        if (part != null) {
            handleParticleSelection(player, part);
            return;
        }

        // Check for sound effects
        Sound s = cosmeticsMenu.getSoundAtSlot(slot);
        if (s != null) {
            handleSoundSelection(player, s);
            return;
        }

        // Handle clear buttons
        if (slot == 22) { // Clear particles
            plugin.getCosmeticsManager().stopParticleEffect(player);
            player.sendMessage("§aDeine Partikel-Effekte wurden entfernt.");
            updateGUI(player);
            return;
        }

        // Halo & Trail toggles
        if (slot == 23) { // Halo
            plugin.getCosmeticsManager().startHalo(player);
            player.sendMessage("§aHalo aktiviert.");
            updateGUI(player);
            return;
        }
        if (slot == 24) { // Trail
            plugin.getCosmeticsManager().startTrail(player);
            player.sendMessage("§aTrail aktiviert.");
            updateGUI(player);
            return;
        }

        if (slot == 40) { // Clear sounds
            plugin.getCosmeticsManager().clearSoundEffect(player);
            player.sendMessage("§aDeine Sound-Effekte wurden entfernt.");
            updateGUI(player);
            return;
        }

        // Close button
        if (slot == 49) {
            player.closeInventory();
        }

        // Navigate to Gadgets GUI from header or specific slot if needed (optional wiring)
        if (slot == 4) {
            new de.noctivag.plugin.gui.GadgetsGUI(plugin).open(player);
        }
    }

    private void handleWingToggle(Player player) {
        String perm = "cosmetics.wings";
        if (!player.hasPermission(perm)) {
            player.sendMessage("§cDu hast keine Berechtigung für Flügel!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // TODO: Implement proper ConfigManager interface
        // int cost = ((ConfigManager) plugin.getConfigManager()).getConfig().getInt("cosmetics.wings.cost", 2000);
        int cost = 2000; // Default cost
        Player exactPlayer = plugin.getServer().getPlayerExact(player.getName());
        if (!plugin.getEconomyManager().hasBalance(player, cost) &&
            (exactPlayer == null || !exactPlayer.hasPermission("basicsplugin.*"))) {
            player.sendMessage("§cDu hast nicht genug Coins für Flügel!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // Toggle wings (charge if turning on)
        boolean wasActive = plugin.getCosmeticsManager().isWingActive(player);
        if (wasActive) {
            plugin.getCosmeticsManager().stopWings(player);
            player.sendMessage("§aFlügel deaktiviert.");
        } else {
            // withdraw money
            if (!plugin.getEconomyManager().withdrawMoney(player, cost)) {
                player.sendMessage("Transaction failed. Not enough balance.");
                return;
            }
            plugin.getCosmeticsManager().startWings(player);
            player.sendMessage("§aFlügel aktiviert! §7(Kosten: §6" + cost + " Coins§7)");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

        updateGUI(player);
    }

    private void handleParticleSelection(Player player, Particle particle) {
        String permission = "cosmetics.particles." + particle.name().toLowerCase();
        if (!player.hasPermission(permission)) {
            player.sendMessage("§cDu hast keine Berechtigung für diesen Effekt!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // TODO: Implement proper ConfigManager interface
        // int cost = ((ConfigManager) plugin.getConfigManager()).getConfig().getInt("particle-effects.effects." + particle.name() + ".cost", 1000);
        int cost = 1000; // Default cost
        if (!plugin.getEconomyManager().hasBalance(player, cost)) {
            player.sendMessage("§cDu hast nicht genug Coins für diesen Effekt!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // Check if player is toggling off the effect
        if (plugin.getCosmeticsManager().getActiveParticleEffect(player) != null &&
            plugin.getCosmeticsManager().getActiveParticleEffect(player).getType() == particle) {
            plugin.getCosmeticsManager().stopParticleEffect(player);
            player.sendMessage("§aPartikel-Effekt deaktiviert.");
        } else {
            if (!plugin.getEconomyManager().withdrawMoney(player, cost)) return;
            plugin.getCosmeticsManager().setParticleEffect(player, particle);
            player.sendMessage("§aPartikel-Effekt aktiviert! §7(Kosten: §6" + cost + " Coins§7)");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

        updateGUI(player);
    }

    private void handleSoundSelection(Player player, Sound sound) {
        String permission = "cosmetics.sounds." + sound.name().toLowerCase();
        if (!player.hasPermission(permission)) {
            player.sendMessage("§cDu hast keine Berechtigung für diesen Effekt!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // TODO: Implement proper ConfigManager interface
        // int cost = ((ConfigManager) plugin.getConfigManager()).getConfig().getInt("sound-effects.effects." + sound.name() + ".cost", 800);
        int cost = 800; // Default cost
        if (!plugin.getEconomyManager().hasBalance(player, cost)) {
            player.sendMessage("§cDu hast nicht genug Coins für diesen Effekt!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // Check if player is toggling off the effect
        if (plugin.getCosmeticsManager().getActiveSoundEffect(player) == sound) {
            plugin.getCosmeticsManager().clearSoundEffect(player);
            player.sendMessage("§aSound-Effekt deaktiviert.");
        } else {
            if (!plugin.getEconomyManager().withdrawMoney(player, cost)) return;
            plugin.getCosmeticsManager().setSoundEffect(player, sound);
            player.sendMessage("§aSound-Effekt aktiviert! §7(Kosten: §6" + cost + " Coins§7)");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

        updateGUI(player);
    }

    private void updateGUI(Player player) {
        // Re-open the GUI to update the display
        new CosmeticsMenu(plugin, plugin.getCosmeticsManager()).open(player);
    }
}
