package de.noctivag.plugin.skyblock;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SkyblockBank {
    private double balance;
    private double interestRate;
    private long lastInterestTime;
    private final Map<String, Double> transactionHistory = new HashMap<>();
    
    public SkyblockBank() {
        this.balance = 0.0;
        this.interestRate = 0.02; // 2% interest rate
        this.lastInterestTime = System.currentTimeMillis();
    }
    
    public void deposit(double amount) {
        this.balance += amount;
        addTransaction("DEPOSIT", amount);
    }
    
    public boolean withdraw(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            addTransaction("WITHDRAW", -amount);
            return true;
        }
        return false;
    }
    
    public void addInterest() {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - lastInterestTime;
        
        // Add interest every hour (3600000 ms)
        if (timeDiff >= 3600000) {
            double interest = balance * interestRate;
            balance += interest;
            addTransaction("INTEREST", interest);
            lastInterestTime = currentTime;
        }
    }
    
    private void addTransaction(String type, double amount) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        transactionHistory.put(timestamp, amount);
        
        // Keep only last 100 transactions
        if (transactionHistory.size() > 100) {
            String oldestKey = transactionHistory.keySet().iterator().next();
            transactionHistory.remove(oldestKey);
        }
    }
    
    public double getBalance() {
        return balance;
    }
    
    public double getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    
    public Map<String, Double> getTransactionHistory() {
        return new HashMap<>(transactionHistory);
    }
    
    public void save() {
        // Save bank data
        // Implementation would go here
    }
}
