package de.noctivag.skyblock.bank;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Complete Bank System matching Hypixel Skyblock
 * Includes personal and coop bank
 */
public class BankSystem {

    private final SkyblockPlugin plugin;
    private final Map<UUID, BankAccount> personalBanks = new HashMap<>();
    private final Map<String, CoopBank> coopBanks = new HashMap<>();

    public BankSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get or create personal bank
     */
    public BankAccount getPersonalBank(UUID playerId) {
        return personalBanks.computeIfAbsent(playerId, k -> new BankAccount(playerId));
    }

    /**
     * Get or create coop bank
     */
    public CoopBank getCoopBank(String coopId) {
        return coopBanks.computeIfAbsent(coopId, k -> new CoopBank(coopId));
    }

    /**
     * Deposit coins to personal bank
     */
    public boolean depositPersonal(Player player, long amount) {
        if (amount <= 0) {
            player.sendMessage("§cAmount must be positive!");
            return false;
        }

        // TODO: Check player purse balance
        // if (economyManager.getBalance(player) < amount) {
        //     player.sendMessage("§cYou don't have enough coins!");
        //     return false;
        // }

        BankAccount bank = getPersonalBank(player.getUniqueId());
        
        if (bank.getBalance() + amount > bank.getCapacity()) {
            player.sendMessage("§cBank is full! Current capacity: §6" + formatCoins(bank.getCapacity()));
            return false;
        }

        // TODO: Withdraw from purse
        bank.deposit(amount);

        player.sendMessage("§aDeposited §6" + formatCoins(amount) + " §ato your bank!");
        player.sendMessage("§7Bank Balance: §6" + formatCoins(bank.getBalance()));

        return true;
    }

    /**
     * Withdraw coins from personal bank
     */
    public boolean withdrawPersonal(Player player, long amount) {
        if (amount <= 0) {
            player.sendMessage("§cAmount must be positive!");
            return false;
        }

        BankAccount bank = getPersonalBank(player.getUniqueId());

        if (bank.getBalance() < amount) {
            player.sendMessage("§cYou don't have enough coins in your bank!");
            player.sendMessage("§7Bank Balance: §6" + formatCoins(bank.getBalance()));
            return false;
        }

        bank.withdraw(amount);
        // TODO: Add to purse

        player.sendMessage("§aWithdrew §6" + formatCoins(amount) + " §afrom your bank!");
        player.sendMessage("§7Bank Balance: §6" + formatCoins(bank.getBalance()));

        return true;
    }

    /**
     * Deposit to coop bank
     */
    public boolean depositCoop(Player player, String coopId, long amount) {
        if (amount <= 0) {
            player.sendMessage("§cAmount must be positive!");
            return false;
        }

        CoopBank bank = getCoopBank(coopId);

        if (!bank.isMember(player.getUniqueId())) {
            player.sendMessage("§cYou are not a member of this coop!");
            return false;
        }

        if (bank.getBalance() + amount > bank.getCapacity()) {
            player.sendMessage("§cCoop bank is full!");
            return false;
        }

        // TODO: Withdraw from purse
        bank.deposit(amount, player.getUniqueId());

        player.sendMessage("§aDeposited §6" + formatCoins(amount) + " §ato the coop bank!");
        player.sendMessage("§7Coop Balance: §6" + formatCoins(bank.getBalance()));

        return true;
    }

    /**
     * Withdraw from coop bank
     */
    public boolean withdrawCoop(Player player, String coopId, long amount) {
        if (amount <= 0) {
            player.sendMessage("§cAmount must be positive!");
            return false;
        }

        CoopBank bank = getCoopBank(coopId);

        if (!bank.isMember(player.getUniqueId())) {
            player.sendMessage("§cYou are not a member of this coop!");
            return false;
        }

        if (bank.getBalance() < amount) {
            player.sendMessage("§cCoop bank doesn't have enough coins!");
            return false;
        }

        bank.withdraw(amount, player.getUniqueId());
        // TODO: Add to purse

        player.sendMessage("§aWithdrew §6" + formatCoins(amount) + " §afrom the coop bank!");
        player.sendMessage("§7Coop Balance: §6" + formatCoins(bank.getBalance()));

        return true;
    }

    /**
     * Upgrade bank capacity
     */
    public boolean upgradePersonalBank(Player player) {
        BankAccount bank = getPersonalBank(player.getUniqueId());
        long upgradeCost = bank.getUpgradeCost();

        // TODO: Check balance and withdraw
        
        bank.upgradeCapacity();
        player.sendMessage("§aBank upgraded!");
        player.sendMessage("§7New Capacity: §6" + formatCoins(bank.getCapacity()));

        return true;
    }

    private String formatCoins(long amount) {
        if (amount >= 1_000_000_000) return String.format("%.1fB", amount / 1_000_000_000.0);
        if (amount >= 1_000_000) return String.format("%.1fM", amount / 1_000_000.0);
        if (amount >= 1_000) return String.format("%.1fK", amount / 1_000.0);
        return String.valueOf(amount);
    }

    /**
     * Personal bank account
     */
    public static class BankAccount {
        private final UUID ownerId;
        private long balance;
        private int capacityLevel;
        private static final long BASE_CAPACITY = 100_000;
        private static final long CAPACITY_INCREMENT = 100_000;

        public BankAccount(UUID ownerId) {
            this.ownerId = ownerId;
            this.balance = 0;
            this.capacityLevel = 1;
        }

        public void deposit(long amount) {
            this.balance += amount;
        }

        public void withdraw(long amount) {
            this.balance -= amount;
        }

        public long getBalance() {
            return balance;
        }

        public long getCapacity() {
            return BASE_CAPACITY + (capacityLevel - 1) * CAPACITY_INCREMENT;
        }

        public void upgradeCapacity() {
            this.capacityLevel++;
        }

        public long getUpgradeCost() {
            return 100_000 * capacityLevel;
        }

        public int getCapacityLevel() {
            return capacityLevel;
        }
    }

    /**
     * Coop bank account
     */
    public static class CoopBank {
        private final String coopId;
        private long balance;
        private final Map<UUID, Long> contributions = new HashMap<>();
        private final Map<UUID, Long> withdrawals = new HashMap<>();
        private static final long COOP_CAPACITY = 25_000_000;

        public CoopBank(String coopId) {
            this.coopId = coopId;
            this.balance = 0;
        }

        public void deposit(long amount, UUID playerId) {
            this.balance += amount;
            contributions.put(playerId, contributions.getOrDefault(playerId, 0L) + amount);
        }

        public void withdraw(long amount, UUID playerId) {
            this.balance -= amount;
            withdrawals.put(playerId, withdrawals.getOrDefault(playerId, 0L) + amount);
        }

        public boolean isMember(UUID playerId) {
            // TODO: Check actual coop membership
            return true;
        }

        public long getBalance() {
            return balance;
        }

        public long getCapacity() {
            return COOP_CAPACITY;
        }

        public long getContribution(UUID playerId) {
            return contributions.getOrDefault(playerId, 0L);
        }

        public long getWithdrawals(UUID playerId) {
            return withdrawals.getOrDefault(playerId, 0L);
        }
    }
}
