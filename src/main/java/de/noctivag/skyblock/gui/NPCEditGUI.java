package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.npcs.AdvancedNPCSystem;
import de.noctivag.skyblock.npcs.HypixelStyleNPC;
import de.noctivag.skyblock.npcs.GameNPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class NPCEditGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;
    // Internal view that unifies GameNPC and HypixelStyleNPC for this GUI
    private final NPCView npc;
    private final AdvancedNPCSystem npcSystem;

    // Single public constructor that accepts either concrete NPC type and delegates to an internal view.
    public NPCEditGUI(SkyblockPlugin SkyblockPlugin, Player player, Object npcObj) {
        this(SkyblockPlugin, player, npcObj instanceof HypixelStyleNPC ? new NPCView((HypixelStyleNPC) npcObj)
                : npcObj instanceof GameNPC ? new NPCView((GameNPC) npcObj)
                : throwIllegal(npcObj));
    }

    // Explicit overloads to help the compiler / call sites in the codebase resolve
    // directly to the correct constructor (avoids ambiguous or stale resolution).
    public NPCEditGUI(SkyblockPlugin SkyblockPlugin, Player player, HypixelStyleNPC npc) {
        this(SkyblockPlugin, player, new NPCView(npc));
    }

    public NPCEditGUI(SkyblockPlugin SkyblockPlugin, Player player, GameNPC npc) {
        this(SkyblockPlugin, player, new NPCView(npc));
    }

    private static NPCView throwIllegal(Object obj) {
        throw new IllegalArgumentException("Unsupported NPC type: " + (obj == null ? "null" : obj.getClass().getName()));
    }

    private NPCEditGUI(SkyblockPlugin SkyblockPlugin, Player player, NPCView npcView) {
        super(54, Component.text("§6§lEdit NPC: " + npcView.getDisplayName()).color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        this.npc = npcView;
        this.npcSystem = SkyblockPlugin.getAdvancedNPCSystem();
        setupItems();
    }

    private void setupItems() {
        // Header
        setupHeader();

        // NPC Information
        setupNPCInfo();

        // Edit Options
        setupEditOptions();

        // Actions
        setupActions();

        // Navigation
        setupNavigation();

        // Decorative borders
        setupDecorativeBorders();
    }

    private void setupHeader() {
        ItemStack header = new ItemStack(npc.getType().getIcon());
        ItemMeta meta = header.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text("§6§lEdit NPC: " + npc.getDisplayName()).color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));

            List<Component> lore = Arrays.asList(
                Component.text("§7NPC ID: §e" + npc.getNpcId()),
                Component.text("§7Type: " + npc.getType().getDisplayName()),
                Component.text("§7World: §e" + npc.getLocation().getWorld().getName()),
                Component.text("§7Position: §e" + npc.getLocation().getBlockX() + ", " + npc.getLocation().getBlockY() + ", " + npc.getLocation().getBlockZ()),
                Component.text(""),
                Component.text("§eEdit this NPC's properties!")
            );
            meta.lore(lore);
            header.setItemMeta(meta);
        }
        getInventory().setItem(4, header);
    }

    private void setupNPCInfo() {
        // Display Name
        setItem(10, Material.NAME_TAG, "§e§l📝 Display Name",
            "§7Current: §a" + npc.getDisplayName(),
            "§7• Change NPC display name",
            "§7• Visible to all players",
            "§7• Supports color codes",
            "",
            "§eClick to edit!");

        // NPC Type
        setItem(11, npc.getType().getIcon(), "§b§l🔄 NPC Type",
            "§7Current: " + npc.getType().getDisplayName(),
            "§7• Change NPC type",
            "§7• Affects NPC behavior",
            "§7• Changes appearance",
            "",
            "§eClick to change!");

        // Custom Data
        setItem(12, Material.PAPER, "§a§l📄 Custom Data",
            "§7Current: §a" + (npc.getCustomData().isEmpty() ? "None" : npc.getCustomData()),
            "§7• Set custom data",
            "§7• Type-specific information",
            "§7• JSON format supported",
            "",
            "§eClick to edit!");

        // Location
        setItem(13, Material.COMPASS, "§d§l📍 Location",
            "§7Current: §a" + npc.getLocation().getBlockX() + ", " + npc.getLocation().getBlockY() + ", " + npc.getLocation().getBlockZ(),
            "§7• Teleport to NPC",
            "§7• View location details",
            "§7• Copy coordinates",
            "",
            "§eClick to teleport!");

        // Status
        setItem(14, Material.GREEN_DYE, "§a§l✅ Status",
            "§7Current: §aActive",
            "§7• NPC is active and working",
            "§7• Players can interact",
            "§7• All systems operational",
            "",
            "§eClick to view details!");
    }

    private void setupEditOptions() {
        // Rename NPC
        setItem(19, Material.ANVIL, "§6§l✏️ Rename NPC",
            "§7Change the NPC's display name",
            "§7• Opens rename interface",
            "§7• Supports color codes",
            "§7• Real-time preview",
            "",
            "§eClick to rename!");

        // Change Type
        setItem(20, Material.ENCHANTING_TABLE, "§b§l🔄 Change Type",
            "§7Change the NPC's type",
            "§7• Opens type selection",
            "§7• Changes behavior",
            "§7• Updates appearance",
            "",
            "§eClick to change type!");

        // Edit Custom Data
        setItem(21, Material.WRITABLE_BOOK, "§a§l📝 Edit Data",
            "§7Edit custom data",
            "§7• Opens data editor",
            "§7• JSON format",
            "§7• Type-specific fields",
            "",
            "§eClick to edit data!");

        // Move NPC
        setItem(22, Material.ENDER_PEARL, "§d§l🚀 Move NPC",
            "§7Move NPC to new location",
            "§7• Teleport to new position",
            "§7• Maintains all settings",
            "§7• Updates database",
            "",
            "§eClick to move!");

        // Clone NPC
        setItem(23, Material.SLIME_BALL, "§e§l📋 Clone NPC",
            "§7Create a copy of this NPC",
            "§7• Duplicates all settings",
            "§7• New unique ID",
            "§7• Same location",
            "",
            "§eClick to clone!");
    }

    private void setupActions() {
        // Test NPC
        setItem(28, Material.EMERALD, "§a§l🧪 Test NPC",
            "§7Test the NPC's functionality",
            "§7• Simulates player interaction",
            "§7• Shows what happens",
            "§7• Debug information",
            "",
            "§eClick to test!");

        // Reset NPC
        setItem(29, Material.REDSTONE, "§c§l🔄 Reset NPC",
            "§7Reset NPC to default settings",
            "§7• Restores default values",
            "§7• Keeps location",
            "§7• WARNING: Loses custom data",
            "",
            "§eClick to reset!");

        // Delete NPC
        setItem(30, Material.BARRIER, "§c§l🗑️ Delete NPC",
            "§7Permanently delete this NPC",
            "§7• Removes from world",
            "§7• Removes from database",
            "§7• WARNING: Cannot be undone!",
            "",
            "§eClick to delete!");

        // Export NPC
        setItem(31, Material.PAPER, "§e§l📄 Export NPC",
            "§7Export NPC configuration",
            "§7• Save to file",
            "§7• Backup settings",
            "§7• Share with others",
            "",
            "§eClick to export!");

        // Permissions
        setItem(32, Material.SHIELD, "§6§l🛡️ Permissions",
            "§7Manage NPC permissions",
            "§7• Who can use this NPC",
            "§7• Permission requirements",
            "§7• Access control",
            "",
            "§eClick to manage!");
    }

    private void setupNavigation() {
        // Back to Management
        setItem(45, Material.ARROW, "§7§l⬅️ Back to Management",
            "§7Return to NPC management",
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
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }

        getInventory().setItem(slot, item);
    }

    // Adapter to present common NPC data regardless of concrete NPC class
    private static class NPCView {
        private final String npcId;
        private final AdvancedNPCSystem.NPCType type;
        private final Location location;
        private final String displayName;
        private final String customData;
        private final Villager entity;

        NPCView(HypixelStyleNPC npc) {
            this.npcId = npc.getNpcId();
            this.type = npc.getType();
            this.location = npc.getLocation();
            this.displayName = npc.getDisplayName();
            this.customData = npc.getCustomData();
            this.entity = npc.getEntity();
        }

        NPCView(GameNPC npc) {
            this.npcId = npc.getNpcId();
            this.type = npc.getType();
            this.location = npc.getLocation();
            this.displayName = npc.getDisplayName();
            this.customData = npc.getCustomData();
            this.entity = npc.getEntity();
        }

        public String getNpcId() { return npcId; }
        public AdvancedNPCSystem.NPCType getType() { return type; }
        public Location getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getCustomData() { return customData; }
        public Villager getEntity() { return entity; }
    }
}
