package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.core.CorePlatform;
import de.noctivag.plugin.economy.EconomySystem;
import de.noctivag.plugin.pets.PetSystem;
import de.noctivag.plugin.skyblock.SkyblockManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Ultimate GUI System - Umfassendes GUI-System für alle Plugin-Features
 * 
 * Verantwortlich für:
 * - Hauptmenü-Navigation
 * - Feature-spezifische GUIs
 * - GUI-Templates
 * - Animationen
 * - Benutzerfreundlichkeit
 * - Responsive Design
 */
public class UltimateGUISystem {
    private final Plugin plugin;
    private final CorePlatform corePlatform;
    private final EconomySystem economySystem;
    private final PetSystem petSystem;
    private final SkyblockManager skyblockManager;
    
    // GUI Templates
    private final Map<String, GUITemplate> templates = new HashMap<>();
    private final Map<UUID, GUISession> activeSessions = new HashMap<>();
    
    public UltimateGUISystem(Plugin plugin, CorePlatform corePlatform, EconomySystem economySystem, 
                           PetSystem petSystem, SkyblockManager skyblockManager) {
        this.plugin = plugin;
        this.corePlatform = corePlatform;
        this.economySystem = economySystem;
        this.petSystem = petSystem;
        this.skyblockManager = skyblockManager;
        
        initializeTemplates();
    }
    
    private void initializeTemplates() {
        // Main Menu Template
        GUITemplate mainMenu = new GUITemplate("main_menu", 54, "§6§lUltimate Menu");
        mainMenu.setBorder(Material.BLACK_STAINED_GLASS_PANE);
        mainMenu.addItem(10, Material.NETHER_STAR, "§6§l⚙️ CORE FEATURES", 
            Arrays.asList("§7Wichtige Plugin-Funktionen", "§7• Einstellungen", "§7• Profil", "§7• Feature-Buch", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(12, Material.GOLD_INGOT, "§6§l🏆 ACHIEVEMENTS", 
            Arrays.asList("§7Schaue deine Achievements an", "§7• Belohnungen und Fortschritt", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(14, Material.CHEST, "§e§lKITS & BELOHNUNGEN", 
            Arrays.asList("§7Erhalte tägliche Belohnungen", "§7• Tägliche Belohnungen", "§7• Kits", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(16, Material.ENDER_CHEST, "§d§lKOSMETIK", 
            Arrays.asList("§7Passe dein Aussehen an", "§7• Partikel-Effekte", "§7• Wings & Halo", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(19, Material.DIAMOND_SWORD, "§b§lKAMPF & EVENTS", 
            Arrays.asList("§7Tritt Events bei und kämpfe", "§7• Boss-Events", "§7• PvP-Arenen", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(21, Material.PLAYER_HEAD, "§a§lFREUNDE & PARTY", 
            Arrays.asList("§7Verwalte deine Freunde", "§7• Freundesliste", "§7• Party-System", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(23, Material.COMPASS, "§c§lTELEPORTATION", 
            Arrays.asList("§7Teleportiere dich", "§7• Warps", "§7• Homes", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(25, Material.CRAFTING_TABLE, "§f§lBASIC COMMANDS", 
            Arrays.asList("§7Nützliche Befehle", "§7• Workbenches", "§7• Utility", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(28, Material.WRITABLE_BOOK, "§7§lNACHRICHTEN", 
            Arrays.asList("§7Verwalte Join/Leave", "§7• Nachrichten", "§7• Presets", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(30, Material.SHIELD, "§6§l🏰 GUILD SYSTEM", 
            Arrays.asList("§7Erstelle oder trete bei", "§7• Guild-Erstellung", "§7• Guild-Wars", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(32, Material.REDSTONE_TORCH, "§4§lADMIN-TOOLS", 
            Arrays.asList("§7Tools für Administratoren", "§7• Server-Verwaltung", "§7• Player-Management", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(34, Material.LECTERN, "§3§lPLUGIN FEATURES", 
            Arrays.asList("§7Alle Funktionen", "§7• Feature-Buch", "§7• Hilfe", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(37, Material.COMPARATOR, "§2§lEINSTELLUNGEN", 
            Arrays.asList("§7Passe deine Einstellungen an", "§7• GUI-Einstellungen", "§7• Benachrichtigungen", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(39, Material.LECTERN, "§3§l📊 SERVER INFO", 
            Arrays.asList("§7Wichtige Server-Informationen", "§7• Online-Spieler", "§7• Server-Uptime", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(41, Material.BOOK, "§9§lHILFE & SUPPORT", 
            Arrays.asList("§7Hilfe und Support", "§7• FAQ", "§7• Kontakt", "", "§eKlicke zum Öffnen"));
        mainMenu.addItem(49, Material.BARRIER, "§c§l❌ SCHLIESSEN", 
            Arrays.asList("§7Schließt das Menü", "", "§eKlicke zum Schließen"));
        templates.put("main_menu", mainMenu);
        
        // Economy Menu Template
        GUITemplate economyMenu = new GUITemplate("economy_menu", 54, "§6§lEconomy System");
        economyMenu.setBorder(Material.GOLD_BLOCK);
        economyMenu.addItem(10, Material.GOLD_INGOT, "§6§l💰 BALANCE", 
            Arrays.asList("§7Schaue dein Guthaben an", "§7• Aktueller Kontostand", "§7• Transaktions-Historie", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(12, Material.ANVIL, "§e§l🔨 AUCTION HOUSE", 
            Arrays.asList("§7Kaufe und verkaufe Items", "§7• Auktionen erstellen", "§7• Bieten", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(14, Material.EMERALD, "§a§l💎 BAZAAR", 
            Arrays.asList("§7Instant Trading", "§7• Buy/Sell Orders", "§7• Market Analysis", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(16, Material.VILLAGER_SPAWN_EGG, "§b§l🏪 NPC SHOPS", 
            Arrays.asList("§7Kaufe von NPCs", "§7• Verschiedene Shops", "§7• Kategorien", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(19, Material.PLAYER_HEAD, "§d§l🤝 TRADING", 
            Arrays.asList("§7Trade mit anderen Spielern", "§7• Trade Requests", "§7• Trade History", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(21, Material.BOOK, "§9§l📊 MARKET ANALYSIS", 
            Arrays.asList("§7Marktanalyse und Trends", "§7• Preisvorhersagen", "§7• Volatilität", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(23, Material.CLOCK, "§6§l⏰ PRICE HISTORY", 
            Arrays.asList("§7Preisverlauf anzeigen", "§7• Historische Daten", "§7• Charts", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(25, Material.REDSTONE_TORCH, "§c§l⚡ MARKET ALERTS", 
            Arrays.asList("§7Marktbenachrichtigungen", "§7• Preisalarme", "§7• Trend-Warnungen", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(28, Material.GOLD_BLOCK, "§6§l💎 COIN EXCHANGE", 
            Arrays.asList("§7Tausche Coins", "§7• Währungsumtausch", "§7• Kurse", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(30, Material.ENDER_CHEST, "§5§l📦 INVENTORY", 
            Arrays.asList("§7Verwalte dein Inventar", "§7• Items verkaufen", "§7• Bulk Operations", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(32, Material.BOOK, "§9§l📚 ECONOMY GUIDE", 
            Arrays.asList("§7Economy-System Guide", "§7• Tipps & Tricks", "§7• Strategien", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(34, Material.REDSTONE_TORCH, "§c§l⚙️ ECONOMY SETTINGS", 
            Arrays.asList("§7Economy-Einstellungen", "§7• Benachrichtigungen", "§7• Präferenzen", "", "§eKlicke zum Öffnen"));
        economyMenu.addItem(49, Material.ARROW, "§7§l← ZURÜCK", 
            Arrays.asList("§7Zum Hauptmenü", "", "§eKlicke zum Zurückgehen"));
        templates.put("economy_menu", economyMenu);
        
        // Pet Menu Template
        GUITemplate petMenu = new GUITemplate("pet_menu", 54, "§d§lPet System");
        petMenu.setBorder(Material.PINK_STAINED_GLASS_PANE);
        petMenu.addItem(10, Material.WOLF_SPAWN_EGG, "§a§l🐕 MY PETS", 
            Arrays.asList("§7Verwalte deine Haustiere", "§7• Pet-Liste", "§7• Pet-Details", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(12, Material.GOLD_INGOT, "§6§l🏪 PET SHOP", 
            Arrays.asList("§7Kaufe neue Haustiere", "§7• Verschiedene Rassen", "§7• Seltene Pets", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(14, Material.EXPERIENCE_BOTTLE, "§a§l📈 PET LEVELING", 
            Arrays.asList("§7Level deine Haustiere", "§7• XP sammeln", "§7• Level-Ups", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(16, Material.DIAMOND, "§b§l💎 PET UPGRADES", 
            Arrays.asList("§7Upgrade deine Haustiere", "§7• Stats verbessern", "§7• Fähigkeiten", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(19, Material.BREAD, "§e§l🍞 PET CARE", 
            Arrays.asList("§7Pflege deine Haustiere", "§7• Füttern", "§7• Glück", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(21, Material.CHEST, "§6§l📦 PET INVENTORY", 
            Arrays.asList("§7Pet-Inventar verwalten", "§7• Items sammeln", "§7• Auto-Collect", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(23, Material.BOOK, "§9§l📚 PET GUIDE", 
            Arrays.asList("§7Pet-System Guide", "§7• Tipps & Tricks", "§7• Rassen-Info", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(25, Material.REDSTONE_TORCH, "§c§l⚙️ PET SETTINGS", 
            Arrays.asList("§7Pet-Einstellungen", "§7• Auto-Feed", "§7• Benachrichtigungen", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(28, Material.GOLD_INGOT, "§6§l🏆 PET ACHIEVEMENTS", 
            Arrays.asList("§7Pet-Achievements", "§7• Erfolge", "§7• Belohnungen", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(30, Material.PLAYER_HEAD, "§a§l👥 PET SOCIAL", 
            Arrays.asList("§7Pet-Social Features", "§7• Pet-Showcase", "§7• Pet-Battles", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(32, Material.ENDER_CHEST, "§5§l📦 PET STORAGE", 
            Arrays.asList("§7Pet-Storage verwalten", "§7• Items lagern", "§7• Organisieren", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(34, Material.BOOK, "§9§l📚 PET ENCYCLOPEDIA", 
            Arrays.asList("§7Pet-Enzyklopädie", "§7• Alle Rassen", "§7• Stats & Info", "", "§eKlicke zum Öffnen"));
        petMenu.addItem(49, Material.ARROW, "§7§l← ZURÜCK", 
            Arrays.asList("§7Zum Hauptmenü", "", "§eKlicke zum Zurückgehen"));
        templates.put("pet_menu", petMenu);
        
        // Skyblock Menu Template
        GUITemplate skyblockMenu = new GUITemplate("skyblock_menu", 54, "§b§lSkyblock System");
        skyblockMenu.setBorder(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        skyblockMenu.addItem(10, Material.GRASS_BLOCK, "§a§l🏝️ ISLAND", 
            Arrays.asList("§7Verwalte deine Insel", "§7• Insel-Info", "§7• Upgrades", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(12, Material.IRON_GOLEM_SPAWN_EGG, "§6§l🤖 MINIONS", 
            Arrays.asList("§7Verwalte deine Minions", "§7• Minion-Liste", "§7• Upgrades", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(14, Material.EXPERIENCE_BOTTLE, "§a§l📈 SKILLS", 
            Arrays.asList("§7Schaue deine Skills an", "§7• Farming", "§7• Mining", "§7• Combat", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(16, Material.CHEST, "§e§l📦 COLLECTIONS", 
            Arrays.asList("§7Schaue deine Sammlungen an", "§7• Items sammeln", "§7• Belohnungen", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(19, Material.DIAMOND_SWORD, "§c§l⚔️ SLAYERS", 
            Arrays.asList("§7Bekämpfe Slayer-Bosse", "§7• Verschiedene Bosse", "§7• Belohnungen", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(21, Material.ENCHANTING_TABLE, "§d§l✨ ENCHANTING", 
            Arrays.asList("§7Verzaubere deine Items", "§7• Enchantments", "§7• Reforging", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(23, Material.ENDER_CHEST, "§5§l🏦 BANK", 
            Arrays.asList("§7Verwalte deine Bank", "§7• Coins lagern", "§7• Zinsen", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(25, Material.COMPASS, "§c§l🧭 TELEPORTATION", 
            Arrays.asList("§7Teleportiere dich", "§7• Insel", "§7• Hub", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(28, Material.BOOK, "§9§l📚 SKYBLOCK GUIDE", 
            Arrays.asList("§7Skyblock-System Guide", "§7• Tipps & Tricks", "§7• Strategien", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(30, Material.REDSTONE_TORCH, "§c§l⚙️ SKYBLOCK SETTINGS", 
            Arrays.asList("§7Skyblock-Einstellungen", "§7• Benachrichtigungen", "§7• Präferenzen", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(32, Material.GOLD_INGOT, "§6§l🏆 SKYBLOCK ACHIEVEMENTS", 
            Arrays.asList("§7Skyblock-Achievements", "§7• Erfolge", "§7• Belohnungen", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(34, Material.PLAYER_HEAD, "§a§l👥 SKYBLOCK SOCIAL", 
            Arrays.asList("§7Skyblock-Social Features", "§7• Insel-Besuche", "§7• Kooperation", "", "§eKlicke zum Öffnen"));
        skyblockMenu.addItem(49, Material.ARROW, "§7§l← ZURÜCK", 
            Arrays.asList("§7Zum Hauptmenü", "", "§eKlicke zum Zurückgehen"));
        templates.put("skyblock_menu", skyblockMenu);
    }
    
    public void openMainMenu(Player player) {
        GUITemplate template = templates.get("main_menu");
        if (template == null) return;
        
        // Create session
        GUISession session = new GUISession(player.getUniqueId(), "main_menu");
        activeSessions.put(player.getUniqueId(), session);
        
        // Open GUI
        openGUI(player, template);
    }
    
    public void openEconomyMenu(Player player) {
        GUITemplate template = templates.get("economy_menu");
        if (template == null) return;
        
        // Create session
        GUISession session = new GUISession(player.getUniqueId(), "economy_menu");
        activeSessions.put(player.getUniqueId(), session);
        
        // Open GUI
        openGUI(player, template);
    }
    
    public void openPetMenu(Player player) {
        GUITemplate template = templates.get("pet_menu");
        if (template == null) return;
        
        // Create session
        GUISession session = new GUISession(player.getUniqueId(), "pet_menu");
        activeSessions.put(player.getUniqueId(), session);
        
        // Open GUI
        openGUI(player, template);
    }
    
    public void openSkyblockMenu(Player player) {
        GUITemplate template = templates.get("skyblock_menu");
        if (template == null) return;
        
        // Create session
        GUISession session = new GUISession(player.getUniqueId(), "skyblock_menu");
        activeSessions.put(player.getUniqueId(), session);
        
        // Open GUI
        openGUI(player, template);
    }
    
    private void openGUI(Player player, GUITemplate template) {
        org.bukkit.inventory.Inventory inventory = org.bukkit.Bukkit.createInventory(null, template.getSize(), template.getTitle());
        
        // Fill border
        if (template.getBorder() != null) {
            for (int i = 0; i < template.getSize(); i++) {
                if (i < 9 || i >= template.getSize() - 9 || i % 9 == 0 || i % 9 == 8) {
                    inventory.setItem(i, new ItemStack(template.getBorder()));
                }
            }
        }
        
        // Add items
        for (Map.Entry<Integer, GUIItem> entry : template.getItems().entrySet()) {
            inventory.setItem(entry.getKey(), createItemStack(entry.getValue()));
        }
        
        player.openInventory(inventory);
    }
    
    private ItemStack createItemStack(GUIItem item) {
        ItemStack itemStack = new ItemStack(item.getMaterial());
        ItemMeta meta = itemStack.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(item.getName()));
            List<Component> componentLore = new ArrayList<>();
            for (String lore : item.getLore()) {
                componentLore.add(Component.text(lore));
            }
            meta.lore(componentLore);
            itemStack.setItemMeta(meta);
        }
        
        return itemStack;
    }
    
    public void handleClick(Player player, int slot) {
        GUISession session = activeSessions.get(player.getUniqueId());
        if (session == null) return;
        
        GUITemplate template = templates.get(session.getCurrentGUI());
        if (template == null) return;
        
        GUIItem item = template.getItem(slot);
        if (item == null) return;
        
        // Handle click based on current GUI
        switch (session.getCurrentGUI()) {
            case "main_menu" -> handleMainMenuClick(player, slot);
            case "economy_menu" -> handleEconomyMenuClick(player, slot);
            case "pet_menu" -> handlePetMenuClick(player, slot);
            case "skyblock_menu" -> handleSkyblockMenuClick(player, slot);
        }
    }
    
    private void handleMainMenuClick(Player player, int slot) {
        switch (slot) {
            case 10 -> {
                // Core Features
                new CoreFeaturesGUI(plugin).openCoreFeaturesGUI(player);
            }
            case 12 -> {
                // Achievements
                new AchievementsGUI(plugin).openAchievementsGUI(player);
            }
            case 14 -> {
                // Kits & Rewards
                new KitsGUI(plugin).openKitsGUI(player);
            }
            case 16 -> {
                // Cosmetics
                new CosmeticsGUI(plugin).openCosmeticsGUI(player);
            }
            case 19 -> {
                // Combat & Events
                new CombatGUI(plugin).openCombatGUI(player);
            }
            case 21 -> {
                // Friends & Party
                new FriendsGUI(plugin).openFriendsGUI(player);
            }
            case 23 -> {
                // Teleportation
                new TeleportationGUI(plugin).openTeleportationGUI(player);
            }
            case 25 -> {
                // Basic Commands
                new BasicCommandsGUI(plugin).openBasicCommandsGUI(player);
            }
            case 28 -> {
                // Messages
                new MessagesGUI(plugin).openMessagesGUI(player);
            }
            case 30 -> {
                // Guild System
                new GuildGUI(plugin).openGuildGUI(player);
            }
            case 32 -> {
                // Admin Tools
                new AdminGUI(plugin).openAdminGUI(player);
            }
            case 34 -> {
                // Plugin Features
                new FeaturesGUI(plugin).openFeaturesGUI(player);
            }
            case 37 -> {
                // Settings
                new SettingsGUI(plugin).openSettingsGUI(player);
            }
            case 39 -> {
                // Server Info
                new ServerInfoGUI(plugin).openServerInfoGUI(player);
            }
            case 41 -> {
                // Help & Support
                new HelpGUI(plugin).openHelpGUI(player);
            }
            case 49 -> {
                // Close
                player.closeInventory();
            }
        }
    }
    
    private void handleEconomyMenuClick(Player player, int slot) {
        switch (slot) {
            case 10 -> {
                // Balance
                new BalanceGUI(plugin).openBalanceGUI(player);
            }
            case 12 -> {
                // Auction House
                new AuctionHouseGUI(plugin).openAuctionHouseGUI(player);
            }
            case 14 -> {
                // Bazaar
                new BazaarGUI(plugin).openBazaarGUI(player);
            }
            case 16 -> {
                // NPC Shops
                new NPCShopsGUI(plugin).openNPCShopsGUI(player);
            }
            case 19 -> {
                // Trading
                new TradingGUI(plugin).openTradingGUI(player);
            }
            case 21 -> {
                // Market Analysis
                new MarketAnalysisGUI(plugin).openMarketAnalysisGUI(player);
            }
            case 23 -> {
                // Price History
                new PriceHistoryGUI(plugin).openPriceHistoryGUI(player);
            }
            case 25 -> {
                // Market Alerts
                new MarketAlertsGUI(plugin).openMarketAlertsGUI(player);
            }
            case 28 -> {
                // Coin Exchange
                new CoinExchangeGUI(plugin).openCoinExchangeGUI(player);
            }
            case 30 -> {
                // Inventory
                new InventoryGUI(plugin).openInventoryGUI(player);
            }
            case 32 -> {
                // Economy Guide
                new EconomyGuideGUI(plugin).openEconomyGuideGUI(player);
            }
            case 34 -> {
                // Economy Settings
                new EconomySettingsGUI(plugin).openEconomySettingsGUI(player);
            }
            case 49 -> {
                // Back
                openMainMenu(player);
            }
        }
    }
    
    private void handlePetMenuClick(Player player, int slot) {
        switch (slot) {
            case 10 -> {
                // My Pets
                new PetGUI(plugin).openPetGUI(player);
            }
            case 12 -> {
                // Pet Shop
                new PetShopGUI(plugin).openPetShopGUI(player);
            }
            case 14 -> {
                // Pet Leveling
                new PetLevelingGUI(plugin).openPetLevelingGUI(player);
            }
            case 16 -> {
                // Pet Upgrades
                new PetUpgradesGUI(plugin).openPetUpgradesGUI(player);
            }
            case 19 -> {
                // Pet Care
                new PetCareGUI(plugin).openPetCareGUI(player);
            }
            case 21 -> {
                // Pet Inventory
                new PetInventoryGUI(plugin).openPetInventoryGUI(player);
            }
            case 23 -> {
                // Pet Guide
                new PetGuideGUI(plugin).openPetGuideGUI(player);
            }
            case 25 -> {
                // Pet Settings
                new PetSettingsGUI(plugin).openPetSettingsGUI(player);
            }
            case 28 -> {
                // Pet Achievements
                new PetAchievementsGUI(plugin).openPetAchievementsGUI(player);
            }
            case 30 -> {
                // Pet Social
                new PetSocialGUI(plugin).openPetSocialGUI(player);
            }
            case 32 -> {
                // Pet Storage
                new PetStorageGUI(plugin).openPetStorageGUI(player);
            }
            case 34 -> {
                // Pet Encyclopedia
                new PetEncyclopediaGUI(plugin).openPetEncyclopediaGUI(player);
            }
            case 49 -> {
                // Back
                openMainMenu(player);
            }
        }
    }
    
    private void handleSkyblockMenuClick(Player player, int slot) {
        switch (slot) {
            case 10 -> {
                // Island
                new SkyblockGUI(plugin).openSkyblockGUI(player);
            }
            case 12 -> {
                // Minions
                new MinionsGUI(plugin).openMinionsGUI(player);
            }
            case 14 -> {
                // Skills
                new SkillsGUI(plugin).openSkillsGUI(player);
            }
            case 16 -> {
                // Collections
                new CollectionsGUI(plugin).openCollectionsGUI(player);
            }
            case 19 -> {
                // Slayers
                new SlayersGUI(plugin).openSlayersGUI(player);
            }
            case 21 -> {
                // Enchanting
                new EnchantingGUI(plugin, player, null).openGUI(player); // EnchantingSystem not implemented yet
            }
            case 23 -> {
                // Bank
                new BankGUI(plugin).openBankGUI(player);
            }
            case 25 -> {
                // Teleportation
                new TeleportationGUI(plugin).openTeleportationGUI(player);
            }
            case 28 -> {
                // Skyblock Guide
                new SkyblockGuideGUI(plugin).openSkyblockGuideGUI(player);
            }
            case 30 -> {
                // Skyblock Settings
                new SkyblockSettingsGUI(plugin).openSkyblockSettingsGUI(player);
            }
            case 32 -> {
                // Skyblock Achievements
                new SkyblockAchievementsGUI(plugin).openSkyblockAchievementsGUI(player);
            }
            case 34 -> {
                // Skyblock Social
                new SkyblockSocialGUI(plugin).openSkyblockSocialGUI(player);
            }
            case 49 -> {
                // Back
                openMainMenu(player);
            }
        }
    }
    
    public void closeSession(Player player) {
        activeSessions.remove(player.getUniqueId());
    }
    
    public GUISession getSession(Player player) {
        return activeSessions.get(player.getUniqueId());
    }
    
    // GUI Template Class
    public static class GUITemplate {
        private final String id;
        private final int size;
        private final String title;
        private Material border;
        private final Map<Integer, GUIItem> items = new HashMap<>();
        
        public GUITemplate(String id, int size, String title) {
            this.id = id;
            this.size = size;
            this.title = title;
        }
        
        public void setBorder(Material border) {
            this.border = border;
        }
        
        public void addItem(int slot, Material material, String name, List<String> lore) {
            items.put(slot, new GUIItem(material, name, lore));
        }
        
        public String getId() { return id; }
        public int getSize() { return size; }
        public String getTitle() { return title; }
        public Material getBorder() { return border; }
        public Map<Integer, GUIItem> getItems() { return new HashMap<>(items); }
        public GUIItem getItem(int slot) { return items.get(slot); }
    }
    
    // GUI Item Class
    public static class GUIItem {
        private final Material material;
        private final String name;
        private final List<String> lore;
        
        public GUIItem(Material material, String name, List<String> lore) {
            this.material = material;
            this.name = name;
            this.lore = lore;
        }
        
        public Material getMaterial() { return material; }
        public String getName() { return name; }
        public List<String> getLore() { return new ArrayList<>(lore); }
    }
    
    // GUI Session Class
    public static class GUISession {
        private final UUID playerId;
        private final String currentGUI;
        private final long startTime;
        
        public GUISession(UUID playerId, String currentGUI) {
            this.playerId = playerId;
            this.currentGUI = currentGUI;
            this.startTime = System.currentTimeMillis();
        }
        
        public UUID getPlayerId() { return playerId; }
        public String getCurrentGUI() { return currentGUI; }
        public long getStartTime() { return startTime; }
    }
}
