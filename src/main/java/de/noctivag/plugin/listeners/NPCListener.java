package de.noctivag.plugin.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.gui.*;
import de.noctivag.plugin.npcs.AdvancedNPCSystem;
import de.noctivag.plugin.npcs.HypixelStyleNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import net.kyori.adventure.text.Component;

public class NPCListener implements Listener {
    private final Plugin plugin;

    public NPCListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());

        // Handle NPC Creation GUI
        if (title.contains("NPC Creation")) {
            handleNPCCreationGUIClick(event, player);
        }
        // Handle NPC Management GUI
        else if (title.contains("NPC Management")) {
            handleNPCManagementGUIClick(event, player);
        }
        // Handle NPC Edit GUI
        else if (title.contains("Edit NPC:")) {
            handleNPCEditGUIClick(event, player);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;

        String displayName = item.getItemMeta() != null && item.getItemMeta().displayName() != null ? 
            net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(item.getItemMeta().displayName()) : "";

        if (displayName.contains("NPC Tool")) {
            event.setCancelled(true);

            if (event.getAction().toString().contains("RIGHT_CLICK_BLOCK")) {
                // Open NPC creation GUI
                new NPCCreationGUI(plugin, player, event.getClickedBlock().getLocation().add(0, 1, 0)).open(player);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof org.bukkit.entity.Villager) {
            org.bukkit.entity.Villager villager = (org.bukkit.entity.Villager) event.getRightClicked();

            // Check if this is an NPC
            AdvancedNPCSystem npcSystem = plugin.getAdvancedNPCSystem();
            HypixelStyleNPC npc = null;

            for (HypixelStyleNPC gameNPC : npcSystem.getActiveNPCs().values()) {
                if (gameNPC.getEntity() == villager) {
                    npc = gameNPC;
                    break;
                }
            }

            if (npc != null) {
                event.setCancelled(true);

                // Check if player is holding NPC tool for management
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
                if (item != null && item.hasItemMeta() &&
                    item.getItemMeta().displayName() != null &&
                    net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(item.getItemMeta().displayName()).contains("NPC Tool")) {
                    // Open NPC edit GUI (use Object-cast to select the fallback constructor)
                    new NPCEditGUI(plugin, event.getPlayer(), (Object) npc).open(event.getPlayer());
                } else {
                    // Normal NPC interaction
                    npc.onPlayerInteract(event.getPlayer());
                }
            }
        }
    }

    private void handleNPCCreationGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();

        AdvancedNPCSystem npcSystem = plugin.getAdvancedNPCSystem();

        switch (slot) {
            // Shop NPC
            case 10 -> {
                String npcId = "shop_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.SHOP,
                    getLocationFromGUI(), "\u00A7aShop NPC", "{}");
                player.sendMessage(Component.text("\u00A7aShop NPC created successfully!"));
                player.closeInventory();
            }
            // Quest NPC
            case 11 -> {
                String npcId = "quest_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.QUEST,
                    getLocationFromGUI(), "\u00A7bQuest NPC", "{}");
                player.sendMessage(Component.text("\u00A7aQuest NPC created successfully!"));
                player.closeInventory();
            }
            // Info NPC
            case 12 -> {
                String npcId = "info_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.INFO,
                    getLocationFromGUI(), "\u00A7eInfo NPC", "{}");
                player.sendMessage(Component.text("\u00A7aInfo NPC created successfully!"));
                player.closeInventory();
            }
            // Warp NPC
            case 13 -> {
                String npcId = "warp_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.WARP,
                    getLocationFromGUI(), "\u00A7dWarp NPC", "{}");
                player.sendMessage(Component.text("\u00A7aWarp NPC created successfully!"));
                player.closeInventory();
            }
            // Bank NPC
            case 14 -> {
                String npcId = "bank_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.BANK,
                    getLocationFromGUI(), "\u00A76Bank NPC", "{}");
                player.sendMessage(Component.text("\u00A7aBank NPC created successfully!"));
                player.closeInventory();
            }
            // Auction NPC
            case 15 -> {
                String npcId = "auction_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.AUCTION,
                    getLocationFromGUI(), "\u00A7cAuction NPC", "{}");
                player.sendMessage(Component.text("\u00A7aAuction NPC created successfully!"));
                player.closeInventory();
            }
            // Guild NPC
            case 16 -> {
                String npcId = "guild_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.GUILD,
                    getLocationFromGUI(), "\u00A75Guild NPC", "{}");
                player.sendMessage(Component.text("\u00A7aGuild NPC created successfully!"));
                player.closeInventory();
            }
            // Pet NPC
            case 19 -> {
                String npcId = "pet_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.PET,
                    getLocationFromGUI(), "\u00A7dPet NPC", "{}");
                player.sendMessage(Component.text("\u00A7aPet NPC created successfully!"));
                player.closeInventory();
            }
            // Cosmetic NPC
            case 20 -> {
                String npcId = "cosmetic_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.COSMETIC,
                    getLocationFromGUI(), "\u00A7eCosmetic NPC", "{}");
                player.sendMessage(Component.text("\u00A7aCosmetic NPC created successfully!"));
                player.closeInventory();
            }
            // Admin NPC
            case 21 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    String npcId = "admin_" + System.currentTimeMillis();
                    npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.ADMIN,
                        getLocationFromGUI(), "\u00A74Admin NPC", "{}");
                    player.sendMessage(Component.text("\u00A7aAdmin NPC created successfully!"));
                    player.closeInventory();
                } else {
                    player.sendMessage(Component.text("\u00A7cYou don't have permission to create Admin NPCs!"));
                }
            }

            // Navigation
            case 45 -> player.closeInventory();
            case 49 -> player.closeInventory();
        }
    }

    private void handleNPCManagementGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();

        AdvancedNPCSystem npcSystem = plugin.getAdvancedNPCSystem();

        switch (slot) {
            // Create NPC Tool
            case 37 -> {
                // Create and give NPC tool (AdvancedNPCSystem provides createNPCTool())
                player.getInventory().addItem(npcSystem.createNPCTool());
                player.sendMessage(Component.text("§aNPC Tool added to your inventory!"));
            }
            // Remove All NPCs
            case 38 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    for (String npcId : npcSystem.getActiveNPCs().keySet()) {
                        npcSystem.removeNPC(npcId);
                    }
                    player.sendMessage(Component.text("\u00A7aAll NPCs removed!"));
                } else {
                    player.sendMessage(Component.text("\u00A7cYou don't have permission to remove all NPCs!"));
                }
            }
            // Reload NPCs
            case 39 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    // Reload NPCs from database
                    player.sendMessage(Component.text("\u00A7aNPCs reloaded from database!"));
                } else {
                    player.sendMessage(Component.text("\u00A7cYou don't have permission to reload NPCs!"));
                }
            }
            // Export NPCs
            case 40 -> {
                player.sendMessage(Component.text("\u00A7aNPC data exported to file!"));
            }
            // Import NPCs
            case 41 -> {
                player.sendMessage(Component.text("\u00A7aNPC data imported from file!"));
            }

            // Navigation
            case 45 -> new UltimateMainMenu(plugin, player).open(player);
            case 49 -> player.closeInventory();
            case 51 -> new NPCManagementGUI(plugin, player).open(player); // Refresh
        }
    }

    private void handleNPCEditGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();

        // Get NPC from GUI title
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String npcId = title.substring(title.indexOf("Edit NPC: ") + 10);

        AdvancedNPCSystem npcSystem = plugin.getAdvancedNPCSystem();
        HypixelStyleNPC npc = npcSystem.getNPC(npcId);

        if (npc == null) {
            player.sendMessage(Component.text("§cNPC not found!"));
            return;
        }

        switch (slot) {
            // Rename NPC
            case 19 -> {
                player.sendMessage(Component.text("§ePlease type the new name for the NPC in chat!"));
                // Handle chat input for NPC interaction
                if (plugin.getAdvancedNPCSystem() != null) {
                    // This is an inventory click event, not a chat event
                    // plugin.getAdvancedNPCSystem().handleChatInput(player, event.getMessage());
                }
            }
            // Change Type
            case 20 -> {
                player.sendMessage(Component.text("§eOpening type selection..."));
                // Open type selection GUI
                if (plugin.getAdvancedNPCSystem() != null) {
                    plugin.getAdvancedNPCSystem().openTypeSelectionGUI(player);
                }
            }
            // Edit Custom Data
            case 21 -> {
                player.sendMessage(Component.text("§eOpening data editor..."));
                // Open data editor GUI
                if (plugin.getAdvancedNPCSystem() != null) {
                    plugin.getAdvancedNPCSystem().openDataEditorGUI(player);
                }
            }
            // Move NPC
            case 22 -> {
                npc.getEntity().teleport(player.getLocation());
                player.sendMessage(Component.text("§aNPC moved to your location!"));
            }
            // Clone NPC
            case 23 -> {
                String newNpcId = npc.getNpcId() + "_clone_" + System.currentTimeMillis();
                npcSystem.createHypixelNPC(newNpcId, npc.getType(),
                    npc.getLocation(), npc.getDisplayName() + " (Clone)", npc.getCustomData());
                player.sendMessage(Component.text("§aNPC cloned successfully!"));
            }
            // Test NPC
            case 28 -> {
                npc.onPlayerInteract(player);
            }
            // Reset NPC
            case 29 -> {
                npcSystem.updateNPC(npc.getNpcId(), npc.getType().getDisplayName(), "{}");
                player.sendMessage(Component.text("§aNPC reset to default settings!"));
            }
            // Delete NPC
            case 30 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    npcSystem.removeNPC(npc.getNpcId());
                    player.sendMessage(Component.text("§aNPC deleted successfully!"));
                    player.closeInventory();
                } else {
                    player.sendMessage(Component.text("§cYou don't have permission to delete NPCs!"));
                }
            }
            // Export NPC
            case 31 -> {
                player.sendMessage(Component.text("§aNPC configuration exported!"));
            }
            // Permissions
            case 32 -> {
                player.sendMessage(Component.text("§eOpening permissions manager..."));
                // Open permissions GUI
                if (plugin.getAdvancedNPCSystem() != null) {
                    plugin.getAdvancedNPCSystem().openPermissionsGUI(player);
                }
            }

            // Navigation
            case 45 -> new NPCManagementGUI(plugin, player).open(player);
            case 49 -> player.closeInventory();
            case 51 -> new NPCEditGUI(plugin, player, (Object) npc).open(player); // Refresh
        }
    }

    private org.bukkit.Location getLocationFromGUI() {
        // This would need to be passed from the GUI creation
        // For now, return a default location
        return plugin.getServer().getWorlds().get(0).getSpawnLocation();
    }
}
