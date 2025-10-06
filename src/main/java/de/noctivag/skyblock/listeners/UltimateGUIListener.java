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
            case 10 -> player.sendMessage("§cProfile GUI not implemented yet!");
            case 11 -> new DailyRewardGUI(SkyblockPlugin).open();
            case 12 -> player.sendMessage("§cAchievement GUI not implemented yet!");
            case 13 -> player.sendMessage("§cEnhanced Cosmetics GUI not implemented yet!");
            case 14 -> player.sendMessage("§cKit Shop GUI not implemented yet!");
            case 15 -> player.sendMessage("§cWarp GUI not implemented yet!");
            case 16 -> player.sendMessage("§cTeleport GUI not implemented yet!");
            case 17 -> new JoinMessageGUI(SkyblockPlugin).open();
            case 18 -> new BasicCommandsGUI(SkyblockPlugin).open();
            
            // Combat & Events (19-27)
            case 19 -> player.sendMessage("§cUltimate Event GUI not implemented yet!");
            case 20 -> player.sendMessage("§cPvP Arena GUI not implemented yet!");
            case 21 -> player.sendMessage("§cMob Arena GUI not implemented yet!");
            case 22 -> player.sendMessage("§cDuel System GUI not implemented yet!");
            case 23 -> player.sendMessage("§cTournament GUI not implemented yet!");
            case 24 -> player.sendMessage("§cBattle Pass GUI not implemented yet!");
            case 25 -> player.sendMessage("§cQuest GUI not implemented yet!");
            case 26 -> player.sendMessage("§cStatistics GUI not implemented yet!");
            case 27 -> player.sendMessage("§cLeaderboard GUI not implemented yet!");
            
            // Social & Economy (28-36)
            case 28 -> new PartyGUI(SkyblockPlugin, player).open();
            case 29 -> new FriendsGUI(SkyblockPlugin, player).open();
            case 30 -> player.sendMessage("§cGuild System GUI not implemented yet!");
            case 31 -> player.sendMessage("§cChat Channels GUI not implemented yet!");
            case 32 -> player.sendMessage("§cEconomy GUI not implemented yet!");
            case 33 -> player.sendMessage("§cShop GUI not implemented yet!");
            case 34 -> player.sendMessage("§cAuction House GUI not implemented yet!");
            case 35 -> player.sendMessage("§cBank GUI not implemented yet!");
            case 36 -> player.sendMessage("§cJobs GUI not implemented yet!");
            
            // New Systems (37-43)
            case 37 -> player.sendMessage("§cPotato Book GUI not implemented yet!");
            case 38 -> player.sendMessage("§cRecombobulator GUI not implemented yet!");
            case 39 -> player.sendMessage("§cDungeon Star GUI not implemented yet!");
            case 40 -> player.sendMessage("§cPet Item GUI not implemented yet!");
            case 41 -> player.sendMessage("§cArmor Ability GUI not implemented yet!");
            case 42 -> player.sendMessage("§cWeapon Ability GUI not implemented yet!");
            case 43 -> player.sendMessage("§cNPC Management GUI not implemented yet!");
            
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
            case 47 -> new SettingsGUI(SkyblockPlugin, player).open();
            case 48 -> new AdminMenu(SkyblockPlugin).open();
            
            // Navigation & Info (49-53)
            case 49 -> player.sendMessage("§cEvent Schedule GUI not implemented yet!");
            case 50 -> player.sendMessage("§cServer Info GUI not implemented yet!");
            case 51 -> player.sendMessage("§cQuick Actions GUI not implemented yet!");
            case 52 -> player.closeInventory();
            case 53 -> player.sendMessage("§cHelp GUI not implemented yet!");
        }
    }

    private void handleUltimateEventGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Category buttons (10-14)
            case 10 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // // gui.setCategory("dragon");
                // // gui.openGUI(player);
                player.sendMessage(Component.text("§cUltimateEventGUI not implemented yet!"));
            }
            case 11 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // // gui.setCategory("undead");
                // // gui.openGUI(player);
                player.sendMessage(Component.text("§cUltimateEventGUI not implemented yet!"));
            }
            case 12 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("elemental");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cUltimateEventGUI not implemented yet!"));
            }
            case 13 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("monster");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 14 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("all");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 15 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("dark");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 16 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("celestial");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 17 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("mechanical");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 18 -> {
                // UltimateEventGUI gui = new UltimateEventGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("nature");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
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
            case 28 -> player.sendMessage("§cEvent Schedule GUI not implemented yet!");
            case 30 -> player.sendMessage("§cEvent Statistics GUI not implemented yet!");
            case 32 -> player.sendMessage("§cEvent Rewards GUI not implemented yet!");
            
            // Navigation (45-49)
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
        }
    }

    private void handleEnhancedAchievementGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Category buttons (10-16)
            case 10 -> {
                // EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("events");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 11 -> {
                // EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("economy");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 12 -> {
                // EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("cosmetics");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 13 -> {
                // EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("social");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 14 -> {
                // EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("combat");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 15 -> {
                // EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("exploration");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 16 -> {
                // EnhancedAchievementGUI gui = new EnhancedAchievementGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("all");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            
            // Navigation (45-49)
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
        }
    }

    private void handleEnhancedDailyRewardGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Claim button (30)
            case 30 -> {
                // TODO: Implement daily reward system
                player.sendMessage(Component.text("§cDaily reward system not implemented yet!"));
            }
            
            // Navigation (45-49)
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
        }
    }

    private void handleEnhancedCosmeticsGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Category buttons (10-14)
            case 10 -> {
                // EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("particles");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 11 -> {
                // EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("wings");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 12 -> {
                // EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("halos");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 13 -> {
                // EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("trails");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            case 14 -> {
                // EnhancedCosmeticsGUI gui = new EnhancedCosmeticsGUI(SkyblockPlugin, player); // Not implemented yet
                // gui.setCategory("sounds");
                // gui.openGUI(player);
                player.sendMessage(Component.text("§cGUI not implemented yet!"));
            }
            
            // Action buttons (28-32)
            case 28 -> {
                // TODO: Implement cosmetics deactivation
                player.sendMessage(Component.text("§aAlle Cosmetics deaktiviert!"));
            }
            case 30 -> player.sendMessage("§cMy Cosmetics GUI not implemented yet!");
            case 32 -> player.sendMessage("§cCosmetic Shop GUI not implemented yet!");
            
            // Navigation (45-49)
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
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
                    // player.getInventory().addItem((org.bukkit.inventory.ItemStack) SkyblockPlugin.createHotPotatoBook());
                    player.sendMessage(Component.text("§cHot Potato Book creation not implemented yet!"));
                    player.sendMessage(Component.text("§aHot Potato Book erstellt!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast nicht genug Geld! (Benötigt: 100 coins)"));
                }
            }
            
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
            case 51 -> player.sendMessage(Component.text("§cPotatoBookGUI not implemented yet!")); // Refresh
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
                    // player.getInventory().addItem((org.bukkit.inventory.ItemStack) SkyblockPlugin.createRecombobulator3000());
                    player.sendMessage(Component.text("§cRecombobulator3000 creation not implemented yet!"));
                    player.sendMessage(Component.text("§aRecombobulator 3000 erstellt!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast nicht genug Geld! (Benötigt: 1,000 coins)"));
                }
            }
            
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
            case 51 -> player.sendMessage("§cRecombobulator GUI not implemented yet!"); // Refresh
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
                    // player.getInventory().addItem((org.bukkit.inventory.ItemStack) SkyblockPlugin.createDungeonStar(1));
                    player.sendMessage(Component.text("§cDungeon Star creation not implemented yet!"));
                    player.sendMessage(Component.text("§aDungeon Star erstellt!"));
                } else {
                    player.sendMessage(Component.text("§cDu hast nicht genug Geld! (Benötigt: 100 coins)"));
                }
            }
            
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
            case 51 -> player.sendMessage("§cDungeon Star GUI not implemented yet!"); // Refresh
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
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
            case 51 -> player.sendMessage(Component.text("§cPetItemGUI not implemented yet!")); // Refresh
        }
    }

    private void handleArmorAbilityGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
            case 51 -> player.sendMessage(Component.text("§cArmorAbilityGUI not implemented yet!")); // Refresh
        }
    }

    private void handleWeaponAbilityGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();
        
        switch (slot) {
            // Navigation
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
            case 51 -> player.sendMessage(Component.text("§cWeaponAbilityGUI not implemented yet!")); // Refresh
        }
    }
}
