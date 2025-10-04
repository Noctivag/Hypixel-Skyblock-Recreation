package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.cosmetics.CosmeticsManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CosmeticsMenu extends CustomGUI {
    private final Map<Integer, Particle> particleSlots;
    private final Map<Integer, Sound> soundSlots;
    private final SkyblockPlugin plugin;
    private final CosmeticsManager cosmeticsManager;

    public CosmeticsMenu(SkyblockPlugin plugin, CosmeticsManager cosmeticsManager) {
        super(54, Component.text("§d✦ Kosmetik-Menü ✦"));
        this.plugin = plugin;
        this.cosmeticsManager = cosmeticsManager;
        this.particleSlots = new HashMap<>();
        this.soundSlots = new HashMap<>();
        initializeEffectSlots();
    }

    private void initializeEffectSlots() {
        // Particle slots
        particleSlots.put(10, Particle.HEART);
        particleSlots.put(11, Particle.FLAME);
        particleSlots.put(12, Particle.PORTAL);
        particleSlots.put(13, Particle.NOTE);
        particleSlots.put(14, Particle.END_ROD);
        particleSlots.put(15, Particle.TOTEM_OF_UNDYING);
        particleSlots.put(16, Particle.DRAGON_BREATH);

    // Additional cosmetic effects
        particleSlots.put(17, Particle.SOUL); // HALO visual (mapped)
        particleSlots.put(18, Particle.CRIT); // TRAIL visual (mapped)
        // Wing special slot (handled specially)
        // slot 21 reserved for wings

        // Sound slots
        soundSlots.put(28, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        soundSlots.put(29, Sound.BLOCK_NOTE_BLOCK_PLING);
        soundSlots.put(30, Sound.BLOCK_AMETHYST_BLOCK_CHIME);
        soundSlots.put(31, Sound.BLOCK_BELL_USE);
        soundSlots.put(32, Sound.ENTITY_PLAYER_LEVELUP);
        soundSlots.put(33, Sound.BLOCK_ENCHANTMENT_TABLE_USE);
        soundSlots.put(34, Sound.ENTITY_ENDER_DRAGON_FLAP);
    }

    public void open(Player player) {
        setupMenu(player);
        player.openInventory(getInventory());
    }

    private void setupMenu(Player player) {
        clearInventory();

        // Headers
        setItem(4, createGuiItem(Material.NETHER_STAR, "§d§lKosmetik-Shop",
            "§7Wähle einen Effekt aus"));

        // Particle Effects Section
        setupParticleEffects(player);

        // Sound Effects Section
        setupSoundEffects(player);

        // Navigation
        setItem(49, createGuiItem(Material.BARRIER, "§c§lZurück",
            "§7Klicke, um zum Hauptmenü",
            "§7zurückzukehren"));

        // Fill empty slots
        fillEmptySlots();
    }

    private void setupParticleEffects(Player player) {
        for (Map.Entry<Integer, Particle> entry : particleSlots.entrySet()) {
            Particle particle = entry.getValue();
            boolean isActive = cosmeticsManager.getActiveParticleEffect(player) != null &&
                             cosmeticsManager.getActiveParticleEffect(player).getType() == particle;

            setItem(entry.getKey(), createParticleItem(particle, isActive, player));
        }

        // Wing button
        boolean wingsActive = cosmeticsManager.isWingActive(player);
        int wingsCost = plugin.getConfigManager().getConfig().getInt("cosmetics.wings.cost", 2000);
        setItem(21, createGuiItem(wingsActive ? Material.ELYTRA : Material.FEATHER,
            (wingsActive ? "§aFlügel §a§l✓" : "§eFlügel"),
            wingsActive ? "§7Flügel sind aktiv" : "§7Klicke, um Flügel zu aktivieren",
            "§7Kosten: §6" + wingsCost + " Coins"));

        // Halo toggle
        boolean haloActive = plugin.getCosmeticsManager().isWingActive(player) && false; // placeholder, handled by manager
        setItem(23, createGuiItem(Material.END_ROD, "§eHalo",
            "§7Aktiviere einen Halo-Effekt"));

        // Trail toggle
        int trailCost = plugin.getConfigManager().getConfig().getInt("cosmetics.trail.cost", 1200);
        setItem(24, createGuiItem(Material.SOUL_SAND, "§eTrail",
            "§7Aktiviere eine Spur hinter dir", "§7Kosten: §6" + trailCost + " Coins"));

        // Clear particles button
        if (cosmeticsManager.getActiveParticleEffect(player) != null) {
            setItem(22, createGuiItem(Material.BARRIER, "§c§lPartikel deaktivieren",
                "§7Klicke, um deine aktiven",
                "§7Partikel zu deaktivieren"));
        }
    }

    private void setupSoundEffects(Player player) {
        for (Map.Entry<Integer, Sound> entry : soundSlots.entrySet()) {
            Sound sound = entry.getValue();
            boolean isActive = cosmeticsManager.getActiveSoundEffect(player) == sound;

            setItem(entry.getKey(), createSoundItem(sound, isActive, player));
        }

        // Clear sounds button
        if (cosmeticsManager.getActiveSoundEffect(player) != null) {
            setItem(40, createGuiItem(Material.BARRIER, "§c§lSound deaktivieren",
                "§7Klicke, um deinen aktiven",
                "§7Sound zu deaktivieren"));
        }
    }

    private ItemStack createParticleItem(Particle particle, boolean active, Player player) {
        Material material = getParticleMaterial(particle);
        String name = formatParticleName(particle);
        String permission = "cosmetics.particles." + particle.name().toLowerCase();
        int cost = plugin.getConfigManager().getConfig().getInt(
            "particle-effects.effects." + particle.name() + ".cost", 1000);

        List<String> lore = new ArrayList<>();
        if (active) {
            lore.add("§aAktiv");
            lore.add("§7Klicke zum Deaktivieren");
        } else if (!player.hasPermission(permission)) {
            lore.add("§cKeine Berechtigung");
            lore.add("§7Benötigt: " + permission);
        } else {
            lore.add("§7Kosten: §6" + cost + " Coins");
            lore.add("§7Klicke zum Aktivieren");
        }

        return createGuiItem(material, (active ? "§a" : "§7") + name,
            lore.toArray(new String[0]));
    }

    private ItemStack createSoundItem(Sound sound, boolean active, Player player) {
        Material material = getSoundMaterial(sound);
        String name = formatSoundName(sound);
        String permission = "cosmetics.sounds." + sound.name().toLowerCase();
        int cost = plugin.getConfigManager().getConfig().getInt(
            "sound-effects.effects." + sound.name() + ".cost", 800);

        List<String> lore = new ArrayList<>();
        if (active) {
            lore.add("§aAktiv");
            lore.add("§7Klicke zum Deaktivieren");
        } else if (!player.hasPermission(permission)) {
            lore.add("§cKeine Berechtigung");
            lore.add("§7Benötigt: " + permission);
        } else {
            lore.add("§7Kosten: §6" + cost + " Coins");
            lore.add("§7Klicke zum Aktivieren");
        }

        return createGuiItem(material, (active ? "§a" : "§7") + name,
            lore.toArray(new String[0]));
    }

    private void fillEmptySlots() {
        ItemStack filler = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < getInventory().getSize(); i++) {
            if (getInventory().getItem(i) == null) {
                setItem(i, filler);
            }
        }
    }

    private Material getParticleMaterial(Particle particle) {
        return switch (particle) {
            case HEART -> Material.RED_DYE;
            case FLAME -> Material.BLAZE_POWDER;
            case PORTAL -> Material.ENDER_PEARL;
            case NOTE -> Material.NOTE_BLOCK;
            case END_ROD -> Material.END_ROD;
            case TOTEM_OF_UNDYING -> Material.TOTEM_OF_UNDYING;
            case DRAGON_BREATH -> Material.DRAGON_BREATH;
            case SOUL -> Material.GLOWSTONE_DUST; // HALO visual (mapped)
            case CRIT -> Material.SUGAR; // TRAIL visual (mapped)
            default -> Material.GUNPOWDER;
        };
    }

    private Material getSoundMaterial(Sound sound) {
        // TODO: Replace with valid Bukkit Sound enum values
        // The following Sound enum values don't exist in Bukkit API:
        // ENTITY_EXPERIENCE_ORB_PICKUP, BLOCK_NOTE_BLOCK_PLING, BLOCK_AMETHYST_BLOCK_CHIME,
        // BLOCK_BELL_USE, ENTITY_PLAYER_LEVELUP, BLOCK_ENCHANTMENT_TABLE_USE, ENTITY_ENDER_DRAGON_FLAP
        return Material.MUSIC_DISC_13; // Placeholder
    }

    private String formatParticleName(Particle particle) {
        return particle.name()
            .replace("_", " ")
            .toLowerCase()
            .substring(0, 1).toUpperCase() +
            particle.name().replace("_", " ")
            .toLowerCase().substring(1);
    }

    private String formatSoundName(Sound sound) {
        String name = sound.name()
            .replace("ENTITY_", "")
            .replace("BLOCK_", "")
            .replace("_", " ")
            .toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public Particle getParticleAtSlot(int slot) {
        return particleSlots.get(slot);
    }

    public Sound getSoundAtSlot(int slot) {
        return soundSlots.get(slot);
    }
}
