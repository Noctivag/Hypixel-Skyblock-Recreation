package de.noctivag.skyblock.features.economy.bazaar.types;
import org.bukkit.inventory.ItemStack;

/**
 * Bazaar Order Result enum for order outcomes
 */
public enum BazaarOrderResult {
    SUCCESS("Order placed successfully"),
    INSUFFICIENT_FUNDS("Insufficient funds"),
    INSUFFICIENT_ITEMS("Insufficient items"),
    INVALID_PRICE("Invalid price"),
    INVALID_AMOUNT("Invalid amount"),
    ORDER_FILLED("Order filled immediately"),
    PARTIAL_FILL("Order partially filled"),
    ERROR("An error occurred");

    private final String message;

    BazaarOrderResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return this == SUCCESS || this == ORDER_FILLED || this == PARTIAL_FILL;
    }
}
