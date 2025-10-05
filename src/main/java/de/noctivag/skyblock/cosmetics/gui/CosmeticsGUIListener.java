package de.noctivag.skyblock.cosmetics.gui;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.CosmeticsMenu;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CosmeticsGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public CosmeticsGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
            SkyblockPlugin.getCosmeticsManager().stopParticleEffect(player);
            player.sendMessage(Component.text("§aDeine Partikel-Effekte wurden entfernt."));
            updateGUI(player);
            return;
        }

        // Halo & Trail toggles
        if (slot == 23) { // Halo
            SkyblockPlugin.getCosmeticsManager().startHalo(player);
            player.sendMessage(Component.text("§aHalo aktiviert."));
            updateGUI(player);
            return;
        }
        if (slot == 24) { // Trail
            SkyblockPlugin.getCosmeticsManager().startTrail(player);
            player.sendMessage(Component.text("§aTrail aktiviert."));
            updateGUI(player);
            return;
        }

        if (slot == 40) { // Clear sounds
            SkyblockPlugin.getCosmeticsManager().clearSoundEffect(player);
            player.sendMessage(Component.text("§aDeine Sound-Effekte wurden entfernt."));
            updateGUI(player);
            return;
        }

        // Close button
        if (slot == 49) {
            player.closeInventory();
        }

        // Navigate to Gadgets GUI from header or specific slot if needed (optional wiring)
        if (slot == 4) {
            new de.noctivag.skyblock.gui.GadgetsGUI(SkyblockPlugin).open(player);
        }
    }

    private void handleWingToggle(Player player) {
        String perm = "cosmetics.wings";
        if (!player.hasPermission(perm)) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für Flügel!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // TODO: Implement proper ConfigManager interface
        // int cost = ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getInt("cosmetics.wings.cost", 2000);
        int cost = 2000; // Default cost
        Player exactPlayer = SkyblockPlugin.getServer().getPlayerExact(player.getName());
        if (!SkyblockPlugin.getEconomyManager().hasBalance(player, cost) &&
            (exactPlayer == null || !exactPlayer.hasPermission("basicsplugin.*"))) {
            player.sendMessage(Component.text("§cDu hast nicht genug Coins für Flügel!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // Toggle wings (charge if turning on)
        boolean wasActive = SkyblockPlugin.getCosmeticsManager().isWingActive(player);
        if (wasActive) {
            SkyblockPlugin.getCosmeticsManager().stopWings(player);
            player.sendMessage(Component.text("§aFlügel deaktiviert."));
        } else {
            // withdraw money
            if (!SkyblockPlugin.getEconomyManager().withdrawMoney(player, cost)) {
                player.sendMessage(Component.text("Transaction failed. Not enough balance."));
                return;
            }
            SkyblockPlugin.getCosmeticsManager().startWings(player);
            player.sendMessage("§aFlügel aktiviert! §7(Kosten: §6" + cost + " Coins§7)");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

        updateGUI(player);
    }

    private void handleParticleSelection(Player player, Particle particle) {
        String permission = "cosmetics.particles." + particle.name().toLowerCase();
        if (!player.hasPermission(permission)) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Effekt!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // TODO: Implement proper ConfigManager interface
        // int cost = ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getInt("particle-effects.effects." + particle.name() + ".cost", 1000);
        int cost = 1000; // Default cost
        if (!SkyblockPlugin.getEconomyManager().hasBalance(player, cost)) {
            player.sendMessage(Component.text("§cDu hast nicht genug Coins für diesen Effekt!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // Check if player is toggling off the effect
        if (SkyblockPlugin.getCosmeticsManager().getActiveParticleEffect(player) != null &&
            SkyblockPlugin.getCosmeticsManager().getActiveParticleEffect(player).getType() == particle) {
            SkyblockPlugin.getCosmeticsManager().stopParticleEffect(player);
            player.sendMessage(Component.text("§aPartikel-Effekt deaktiviert."));
        } else {
            if (!SkyblockPlugin.getEconomyManager().withdrawMoney(player, cost)) return;
            SkyblockPlugin.getCosmeticsManager().setParticleEffect(player, particle);
            player.sendMessage("§aPartikel-Effekt aktiviert! §7(Kosten: §6" + cost + " Coins§7)");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

        updateGUI(player);
    }

    private void handleSoundSelection(Player player, Sound sound) {
        String permission = "cosmetics.sounds." + sound.name().toLowerCase();
        if (!player.hasPermission(permission)) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Effekt!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // TODO: Implement proper ConfigManager interface
        // int cost = ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getInt("sound-effects.effects." + sound.name() + ".cost", 800);
        int cost = 800; // Default cost
        if (!SkyblockPlugin.getEconomyManager().hasBalance(player, cost)) {
            player.sendMessage(Component.text("§cDu hast nicht genug Coins für diesen Effekt!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // Check if player is toggling off the effect
        if (SkyblockPlugin.getCosmeticsManager().getActiveSoundEffect(player) == sound) {
            SkyblockPlugin.getCosmeticsManager().clearSoundEffect(player);
            player.sendMessage(Component.text("§aSound-Effekt deaktiviert."));
        } else {
            if (!SkyblockPlugin.getEconomyManager().withdrawMoney(player, cost)) return;
            SkyblockPlugin.getCosmeticsManager().setSoundEffect(player, sound);
            player.sendMessage("§aSound-Effekt aktiviert! §7(Kosten: §6" + cost + " Coins§7)");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

        updateGUI(player);
    }

    private void updateGUI(Player player) {
        // Re-open the GUI to update the display
        new CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager()).open(player);
    }
}
