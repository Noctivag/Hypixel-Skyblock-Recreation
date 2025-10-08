package de.noctivag.skyblock.economy;

/**
 * Basisklasse für Handelsobjekte (z.B. BazaarItem, Auction, MarketData)
 */
public abstract class TradeObject {
    protected final String itemId;
    protected double price;
    protected int amount;

    public TradeObject(String itemId, double price, int amount) {
        this.itemId = itemId;
        this.price = price;
        this.amount = amount;
    }

    public String getItemId() { return itemId; }
    public double getPrice() { return price; }
    public int getAmount() { return amount; }
    public void setPrice(double price) { this.price = price; }
    public void setAmount(int amount) { this.amount = amount; }
}
