package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.dungeons.DungeonFloor;
import de.noctivag.skyblock.dungeons.DungeonClass;
import de.noctivag.skyblock.dungeons.DungeonSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DungeonFinderGUI extends Menu {
    public DungeonFinderGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lDungeon Finder", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Get player dungeon data
        DungeonClass playerClass = plugin.getDungeonsSystem().getPlayerClass(player);
        if (playerClass == null) {
            player.sendMessage("§cError loading dungeon data!");
            return;
        }
        
        // Player Class Info
        setupClassInfo(4, playerClass);
        
        // Normal Floors
        setupFloorItem(19, DungeonFloor.F1);
        setupFloorItem(20, DungeonFloor.F2);
        setupFloorItem(21, DungeonFloor.F3);
        setupFloorItem(22, DungeonFloor.F4);
        setupFloorItem(23, DungeonFloor.F5);
        setupFloorItem(24, DungeonFloor.F6);
        setupFloorItem(25, DungeonFloor.F7);
        
        // Master Mode Floors
        setupFloorItem(28, DungeonFloor.M1);
        setupFloorItem(29, DungeonFloor.M2);
        setupFloorItem(30, DungeonFloor.M3);
        setupFloorItem(31, DungeonFloor.M4);
        setupFloorItem(32, DungeonFloor.M5);
        setupFloorItem(33, DungeonFloor.M6);
        setupFloorItem(34, DungeonFloor.M7);
        
        // Statistics
        setupStatisticsItem(40);
        
        // Close Button
        setCloseButton(49);
    }
    
    private void setupClassInfo(int slot, DungeonClass playerClass) {
        String rarity = getRarityForClass(playerClass);
        String[] lore = {
            "&7Your Dungeon Class",
            "",
            "&7" + playerClass.getDescription(),
            "",
            "&7Abilities:",
            "&a• " + playerClass.getAbilities()[0],
            "&a• " + playerClass.getAbilities()[1],
            "&a• " + playerClass.getAbilities()[2],
            "",
            "&eClick to change class"
        };
        
        setItem(slot, playerClass.getMaterial(), playerClass.getColoredDisplayName(), rarity, lore);
    }
    
    private void setupFloorItem(int slot, DungeonFloor floor) {
        int availableSessions = plugin.getDungeonsSystem().getAvailableSessions(floor).size();
        int waitingSessions = plugin.getDungeonsSystem().getSessionsByFloor(floor).stream()
                .mapToInt(session -> session.getPlayerCount())
                .sum();
        
        String rarity = getRarityForFloor(floor);
        String[] lore = {
            "&7" + floor.getDescription(),
            "",
            "&7Recommended Level: &a" + floor.getRecommendedLevel(),
            "&7Players: &a" + floor.getMinPlayers() + "-" + floor.getMaxPlayers(),
            "&7Base Reward: &e" + formatNumber(floor.getBaseReward()) + " coins",
            "",
            "&7Available Sessions: &a" + availableSessions,
            "&7Players Waiting: &a" + waitingSessions,
            "&7Difficulty: " + floor.getDifficultyColor() + floor.getDifficultyName(),
            "&7Boss: " + floor.getBossName(),
            "",
            "&eClick to find/join session"
        };
        
        setItem(slot, floor.getMaterial(), floor.getColoredDisplayName(), rarity, lore);
    }
    
    private void setupStatisticsItem(int slot) {
        var stats = plugin.getDungeonsSystem().getDungeonStatistics();
        
        String rarity = getRarityForStats(stats.getTotalSessions());
        String[] lore = {
            "&7Dungeon Statistics",
            "",
            "&7Total Sessions: &a" + stats.getTotalSessions(),
            "&7Active Sessions: &a" + stats.getActiveSessions(),
            "&7Waiting Sessions: &e" + stats.getWaitingSessions(),
            "&7In Progress: &b" + stats.getInProgressSessions(),
            "&7Total Players: &a" + stats.getTotalPlayers(),
            "",
            "&7Click to view detailed statistics",
            "&eKlicke zum Öffnen"
        };
        
        setItem(slot, Material.BOOK, "&6📊 Dungeon Statistics", rarity, lore);
    }
    
    private String getRarityForClass(DungeonClass dungeonClass) {
        switch (dungeonClass) {
            case ARCHER: return "uncommon";
            case BERSERKER: return "rare";
            case HEALER: return "epic";
            case MAGE: return "legendary";
            case TANK: return "mythic";
            default: return "common";
        }
    }
    
    private String getRarityForFloor(DungeonFloor floor) {
        if (floor.isMasterMode()) return "mythic";
        if (floor.getFloorNumber() >= 6) return "legendary";
        if (floor.getFloorNumber() >= 4) return "epic";
        if (floor.getFloorNumber() >= 2) return "rare";
        return "uncommon";
    }
    
    private String getRarityForStats(int count) {
        if (count >= 20) return "mythic";
        if (count >= 15) return "legendary";
        if (count >= 10) return "epic";
        if (count >= 5) return "rare";
        if (count >= 1) return "uncommon";
        return "common";
    }
    
    private String formatNumber(long number) {
        if (number >= 1_000_000_000_000L) {
            return String.format("%.1fT", number / 1_000_000_000_000.0);
        } else if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000L) {
            return String.format("%.1fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Get player dungeon data
        DungeonClass playerClass = plugin.getDungeonsSystem().getPlayerClass(player);
        if (playerClass == null) {
            player.sendMessage("§cError loading dungeon data!");
            return;
        }
        
        switch (slot) {
            case 4:
                showClassSelection();
                break;
            case 19: case 20: case 21: case 22: case 23: case 24: case 25:
                handleFloorClick(slot - 19 + 1);
                break;
            case 28: case 29: case 30: case 31: case 32: case 33: case 34:
                handleFloorClick(slot - 28 + 8);
                break;
            case 40:
                showDetailedStatistics();
                break;
            case 49:
                close();
                break;
        }
    }
    
    private void showClassSelection() {
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage("§6🏹 Dungeon Class Selection");
        player.sendMessage("§8§m--------------------------------");
        
        for (DungeonClass dungeonClass : DungeonClass.values()) {
            String current = (dungeonClass == plugin.getDungeonsSystem().getPlayerClass(player)) ? " §a(CURRENT)" : "";
            player.sendMessage(dungeonClass.getColoredDisplayName() + current);
            player.sendMessage("§7" + dungeonClass.getDescription());
            player.sendMessage("");
        }
        
        player.sendMessage("§7Use §e/class <class> §7to change your class");
        player.sendMessage("§8§m--------------------------------");
    }
    
    private void handleFloorClick(int floorNumber) {
        DungeonFloor floor = DungeonFloor.getByNumber(floorNumber);
        if (floor == null) {
            player.sendMessage("§cFloor not found!");
            return;
        }
        
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage(floor.getColoredDisplayName());
        player.sendMessage("§8§m--------------------------------");
        
        var availableSessions = plugin.getDungeonsSystem().getAvailableSessions(floor);
        if (availableSessions.isEmpty()) {
            player.sendMessage("§7No available sessions for this floor.");
            player.sendMessage("§7Creating new session...");
            
            // Create new session (placeholder)
            player.sendMessage("§aSession created! Waiting for players...");
        } else {
            player.sendMessage("§7Available Sessions: §a" + availableSessions.size());
            for (DungeonSession session : availableSessions) {
                player.sendMessage("§7Session: §a" + session.getPlayerCount() + "/" + session.getFloor().getMaxPlayers() + " players");
            }
            player.sendMessage("§7Joining session...");
        }
        
        player.sendMessage("§8§m--------------------------------");
    }
    
    private void showDetailedStatistics() {
        var stats = plugin.getDungeonsSystem().getDungeonStatistics();
        
        player.sendMessage("§8§m--------------------------------");
        player.sendMessage("§6📊 Detailed Dungeon Statistics");
        player.sendMessage("§8§m--------------------------------");
        
        player.sendMessage("§7Total Sessions: §a" + stats.getTotalSessions());
        player.sendMessage("§7Active Sessions: §a" + stats.getActiveSessions());
        player.sendMessage("§7Waiting Sessions: §e" + stats.getWaitingSessions());
        player.sendMessage("§7In Progress: §b" + stats.getInProgressSessions());
        player.sendMessage("§7Total Players: §a" + stats.getTotalPlayers());
        
        player.sendMessage("");
        player.sendMessage("§7Your Class: " + plugin.getDungeonsSystem().getPlayerClass(player).getColoredDisplayName());
        player.sendMessage("§7In Session: " + (plugin.getDungeonsSystem().isPlayerInSession(player) ? "§aYes" : "§cNo"));
        
        player.sendMessage("§8§m--------------------------------");
    }
}
