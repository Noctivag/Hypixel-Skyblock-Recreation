package de.noctivag.plugin.banking;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedBankingSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerBankData> playerBankData = new ConcurrentHashMap<>();
    private final Map<BankType, List<Bank>> banks = new HashMap<>();
    
    public AdvancedBankingSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeBanks();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeBanks() {
        // Basic Banks
        List<Bank> basicBanks = new ArrayList<>();
        basicBanks.add(new Bank(
            "Basic Bank", "§aBasic Bank", Material.CHEST,
            "§7A basic bank for storing coins and items.",
            BankType.BASIC, BankRarity.COMMON, 1, Arrays.asList("§7- Store coins", "§7- Store items"),
            Arrays.asList("§7- 1000 coin limit", "§7- 10 item slots"), 1000, 10
        ));
        basicBanks.add(new Bank(
            "Standard Bank", "§eStandard Bank", Material.ENDER_CHEST,
            "§7A standard bank with more storage capacity.",
            BankType.BASIC, BankRarity.UNCOMMON, 2, Arrays.asList("§7- Store coins", "§7- Store items"),
            Arrays.asList("§7- 5000 coin limit", "§7- 25 item slots"), 5000, 25
        ));
        basicBanks.add(new Bank(
            "Premium Bank", "§6Premium Bank", Material.GOLD_BLOCK,
            "§7A premium bank with high storage capacity.",
            BankType.BASIC, BankRarity.RARE, 3, Arrays.asList("§7- Store coins", "§7- Store items"),
            Arrays.asList("§7- 10000 coin limit", "§7- 50 item slots"), 10000, 50
        ));
        banks.put(BankType.BASIC, basicBanks);
        
        // Investment Banks
        List<Bank> investmentBanks = new ArrayList<>();
        investmentBanks.add(new Bank(
            "Investment Bank", "§bInvestment Bank", Material.EMERALD_BLOCK,
            "§7An investment bank that grows your money over time.",
            BankType.INVESTMENT, BankRarity.UNCOMMON, 2, Arrays.asList("§7- Invest coins", "§7- Earn interest"),
            Arrays.asList("§7- 5% interest rate", "§7- 10000 coin limit"), 10000, 0
        ));
        investmentBanks.add(new Bank(
            "High Yield Bank", "§dHigh Yield Bank", Material.DIAMOND_BLOCK,
            "§7A high yield bank with better interest rates.",
            BankType.INVESTMENT, BankRarity.RARE, 3, Arrays.asList("§7- Invest coins", "§7- Earn interest"),
            Arrays.asList("§7- 10% interest rate", "§7- 25000 coin limit"), 25000, 0
        ));
        investmentBanks.add(new Bank(
            "Elite Investment Bank", "§cElite Investment Bank", Material.NETHERITE_BLOCK,
            "§7An elite investment bank with maximum returns.",
            BankType.INVESTMENT, BankRarity.EPIC, 4, Arrays.asList("§7- Invest coins", "§7- Earn interest"),
            Arrays.asList("§7- 15% interest rate", "§7- 50000 coin limit"), 50000, 0
        ));
        banks.put(BankType.INVESTMENT, investmentBanks);
        
        // Loan Banks
        List<Bank> loanBanks = new ArrayList<>();
        loanBanks.add(new Bank(
            "Loan Bank", "§eLoan Bank", Material.GOLD_INGOT,
            "§7A loan bank that provides loans to players.",
            BankType.LOAN, BankRarity.UNCOMMON, 2, Arrays.asList("§7- Take loans", "§7- Pay interest"),
            Arrays.asList("§7- 5% interest rate", "§7- 5000 coin limit"), 5000, 0
        ));
        loanBanks.add(new Bank(
            "Credit Bank", "§6Credit Bank", Material.EMERALD,
            "§7A credit bank with better loan terms.",
            BankType.LOAN, BankRarity.RARE, 3, Arrays.asList("§7- Take loans", "§7- Pay interest"),
            Arrays.asList("§7- 3% interest rate", "§7- 15000 coin limit"), 15000, 0
        ));
        loanBanks.add(new Bank(
            "Elite Credit Bank", "§cElite Credit Bank", Material.DIAMOND,
            "§7An elite credit bank with the best loan terms.",
            BankType.LOAN, BankRarity.EPIC, 4, Arrays.asList("§7- Take loans", "§7- Pay interest"),
            Arrays.asList("§7- 1% interest rate", "§7- 50000 coin limit"), 50000, 0
        ));
        banks.put(BankType.LOAN, loanBanks);
        
        // Trading Banks
        List<Bank> tradingBanks = new ArrayList<>();
        tradingBanks.add(new Bank(
            "Trading Bank", "§aTrading Bank", Material.EMERALD_BLOCK,
            "§7A trading bank for buying and selling items.",
            BankType.TRADING, BankRarity.UNCOMMON, 2, Arrays.asList("§7- Buy items", "§7- Sell items"),
            Arrays.asList("§7- Item trading", "§7- Price updates"), 0, 0
        ));
        tradingBanks.add(new Bank(
            "Stock Bank", "§6Stock Bank", Material.GOLD_BLOCK,
            "§7A stock bank for trading stocks and shares.",
            BankType.TRADING, BankRarity.RARE, 3, Arrays.asList("§7- Buy stocks", "§7- Sell stocks"),
            Arrays.asList("§7- Stock trading", "§7- Market analysis"), 0, 0
        ));
        tradingBanks.add(new Bank(
            "Crypto Bank", "§5Crypto Bank", Material.NETHER_STAR,
            "§7A crypto bank for trading cryptocurrencies.",
            BankType.TRADING, BankRarity.EPIC, 4, Arrays.asList("§7- Buy crypto", "§7- Sell crypto"),
            Arrays.asList("§7- Crypto trading", "§7- Blockchain"), 0, 0
        ));
        banks.put(BankType.TRADING, tradingBanks);
        
        // Special Banks
        List<Bank> specialBanks = new ArrayList<>();
        specialBanks.add(new Bank(
            "Vault Bank", "§dVault Bank", Material.ENDER_CHEST,
            "§7A vault bank with maximum security.",
            BankType.SPECIAL, BankRarity.LEGENDARY, 5, Arrays.asList("§7- Maximum security", "§7- Special features"),
            Arrays.asList("§7- 100000 coin limit", "§7- 100 item slots"), 100000, 100
        ));
        specialBanks.add(new Bank(
            "Time Bank", "§bTime Bank", Material.CLOCK,
            "§7A time bank that manipulates time for investments.",
            BankType.SPECIAL, BankRarity.LEGENDARY, 5, Arrays.asList("§7- Time manipulation", "§7- Special features"),
            Arrays.asList("§7- 20% interest rate", "§7- 75000 coin limit"), 75000, 0
        ));
        specialBanks.add(new Bank(
            "Quantum Bank", "§5Quantum Bank", Material.END_CRYSTAL,
            "§7A quantum bank with quantum mechanics.",
            BankType.SPECIAL, BankRarity.MYTHIC, 6, Arrays.asList("§7- Quantum mechanics", "§7- Special features"),
            Arrays.asList("§7- 50% interest rate", "§7- 200000 coin limit"), 200000, 0
        ));
        banks.put(BankType.SPECIAL, specialBanks);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Bank") || displayName.contains("Banking")) {
            openBankingGUI(player);
        }
    }
    
    public void openBankingGUI(Player player) {
        openBankingMainGUI(player);
    }
    
    public void openBankingMainGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBanking - Main Menu");
        
        PlayerBankData bankData = getPlayerBankData(player.getUniqueId());
        
        // Player Info Panel
        addGUIItem(gui, 4, Material.PLAYER_HEAD, "§e§l" + player.getName(), 
            "§7Banking Level: §e" + bankData.getBankingLevel(),
            "§7XP: §b" + String.format("%.1f", bankData.getBankingXP()) + " §7/ §b" + String.format("%.1f", bankData.getXPForNextLevel()),
            "§7Progress: §a" + String.format("%.1f%%", bankData.getXPProgress() * 100));
        
        // Currency Balances
        addGUIItem(gui, 19, Material.GOLD_INGOT, "§6§lCoins", 
            "§7Balance: §6" + String.format("%.2f", bankData.getCoins()),
            "§7Capacity: §6" + String.format("%.0f", bankData.getCurrentBalance(CurrencyType.COINS)) + " §7/ §6" + String.format("%.0f", bankData.getMaxCapacity(CurrencyType.COINS)),
            "",
            "§eClick to manage coins");
        
        addGUIItem(gui, 20, Material.EMERALD, "§b§lTokens", 
            "§7Balance: §b" + String.format("%.2f", bankData.getTokens()),
            "§7Capacity: §b" + String.format("%.0f", bankData.getCurrentBalance(CurrencyType.TOKENS)) + " §7/ §b" + String.format("%.0f", bankData.getMaxCapacity(CurrencyType.TOKENS)),
            "",
            "§eClick to manage tokens");
        
        addGUIItem(gui, 21, Material.DIAMOND, "§d§lGems", 
            "§7Balance: §d" + String.format("%.2f", bankData.getGems()),
            "§7Capacity: §d" + String.format("%.0f", bankData.getCurrentBalance(CurrencyType.GEMS)) + " §7/ §d" + String.format("%.0f", bankData.getMaxCapacity(CurrencyType.GEMS)),
            "",
            "§eClick to manage gems");
        
        // Banking Features
        addGUIItem(gui, 22, Material.EMERALD_BLOCK, "§a§lInvestments", 
            "§7Invested: §a" + String.format("%.2f", bankData.getInvestedCoins()),
            "§7Interest Rate: §a" + String.format("%.1f%%", bankData.getInterestRate() * 100),
            "",
            "§eClick to manage investments");
        
        addGUIItem(gui, 23, Material.GOLD_INGOT, "§6§lLoans", 
            "§7Current Loan: §6" + String.format("%.2f", bankData.getLoanAmount()),
            "§7Max Loan: §6" + String.format("%.0f", bankData.getMaxLoanLimit()),
            "",
            "§eClick to manage loans");
        
        addGUIItem(gui, 24, Material.ANVIL, "§d§lUpgrades", 
            "§7Coin Capacity: §6" + bankData.getCoinCapacityUpgrade(),
            "§7Token Capacity: §b" + bankData.getTokenCapacityUpgrade(),
            "§7Gem Capacity: §d" + bankData.getGemCapacityUpgrade(),
            "§7Interest Rate: §e" + bankData.getInterestRateUpgrade(),
            "§7Security: §c" + bankData.getSecurityUpgrade(),
            "",
            "§eClick to upgrade");
        
        // Navigation
        addGUIItem(gui, 36, Material.BOOK, "§a§lTransaction History", "§7View your transaction history");
        addGUIItem(gui, 37, Material.SHIELD, "§c§lSecurity", "§7Manage security settings");
        addGUIItem(gui, 38, Material.COMPASS, "§b§lMarket Overview", "§7View market trends");
        
        // Close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the banking menu");
        
        player.openInventory(gui);
    }
    
    public void openCurrencyManagementGUI(Player player, CurrencyType currency) {
        Inventory gui = Bukkit.createInventory(null, 36, "§6§lBanking - " + currency.getDisplayName());
        
        PlayerBankData bankData = getPlayerBankData(player.getUniqueId());
        double balance = bankData.getCurrentBalance(currency);
        double maxCapacity = bankData.getMaxCapacity(currency);
        
        // Balance Info
        addGUIItem(gui, 4, currency == CurrencyType.COINS ? Material.GOLD_INGOT : 
                           currency == CurrencyType.TOKENS ? Material.EMERALD : Material.DIAMOND,
                   currency.getDisplayName() + " §7Management",
                   "§7Current Balance: " + currency.getDisplayName() + " §f" + String.format("%.2f", balance),
                   "§7Max Capacity: " + currency.getDisplayName() + " §f" + String.format("%.0f", maxCapacity),
                   "§7Usage: §e" + String.format("%.1f%%", (balance / maxCapacity) * 100));
        
        // Deposit/Withdraw Options
        addGUIItem(gui, 19, Material.GREEN_CONCRETE, "§a§lDeposit 100", "§7Deposit 100 " + currency.name().toLowerCase());
        addGUIItem(gui, 20, Material.GREEN_CONCRETE, "§a§lDeposit 1000", "§7Deposit 1000 " + currency.name().toLowerCase());
        addGUIItem(gui, 21, Material.GREEN_CONCRETE, "§a§lDeposit All", "§7Deposit all " + currency.name().toLowerCase());
        addGUIItem(gui, 22, Material.GREEN_CONCRETE, "§a§lCustom Deposit", "§7Enter custom amount");
        
        addGUIItem(gui, 25, Material.RED_CONCRETE, "§c§lWithdraw 100", "§7Withdraw 100 " + currency.name().toLowerCase());
        addGUIItem(gui, 26, Material.RED_CONCRETE, "§c§lWithdraw 1000", "§7Withdraw 1000 " + currency.name().toLowerCase());
        addGUIItem(gui, 27, Material.RED_CONCRETE, "§c§lWithdraw All", "§7Withdraw all " + currency.name().toLowerCase());
        addGUIItem(gui, 28, Material.RED_CONCRETE, "§c§lCustom Withdraw", "§7Enter custom amount");
        
        // Back button
        addGUIItem(gui, 31, Material.ARROW, "§7§lBack to Main Menu", "§7Return to main banking menu");
        
        player.openInventory(gui);
    }
    
    public void openTransactionHistoryGUI(Player player, int page) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lTransaction History - Page " + (page + 1));
        
        PlayerBankData bankData = getPlayerBankData(player.getUniqueId());
        List<BankTransaction> transactions = bankData.getTransactionHistory();
        
        int startIndex = page * 45;
        int endIndex = Math.min(startIndex + 45, transactions.size());
        
        // Add transactions
        for (int i = startIndex; i < endIndex; i++) {
            BankTransaction transaction = transactions.get(i);
            Material icon = switch (transaction.getType()) {
                case DEPOSIT -> Material.GREEN_CONCRETE;
                case WITHDRAW -> Material.RED_CONCRETE;
                case INVEST -> Material.EMERALD_BLOCK;
                case INTEREST -> Material.GOLD_BLOCK;
                case LOAN -> Material.GOLD_INGOT;
                case LOAN_PAYMENT -> Material.GREEN_TERRACOTTA;
                case UPGRADE -> Material.ANVIL;
                case TRANSFER -> Material.ARROW;
            };
            
            addGUIItem(gui, i - startIndex, icon,
                       transaction.getType().getDisplayName(),
                       "§7Currency: " + transaction.getCurrency().getDisplayName(),
                       "§7Amount: " + transaction.getCurrency().getDisplayName() + " §f" + String.format("%.2f", transaction.getAmount()),
                       "§7Time: §e" + transaction.getFormattedTime());
        }
        
        // Navigation
        if (page > 0) {
            addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page");
        }
        
        addGUIItem(gui, 49, Material.ARROW, "§7§lBack to Main Menu", "§7Return to main banking menu");
        
        if (endIndex < transactions.size()) {
            addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page");
        }
        
        player.openInventory(gui);
    }
    
    public void openUpgradesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBanking Upgrades");
        
        PlayerBankData bankData = getPlayerBankData(player.getUniqueId());
        
        // Upgrade Options
        addGUIItem(gui, 10, Material.GOLD_INGOT, "§6§lCoin Capacity",
                   "§7Current Level: §e" + bankData.getCoinCapacityUpgrade(),
                   "§7Max Capacity: §6" + String.format("%.0f", bankData.getMaxCapacity(CurrencyType.COINS)),
                   "§7Next Level Cost: §6" + String.format("%.0f", bankData.getUpgradeCost(UpgradeType.COIN_CAPACITY)) + " coins",
                   "",
                   "§eClick to upgrade");
        
        addGUIItem(gui, 11, Material.EMERALD, "§b§lToken Capacity",
                   "§7Current Level: §e" + bankData.getTokenCapacityUpgrade(),
                   "§7Max Capacity: §b" + String.format("%.0f", bankData.getMaxCapacity(CurrencyType.TOKENS)),
                   "§7Next Level Cost: §b" + String.format("%.0f", bankData.getUpgradeCost(UpgradeType.TOKEN_CAPACITY)) + " tokens",
                   "",
                   "§eClick to upgrade");
        
        addGUIItem(gui, 12, Material.DIAMOND, "§d§lGem Capacity",
                   "§7Current Level: §e" + bankData.getGemCapacityUpgrade(),
                   "§7Max Capacity: §d" + String.format("%.0f", bankData.getMaxCapacity(CurrencyType.GEMS)),
                   "§7Next Level Cost: §d" + String.format("%.0f", bankData.getUpgradeCost(UpgradeType.GEM_CAPACITY)) + " gems",
                   "",
                   "§eClick to upgrade");
        
        addGUIItem(gui, 13, Material.GOLD_BLOCK, "§e§lInterest Rate",
                   "§7Current Level: §e" + bankData.getInterestRateUpgrade(),
                   "§7Current Rate: §e" + String.format("%.1f%%", bankData.getInterestRate() * 100),
                   "§7Next Level Cost: §6" + String.format("%.0f", bankData.getUpgradeCost(UpgradeType.INTEREST_RATE)) + " coins",
                   "",
                   "§eClick to upgrade");
        
        addGUIItem(gui, 14, Material.SHIELD, "§c§lSecurity",
                   "§7Current Level: §e" + bankData.getSecurityUpgrade(),
                   "§7Two-Factor: " + (bankData.isTwoFactorEnabled() ? "§aEnabled" : "§cDisabled"),
                   "§7Next Level Cost: §6" + String.format("%.0f", bankData.getUpgradeCost(UpgradeType.SECURITY)) + " coins",
                   "",
                   "§eClick to upgrade");
        
        // Back button
        addGUIItem(gui, 49, Material.ARROW, "§7§lBack to Main Menu", "§7Return to main banking menu");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(description));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String... descriptions) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(descriptions));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public void depositCoins(Player player, int amount) {
        PlayerBankData data = getPlayerBankData(player.getUniqueId());
        data.depositCoins(amount);
        player.sendMessage("§a" + amount + " coins eingezahlt!");
    }
    
    public void withdrawCoins(Player player, int amount) {
        PlayerBankData data = getPlayerBankData(player.getUniqueId());
        if (data.withdrawCoins(amount)) {
            player.sendMessage("§a" + amount + " coins abgehoben!");
        } else {
            player.sendMessage("§cNicht genügend coins in der Bank!");
        }
    }
    
    public void investCoins(Player player, int amount) {
        PlayerBankData data = getPlayerBankData(player.getUniqueId());
        if (data.investCoins(amount)) {
            player.sendMessage("§a" + amount + " coins investiert!");
        } else {
            player.sendMessage("§cNicht genügend coins zum Investieren!");
        }
    }
    
    public void takeLoan(Player player, int amount) {
        PlayerBankData data = getPlayerBankData(player.getUniqueId());
        if (data.takeLoan(amount)) {
            player.sendMessage("§a" + amount + " coins als Kredit erhalten!");
        } else {
            player.sendMessage("§cKreditlimit erreicht!");
        }
    }
    
    public void payLoan(Player player, int amount) {
        PlayerBankData data = getPlayerBankData(player.getUniqueId());
        if (data.payLoan(amount)) {
            player.sendMessage("§a" + amount + " coins Kredit zurückgezahlt!");
        } else {
            player.sendMessage("§cNicht genügend coins zum Zurückzahlen!");
        }
    }
    
    public PlayerBankData getPlayerBankData(UUID playerId) {
        return playerBankData.computeIfAbsent(playerId, k -> new PlayerBankData(playerId));
    }
    
    public List<Bank> getBanks(BankType category) {
        return banks.getOrDefault(category, new ArrayList<>());
    }
    
    public enum BankType {
        BASIC("§aBasic", "§7Basic banks for storing coins and items"),
        INVESTMENT("§bInvestment", "§7Investment banks that grow your money"),
        LOAN("§eLoan", "§7Loan banks that provide loans"),
        TRADING("§aTrading", "§7Trading banks for buying and selling"),
        SPECIAL("§dSpecial", "§7Special banks with unique features");
        
        private final String displayName;
        private final String description;
        
        BankType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum BankRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        BankRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class Bank {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final BankType type;
        private final BankRarity rarity;
        private final int level;
        private final List<String> features;
        private final List<String> benefits;
        private final int coinLimit;
        private final int itemSlots;
        
        public Bank(String name, String displayName, Material material, String description,
                   BankType type, BankRarity rarity, int level, List<String> features,
                   List<String> benefits, int coinLimit, int itemSlots) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.level = level;
            this.features = features;
            this.benefits = benefits;
            this.coinLimit = coinLimit;
            this.itemSlots = itemSlots;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public BankType getType() { return type; }
        public BankRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
        public List<String> getFeatures() { return features; }
        public List<String> getBenefits() { return benefits; }
        public int getCoinLimit() { return coinLimit; }
        public int getItemSlots() { return itemSlots; }
    }
    
    public static class PlayerBankData {
        private final UUID playerId;
        
        // Multi-Currency Support
        private double coins;
        private double tokens;
        private double gems;
        private double investedCoins;
        private double loanAmount;
        private long lastUpdate;
        
        // Banking Level and XP
        private int bankingLevel;
        private double bankingXP;
        private double totalXP;
        
        // Bank Upgrades
        private int coinCapacityUpgrade;
        private int tokenCapacityUpgrade;
        private int gemCapacityUpgrade;
        private int interestRateUpgrade;
        private int securityUpgrade;
        
        // Transaction History
        private final List<BankTransaction> transactionHistory = new ArrayList<>();
        
        // Security Features
        private boolean twoFactorEnabled;
        private String securityPin;
        private long lastLoginTime;
        private int failedLoginAttempts;
        
        public PlayerBankData(UUID playerId) {
            this.playerId = playerId;
            this.coins = 0;
            this.tokens = 0;
            this.gems = 0;
            this.investedCoins = 0;
            this.loanAmount = 0;
            this.lastUpdate = System.currentTimeMillis();
            
            this.bankingLevel = 1;
            this.bankingXP = 0;
            this.totalXP = 0;
            
            this.coinCapacityUpgrade = 0;
            this.tokenCapacityUpgrade = 0;
            this.gemCapacityUpgrade = 0;
            this.interestRateUpgrade = 0;
            this.securityUpgrade = 0;
            
            this.twoFactorEnabled = false;
            this.securityPin = null;
            this.lastLoginTime = System.currentTimeMillis();
            this.failedLoginAttempts = 0;
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        // Coin Operations
        public void depositCoins(double amount) {
            if (amount > 0 && canDeposit(amount, CurrencyType.COINS)) {
                this.coins += amount;
                addTransaction(new BankTransaction(TransactionType.DEPOSIT, CurrencyType.COINS, amount));
                addXP(amount * 0.001); // 0.1% of amount as XP
            }
        }
        
        public boolean withdrawCoins(double amount) {
            if (amount > 0 && this.coins >= amount) {
                this.coins -= amount;
                addTransaction(new BankTransaction(TransactionType.WITHDRAW, CurrencyType.COINS, amount));
                addXP(amount * 0.0005); // 0.05% of amount as XP
                return true;
            }
            return false;
        }
        
        // Token Operations
        public void depositTokens(double amount) {
            if (amount > 0 && canDeposit(amount, CurrencyType.TOKENS)) {
                this.tokens += amount;
                addTransaction(new BankTransaction(TransactionType.DEPOSIT, CurrencyType.TOKENS, amount));
                addXP(amount * 0.002); // Tokens give more XP
            }
        }
        
        public boolean withdrawTokens(double amount) {
            if (amount > 0 && this.tokens >= amount) {
                this.tokens -= amount;
                addTransaction(new BankTransaction(TransactionType.WITHDRAW, CurrencyType.TOKENS, amount));
                addXP(amount * 0.001);
                return true;
            }
            return false;
        }
        
        // Gem Operations
        public void depositGems(double amount) {
            if (amount > 0 && canDeposit(amount, CurrencyType.GEMS)) {
                this.gems += amount;
                addTransaction(new BankTransaction(TransactionType.DEPOSIT, CurrencyType.GEMS, amount));
                addXP(amount * 0.005); // Gems give the most XP
            }
        }
        
        public boolean withdrawGems(double amount) {
            if (amount > 0 && this.gems >= amount) {
                this.gems -= amount;
                addTransaction(new BankTransaction(TransactionType.WITHDRAW, CurrencyType.GEMS, amount));
                addXP(amount * 0.002);
                return true;
            }
            return false;
        }
        
        // Investment Operations
        public boolean investCoins(double amount) {
            if (amount > 0 && this.coins >= amount) {
                this.coins -= amount;
                this.investedCoins += amount;
                addTransaction(new BankTransaction(TransactionType.INVEST, CurrencyType.COINS, amount));
                addXP(amount * 0.002); // Investment gives more XP
                return true;
            }
            return false;
        }
        
        public void addInterest(double amount) {
            this.investedCoins += amount;
            addTransaction(new BankTransaction(TransactionType.INTEREST, CurrencyType.COINS, amount));
        }
        
        // Loan Operations
        public boolean takeLoan(double amount) {
            double maxLoan = getMaxLoanLimit();
            if (amount > 0 && this.loanAmount + amount <= maxLoan) {
                this.loanAmount += amount;
                addTransaction(new BankTransaction(TransactionType.LOAN, CurrencyType.COINS, amount));
                return true;
            }
            return false;
        }
        
        public boolean payLoan(double amount) {
            if (amount > 0 && this.loanAmount >= amount) {
                this.loanAmount -= amount;
                addTransaction(new BankTransaction(TransactionType.LOAN_PAYMENT, CurrencyType.COINS, amount));
                addXP(amount * 0.001);
                return true;
            }
            return false;
        }
        
        // Upgrade Operations
        public boolean upgradeCoinCapacity() {
            double cost = getUpgradeCost(UpgradeType.COIN_CAPACITY);
            if (this.coins >= cost) {
                this.coins -= cost;
                this.coinCapacityUpgrade++;
                addTransaction(new BankTransaction(TransactionType.UPGRADE, CurrencyType.COINS, cost));
                addXP(cost * 0.01);
                return true;
            }
            return false;
        }
        
        public boolean upgradeTokenCapacity() {
            double cost = getUpgradeCost(UpgradeType.TOKEN_CAPACITY);
            if (this.tokens >= cost) {
                this.tokens -= cost;
                this.tokenCapacityUpgrade++;
                addTransaction(new BankTransaction(TransactionType.UPGRADE, CurrencyType.TOKENS, cost));
                addXP(cost * 0.01);
                return true;
            }
            return false;
        }
        
        public boolean upgradeGemCapacity() {
            double cost = getUpgradeCost(UpgradeType.GEM_CAPACITY);
            if (this.gems >= cost) {
                this.gems -= cost;
                this.gemCapacityUpgrade++;
                addTransaction(new BankTransaction(TransactionType.UPGRADE, CurrencyType.GEMS, cost));
                addXP(cost * 0.01);
                return true;
            }
            return false;
        }
        
        public boolean upgradeInterestRate() {
            double cost = getUpgradeCost(UpgradeType.INTEREST_RATE);
            if (this.coins >= cost) {
                this.coins -= cost;
                this.interestRateUpgrade++;
                addTransaction(new BankTransaction(TransactionType.UPGRADE, CurrencyType.COINS, cost));
                addXP(cost * 0.01);
                return true;
            }
            return false;
        }
        
        public boolean upgradeSecurity() {
            double cost = getUpgradeCost(UpgradeType.SECURITY);
            if (this.coins >= cost) {
                this.coins -= cost;
                this.securityUpgrade++;
                addTransaction(new BankTransaction(TransactionType.UPGRADE, CurrencyType.COINS, cost));
                addXP(cost * 0.01);
                return true;
            }
            return false;
        }
        
        // Capacity Calculations
        private boolean canDeposit(double amount, CurrencyType type) {
            double current = getCurrentBalance(type);
            double max = getMaxCapacity(type);
            return current + amount <= max;
        }
        
        public double getMaxCapacity(CurrencyType type) {
            return switch (type) {
                case COINS -> 100000 + (coinCapacityUpgrade * 50000);
                case TOKENS -> 10000 + (tokenCapacityUpgrade * 5000);
                case GEMS -> 1000 + (gemCapacityUpgrade * 500);
            };
        }
        
        public double getCurrentBalance(CurrencyType type) {
            return switch (type) {
                case COINS -> coins;
                case TOKENS -> tokens;
                case GEMS -> gems;
            };
        }
        
        public double getMaxLoanLimit() {
            return 50000 + (bankingLevel * 10000);
        }
        
        public double getInterestRate() {
            return 0.01 + (interestRateUpgrade * 0.005); // Base 1% + upgrades
        }
        
        // XP and Level System
        public void addXP(double amount) {
            this.bankingXP += amount;
            this.totalXP += amount;
            
            int newLevel = calculateLevel(bankingXP);
            if (newLevel > bankingLevel) {
                this.bankingLevel = newLevel;
                // Level up rewards could be added here
            }
        }
        
        private int calculateLevel(double xp) {
            return (int) Math.floor(Math.sqrt(xp / 1000)) + 1;
        }
        
        public double getXPForNextLevel() {
            double xpForNext = Math.pow(bankingLevel, 2) * 1000;
            double xpForCurrent = Math.pow(bankingLevel - 1, 2) * 1000;
            return xpForNext - xpForCurrent;
        }
        
        public double getXPProgress() {
            double xpForNext = Math.pow(bankingLevel, 2) * 1000;
            double xpForCurrent = Math.pow(bankingLevel - 1, 2) * 1000;
            return (bankingXP - xpForCurrent) / (xpForNext - xpForCurrent);
        }
        
        // Upgrade Costs
        public double getUpgradeCost(UpgradeType type) {
            int level = switch (type) {
                case COIN_CAPACITY -> coinCapacityUpgrade;
                case TOKEN_CAPACITY -> tokenCapacityUpgrade;
                case GEM_CAPACITY -> gemCapacityUpgrade;
                case INTEREST_RATE -> interestRateUpgrade;
                case SECURITY -> securityUpgrade;
            };
            return Math.pow(2, level) * 1000; // Exponential cost increase
        }
        
        // Security Features
        public boolean setSecurityPin(String pin) {
            if (pin != null && pin.length() >= 4) {
                this.securityPin = pin;
                this.twoFactorEnabled = true;
                return true;
            }
            return false;
        }
        
        public boolean verifySecurityPin(String pin) {
            return this.securityPin != null && this.securityPin.equals(pin);
        }
        
        public void recordLoginAttempt(boolean success) {
            if (success) {
                this.failedLoginAttempts = 0;
                this.lastLoginTime = System.currentTimeMillis();
            } else {
                this.failedLoginAttempts++;
            }
        }
        
        public boolean isAccountLocked() {
            return failedLoginAttempts >= 5;
        }
        
        // Transaction Management
        private void addTransaction(BankTransaction transaction) {
            transactionHistory.add(transaction);
            // Keep only last 100 transactions
            if (transactionHistory.size() > 100) {
                transactionHistory.remove(0);
            }
        }
        
        // Getters
        public double getCoins() { return coins; }
        public double getTokens() { return tokens; }
        public double getGems() { return gems; }
        public double getInvestedCoins() { return investedCoins; }
        public double getLoanAmount() { return loanAmount; }
        public long getLastUpdate() { return lastUpdate; }
        public int getBankingLevel() { return bankingLevel; }
        public double getBankingXP() { return bankingXP; }
        public double getTotalXP() { return totalXP; }
        public List<BankTransaction> getTransactionHistory() { return new ArrayList<>(transactionHistory); }
        public boolean isTwoFactorEnabled() { return twoFactorEnabled; }
        public int getFailedLoginAttempts() { return failedLoginAttempts; }
        public long getLastLoginTime() { return lastLoginTime; }
        
        // Upgrade Levels
        public int getCoinCapacityUpgrade() { return coinCapacityUpgrade; }
        public int getTokenCapacityUpgrade() { return tokenCapacityUpgrade; }
        public int getGemCapacityUpgrade() { return gemCapacityUpgrade; }
        public int getInterestRateUpgrade() { return interestRateUpgrade; }
        public int getSecurityUpgrade() { return securityUpgrade; }
    }
    
    // Enums for Banking System
    public enum CurrencyType {
        COINS("§6Coins", "§7The main currency"),
        TOKENS("§bTokens", "§7Premium currency"),
        GEMS("§dGems", "§7Rare currency");
        
        private final String displayName;
        private final String description;
        
        CurrencyType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum TransactionType {
        DEPOSIT("§aDeposit"),
        WITHDRAW("§cWithdraw"),
        INVEST("§bInvest"),
        INTEREST("§eInterest"),
        LOAN("§6Loan"),
        LOAN_PAYMENT("§aLoan Payment"),
        UPGRADE("§dUpgrade"),
        TRANSFER("§9Transfer");
        
        private final String displayName;
        
        TransactionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    public enum UpgradeType {
        COIN_CAPACITY("§6Coin Capacity"),
        TOKEN_CAPACITY("§bToken Capacity"),
        GEM_CAPACITY("§dGem Capacity"),
        INTEREST_RATE("§eInterest Rate"),
        SECURITY("§cSecurity");
        
        private final String displayName;
        
        UpgradeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    // Bank Transaction Class
    public static class BankTransaction {
        private final TransactionType type;
        private final CurrencyType currency;
        private final double amount;
        private final long timestamp;
        
        public BankTransaction(TransactionType type, CurrencyType currency, double amount) {
            this.type = type;
            this.currency = currency;
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
        }
        
        public TransactionType getType() { return type; }
        public CurrencyType getCurrency() { return currency; }
        public double getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
        
        public String getFormattedTime() {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(new java.util.Date(timestamp));
        }
    }
}
