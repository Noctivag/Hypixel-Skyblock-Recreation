package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.slayers.CompleteSlayerSystem;
import de.noctivag.skyblock.dungeons.CompleteDungeonSystem;
import de.noctivag.skyblock.combat.CompleteCombatSystem;
import de.noctivag.skyblock.slayers.SlayerType;
import de.noctivag.skyblock.dungeons.CompleteDungeonSystem.DungeonFloor;
import de.noctivag.skyblock.dungeons.CompleteDungeonSystem.DungeonClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Combat & Adventure GUI System - Centralized GUI for all combat and adventure features
 * 
 * Features:
 * - Slayer quest management
 * - Dungeon party and instance management
 * - Combat statistics and abilities
 * - Integrated navigation between systems
 * - Real-time status updates
 */
public class CombatAdventureGUI implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final CompleteSlayerSystem slayerSystem;
    private final CompleteDungeonSystem dungeonSystem;
    private final CompleteCombatSystem combatSystem;
    
    public CombatAdventureGUI(SkyblockPlugin SkyblockPlugin, CompleteSlayerSystem slayerSystem, 
                            CompleteDungeonSystem dungeonSystem, CompleteCombatSystem combatSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.slayerSystem = slayerSystem;
        this.dungeonSystem = dungeonSystem;
        this.combatSystem = combatSystem;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    public void openMainGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§5§lCombat & Adventure"));
        
        // Main system buttons
        addGUIItem(gui, 10, Material.ZOMBIE_HEAD, "§2§lSlayer System", 
            "§7Defeat powerful bosses\n§7and earn rewards!");
        addGUIItem(gui, 12, Material.STONE_BRICKS, "§5§lDungeon System", 
            "§7Explore dangerous dungeons\n§7with friends!");
        addGUIItem(gui, 14, Material.DIAMOND_SWORD, "§c§lCombat System", 
            "§7Master the art of combat\n§7and become legendary!");
        
        // Quick stats
        addGUIItem(gui, 19, Material.EXPERIENCE_BOTTLE, "§6§lMy Progress", 
            "§7View your overall progress\n§7across all systems");
        addGUIItem(gui, 21, Material.CHEST, "§e§lRewards", 
            "§7Claim your earned rewards\n§7and view collections");
        addGUIItem(gui, 23, Material.EMERALD, "§a§lShop", 
            "§7Buy combat and adventure\n§7items and upgrades");
        
        // Active activities
        addGUIItem(gui, 28, Material.CLOCK, "§b§lActive Activities", 
            "§7View your current\n§7slayer quests and dungeon runs");
        addGUIItem(gui, 30, Material.PLAYER_HEAD, "§d§lParty Management", 
            "§7Manage your dungeon parties\n§7and invite friends");
        addGUIItem(gui, 32, Material.PLAYER_HEAD, "§6§lLeaderboards", 
            "§7View rankings and\n§7compete with others");
        
        // Navigation
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close this menu");
        
        player.openInventory(gui);
    }
    
    public void openSlayerGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§2§lSlayer System"));
        
        // Slayer types
        int slot = 10;
        for (SlayerType slayerType : SlayerType.values()) {
            addGUIItem(gui, slot, getSlayerIcon(slayerType), 
                slayerType.getDisplayName() + " Slayer", 
                "§7Defeat " + slayerType.getDisplayName() + " bosses\n§7and progress through tiers!");
            slot++;
        }
        
        // Active quest info
        if (slayerSystem.hasActiveQuest(player.getUniqueId())) {
            var activeQuest = slayerSystem.getActiveQuest(player.getUniqueId());
            addGUIItem(gui, 22, Material.CLOCK, "§a§lActive Quest", 
                "§7Type: §e" + activeQuest.getSlayerType().getDisplayName() + 
                "\n§7Tier: §e" + activeQuest.getTier() + 
                "\n§7Time Left: §e" + formatTime(activeQuest.getTimeRemaining()));
        } else {
            addGUIItem(gui, 22, Material.BARRIER, "§c§lNo Active Quest", 
                "§7You don't have an active\n§7slayer quest.");
        }
        
        // Quick actions
        addGUIItem(gui, 28, Material.DIAMOND_SWORD, "§c§lStart Tier 1 Quest", 
            "§7Quick start a tier 1\n§7slayer quest");
        addGUIItem(gui, 29, Material.GOLDEN_SWORD, "§e§lStart Tier 2 Quest", 
            "§7Quick start a tier 2\n§7slayer quest");
        addGUIItem(gui, 30, Material.IRON_SWORD, "§7§lStart Tier 3 Quest", 
            "§7Quick start a tier 3\n§7slayer quest");
        
        // Management
        addGUIItem(gui, 37, Material.BOOK, "§6§lMy Progress", "§7View slayer progress");
        addGUIItem(gui, 38, Material.CHEST, "§e§lRewards", "§7View slayer rewards");
        addGUIItem(gui, 39, Material.EMERALD, "§a§lShop", "§7Buy slayer items");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Return to main menu");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close this menu");
        
        player.openInventory(gui);
    }
    
    public void openDungeonGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§5§lDungeon System"));
        
        // Dungeon floors
        addGUIItem(gui, 10, Material.STONE_BRICKS, "§a§lFloor 1 - Entrance", 
            "§7The entrance to the Catacombs\n§7Difficulty: Easy\n§7Required Level: 1");
        addGUIItem(gui, 11, Material.STONE_BRICKS, "§e§lFloor 2", 
            "§7The second floor\n§7Difficulty: Easy\n§7Required Level: 3");
        addGUIItem(gui, 12, Material.STONE_BRICKS, "§6§lFloor 3", 
            "§7The third floor\n§7Difficulty: Medium\n§7Required Level: 5");
        addGUIItem(gui, 13, Material.STONE_BRICKS, "§c§lFloor 4", 
            "§7The fourth floor\n§7Difficulty: Medium\n§7Required Level: 8");
        addGUIItem(gui, 14, Material.STONE_BRICKS, "§5§lFloor 5", 
            "§7The fifth floor\n§7Difficulty: Hard\n§7Required Level: 12");
        addGUIItem(gui, 15, Material.STONE_BRICKS, "§d§lFloor 6", 
            "§7The sixth floor\n§7Difficulty: Hard\n§7Required Level: 16");
        addGUIItem(gui, 16, Material.STONE_BRICKS, "§4§lFloor 7", 
            "§7The seventh floor\n§7Difficulty: Master\n§7Required Level: 20");
        
        // Active instance info
        if (dungeonSystem.hasActiveInstance(player.getUniqueId())) {
            var activeInstance = dungeonSystem.getActiveInstance(player.getUniqueId());
            addGUIItem(gui, 22, Material.CLOCK, "§a§lActive Dungeon", 
                "§7Floor: §e" + activeInstance.getFloor().getDisplayName() + 
                "\n§7Time: §e" + formatTime(java.lang.System.currentTimeMillis() - activeInstance.getStartTime()));
        } else {
            addGUIItem(gui, 22, Material.BARRIER, "§c§lNo Active Dungeon", 
                "§7You are not currently\n§7in a dungeon run.");
        }
        
        // Party management
        var activeParty = dungeonSystem.getActiveParty(player.getUniqueId());
        if (activeParty != null) {
            addGUIItem(gui, 25, Material.PLAYER_HEAD, "§b§lParty: " + activeParty.getMembers().size() + "/5", 
                "§7Leader: §e" + Bukkit.getPlayer(activeParty.getLeaderId()).getName() + 
                "\n§7Members: §a" + activeParty.getMembers().size());
        } else {
            addGUIItem(gui, 25, Material.BARRIER, "§c§lNo Party", 
                "§7You are not in a party.\n§7Create or join one!");
        }
        
        // Class selection
        addGUIItem(gui, 28, Material.DIAMOND_SWORD, "§c§lBerserker", 
            "§7High damage, low defense\n§7Damage: +50%\n§7Defense: -50%");
        addGUIItem(gui, 29, Material.BOW, "§a§lArcher", 
            "§7Ranged damage, medium defense\n§7Damage: +20%\n§7Defense: -20%");
        addGUIItem(gui, 30, Material.STICK, "§b§lMage", 
            "§7Magic damage, low defense\n§7Damage: +30%\n§7Defense: -40%");
        addGUIItem(gui, 31, Material.SHIELD, "§e§lTank", 
            "§7High defense, low damage\n§7Damage: -30%\n§7Defense: +100%");
        addGUIItem(gui, 32, Material.GOLDEN_APPLE, "§d§lHealer", 
            "§7Healing support, medium defense\n§7Damage: -20%\n§7Defense: +20%");
        
        // Management
        addGUIItem(gui, 37, Material.BOOK, "§6§lMy Progress", "§7View dungeon progress");
        addGUIItem(gui, 38, Material.CHEST, "§e§lRewards", "§7View dungeon rewards");
        addGUIItem(gui, 39, Material.EMERALD, "§a§lShop", "§7Buy dungeon items");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Return to main menu");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close this menu");
        
        player.openInventory(gui);
    }
    
    public void openCombatGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§lCombat System"));
        
        // Combat stats
        var stats = combatSystem.getPlayerCombatStats(player.getUniqueId());
        addGUIItem(gui, 4, Material.DIAMOND_SWORD, "§6§lCombat Level " + stats.getCombatLevel(), 
            "§7XP: §a" + stats.getCombatXP() + "/" + getRequiredXP(stats.getCombatLevel() + 1) +
            "\n§7Kills: §c" + stats.getTotalKills() +
            "\n§7Deaths: §4" + stats.getTotalDeaths() +
            "\n§7K/D Ratio: §e" + String.format("%.2f", stats.getKillDeathRatio()) +
            "\n§7Critical Rate: §b" + String.format("%.1f", stats.getCriticalHitRate()) + "%");
        
        // Combat abilities
        addGUIItem(gui, 10, Material.BLAZE_POWDER, "§c§lBerserker Rage", 
            "§7Increases damage by 50%\n§7Duration: 10 seconds\n§7Cooldown: 60 seconds");
        addGUIItem(gui, 11, Material.SHIELD, "§e§lDefensive Stance", 
            "§7Reduces damage taken by 30%\n§7Duration: 15 seconds\n§7Cooldown: 45 seconds");
        addGUIItem(gui, 12, Material.ARROW, "§a§lPrecision Shot", 
            "§7Next attack deals 200% damage\n§7Cooldown: 30 seconds");
        addGUIItem(gui, 13, Material.GOLDEN_APPLE, "§d§lBattle Heal", 
            "§7Instantly heals 50% of max health\n§7Cooldown: 120 seconds");
        addGUIItem(gui, 14, Material.ENDER_PEARL, "§5§lShadow Strike", 
            "§7Teleport behind target and attack\n§7Cooldown: 90 seconds");
        
        // Combat settings
        addGUIItem(gui, 19, Material.REDSTONE, "§c§lDamage Numbers", 
            "§7Toggle damage number display\n§7Current: " + (stats.isCombatMode() ? "§aON" : "§cOFF"));
        addGUIItem(gui, 20, Material.GOLD_INGOT, "§6§lAuto Attack", 
            "§7Toggle auto attack mode\n§7Current: " + (stats.isPvpEnabled() ? "§aON" : "§cOFF"));
        addGUIItem(gui, 21, Material.EMERALD, "§a§lPvP Mode", 
            "§7Toggle PvP combat\n§7Current: " + (stats.isPvpEnabled() ? "§aON" : "§cOFF"));
        addGUIItem(gui, 22, Material.DIAMOND, "§b§lCombat Mode", 
            "§7Toggle combat mode\n§7Current: " + (stats.isCombatMode() ? "§aON" : "§cOFF"));
        
        // Weapon categories
        addGUIItem(gui, 28, Material.DIAMOND_SWORD, "§c§lSwords", "§7View sword collection");
        addGUIItem(gui, 29, Material.BOW, "§a§lBows", "§7View bow collection");
        addGUIItem(gui, 30, Material.STICK, "§b§lWands", "§7View wand collection");
        addGUIItem(gui, 31, Material.SHIELD, "§e§lShields", "§7View shield collection");
        
        // Armor categories
        addGUIItem(gui, 37, Material.DIAMOND_HELMET, "§b§lHelmets", "§7View helmet collection");
        addGUIItem(gui, 38, Material.DIAMOND_CHESTPLATE, "§b§lChestplates", "§7View chestplate collection");
        addGUIItem(gui, 39, Material.DIAMOND_LEGGINGS, "§b§lLeggings", "§7View leggings collection");
        addGUIItem(gui, 40, Material.DIAMOND_BOOTS, "§b§lBoots", "§7View boots collection");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Return to main menu");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close this menu");
        
        player.openInventory(gui);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        ItemStack item = event.getCurrentItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        String itemName = LegacyComponentSerializer.legacySection().serialize(item.getItemMeta().displayName());
        
        event.setCancelled(true);
        
        // Main GUI handling
        if (title.contains("Combat & Adventure")) {
            handleMainGUIClick(player, itemName);
        }
        // Slayer GUI handling
        else if (title.contains("Slayer System")) {
            handleSlayerGUIClick(player, itemName);
        }
        // Dungeon GUI handling
        else if (title.contains("Dungeon System")) {
            handleDungeonGUIClick(player, itemName);
        }
        // Combat GUI handling
        else if (title.contains("Combat System")) {
            handleCombatGUIClick(player, itemName);
        }
    }
    
    private void handleMainGUIClick(Player player, String itemName) {
        if (itemName.contains("Slayer System")) {
            openSlayerGUI(player);
        } else if (itemName.contains("Dungeon System")) {
            openDungeonGUI(player);
        } else if (itemName.contains("Combat System")) {
            openCombatGUI(player);
        } else if (itemName.contains("Close")) {
            player.closeInventory();
        }
    }
    
    private void handleSlayerGUIClick(Player player, String itemName) {
        if (itemName.contains("Zombie Slayer")) {
            slayerSystem.startSlayerQuest(player, SlayerType.ZOMBIE, 1);
        } else if (itemName.contains("Spider Slayer")) {
            slayerSystem.startSlayerQuest(player, SlayerType.SPIDER, 1);
        } else if (itemName.contains("Wolf Slayer")) {
            slayerSystem.startSlayerQuest(player, SlayerType.WOLF, 1);
        } else if (itemName.contains("Enderman Slayer")) {
            slayerSystem.startSlayerQuest(player, SlayerType.ENDERMAN, 1);
        } else if (itemName.contains("Blaze Slayer")) {
            slayerSystem.startSlayerQuest(player, SlayerType.BLAZE, 1);
        } else if (itemName.contains("Start Tier 1 Quest")) {
            // Quick start logic
            player.sendMessage(Component.text("§aSelect a slayer type from above!"));
        } else if (itemName.contains("Back")) {
            openMainGUI(player);
        } else if (itemName.contains("Close")) {
            player.closeInventory();
        }
    }
    
    private void handleDungeonGUIClick(Player player, String itemName) {
        if (itemName.contains("Floor 1")) {
            dungeonSystem.startDungeonRun(player, DungeonFloor.F1);
        } else if (itemName.contains("Floor 2")) {
            dungeonSystem.startDungeonRun(player, DungeonFloor.F2);
        } else if (itemName.contains("Floor 3")) {
            dungeonSystem.startDungeonRun(player, DungeonFloor.F3);
        } else if (itemName.contains("Floor 4")) {
            dungeonSystem.startDungeonRun(player, DungeonFloor.F4);
        } else if (itemName.contains("Floor 5")) {
            dungeonSystem.startDungeonRun(player, DungeonFloor.F5);
        } else if (itemName.contains("Floor 6")) {
            dungeonSystem.startDungeonRun(player, DungeonFloor.F6);
        } else if (itemName.contains("Floor 7")) {
            dungeonSystem.startDungeonRun(player, DungeonFloor.F7);
        } else if (itemName.contains("Berserker")) {
            dungeonSystem.getPlayerDungeonData(player.getUniqueId()).setSelectedClass(DungeonClass.BERSERKER);
            player.sendMessage(Component.text("§a§lCLASS SELECTED: Berserker"));
        } else if (itemName.contains("Archer")) {
            dungeonSystem.getPlayerDungeonData(player.getUniqueId()).setSelectedClass(DungeonClass.ARCHER);
            player.sendMessage(Component.text("§a§lCLASS SELECTED: Archer"));
        } else if (itemName.contains("Mage")) {
            dungeonSystem.getPlayerDungeonData(player.getUniqueId()).setSelectedClass(DungeonClass.MAGE);
            player.sendMessage(Component.text("§a§lCLASS SELECTED: Mage"));
        } else if (itemName.contains("Tank")) {
            dungeonSystem.getPlayerDungeonData(player.getUniqueId()).setSelectedClass(DungeonClass.TANK);
            player.sendMessage(Component.text("§a§lCLASS SELECTED: Tank"));
        } else if (itemName.contains("Healer")) {
            dungeonSystem.getPlayerDungeonData(player.getUniqueId()).setSelectedClass(DungeonClass.HEALER);
            player.sendMessage(Component.text("§a§lCLASS SELECTED: Healer"));
        } else if (itemName.contains("Back")) {
            openMainGUI(player);
        } else if (itemName.contains("Close")) {
            player.closeInventory();
        }
    }
    
    private void handleCombatGUIClick(Player player, String itemName) {
        if (itemName.contains("Berserker Rage")) {
            // Activate berserker rage ability
            player.sendMessage(Component.text("§c§lBERSERKER RAGE ACTIVATED!"));
            player.sendMessage(Component.text("§7+50% damage for 10 seconds!"));
        } else if (itemName.contains("Defensive Stance")) {
            // Activate defensive stance ability
            player.sendMessage(Component.text("§e§lDEFENSIVE STANCE ACTIVATED!"));
            player.sendMessage(Component.text("§7-30% damage taken for 15 seconds!"));
        } else if (itemName.contains("Precision Shot")) {
            // Activate precision shot ability
            player.sendMessage(Component.text("§a§lPRECISION SHOT READY!"));
            player.sendMessage(Component.text("§7Next attack deals 200% damage!"));
        } else if (itemName.contains("Battle Heal")) {
            // Activate battle heal ability
            player.sendMessage(Component.text("§d§lBATTLE HEAL ACTIVATED!"));
            player.sendMessage(Component.text("§7Healed 50% of max health!"));
        } else if (itemName.contains("Shadow Strike")) {
            // Activate shadow strike ability
            player.sendMessage(Component.text("§5§lSHADOW STRIKE READY!"));
            player.sendMessage(Component.text("§7Next attack teleports behind target!"));
        } else if (itemName.contains("Back")) {
            openMainGUI(player);
        } else if (itemName.contains("Close")) {
            player.closeInventory();
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private Material getSlayerIcon(SlayerType slayerType) {
        return switch (slayerType) {
            case ZOMBIE -> Material.ZOMBIE_HEAD;
            case SPIDER -> Material.SPIDER_EYE;
            case WOLF -> Material.WOLF_SPAWN_EGG;
            case ENDERMAN -> Material.ENDER_PEARL;
            case BLAZE -> Material.BLAZE_POWDER;
        };
    }
    
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        return String.format("%d:%02d", minutes, seconds);
    }
    
    private int getRequiredXP(int level) {
        return level * 1000;
    }
}
