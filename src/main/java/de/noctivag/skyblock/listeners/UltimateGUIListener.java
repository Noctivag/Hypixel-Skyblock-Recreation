package de.noctivag.skyblock.listeners;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

public class UltimateGUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public UltimateGUIListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        InventoryView view = event.getView();
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(view.title());
        
        // Handle Ultimate Main Menu
        if (title.contains("ULTIMATE MENU")) {
            handleUltimateMainMenuClick(event, player);
        }
        // Handle Ultimate Event GUI
        else if (title.contains("ULTIMATE EVENTS")) {
            handleUltimateEventGUIClick(event, player);
        }
        // Handle Enhanced Achievement GUI
        else if (title.contains("Achievements")) {
            handleEnhancedAchievementGUIClick(event, player);
        }
        // Handle Enhanced Daily Reward GUI
        else if (title.contains("Daily Rewards")) {
            handleEnhancedDailyRewardGUIClick(event, player);
        }
        // Handle Enhanced Cosmetics GUI
        else if (title.contains("Enhanced Cosmetics")) {
            handleEnhancedCosmeticsGUIClick(event, player);
        }
        // Handle Potato Book GUI
        else if (title.contains("Potato Book System")) {
            handlePotatoBookGUIClick(event, player);
        }
        // Handle Recombobulator GUI
        else if (title.contains("Recombobulator 3000")) {
            handleRecombobulatorGUIClick(event, player);
        }
        // Handle Dungeon Star GUI
        else if (title.contains("Dungeon Star System")) {
            handleDungeonStarGUIClick(event, player);
        }
        // Handle Pet Item GUI
        else if (title.contains("Pet Item System")) {
            handlePetItemGUIClick(event, player);
        }
        // Handle Armor Ability GUI
        else if (title.contains("Armor Ability System")) {
            handleArmorAbilityGUIClick(event, player);
        }
        // Handle Weapon Ability GUI
        else if (title.contains("Weapon Ability System")) {
            handleWeaponAbilityGUIClick(event, player);
        }
    }

    private void handleUltimateMainMenuClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Core Features (10-18)
            case 10 -> new ProfileGUI(SkyblockPlugin, player).openGUI(player);
            case 11 -> new EnhancedDailyRewardGUI(SkyblockPlugin, player).openGUI(player);
            case 12 -> new EnhancedAchievementGUI(SkyblockPlugin, player).openGUI(player);
            case 13 -> new EnhancedCosmeticsGUI(SkyblockPlugin, player).openGUI(player);
            case 14 -> new KitShopGUI(SkyblockPlugin, player).openGUI(player);
            case 15 -> new WarpGUI(SkyblockPlugin, player).openGUI(player);
            case 16 -> new TeleportGUI(SkyblockPlugin, player).openGUI(player);
            case 17 -> new JoinMessageGUI(SkyblockPlugin, player).openGUI(player);
            case 18 -> new BasicCommandsGUI(SkyblockPlugin).openGUI(player);
            
            // Combat & Events (19-27)
            case 19 -> new UltimateEventGUI(SkyblockPlugin, player).openGUI(player);
            case 20 -> new PvPArenaGUI(SkyblockPlugin, player).openGUI(player);
            case 21 -> new MobArenaGUI(SkyblockPlugin, player).openGUI(player);
            case 22 -> new DuelSystemGUI(SkyblockPlugin, player).openGUI(player);
            case 23 -> new TournamentGUI(SkyblockPlugin, player).openGUI(player);
            case 24 -> new BattlePassGUI(SkyblockPlugin, player).openGUI(player);
            case 25 -> new QuestGUI(SkyblockPlugin, player).openGUI(player);
            case 26 -> new StatisticsGUI(SkyblockPlugin, player).openGUI(player);
            case 27 -> new LeaderboardGUI(SkyblockPlugin, player).openGUI(player);
            
            // Social & Economy (28-36)
            case 28 -> new PartyGUI(SkyblockPlugin, player).openGUI(player);
            case 29 -> new FriendsGUI(SkyblockPlugin, player).openGUI(player);
            case 30 -> new GuildSystemGUI(SkyblockPlugin, player).openGUI(player);
            case 31 -> new ChatChannelsGUI(SkyblockPlugin, player).openGUI(player);
            case 32 -> new EconomyGUI(SkyblockPlugin, player).openGUI(player);
            case 33 -> new ShopGUI(SkyblockPlugin, player).openGUI(player);
            case 34 -> new AuctionHouseGUI(SkyblockPlugin).openAuctionHouseGUI(player);
            case 35 -> new BankGUI(SkyblockPlugin).openBankGUI(player);
            case 36 -> new JobsGUI(SkyblockPlugin, player).openGUI(player);
            
            // New Systems (37-43)
            case 37 -> new PotatoBookGUI(SkyblockPlugin, player).openGUI(player);
            case 38 -> new RecombobulatorGUI(SkyblockPlugin, player).openGUI(player);
            case 39 -> new DungeonStarGUI(SkyblockPlugin, player).open(player);
            case 40 -> new PetItemGUI(SkyblockPlugin, player).openGUI(player);
            case 41 -> new ArmorAbilityGUI(SkyblockPlugin, player).openGUI(player);
            case 42 -> new WeaponAbilityGUI(SkyblockPlugin, player).openGUI(player);
            case 43 -> new NPCManagementGUI(SkyblockPlugin, player).openGUI(player);
            
            // Core Hypixel SkyBlock Features (44-46)
            case 44 -> {
                // Booster Cookie
                if (SkyblockPlugin.getEconomyManager().getBalance(player) >= 1000) {
                    SkyblockPlugin.getEconomyManager().withdrawMoney(player, 1000);
                    player.getInventory().addItem((org.bukkit.inventory.ItemStack) SkyblockPlugin.createBoosterCookie());
                    player.sendMessage(Component.text("§aBooster Cookie gekauft!"));
                } else {
                    player.sendMessage(Component.text("§cDu brauchst mindestens 1000 Coins für einen Booster Cookie!"));
                }
            }
            case 45 -> SkyblockPlugin.openRecipeBook(player);
            case 46 -> SkyblockPlugin.openCalendar(player);
            
            // Utility & Admin (47-48)
            case 47 -> new SettingsGUI(SkyblockPlugin).openGUI(player);
            case 48 -> new AdminMenu(SkyblockPlugin, player).open(player);
            
            // Navigation & Info (49-53)
            case 49 -> new EventScheduleGUI(SkyblockPlugin, player).openGUI(player);
            case 50 -> new ServerInfoGUI(SkyblockPlugin).openServerInfoGUI(player);
            case 51 -> new QuickActionsGUI(SkyblockPlugin, player).openGUI(player);
            case 52 -> player.closeInventory();
            case 53 -> new HelpGUI(SkyblockPlugin).openHelpGUI(player);
        }
    }

    private void handleUltimateEventGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Category buttons (10-14)
            case 10 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("dragon");
                gui.openGUI(player);
            }
            case 11 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("undead");
                gui.openGUI(player);
            }
            case 12 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("elemental");
                gui.openGUI(player);
            }
            case 13 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("monster");
                gui.openGUI(player);
            }
            case 14 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("all");
                gui.openGUI(player);
            }
            case 15 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("dark");
                gui.openGUI(player);
            }
            case 16 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("celestial");
                gui.openGUI(player);
            }
            case 17 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("mechanical");
                gui.openGUI(player);
            }
            case 18 -> {
                UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player);
                gui.setCategory("nature");
                gui.openGUI(player);
            }
            
            // Event slots (19-27)
            case 19, 20, 21, 22, 23, 24, 25, 26, 27 -> {
                // Get event ID from slot
                String eventId = getEventIdFromSlot(slot);
                if (eventId != null) {
                    SkyblockPlugin.joinEvent(player, eventId);
                }
            }
            
            // Info buttons (28-32)
            case 28 -> new EventScheduleGUI(SkyblockPlugin, player).openGUI(player);
            case 30 -> new EventStatisticsGUI(SkyblockPlugin, player).openGUI(player);
            case 32 -> new EventRewardsGUI(SkyblockPlugin, player).openGUI(player);
            
            // Navigation (45-49)
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
        }
    }

    private void handleEnhancedAchievementGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Category buttons (10-16)
            case 10 -> {
                EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player);
                gui.setCategory("events");
                gui.openGUI(player);
            }
            case 11 -> {
                EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player);
                gui.setCategory("economy");
                gui.openGUI(player);
            }
            case 12 -> {
                EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player);
                gui.setCategory("cosmetics");
                gui.openGUI(player);
            }
            case 13 -> {
                EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player);
                gui.setCategory("social");
                gui.openGUI(player);
            }
            case 14 -> {
                EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player);
                gui.setCategory("combat");
                gui.openGUI(player);
            }
            case 15 -> {
                EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player);
                gui.setCategory("exploration");
                gui.openGUI(player);
            }
            case 16 -> {
                EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player);
                gui.setCategory("all");
                gui.openGUI(player);
            }
            
            // Navigation (45-49)
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
        }
    }

    private void handleEnhancedDailyRewardGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Claim button (30)
            case 30 -> {
                if (SkyblockPlugin.canClaimReward(player)) {
                    SkyblockPlugin.claimReward(player);
                    new EnhancedDailyRewardGUI(SkyblockPlugin, player).openGUI(player); // Refresh
                } else {
                    player.sendMessage(Component.text("§cDu hast deine tägliche Belohnung bereits erhalten!"));
                }
            }
            
            // Navigation (45-49)
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
        }
    }

    private void handleEnhancedCosmeticsGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Category buttons (10-14)
            case 10 -> {
                EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player);
                gui.setCategory("particles");
                gui.openGUI(player);
            }
            case 11 -> {
                EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player);
                gui.setCategory("wings");
                gui.openGUI(player);
            }
            case 12 -> {
                EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player);
                gui.setCategory("halos");
                gui.openGUI(player);
            }
            case 13 -> {
                EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player);
                gui.setCategory("trails");
                gui.openGUI(player);
            }
            case 14 -> {
                EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player);
                gui.setCategory("sounds");
                gui.openGUI(player);
            }
            
            // Action buttons (28-32)
            case 28 -> {
                SkyblockPlugin.deactivateAllCosmetics(player);
                player.sendMessage(Component.text("§aAlle Cosmetics deaktiviert!"));
            }
            case 30 -> new MyCosmeticsGUI(SkyblockPlugin, player).openGUI(player);
            case 32 -> new CosmeticShopGUI(SkyblockPlugin, player).openGUI(player);
            
            // Navigation (45-49)
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
        }
    }

    private String getEventIdFromSlot(int slot) {
        // Map slots to event IDs - now with 35+ bosses
        return switch (slot) {
            // Row 1 (19-27) - Original Bosses
            case 19 -> "ender_dragon";
            case 20 -> "wither";
            case 21 -> "titan";
            case 22 -> "elder_guardian";
            case 23 -> "ravager";
            case 24 -> "phantom_king";
            case 25 -> "blaze_king";
            case 26 -> "enderman_lord";
            case 27 -> "dragon_king";
            
            // Row 2 (28-36) - Advanced Bosses
            case 28 -> "shadow_lord";
            case 29 -> "ice_queen";
            case 30 -> "fire_emperor";
            case 31 -> "storm_king";
            case 32 -> "void_master";
            case 33 -> "cosmic_guardian";
            case 34 -> "phoenix_king";
            case 35 -> "kraken_lord";
            case 36 -> "minotaur_king";
            
            // Row 3 (37-45) - Mythical & Elemental Bosses
            case 37 -> "cerberus";
            case 38 -> "hydra";
            case 39 -> "earth_titan";
            case 40 -> "wind_spirit";
            case 41 -> "water_serpent";
            case 42 -> "lava_golem";
            case 43 -> "shadow_beast";
            case 44 -> "vampire_lord";
            case 45 -> "lich_king";
            
            // Row 4 (46-54) - Celestial & Mechanical Bosses
            case 46 -> "demon_prince";
            case 47 -> "angel_archon";
            case 48 -> "star_weaver";
            case 49 -> "moon_goddess";
            case 50 -> "sun_emperor";
            case 51 -> "steam_giant";
            case 52 -> "crystal_guardian";
            case 53 -> "clockwork_dragon";
            case 54 -> "gear_titan";
            
            // Row 5 (55-63) - Nature Bosses
            case 55 -> "forest_guardian";
            case 56 -> "mountain_spirit";
            case 57 -> "desert_pharaoh";
            case 58 -> "ocean_leviathan";
            
            default -> null;
        };
    }

    private void handlePotatoBookGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Create Hot Potato Book
            case 11 -> {
                if (SkyblockPlugin.getEconomyManager().getBalance(player) >= 100) {
                    SkyblockPlugin.getEconomyManager().withdrawMoney(player, 100);
                    player.getInventory().addItem((org.bukkit.inventory.ItemStack) SkyblockPlugin.createHotPotatoBook());
                    player.sendMessage(Component.text("§aHot Potato Book erstellt!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast nicht genug Geld! (Benötigt: 100 coins)"));
                }
            }
            
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
            case 51 -> new PotatoBookGUI(SkyblockPlugin, player).openGUI(player); // Refresh
        }
    }

    private void handleRecombobulatorGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Create Recombobulator
            case 11 -> {
                if (SkyblockPlugin.getEconomyManager().getBalance(player) >= 1000) {
                    SkyblockPlugin.getEconomyManager().withdrawMoney(player, 1000);
                    player.getInventory().addItem((org.bukkit.inventory.ItemStack) SkyblockPlugin.createRecombobulator3000());
                    player.sendMessage(Component.text("§aRecombobulator 3000 erstellt!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast nicht genug Geld! (Benötigt: 1,000 coins)"));
                }
            }
            
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
            case 51 -> new RecombobulatorGUI(SkyblockPlugin, player).open(player); // Refresh
        }
    }

    private void handleDungeonStarGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Create Dungeon Star
            case 15 -> {
                if (SkyblockPlugin.getEconomyManager().getBalance(player) >= 100) {
                    SkyblockPlugin.getEconomyManager().withdrawMoney(player, 100);
                    player.getInventory().addItem((org.bukkit.inventory.ItemStack) SkyblockPlugin.createDungeonStar(1));
                    player.sendMessage(Component.text("§aDungeon Star erstellt!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast nicht genug Geld! (Benötigt: 100 coins)"));
                }
            }
            
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
            case 51 -> new DungeonStarGUI(SkyblockPlugin, player).open(player); // Refresh
        }
    }

    private void handlePetItemGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Create Pet Item
            case 15 -> {
                // Open pet item creation menu
                player.sendMessage(Component.text("§aPet Item Erstellung geöffnet!"));
            }
            
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
            case 51 -> new PetItemGUI(SkyblockPlugin, player).openGUI(player); // Refresh
        }
    }

    private void handleArmorAbilityGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
            case 51 -> new ArmorAbilityGUI(SkyblockPlugin, player).openGUI(player); // Refresh
        }
    }

    private void handleWeaponAbilityGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open(player);
            case 49 -> player.closeInventory();
            case 51 -> new WeaponAbilityGUI(SkyblockPlugin, player).openGUI(player); // Refresh
        }
    }
}
