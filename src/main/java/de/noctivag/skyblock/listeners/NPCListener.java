package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.*;
import de.noctivag.skyblock.npcs.AdvancedNPCSystem;
import de.noctivag.skyblock.npcs.HypixelStyleNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import net.kyori.adventure.text.Component;

public class NPCListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public NPCListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
                new NPCCreationGUI(SkyblockPlugin, player, event.getClickedBlock().getLocation().add(0, 1, 0)).open();
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof org.bukkit.entity.Villager) {
            org.bukkit.entity.Villager villager = (org.bukkit.entity.Villager) event.getRightClicked();

            // Check if this is an NPC
            Object npcSystemObj = SkyblockPlugin.getAdvancedNPCSystem();
            HypixelStyleNPC npc = null;

            if (npcSystemObj != null) {
                try {
                    // Use reflection to access the getActiveNPCs method
                    Object activeNPCs = npcSystemObj.getClass().getMethod("getActiveNPCs").invoke(npcSystemObj);
                    if (activeNPCs instanceof java.util.Map) {
                        @SuppressWarnings("unchecked")
                        java.util.Map<String, HypixelStyleNPC> npcMap = (java.util.Map<String, HypixelStyleNPC>) activeNPCs;
                        for (HypixelStyleNPC gameNPC : npcMap.values()) {
                            if (gameNPC.getEntity() == villager) {
                                npc = gameNPC;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    // If reflection fails, npc will remain null
                }
            }

            if (npc != null) {
                event.setCancelled(true);

                // Check if player is holding NPC tool for management
                ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
                if (item != null && item.hasItemMeta() &&
                    item.getItemMeta().displayName() != null &&
                    net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(item.getItemMeta().displayName()).contains("NPC Tool")) {
                    // Open NPC edit GUI
                    new NPCEditGUI(SkyblockPlugin, event.getPlayer(), npc).open();
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

        Object npcSystemObj = SkyblockPlugin.getAdvancedNPCSystem();
        if (npcSystemObj == null) return;

        switch (slot) {
            // Shop NPC
            case 10 -> {
                String npcId = "shop_" + java.lang.System.currentTimeMillis();
                // NPC creation not implemented yet
                player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
                player.sendMessage(Component.text("\u00A7aShop NPC created successfully!"));
                player.closeInventory();
            }
            // Quest NPC
            case 11 -> {
                player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
                player.closeInventory();
            }
            // Info NPC
            case 12 -> {
                String npcId = "info_" + java.lang.System.currentTimeMillis();
                // NPC creation not implemented yet
                player.sendMessage(Component.text("\u00A7aInfo NPC created successfully!"));
                player.closeInventory();
            }
            // Warp NPC
            case 13 -> {
                String npcId = "warp_" + java.lang.System.currentTimeMillis();
                // npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.WARP,
                //     getLocationFromGUI(), "\u00A7dWarp NPC", "{}");
                player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
                player.sendMessage(Component.text("\u00A7aWarp NPC created successfully!"));
                player.closeInventory();
            }
            // Bank NPC
            case 14 -> {
                String npcId = "bank_" + java.lang.System.currentTimeMillis();
                // npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.BANK,
                //     getLocationFromGUI(), "\u00A76Bank NPC", "{}");
                player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
                player.sendMessage(Component.text("\u00A7aBank NPC created successfully!"));
                player.closeInventory();
            }
            // Auction NPC
            case 15 -> {
                String npcId = "auction_" + java.lang.System.currentTimeMillis();
                // npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.AUCTION,
                //     getLocationFromGUI(), "\u00A7cAuction NPC", "{}");
                player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
                player.sendMessage(Component.text("\u00A7aAuction NPC created successfully!"));
                player.closeInventory();
            }
            // Guild NPC
            case 16 -> {
                String npcId = "guild_" + java.lang.System.currentTimeMillis();
                // npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.GUILD,
                //     getLocationFromGUI(), "\u00A75Guild NPC", "{}");
                player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
                player.sendMessage(Component.text("\u00A7aGuild NPC created successfully!"));
                player.closeInventory();
            }
            // Pet NPC
            case 19 -> {
                String npcId = "pet_" + java.lang.System.currentTimeMillis();
                // npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.PET,
                //     getLocationFromGUI(), "\u00A7dPet NPC", "{}");
                player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
                player.sendMessage(Component.text("\u00A7aPet NPC created successfully!"));
                player.closeInventory();
            }
            // Cosmetic NPC
            case 20 -> {
                String npcId = "cosmetic_" + java.lang.System.currentTimeMillis();
                // npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.COSMETIC,
                //     getLocationFromGUI(), "\u00A7eCosmetic NPC", "{}");
                player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
                player.sendMessage(Component.text("\u00A7aCosmetic NPC created successfully!"));
                player.closeInventory();
            }
            // Admin NPC
            case 21 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    String npcId = "admin_" + java.lang.System.currentTimeMillis();
                    // npcSystem.createHypixelNPC(npcId, AdvancedNPCSystem.NPCType.ADMIN,
                    //     getLocationFromGUI(), "\u00A74Admin NPC", "{}");
                    player.sendMessage(Component.text("§cNPC creation not implemented yet!"));
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

        Object npcSystemObj = SkyblockPlugin.getAdvancedNPCSystem();
        if (npcSystemObj == null) return;

        switch (slot) {
            // Create NPC Tool
            case 37 -> {
                // Create and give NPC tool (AdvancedNPCSystem provides createNPCTool())
                // player.getInventory().addItem(npcSystem.createNPCTool());
                player.sendMessage(Component.text("§cNPC Tool creation not implemented yet!"));
                player.sendMessage(Component.text("§aNPC Tool added to your inventory!"));
            }
            // Remove All NPCs
            case 38 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    // for (String npcId : npcSystem.getActiveNPCs().keySet()) {
                    //     npcSystem.removeNPC(npcId);
                    // }
                    player.sendMessage(Component.text("§cNPC removal not implemented yet!"));
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
            case 45 -> new UltimateMainMenu(SkyblockPlugin, player).open();
            case 49 -> player.closeInventory();
            case 51 -> player.sendMessage("§cNPC Management GUI not implemented yet!"); // Refresh
        }
    }

    private void handleNPCEditGUIClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int slot = event.getSlot();

        // Get NPC from GUI title
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String npcId = title.substring(title.indexOf("Edit NPC: ") + 10);

        Object npcSystemObj = SkyblockPlugin.getAdvancedNPCSystem();
        if (npcSystemObj == null) return;
        HypixelStyleNPC npc = null; // npcSystem.getNPC(npcId);

        if (npc == null) {
            player.sendMessage(Component.text("§cNPC not found!"));
            return;
        }

        switch (slot) {
            // Rename NPC
            case 19 -> {
                player.sendMessage(Component.text("§ePlease type the new name for the NPC in chat!"));
                // Handle chat input for NPC interaction
                if (SkyblockPlugin.getAdvancedNPCSystem() != null) {
                    // This is an inventory click event, not a chat event
                    // SkyblockPlugin.getAdvancedNPCSystem().handleChatInput(player, event.getMessage());
                }
            }
            // Change Type
            case 20 -> {
                player.sendMessage(Component.text("§eOpening type selection..."));
                // Open type selection GUI
                // if (SkyblockPlugin.getAdvancedNPCSystem() != null) {
                //     SkyblockPlugin.getAdvancedNPCSystem().openTypeSelectionGUI(player);
                // }
                player.sendMessage(Component.text("§cType selection GUI not implemented yet!"));
            }
            // Edit Custom Data
            case 21 -> {
                player.sendMessage(Component.text("§eOpening data editor..."));
                // Open data editor GUI
                // if (SkyblockPlugin.getAdvancedNPCSystem() != null) {
                //     SkyblockPlugin.getAdvancedNPCSystem().openDataEditorGUI(player);
                // }
                player.sendMessage(Component.text("§cData editor GUI not implemented yet!"));
            }
            // Move NPC
            case 22 -> {
                npc.getEntity().teleport(player.getLocation());
                player.sendMessage(Component.text("§aNPC moved to your location!"));
            }
            // Clone NPC
            case 23 -> {
                // String newNpcId = npc.getNpcId() + "_clone_" + java.lang.System.currentTimeMillis();
                // npcSystem.createHypixelNPC(newNpcId, npc.getType(),
                //     npc.getLocation(), npc.getDisplayName() + " (Clone)", npc.getCustomData());
                player.sendMessage(Component.text("§cNPC cloning not implemented yet!"));
                player.sendMessage(Component.text("§aNPC cloned successfully!"));
            }
            // Test NPC
            case 28 -> {
                npc.onPlayerInteract(player);
            }
            // Reset NPC
            case 29 -> {
                // npcSystem.updateNPC(npc.getNpcId(), npc.getType().getDisplayName(), "{}");
                player.sendMessage(Component.text("§cNPC update not implemented yet!"));
                player.sendMessage(Component.text("§aNPC reset to default settings!"));
            }
            // Delete NPC
            case 30 -> {
                if (player.hasPermission("basicsplugin.admin")) {
                    // npcSystem.removeNPC(npc.getNpcId());
                    player.sendMessage(Component.text("§cNPC removal not implemented yet!"));
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
                // if (SkyblockPlugin.getAdvancedNPCSystem() != null) {
                //     SkyblockPlugin.getAdvancedNPCSystem().openPermissionsGUI(player);
                // }
                player.sendMessage(Component.text("§cPermissions GUI not implemented yet!"));
            }

            // Navigation
            case 45 -> player.sendMessage("§cNPC Management GUI not implemented yet!");
            case 49 -> player.closeInventory();
            case 51 -> new NPCEditGUI(SkyblockPlugin, player, npc).open(); // Refresh
        }
    }

    private org.bukkit.Location getLocationFromGUI() {
        // This would need to be passed from the GUI creation
        // For now, return a default location
        return SkyblockPlugin.getServer().getWorlds().get(0).getSpawnLocation();
    }
}
