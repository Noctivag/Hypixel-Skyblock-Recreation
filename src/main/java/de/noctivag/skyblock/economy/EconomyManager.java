package de.noctivag.skyblock.economy;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Sound;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public class EconomyManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, Double> balances = new HashMap<>();
    private final File file;
    private FileConfiguration config;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    public EconomyManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.file = new File(SkyblockPlugin.getDataFolder(), "economy.yml");
        loadBalances();
    }

    private void loadBalances() {
        if (!file.exists()) {
            SkyblockPlugin.saveResource("economy.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        if (config.contains("balances")) {
            for (String key : config.getConfigurationSection("balances").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    double bal = config.getDouble("balances." + key, 0.0);
                    balances.put(uuid, bal);
                } catch (IllegalArgumentException e) {
                    SkyblockPlugin.getLogger().warning("Ungültige UUID in economy.yml: " + key);
                }
            }
        }
    }

    public void saveBalances() {
        if (config == null) config = new YamlConfiguration();
        for (Map.Entry<UUID, Double> e : balances.entrySet()) {
            config.set("balances." + e.getKey().toString(), e.getValue());
        }
        try {
            config.save(file);
        } catch (IOException ex) {
            SkyblockPlugin.getLogger().severe("Konnte economy.yml nicht speichern: " + ex.getMessage());
        }
    }

    public double getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }

    public void setBalance(Player player, double amount) {
        amount = Math.max(0.0, amount); // Prevent negative balance

        balances.put(player.getUniqueId(), amount);
        saveBalances();

        // Notify player
        // TODO: Implement proper MessageManager interface
        // player.sendMessage(((MessageManager) SkyblockPlugin.getMessageManager()).getMessage("economy.balance", formatMoney(amount)));
        player.sendMessage("§7Balance: " + formatMoney(amount));
    }

    public boolean hasBalance(Player player, double amount) {
        // Check if player has rank-based cost exemption
        if (hasCostExemption(player)) {
            return true;
        }
        return getBalance(player) >= amount;
    }

    public boolean hasCostExemption(Player player) {
        // TODO: Implement proper RankManager and ConfigManager interfaces
        // String rank = ((RankManager) SkyblockPlugin.getRankManager()).getPlayerRank(player);
        // return ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getStringList("economy.exempt-ranks").contains(rank);
        return false; // Placeholder - no exemption
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean withdrawMoney(Player player, double amount) {
        if (amount <= 0) {
            // TODO: Implement proper MessageManager interface
            // player.sendMessage(((MessageManager) SkyblockPlugin.getMessageManager()).getMessage("economy.invalid-amount"));
            player.sendMessage(Component.text("§cInvalid amount!"));
            return false;
        }
        
        // Check for rank-based exemption
        if (hasCostExemption(player)) {
            player.sendMessage(Component.text("§a§lVIP §7Du hast keine Kosten aufgrund deines Rangs!"));
            return true;
        }

        if (!hasBalance(player, amount)) {
            // TODO: Implement proper MessageManager interface
            // player.sendMessage(((MessageManager) SkyblockPlugin.getMessageManager()).getMessage("economy.not-enough", formatMoney(amount)));
            player.sendMessage("§cNot enough money! Need: " + formatMoney(amount));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return false;
        }

        double newBalance = getBalance(player) - amount;
        setBalance(player, newBalance);

        // Log to DB if available
        try {
            if (SkyblockPlugin.getDatabaseManager() != null && SkyblockPlugin.getDatabaseManager().isConnected()) {
                SkyblockPlugin.getDatabaseManager().saveEconomyTransaction(player.getUniqueId().toString(), "withdraw", amount, newBalance, "Internal withdrawal");
            }
        } catch (Exception ignored) {}

        // TODO: Implement proper MessageManager interface
        // player.sendMessage(((MessageManager) SkyblockPlugin.getMessageManager()).getMessage("economy.transaction.success", formatMoney(newBalance)));
        player.sendMessage("§aTransaction successful! New balance: " + formatMoney(newBalance));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        return true; // Explicitly return true to indicate success
    }

    public void depositMoney(Player player, double amount) {
        if (amount <= 0) return;
        double newBalance = getBalance(player) + amount;
        setBalance(player, newBalance);

        // Log to DB if available
        try {
            if (SkyblockPlugin.getDatabaseManager() != null && SkyblockPlugin.getDatabaseManager().isConnected()) {
                SkyblockPlugin.getDatabaseManager().saveEconomyTransaction(player.getUniqueId().toString(), "deposit", amount, newBalance, "Internal deposit");
            }
        } catch (Exception ignored) {}

        // TODO: Implement proper MessageManager interface
        // player.sendMessage(((MessageManager) SkyblockPlugin.getMessageManager()).getMessage("economy.transaction.success", formatMoney(newBalance)));
        player.sendMessage("§aTransaction successful! New balance: " + formatMoney(newBalance));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }

    public void createAccount(Player player) {
        if (!balances.containsKey(player.getUniqueId())) {
            // TODO: Implement proper ConfigManager interface
            // double startingBalance = ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getDouble("economy.starting-balance", 100.0);
            double startingBalance = 100.0; // Default starting balance
            setBalance(player, startingBalance);

            // Send first join message with starting balance
            if (!player.hasPlayedBefore()) {
                // TODO: Implement proper MessageManager interface
                // player.sendMessage(((MessageManager) SkyblockPlugin.getMessageManager()).getMessage("economy.first-join", formatMoney(startingBalance)));
                player.sendMessage("§aWelcome! Starting balance: " + formatMoney(startingBalance));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }
    }

    public String formatMoney(double amount) {
        return df.format(amount) + " " + getCurrencyName();
    }

    public String getCurrencyName() {
        try {
            // TODO: Implement proper ConfigManager interface
            // return ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getString("economy.currency.name", "Coins");
            return "Coins"; // Default currency name
        } catch (Exception e) {
            return "Coins";
        }
    }
    
    /**
     * Give money to a player (alias for depositMoney)
     */
    public void giveMoney(Player player, double amount) {
        depositMoney(player, amount);
    }
    
    /**
     * Set balance silently (without messages)
     */
    public void setBalanceSilent(Player player, double amount) {
        balances.put(player.getUniqueId(), Math.max(0, amount));
        saveBalances();
    }
    
    /**
     * Reset player balance to starting balance
     */
    public void resetBalance(Player player) {
        // TODO: Implement proper ConfigManager interface
        // double startingBalance = ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getDouble("economy.starting-balance", 100.0);
        double startingBalance = 100.0; // Default starting balance
        setBalance(player, startingBalance);
    }
}
