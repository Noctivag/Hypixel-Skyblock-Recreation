package de.noctivag.plugin.community;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommunityCenterSystem {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCommunityData> playerCommunityData = new ConcurrentHashMap<>();
    private final Map<String, CommunityProject> communityProjects = new HashMap<>();
    
    public CommunityCenterSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeCommunityProjects();
    }
    
    private void initializeCommunityProjects() {
        communityProjects.put("farming_project", new CommunityProject("farming_project", "Farming Project", "§aFarming Project", 
            Material.WHEAT, "§7Help build a community farm.", 1000, 500));
        communityProjects.put("mining_project", new CommunityProject("mining_project", "Mining Project", "§7Mining Project", 
            Material.DIAMOND_PICKAXE, "§7Help build a community mine.", 2000, 1000));
    }
    
    public void openCommunityCenterGUI(Player player) {
        Inventory gui = plugin.getServer().createInventory(null, 54, "§6§lCommunity Center");
        
        addGUIItem(gui, 10, Material.WHEAT, "§a§lFarming Projects", "§7Community farming projects");
        addGUIItem(gui, 11, Material.DIAMOND_PICKAXE, "§7§lMining Projects", "§7Community mining projects");
        addGUIItem(gui, 12, Material.BRICKS, "§e§lBuilding Projects", "§7Community building projects");
        addGUIItem(gui, 13, Material.EMERALD, "§b§lDonations", "§7Donate to community projects");
        
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the community center menu.");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public static class CommunityProject {
        private final String id, name, displayName, description;
        private final Material icon;
        private final int requiredResources, currentResources;
        
        public CommunityProject(String id, String name, String displayName, Material icon, 
                              String description, int requiredResources, int currentResources) {
            this.id = id; this.name = name; this.displayName = displayName; this.icon = icon;
            this.description = description; this.requiredResources = requiredResources; 
            this.currentResources = currentResources;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public int getRequiredResources() { return requiredResources; }
        public int getCurrentResources() { return currentResources; }
    }
    
    public static class PlayerCommunityData {
        private final UUID playerId;
        private final Map<String, Integer> projectContributions = new HashMap<>();
        private final int totalContributions;
        
        public PlayerCommunityData(UUID playerId) {
            this.playerId = playerId;
            this.totalContributions = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public Map<String, Integer> getProjectContributions() { return projectContributions; }
        public int getTotalContributions() { return totalContributions; }
    }
}
