package de.noctivag.skyblock.banking;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player Banking Data - Individuelle Banking-Daten für Spieler
 * 
 * Verantwortlich für:
 * - Banking Level und XP
 * - Banking Statistics
 * - Banking Progress
 * - Bank Transactions
 */
public class PlayerBanking {
    private final UUID playerId;
    private int bankingLevel;
    private double bankingXP;
    private double totalXP;
    private long lastActivity;
    
    // Banking Statistics
    private int transactions;
    private double totalDeposited;
    private double totalWithdrawn;
    private double totalInterest;
    private double currentBalance;
    private final Map<String, Integer> transactionTypes = new ConcurrentHashMap<>();
    
    // Banking Settings
    private boolean autoDeposit;
    private boolean autoInterest;
    private double interestRate;
    private double maxBalance;
    
    public PlayerBanking(UUID playerId) {
        this.playerId = playerId;
        this.bankingLevel = 1;
        this.bankingXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = System.currentTimeMillis();
        
        // Initialize statistics
        this.transactions = 0;
        this.totalDeposited = 0.0;
        this.totalWithdrawn = 0.0;
        this.totalInterest = 0.0;
        this.currentBalance = 0.0;
        
        // Initialize settings
        this.autoDeposit = false;
        this.autoInterest = true;
        this.interestRate = 0.01; // 1% base interest rate
        this.maxBalance = 1000000.0; // 1M max balance
    }
    
    public void addXP(double xpAmount) {
        this.bankingXP += xpAmount;
        this.totalXP += xpAmount;
        this.lastActivity = System.currentTimeMillis();
        
        // Check for level up
        int newLevel = calculateLevel(bankingXP);
        if (newLevel > bankingLevel) {
            bankingLevel = newLevel;
            // Increase interest rate and max balance with level
            this.interestRate = Math.min(0.05, 0.01 + (bankingLevel - 1) * 0.002);
            this.maxBalance = Math.min(10000000.0, 1000000.0 + (bankingLevel - 1) * 500000.0);
        }
    }
    
    public int calculateLevel(double xp) {
        int level = 1;
        double totalXPRequired = 0;
        
        while (totalXPRequired <= xp) {
            level++;
            totalXPRequired += getXPRequiredForLevel(level);
        }
        
        return level - 1;
    }
    
    public double getXPRequiredForLevel(int level) {
        if (level <= 1) return 0;
        if (level <= 10) return level * 50.0;
        if (level <= 25) return 10 * 50.0 + (level - 10) * 75.0;
        if (level <= 50) return 10 * 50.0 + 15 * 75.0 + (level - 25) * 100.0;
        return 10 * 50.0 + 15 * 75.0 + 25 * 100.0 + (level - 50) * 125.0;
    }
    
    public double getXPToNextLevel() {
        double xpForNextLevel = getXPRequiredForLevel(bankingLevel + 1);
        return xpForNextLevel - bankingXP;
    }
    
    public double getXPProgress() {
        double xpForCurrentLevel = getXPRequiredForLevel(bankingLevel);
        double xpForNextLevel = getXPRequiredForLevel(bankingLevel + 1);
        
        if (xpForNextLevel == xpForCurrentLevel) return 1.0;
        return (bankingXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
    }
    
    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        if (currentBalance + amount > maxBalance) return false;
        
        this.currentBalance += amount;
        this.totalDeposited += amount;
        this.transactions++;
        this.transactionTypes.put("deposit", transactionTypes.getOrDefault("deposit", 0) + 1);
        
        // Add XP for deposit
        addXP(amount * 0.001); // 0.1% of amount as XP
        
        return true;
    }
    
    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        if (currentBalance < amount) return false;
        
        this.currentBalance -= amount;
        this.totalWithdrawn += amount;
        this.transactions++;
        this.transactionTypes.put("withdraw", transactionTypes.getOrDefault("withdraw", 0) + 1);
        
        // Add XP for withdraw
        addXP(amount * 0.0005); // 0.05% of amount as XP
        
        return true;
    }
    
    public void addInterest(double amount) {
        this.currentBalance += amount;
        this.totalInterest += amount;
        this.transactions++;
        this.transactionTypes.put("interest", transactionTypes.getOrDefault("interest", 0) + 1);
    }
    
    public double calculateInterest() {
        return currentBalance * interestRate;
    }
    
    public double getNetWorth() {
        return currentBalance + totalWithdrawn;
    }
    
    public double getTransactionVolume() {
        return totalDeposited + totalWithdrawn;
    }
    
    // Getters and Setters
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getBankingLevel() {
        return bankingLevel;
    }
    
    public void setBankingLevel(int bankingLevel) {
        this.bankingLevel = Math.max(1, bankingLevel);
    }
    
    public double getBankingXP() {
        return bankingXP;
    }
    
    public void setBankingXP(double bankingXP) {
        this.bankingXP = Math.max(0, bankingXP);
    }
    
    public double getTotalXP() {
        return totalXP;
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public int getTransactions() {
        return transactions;
    }
    
    public double getTotalDeposited() {
        return totalDeposited;
    }
    
    public double getTotalWithdrawn() {
        return totalWithdrawn;
    }
    
    public double getTotalInterest() {
        return totalInterest;
    }
    
    public double getCurrentBalance() {
        return currentBalance;
    }
    
    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = Math.max(0, Math.min(maxBalance, currentBalance));
    }
    
    public Map<String, Integer> getTransactionTypes() {
        return new HashMap<>(transactionTypes);
    }
    
    public boolean isAutoDeposit() {
        return autoDeposit;
    }
    
    public void setAutoDeposit(boolean autoDeposit) {
        this.autoDeposit = autoDeposit;
    }
    
    public boolean isAutoInterest() {
        return autoInterest;
    }
    
    public void setAutoInterest(boolean autoInterest) {
        this.autoInterest = autoInterest;
    }
    
    public double getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(double interestRate) {
        this.interestRate = Math.max(0, Math.min(0.1, interestRate));
    }
    
    public double getMaxBalance() {
        return maxBalance;
    }
    
    public void setMaxBalance(double maxBalance) {
        this.maxBalance = Math.max(1000, maxBalance);
    }
    
    public void reset() {
        this.bankingLevel = 1;
        this.bankingXP = 0.0;
        this.totalXP = 0.0;
        this.lastActivity = System.currentTimeMillis();
        
        this.transactions = 0;
        this.totalDeposited = 0.0;
        this.totalWithdrawn = 0.0;
        this.totalInterest = 0.0;
        this.currentBalance = 0.0;
        
        this.transactionTypes.clear();
        
        this.interestRate = 0.01;
        this.maxBalance = 1000000.0;
    }
    
    public String getBankingProgressBar() {
        double progress = getXPProgress();
        int barLength = 20;
        int filledLength = (int) (progress * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§a");
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    public String getBankingSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("§6§lBanking Summary:\n");
        summary.append("§7Level: §e").append(bankingLevel).append("\n");
        summary.append("§7XP: §a").append(String.format("%.1f", bankingXP)).append("\n");
        summary.append("§7To Next Level: §b").append(String.format("%.1f", getXPToNextLevel())).append("\n");
        summary.append("§7Current Balance: §a").append(String.format("%.2f", currentBalance)).append("\n");
        summary.append("§7Total Deposited: §a").append(String.format("%.2f", totalDeposited)).append("\n");
        summary.append("§7Total Withdrawn: §c").append(String.format("%.2f", totalWithdrawn)).append("\n");
        summary.append("§7Total Interest: §e").append(String.format("%.2f", totalInterest)).append("\n");
        summary.append("§7Interest Rate: §e").append(String.format("%.1f%%", interestRate * 100)).append("\n");
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerBanking{" +
                "playerId=" + playerId +
                ", bankingLevel=" + bankingLevel +
                ", bankingXP=" + bankingXP +
                ", currentBalance=" + currentBalance +
                ", transactions=" + transactions +
                '}';
    }
}
