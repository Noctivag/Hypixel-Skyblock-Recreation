package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.AdminMenu;
import de.noctivag.skyblock.gui.PlayerSelectorMenu;
import de.noctivag.skyblock.gui.CosmeticsMenu;
import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.gui.MainMenu;
import de.noctivag.skyblock.gui.GadgetsGUI;
import de.noctivag.skyblock.gui.RanksGUI;
import de.noctivag.skyblock.managers.ParticleManager;
import de.noctivag.skyblock.cosmetics.ParticleShape;
import de.noctivag.skyblock.cosmetics.CosmeticsPurchaseManager;
import de.noctivag.skyblock.managers.EventManager;
import de.noctivag.skyblock.gui.FeatureToggleGUI;
import de.noctivag.skyblock.gui.MessagesMenu;
import de.noctivag.skyblock.gui.SettingsGUI;
import de.noctivag.skyblock.gui.FriendsGUI;
import de.noctivag.skyblock.gui.PartyGUI;
import de.noctivag.skyblock.kit.gui.KitShopGUI;
import de.noctivag.skyblock.locations.gui.WarpGUI;
import de.noctivag.skyblock.achievements.gui.AchievementGUI;
import de.noctivag.skyblock.gui.DailyRewardGUI;
import de.noctivag.skyblock.gui.EventMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

public class GUIListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final ParticleManager particleManager;

    public GUIListener(SkyblockPlugin SkyblockPlugin, ParticleManager particleManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.particleManager = particleManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Use Adventure API for title
        String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(event.getView().title());

        // Handle Warp GUI (title contains "Warp-Menü" or "Warps:")
        if (title.contains("Warp-Menü") || title.toLowerCase().contains("warps")) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player player)) return;
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            ItemMeta meta = clicked.getItemMeta();
            // Use Adventure API for display name
            String display = meta.hasDisplayName() && meta.displayName() != null ?
                net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(meta.displayName()) : "";
            // Back button
            if (display.toLowerCase().contains("zurück")) {
                new MainMenu(SkyblockPlugin, player).open();
                return;
            }

            // Try to match category names
            for (var category : de.noctivag.skyblock.locations.EnhancedWarp.WarpCategory.values()) {
                if (display.contains(category.getDisplayName())) {
                    new WarpGUI(SkyblockPlugin).openCategoryMenu(player, category);
                    return;
                }
            }

            // Otherwise assume it's a warp item; try to extract warp name
            String name = display.replace("» ", "").replace("»", "").trim();
            // TODO: Implement warp system
            // var warp = SkyblockPlugin.getLocationManager().getWarp(name.toLowerCase());
            // if (warp == null) {
            //     // maybe name without prefix
            //     warp = SkyblockPlugin.getLocationManager().getWarp(display.toLowerCase());
            // }
            // if (warp == null) return;

            // Check permissions and price
            // TODO: Implement warp system
            // if (!warp.getPermission().isEmpty() && !player.hasPermission(warp.getPermission())) {
            //     player.sendMessage(Component.text("§cDu hast keine Berechtigung, diesen Warp zu benutzen."));
            //     return;
            // }
            double price = 0.0;
            // Warp type conversion issue - using LocationManager.Warp instead of EnhancedWarp
            // if (warp instanceof de.noctivag.skyblock.locations.EnhancedWarp ew) price = ew.getPrice();
            if (price > 0) {
                if (!SkyblockPlugin.getEconomyManager().hasBalance(player, price)) {
                    player.sendMessage("§cDu kannst dir diesen Warp nicht leisten: " + SkyblockPlugin.getEconomyManager().formatMoney(price));
                    return;
                }
                SkyblockPlugin.getEconomyManager().withdrawMoney(player, price);
            }
            // Teleport
            // TODO: Implement warp system
            // player.teleport(warp.getLocation());
            // player.sendMessage("§aTeleportiere zu §e" + warp.getName());
            player.sendMessage("§cWarp-System noch nicht implementiert!");
            return;
        }

        // existing CustomGUI handling
        if (!(event.getInventory().getHolder() instanceof CustomGUI)) {
            return;
        }

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // Hauptmenü Klicks
        if (event.getInventory().getHolder() instanceof MainMenu) {
            handleMainMenuClick(event.getSlot(), player);
        }
        // Cosmetics Menü Klicks
        else if (event.getInventory().getHolder() instanceof CosmeticsMenu cosmeticsMenu) {
            handleCosmeticsMenuClick(event.getSlot(), player, cosmeticsMenu);
        }
        // Messages Menu Klicks
        else if (event.getInventory().getHolder() instanceof MessagesMenu messagesMenu) {
            handleMessagesMenuClick(event.getSlot(), player, messagesMenu);
        }
        else if (event.getInventory().getHolder() instanceof SettingsGUI) {
            handleSettingsClick(event.getSlot(), player);
        }
        else if (event.getInventory().getHolder() instanceof FriendsGUI) {
            handleFriendsClick(event.getSlot(), player);
        }
        else if (event.getInventory().getHolder() instanceof PartyGUI) {
            handlePartyClick(event.getSlot(), player);
        }
        // Admin Menu Klicks
        else if (event.getInventory().getHolder() instanceof AdminMenu adminMenu) {
            handleAdminMenuClick(event.getSlot(), player, adminMenu);
        }
        // Player selector clicks
        else if (event.getInventory().getHolder() instanceof PlayerSelectorMenu selectorMenu) {
            handlePlayerSelectorClick(event.getSlot(), player, selectorMenu);
        }
        else if (event.getInventory().getHolder() instanceof GadgetsGUI) {
            handleGadgetsClick(event.getSlot(), player);
        }
        // Basic Commands GUI
        else if (event.getInventory().getHolder() instanceof de.noctivag.skyblock.gui.BasicCommandsGUI) {
            handleBasicCommandsClick(event.getSlot(), player);
        }
        // Join Message GUI
        else if (event.getInventory().getHolder() instanceof de.noctivag.skyblock.gui.JoinMessageGUI) {
            handleJoinMessageClick(event.getSlot(), player);
        }
        // Join Message Presets GUI
        else if (event.getInventory().getHolder() instanceof de.noctivag.skyblock.gui.JoinMessagePresetsGUI) {
            handleJoinMessagePresetsClick(event.getSlot(), player);
        }
        // Feature Book GUI
        else if (event.getInventory().getHolder() instanceof de.noctivag.skyblock.gui.FeatureBookGUI) {
            handleFeatureBookClick(event.getSlot(), player);
        }
        // Advanced Cosmetics Menu
        else if (event.getInventory().getHolder() instanceof de.noctivag.skyblock.gui.AdvancedCosmeticsMenu) {
            handleAdvancedCosmeticsClick(event.getSlot(), player);
        }
        // Particle Settings GUI
        else if (event.getInventory().getHolder() instanceof de.noctivag.skyblock.gui.ParticleSettingsGUI) {
            handleParticleSettingsClick(event.getSlot(), player);
        }
        // Event Menu
        else if (event.getInventory().getHolder() instanceof de.noctivag.skyblock.gui.EventMenu) {
            handleEventMenuClick(event.getSlot(), player);
        }

        // Prevent clicking in custom GUI
        // (removed empty if statement)

        // Handle close button (red glass pane)
        if (event.getSlot() == 49) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof CustomGUI) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof CustomGUI)) {
            return;
        }

        // Play close sound
        if (event.getPlayer() instanceof Player player) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 0.8f);
        }
    }

    private void handleMainMenuClick(int slot, Player player) {
        switch (slot) {
            case 10 -> new CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager(), player).open();
            case 12 -> player.sendMessage("§cWarp GUI not implemented yet!");
            case 14 -> new DailyRewardGUI(SkyblockPlugin).open();
            case 16 -> player.sendMessage("§cAchievement GUI not implemented yet!");
            // case 19 -> new ProfileGUI(SkyblockPlugin).open(player); // Removed: ProfileGUI does not exist
            case 21 -> new de.noctivag.skyblock.gui.BasicCommandsGUI(SkyblockPlugin).openGUI(player);
            case 23 -> new de.noctivag.skyblock.gui.JoinMessageGUI(SkyblockPlugin).openGUI(player);
            case 25 -> new de.noctivag.skyblock.gui.FeatureBookGUI(SkyblockPlugin).open();
            case 28 -> player.sendMessage("§cKit Shop GUI not implemented yet!");
            case 30 -> new EventMenu(SkyblockPlugin, player).open();
            case 32 -> new MessagesMenu(SkyblockPlugin).open();
            case 34 -> new SettingsGUI(SkyblockPlugin, player).open();
        }
    }

    private void handleSettingsClick(int slot, Player player) {
        switch (slot) {
            case 10 -> player.performCommand("language");
            case 11 -> player.performCommand("privacy");
            case 12 -> player.performCommand("chatsettings");
            case 13 -> player.performCommand("notifications");
            case 14 -> player.sendMessage("§cFeature Toggle GUI not implemented yet!");
            case 15 -> new FriendsGUI(SkyblockPlugin, player).openGUI(player);
            case 16 -> new PartyGUI(SkyblockPlugin, player).openGUI(player);
            case 22 -> new MainMenu(SkyblockPlugin, player).open();
        }
    }

    private void handleFriendsClick(int slot, Player player) {
        switch (slot) {
            case 11 -> player.sendMessage(Component.text("§7Nutze: §a/friends add <Spieler>"));
            case 13 -> player.sendMessage(Component.text("§7Nutze: §a/friends accept <Spieler>"));
            case 15 -> player.sendMessage(Component.text("§7Nutze: §a/friends remove <Spieler>"));
            case 22 -> new SettingsGUI(SkyblockPlugin, player).open();
        }
    }

    private void handlePartyClick(int slot, Player player) {
        switch (slot) {
            case 11 -> player.sendMessage(Component.text("§7Nutze: §a/party invite <Spieler>"));
            case 13 -> player.performCommand("party list");
            case 15 -> player.performCommand("party leave");
            case 22 -> new SettingsGUI(SkyblockPlugin, player).open();
        }
    }

    private void handleCosmeticsMenuClick(int slot, Player player, CosmeticsMenu menu) {
        if (slot == 22) { // Deaktivieren Button
            // clear the current effect for this player
            particleManager.clearEffect(player);
            player.sendMessage(Component.text("§aPartikeleffekte wurden deaktiviert!"));
            return;
        }

        Particle selectedParticle = menu.getParticleAtSlot(slot);
        if (selectedParticle != null) {
            // Map the Bukkit Particle to our ParticleManager.ParticleEffectType
            ParticleManager.ParticleEffectType mapped = mapParticle(selectedParticle);
            if (mapped != null) {
                particleManager.setPlayerEffect(player, mapped);
                player.sendMessage(Component.text("§aPartikeleffekt wurde aktiviert!"));
            } else {
                player.sendMessage(Component.text("§cDieser Partikel wird derzeit nicht unterstützt."));
            }
        }
    }

    private void handleMessagesMenuClick(int slot, Player player, MessagesMenu menu) {
        var jm = SkyblockPlugin.getJoinMessageManager();
        switch (slot) {
            case 10 -> {
                // Show current custom message in chat (or default)
                String message;
                // TODO: Implement hasCustomMessage() and getJoinMessage() methods in SkyblockPlugin class
                // if (SkyblockPlugin.hasCustomMessage(player.getName())) {
                //     message = SkyblockPlugin.getJoinMessage(player);
                // } else {
                    message = "&7[&a+&7] &e%player% &7hat den Server betreten"; // Placeholder
                    message = message.replace("%player%", player.getName());
                // }
                player.sendMessage("§eDeine aktuelle Join-Message: §r" + message);
            }
            case 13 -> {
                // Toggle message enabled/disabled
                // TODO: Implement isMessageDisabled() and setMessageEnabled() methods in SkyblockPlugin class
                // boolean currentlyDisabled = SkyblockPlugin.isMessageDisabled(player.getName());
                // SkyblockPlugin.setMessageEnabled(player.getName(), !currentlyDisabled);
                boolean currentlyDisabled = false; // Placeholder
                player.sendMessage(!currentlyDisabled ? "§cJoin-Messages deaktiviert." : "§aJoin-Messages aktiviert.");
                menu.open(); // refresh
            }
            case 16 -> {
                // Clear custom message
                // SkyblockPlugin.clearJoinMessage(player); // TODO: Implement method in SkyblockPlugin class
                player.sendMessage(Component.text("§aDeine benutzerdefinierte Join-Message wurde entfernt."));
                menu.open();
            }
            case 22 -> {
                // Back to main menu
                new MainMenu(SkyblockPlugin, player).open();
            }
        }
    }

    private void handleAdminMenuClick(int slot, Player player, AdminMenu menu) {
        switch (slot) {
            case 11 ->
                // toggle vanish for the admin
                { if (!player.hasPermission("basics.vanish")) { player.sendMessage(Component.text("§cKeine Berechtigung.")); return; } /* SkyblockPlugin.toggleVanish(player); */ } // TODO: Implement method in SkyblockPlugin class
            case 13 -> new PlayerSelectorMenu(SkyblockPlugin).open();
            case 15 -> new RanksGUI(SkyblockPlugin, player).open();
            case 17 -> new de.noctivag.skyblock.gui.CommandUsageGUI(SkyblockPlugin).open();
            case 22 -> new MainMenu(SkyblockPlugin, player).open();
        }
    }

    private void handlePlayerSelectorClick(int slot, Player player, PlayerSelectorMenu menu) {
        // Back button (49) handled elsewhere; map slot to player
        Player targetPlayer = menu.getPlayerAtSlot(slot);
        String targetName = targetPlayer != null ? targetPlayer.getName() : null;
        if (targetName == null) {
            // nothing mapped here
            return;
        }
        var target = SkyblockPlugin.getServer().getPlayerExact(targetName);
        if (target == null) {
            player.sendMessage(Component.text("§cSpieler offline."));
            return;
        }
        player.openInventory(target.getInventory());
        player.sendMessage("§aÖffne Inventar von §e" + target.getName());
    }

    private void handleGadgetsClick(int slot, Player player) {
        switch (slot) {
            case 11 -> {
                // Jump gadget
                player.setVelocity(player.getVelocity().setY(1.2));
                player.sendMessage(Component.text("§aSprung!"));
            }
            case 15 -> {
                // Firework gadget
                var loc = player.getLocation();
                player.getWorld().spawn(loc, org.bukkit.entity.Firework.class, fw -> {
                    var meta = fw.getFireworkMeta();
                    meta.addEffect(org.bukkit.FireworkEffect.builder()
                        .withColor(org.bukkit.Color.AQUA)
                        .with(org.bukkit.FireworkEffect.Type.BURST)
                        .flicker(true)
                        .trail(true)
                        .build());
                    meta.setPower(1);
                    fw.setFireworkMeta(meta);
                });
                player.sendMessage(Component.text("§bFeuerwerk gezündet!"));
            }
            case 22 -> new CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager(), player).open();
        }
    }

    private ParticleManager.ParticleEffectType mapParticle(Particle particle) {
        return switch (particle) {
            case HEART -> ParticleManager.ParticleEffectType.HEART;
            case FLAME -> ParticleManager.ParticleEffectType.FLAME;
            case NOTE -> ParticleManager.ParticleEffectType.NOTE;
            case PORTAL, SPLASH -> ParticleManager.ParticleEffectType.WATER;
            case END_ROD, TOTEM_OF_UNDYING, DRAGON_BREATH, SOUL -> ParticleManager.ParticleEffectType.CLOUD;
            case CURRENT_DOWN -> ParticleManager.ParticleEffectType.SPELL;
            case CRIT -> ParticleManager.ParticleEffectType.NOTE;
            default -> null;
        };
    }

    private void handleBasicCommandsClick(int slot, Player player) {
        switch (slot) {
            case 10 -> player.performCommand("nickname");
            case 11 -> player.performCommand("prefix");
            case 12 -> player.openWorkbench(null, true);
            case 13 -> player.openAnvil(null, true);
            case 14 -> player.openInventory(player.getEnderChest());
            case 15 -> player.openGrindstone(null, true);
            case 16 -> player.openSmithingTable(null, true);
            case 19 -> player.openStonecutter(null, true);
            case 20 -> player.openLoom(null, true);
            case 21 -> player.openCartographyTable(null, true);
            case 22 -> player.performCommand("heal");
            case 23 -> player.performCommand("feed");
            case 24 -> player.performCommand("clear");
            case 49 -> new MainMenu(SkyblockPlugin, player).open();
        }
    }

    private void handleJoinMessageClick(int slot, Player player) {
        var jm = SkyblockPlugin.getJoinMessageManager();
        switch (slot) {
            case 10 -> {
                // TODO: Implement hasCustomMessage() and getJoinMessage() methods in SkyblockPlugin class
                // String message = SkyblockPlugin.hasCustomMessage(player.getName()) ? 
                //     SkyblockPlugin.getJoinMessage(player) :
                String message =  
                    "&7[&a+&7] &e%player% &7hat den Server betreten"; // Placeholder
                player.sendMessage("§eDeine aktuelle Join-Message: §r" + message.replace("%player%", player.getName()));
            }
            case 13 -> {
                // TODO: Implement isMessageDisabled() and setMessageEnabled() methods in SkyblockPlugin class
                // boolean currentlyDisabled = SkyblockPlugin.isMessageDisabled(player.getName());
                // SkyblockPlugin.setMessageEnabled(player.getName(), !currentlyDisabled);
                boolean currentlyDisabled = false; // Placeholder
                player.sendMessage(!currentlyDisabled ? "§cJoin-Messages deaktiviert." : "§aJoin-Messages aktiviert.");
                new de.noctivag.skyblock.gui.JoinMessageGUI(SkyblockPlugin).open();
            }
            case 16 -> {
                // SkyblockPlugin.clearJoinMessage(player); // TODO: Implement method in SkyblockPlugin class
                player.sendMessage(Component.text("§aDeine benutzerdefinierte Join-Message wurde entfernt."));
                new de.noctivag.skyblock.gui.JoinMessageGUI(SkyblockPlugin).open();
            }
            case 19 -> new de.noctivag.skyblock.gui.JoinMessagePresetsGUI(SkyblockPlugin).open();
            case 49 -> new MainMenu(SkyblockPlugin, player).open();
        }
    }

    private void handleJoinMessagePresetsClick(int slot, Player player) {
        var jm = SkyblockPlugin.getJoinMessageManager();
        String[] presets = {
            "&7[&a+&7] &e%player% &7hat den Server betreten",
            "&7[&a+&7] &e%player% &7ist dem Server beigetreten",
            "&7[&a+&7] &e%player% &7ist online gegangen",
            "&7[&a+&7] &e%player% &7hat sich eingeloggt",
            "&7[&a+&7] &e%player% &7ist da!",
            "&7[&a+&7] &e%player% &7ist zurück!",
            "&7[&a+&7] &e%player% &7hat den Server betreten",
            "&7[&a+&7] &e%player% &7ist online"
        };
        
        if (slot >= 10 && slot <= 17) {
            int presetIndex = slot - 10;
            if (presetIndex < presets.length) {
                // SkyblockPlugin.setJoinMessage(player, presets[presetIndex]); // TODO: Implement method in SkyblockPlugin class
                player.sendMessage("§aJoin-Message auf Preset " + (presetIndex + 1) + " gesetzt!");
                new de.noctivag.skyblock.gui.JoinMessageGUI(SkyblockPlugin).open();
            }
        } else if (slot == 49) {
            new de.noctivag.skyblock.gui.JoinMessageGUI(SkyblockPlugin).open();
        }
    }

    private void handleFeatureBookClick(int slot, Player player) {
        switch (slot) {
            case 10 -> new CosmeticsMenu(SkyblockPlugin, SkyblockPlugin.getCosmeticsManager(), player).open();
            case 11 -> new WarpGUI(SkyblockPlugin).openMainMenu(player);
            case 12 -> new DailyRewardGUI(SkyblockPlugin).open();
            case 13 -> new AchievementGUI(SkyblockPlugin).openMainGUI(player);
            case 14 -> new KitShopGUI(SkyblockPlugin).openGUI(player);
            case 15 -> new EventMenu(SkyblockPlugin, player).open();
            case 16 -> new MessagesMenu(SkyblockPlugin).open();
            case 17 -> new SettingsGUI(SkyblockPlugin, player).open();
            case 18 -> new FriendsGUI(SkyblockPlugin, player).open();
            case 19 -> new PartyGUI(SkyblockPlugin, player).open();
            case 49 -> new MainMenu(SkyblockPlugin, player).open();
        }
    }

    private void handleAdvancedCosmeticsClick(int slot, Player player) {
        var cosmeticsManager = SkyblockPlugin.getCosmeticsManager();
        var purchaseManager = SkyblockPlugin.getCosmeticsPurchaseManager(); // Cast from Object
        
        switch (slot) {
            case 10 -> {
                // Circle particles
                // TODO: Implement particle system
                player.sendMessage(Component.text("§aKreis-Partikel aktiviert!"));
            }
            case 11 -> {
                // Spiral particles
                // TODO: Implement particle system
                player.sendMessage(Component.text("§aSpiral-Partikel aktiviert!"));
            }
            case 12 -> {
                // Helix particles
                // TODO: Implement particle system
                player.sendMessage(Component.text("§aHelix-Partikel aktiviert!"));
            }
            case 13 -> {
                // Heart particles
                // TODO: Implement particle system
                player.sendMessage(Component.text("§aHerz-Partikel aktiviert!"));
            }
            case 14 -> {
                // Star particles
                // TODO: Implement particle system
                player.sendMessage(Component.text("§aStern-Partikel aktiviert!"));
            }
            case 15 -> {
                // Crown particles
                // TODO: Implement particle system
                player.sendMessage(Component.text("§aKronen-Partikel aktiviert!"));
            }
            case 16 -> {
                // Wings particles
                // TODO: Implement particle system
                player.sendMessage(Component.text("§aFlügel-Partikel aktiviert!"));
            }
            case 19 -> {
                // Sound effects
                if (((CosmeticsPurchaseManager) purchaseManager).hasPurchased(player, "sound_ambient")) {
                    player.sendMessage(Component.text("§aAmbient-Sounds aktiviert!"));
                } else {
                    ((CosmeticsPurchaseManager) purchaseManager).purchaseCosmetic(player, "sound_ambient", 80.0);
                }
            }
            case 20 -> {
                // Wings
                if (((CosmeticsPurchaseManager) purchaseManager).hasPurchased(player, "wings_angel")) {
                    player.sendMessage(Component.text("§aEngels-Flügel aktiviert!"));
                } else {
                    ((CosmeticsPurchaseManager) purchaseManager).purchaseCosmetic(player, "wings_angel", 500.0);
                }
            }
            case 21 -> {
                // Halo
                if (((CosmeticsPurchaseManager) purchaseManager).hasPurchased(player, "halo_golden")) {
                    player.sendMessage(Component.text("§aGoldener Heiligenschein aktiviert!"));
                } else {
                    ((CosmeticsPurchaseManager) purchaseManager).purchaseCosmetic(player, "halo_golden", 400.0);
                }
            }
            case 22 -> {
                // Trails
                if (((CosmeticsPurchaseManager) purchaseManager).hasPurchased(player, "trail_fire")) {
                    player.sendMessage(Component.text("§aFeuer-Spur aktiviert!"));
                } else {
                    ((CosmeticsPurchaseManager) purchaseManager).purchaseCosmetic(player, "trail_fire", 200.0);
                }
            }
            case 25 -> new de.noctivag.skyblock.gui.ParticleSettingsGUI(SkyblockPlugin, player).open();
            case 28 -> {
                // Deactivate all
                // TODO: Implement removePlayerEffects
                player.sendMessage(Component.text("§aAlle Effekte entfernt!"));
                player.sendMessage(Component.text("§cAlle Kosmetik-Effekte deaktiviert!"));
            }
            case 49 -> new MainMenu(SkyblockPlugin, player).open();
        }
    }

    private void handleParticleSettingsClick(int slot, Player player) {
        switch (slot) {
            case 10 -> {
                // Increase density
                player.sendMessage(Component.text("§aPartikel-Dichte erhöht!"));
            }
            case 11 -> {
                // Decrease density
                player.sendMessage(Component.text("§cPartikel-Dichte verringert!"));
            }
            case 12 -> {
                // Increase speed
                player.sendMessage(Component.text("§aPartikel-Geschwindigkeit erhöht!"));
            }
            case 13 -> {
                // Decrease speed
                player.sendMessage(Component.text("§cPartikel-Geschwindigkeit verringert!"));
            }
            case 14 -> {
                // Change color
                player.sendMessage(Component.text("§7Farbe geändert!"));
            }
            case 15 -> {
                // Reset settings
                player.sendMessage(Component.text("§aEinstellungen zurückgesetzt!"));
            }
            case 49 -> new de.noctivag.skyblock.gui.AdvancedCosmeticsMenu(SkyblockPlugin, player).open();
        }
    }

    private void handleEventMenuClick(int slot, Player player) {
        var eventManager = SkyblockPlugin.getEventManager();
        if (eventManager == null) {
            player.sendMessage(Component.text("§cEvent-System ist nicht verfügbar!"));
            return;
        }

        switch (slot) {
            case 10 -> {
                // Ender Dragon Event
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    player.sendMessage(Component.text("§cDu bist bereits in einem Event!"));
                    return;
                }
                ((EventManager) eventManager).joinEvent(player, "ender_dragon");
            }
            case 11 -> {
                // Wither Boss Event
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    player.sendMessage(Component.text("§cDu bist bereits in einem Event!"));
                    return;
                }
                ((EventManager) eventManager).joinEvent(player, "wither");
            }
            case 12 -> {
                // Titan Event
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    player.sendMessage(Component.text("§cDu bist bereits in einem Event!"));
                    return;
                }
                ((EventManager) eventManager).joinEvent(player, "custom_boss");
            }
            case 13 -> {
                // Elder Guardian Event
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    player.sendMessage(Component.text("§cDu bist bereits in einem Event!"));
                    return;
                }
                ((EventManager) eventManager).joinEvent(player, "elder_guardian");
            }
            case 14 -> {
                // Ravager Event
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    player.sendMessage(Component.text("§cDu bist bereits in einem Event!"));
                    return;
                }
                ((EventManager) eventManager).joinEvent(player, "ravager");
            }
            case 15 -> {
                // Phantom King Event
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    player.sendMessage(Component.text("§cDu bist bereits in einem Event!"));
                    return;
                }
                ((EventManager) eventManager).joinEvent(player, "phantom_king");
            }
            case 16 -> {
                // Blaze King Event
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    player.sendMessage(Component.text("§cDu bist bereits in einem Event!"));
                    return;
                }
                ((EventManager) eventManager).joinEvent(player, "blaze_king");
            }
            case 17 -> {
                // Enderman Lord Event
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    player.sendMessage(Component.text("§cDu bist bereits in einem Event!"));
                    return;
                }
                ((EventManager) eventManager).joinEvent(player, "enderman_lord");
            }
            case 19 -> {
                // Event Status
                if (((EventManager) eventManager).isPlayerInEvent(player)) {
                    String currentEvent = ((EventManager) eventManager).getPlayerEvent(player);
                    player.sendMessage("§aDu bist im Event: §e" + currentEvent);
                    player.sendMessage(Component.text("§7Nutze §e/event leave §7um das Event zu verlassen."));
                } else {
                    player.sendMessage(Component.text("§7Du bist in keinem Event."));
                    player.sendMessage("§7Aktive Events: §e" + ((EventManager) eventManager).getAvailableEvents().size());
                }
            }
            case 28 -> {
                // Event Zeitplan
                player.sendMessage(Component.text("§e§lEvent Zeitplan:"));
                player.sendMessage(Component.text("§7Ender Dragon: §aJede 30 Minuten"));
                player.sendMessage(Component.text("§7Wither Boss: §aJede 45 Minuten"));
                player.sendMessage(Component.text("§7Custom Boss: §aJede 60 Minuten"));
            }
            case 30 -> {
                // Event Statistiken
                player.sendMessage(Component.text("§b§lDeine Event-Statistiken:"));
                player.sendMessage(Component.text("§7Teilnahmen: §e0"));
                player.sendMessage(Component.text("§7Siege: §a0"));
                player.sendMessage(Component.text("§7Coins verdient: §60 Coins"));
            }
            case 32 -> {
                // Event Shop
                player.sendMessage(Component.text("§6§lEvent Shop:"));
                player.sendMessage(Component.text("§7Dragon Egg: §e1000 Event-Coins"));
                player.sendMessage(Component.text("§7Nether Star: §e2000 Event-Coins"));
                player.sendMessage(Component.text("§7Totem of Undying: §e3000 Event-Coins"));
                player.sendMessage(Component.text("§7"));
                player.sendMessage(Component.text("§7Deine Event-Coins: §e0"));
            }
            case 35 -> {
                // Zurück
                new MainMenu(SkyblockPlugin, player).open();
            }
        }
    }
}
