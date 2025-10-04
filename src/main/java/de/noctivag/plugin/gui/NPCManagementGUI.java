package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.npcs.AdvancedNPCSystem;
import de.noctivag.plugin.npcs.HypixelStyleNPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NPCManagementGUI extends CustomGUI {
    private final Plugin plugin;
    private final Player player;
    private final AdvancedNPCSystem npcSystem;

    public NPCManagementGUI(Plugin plugin, Player player) {
        super(54, Component.text("§6§lNPC Management").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        this.npcSystem = plugin.getAdvancedNPCSystem();
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();
        
        // NPC List
        setupNPCList();
        
        // Management Tools
        setupManagementTools();
        
        // Statistics
        setupStatistics();
        
        // Navigation
        setupNavigation();
        
        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta meta = header.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lNPC Management").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
            
            Map<String, HypixelStyleNPC> activeNPCs = npcSystem.getActiveNPCs();
            List<Component> lore = Arrays.asList(
                Component.text("§7Manage all NPCs on the server"),
                Component.text("§7Total NPCs: §a" + activeNPCs.size()),
                Component.text("§7NPC Types: §a" + AdvancedNPCSystem.NPCType.values().length),
                Component.text(""),
                Component.text("§eClick on NPCs to manage them!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupNPCList() {
        Map<String, HypixelStyleNPC> activeNPCs = npcSystem.getActiveNPCs();
        int slot = 10;
        
        for (Map.Entry<String, HypixelStyleNPC> entry : activeNPCs.entrySet()) {
            if (slot >= 35) break; // Don't overflow
            
            HypixelStyleNPC npc = entry.getValue();
            AdvancedNPCSystem.NPCType type = npc.getType();
            
            setItem(slot, type.getIcon(), "§6§l" + npc.getDisplayName(), 
                "§7Type: " + type.getDisplayName(),
                "§7ID: §e" + npc.getNpcId(),
                "§7World: §e" + npc.getLocation().getWorld().getName(),
                "§7X: §e" + npc.getLocation().getBlockX(),
                "§7Y: §e" + npc.getLocation().getBlockY(),
                "§7Z: §e" + npc.getLocation().getBlockZ(),
                "",
                "§eClick to manage!");
            
            slot++;
        }
    }

    private void setupManagementTools() {
        // Create NPC Tool
        setItem(37, Material.STICK, "§a§l➕ Create NPC Tool", 
            "§7Get the NPC creation tool",
            "§7• Right-click to place NPCs",
            "§7• Left-click to manage NPCs",
            "",
            "§eClick to get tool!");

        // Remove All NPCs
        setItem(38, Material.BARRIER, "§c§l🗑️ Remove All NPCs", 
            "§7Remove all NPCs from server",
            "§7• WARNING: This cannot be undone!",
            "§7• Removes all NPCs permanently",
            "",
            "§eClick to remove all!");

        // Reload NPCs
        setItem(39, Material.EMERALD, "§a§l🔄 Reload NPCs", 
            "§7Reload all NPCs from database",
            "§7• Refreshes NPC data",
            "§7• Fixes any issues",
            "",
            "§eClick to reload!");

        // Export NPCs
        setItem(40, Material.PAPER, "§e§l📄 Export NPCs", 
            "§7Export NPC data to file",
            "§7• Backup NPC configuration",
            "§7• Save to file",
            "",
            "§eClick to export!");

        // Import NPCs
        setItem(41, Material.BOOK, "§b§l📥 Import NPCs", 
            "§7Import NPC data from file",
            "§7• Restore NPC configuration",
            "§7• Load from file",
            "",
            "§eClick to import!");
    }

    private void setupStatistics() {
        Map<String, HypixelStyleNPC> activeNPCs = npcSystem.getActiveNPCs();
        AdvancedNPCSystem.PlayerNPCManager playerManager = npcSystem.getPlayerNPCManager(player.getUniqueId());
        
        // Player Statistics
        setItem(28, Material.GOLD_INGOT, "§6§l📈 Your Statistics", 
            "§7Your NPC management stats:",
            "§7• NPCs created: §a" + playerManager.getNPCsCreated(),
            "§7• NPCs managed: §a" + playerManager.getNPCsManaged(),
            "§7• Total interactions: §a" + (playerManager.getNPCsCreated() + playerManager.getNPCsManaged()),
            "",
            "§eClick to view detailed stats!");

        // Global Statistics
        setItem(29, Material.DIAMOND, "§b§l🌍 Global Statistics", 
            "§7Global NPC statistics:",
            "§7• Total NPCs: §a" + activeNPCs.size(),
            "§7• Most common type: §aShop",
            "§7• Average per world: §a" + (activeNPCs.size() / 3),
            "",
            "§eClick to view global stats!");

        // NPC Type Breakdown
        setItem(30, Material.PAPER, "§e§l📊 NPC Type Breakdown", 
            "§7NPC types on server:",
            "§7• Shop NPCs: §a" + getNPCTypeCount(activeNPCs, AdvancedNPCSystem.NPCType.SHOP),
            "§7• Quest NPCs: §a" + getNPCTypeCount(activeNPCs, AdvancedNPCSystem.NPCType.QUEST),
            "§7• Info NPCs: §a" + getNPCTypeCount(activeNPCs, AdvancedNPCSystem.NPCType.INFO),
            "§7• Warp NPCs: §a" + getNPCTypeCount(activeNPCs, AdvancedNPCSystem.NPCType.WARP),
            "§7• Bank NPCs: §a" + getNPCTypeCount(activeNPCs, AdvancedNPCSystem.NPCType.BANK),
            "",
            "§eClick to view full breakdown!");
    }

    private int getNPCTypeCount(Map<String, HypixelStyleNPC> npcs, AdvancedNPCSystem.NPCType type) {
        return (int) npcs.values().stream().filter(npc -> npc.getType() == type).count();
    }

    private void setupNavigation() {
        // Back to Main Menu
        setItem(45, Material.ARROW, "§7§l⬅️ Back to Main Menu", 
            "§7Return to the main menu",
            "",
            "§eClick to go back!");

        // Close
        setItem(49, Material.BARRIER, "§c§l❌ Close", 
            "§7Close this menu",
            "",
            "§eClick to close!");

        // Refresh
        setItem(51, Material.EMERALD, "§a§l🔄 Refresh", 
            "§7Refresh this menu",
            "",
            "§eClick to refresh!");
    }

    private void setupDecorativeBorders() {
        // Top border
        for (int i = 0; i < 9; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        
        // Side borders
        for (int i = 9; i < 54; i += 9) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 17; i < 54; i += 9) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        
        // Bottom border
        for (int i = 45; i < 54; i++) {
            if (i != 49) { // Don't override close button
                setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
            }
        }
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
