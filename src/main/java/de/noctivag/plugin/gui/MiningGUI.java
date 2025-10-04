package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.skyblock.MiningAreaSystem;
import de.noctivag.plugin.skyblock.SkyblockManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MiningGUI extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    
    public MiningGUI(Plugin plugin, Player player) {
        super(54, Component.text("§6§lMining Areas"));
        this.plugin = plugin;
        this.player = player;
        initializeItems();
    }
    
    private void initializeItems() {
        // Fill borders with glass panes
        for (int i = 0; i < 9; i++) setItem(i, Material.ORANGE_STAINED_GLASS_PANE, " ");
        for (int i = 45; i < 54; i++) setItem(i, Material.ORANGE_STAINED_GLASS_PANE, " ");
        setItem(9, Material.ORANGE_STAINED_GLASS_PANE, " ");
        setItem(17, Material.ORANGE_STAINED_GLASS_PANE, " ");
        setItem(18, Material.ORANGE_STAINED_GLASS_PANE, " ");
        setItem(26, Material.ORANGE_STAINED_GLASS_PANE, " ");
        setItem(27, Material.ORANGE_STAINED_GLASS_PANE, " ");
        setItem(35, Material.ORANGE_STAINED_GLASS_PANE, " ");
        setItem(36, Material.ORANGE_STAINED_GLASS_PANE, " ");
        setItem(44, Material.ORANGE_STAINED_GLASS_PANE, " ");
        
        // Header
        setItem(4, Material.DIAMOND_PICKAXE, "§6§lMining Areas", 
            Arrays.asList("§7Choose a mining area to explore", "§7and start your mining adventure!"));
        
        // Mining Areas
        int slot = 10;
        Map<String, MiningAreaSystem.MiningArea> areas = plugin.getMiningAreaSystem().getAllMiningAreas();
        
        for (Map.Entry<String, MiningAreaSystem.MiningArea> entry : areas.entrySet()) {
            MiningAreaSystem.MiningArea area = entry.getValue();
            String areaId = entry.getKey();
            
            // Get player's mining level
            int playerLevel = plugin.getSkyblockManager().getSkills(player.getUniqueId())
                .getLevel(SkyblockManager.SkyblockSkill.MINING);
            
            // Get required level for this area (highest required level)
            int requiredLevel = getRequiredLevelForArea(area);
            
            // Determine if player can access this area
            boolean canAccess = playerLevel >= requiredLevel;
            
            // Get area icon
            Material icon = getAreaIcon(areaId);
            
            // Create lore
            List<String> lore = new ArrayList<>();
            lore.add("§7" + getAreaDescription(areaId));
            lore.add("");
            lore.add("§7Required Level: §e" + requiredLevel);
            lore.add("§7Your Level: §e" + playerLevel);
            lore.add("");
            
            if (canAccess) {
                lore.add("§a§l✓ ACCESSIBLE");
                lore.add("§7Click to teleport!");
            } else {
                lore.add("§c§l✗ LOCKED");
                lore.add("§7Level up to unlock!");
            }
            
            // Add block information
            lore.add("");
            lore.add("§7Available Blocks:");
            Map<Material, Integer> requiredLevels = area.getRequiredLevels();
            int count = 0;
            for (Map.Entry<Material, Integer> blockEntry : requiredLevels.entrySet()) {
                if (count >= 3) break; // Limit to 3 blocks
                Material block = blockEntry.getKey();
                int level = blockEntry.getValue();
                lore.add("§7• " + block.name() + " §e(Level " + level + ")");
                count++;
            }
            if (requiredLevels.size() > 3) {
                lore.add("§7• ... and " + (requiredLevels.size() - 3) + " more");
            }
            
            setItem(slot, icon, "§6§l" + area.getName(), lore);
            slot++;
        }
        
        // Player Stats
        setItem(28, Material.EXPERIENCE_BOTTLE, "§b§lYour Mining Stats",
            Arrays.asList("§7Mining Level: §e" + plugin.getSkyblockManager().getSkills(player.getUniqueId())
                .getLevel(SkyblockManager.SkyblockSkill.MINING),
                "§7Mining XP: §e" + plugin.getSkyblockManager().getSkills(player.getUniqueId())
                .getXP(SkyblockManager.SkyblockSkill.MINING),
                "§7XP to Next Level: §e" + plugin.getSkyblockManager().getSkills(player.getUniqueId())
                .getXPToNextLevel(SkyblockManager.SkyblockSkill.MINING),
                "",
                "§7Click to view detailed stats!"));
        
        // Current Area
        MiningAreaSystem.MiningArea currentArea = plugin.getMiningAreaSystem().getPlayerCurrentArea(player.getUniqueId());
        if (currentArea != null) {
            setItem(30, Material.COMPASS, "§a§lCurrent Area",
                Arrays.asList("§7Area: §e" + currentArea.getName(),
                    "§7Description: §7" + getAreaDescription(currentArea.getId()),
                    "",
                    "§7You are currently in this area!"));
        } else {
            setItem(30, Material.COMPASS, "§c§lNo Active Area",
                Arrays.asList("§7You are not in any mining area",
                    "",
                    "§7Visit a mining area to start mining!"));
        }
        
        // Mining Tips
        setItem(32, Material.BOOK, "§e§lMining Tips",
            Arrays.asList("§7• Higher level areas give more XP",
                "§7• Different areas have different blocks",
                "§7• Blocks regenerate after a certain time",
                "§7• Creative mode bypasses all restrictions",
                "§7• Use better tools for faster mining",
                "",
                "§7Happy mining!"));
        
        // Back Button
        setItem(49, Material.ARROW, "§7Zurück", Arrays.asList("§7Zum Hauptmenü"));
    }
    
    private int getRequiredLevelForArea(MiningAreaSystem.MiningArea area) {
        // Get the highest required level for any block in this area
        int maxLevel = 0;
        for (int level : area.getRequiredLevels().values()) {
            if (level > maxLevel) {
                maxLevel = level;
            }
        }
        return maxLevel;
    }
    
    private Material getAreaIcon(String areaId) {
        return switch (areaId) {
            case "deep_caverns" -> Material.COAL_ORE;
            case "dwarven_mines" -> Material.IRON_ORE;
            case "crystal_hollows" -> Material.END_CRYSTAL;
            case "the_end" -> Material.ENDER_PEARL;
            case "the_nether" -> Material.ANCIENT_DEBRIS;
            case "overworld" -> Material.STONE;
            default -> Material.COBBLESTONE;
        };
    }
    
    private String getAreaDescription(String areaId) {
        return switch (areaId) {
            case "deep_caverns" -> "Beginner mining area with basic ores and stones";
            case "dwarven_mines" -> "Intermediate mining area with rare ores and materials";
            case "crystal_hollows" -> "Advanced mining area with end materials and crystals";
            case "the_end" -> "Expert mining area with end game materials and rare blocks";
            case "the_nether" -> "Expert mining area with nether materials and ancient debris";
            case "overworld" -> "Basic mining area for beginners with common blocks";
            default -> "Unknown mining area";
        };
    }
}
