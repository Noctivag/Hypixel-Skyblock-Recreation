package de.noctivag.skyblock.features.economy.bazaar.types;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Bazaar Order class for bazaar trading
 */
public class BazaarOrder {
    private String id;
    private UUID playerId;
    private String itemId;
    private BazaarOrderType type;
    private double price;
    private int amount;
    private int filledAmount;
    private long timestamp;
    private boolean isActive;

    public BazaarOrder(String id, UUID playerId, String itemId, BazaarOrderType type, double price, int amount) {
        this.id = id;
        this.playerId = playerId;
        this.itemId = itemId;
        this.type = type;
        this.price = price;
        this.amount = amount;
        this.filledAmount = 0;
        this.timestamp = java.lang.System.currentTimeMillis();
        this.isActive = true;
    }

    public String getId() {
        return id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getItemId() {
        return itemId;
    }

    public BazaarOrderType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public int getFilledAmount() {
        return filledAmount;
    }

    public void setFilledAmount(int filledAmount) {
        this.filledAmount = filledAmount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getRemainingAmount() {
        return amount - filledAmount;
    }

    public boolean isFullyFilled() {
        return filledAmount >= amount;
    }

    public enum BazaarOrderType {
        BUY("Buy", "Purchase order"),
        SELL("Sell", "Sell order");

        private final String name;
        private final String description;

        BazaarOrderType(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
