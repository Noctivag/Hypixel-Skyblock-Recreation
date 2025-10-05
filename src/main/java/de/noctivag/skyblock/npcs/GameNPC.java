package de.noctivag.skyblock.npcs;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Game NPC Class - Represents a single NPC in the game
 */
public class GameNPC {
    private final SkyblockPlugin SkyblockPlugin;
    private final String npcId;
    private final AdvancedNPCSystem.NPCType type;
    private Location location;
    private String displayName;
    private String customData;
    private Villager entity;
    private BukkitTask animationTask;
    
    public GameNPC(SkyblockPlugin SkyblockPlugin, String npcId, AdvancedNPCSystem.NPCType type, Location location, String displayName, String customData) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.npcId = npcId;
        this.type = type;
        this.location = location;
        this.displayName = displayName;
        this.customData = customData;
        
        spawn();
        startAnimation();
    }
    
    private void spawn() {
        entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        entity.customName(net.kyori.adventure.text.Component.text(displayName));
        entity.setCustomNameVisible(true);
        entity.setAI(false);
        entity.setInvulnerable(true);
        entity.setSilent(true);
        
        // Set profession based on NPC type
        switch (type) {
            case SHOP -> entity.setProfession(Villager.Profession.TOOLSMITH);
            case QUEST -> entity.setProfession(Villager.Profession.CARTOGRAPHER);
            case INFO -> entity.setProfession(Villager.Profession.LIBRARIAN);
            case WARP -> entity.setProfession(Villager.Profession.CLERIC);
            case BANK -> entity.setProfession(Villager.Profession.ARMORER);
            case AUCTION -> entity.setProfession(Villager.Profession.WEAPONSMITH);
            case GUILD -> entity.setProfession(Villager.Profession.BUTCHER);
        }
    }
    
    private void startAnimation() {
        animationTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (entity != null && !entity.isDead()) {
                    // Simple animation - look around
                    entity.setRotation(entity.getLocation().getYaw() + 5, entity.getLocation().getPitch());
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
    }
    
    public void onPlayerInteract(Player player) {
        switch (type) {
            case SHOP -> openShop(player);
            case QUEST -> openQuest(player);
            case INFO -> openInfo(player);
            case WARP -> openWarp(player);
            case BANK -> openBank(player);
            case AUCTION -> openAuction(player);
            case GUILD -> openGuild(player);
            case PET -> openPet(player);
            case COSMETIC -> openCosmetic(player);
            case ADMIN -> openAdmin(player);
        }
    }
    
    private void openShop(Player player) {
        player.sendMessage(Component.text("§aShop NPC: Opening shop interface..."));
        // Open shop GUI
    }
    
    private void openQuest(Player player) {
        player.sendMessage(Component.text("§bQuest NPC: Opening quest interface..."));
        // Open quest GUI
    }
    
    private void openInfo(Player player) {
        player.sendMessage("§eInfo NPC: " + customData);
    }
    
    private void openWarp(Player player) {
        player.sendMessage(Component.text("§dWarp NPC: Opening warp interface..."));
        // Open warp GUI
    }
    
    private void openBank(Player player) {
        player.sendMessage(Component.text("§6Bank NPC: Opening bank interface..."));
        // Open bank GUI
    }
    
    private void openAuction(Player player) {
        player.sendMessage(Component.text("§cAuction NPC: Opening auction house..."));
        // Open auction GUI
    }
    
    private void openGuild(Player player) {
        player.sendMessage(Component.text("§5Guild NPC: Opening guild interface..."));
        // Open guild GUI
    }
    
    private void openPet(Player player) {
        player.sendMessage(Component.text("§dPet NPC: Opening pet interface..."));
        // Open pet GUI
    }
    
    private void openCosmetic(Player player) {
        player.sendMessage(Component.text("§eCosmetic NPC: Opening cosmetics..."));
        // Open cosmetic GUI
    }
    
    private void openAdmin(Player player) {
        if (player.hasPermission("basicsplugin.admin")) {
            player.sendMessage(Component.text("§4Admin NPC: Opening admin interface..."));
            // Open admin GUI
        } else {
            player.sendMessage(Component.text("§cYou don't have permission to use this NPC!"));
        }
    }
    
    public void update() {
        // Update NPC state
        if (entity != null && !entity.isDead()) {
            entity.customName(net.kyori.adventure.text.Component.text(displayName));
        }
    }
    
    public void remove() {
        if (animationTask != null) {
            animationTask.cancel();
        }
        if (entity != null && !entity.isDead()) {
            entity.remove();
        }
    }
    
    public void updateDisplayName(String newDisplayName) {
        this.displayName = newDisplayName;
        if (entity != null && !entity.isDead()) {
            entity.customName(net.kyori.adventure.text.Component.text(newDisplayName));
        }
    }
    
    public void updateCustomData(String newCustomData) {
        this.customData = newCustomData;
    }
    
    // Getters
    public String getNpcId() { return npcId; }
    public AdvancedNPCSystem.NPCType getType() { return type; }
    public Location getLocation() { return location; }
    public String getDisplayName() { return displayName; }
    public String getCustomData() { return customData; }
    public Villager getEntity() { return entity; }
}
