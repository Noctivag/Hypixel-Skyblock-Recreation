package de.noctivag.skyblock.dungeons;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Dungeon System - Hypixel Skyblock Style
 * Implements Catacombs with all floors, classes, and party mechanics
 */
public class AdvancedDungeonSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerDungeonData> playerDungeonData = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonParty> activeParties = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonInstance> activeInstances = new ConcurrentHashMap<>();
    
    public AdvancedDungeonSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        startDungeonUpdateTask();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void startDungeonUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveInstances();
                updateActiveParties();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void updateActiveInstances() {
        for (Map.Entry<UUID, DungeonInstance> entry : activeInstances.entrySet()) {
            DungeonInstance instance = entry.getValue();
            if (instance.isActive()) {
                instance.update();
            } else {
                activeInstances.remove(entry.getKey());
            }
        }
    }
    
    private void updateActiveParties() {
        for (Map.Entry<UUID, DungeonParty> entry : activeParties.entrySet()) {
            DungeonParty party = entry.getValue();
            if (party.isActive()) {
                party.update();
            } else {
                activeParties.remove(entry.getKey());
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Dungeon") || displayName.contains("dungeon")) {
            openDungeonGUI(player);
        }
    }
    
    public void openDungeonGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§5§lDungeon System"));
        
        // Add dungeon floors
        addGUIItem(gui, 10, Material.STONE_BRICKS, "§a§lFloor 1 - Entrance", "§7The entrance to the Catacombs");
        addGUIItem(gui, 11, Material.STONE_BRICKS, "§e§lFloor 2", "§7The second floor of the Catacombs");
        addGUIItem(gui, 12, Material.STONE_BRICKS, "§6§lFloor 3", "§7The third floor of the Catacombs");
        addGUIItem(gui, 13, Material.STONE_BRICKS, "§c§lFloor 4", "§7The fourth floor of the Catacombs");
        addGUIItem(gui, 14, Material.STONE_BRICKS, "§5§lFloor 5", "§7The fifth floor of the Catacombs");
        addGUIItem(gui, 15, Material.STONE_BRICKS, "§d§lFloor 6", "§7The sixth floor of the Catacombs");
        addGUIItem(gui, 16, Material.STONE_BRICKS, "§4§lFloor 7", "§7The seventh floor of the Catacombs");
        
        // Add dungeon management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Dungeon Progress", "§7View your dungeon progress.");
        addGUIItem(gui, 19, Material.DIAMOND_SWORD, "§c§lStart Dungeon", "§7Start a new dungeon run.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lDungeon Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lDungeon Shop", "§7Buy dungeon items.");
        addGUIItem(gui, 22, Material.BARRIER, "§c§lLeave Dungeon", "§7Leave your current dungeon.");
        
        // Add party management
        addGUIItem(gui, 27, Material.PLAYER_HEAD, "§b§lParty Management", "§7Manage your dungeon party.");
        addGUIItem(gui, 28, Material.ARROW, "§a§lJoin Party", "§7Join a dungeon party.");
        addGUIItem(gui, 29, Material.BARRIER, "§c§lLeave Party", "§7Leave your current party.");
        
        // Add class selection
        addGUIItem(gui, 36, Material.DIAMOND_SWORD, "§c§lBerserker", "§7High damage, low defense");
        addGUIItem(gui, 37, Material.BOW, "§a§lArcher", "§7Ranged damage, medium defense");
        addGUIItem(gui, 38, Material.STICK, "§b§lMage", "§7Magic damage, low defense");
        addGUIItem(gui, 39, Material.SHIELD, "§e§lTank", "§7High defense, low damage");
        addGUIItem(gui, 40, Material.GOLDEN_APPLE, "§d§lHealer", "§7Healing support, medium defense");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the dungeon menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aDungeon GUI opened!");
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
    
    public PlayerDungeonData getPlayerDungeonData(UUID playerId) {
        return playerDungeonData.computeIfAbsent(playerId, k -> new PlayerDungeonData(playerId));
    }
    
    public DungeonParty getActiveParty(UUID playerId) {
        return activeParties.get(playerId);
    }
    
    public DungeonInstance getActiveInstance(UUID playerId) {
        return activeInstances.get(playerId);
    }
    
    public enum DungeonFloor {
        F1("§aFloor 1", 1),
        F2("§eFloor 2", 2),
        F3("§6Floor 3", 3),
        F4("§cFloor 4", 4),
        F5("§5Floor 5", 5),
        F6("§dFloor 6", 6),
        F7("§4Floor 7", 7);
        
        private final String displayName;
        private final int floor;
        
        DungeonFloor(String displayName, int floor) {
            this.displayName = displayName;
            this.floor = floor;
        }
        
        public String getDisplayName() { return displayName; }
        public int getFloor() { return floor; }
    }
    
    public enum DungeonClass {
        BERSERKER("§cBerserker", "§7High damage, low defense", Material.DIAMOND_SWORD),
        ARCHER("§aArcher", "§7Ranged damage, medium defense", Material.BOW),
        MAGE("§bMage", "§7Magic damage, low defense", Material.STICK),
        TANK("§eTank", "§7High defense, low damage", Material.SHIELD),
        HEALER("§dHealer", "§7Healing support, medium defense", Material.GOLDEN_APPLE);
        
        private final String displayName;
        private final String description;
        private final Material icon;
        
        DungeonClass(String displayName, String description, Material icon) {
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
    
    public static class PlayerDungeonData {
        private final UUID playerId;
        private int dungeonLevel;
        private int dungeonXP;
        private DungeonClass selectedClass;
        
        public PlayerDungeonData(UUID playerId) {
            this.playerId = playerId;
            this.dungeonLevel = 1;
            this.dungeonXP = 0;
            this.selectedClass = DungeonClass.BERSERKER;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getDungeonLevel() { return dungeonLevel; }
        public int getDungeonXP() { return dungeonXP; }
        public DungeonClass getSelectedClass() { return selectedClass; }
        
        public void addDungeonXP(int xp) {
            this.dungeonXP += xp;
            checkLevelUp();
        }
        
        public void setSelectedClass(DungeonClass dungeonClass) {
            this.selectedClass = dungeonClass;
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(dungeonLevel + 1);
            if (dungeonXP >= requiredXP) {
                dungeonLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
    
    public static class DungeonParty {
        private final UUID leaderId;
        private final List<UUID> members = new ArrayList<>();
        private boolean active;
        
        public DungeonParty(UUID leaderId) {
            this.leaderId = leaderId;
            this.members.add(leaderId);
            this.active = true;
        }
        
        public UUID getLeaderId() { return leaderId; }
        public List<UUID> getMembers() { return new ArrayList<>(members); }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        
        public boolean addMember(UUID playerId) {
            if (members.size() >= 5) return false;
            members.add(playerId);
            return true;
        }
        
        public void update() {
            // Party update logic
        }
    }
    
    public static class DungeonInstance {
        private final UUID playerId;
        private final DungeonFloor floor;
        private final long startTime;
        private boolean active;
        
        public DungeonInstance(UUID playerId, DungeonFloor floor) {
            this.playerId = playerId;
            this.floor = floor;
            this.startTime = System.currentTimeMillis();
            this.active = true;
        }
        
        public UUID getPlayerId() { return playerId; }
        public DungeonFloor getFloor() { return floor; }
        public long getStartTime() { return startTime; }
        public boolean isActive() { return active; }
        
        public void update() {
            // Dungeon instance update logic
        }
    }
}
