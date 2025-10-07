package de.noctivag.skyblock.npcs;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
// import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;

/**
 * Advanced NPC System - Hypixel Skyblock Style
 */
public class AdvancedNPCSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerNPCManager> playerNPCManagers = new ConcurrentHashMap<>();
    private final Map<String, HypixelStyleNPC> activeNPCs = new ConcurrentHashMap<>();
    
    // Advanced systems
    private final NPCDialogueSystem dialogueSystem;
    private final NPCSkinSystem skinSystem;
    private final NPCQuestSystem questSystem;
    private final NPCSkinManager skinManager;
    
    public AdvancedNPCSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        // Initialize advanced systems
        this.dialogueSystem = new NPCDialogueSystem(SkyblockPlugin);
        this.skinSystem = new NPCSkinSystem(SkyblockPlugin);
        this.questSystem = new NPCQuestSystem(SkyblockPlugin);
        this.skinManager = new NPCSkinManager(SkyblockPlugin);
    }
    
    public void initialize() {
        startNPCUpdateTask();
        loadNPCsFromDatabase();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void startNPCUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerNPCManager> entry : playerNPCManagers.entrySet()) {
                    PlayerNPCManager manager = entry.getValue();
                    manager.update();
                }
                
                for (HypixelStyleNPC npc : activeNPCs.values()) {
                    npc.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
    }
    
    private void loadNPCsFromDatabase() {
        // TODO: Implement proper database loading
        // Load NPCs from database (placeholder)
        /*
        databaseManager.executeQuery("SELECT * FROM npcs").thenAccept(resultSet -> {
            try {
                while (resultSet.next()) {
                    String npcId = resultSet.getString("npc_id");
                    String npcType = resultSet.getString("npc_type");
                    String world = resultSet.getString("world");
                    double x = resultSet.getDouble("x");
                    double y = resultSet.getDouble("y");
                    double z = resultSet.getDouble("z");
                    float yaw = resultSet.getFloat("yaw");
                    float pitch = resultSet.getFloat("pitch");
                    String displayName = resultSet.getString("display_name");
                    String customData = resultSet.getString("custom_data");
                    
                    Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    createHypixelNPC(npcId, NPCType.valueOf(npcType), location, displayName, customData);
                }
                resultSet.close();
            } catch (Exception e) {
                SkyblockPlugin.getLogger().severe("Error loading NPCs from database: " + e.getMessage());
            }
        });
        */
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.displayName() != null ? meta.displayName().toString() : "";
        
        if (displayName.contains("NPC Tool")) {
            handleNPCToolUse(player, event);
        }
    }
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
        Villager villager = (Villager) event.getRightClicked();
            String npcId = getNPCIdFromEntity(villager);
        
            if (npcId != null) {
                HypixelStyleNPC npc = activeNPCs.get(npcId);
        if (npc != null) {
                    npc.onPlayerInteract(event.getPlayer());
                    // Start dialogue system
                    dialogueSystem.startDialogue(event.getPlayer(), npc.getType().name().toLowerCase(), npc.getDisplayName());
                }
            }
        }
    }
    
    private void handleNPCToolUse(Player player, PlayerInteractEvent event) {
        if (event.getAction().toString().contains("RIGHT_CLICK_BLOCK")) {
            Location location = event.getClickedBlock().getLocation().add(0, 1, 0);
            openNPCCreationGUI(player, location);
        }
    }
    
    private void openNPCCreationGUI(Player player, Location location) {
        new de.noctivag.skyblock.gui.NPCCreationGUI(SkyblockPlugin, player, location).open(player);
    }
    
    public HypixelStyleNPC createHypixelNPC(String npcId, NPCType type, Location location, String displayName, String customData) {
        HypixelStyleNPC npc = new HypixelStyleNPC(SkyblockPlugin, npcId, type, location, displayName, customData);
        activeNPCs.put(npcId, npc);
        saveNPCToDatabase(npc);
        return npc;
    }
    
    // Legacy method for compatibility
    public GameNPC createNPC(String npcId, NPCType type, Location location, String displayName, String customData) {
        return new GameNPC(SkyblockPlugin, npcId, type, location, displayName, customData);
    }
    
    public void removeNPC(String npcId) {
        HypixelStyleNPC npc = activeNPCs.remove(npcId);
        if (npc != null) {
            npc.remove();
            databaseManager.executeUpdate("DELETE FROM npcs WHERE npc_id = ?", npcId);
        }
    }
    
    public void updateNPC(String npcId, String displayName, String customData) {
        HypixelStyleNPC npc = activeNPCs.get(npcId);
        if (npc != null) {
            npc.updateDisplayName(displayName);
            npc.updateCustomData(customData);
            databaseManager.executeUpdate("UPDATE npcs SET display_name = ?, custom_data = ? WHERE npc_id = ?", 
                displayName, customData, npcId);
        }
    }
    
    private void saveNPCToDatabase(HypixelStyleNPC npc) {
        Location loc = npc.getLocation();
        databaseManager.executeUpdate("""
            INSERT INTO npcs (npc_id, npc_type, world, x, y, z, yaw, pitch, display_name, custom_data, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
            ON DUPLICATE KEY UPDATE
            npc_type = VALUES(npc_type),
            world = VALUES(world),
            x = VALUES(x),
            y = VALUES(y),
            z = VALUES(z),
            yaw = VALUES(yaw),
            pitch = VALUES(pitch),
            display_name = VALUES(display_name),
            custom_data = VALUES(custom_data),
            updated_at = NOW()
        """, npc.getNpcId(), npc.getType().name(), loc.getWorld().getName(), 
             loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), 
             npc.getDisplayName(), npc.getCustomData());
    }
    
    private String getNPCIdFromEntity(Villager villager) {
        for (HypixelStyleNPC npc : activeNPCs.values()) {
            if (npc.getEntity() == villager) {
                return npc.getNpcId();
            }
        }
        return null;
    }
    
    public ItemStack createNPCTool() {
        ItemStack tool = new ItemStack(Material.STICK);
        ItemMeta meta = tool.getItemMeta();
        
        meta.displayName(net.kyori.adventure.text.Component.text("§6§lNPC Tool"));
        List<net.kyori.adventure.text.Component> lore = new ArrayList<>();
        lore.add(net.kyori.adventure.text.Component.text("§7Right-click on a block to place an NPC"));
        lore.add(net.kyori.adventure.text.Component.text("§7Left-click on an NPC to manage it"));
        lore.add(net.kyori.adventure.text.Component.text(""));
        lore.add(net.kyori.adventure.text.Component.text("§eRight-click on block to place NPC"));
        lore.add(net.kyori.adventure.text.Component.text("§eLeft-click on NPC to manage"));
        
        meta.lore(lore);
        tool.setItemMeta(meta);
        
        return tool;
    }
    
    public PlayerNPCManager getPlayerNPCManager(UUID playerId) {
        return playerNPCManagers.computeIfAbsent(playerId, k -> new PlayerNPCManager(playerId));
    }
    
    public Map<String, HypixelStyleNPC> getActiveNPCs() {
        return activeNPCs;
    }
    
    public HypixelStyleNPC getNPC(String npcId) {
        return activeNPCs.get(npcId);
    }
    
    // Getter methods for advanced systems
    public NPCDialogueSystem getDialogueSystem() { return dialogueSystem; }
    public NPCSkinSystem getSkinSystem() { return skinSystem; }
    public NPCQuestSystem getQuestSystem() { return questSystem; }
    public NPCSkinManager getSkinManager() { return skinManager; }
    
    // Player-based NPC methods
    public PlayerBasedNPC createPlayerNPC(String npcId, NPCType type, Location location, String displayName, String customData) {
        PlayerBasedNPC npc = new PlayerBasedNPC(SkyblockPlugin, npcId, type, location, displayName, customData);
        // Note: In a real implementation, you would store this in a separate map
        // and handle the player entity creation properly
        return npc;
    }
    
    public enum NPCType {
        SHOP("§aShop", "§7NPC that opens a shop interface", Material.EMERALD),
        QUEST("§bQuest", "§7NPC that gives quests", Material.BOOK),
        INFO("§eInfo", "§7NPC that provides information", Material.PAPER),
        WARP("§dWarp", "§7NPC that teleports players", Material.ENDER_PEARL),
        BANK("§6Bank", "§7NPC that opens banking interface", Material.GOLD_INGOT),
        AUCTION("§cAuction", "§7NPC that opens auction house", Material.GOLD_BLOCK),
        GUILD("§5Guild", "§7NPC that manages guilds", Material.SHIELD),
        PET("§dPet", "§7NPC that manages pets", Material.BONE),
        COSMETIC("§eCosmetic", "§7NPC that opens cosmetics", Material.NETHER_STAR),
        ADMIN("§4Admin", "§7NPC for admin functions", Material.COMMAND_BLOCK);
        
        private final String displayName;
        private final String description;
        private final Material icon;
        
        NPCType(String displayName, String description, Material icon) {
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
    
    public static class PlayerNPCManager {
        private final UUID playerId;
        private int npcsCreated = 0;
        private int npcsManaged = 0;
        private long lastUpdate;
        
        public PlayerNPCManager(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = java.lang.System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save player NPC data to database
        }
        
        public void addNPCCreated() {
            npcsCreated++;
        }
        
        public void addNPCManaged() {
            npcsManaged++;
        }
        
        public int getNPCsCreated() { return npcsCreated; }
        public int getNPCsManaged() { return npcsManaged; }
        public UUID getPlayerId() { return playerId; }
    }
    
    // Missing method implementations for compilation fixes
    public void openTypeSelectionGUI(org.bukkit.entity.Player player) {
        player.sendMessage(Component.text("§cType Selection GUI not implemented yet!"));
    }
    
    public void openDataEditorGUI(org.bukkit.entity.Player player) {
        player.sendMessage(Component.text("§cData Editor GUI not implemented yet!"));
    }
    
    public void openPermissionsGUI(org.bukkit.entity.Player player) {
        player.sendMessage(Component.text("§cPermissions GUI not implemented yet!"));
    }
    
}
