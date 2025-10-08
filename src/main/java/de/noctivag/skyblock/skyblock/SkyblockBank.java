package de.noctivag.skyblock.skyblock;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Skyblock Bank - Player's banking data
 */
public class SkyblockBank {
    
    private final UUID playerId;
    private double balance;
    private double interestRate;
    private long lastInterest;
    private Map<String, Double> transactions;
    
    public SkyblockBank(UUID playerId) {
        this.playerId = playerId;
        this.balance = 0.0;
        this.interestRate = 0.02; // 2% interest rate
        this.lastInterest = System.currentTimeMillis();
        this.transactions = new HashMap<>();
    }
    
    // Getters and setters
    public UUID getPlayerId() { return playerId; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    
    public long getLastInterest() { return lastInterest; }
    public void setLastInterest(long lastInterest) { this.lastInterest = lastInterest; }
    
    public Map<String, Double> getTransactions() { return transactions; }
    public void setTransactions(Map<String, Double> transactions) { this.transactions = transactions; }
    
    /**
     * Deposit money
     */
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("deposit", amount);
            return true;
        }
        return false;
    }
    
    /**
     * Withdraw money
     */
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            addTransaction("withdraw", -amount);
            return true;
        }
        return false;
    }
    
    /**
     * Add transaction
     */
    private void addTransaction(String type, double amount) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        transactions.put(timestamp + "_" + type, amount);
    }
    
    /**
     * Calculate interest
     */
    public double calculateInterest() {
        long timeSinceLastInterest = System.currentTimeMillis() - lastInterest;
        long hours = timeSinceLastInterest / (1000 * 60 * 60);
        
        if (hours >= 24) { // Daily interest
            double interest = balance * interestRate;
            lastInterest = System.currentTimeMillis();
            return interest;
        }
        
        return 0.0;
    }
    
    public void save() {
        // Placeholder save method
    }
}