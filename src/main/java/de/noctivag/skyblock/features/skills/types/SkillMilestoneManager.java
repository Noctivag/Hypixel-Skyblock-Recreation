package de.noctivag.skyblock.features.skills.types;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Skill Milestone Manager class for managing skill milestones
 */
public class SkillMilestoneManager implements Service {
    private Map<UUID, Map<SkillType, Integer>> playerMilestones;
    private Map<SkillType, Map<Integer, SkillMilestone>> milestones;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public SkillMilestoneManager() {
        this.playerMilestones = new HashMap<>();
        this.milestones = new HashMap<>();
        initializeMilestones();
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        // Initialize milestone manager
        status = SystemStatus.RUNNING;
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        // Shutdown milestone manager
        status = SystemStatus.DISABLED;
    }

    @Override
    public String getName() {
        return "SkillMilestoneManager";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }

    public int getPriority() {
        return 50;
    }

    public boolean isRequired() {
        return false;
    }

    private void initializeMilestones() {
        for (SkillType skillType : SkillType.values()) {
            milestones.put(skillType, new HashMap<>());
            // Add default milestones for each skill type
            for (int i = 1; i <= 50; i++) {
                milestones.get(skillType).put(i, new SkillMilestone(i, "Level " + i, "Reach level " + i, 1000L * i));
            }
        }
    }

    public Map<UUID, Map<SkillType, Integer>> getPlayerMilestones() {
        return playerMilestones;
    }

    public Map<SkillType, Map<Integer, SkillMilestone>> getMilestones() {
        return milestones;
    }

    public int getPlayerMilestone(UUID playerId, SkillType skillType) {
        return playerMilestones.getOrDefault(playerId, new HashMap<>()).getOrDefault(skillType, 0);
    }

    public void setPlayerMilestone(UUID playerId, SkillType skillType, int milestone) {
        playerMilestones.computeIfAbsent(playerId, k -> new HashMap<>()).put(skillType, milestone);
    }

    public SkillMilestone getMilestone(SkillType skillType, int level) {
        return milestones.get(skillType).get(level);
    }

    public void addMilestone(SkillType skillType, SkillMilestone milestone) {
        milestones.get(skillType).put(milestone.getLevel(), milestone);
    }

    public void removeMilestone(SkillType skillType, int level) {
        milestones.get(skillType).remove(level);
    }

    public static class SkillMilestone {
        private int level;
        private String name;
        private String description;
        private long experienceRequired;

        public SkillMilestone(int level, String name, String description, long experienceRequired) {
            this.level = level;
            this.name = name;
            this.description = description;
            this.experienceRequired = experienceRequired;
        }

        public int getLevel() {
            return level;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public long getExperienceRequired() {
            return experienceRequired;
        }
    }
}
