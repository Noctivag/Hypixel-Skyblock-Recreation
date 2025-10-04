package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.cosmetics.AdvancedParticleManager;
import de.noctivag.plugin.cosmetics.ParticleShape;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class AdvancedCosmeticsMenu extends CustomGUI {
    private final Plugin plugin;
    private final AdvancedParticleManager particleManager;

    public AdvancedCosmeticsMenu(Plugin plugin, AdvancedParticleManager particleManager) {
        super(54, Component.text("§d§lAdvanced Kosmetik"));
        this.plugin = plugin;
        this.particleManager = particleManager;
    }

    public AdvancedCosmeticsMenu(Plugin plugin) {
        super(54, Component.text("§d§lAdvanced Kosmetik"));
        this.plugin = plugin;
        // Placeholder - method not implemented
        this.particleManager = null;
    }

    public void open(Player player) {
        clearInventory();

        // Header
        setItem(4, createGuiItem(Material.NETHER_STAR, "§d§lAdvanced Kosmetik",
            "§7Wähle deine Partikel-Formen"));

        // Particle Shapes - Row 1
        setItem(10, createGuiItem(Material.RED_DYE, "§c§lHerz",
            "§7Herz-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(11, createGuiItem(Material.NETHER_STAR, "§6§lStern",
            "§7Stern-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(12, createGuiItem(Material.DIAMOND, "§b§lDiamant",
            "§7Diamant-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(13, createGuiItem(Material.EMERALD, "§a§lKreis",
            "§7Kreis-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(14, createGuiItem(Material.END_ROD, "§d§lSpirale",
            "§7Spirale-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(15, createGuiItem(Material.BEACON, "§5§lHelix",
            "§73D-Helix Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(16, createGuiItem(Material.WATER_BUCKET, "§9§lWelle",
            "§7Wellen-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        // Particle Shapes - Row 2
        setItem(19, createGuiItem(Material.GOLDEN_HELMET, "§6§lKrone",
            "§7Kronen-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(20, createGuiItem(Material.ELYTRA, "§e§lFlügel",
            "§7Flügel-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(21, createGuiItem(Material.GLOWSTONE, "§a§lAura",
            "§7Aura-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(22, createGuiItem(Material.ENDER_PEARL, "§5§lOrbit",
            "§7Orbit-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(23, createGuiItem(Material.REDSTONE, "§c§lPuls",
            "§7Pulsierender Effekt",
            "§eLinksklick: §7Aktivieren"));

        setItem(24, createGuiItem(Material.WATER_BUCKET, "§9§lRegen",
            "§7Regen-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(25, createGuiItem(Material.SNOWBALL, "§f§lSchnee",
            "§7Schnee-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        // Special Effects - Row 3
        setItem(28, createGuiItem(Material.FIRE_CHARGE, "§c§lFeuer",
            "§7Feuer-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        setItem(29, createGuiItem(Material.LIGHTNING_ROD, "§e§lBlitz",
            "§7Blitz-Form Partikel",
            "§eLinksklick: §7Aktivieren"));

        // Settings
        setItem(37, createGuiItem(Material.ANVIL, "§e§lEinstellungen",
            "§7Partikel-Einstellungen",
            "§eLinksklick: §7Öffnen"));

        // Deactivate button
        setItem(45, createGuiItem(Material.BARRIER, "§c§lDeaktivieren",
            "§7Deaktiviere alle Partikel-Effekte"));

        // Back button
        setItem(49, createGuiItem(Material.ARROW, "§7Zurück",
            "§7Zum Hauptmenü"));

        player.openInventory(getInventory());
    }

    public ParticleShape getShapeAtSlot(int slot) {
        return switch (slot) {
            case 10 -> ParticleShape.HEART;
            case 11 -> ParticleShape.STAR;
            case 12 -> ParticleShape.DIAMOND;
            case 13 -> ParticleShape.CIRCLE;
            case 14 -> ParticleShape.SPIRAL;
            case 15 -> ParticleShape.HELIX;
            case 16 -> ParticleShape.WAVE;
            case 19 -> ParticleShape.CROWN;
            case 20 -> ParticleShape.WINGS;
            case 21 -> ParticleShape.AURA;
            case 22 -> ParticleShape.ORBIT;
            case 23 -> ParticleShape.PULSE;
            case 24 -> ParticleShape.RAIN;
            case 25 -> ParticleShape.SNOW;
            case 28 -> ParticleShape.FIRE;
            case 29 -> ParticleShape.LIGHTNING;
            default -> null;
        };
    }
}
