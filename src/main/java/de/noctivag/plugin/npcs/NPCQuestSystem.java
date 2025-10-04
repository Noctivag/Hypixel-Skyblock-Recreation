package de.noctivag.plugin.npcs;
import de.noctivag.plugin.skills.SkillsSystem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * NPC Quest System - Hypixel Style Quests
 */
public class NPCQuestSystem {
    private final Plugin plugin;
    private final Map<UUID, List<Quest>> playerQuests = new HashMap<>();
    private final Map<String, Quest> availableQuests = new HashMap<>();

    public NPCQuestSystem(Plugin plugin) {
        this.plugin = plugin;
        initializeQuests();
    }

    private void initializeQuests() {
        // Beginner Quests
        availableQuests.put("kill_zombies", new Quest(
            "kill_zombies",
            "§bZombie Slayer",
            "§7Kill 10 zombies to prove your combat skills",
            QuestType.KILL_MOBS,
            "ZOMBIE",
            10,
            Arrays.asList("§aReward: §e$500", "§aReward: §e100 XP"),
            QuestRarity.COMMON
        ));

        availableQuests.put("mine_stone", new Quest(
            "mine_stone",
            "§bStone Miner",
            "§7Mine 50 stone blocks",
            QuestType.MINE_BLOCKS,
            "STONE",
            50,
            Arrays.asList("§aReward: §e$250", "§aReward: §e50 XP"),
            QuestRarity.COMMON
        ));

        availableQuests.put("find_diamonds", new Quest(
            "find_diamonds",
            "§bDiamond Hunter",
            "§7Find 5 diamonds",
            QuestType.COLLECT_ITEMS,
            "DIAMOND",
            5,
            Arrays.asList("§aReward: §e$1000", "§aReward: §e200 XP"),
            QuestRarity.RARE
        ));

        // Advanced Quests
        availableQuests.put("build_house", new Quest(
            "build_house",
            "§bArchitect",
            "§7Build a house with at least 100 blocks",
            QuestType.BUILD,
            "ANY",
            100,
            Arrays.asList("§aReward: §e$750", "§aReward: §e150 XP"),
            QuestRarity.UNCOMMON
        ));

        availableQuests.put("kill_dragon", new Quest(
            "kill_dragon",
            "§bDragon Slayer",
            "§7Defeat the Ender Dragon",
            QuestType.KILL_BOSS,
            "ENDER_DRAGON",
            1,
            Arrays.asList("§aReward: §e$5000", "§aReward: §e1000 XP", "§aReward: §eDragon Egg"),
            QuestRarity.LEGENDARY
        ));
    }

    public void giveQuest(Player player, String questId) {
        Quest quest = availableQuests.get(questId);
        if (quest == null) return;

        List<Quest> playerQuestList = playerQuests.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

        // Check if player already has this quest
        if (playerQuestList.stream().anyMatch(q -> q.getId().equals(questId))) {
            player.sendMessage("§cYou already have this quest!");
            return;
        }

        // Give quest to player
        Quest playerQuest = new Quest(quest);
        playerQuest.setStatus(QuestStatus.ACTIVE);
        playerQuest.setStartTime(System.currentTimeMillis());
        playerQuestList.add(playerQuest);

        player.sendMessage("§aQuest accepted: §e" + quest.getTitle());
        player.sendMessage("§7" + quest.getDescription());
        player.sendMessage("§7Progress: §e0/" + quest.getTargetAmount());
    }

    public void updateQuestProgress(Player player, QuestType type, String target, int amount) {
        List<Quest> playerQuestList = playerQuests.get(player.getUniqueId());
        if (playerQuestList == null) return;

        for (Quest quest : playerQuestList) {
            if (quest.getStatus() == QuestStatus.ACTIVE &&
                quest.getType() == type &&
                (quest.getTarget().equals(target) || quest.getTarget().equals("ANY"))) {

                quest.setProgress(quest.getProgress() + amount);

                if (quest.getProgress() >= quest.getTargetAmount()) {
                    completeQuest(player, quest);
                } else {
                    player.sendMessage("§aQuest Progress: §e" + quest.getTitle() + " §7(" +
                        quest.getProgress() + "/" + quest.getTargetAmount() + ")");
                }
            }
        }
    }

    private void completeQuest(Player player, Quest quest) {
        quest.setStatus(QuestStatus.COMPLETED);
        quest.setCompletionTime(System.currentTimeMillis());

        player.sendMessage("§a§lQuest Completed: §e" + quest.getTitle());
        player.sendMessage("§7Rewards:");
        for (String reward : quest.getRewards()) {
            player.sendMessage("§7  " + reward);
        }

        // Give rewards
        giveRewards(player, quest);
    }

    private void giveRewards(Player player, Quest quest) {
        for (String reward : quest.getRewards()) {
            if (reward.contains("$")) {
                // Give money
                String amount = reward.replaceAll("[^0-9]", "");
                // Give money through economy system
                if (plugin.getEconomyManager() != null) {
                    plugin.getEconomyManager().giveMoney(player, Double.parseDouble(amount));
                    player.sendMessage("§aDu hast " + amount + " Coins erhalten!");
                }
                player.sendMessage("§aReceived: §e$" + amount);
            } else if (reward.contains("XP")) {
                // Give XP
                String amount = reward.replaceAll("[^0-9]", "");
                // Give XP through skill system
                SkillsSystem skillsSystem = plugin.getSkillsSystem();
                if (skillsSystem != null) {
                    // Placeholder for skills system integration
                    skillsSystem.addXP(player, "Combat", Integer.parseInt(amount));
                }
                player.sendMessage("§aReceived: §e" + amount + " XP");
            } else if (reward.contains("Dragon Egg")) {
                // Give special item
                // Give dragon egg
                ItemStack dragonEgg = new ItemStack(org.bukkit.Material.DRAGON_EGG);
                org.bukkit.inventory.meta.ItemMeta meta = dragonEgg.getItemMeta();
                if (meta != null) {
                    meta.displayName(net.kyori.adventure.text.Component.text("§5§lDragon Egg"));
                    meta.lore(java.util.Collections.singletonList(
                        net.kyori.adventure.text.Component.text("§7A rare dragon egg from a quest reward!")
                    ));
                    dragonEgg.setItemMeta(meta);
                }
                player.getInventory().addItem(dragonEgg);
                player.sendMessage("§aReceived: §eDragon Egg");
            }
        }
    }

    public List<Quest> getPlayerQuests(Player player) {
        return playerQuests.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    public List<Quest> getAvailableQuests() {
        return new ArrayList<>(availableQuests.values());
    }

    public Quest getQuest(String questId) {
        return availableQuests.get(questId);
    }

    public enum QuestType {
        KILL_MOBS, KILL_BOSS, MINE_BLOCKS, COLLECT_ITEMS, BUILD, EXPLORE, CRAFT
    }

    public enum QuestStatus {
        AVAILABLE, ACTIVE, COMPLETED, FAILED
    }

    public enum QuestRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);

        @Getter private final String displayName;
        @Getter private final double multiplier;

        QuestRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
    }

    @Getter
    @Setter
    public static class Quest {
        private final String id;
        private final String title;
        private final String description;
        private final QuestType type;
        private final String target;
        private final int targetAmount;
        private final List<String> rewards;
        private final QuestRarity rarity;

        private QuestStatus status = QuestStatus.AVAILABLE;
        private int progress = 0;
        private long startTime = 0;
        private long completionTime = 0;

        public Quest(String id, String title, String description, QuestType type,
                    String target, int targetAmount, List<String> rewards, QuestRarity rarity) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.type = type;
            this.target = target;
            this.targetAmount = targetAmount;
            this.rewards = rewards;
            this.rarity = rarity;
        }

        public Quest(Quest other) {
            this.id = other.id;
            this.title = other.title;
            this.description = other.description;
            this.type = other.type;
            this.target = other.target;
            this.targetAmount = other.targetAmount;
            this.rewards = other.rewards;
            this.rarity = other.rarity;
            this.status = other.status;
            this.progress = other.progress;
            this.startTime = other.startTime;
            this.completionTime = other.completionTime;
        }
        
        // Getter methods
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public QuestType getType() { return type; }
        public String getTarget() { return target; }
        public int getTargetAmount() { return targetAmount; }
        public List<String> getRewards() { return new ArrayList<>(rewards); }
        public QuestRarity getRarity() { return rarity; }
        public QuestStatus getStatus() { return status; }
        public int getProgress() { return progress; }
        public long getStartTime() { return startTime; }
        public long getCompletionTime() { return completionTime; }
        
        // Setter methods
        public void setStatus(QuestStatus status) { this.status = status; }
        public void setProgress(int progress) { this.progress = progress; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public void setCompletionTime(long completionTime) { this.completionTime = completionTime; }
    }
}
