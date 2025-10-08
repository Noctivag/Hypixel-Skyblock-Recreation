package de.noctivag.skyblock.minions;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a minion that has been placed in the world
 */
public class PlacedMinion extends BaseMinion {
    private final MinionTier minionTier;
    private final long placedTime;
    private final Map<String, MinionUpgrade> upgrades;
    private final Map<Material, Integer> storage;
    private final List<MinionFuel> fuel;
    private final long lastActionTime;
    private final long totalActions;
    private final long totalProduction;

    public PlacedMinion(String minionId, UUID ownerId, MinionTier minionTier, Location location) {
        super(minionId, ownerId, minionTier.getMinionType().name(), minionTier.getMinionType().getDisplayName(), minionTier.getMinionType().getMaterial(), minionTier.getTier(), true, location);
        this.minionTier = minionTier;
        this.placedTime = System.currentTimeMillis();
        this.upgrades = new ConcurrentHashMap<>();
        this.storage = new ConcurrentHashMap<>();
        this.fuel = new ArrayList<>();
        this.lastActionTime = System.currentTimeMillis();
        this.totalActions = 0;
        this.totalProduction = 0;
    }

    public PlacedMinion(String minionId, UUID ownerId, MinionTier minionTier, Location location,
                       Map<String, MinionUpgrade> upgrades, Map<Material, Integer> storage,
                       List<MinionFuel> fuel, long lastActionTime, long totalActions, 
                       long totalProduction, boolean isActive) {
        super(minionId, ownerId, minionTier.getMinionType().name(), minionTier.getMinionType().getDisplayName(), minionTier.getMinionType().getMaterial(), minionTier.getTier(), isActive, location);
        this.minionTier = minionTier;
        this.placedTime = System.currentTimeMillis();
        this.upgrades = new ConcurrentHashMap<>(upgrades);
        this.storage = new ConcurrentHashMap<>(storage);
        this.fuel = new ArrayList<>(fuel);
        this.lastActionTime = lastActionTime;
        this.totalActions = totalActions;
        this.totalProduction = totalProduction;
    }

    // Getters
    public String getMinionId() { return minionId; }
    public UUID getOwnerId() { return ownerId; }
    public MinionTier getMinionTier() { return minionTier; }
    public Location getLocation() { return location.clone(); }
    public long getPlacedTime() { return placedTime; }
    public Map<String, MinionUpgrade> getUpgrades() { return new HashMap<>(upgrades); }
    public Map<Material, Integer> getStorage() { return new HashMap<>(storage); }
    public List<MinionFuel> getFuel() { return new ArrayList<>(fuel); }
    public long getLastActionTime() { return lastActionTime; }
    public long getTotalActions() { return totalActions; }
    public long getTotalProduction() { return totalProduction; }
    public boolean isActive() { return isActive; }

    /**
     * Get minion display name
     */
    public String getDisplayName() {
        return minionTier.getFullDisplayName();
    }

    /**
     * Get minion description
     */
    public String getDescription() {
        return minionTier.getTierDescription();
    }

    /**
     * Get minion lore
     */
    public String[] getMinionLore() {
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList(minionTier.getTierLore()));
        lore.add("");
        lore.add("&7Upgrades: &a" + upgrades.size() + "/" + minionTier.getUpgradeSlots());
        lore.add("&7Storage: &a" + getStorageUsed() + "/" + minionTier.getStorageCapacity());
        lore.add("&7Fuel: &a" + fuel.size() + "/" + minionTier.getFuelSlots());
        lore.add("");
        lore.add("&7Total Actions: &a" + totalActions);
        lore.add("&7Total Production: &a" + totalProduction);
        lore.add("&7Production Rate: &a" + String.format("%.2f", getProductionRate()) + "/hour");
        lore.add("");
        lore.add("&7Placed: &a" + formatTimeAgo(placedTime));
        lore.add("&7Last Action: &a" + formatTimeAgo(lastActionTime));
        lore.add("");
        lore.add(isActive ? "&a✓ Active" : "&c✗ Inactive");
        
        return lore.toArray(new String[0]);
    }

    /**
     * Get production rate per hour
     */
    public double getProductionRate() {
        double baseRate = minionTier.getProductionRatePerHour();
        
        // Apply speed upgrades
        for (MinionUpgrade upgrade : upgrades.values()) {
            if (upgrade.getUpgradeType() == MinionUpgrade.UpgradeType.SPEED) {
                baseRate *= (1.0 + upgrade.getEffectValue());
            }
        }
        
        // Apply fuel efficiency
        double fuelMultiplier = getFuelEfficiencyMultiplier();
        baseRate *= fuelMultiplier;
        
        return baseRate;
    }

    /**
     * Get fuel efficiency multiplier
     */
    public double getFuelEfficiencyMultiplier() {
        double multiplier = 1.0;
        
        for (MinionFuel fuelItem : fuel) {
            multiplier *= fuelItem.getEfficiencyMultiplier();
        }
        
        // Apply fuel efficiency upgrades
        for (MinionUpgrade upgrade : upgrades.values()) {
            if (upgrade.getUpgradeType() == MinionUpgrade.UpgradeType.FUEL_EFFICIENCY) {
                multiplier *= (1.0 + upgrade.getEffectValue());
            }
        }
        
        return multiplier;
    }

    /**
     * Get storage used
     */
    public int getStorageUsed() {
        return storage.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get storage available
     */
    public int getStorageAvailable() {
        return minionTier.getStorageCapacity() - getStorageUsed();
    }

    /**
     * Check if storage is full
     */
    public boolean isStorageFull() {
        return getStorageAvailable() <= 0;
    }

    /**
     * Add item to storage
     */
    public boolean addToStorage(Material material, int amount) {
        if (getStorageAvailable() < amount) {
            return false; // Not enough storage space
        }
        
        storage.put(material, storage.getOrDefault(material, 0) + amount);
        return true;
    }

    /**
     * Remove item from storage
     */
    public boolean removeFromStorage(Material material, int amount) {
        int currentAmount = storage.getOrDefault(material, 0);
        if (currentAmount < amount) {
            return false; // Not enough items in storage
        }
        
        if (currentAmount == amount) {
            storage.remove(material);
        } else {
            storage.put(material, currentAmount - amount);
        }
        
        return true;
    }

    /**
     * Get item count in storage
     */
    public int getStorageCount(Material material) {
        return storage.getOrDefault(material, 0);
    }

    /**
     * Add upgrade
     */
    public boolean addUpgrade(MinionUpgrade upgrade) {
        if (upgrades.size() >= minionTier.getUpgradeSlots()) {
            return false; // No more upgrade slots
        }
        
        if (!upgrade.isStackable() && upgrades.containsKey(upgrade.getId())) {
            return false; // Upgrade already applied and not stackable
        }
        
        upgrades.put(upgrade.getId(), upgrade);
        return true;
    }

    /**
     * Remove upgrade
     */
    public boolean removeUpgrade(String upgradeId) {
        return upgrades.remove(upgradeId) != null;
    }

    /**
     * Check if upgrade is applied
     */
    public boolean hasUpgrade(String upgradeId) {
        return upgrades.containsKey(upgradeId);
    }

    /**
     * Check if upgrade type is applied
     */
    public boolean hasUpgradeType(MinionUpgrade.UpgradeType upgradeType) {
        return upgrades.values().stream()
                .anyMatch(upgrade -> upgrade.getUpgradeType() == upgradeType);
    }

    /**
     * Add fuel
     */
    public boolean addFuel(MinionFuel fuelItem) {
        if (fuel.size() >= minionTier.getFuelSlots()) {
            return false; // No more fuel slots
        }
        
        fuel.add(fuelItem);
        return true;
    }

    /**
     * Remove fuel
     */
    public boolean removeFuel(int index) {
        if (index < 0 || index >= fuel.size()) {
            return false;
        }
        
        fuel.remove(index);
        return true;
    }

    /**
     * Get fuel efficiency
     */
    public double getFuelEfficiency() {
        return fuel.stream()
                .mapToDouble(MinionFuel::getEfficiencyMultiplier)
                .reduce(1.0, (a, b) -> a * b);
    }

    /**
     * Check if minion can perform action
     */
    public boolean canPerformAction() {
        if (!isActive) return false;
        if (isStorageFull()) return false;
        
        long currentTime = System.currentTimeMillis();
        long timeSinceLastAction = currentTime - lastActionTime;
        long actionInterval = (long) (minionTier.getActionTime() * 1000); // Convert to milliseconds
        
        return timeSinceLastAction >= actionInterval;
    }

    /**
     * Perform minion action
     */
    public boolean performAction() {
        if (!canPerformAction()) {
            return false;
        }
        
        // Add produced item to storage
        Material producedMaterial = minionTier.getMinionType().getMaterial();
        int amount = 1;
        
        // Check for double drops upgrade
        if (hasUpgradeType(MinionUpgrade.UpgradeType.DOUBLE_DROPS)) {
            MinionUpgrade doubleDropsUpgrade = upgrades.values().stream()
                    .filter(upgrade -> upgrade.getUpgradeType() == MinionUpgrade.UpgradeType.DOUBLE_DROPS)
                    .findFirst().orElse(null);
            
            if (doubleDropsUpgrade != null && Math.random() < doubleDropsUpgrade.getEffectValue()) {
                amount *= 2;
            }
        }
        
        // Add to storage
        addToStorage(producedMaterial, amount);
        
        // Update statistics
        // Note: In a real implementation, you'd update lastActionTime and totalActions here
        
        return true;
    }

    /**
     * Get minion status
     */
    public String getStatus() {
        if (!isActive) return "&cInactive";
        if (isStorageFull()) return "&eStorage Full";
        if (canPerformAction()) return "&aReady";
        return "&7Working";
    }

    /**
     * Get time since last action
     */
    public long getTimeSinceLastAction() {
        return System.currentTimeMillis() - lastActionTime;
    }

    /**
     * Get time until next action
     */
    public long getTimeUntilNextAction() {
        long actionInterval = (long) (minionTier.getActionTime() * 1000);
        long timeSinceLastAction = getTimeSinceLastAction();
        return Math.max(0, actionInterval - timeSinceLastAction);
    }

    /**
     * Format time ago
     */
    private String formatTimeAgo(long timestamp) {
        long timeAgo = System.currentTimeMillis() - timestamp;
        
        if (timeAgo < 60000) { // Less than 1 minute
            return "Just now";
        } else if (timeAgo < 3600000) { // Less than 1 hour
            return (timeAgo / 60000) + "m ago";
        } else if (timeAgo < 86400000) { // Less than 1 day
            return (timeAgo / 3600000) + "h ago";
        } else {
            return (timeAgo / 86400000) + "d ago";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlacedMinion that = (PlacedMinion) obj;
        return minionId.equals(that.minionId);
    }

    @Override
    public int hashCode() {
        return minionId.hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName() + " at " + location.toString();
    }
}
