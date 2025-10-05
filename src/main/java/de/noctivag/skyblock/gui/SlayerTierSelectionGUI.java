package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.services.AdvancedSlayerManager;
import de.noctivag.skyblock.services.PlayerProfileService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SlayerTierSelectionGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;
    private final AdvancedSlayerManager slayerManager;
    private final String slayerType;

    public SlayerTierSelectionGUI(SkyblockPluginRefactored plugin, Player player, String slayerType) {
        super(player);
        this.plugin = plugin;
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.slayerManager = plugin.getServiceManager().getService(AdvancedSlayerManager.class);
        this.slayerType = slayerType;
        this.inventory = Bukkit.createInventory(this, 27, "§8" + slayerType + " Slayer - Tier Auswahl");
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Title item
        ItemStack titleItem = new ItemStack(getSlayerIcon());
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§c§l" + slayerType.toUpperCase() + " SLAYER");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Wähle einen Schwierigkeitsgrad");
        titleLore.add("§7Dein Level: §e" + slayerManager.getSlayerLevel(profile, slayerType));
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Tier 1
        ItemStack tier1 = createTierItem(1, profile);
        inventory.setItem(10, tier1);

        // Tier 2
        ItemStack tier2 = createTierItem(2, profile);
        inventory.setItem(12, tier2);

        // Tier 3
        ItemStack tier3 = createTierItem(3, profile);
        inventory.setItem(14, tier3);

        // Tier 4
        ItemStack tier4 = createTierItem(4, profile);
        inventory.setItem(16, tier4);

        // Tier 5
        ItemStack tier5 = createTierItem(5, profile);
        inventory.setItem(22, tier5);

        addNavigationButtons();
    }

    private ItemStack createTierItem(int tier, PlayerProfile profile) {
        Material icon = getTierIcon(tier);
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        
        String tierName = getTierName(tier);
        meta.setDisplayName("§e§lTier " + tier + " §7- " + tierName);
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Schwierigkeit: " + getTierDifficulty(tier));
        lore.add("§7Kosten: §6" + (tier * 1000) + " Coins");
        lore.add("§7Kills benötigt: §a" + (tier * 10));
        lore.add("§7XP Belohnung: §b" + (tier * 50));
        lore.add("§7Coin Belohnung: §6" + (tier * 500));
        lore.add("");
        
        // Check if player can access this tier
        int requiredLevel = tier - 1;
        int currentLevel = slayerManager.getSlayerLevel(profile, slayerType);
        
        if (currentLevel >= requiredLevel) {
            lore.add("§a✓ Du kannst diese Quest starten!");
            lore.add("§eKlicke um zu starten!");
        } else {
            lore.add("§c✗ Du benötigst Level " + requiredLevel);
            lore.add("§cAktuell: Level " + currentLevel);
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private Material getSlayerIcon() {
        switch (slayerType.toLowerCase()) {
            case "zombie":
                return Material.ROTTEN_FLESH;
            case "spider":
                return Material.SPIDER_EYE;
            case "wolf":
                return Material.BONE;
            case "enderman":
                return Material.ENDER_PEARL;
            case "blaze":
                return Material.BLAZE_POWDER;
            default:
                return Material.SKULL_ITEM;
        }
    }

    private Material getTierIcon(int tier) {
        switch (tier) {
            case 1:
                return Material.WOODEN_SWORD;
            case 2:
                return Material.STONE_SWORD;
            case 3:
                return Material.IRON_SWORD;
            case 4:
                return Material.DIAMOND_SWORD;
            case 5:
                return Material.NETHERITE_SWORD;
            default:
                return Material.STICK;
        }
    }

    private String getTierName(int tier) {
        switch (tier) {
            case 1:
                return "§aEinfach";
            case 2:
                return "§eMittel";
            case 3:
                return "§6Schwer";
            case 4:
                return "§cExtrem";
            case 5:
                return "§4Unmöglich";
            default:
                return "§7Unbekannt";
        }
    }

    private String getTierDifficulty(int tier) {
        switch (tier) {
            case 1:
                return "§aEinfach";
            case 2:
                return "§eMittel";
            case 3:
                return "§6Schwer";
            case 4:
                return "§cExtrem";
            case 5:
                return "§4Unmöglich";
            default:
                return "§7Unbekannt";
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getInventory().equals(inventory)) return;
        
        event.setCancelled(true);
        
        Player clickedPlayer = (Player) event.getWhoClicked();
        if (!clickedPlayer.equals(player)) return;
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle navigation buttons
        if (event.getSlot() == 18) { // Back button
            clickedPlayer.closeInventory();
            new AdvancedSlayerGUI(plugin, clickedPlayer).open();
            return;
        }
        if (event.getSlot() == 26) { // Close button
            clickedPlayer.closeInventory();
            return;
        }
        
        // Handle tier selection
        int tier = -1;
        switch (event.getSlot()) {
            case 10:
                tier = 1;
                break;
            case 12:
                tier = 2;
                break;
            case 14:
                tier = 3;
                break;
            case 16:
                tier = 4;
                break;
            case 22:
                tier = 5;
                break;
        }
        
        if (tier > 0) {
            startSlayerQuest(clickedPlayer, tier);
        }
    }

    private void startSlayerQuest(Player player, int tier) {
        PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Check if player can access this tier
        int requiredLevel = tier - 1;
        int currentLevel = slayerManager.getSlayerLevel(profile, slayerType);
        
        if (currentLevel < requiredLevel) {
            player.sendMessage("§cDu benötigst Level " + requiredLevel + " für Tier " + tier + "!");
            return;
        }

        // Check if player has enough coins
        double cost = tier * 1000;
        if (profile.getCoins() < cost) {
            player.sendMessage("§cDu benötigst " + cost + " Coins für diese Quest!");
            return;
        }

        // Start the quest
        slayerManager.startSlayerQuest(player, slayerType, tier).thenAccept(success -> {
            if (success) {
                player.closeInventory();
            } else {
                player.sendMessage("§cFehler beim Starten der Quest!");
            }
        });
    }
}
