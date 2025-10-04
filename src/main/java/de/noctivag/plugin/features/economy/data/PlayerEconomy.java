package de.noctivag.plugin.features.economy.data;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Player Economy class for storing player economy data
 */
public class PlayerEconomy {
    private UUID playerId;
    private double coins;
    private double bankBalance;
    private double purseBalance;
    private long lastLogin;
    private long totalCoinsEarned;
    private long totalCoinsSpent;
    private int transactionsCount;

    public PlayerEconomy(UUID playerId) {
        this.playerId = playerId;
        this.coins = 0.0;
        this.bankBalance = 0.0;
        this.purseBalance = 0.0;
        this.lastLogin = System.currentTimeMillis();
        this.totalCoinsEarned = 0;
        this.totalCoinsSpent = 0;
        this.transactionsCount = 0;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public double getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(double bankBalance) {
        this.bankBalance = bankBalance;
    }

    public double getPurseBalance() {
        return purseBalance;
    }

    public void setPurseBalance(double purseBalance) {
        this.purseBalance = purseBalance;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public long getTotalCoinsEarned() {
        return totalCoinsEarned;
    }

    public void setTotalCoinsEarned(long totalCoinsEarned) {
        this.totalCoinsEarned = totalCoinsEarned;
    }

    public long getTotalCoinsSpent() {
        return totalCoinsSpent;
    }

    public void setTotalCoinsSpent(long totalCoinsSpent) {
        this.totalCoinsSpent = totalCoinsSpent;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public double getTotalBalance() {
        return coins + bankBalance + purseBalance;
    }

    public void addCoins(double amount) {
        this.coins += amount;
        this.totalCoinsEarned += (long) amount;
        this.transactionsCount++;
    }

    public boolean removeCoins(double amount) {
        if (this.coins >= amount) {
            this.coins -= amount;
            this.totalCoinsSpent += (long) amount;
            this.transactionsCount++;
            return true;
        }
        return false;
    }
}
