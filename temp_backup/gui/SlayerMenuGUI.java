package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.enums.SlayerType;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.models.SlayerQuest;
import de.noctivag.skyblock.services.PlayerProfileService;
import de.noctivag.skyblock.services.SlayerManager;
import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Slayer-Menü GUI für die Anzeige aller Slayer-Bosse und aktiver Quests
 * Zeigt Slayer-Typen, Level, Kosten und aktuelle Quest an
 */
public class SlayerMenuGUI extends Menu {
    
    private final SkyblockPlugin plugin;
    private final PlayerProfileService profileService;
    private final SlayerManager slayerManager;
    
    // Slayer-Slots (3x2 Grid)
    private static final int[] SLAYER_SLOTS = {
        19, 20, 21,  // Reihe 1
        28, 29, 30   // Reihe 2
    };
    
    // Info-Slot für aktive Quest
    private static final int ACTIVE_QUEST_SLOT = 25;
    
    public SlayerMenuGUI(Player player, SkyblockPlugin plugin) {
        super(player, 54); // 6 Reihen
        this.plugin = plugin;
        this.profileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.slayerManager = plugin.getServiceManager().getService(SlayerManager.class);
    }
    
    @Override
    public String getMenuTitle() {
        return "§6§lSlayer Menu";
    }
    
    @Override
    public void setMenuItems() {
        // Fülle Border
        fillBorderWith(BORDER_MATERIAL);
        
        // Lade Spielerprofil asynchron
        loadAndDisplaySlayers();
        
        // Navigation-Buttons
        addNavigationButtons();
    }
    
    /**
     * Lädt das Spielerprofil und zeigt die Slayer an
     */
    private void loadAndDisplaySlayers() {
        UUID playerUUID = player.getUniqueId();
        
        CompletableFuture<PlayerProfile> profileFuture = profileService.loadProfile(playerUUID);
        profileFuture.thenAccept(profile -> {
            if (profile != null) {
                // Zeige Slayer im Haupt-Thread an
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    displaySlayers(profile);
                });
            } else {
                // Fallback: Zeige Standard-Slayer
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    displayDefaultSlayers();
                });
            }
        }).exceptionally(throwable -> {
            plugin.getLogger().warning("Error loading player profile for slayers: " + throwable.getMessage());
            // Fallback: Zeige Standard-Slayer
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                displayDefaultSlayers();
            });
            return null;
        });
    }
    
    /**
     * Zeigt die Slayer basierend auf dem Spielerprofil an
     * @param profile Das Spielerprofil
     */
    private void displaySlayers(PlayerProfile profile) {
        SlayerType[] slayers = SlayerType.values();
        
        for (int i = 0; i < Math.min(slayers.length, SLAYER_SLOTS.length); i++) {
            SlayerType slayer = slayers[i];
            int slot = SLAYER_SLOTS[i];
            
            ItemStack slayerItem = createSlayerItem(slayer, profile);
            inventory.setItem(slot, slayerItem);
        }
        
        // Zeige aktive Quest
        displayActiveQuest(profile);
    }
    
    /**
     * Zeigt Standard-Slayer an (Fallback)
     */
    private void displayDefaultSlayers() {
        SlayerType[] slayers = SlayerType.values();
        
        for (int i = 0; i < Math.min(slayers.length, SLAYER_SLOTS.length); i++) {
            SlayerType slayer = slayers[i];
            int slot = SLAYER_SLOTS[i];
            
            ItemStack slayerItem = createDefaultSlayerItem(slayer);
            inventory.setItem(slot, slayerItem);
        }
        
        // Zeige Standard-Info
        displayDefaultActiveQuest();
    }
    
    /**
     * Erstellt ein Slayer-Item basierend auf dem Spielerprofil
     * @param slayer Der Slayer-Typ
     * @param profile Das Spielerprofil
     * @return Slayer-Item
     */
    private ItemStack createSlayerItem(SlayerType slayer, PlayerProfile profile) {
        ItemStack item = new ItemStack(slayer.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Slayer-Level aus Profil
            int slayerLevel = getSlayerLevel(profile, slayer);
            int slayerXP = profile.getSlayerXP(slayer.name());
            
            // Name
            meta.displayName(Component.text("§6" + slayer.getGermanName())
                .color(NamedTextColor.GOLD));
            
            // Lore
            List<String> lore = new ArrayList<>();
            lore.add("§7" + slayer.getDescription());
            lore.add("");
            lore.add("§7Level: §6" + slayerLevel);
            lore.add("§7XP: §2" + formatNumber(slayerXP));
            lore.add("");
            lore.add("§7Verfügbare Tiers:");
            
            // Zeige verfügbare Tiers
            for (int tier = slayer.getMinTier(); tier <= slayer.getMaxTier(); tier++) {
                double cost = slayer.getCostForTier(tier);
                String costText = formatNumber(cost);
                
                if (profile.getCoins() >= cost) {
                    lore.add("§7Tier " + tier + ": §a" + costText + "§7 Coins §a✓");
                } else {
                    lore.add("§7Tier " + tier + ": §c" + costText + "§7 Coins §c✗");
                }
            }
            
            lore.add("");
            lore.add("§eKlicke für Details!");
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Erstellt ein Standard-Slayer-Item (Fallback)
     * @param slayer Der Slayer-Typ
     * @return Standard-Slayer-Item
     */
    private ItemStack createDefaultSlayerItem(SlayerType slayer) {
        ItemStack item = new ItemStack(slayer.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6" + slayer.getGermanName())
                .color(NamedTextColor.GOLD));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7" + slayer.getDescription());
            lore.add("");
            lore.add("§7Level: §61");
            lore.add("§7XP: §20");
            lore.add("");
            lore.add("§7Verfügbare Tiers:");
            
            for (int tier = slayer.getMinTier(); tier <= slayer.getMaxTier(); tier++) {
                double cost = slayer.getCostForTier(tier);
                String costText = formatNumber(cost);
                lore.add("§7Tier " + tier + ": §a" + costText + "§7 Coins §a✓");
            }
            
            lore.add("");
            lore.add("§eKlicke für Details!");
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Zeigt die aktive Quest an
     * @param profile Das Spielerprofil
     */
    private void displayActiveQuest(PlayerProfile profile) {
        SlayerQuest activeQuest = profile.getActiveSlayerQuest();
        
        if (activeQuest != null && activeQuest.isActive()) {
            ItemStack questItem = new ItemStack(Material.BOOK);
            ItemMeta meta = questItem.getItemMeta();
            
            if (meta != null) {
                meta.displayName(Component.text("§a§lAktive Quest")
                    .color(NamedTextColor.GREEN));
                
                List<String> lore = new ArrayList<>();
                lore.add("§7Boss: §6" + activeQuest.getSlayerType().name());
                lore.add("§7Tier: §6" + activeQuest.getTier());
                lore.add("§7Fortschritt: §6" + activeQuest.getCurrentKills() + "§7/§6" + activeQuest.getRequiredKills());
                lore.add("§7" + createProgressBar(activeQuest.getCurrentKills(), activeQuest.getRequiredKills(), 20));
                lore.add("");
                lore.add("§7Zeit: §6" + activeQuest.getQuestDurationSeconds() + "§7 Sekunden");
                lore.add("");
                lore.add("§cKlicke zum Abbrechen!");
                
                meta.lore(lore.stream().map(Component::text).toList());
            }
            
            questItem.setItemMeta(meta);
            inventory.setItem(ACTIVE_QUEST_SLOT, questItem);
        } else {
            // Keine aktive Quest
            ItemStack noQuestItem = new ItemStack(Material.BARRIER);
            ItemMeta meta = noQuestItem.getItemMeta();
            
            if (meta != null) {
                meta.displayName(Component.text("§7§lKeine aktive Quest")
                    .color(NamedTextColor.GRAY));
                
                List<String> lore = Arrays.asList(
                    "§7Du hast derzeit keine",
                    "§7aktive Slayer-Quest.",
                    "",
                    "§7Wähle einen Slayer aus,",
                    "§7um eine Quest zu starten!"
                );
                
                meta.lore(lore.stream().map(Component::text).toList());
            }
            
            noQuestItem.setItemMeta(meta);
            inventory.setItem(ACTIVE_QUEST_SLOT, noQuestItem);
        }
    }
    
    /**
     * Zeigt Standard-Info für aktive Quest an (Fallback)
     */
    private void displayDefaultActiveQuest() {
        ItemStack noQuestItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = noQuestItem.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§7§lKeine aktive Quest")
                .color(NamedTextColor.GRAY));
            
            List<String> lore = Arrays.asList(
                "§7Du hast derzeit keine",
                "§7aktive Slayer-Quest.",
                "",
                "§7Wähle einen Slayer aus,",
                "§7um eine Quest zu starten!"
            );
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        noQuestItem.setItemMeta(meta);
        inventory.setItem(ACTIVE_QUEST_SLOT, noQuestItem);
    }
    
    /**
     * Berechnet das Slayer-Level basierend auf XP
     * @param profile Das Spielerprofil
     * @param slayer Der Slayer-Typ
     * @return Slayer-Level
     */
    private int getSlayerLevel(PlayerProfile profile, SlayerType slayer) {
        int xp = profile.getSlayerXP(slayer.name());
        
        // Vereinfachte Level-Berechnung: 100 XP pro Level
        return Math.max(1, xp / 100);
    }
}
