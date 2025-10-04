package de.noctivag.skyblock.rewards.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.rewards.DailyReward;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyRewardGUIListener implements Listener {
    private final SkyblockPlugin plugin;
    private final Map<Player, RewardEditSession> editingSessions;

    public DailyRewardGUIListener(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.editingSessions = new HashMap<>();
    }

    // Returns true if the player is currently editing rewards via the GUI
    public boolean isEditingRewards(Player player) {
        return editingSessions.containsKey(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!title.contains("§6Tägliche Belohnungen") && !title.contains("§cBelohnungen Verwalten")) {
            return;
        }

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        // Hauptmenü-Interaktionen
        if (title.equals("§8» §6Tägliche Belohnungen §8«")) {
            handleMainMenuClick(player, clickedItem);
        }
        // Admin-Menü-Interaktionen
        else if (title.equals("§8» §cBelohnungen Verwalten §8«")) {
            handleAdminMenuClick(player, clickedItem, event.isRightClick());
        }
    }

    private void handleMainMenuClick(Player player, ItemStack clickedItem) {
        if (clickedItem.getType() == Material.EXPERIENCE_BOTTLE) {
            if (plugin.getDailyRewardManager().canClaimReward(player)) {
                plugin.getDailyRewardManager().claimReward(player);
                new DailyRewardGUI(plugin).openRewardGUI(player); // Aktualisiere GUI
            }
        }
        else if (clickedItem.getType() == Material.COMMAND_BLOCK && player.hasPermission("plugin.admin")) {
            new DailyRewardGUI(plugin).openAdminGUI(player);
        }
    }

    private void handleAdminMenuClick(Player player, ItemStack clickedItem, boolean isRightClick) {
        String itemName = clickedItem.getItemMeta().getDisplayName();

        if (itemName.contains("Tag")) {
            int day = Integer.parseInt(itemName.replaceAll("\\D+", ""));

            if (isRightClick) {
                // Lösche Belohnung
                plugin.getDailyRewardManager().removeReward(day);
                player.sendMessage("§aBelohnung für Tag " + day + " wurde entfernt!");
            } else {
                // Bearbeite Belohnung
                openRewardEditor(player, day);
            }
        }
        else if (itemName.contains("Belohnung hinzufügen")) {
            openRewardEditor(player, findNextAvailableDay());
        }
        else if (itemName.equals("§c« Zurück")) {
            new DailyRewardGUI(plugin).openRewardGUI(player);
        }
    }

    private void openRewardEditor(Player player, int day) {
        RewardEditSession session = new RewardEditSession(day);
        editingSessions.put(player, session);

        player.closeInventory();
        player.sendMessage("§6=== Belohnung für Tag " + day + " bearbeiten ===");
        player.sendMessage("§7Gib die Belohnungen im Format ein:");
        player.sendMessage("§eTYP MENGE WERT");
        player.sendMessage("§7Beispiele:");
        player.sendMessage("§eCOINS 100");
        player.sendMessage("§eEXP 500");
        player.sendMessage("§eITEM DIAMOND 5");
        player.sendMessage("§eKIT vip");
        player.sendMessage("§7Oder §ccancel §7zum Abbrechen, §afertig §7zum Speichern");
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

         // Wenn der Spieler im Edit-Modus ist, nicht aufräumen
         if (editingSessions.containsKey(player)) return;

        // Ansonsten GUI aufräumen
        if (event.getView().getTitle().contains("Belohnungen")) {
            editingSessions.remove(player);
        }
    }

    // New: handle chat input for reward editing sessions
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!editingSessions.containsKey(player)) return;

        event.setCancelled(true); // swallow chat while editing
        String message = event.getMessage();
        // delegate to the existing handler on the main thread
        getServerScheduler().runTask(plugin, () -> handleChatInput(player, message));
    }

    // Helper to schedule tasks on main thread (avoids adding Bukkit imports here)
    private org.bukkit.scheduler.BukkitScheduler getServerScheduler() {
        return plugin.getServer().getScheduler();
    }

    public void handleChatInput(Player player, String message) {
        RewardEditSession session = editingSessions.get(player);
        if (session == null) return;

        if (message.equalsIgnoreCase("cancel")) {
            editingSessions.remove(player);
            player.sendMessage("§cBearbeitung abgebrochen!");
            new DailyRewardGUI(plugin).openAdminGUI(player);
            return;
        }

        if (message.equalsIgnoreCase("fertig")) {
            if (session.rewards.isEmpty()) {
                player.sendMessage("§cDu musst mindestens eine Belohnung hinzufügen!");
                return;
            }

            // Erstelle neue Belohnung
            DailyReward reward = new DailyReward(
                session.day,
                session.rewards,
                session.day % 7 == 0, // Spezielle Belohnung jeden 7. Tag
                Material.CHEST // Standard-Icon
            );

            plugin.getDailyRewardManager().setReward(session.day, reward);
            editingSessions.remove(player);
            player.sendMessage("§aBelohnungen für Tag " + session.day + " gespeichert!");
            new DailyRewardGUI(plugin).openAdminGUI(player);
            return;
        }

        // Verarbeite Belohnungs-Input
        try {
            String[] parts = message.split(" ");
            DailyReward.RewardType type = DailyReward.RewardType.valueOf(parts[0].toUpperCase());

            switch (type) {
                case COINS:
                case EXP:
                    if (parts.length != 2) throw new IllegalArgumentException();
                    int amount = Integer.parseInt(parts[1]);
                    session.rewards.add(new DailyReward.RewardItem(type, amount, 1));
                    break;

                case ITEM:
                    if (parts.length != 3) throw new IllegalArgumentException();
                    org.bukkit.Material material = org.bukkit.Material.valueOf(parts[1].toUpperCase());
                    int itemAmount = Integer.parseInt(parts[2]);
                    session.rewards.add(new DailyReward.RewardItem(type, new ItemStack(material), itemAmount));
                    break;

                case KIT:
                case PERMISSION:
                    if (parts.length != 2) throw new IllegalArgumentException();
                    session.rewards.add(new DailyReward.RewardItem(type, parts[1], 1));
                    break;
            }

            player.sendMessage("§aBelohnung hinzugefügt! Gib weitere ein oder schreibe §efertig§a.");

        } catch (Exception e) {
            player.sendMessage("§cUngültiges Format! Beispiele:");
            player.sendMessage("§eCOINS 100");
            player.sendMessage("§eEXP 500");
            player.sendMessage("§eITEM DIAMOND 5");
            player.sendMessage("§eKIT vip");
        }
    }

    private int findNextAvailableDay() {
        for (int i = 1; i <= 7; i++) {
            if (plugin.getDailyRewardManager().getReward(i) == null) {
                return i;
            }
        }
        return 1; // Fallback auf Tag 1
    }

    public void handleChatInputPublic(Player player, String message) {
        handleChatInput(player, message);
    }

    private static class RewardEditSession {
        final int day;
        final List<DailyReward.RewardItem> rewards;

        RewardEditSession(int day) {
            this.day = day;
            this.rewards = new ArrayList<>();
        }
    }
}
