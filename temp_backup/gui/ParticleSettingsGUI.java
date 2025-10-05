package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.cosmetics.AdvancedParticleManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ParticleSettingsGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final AdvancedParticleManager particleManager;

    public ParticleSettingsGUI(SkyblockPlugin SkyblockPlugin, AdvancedParticleManager particleManager) {
        super(27, Component.text("§e§lPartikel-Einstellungen"));
        this.SkyblockPlugin = SkyblockPlugin;
        this.particleManager = particleManager;
    }

    public ParticleSettingsGUI(SkyblockPlugin SkyblockPlugin) {
        super(27, Component.text("§e§lPartikel-Einstellungen"));
        this.SkyblockPlugin = SkyblockPlugin;
        this.particleManager = (AdvancedParticleManager) SkyblockPlugin.getAdvancedParticleManager(); // Cast from Object
    }

    public void open(Player player) {
        clearInventory();

        // Header
        setItem(4, createGuiItem(Material.ANVIL, "§e§lPartikel-Einstellungen",
            "§7Passe deine Partikel an"));

        // Particle Count Settings
        setItem(10, createGuiItem(Material.ARROW, "§a§lWeniger Partikel",
            "§7Reduziere die Anzahl der Partikel",
            "§eLinksklick: §7-5 Partikel"));

        setItem(11, createGuiItem(Material.SPECTRAL_ARROW, "§c§lMehr Partikel",
            "§7Erhöhe die Anzahl der Partikel",
            "§eLinksklick: §7+5 Partikel"));

        // Speed Settings
        setItem(12, createGuiItem(Material.FEATHER, "§a§lLangsamer",
            "§7Reduziere die Geschwindigkeit",
            "§eLinksklick: §7-0.1 Geschwindigkeit"));

        setItem(13, createGuiItem(Material.FIREWORK_ROCKET, "§c§lSchneller",
            "§7Erhöhe die Geschwindigkeit",
            "§eLinksklick: §7+0.1 Geschwindigkeit"));

        // Particle Type Settings
        setItem(14, createGuiItem(Material.RED_DYE, "§c§lHerz-Partikel",
            "§7Verwende Herz-Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(15, createGuiItem(Material.FIRE_CHARGE, "§6§lFlammen-Partikel",
            "§7Verwende Flammen-Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(16, createGuiItem(Material.END_ROD, "§b§lEnd-Rod Partikel",
            "§7Verwende End-Rod Partikel",
            "§eLinksklick: §7Aktivieren"));

        // Reset Settings
        setItem(22, createGuiItem(Material.BARRIER, "§c§lZurücksetzen",
            "§7Setze alle Einstellungen zurück",
            "§eLinksklick: §7Bestätigen"));

        // Back button
        setItem(18, createGuiItem(Material.ARROW, "§7Zurück",
            "§7Zum Kosmetik-Menü"));

        player.openInventory(getInventory());
    }
}
