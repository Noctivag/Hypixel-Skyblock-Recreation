package de.noctivag.skyblock.features.armor;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.armor.types.ArmorSlot;
import de.noctivag.skyblock.features.armor.types.DragonArmorType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player's equipped armor management
 */
public class PlayerArmor {
    
    private final UUID playerId;
    private final Map<ArmorSlot, DragonArmorPiece> equippedArmor = new ConcurrentHashMap<>();
    private final Map<ArmorSlot, Long> equipTime = new ConcurrentHashMap<>();
    
    public PlayerArmor(UUID playerId) {
        this.playerId = playerId;
    }
    
    /**
     * Equip an armor piece
     */
    public boolean equipPiece(ArmorSlot slot, DragonArmorPiece piece) {
        // Unequip current piece if any
        unequipPiece(slot);
        
        // Equip new piece
        equippedArmor.put(slot, piece);
        equipTime.put(slot, java.lang.System.currentTimeMillis());
        
        return true;
    }
    
    /**
     * Unequip an armor piece
     */
    public DragonArmorPiece unequipPiece(ArmorSlot slot) {
        DragonArmorPiece removed = equippedArmor.remove(slot);
        equipTime.remove(slot);
        return removed;
    }
    
    /**
     * Get equipped armor piece
     */
    public DragonArmorPiece getEquippedPiece(ArmorSlot slot) {
        return equippedArmor.get(slot);
    }
    
    /**
     * Get all equipped armor
     */
    public Map<ArmorSlot, DragonArmorPiece> getAllEquippedArmor() {
        return new ConcurrentHashMap<>(equippedArmor);
    }
    
    /**
     * Check if a slot is occupied
     */
    public boolean isSlotOccupied(ArmorSlot slot) {
        return equippedArmor.containsKey(slot);
    }
    
    /**
     * Get armor set type if full set is equipped
     */
    public DragonArmorType getArmorSetType() {
        if (equippedArmor.size() != 4) return null; // Not full set
        
        DragonArmorType firstType = null;
        for (DragonArmorPiece piece : equippedArmor.values()) {
            if (firstType == null) {
                firstType = piece.getArmorType();
            } else if (piece.getArmorType() != firstType) {
                return null; // Different types, not a set
            }
        }
        
        return firstType;
    }
    
    /**
     * Check if full armor set is equipped
     */
    public boolean hasFullSet() {
        return getArmorSetType() != null;
    }
    
    /**
     * Get set bonus level (1-4 pieces)
     */
    public int getSetBonusLevel() {
        DragonArmorType setType = getArmorSetType();
        if (setType == null) return 0;
        
        return equippedArmor.size(); // Number of pieces equipped
    }
    
    /**
     * Get time equipped for a slot
     */
    public long getTimeEquipped(ArmorSlot slot) {
        Long equipTime = this.equipTime.get(slot);
        if (equipTime == null) return 0;
        
        return java.lang.System.currentTimeMillis() - equipTime;
    }
    
    /**
     * Get total time equipped
     */
    public long getTotalTimeEquipped() {
        long totalTime = 0;
        for (ArmorSlot slot : ArmorSlot.values()) {
            totalTime += getTimeEquipped(slot);
        }
        return totalTime;
    }
    
    /**
     * Check if armor is broken
     */
    public boolean isArmorBroken(ArmorSlot slot) {
        DragonArmorPiece piece = getEquippedPiece(slot);
        if (piece == null) return false;
        
        return piece.getDurability() <= 0;
    }
    
    /**
     * Check if any armor is broken
     */
    public boolean hasBrokenArmor() {
        for (ArmorSlot slot : ArmorSlot.values()) {
            if (isArmorBroken(slot)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Repair armor piece
     */
    public boolean repairArmor(ArmorSlot slot) {
        DragonArmorPiece piece = getEquippedPiece(slot);
        if (piece == null) return false;
        
        piece.repair();
        return true;
    }
    
    /**
     * Repair all armor
     */
    public void repairAllArmor() {
        for (ArmorSlot slot : ArmorSlot.values()) {
            repairArmor(slot);
        }
    }
    
    /**
     * Get armor durability percentage
     */
    public double getArmorDurabilityPercentage(ArmorSlot slot) {
        DragonArmorPiece piece = getEquippedPiece(slot);
        if (piece == null) return 0.0;
        
        return piece.getDurabilityPercentage();
    }
    
    /**
     * Get average armor durability
     */
    public double getAverageDurability() {
        if (equippedArmor.isEmpty()) return 0.0;
        
        double totalDurability = 0.0;
        int pieces = 0;
        
        for (ArmorSlot slot : ArmorSlot.values()) {
            DragonArmorPiece piece = getEquippedPiece(slot);
            if (piece != null) {
                totalDurability += piece.getDurabilityPercentage();
                pieces++;
            }
        }
        
        return pieces > 0 ? totalDurability / pieces : 0.0;
    }
    
    /**
     * Get armor statistics
     */
    public ArmorStatistics getStatistics() {
        return new ArmorStatistics(this);
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Armor statistics
     */
    public static class ArmorStatistics {
        private final int equippedPieces;
        private final boolean hasFullSet;
        private final DragonArmorType setType;
        private final int setBonusLevel;
        private final double averageDurability;
        private final long totalTimeEquipped;
        
        public ArmorStatistics(PlayerArmor armor) {
            this.equippedPieces = armor.equippedArmor.size();
            this.hasFullSet = armor.hasFullSet();
            this.setType = armor.getArmorSetType();
            this.setBonusLevel = armor.getSetBonusLevel();
            this.averageDurability = armor.getAverageDurability();
            this.totalTimeEquipped = armor.getTotalTimeEquipped();
        }
        
        public int getEquippedPieces() {
            return equippedPieces;
        }
        
        public boolean hasFullSet() {
            return hasFullSet;
        }
        
        public DragonArmorType getSetType() {
            return setType;
        }
        
        public int getSetBonusLevel() {
            return setBonusLevel;
        }
        
        public double getAverageDurability() {
            return averageDurability;
        }
        
        public long getTotalTimeEquipped() {
            return totalTimeEquipped;
        }
        
        @Override
        public String toString() {
            return String.format("ArmorStats{pieces=%d, fullSet=%s, setType=%s, bonusLevel=%d, durability=%.1f%%}", 
                equippedPieces, hasFullSet, setType, setBonusLevel, averageDurability);
        }
    }
}
