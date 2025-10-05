package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.rewards.DailyRewardSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

public class EnhancedDailyRewardGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;
    private final DailyRewardSystem dailyRewardSystem;

    public EnhancedDailyRewardGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§lDaily Rewards"));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        // Placeholder - method not implemented
        this.dailyRewardSystem = null;
        setupItems();
    }

    private void setupItems() {
        // Header
        int currentStreak = dailyRewardSystem.getCurrentStreak(player);
        boolean canClaim = dailyRewardSystem.canClaimReward(player);
        
        setItem(4, Material.CHEST, "§6§lDaily Rewards", 
            "§7Aktuelle Streak: §e" + currentStreak + " Tage",
            "§7Status: " + (canClaim ? "§a§lVerfügbar" : "§c§lBereits erhalten"),
            "§7Nächste Belohnung in: §e" + dailyRewardSystem.getDaysUntilNextReward(player) + " Tagen");
        
        // Daily rewards (7 days)
        for (int day = 1; day <= 7; day++) {
            int slot = 19 + (day - 1); // Slots 19-25
            DailyRewardSystem.DailyReward reward = dailyRewardSystem.getRewardForDay(day);
            
            if (reward != null) {
                boolean isCurrentDay = (currentStreak % 7) + 1 == day;
                boolean isClaimed = !canClaim && isCurrentDay;
                boolean isAvailable = canClaim && isCurrentDay;
                
                Material icon = reward.getIcon();
                String name = reward.getName();
                String status = "";
                
                if (isClaimed) {
                    icon = Material.GRAY_DYE;
                    name = "§7§l" + name + " §8(Erhalten)";
                    status = "§c§lBereits erhalten";
                } else if (isAvailable) {
                    name = "§a§l" + name + " §8(Verfügbar)";
                    status = "§a§lKlicke zum Erhalten";
                } else {
                    name = "§7§l" + name;
                    status = "§7Noch nicht verfügbar";
                }
                
                setItem(slot, icon, name,
                    "§7" + reward.getDescription(),
                    "",
                    "§eGeld: §f" + SkyblockPlugin.getEconomyManager().formatMoney(reward.getMoneyReward()),
                    "§7Items: §f" + reward.getItems().size() + " Items",
                    "",
                    status);
            }
        }
        
        // Streak info
        setItem(28, Material.CLOCK, "§e§lStreak Info",
            "§7Aktuelle Streak: §e" + currentStreak + " Tage",
            "",
            "§7Streak-Boni:",
            "§7• 3+ Tage: §e1.5x Belohnung",
            "§7• 7+ Tage: §e2x Belohnung",
            "",
            "§7Nächster Bonus in: §e" + Math.max(0, 7 - (currentStreak % 7)) + " Tagen");
        
        // Claim button
        if (canClaim) {
            setItem(30, Material.EMERALD, "§a§lBelohnung Abholen",
                "§7Klicke um deine tägliche Belohnung abzuholen",
                "",
                "§eDu erhältst:",
                "§7• Geld-Belohnung",
                "§7• Verschiedene Items",
                "§7• Streak-Bonus (falls verfügbar)");
        } else {
            setItem(30, Material.BARRIER, "§c§lBereits Erhalten",
                "§7Du hast deine tägliche Belohnung bereits erhalten",
                "",
                "§7Komm morgen wieder für die nächste Belohnung!");
        }
        
        // History
        setItem(32, Material.BOOK, "§b§lBelohnungs-Historie",
            "§7Zeige deine letzten Belohnungen",
            "",
            "§7Letzte Belohnung: §e" + (dailyRewardSystem.getLastClaimDate(player) != null ? 
                dailyRewardSystem.getLastClaimDate(player) : "Noch keine"),
            "§7Gesamt erhalten: §e" + currentStreak + " Belohnungen");
        
        // Navigation
        setItem(45, Material.ARROW, "§7§lZurück", "§7Zurück zum Hauptmenü");
        setItem(49, Material.BARRIER, "§c§lSchließen", "§7Schließe das Menü");
        
        // Decorative items
        for (int i = 0; i < 9; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 36; i < 45; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 46; i < 49; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
        for (int i = 50; i < 54; i++) {
            setItem(i, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        }
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
