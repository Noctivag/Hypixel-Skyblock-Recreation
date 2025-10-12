package de.noctivag.skyblock.dungeons.gui;

import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;
import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.dungeons.DungeonManager;
import de.noctivag.skyblock.dungeons.instances.DungeonInstance;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Advanced Dungeon GUI - Advanced GUI for dungeon management
 */
public class AdvancedDungeonGUI extends CustomGUI {
    
    private final DungeonManager dungeonManager;
    private final Player player;
    
    public AdvancedDungeonGUI(Player player, DungeonManager dungeonManager) {
        super("§cAdvanced Dungeon Menu", 54);
        this.player = player;
        this.dungeonManager = dungeonManager;
    }
    
    @Override
    public void setupItems() {
        // Check if player is in a dungeon
        DungeonInstance playerInstance = dungeonManager.getPlayerDungeonInstance(player);
        
        if (playerInstance != null) {
            // Show current dungeon info
            setupCurrentDungeon(playerInstance);
        } else {
            // Show available dungeons
            setupAvailableDungeons();
        }
        
        // Add navigation items
        setupNavigation();
    }
    
    private void setupCurrentDungeon(DungeonInstance instance) {
        // Current dungeon info
        ItemStack dungeonInfoItem = new ItemStack(Material.BOOK);
        ItemMeta dungeonInfoMeta = dungeonInfoItem.getItemMeta();
        if (dungeonInfoMeta != null) {
            dungeonInfoMeta.displayName(Component.text("§cCurrent Dungeon Information"));
            dungeonInfoMeta.lore(Arrays.asList(
                "§7Type: §c" + instance.getDungeonType(),
                "§7Floor: §c" + instance.getCurrentFloor() + "/" + instance.getMaxFloors(),
                "§7Players: §c" + instance.getPlayers().size(),
                "§7Time: §c" + formatTime(instance.getElapsedTime().stream().map(Component::text).collect(Collectors.toList())),
                "§7Status: §a" + (instance.isActive() ? "Active" : "Inactive"),
                "",
                "§eClick to view details"
            ));
            dungeonInfoItem.setItemMeta(dungeonInfoMeta);
        }
        inventory.setItem(22, dungeonInfoItem);
        
        // Leave dungeon button
        ItemStack leaveItem = new ItemStack(Material.BARRIER);
        ItemMeta leaveMeta = leaveItem.getItemMeta();
        if (leaveMeta != null) {
            leaveMeta.displayName(Component.text("§cLeave Dungeon"));
            leaveMeta.lore(Arrays.asList(
                "§7Leave the current dungeon",
                "§7Warning: This will forfeit all progress!",
                "",
                "§eClick to leave"
            ).stream().map(Component::text).collect(Collectors.toList()));
            leaveItem.setItemMeta(leaveMeta);
        }
        inventory.setItem(40, leaveItem);
    }
    
    private void setupAvailableDungeons() {
        // Catacombs
        ItemStack catacombsItem = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta catacombsMeta = catacombsItem.getItemMeta();
        if (catacombsMeta != null) {
            catacombsMeta.displayName(Component.text("§cCatacombs"));
            catacombsMeta.lore(Arrays.asList(
                "§7Undead dungeon",
                "§7Floors: 5",
                "§7Difficulty: Medium",
                "§7Recommended Level: 20+",
                "",
                "§eClick to enter"
            ).stream().map(Component::text).collect(Collectors.toList()));
            catacombsItem.setItemMeta(catacombsMeta);
        }
        inventory.setItem(20, catacombsItem);
        
        // M7 (placeholder)
        ItemStack m7Item = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta m7Meta = m7Item.getItemMeta();
        if (m7Meta != null) {
            m7Meta.displayName(Component.text("§cM7 (Coming Soon))");
            m7Meta.lore(Arrays.asList(
                "§7Master Mode Floor 7",
                "§7Floors: 7",
                "§7Difficulty: Extreme",
                "§7Recommended Level: 50+",
                "",
                "§cComing Soon"
            ).stream().map(Component::text).collect(Collectors.toList()));
            m7Item.setItemMeta(m7Meta);
        }
        inventory.setItem(22, m7Item);
    }
    
    private void setupNavigation() {
        // Close button
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        if (closeMeta != null) {
            closeMeta.displayName(Component.text("§cClose"));
            closeItem.setItemMeta(closeMeta);
        }
        inventory.setItem(49, closeItem);
    }
    
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return String.format("%d:%02d", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
}

