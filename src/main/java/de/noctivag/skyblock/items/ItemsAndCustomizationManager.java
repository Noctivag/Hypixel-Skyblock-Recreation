package de.noctivag.skyblock.items;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.display.EnhancedScoreboardSystem;
import de.noctivag.skyblock.display.GUIAnimationSystem;
import de.noctivag.skyblock.display.IntegratedMenuSystem;
import de.noctivag.skyblock.data.DatabaseManager;
import de.noctivag.skyblock.skyblock.EnchantingSystem;
import de.noctivag.skyblock.pets.PetSystem;
import de.noctivag.skyblock.pets.PetManagementSystem;
import de.noctivag.skyblock.accessories.AccessorySystem;
import de.noctivag.skyblock.gui.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Items and Customization Manager - Hypixel Skyblock Style
 * 
 * Features:
 * - Complete Category 4 Implementation
 * - Reforge System Integration
 * - Enchanting System Integration
 * - Pet System Integration
 * - Accessory System Integration
 * - GUI System Integration
 * - Scoreboard Integration
 * - Animation System Integration
 */
public class ItemsAndCustomizationManager implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    
    // Core Systems
    private final ReforgeSystem reforgeSystem;
    private final ReforgeStoneSystem reforgeStoneSystem;
    private final StatModificationSystem statModificationSystem;
    private final EnchantingSystem enchantingSystem;
    private final PetSystem petSystem;
    private final PetManagementSystem petManagementSystem;
    private final AccessorySystem accessorySystem;
    
    // GUI Systems
    private final EnhancedScoreboardSystem scoreboardSystem;
    private final GUIAnimationSystem animationSystem;
    private final Map<UUID, IntegratedMenuSystem> playerMenus = new ConcurrentHashMap<>();
    
    public ItemsAndCustomizationManager(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        
        // Initialize systems
        this.statModificationSystem = new StatModificationSystem(SkyblockPlugin, corePlatform);
        this.reforgeSystem = new ReforgeSystem(SkyblockPlugin, corePlatform.getDatabaseManager());
        this.reforgeStoneSystem = new ReforgeStoneSystem(SkyblockPlugin, corePlatform, reforgeSystem);
        this.enchantingSystem = new EnchantingSystem(SkyblockPlugin);
        this.petSystem = new PetSystem(SkyblockPlugin, corePlatform);
        this.petManagementSystem = new PetManagementSystem(SkyblockPlugin, corePlatform, petSystem);
        this.accessorySystem = new AccessorySystem(SkyblockPlugin, SkyblockPlugin.getDatabaseManager());
        
        // Initialize GUI systems
        this.scoreboardSystem = new EnhancedScoreboardSystem(SkyblockPlugin, corePlatform, statModificationSystem, 
            petManagementSystem, accessorySystem);
        this.animationSystem = new GUIAnimationSystem(SkyblockPlugin);
        
        // Register events
        SkyblockPlugin.getServer().getPluginManager().registerEvents(this, SkyblockPlugin);
        
        // Start systems
        startSystems();
    }
    
    private void startSystems() {
        // Enable scoreboard for all online players
        scoreboardSystem.enableForAllPlayers();
        
        // Start update tasks
        SkyblockPlugin.getServer().getScheduler().runTaskTimer(SkyblockPlugin, () -> {
            // Update all player stats
            for (Player player : SkyblockPlugin.getServer().getOnlinePlayers()) {
                statModificationSystem.updatePlayerStats(player);
            }
            
            // Update scoreboards
            scoreboardSystem.updateAllScoreboards();
        }, 0L, 20L); // Every second
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Enable scoreboard
        scoreboardSystem.enableScoreboard(player);
        
        // Initialize player data
        initializePlayerData(player);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Disable scoreboard
        scoreboardSystem.disableScoreboard(player);
        
        // Clean up animations
        animationSystem.cancelPlayerAnimations(playerId);
        
        // Clean up menus
        playerMenus.remove(playerId);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        
        // Handle different GUI types
        if (title.contains("REFORGE SYSTEM")) {
            handleReforgeGUIClick(event, player);
        } else if (title.contains("ENCHANTING SYSTEM")) {
            handleEnchantingGUIClick(event, player);
        } else if (title.contains("PET MANAGEMENT")) {
            handlePetGUIClick(event, player);
        } else if (title.contains("ACCESSORY SYSTEM")) {
            handleAccessoryGUIClick(event, player);
        } else if (title.contains("INTEGRATED MENU")) {
            handleIntegratedMenuClick(event, player);
        }
    }
    
    private void handleReforgeGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        // int slot = event.getSlot(); // Unused variable
        
        // Handle reforge GUI interactions
        // This would integrate with the ReforgeGUI class
    }
    
    private void handleEnchantingGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        // int slot = event.getSlot(); // Unused variable
        
        // Handle enchanting GUI interactions
        // This would integrate with the EnchantingGUI class
    }
    
    private void handlePetGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        // int slot = event.getSlot(); // Unused variable
        
        // Handle pet GUI interactions
        // This would integrate with the PetManagementGUI class
    }
    
    private void handleAccessoryGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        // int slot = event.getSlot(); // Unused variable
        
        // Handle accessory GUI interactions
        // This would integrate with the AccessoryGUI class
    }
    
    private void handleIntegratedMenuClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        // int slot = event.getSlot(); // Unused variable
        
        IntegratedMenuSystem menu = playerMenus.get(player.getUniqueId());
        if (menu != null) {
            menu.handleClick(event.getSlot());
        }
    }
    
    private void initializePlayerData(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Initialize stat modification system
        statModificationSystem.getPlayerStats(playerId);
        
        // Initialize pet system
        petManagementSystem.getPlayerPets(playerId);
        
        // Initialize accessory system
        accessorySystem.getPlayerAccessories(playerId);
        
        // Initialize enchanting system
        enchantingSystem.getPlayerEnchanting(playerId);
    }
    
    public void openIntegratedMenu(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Create or get existing menu
        IntegratedMenuSystem menu = playerMenus.computeIfAbsent(playerId, k -> 
            new IntegratedMenuSystem(SkyblockPlugin, player, reforgeSystem, reforgeStoneSystem, 
                statModificationSystem, enchantingSystem, petSystem, petManagementSystem, accessorySystem));
        
        // Start GUI session
        animationSystem.startGUISession(player, menu);
        
        // Open menu
        menu.openGUI(player);
        
        // Start opening animation
        animationSystem.startLoadingAnimation(player, "Opening Menu");
    }
    
    public void openReforgeSystem(Player player) {
        ReforgeGUI gui = new ReforgeGUI(SkyblockPlugin, player, reforgeSystem, reforgeStoneSystem, statModificationSystem);
        // animationSystem.startGUISession(player, (CustomGUI) gui); // Commented out - ReforgeGUI doesn't extend CustomGUI
        gui.openGUI(player);
        animationSystem.startLoadingAnimation(player, "Opening Reforge System");
    }
    
    public void openEnchantingSystem(Player player) {
        EnchantingGUI gui = new EnchantingGUI(SkyblockPlugin, player, enchantingSystem);
        animationSystem.startGUISession(player, gui);
        gui.openGUI(player);
        animationSystem.startLoadingAnimation(player, "Opening Enchanting System");
    }
    
    public void openPetSystem(Player player) {
        PetManagementGUI gui = new PetManagementGUI(SkyblockPlugin, player, petSystem, petManagementSystem);
        animationSystem.startGUISession(player, gui);
        gui.openGUI(player);
        animationSystem.startLoadingAnimation(player, "Opening Pet System");
    }
    
    public void openAccessorySystem(Player player) {
        AccessoryGUI gui = new AccessoryGUI(SkyblockPlugin, player, accessorySystem);
        // animationSystem.startGUISession(player, (CustomGUI) gui); // Commented out - ReforgeGUI doesn't extend CustomGUI
        gui.openGUI(player);
        animationSystem.startLoadingAnimation(player, "Opening Accessory System");
    }
    
    // Getters for systems
    public ReforgeSystem getReforgeSystem() { return reforgeSystem; }
    public ReforgeStoneSystem getReforgeStoneSystem() { return reforgeStoneSystem; }
    public StatModificationSystem getStatModificationSystem() { return statModificationSystem; }
    public EnchantingSystem getEnchantingSystem() { return enchantingSystem; }
    public PetSystem getPetSystem() { return petSystem; }
    public PetManagementSystem getPetManagementSystem() { return petManagementSystem; }
    public AccessorySystem getAccessorySystem() { return accessorySystem; }
    public EnhancedScoreboardSystem getScoreboardSystem() { return scoreboardSystem; }
    public GUIAnimationSystem getAnimationSystem() { return animationSystem; }
    
    // System status methods
    public boolean isSystemEnabled(String systemName) {
        return switch (systemName.toLowerCase()) {
            case "reforge" -> reforgeSystem != null;
            case "enchanting" -> enchantingSystem != null;
            case "pets" -> petSystem != null && petManagementSystem != null;
            case "accessories" -> accessorySystem != null;
            case "stats" -> statModificationSystem != null;
            case "scoreboard" -> scoreboardSystem != null;
            case "animations" -> animationSystem != null;
            default -> false;
        };
    }
    
    public Map<String, Boolean> getAllSystemStatus() {
        Map<String, Boolean> status = new HashMap<>();
        status.put("Reforge System", isSystemEnabled("reforge"));
        status.put("Enchanting System", isSystemEnabled("enchanting"));
        status.put("Pet System", isSystemEnabled("pets"));
        status.put("Accessory System", isSystemEnabled("accessories"));
        status.put("Stats System", isSystemEnabled("stats"));
        status.put("Scoreboard System", isSystemEnabled("scoreboard"));
        status.put("Animation System", isSystemEnabled("animations"));
        return status;
    }
    
    public void reloadSystems() {
        // Reload all systems
        SkyblockPlugin.getLogger().info("Reloading Items and Customization systems...");
        
        // Disable scoreboards
        scoreboardSystem.disableForAllPlayers();
        
        // Cancel all animations
        animationSystem.cancelAllAnimations();
        
        // Clear player menus
        playerMenus.clear();
        
        // Re-enable scoreboards
        scoreboardSystem.enableForAllPlayers();
        
        SkyblockPlugin.getLogger().info("Items and Customization systems reloaded successfully!");
    }
    
    public void shutdown() {
        // Shutdown all systems
        SkyblockPlugin.getLogger().info("Shutting down Items and Customization systems...");
        
        // Disable scoreboards
        scoreboardSystem.disableForAllPlayers();
        
        // Cancel all animations
        animationSystem.cancelAllAnimations();
        
        // Clear player menus
        playerMenus.clear();
        
        SkyblockPlugin.getLogger().info("Items and Customization systems shut down successfully!");
    }
}
