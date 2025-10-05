package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.cosmetics.AdvancedParticleManager;
import de.noctivag.skyblock.cosmetics.ParticleShape;
import de.noctivag.skyblock.gui.AdvancedCosmeticsMenu;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class AdvancedCosmeticsMenuListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final AdvancedParticleManager particleManager;

    public AdvancedCosmeticsMenuListener(SkyblockPlugin SkyblockPlugin, AdvancedParticleManager particleManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.particleManager = particleManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof AdvancedCosmeticsMenu)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getItemMeta() == null) return;
        ItemMeta meta = clicked.getItemMeta();
        String display = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
        if (display == null) return;

        if (display.contains("Zurück")) {
            new de.noctivag.skyblock.gui.MainMenu(SkyblockPlugin).open(player);
            return;
        }

        if (display.contains("Deaktivieren")) {
            particleManager.stopPlayerEffect(player);
            new AdvancedCosmeticsMenu(SkyblockPlugin, particleManager).open(player);
            return;
        }

        if (display.contains("Einstellungen")) {
            new de.noctivag.skyblock.gui.ParticleSettingsGUI(SkyblockPlugin, particleManager).open(player);
            return;
        }

        // Handle particle shape selection
        ParticleShape shape = ((AdvancedCosmeticsMenu) event.getInventory().getHolder()).getShapeAtSlot(event.getSlot());
        if (shape != null) {
            // Check if player has permission
            String permission = "basicsplugin.cosmetics." + shape.name().toLowerCase();
            if (!player.hasPermission(permission)) {
                player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Effekt!"));
                return;
            }

            // Check if player has enough money
            int cost = SkyblockPlugin.getConfigManager().getConfig().getInt("cosmetics.shapes." + shape.name().toLowerCase() + ".cost", 1000);
            if (!SkyblockPlugin.getEconomyManager().hasBalance(player, cost)) {
                player.sendMessage("§cDu kannst dir diesen Effekt nicht leisten: " + SkyblockPlugin.getEconomyManager().formatMoney(cost));
                return;
            }

            // Withdraw money
            if (!SkyblockPlugin.getEconomyManager().withdrawMoney(player, cost)) {
                return;
            }

            // Get particle type and settings
            Particle particle = shape.getParticleType();
            int particleCount = SkyblockPlugin.getConfigManager().getConfig().getInt("cosmetics.shapes." + shape.name().toLowerCase() + ".count", 20);
            double speed = SkyblockPlugin.getConfigManager().getConfig().getDouble("cosmetics.shapes." + shape.name().toLowerCase() + ".speed", 0.1);

            // Activate effect
            particleManager.setPlayerEffect(player, shape, particle, particleCount, speed);
            new AdvancedCosmeticsMenu(SkyblockPlugin, particleManager).open(player);
        }
    }
}
