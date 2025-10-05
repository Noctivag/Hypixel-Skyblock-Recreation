package de.noctivag.skyblock.elections;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ElectionRoomSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerElectionData> playerElectionData = new ConcurrentHashMap<>();
    private final Map<String, Election> elections = new HashMap<>();
    
    public ElectionRoomSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeElections();
    }
    
    private void initializeElections() {
        elections.put("mayor_election", new Election("mayor_election", "Mayor Election", "§6Mayor Election", 
            Material.GOLDEN_HELMET, "§7Vote for the next mayor.", ElectionType.MAYOR, 7));
        elections.put("council_election", new Election("council_election", "Council Election", "§bCouncil Election", 
            Material.EMERALD, "§7Vote for council members.", ElectionType.COUNCIL, 3));
    }
    
    public void openElectionRoomGUI(Player player) {
        Inventory gui = SkyblockPlugin.getServer().createInventory(null, 54, "§6§lElection Room");
        
        addGUIItem(gui, 10, Material.GOLDEN_HELMET, "§6§lMayor Elections", "§7Vote for mayor");
        addGUIItem(gui, 11, Material.EMERALD, "§b§lCouncil Elections", "§7Vote for council members");
        addGUIItem(gui, 12, Material.BOOK, "§e§lElection History", "§7View past election results");
        addGUIItem(gui, 13, Material.CLOCK, "§a§lUpcoming Elections", "§7View upcoming elections");
        
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the election room menu.");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public static class Election {
        private final String id, name, displayName, description;
        private final Material icon;
        private final ElectionType type;
        private final int duration;
        
        public Election(String id, String name, String displayName, Material icon, 
                       String description, ElectionType type, int duration) {
            this.id = id; this.name = name; this.displayName = displayName; this.icon = icon;
            this.description = description; this.type = type; this.duration = duration;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public ElectionType getType() { return type; }
        public int getDuration() { return duration; }
    }
    
    public static class PlayerElectionData {
        private final UUID playerId;
        private final Map<String, String> votes = new HashMap<>();
        private final boolean hasVoted;
        
        public PlayerElectionData(UUID playerId) {
            this.playerId = playerId;
            this.hasVoted = false;
        }
        
        public UUID getPlayerId() { return playerId; }
        public Map<String, String> getVotes() { return votes; }
        public boolean hasVoted() { return hasVoted; }
    }
    
    public enum ElectionType {
        MAYOR("Mayor", "§6Mayor"),
        COUNCIL("Council", "§bCouncil"),
        GUILD("Guild", "§eGuild");
        
        private final String name, displayName;
        
        ElectionType(String name, String displayName) {
            this.name = name; this.displayName = displayName;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
    }
}
