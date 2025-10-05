package de.noctivag.skyblock.models;

import de.noctivag.skyblock.enums.MinionType;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repräsentiert einen Minion mit Level, Position und Inventar
 */
public class Minion {
    
    private final UUID minionId;
    private final MinionType minionType;
    private final Location location;
    private int level;
    private final List<ItemStack> inventory;
    private long lastAction;
    private boolean isActive;
    private final List<MinionUpgrade> upgrades;
    
    public Minion(MinionType minionType, Location location) {
        this.minionId = UUID.randomUUID();
        this.minionType = minionType;
        this.location = location;
        this.level = 1;
        this.inventory = new ArrayList<>();
        this.lastAction = System.currentTimeMillis();
        this.isActive = true;
        this.upgrades = new ArrayList<>();
    }
    
    /**
     * Berechnet die Zeit bis zur nächsten Aktion
     */
    public long getTimeUntilNextAction() {
        long baseTime = minionType.getBaseActionTime();
        long levelReduction = (level - 1) * 1000; // 1 Sekunde weniger pro Level
        long upgradeReduction = getUpgradeReduction();
        
        return Math.max(baseTime - levelReduction - upgradeReduction, 1000); // Minimum 1 Sekunde
    }
    
    /**
     * Berechnet die Reduktion durch Upgrades
     */
    private long getUpgradeReduction() {
        long reduction = 0;
        for (MinionUpgrade upgrade : upgrades) {
            if (upgrade.getType() == de.noctivag.skyblock.enums.MinionUpgradeType.SPEED) {
                reduction += upgrade.getLevel() * 500; // 0.5 Sekunden pro Speed-Level
            }
        }
        return reduction;
    }
    
    /**
     * Führt eine Minion-Aktion aus
     */
    public void performAction() {
        if (!isActive) return;
        
        // Generiere Items basierend auf Minion-Typ
        List<ItemStack> generatedItems = minionType.generateItems(level);
        
        // Füge Items zum Inventar hinzu
        for (ItemStack item : generatedItems) {
            if (hasSpaceInInventory()) {
                inventory.add(item);
            }
        }
        
        lastAction = System.currentTimeMillis();
    }
    
    /**
     * Prüft ob Platz im Inventar ist
     */
    private boolean hasSpaceInInventory() {
        int maxSlots = 15 + (level - 1) * 3; // 15 Slots + 3 pro Level
        return inventory.size() < maxSlots;
    }
    
    /**
     * Berechnet Offline-Fortschritt
     */
    public void calculateOfflineProgress(long offlineTime) {
        if (!isActive) return;
        
        long actionTime = getTimeUntilNextAction();
        long actionsPerformed = offlineTime / actionTime;
        
        for (int i = 0; i < actionsPerformed; i++) {
            performAction();
        }
    }
    
    /**
     * Fügt ein Upgrade hinzu
     */
    public boolean addUpgrade(MinionUpgrade upgrade) {
        if (upgrades.size() >= 3) { // Maximal 3 Upgrades
            return false;
        }
        
        upgrades.add(upgrade);
        return true;
    }
    
    /**
     * Entfernt ein Upgrade
     */
    public boolean removeUpgrade(MinionUpgrade upgrade) {
        return upgrades.remove(upgrade);
    }
    
    /**
     * Leert das Inventar
     */
    public List<ItemStack> emptyInventory() {
        List<ItemStack> items = new ArrayList<>(inventory);
        inventory.clear();
        return items;
    }
    
    // Getters
    public UUID getMinionId() {
        return minionId;
    }
    
    public MinionType getMinionType() {
        return minionType;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public int getLevel() {
        return level;
    }
    
    public List<ItemStack> getInventory() {
        return new ArrayList<>(inventory);
    }
    
    public long getLastAction() {
        return lastAction;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public List<MinionUpgrade> getUpgrades() {
        return new ArrayList<>(upgrades);
    }
    
    // Setters
    public void setLevel(int level) {
        this.level = Math.max(1, Math.min(level, 11)); // Level 1-11
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public void setLastAction(long lastAction) {
        this.lastAction = lastAction;
    }
}
