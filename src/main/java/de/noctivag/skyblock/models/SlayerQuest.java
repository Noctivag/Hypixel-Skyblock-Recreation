package de.noctivag.skyblock.models;

public class SlayerQuest {
    
    private final String slayerType;
    private final int tier;
    private final int killsRequired;
    private int killsCompleted;
    private final long startTime;
    
    public SlayerQuest(String slayerType, int tier, int killsRequired) {
        this.slayerType = slayerType;
        this.tier = tier;
        this.killsRequired = killsRequired;
        this.killsCompleted = 0;
        this.startTime = System.currentTimeMillis();
    }
    
    public void addKill() {
        killsCompleted++;
    }
    
    public boolean isCompleted() {
        return killsCompleted >= killsRequired;
    }
    
    public String getSlayerType() {
        return slayerType;
    }
    
    public int getTier() {
        return tier;
    }
    
    public int getKillsRequired() {
        return killsRequired;
    }
    
    public int getKillsCompleted() {
        return killsCompleted;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getDuration() {
        return System.currentTimeMillis() - startTime;
    }
    
    public double getProgress() {
        return (double) killsCompleted / killsRequired;
    }
}