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

public class AdvancedSlayerGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;
    private final AdvancedSlayerManager slayerManager;

    public AdvancedSlayerGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player);
        this.plugin = plugin;
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.slayerManager = plugin.getServiceManager().getService(AdvancedSlayerManager.class);
        this.inventory = Bukkit.createInventory(this, 54, "§8Slayer Quests");
        
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
        ItemStack titleItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§c§lSlayer Quests");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Wähle eine Slayer-Quest");
        titleLore.add("§7und kämpfe gegen mächtige Bosse!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Zombie Slayer (Revenant Horror)
        ItemStack zombieSlayer = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta zombieMeta = zombieSlayer.getItemMeta();
        zombieMeta.setDisplayName("§c§lZombie Slayer");
        List<String> zombieLore = new ArrayList<>();
        zombieLore.add("§7Boss: §cRevenant Horror");
        zombieLore.add("§7Schwierigkeit: §aEinfach - §cExtrem");
        zombieLore.add("§7Dein Level: §e" + slayerManager.getSlayerLevel(profile, "zombie"));
        zombieLore.add("§7XP: §b" + slayerManager.getSlayerXP(profile, "zombie"));
        zombieLore.add("");
        zombieLore.add("§7Fähigkeiten:");
        zombieLore.add("§4• Pest: Schaden über Zeit");
        zombieLore.add("§6• TNT-Regen: TNT vom Himmel");
        zombieLore.add("§c• Zombie-Armee: Beschwört Minions");
        zombieLore.add("");
        zombieLore.add("§7Belohnungen:");
        zombieLore.add("§a• Revenant Flesh");
        zombieLore.add("§a• Foul Flesh");
        zombieLore.add("§a• Pestilence Rune");
        zombieLore.add("");
        zombieLore.add("§7RNG Drops:");
        zombieLore.add("§d• Scythe Blade (10%)");
        zombieLore.add("§d• Smite VII Book (5%)");
        zombieLore.add("§d• Beheaded Horror (2%)");
        zombieLore.add("§d• Warden Heart (1%)");
        zombieLore.add("");
        zombieLore.add("§eKlicke um Quest zu starten!");
        zombieMeta.setLore(zombieLore);
        zombieSlayer.setItemMeta(zombieMeta);
        inventory.setItem(20, zombieSlayer);

        // Spider Slayer (Tarantula Broodmother)
        ItemStack spiderSlayer = new ItemStack(Material.SPIDER_EYE);
        ItemMeta spiderMeta = spiderSlayer.getItemMeta();
        spiderMeta.setDisplayName("§5§lSpider Slayer");
        List<String> spiderLore = new ArrayList<>();
        spiderLore.add("§7Boss: §5Tarantula Broodmother");
        spiderLore.add("§7Schwierigkeit: §eMittel - §cExtrem");
        spiderLore.add("§7Dein Level: §e" + slayerManager.getSlayerLevel(profile, "spider"));
        spiderLore.add("§7XP: §b" + slayerManager.getSlayerXP(profile, "spider"));
        spiderLore.add("");
        spiderLore.add("§7Fähigkeiten:");
        spiderLore.add("§f• Netz-Falle: Verlangsamung");
        spiderLore.add("§c• Spinnen-Schwarm: Giftige Minions");
        spiderLore.add("§2• Gift-Aura: Bereichs-Gift");
        spiderLore.add("");
        spiderLore.add("§7Belohnungen:");
        spiderLore.add("§a• Tarantula Web");
        spiderLore.add("§a• Toxic Arrow Poison");
        spiderLore.add("");
        spiderLore.add("§7RNG Drops:");
        spiderLore.add("§d• Tarantula Talisman (8%)");
        spiderLore.add("§d• Fly Swatter (5%)");
        spiderLore.add("§d• Digested Mosquito (2%)");
        spiderLore.add("");
        spiderLore.add("§eKlicke um Quest zu starten!");
        spiderMeta.setLore(spiderLore);
        spiderSlayer.setItemMeta(spiderMeta);
        inventory.setItem(22, spiderSlayer);

        // Wolf Slayer (Sven Packmaster)
        ItemStack wolfSlayer = new ItemStack(Material.BONE);
        ItemMeta wolfMeta = wolfSlayer.getItemMeta();
        wolfMeta.setDisplayName("§6§lWolf Slayer");
        List<String> wolfLore = new ArrayList<>();
        wolfLore.add("§7Boss: §6Sven Packmaster");
        wolfLore.add("§7Schwierigkeit: §6Schwer - §cExtrem");
        wolfLore.add("§7Dein Level: §e" + slayerManager.getSlayerLevel(profile, "wolf"));
        wolfLore.add("§7XP: §b" + slayerManager.getSlayerXP(profile, "wolf"));
        wolfLore.add("");
        wolfLore.add("§7Fähigkeiten:");
        wolfLore.add("§c• Heul-Aura: Ruft Rudel herbei");
        wolfLore.add("§e• Agilität: Extreme Geschwindigkeit");
        wolfLore.add("§4• True Damage: Ignoriert Verteidigung");
        wolfLore.add("");
        wolfLore.add("§7Belohnungen:");
        wolfLore.add("§a• Wolf Tooth");
        wolfLore.add("§a• Hamster Wheel");
        wolfLore.add("");
        wolfLore.add("§7RNG Drops:");
        wolfLore.add("§d• Red Claw Egg (12%)");
        wolfLore.add("§d• Grizzly Bait (8%)");
        wolfLore.add("§d• Overflux Capacitor (3%)");
        wolfLore.add("");
        wolfLore.add("§eKlicke um Quest zu starten!");
        wolfMeta.setLore(wolfLore);
        wolfSlayer.setItemMeta(wolfMeta);
        inventory.setItem(24, wolfSlayer);

        // Enderman Slayer (Coming Soon)
        ItemStack endermanSlayer = new ItemStack(Material.ENDER_PEARL);
        ItemMeta endermanMeta = endermanSlayer.getItemMeta();
        endermanMeta.setDisplayName("§d§lEnderman Slayer");
        List<String> endermanLore = new ArrayList<>();
        endermanLore.add("§7Boss: §dVoidgloom Seraph");
        endermanLore.add("§7Schwierigkeit: §cExtrem - §4Unmöglich");
        endermanLore.add("§7Dein Level: §e" + slayerManager.getSlayerLevel(profile, "enderman"));
        endermanLore.add("§7XP: §b" + slayerManager.getSlayerXP(profile, "enderman"));
        endermanLore.add("");
        endermanLore.add("§7Fähigkeiten:");
        endermanLore.add("§d• Teleportation: Blinkt umher");
        endermanLore.add("§4• Void Strike: Massive Schäden");
        endermanLore.add("§8• Shadow Clone: Erstellt Klone");
        endermanLore.add("");
        endermanLore.add("§c§lCOMING SOON!");
        endermanMeta.setLore(endermanLore);
        endermanSlayer.setItemMeta(endermanMeta);
        inventory.setItem(30, endermanSlayer);

        // Blaze Slayer (Coming Soon)
        ItemStack blazeSlayer = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta blazeMeta = blazeSlayer.getItemMeta();
        blazeMeta.setDisplayName("§6§lBlaze Slayer");
        List<String> blazeLore = new ArrayList<>();
        blazeLore.add("§7Boss: §6Inferno Demonlord");
        blazeLore.add("§7Schwierigkeit: §4Unmöglich");
        blazeLore.add("§7Dein Level: §e" + slayerManager.getSlayerLevel(profile, "blaze"));
        blazeLore.add("§7XP: §b" + slayerManager.getSlayerXP(profile, "blaze"));
        blazeLore.add("");
        blazeLore.add("§7Fähigkeiten:");
        blazeLore.add("§6• Inferno: Brennt alles nieder");
        blazeLore.add("§c• Fire Storm: Feuersturm");
        blazeLore.add("§4• Hell's Wrath: Höllen-Zorn");
        blazeLore.add("");
        blazeLore.add("§c§lCOMING SOON!");
        blazeMeta.setLore(blazeLore);
        blazeSlayer.setItemMeta(blazeMeta);
        inventory.setItem(32, blazeSlayer);

        // Current Quest Status
        if (profile.getActiveSlayerQuest() != null) {
            ItemStack currentQuest = new ItemStack(Material.BOOK);
            ItemMeta currentMeta = currentQuest.getItemMeta();
            currentMeta.setDisplayName("§a§lAktive Quest");
            List<String> currentLore = new ArrayList<>();
            currentLore.add("§7Typ: §e" + profile.getActiveSlayerQuest().getSlayerType());
            currentLore.add("§7Tier: §e" + profile.getActiveSlayerQuest().getTier());
            currentLore.add("§7Fortschritt: §a" + profile.getActiveSlayerQuest().getKillsCompleted() + 
                "§7/§a" + profile.getActiveSlayerQuest().getKillsRequired());
            currentLore.add("");
            currentLore.add("§cKlicke um Quest abzubrechen!");
            currentMeta.setLore(currentLore);
            currentQuest.setItemMeta(currentMeta);
            inventory.setItem(40, currentQuest);
        }

        addNavigationButtons();
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
        if (event.getSlot() == 45) { // Back button
            clickedPlayer.closeInventory();
            return;
        }
        if (event.getSlot() == 53) { // Close button
            clickedPlayer.closeInventory();
            return;
        }
        
        // Handle slayer quest selection
        switch (event.getSlot()) {
            case 20: // Zombie Slayer
                openTierSelection(clickedPlayer, "zombie");
                break;
            case 22: // Spider Slayer
                openTierSelection(clickedPlayer, "spider");
                break;
            case 24: // Wolf Slayer
                openTierSelection(clickedPlayer, "wolf");
                break;
            case 30: // Enderman Slayer
                clickedPlayer.sendMessage("§cEnderman Slayer ist noch nicht verfügbar!");
                break;
            case 32: // Blaze Slayer
                clickedPlayer.sendMessage("§cBlaze Slayer ist noch nicht verfügbar!");
                break;
            case 40: // Cancel current quest
                cancelCurrentQuest(clickedPlayer);
                break;
        }
    }

    private void openTierSelection(Player player, String slayerType) {
        player.closeInventory();
        new SlayerTierSelectionGUI(plugin, player, slayerType).open();
    }

    private void cancelCurrentQuest(Player player) {
        PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
        if (profile == null || profile.getActiveSlayerQuest() == null) {
            player.sendMessage("§cDu hast keine aktive Quest!");
            return;
        }

        profile.setActiveSlayerQuest(null);
        player.sendMessage("§aQuest abgebrochen!");
        
        // Refresh GUI
        setMenuItems();
    }
}
