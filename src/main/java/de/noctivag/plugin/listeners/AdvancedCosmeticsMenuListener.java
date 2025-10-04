package de.noctivag.plugin.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.cosmetics.AdvancedParticleManager;
import de.noctivag.plugin.cosmetics.ParticleShape;
import de.noctivag.plugin.gui.AdvancedCosmeticsMenu;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class AdvancedCosmeticsMenuListener implements Listener {
    private final Plugin plugin;
    private final AdvancedParticleManager particleManager;

    public AdvancedCosmeticsMenuListener(Plugin plugin, AdvancedParticleManager particleManager) {
        this.plugin = plugin;
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
            new de.noctivag.plugin.gui.MainMenu(plugin).open(player);
            return;
        }

        if (display.contains("Deaktivieren")) {
            particleManager.stopPlayerEffect(player);
            new AdvancedCosmeticsMenu(plugin, particleManager).open(player);
            return;
        }

        if (display.contains("Einstellungen")) {
            new de.noctivag.plugin.gui.ParticleSettingsGUI(plugin, particleManager).open(player);
            return;
        }

        // Handle particle shape selection
        ParticleShape shape = ((AdvancedCosmeticsMenu) event.getInventory().getHolder()).getShapeAtSlot(event.getSlot());
        if (shape != null) {
            // Check if player has permission
            String permission = "basicsplugin.cosmetics." + shape.name().toLowerCase();
            if (!player.hasPermission(permission)) {
                player.sendMessage("§cDu hast keine Berechtigung für diesen Effekt!");
                return;
            }

            // Check if player has enough money
            int cost = plugin.getConfigManager().getConfig().getInt("cosmetics.shapes." + shape.name().toLowerCase() + ".cost", 1000);
            if (!plugin.getEconomyManager().hasBalance(player, cost)) {
                player.sendMessage("§cDu kannst dir diesen Effekt nicht leisten: " + plugin.getEconomyManager().formatMoney(cost));
                return;
            }

            // Withdraw money
            if (!plugin.getEconomyManager().withdrawMoney(player, cost)) {
                return;
            }

            // Get particle type and settings
            Particle particle = shape.getParticleType();
            int particleCount = plugin.getConfigManager().getConfig().getInt("cosmetics.shapes." + shape.name().toLowerCase() + ".count", 20);
            double speed = plugin.getConfigManager().getConfig().getDouble("cosmetics.shapes." + shape.name().toLowerCase() + ".speed", 0.1);

            // Activate effect
            particleManager.setPlayerEffect(player, shape, particle, particleCount, speed);
            new AdvancedCosmeticsMenu(plugin, particleManager).open(player);
        }
    }
}
