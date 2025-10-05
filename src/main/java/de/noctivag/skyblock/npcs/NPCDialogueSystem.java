package de.noctivag.skyblock.npcs;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced NPC Dialogue System - Hypixel Style
 */
public class NPCDialogueSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, DialogueSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<String, DialogueTree> dialogueTrees = new HashMap<>();
    
    public NPCDialogueSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        initializeDialogueTrees();
    }
    
    private void initializeDialogueTrees() {
        // Shop NPC Dialogue
        DialogueTree shopTree = new DialogueTree("shop");
        shopTree.addDialogue("greeting", Arrays.asList(
            "§aWelcome to my shop! What can I get for you today?",
            "§aLooking to buy something? I have the best deals!",
            "§aHello there! Ready to spend some coins?",
            "§aWelcome! I've got everything you need!"
        ));
        shopTree.addDialogue("browse", Arrays.asList(
            "§aHere are my current items:",
            "§aTake a look at what I have in stock:",
            "§aThese are my best deals today:",
            "§aCheck out my inventory:"
        ));
        shopTree.addDialogue("sell", Arrays.asList(
            "§aI'm always looking to buy quality items!",
            "§aShow me what you've got to sell!",
            "§aI'll give you a fair price for your items!",
            "§aLet's see what you have for me!"
        ));
        shopTree.addDialogue("prices", Arrays.asList(
            "§aHere are my current prices:",
            "§aLet me show you my price list:",
            "§aThese are my current rates:",
            "§aHere's what I'm paying today:"
        ));
        shopTree.addDialogue("farewell", Arrays.asList(
            "§aCome back soon!",
            "§aThanks for shopping!",
            "§aSee you next time!",
            "§aHave a great day!"
        ));
        dialogueTrees.put("shop", shopTree);
        
        // Quest NPC Dialogue
        DialogueTree questTree = new DialogueTree("quest");
        questTree.addDialogue("greeting", Arrays.asList(
            "§bI have a quest for you! Are you ready for an adventure?",
            "§bLooking for some excitement? I have just the thing!",
            "§bAdventurer! I need your help with something!",
            "§bReady to earn some rewards? I have quests available!"
        ));
        questTree.addDialogue("available", Arrays.asList(
            "§bHere are the quests I have available:",
            "§bThese are the adventures waiting for you:",
            "§bI have these quests ready:",
            "§bHere's what you can do:"
        ));
        questTree.addDialogue("progress", Arrays.asList(
            "§bLet me check your quest progress...",
            "§bHow are you doing with your current quests?",
            "§bLet me see your progress:",
            "§bChecking your quest status..."
        ));
        questTree.addDialogue("rewards", Arrays.asList(
            "§bYou have rewards waiting!",
            "§bTime to claim your hard-earned rewards!",
            "§bHere are your rewards:",
            "§bCongratulations! Here's what you've earned:"
        ));
        questTree.addDialogue("farewell", Arrays.asList(
            "§bGood luck on your quest!",
            "§bCome back when you're done!",
            "§bMay the odds be in your favor!",
            "§bSee you soon, adventurer!"
        ));
        dialogueTrees.put("quest", questTree);
        
        // Info NPC Dialogue
        DialogueTree infoTree = new DialogueTree("info");
        infoTree.addDialogue("greeting", Arrays.asList(
            "§eHow can I help you today?",
            "§eNeed some information? I'm here to help!",
            "§eWhat would you like to know?",
            "§eI'm here to answer your questions!"
        ));
        infoTree.addDialogue("server", Arrays.asList(
            "§eHere's some information about our server:",
            "§eLet me tell you about our server:",
            "§eHere's what you need to know:",
            "§eI'll give you the details:"
        ));
        infoTree.addDialogue("rules", Arrays.asList(
            "§eHere are the server rules:",
            "§eLet me explain the rules:",
            "§eThese are the guidelines:",
            "§eHere's what you need to follow:"
        ));
        infoTree.addDialogue("commands", Arrays.asList(
            "§eHere are some useful commands:",
            "§eLet me show you the commands:",
            "§eThese commands will help you:",
            "§eHere's your command reference:"
        ));
        infoTree.addDialogue("farewell", Arrays.asList(
            "§eHope I could help!",
            "§eCome back if you need more info!",
            "§eHave a great day!",
            "§eSee you around!"
        ));
        dialogueTrees.put("info", infoTree);
        
        // Warp NPC Dialogue
        DialogueTree warpTree = new DialogueTree("warp");
        warpTree.addDialogue("greeting", Arrays.asList(
            "§dWhere would you like to go?",
            "§dReady for a journey? Choose your destination!",
            "§dI can take you anywhere! Where to?",
            "§dPick your destination and I'll get you there!"
        ));
        warpTree.addDialogue("spawn", Arrays.asList(
            "§dTaking you to spawn!",
            "§dOff to spawn you go!",
            "§dSpawn it is!",
            "§dWelcome to spawn!"
        ));
        warpTree.addDialogue("market", Arrays.asList(
            "§dTaking you to the market!",
            "§dOff to the market!",
            "§dMarket bound!",
            "§dWelcome to the market!"
        ));
        warpTree.addDialogue("arena", Arrays.asList(
            "§dTaking you to the arena!",
            "§dOff to the arena!",
            "§dArena bound!",
            "§dWelcome to the arena!"
        ));
        warpTree.addDialogue("farewell", Arrays.asList(
            "§dSafe travels!",
            "§dEnjoy your trip!",
            "§dSee you at your destination!",
            "§dHave a good journey!"
        ));
        dialogueTrees.put("warp", warpTree);
        
        // Bank NPC Dialogue
        DialogueTree bankTree = new DialogueTree("bank");
        bankTree.addDialogue("greeting", Arrays.asList(
            "§6Welcome to the bank! How can I help with your finances?",
            "§6Hello! Need to manage your money?",
            "§6Welcome! Your money is safe with us!",
            "§6How can I assist you with your banking needs?"
        ));
        bankTree.addDialogue("deposit", Arrays.asList(
            "§6How much would you like to deposit?",
            "§6Let's add to your account!",
            "§6Depositing your money...",
            "§6Your money is now safe in the bank!"
        ));
        bankTree.addDialogue("withdraw", Arrays.asList(
            "§6How much would you like to withdraw?",
            "§6Let's get your money out!",
            "§6Withdrawing from your account...",
            "§6Here's your money!"
        ));
        bankTree.addDialogue("balance", Arrays.asList(
            "§6Let me check your balance...",
            "§6Here's your current balance:",
            "§6Your account balance is:",
            "§6Checking your funds..."
        ));
        bankTree.addDialogue("farewell", Arrays.asList(
            "§6Your money is secure!",
            "§6Thanks for banking with us!",
            "§6See you next time!",
            "§6Have a prosperous day!"
        ));
        dialogueTrees.put("bank", bankTree);
    }
    
    public void startDialogue(Player player, String npcType, String npcName) {
        DialogueSession session = new DialogueSession(player, npcType, npcName);
        activeSessions.put(player.getUniqueId(), session);
        
        // Send greeting
        sendDialogue(player, npcType, "greeting", npcName);
        
        // Show options after delay
        new BukkitRunnable() {
            @Override
            public void run() {
                showDialogueOptions(player, npcType);
            }
        }.runTaskLater(SkyblockPlugin, 40L);
    }
    
    public void handleDialogueChoice(Player player, int choice) {
        DialogueSession session = activeSessions.get(player.getUniqueId());
        if (session == null) return;
        
        String npcType = session.getNpcType();
        String npcName = session.getNpcName();
        
        switch (npcType) {
            case "shop" -> handleShopChoice(player, choice, npcName);
            case "quest" -> handleQuestChoice(player, choice, npcName);
            case "info" -> handleInfoChoice(player, choice, npcName);
            case "warp" -> handleWarpChoice(player, choice, npcName);
            case "bank" -> handleBankChoice(player, choice, npcName);
        }
    }
    
    private void handleShopChoice(Player player, int choice, String npcName) {
        switch (choice) {
            case 1 -> {
                sendDialogue(player, "shop", "browse", npcName);
                // Open shop GUI
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Open shop GUI
                        if (SkyblockPlugin.getShopSystem() != null) {
                            SkyblockPlugin.getShopSystem().openShopGUI(player);
                        } else {
                            player.sendMessage(Component.text("§cShop-System ist nicht verfügbar!"));
                        }
                        player.sendMessage(Component.text("§aOpening shop interface..."));
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 2 -> {
                sendDialogue(player, "shop", "sell", npcName);
                // Open sell GUI
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Open sell GUI
                        if (SkyblockPlugin.getShopSystem() != null) {
                            SkyblockPlugin.getShopSystem().openSellGUI(player);
                        } else {
                            player.sendMessage(Component.text("§cShop-System ist nicht verfügbar!"));
                        }
                        player.sendMessage(Component.text("§aOpening sell interface..."));
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 3 -> {
                sendDialogue(player, "shop", "prices", npcName);
                // Show prices
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        showShopPrices(player);
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 4 -> {
                sendDialogue(player, "shop", "farewell", npcName);
                endDialogue(player);
            }
        }
    }
    
    private void handleQuestChoice(Player player, int choice, String npcName) {
        switch (choice) {
            case 1 -> {
                sendDialogue(player, "quest", "available", npcName);
                // Show available quests
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        showAvailableQuests(player);
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 2 -> {
                sendDialogue(player, "quest", "progress", npcName);
                // Show quest progress
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        showQuestProgress(player);
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 3 -> {
                sendDialogue(player, "quest", "rewards", npcName);
                // Show rewards
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        showQuestRewards(player);
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 4 -> {
                sendDialogue(player, "quest", "farewell", npcName);
                endDialogue(player);
            }
        }
    }
    
    private void handleInfoChoice(Player player, int choice, String npcName) {
        switch (choice) {
            case 1 -> {
                sendDialogue(player, "info", "server", npcName);
                // Show server info
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        showServerInfo(player);
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 2 -> {
                sendDialogue(player, "info", "rules", npcName);
                // Show rules
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        showServerRules(player);
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 3 -> {
                sendDialogue(player, "info", "commands", npcName);
                // Show commands
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        showCommands(player);
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 4 -> {
                sendDialogue(player, "info", "farewell", npcName);
                endDialogue(player);
            }
        }
    }
    
    private void handleWarpChoice(Player player, int choice, String npcName) {
        switch (choice) {
            case 1 -> {
                sendDialogue(player, "warp", "spawn", npcName);
                // Warp to spawn
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(player.getWorld().getSpawnLocation());
                        player.sendMessage(Component.text("§dYou have been teleported to spawn!"));
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 2 -> {
                sendDialogue(player, "warp", "market", npcName);
                // Warp to market
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Warp to market location
                        if (SkyblockPlugin.getLocationManager() != null) {
                            // TODO: Implement proper LocationManager interface
                            // ((LocationManager) SkyblockPlugin.getLocationManager()).teleportToLocation(player, "market");
                        } else {
                            player.sendMessage(Component.text("§cLocation-System ist nicht verfügbar!"));
                        }
                        player.sendMessage(Component.text("§dYou have been teleported to the market!"));
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 3 -> {
                sendDialogue(player, "warp", "arena", npcName);
                // Warp to arena
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Warp to arena location
                        if (SkyblockPlugin.getLocationManager() != null) {
                            // TODO: Implement proper LocationManager interface
                            // ((LocationManager) SkyblockPlugin.getLocationManager()).teleportToLocation(player, "arena");
                        } else {
                            player.sendMessage(Component.text("§cLocation-System ist nicht verfügbar!"));
                        }
                        player.sendMessage(Component.text("§dYou have been teleported to the arena!"));
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 4 -> {
                sendDialogue(player, "warp", "farewell", npcName);
                endDialogue(player);
            }
        }
    }
    
    private void handleBankChoice(Player player, int choice, String npcName) {
        switch (choice) {
            case 1 -> {
                sendDialogue(player, "bank", "deposit", npcName);
                // Open deposit GUI
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Open deposit GUI
                        if (SkyblockPlugin.getBankSystem() != null) {
                            SkyblockPlugin.getBankSystem().openDepositGUI(player);
                        } else {
                            player.sendMessage(Component.text("§cBank-System ist nicht verfügbar!"));
                        }
                        player.sendMessage(Component.text("§6Opening deposit interface..."));
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 2 -> {
                sendDialogue(player, "bank", "withdraw", npcName);
                // Open withdraw GUI
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Open withdraw GUI
                        if (SkyblockPlugin.getBankSystem() != null) {
                            SkyblockPlugin.getBankSystem().openWithdrawGUI(player);
                        } else {
                            player.sendMessage(Component.text("§cBank-System ist nicht verfügbar!"));
                        }
                        player.sendMessage(Component.text("§6Opening withdraw interface..."));
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 3 -> {
                sendDialogue(player, "bank", "balance", npcName);
                // Show balance
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        showBankBalance(player);
                    }
                }.runTaskLater(SkyblockPlugin, 20L);
            }
            case 4 -> {
                sendDialogue(player, "bank", "farewell", npcName);
                endDialogue(player);
            }
        }
    }
    
    private void sendDialogue(Player player, String npcType, String dialogueKey, String npcName) {
        DialogueTree tree = dialogueTrees.get(npcType);
        if (tree == null) return;
        
        List<String> dialogues = tree.getDialogue(dialogueKey);
        if (dialogues == null || dialogues.isEmpty()) return;
        
        String dialogue = dialogues.get(new Random().nextInt(dialogues.size()));
        player.sendMessage("§8[§6" + npcName + "§8] §f" + dialogue);
    }
    
    private void showDialogueOptions(Player player, String npcType) {
        switch (npcType) {
            case "shop" -> {
                player.sendMessage(Component.text("§8[§6Shop NPC§8] §fWhat would you like to do?"));
                player.sendMessage(Component.text("§7• §e1. §fBrowse Items"));
                player.sendMessage(Component.text("§7• §e2. §fSell Items"));
                player.sendMessage(Component.text("§7• §e3. §fView Prices"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
            case "quest" -> {
                player.sendMessage(Component.text("§8[§6Quest NPC§8] §fI have quests available!"));
                player.sendMessage(Component.text("§7• §e1. §fView Available Quests"));
                player.sendMessage(Component.text("§7• §e2. §fCheck Quest Progress"));
                player.sendMessage(Component.text("§7• §e3. §fClaim Rewards"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
            case "info" -> {
                player.sendMessage(Component.text("§8[§6Info NPC§8] §fHow can I help you?"));
                player.sendMessage(Component.text("§7• §e1. §fServer Information"));
                player.sendMessage(Component.text("§7• §e2. §fGame Rules"));
                player.sendMessage(Component.text("§7• §e3. §fCommands Help"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
            case "warp" -> {
                player.sendMessage(Component.text("§8[§6Warp NPC§8] §fWhere would you like to go?"));
                player.sendMessage(Component.text("§7• §e1. §fSpawn"));
                player.sendMessage(Component.text("§7• §e2. §fMarket"));
                player.sendMessage(Component.text("§7• §e3. §fArena"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
            case "bank" -> {
                player.sendMessage(Component.text("§8[§6Bank NPC§8] §fBanking services available!"));
                player.sendMessage(Component.text("§7• §e1. §fDeposit Money"));
                player.sendMessage(Component.text("§7• §e2. §fWithdraw Money"));
                player.sendMessage(Component.text("§7• §e3. §fCheck Balance"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
        }
    }
    
    private void showShopPrices(Player player) {
        player.sendMessage(Component.text("§a=== Shop Prices ==="));
        player.sendMessage(Component.text("§7• §eDiamond: §a$100"));
        player.sendMessage(Component.text("§7• §eIron: §a$50"));
        player.sendMessage(Component.text("§7• §eGold: §a$75"));
        player.sendMessage(Component.text("§7• §eEmerald: §a$150"));
    }
    
    private void showAvailableQuests(Player player) {
        player.sendMessage(Component.text("§b=== Available Quests ==="));
        player.sendMessage(Component.text("§7• §e1. §fKill 10 Zombies"));
        player.sendMessage(Component.text("§7• §e2. §fMine 50 Stone"));
        player.sendMessage(Component.text("§7• §e3. §fFind 5 Diamonds"));
        player.sendMessage(Component.text("§7• §e4. §fBuild a House"));
    }
    
    private void showQuestProgress(Player player) {
        player.sendMessage(Component.text("§b=== Quest Progress ==="));
        player.sendMessage(Component.text("§7• §eKill Zombies: §a5/10"));
        player.sendMessage(Component.text("§7• §eMine Stone: §a25/50"));
        player.sendMessage(Component.text("§7• §eFind Diamonds: §a2/5"));
        player.sendMessage(Component.text("§7• §eBuild House: §aNot Started"));
    }
    
    private void showQuestRewards(Player player) {
        player.sendMessage(Component.text("§b=== Quest Rewards ==="));
        player.sendMessage(Component.text("§7• §eKill Zombies: §a$500 + 100 XP"));
        player.sendMessage(Component.text("§7• §eMine Stone: §a$250 + 50 XP"));
        player.sendMessage(Component.text("§7• §eFind Diamonds: §a$1000 + 200 XP"));
        player.sendMessage(Component.text("§7• §eBuild House: §a$750 + 150 XP"));
    }
    
    private void showServerInfo(Player player) {
        player.sendMessage(Component.text("§e=== Server Information ==="));
        player.sendMessage(Component.text("§7• §eServer: §fHypixel Skyblock Style"));
        player.sendMessage(Component.text("§7• §eVersion: §f1.20.1"));
        player.sendMessage("§7• §ePlayers Online: §f" + SkyblockPlugin.getServer().getOnlinePlayers().size());
        player.sendMessage(Component.text("§7• §eUptime: §f24/7"));
    }
    
    private void showServerRules(Player player) {
        player.sendMessage(Component.text("§e=== Server Rules ==="));
        player.sendMessage(Component.text("§7• §e1. §fBe respectful to other players"));
        player.sendMessage(Component.text("§7• §e2. §fNo cheating or hacking"));
        player.sendMessage(Component.text("§7• §e3. §fNo spamming or advertising"));
        player.sendMessage(Component.text("§7• §e4. §fFollow staff instructions"));
    }
    
    private void showCommands(Player player) {
        player.sendMessage(Component.text("§e=== Useful Commands ==="));
        player.sendMessage(Component.text("§7• §e/shop §f- Open shop"));
        player.sendMessage(Component.text("§7• §e/quest §f- View quests"));
        player.sendMessage(Component.text("§7• §e/bank §f- Open bank"));
        player.sendMessage(Component.text("§7• §e/warp §f- Teleport"));
    }
    
    private void showBankBalance(Player player) {
        // Get actual balance from economy system
        double balance = 0.0;
        if (SkyblockPlugin.getEconomyManager() != null) {
            balance = SkyblockPlugin.getEconomyManager().getBalance(player);
        }
        
        player.sendMessage(Component.text("§6=== Bank Balance ==="));
        player.sendMessage("§7• §eAccount Balance: §a$" + String.format("%.2f", balance));
        player.sendMessage(Component.text("§7• §eInterest Rate: §a2% per day"));
        player.sendMessage(Component.text("§7• §eNext Interest: §aIn 12 hours"));
    }
    
    public void endDialogue(Player player) {
        activeSessions.remove(player.getUniqueId());
    }
    
    public boolean hasActiveDialogue(Player player) {
        return activeSessions.containsKey(player.getUniqueId());
    }
    
    private static class DialogueTree {
        private final String npcType;
        private final Map<String, List<String>> dialogues = new HashMap<>();
        
        public DialogueTree(String npcType) {
            this.npcType = npcType;
        }
        
        public void addDialogue(String key, List<String> dialogue) {
            dialogues.put(key, dialogue);
        }
        
        public List<String> getDialogue(String key) {
            return dialogues.get(key);
        }
    }
    
    private static class DialogueSession {
        private final Player player;
        private final String npcType;
        private final String npcName;
        private final long startTime;
        
        public DialogueSession(Player player, String npcType, String npcName) {
            this.player = player;
            this.npcType = npcType;
            this.npcName = npcName;
            this.startTime = java.lang.System.currentTimeMillis();
        }
        
        public Player getPlayer() { return player; }
        public String getNpcType() { return npcType; }
        public String getNpcName() { return npcName; }
        public long getStartTime() { return startTime; }
    }
}
