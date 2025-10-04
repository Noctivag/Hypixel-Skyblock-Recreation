package de.noctivag.plugin.features.minions.automation;
import org.bukkit.inventory.ItemStack;

/**
 * Settings for minion automation
 */
public class AutoSettings {
    
    private boolean autoCollect = false;
    private boolean autoSell = false;
    private boolean autoCompact = false;
    private boolean autoSmelt = false;
    private boolean autoCraft = false;
    
    private int collectThreshold = 80; // Collect when storage is 80% full
    private int sellThreshold = 50; // Sell when storage is 50% full
    private int compactThreshold = 32; // Compact when 32+ items
    
    private double sellPrice = 1.0; // Multiplier for sell price
    private boolean keepEnchanted = true; // Keep enchanted items
    private boolean keepRare = true; // Keep rare items
    
    public AutoSettings() {
        // Default settings
    }
    
    // Getters and Setters
    public boolean isAutoCollect() {
        return autoCollect;
    }
    
    public void setAutoCollect(boolean autoCollect) {
        this.autoCollect = autoCollect;
    }
    
    public boolean isAutoSell() {
        return autoSell;
    }
    
    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
    }
    
    public boolean isAutoCompact() {
        return autoCompact;
    }
    
    public void setAutoCompact(boolean autoCompact) {
        this.autoCompact = autoCompact;
    }
    
    public boolean isAutoSmelt() {
        return autoSmelt;
    }
    
    public void setAutoSmelt(boolean autoSmelt) {
        this.autoSmelt = autoSmelt;
    }
    
    public boolean isAutoCraft() {
        return autoCraft;
    }
    
    public void setAutoCraft(boolean autoCraft) {
        this.autoCraft = autoCraft;
    }
    
    public int getCollectThreshold() {
        return collectThreshold;
    }
    
    public void setCollectThreshold(int collectThreshold) {
        this.collectThreshold = Math.max(1, Math.min(100, collectThreshold));
    }
    
    public int getSellThreshold() {
        return sellThreshold;
    }
    
    public void setSellThreshold(int sellThreshold) {
        this.sellThreshold = Math.max(1, Math.min(100, sellThreshold));
    }
    
    public int getCompactThreshold() {
        return compactThreshold;
    }
    
    public void setCompactThreshold(int compactThreshold) {
        this.compactThreshold = Math.max(1, compactThreshold);
    }
    
    public double getSellPrice() {
        return sellPrice;
    }
    
    public void setSellPrice(double sellPrice) {
        this.sellPrice = Math.max(0.1, sellPrice);
    }
    
    public boolean isKeepEnchanted() {
        return keepEnchanted;
    }
    
    public void setKeepEnchanted(boolean keepEnchanted) {
        this.keepEnchanted = keepEnchanted;
    }
    
    public boolean isKeepRare() {
        return keepRare;
    }
    
    public void setKeepRare(boolean keepRare) {
        this.keepRare = keepRare;
    }
    
    @Override
    public String toString() {
        return String.format("AutoSettings{collect=%s, sell=%s, compact=%s, smelt=%s, craft=%s}", 
            autoCollect, autoSell, autoCompact, autoSmelt, autoCraft);
    }
}
