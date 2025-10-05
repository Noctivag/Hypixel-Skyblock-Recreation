package de.noctivag.skyblock.rewards.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.rewards.DailyReward;
import de.noctivag.skyblock.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DailyRewardGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private static final String MAIN_TITLE = "§8» §6Tägliche Belohnungen §8«";
    private static final String ADMIN_TITLE = "§8» §cBelohnungen Verwalten §8«";

    public DailyRewardGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    public void openRewardGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text(ColorUtils.parseColor(MAIN_TITLE)));
        decorateInventory(inv);

        // Streak-Information
        int currentStreak = SkyblockPlugin.getDailyRewardManager().getCurrentStreak(player);
        LocalDateTime lastClaim = SkyblockPlugin.getDailyRewardManager().getLastClaimTime(player);

        ItemStack streakItem = createGuiItem(
            Material.EXPERIENCE_BOTTLE,
            "§6» §eAktuelle Streak",
            Arrays.asList(
                "§7Streak: §e" + currentStreak + " Tage",
                "§7Letzte Belohnung: §e" + (lastClaim != null ?
                    lastClaim.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : "Noch nie"),
                "",
                SkyblockPlugin.getDailyRewardManager().canClaimReward(player) ?
                    "§aKlicke, um deine Belohnung abzuholen!" :
                    "§cDu hast deine Belohnung heute bereits abgeholt!"
            )
        );
        inv.setItem(4, streakItem);

        // Zeige die nächsten 7 Tage
        int startSlot = 19;
        for (int day = 1; day <= 7; day++) {
            DailyReward reward = SkyblockPlugin.getDailyRewardManager().getReward(day);
            if (reward != null) {
                ItemStack rewardItem = createRewardDisplay(reward, currentStreak % 7, day);
                inv.setItem(startSlot + (day - 1), rewardItem);
            }
        }

        // Admin-Button (nur für Admins sichtbar)
        if (player.hasPermission("SkyblockPlugin.admin")) {
            ItemStack adminItem = createGuiItem(
                Material.COMMAND_BLOCK,
                "§c» §4Belohnungen verwalten",
                Arrays.asList(
                    "§7Klicke, um die Belohnungen",
                    "§7zu bearbeiten und anzupassen."
                )
            );
            inv.setItem(49, adminItem);
        }

        player.openInventory(inv);
    }

    public void openAdminGUI(Player player) {
        if (!player.hasPermission("SkyblockPlugin.admin")) {
            player.sendMessage(Component.text("§cDazu hast du keine Berechtigung!"));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text(ColorUtils.parseColor(ADMIN_TITLE)));
        decorateInventory(inv);

        // Belohnungen bearbeiten
        int slot = 10;
        for (int day = 1; day <= 7; day++) {
            DailyReward reward = SkyblockPlugin.getDailyRewardManager().getReward(day);
            ItemStack rewardItem = createAdminRewardItem(day, reward);
            inv.setItem(slot++, rewardItem);
        }

        // Aktionen
        ItemStack addRewardItem = createGuiItem(
            Material.EMERALD,
            "§a» §2Belohnung hinzufügen",
            Arrays.asList(
                "§7Klicke, um eine neue",
                "§7Belohnung hinzuzufügen."
            )
        );
        inv.setItem(31, addRewardItem);

        // Zurück-Button
        ItemStack backButton = createGuiItem(
            Material.ARROW,
            "§c« Zurück",
            List.of("§7Zurück zur Übersicht")
        );
        inv.setItem(49, backButton);

        player.openInventory(inv);
    }

    private ItemStack createRewardDisplay(DailyReward reward, int currentDay, int displayDay) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Belohnungen:");

        for (DailyReward.RewardItem item : reward.getRewards()) {
            String rewardText = String.format("§8» §e%dx %s",
                item.getAmount(),
                formatRewardValue(item));
            lore.add(rewardText);
        }

        lore.add("");
        if (displayDay == currentDay) {
            lore.add("§eHeute verfügbar!");
        } else if (displayDay < currentDay) {
            lore.add("§aBereits erhalten!");
        } else {
            lore.add("§7In " + (displayDay - currentDay) + " Tagen verfügbar");
        }

        return createGuiItem(
            reward.getDisplayIcon(),
            (reward.isSpecialReward() ? "§6" : "§e") + "Tag " + displayDay,
            lore
        );
    }

    private ItemStack createAdminRewardItem(int day, DailyReward reward) {
        if (reward == null) {
            return createGuiItem(
                Material.BARRIER,
                "§c» Tag " + day,
                Arrays.asList(
                    "§7Keine Belohnung konfiguriert",
                    "",
                    "§eKlicke zum Hinzufügen!"
                )
            );
        }

        List<String> lore = new ArrayList<>();
        lore.add("§7Belohnungen:");
        for (DailyReward.RewardItem item : reward.getRewards()) {
            lore.add("§8» §e" + formatRewardValue(item));
        }
        lore.add("");
        lore.add("§eLinksklick: §7Bearbeiten");
        lore.add("§eRechtsklick: §7Entfernen");

        return createGuiItem(
            reward.getDisplayIcon(),
            (reward.isSpecialReward() ? "§6" : "§e") + "Tag " + day,
            lore
        );
    }

    private String formatRewardValue(DailyReward.RewardItem item) {
        return switch (item.getType()) {
            case COINS -> item.getAmount() + " Coins";
            case EXP -> item.getAmount() + " EXP";
            case ITEM -> ((ItemStack) item.getValue()).getType().toString().toLowerCase().replace("_", " ");
            case KIT -> "Kit: " + item.getValue();
            case PERMISSION -> "Berechtigung: " + item.getValue();
            default -> item.getValue().toString();
        };
    }

    private void decorateInventory(Inventory inv) {
        ItemStack border = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "§r", List.of());
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, border);
            inv.setItem(i + 45, border);
        }
        for (int i = 9; i < 45; i += 9) {
            inv.setItem(i, border);
            inv.setItem(i + 8, border);
        }
    }

    private ItemStack createGuiItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        // Use Adventure components for display name and lore
        meta.displayName(Component.text(ColorUtils.parseColor(name)));
        List<Component> loreComp = new ArrayList<>();
        for (String line : lore) loreComp.add(Component.text(ColorUtils.parseColor(line)));
        meta.lore(loreComp);
        item.setItemMeta(meta);
        return item;
    }
}
