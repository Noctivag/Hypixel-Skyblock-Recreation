package de.noctivag.skyblock.economy;

import org.bukkit.entity.Player;

/**
 * Economy interface for the skyblock plugin
 */
public interface EconomyInterface {
    
    /**
     * Get player balance
     */
    double getBalance(Player player);
    
    /**
     * Give money to player
     */
    void giveMoney(Player player, double amount);
    
    /**
     * Deposit money to player
     */
    void depositMoney(Player player, double amount);
    
    /**
     * Deposit money to player (int version)
     */
    void depositMoney(Player player, int amount);
    
    /**
     * Withdraw money from player
     */
    boolean withdrawMoney(Player player, double amount);
    
    /**
     * Set player balance silently
     */
    void setBalanceSilent(Player player, double amount);
    
    /**
     * Reset player balance
     */
    void resetBalance(Player player);
    
    /**
     * Check if player has enough balance
     */
    boolean hasBalance(Player player, double amount);
    
    /**
     * Check if player has cost exemption
     */
    boolean hasCostExemption(Player player);
    
    /**
     * Format money amount
     */
    String formatMoney(double amount);
}
