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

    /**
     * Prüft ob die Quest aktiv ist (nicht abgeschlossen)
     * @return true wenn aktiv
     */
    public boolean isActive() {
        return !isCompleted();
    }

    /**
     * Gibt die aktuellen Kills zurück (Alias für getKillsCompleted)
     * @return Anzahl der Kills
     */
    public int getCurrentKills() {
        return killsCompleted;
    }

    /**
     * Gibt die benötigten Kills zurück (Alias für getKillsRequired)
     * @return Anzahl der benötigten Kills
     */
    public int getRequiredKills() {
        return killsRequired;
    }

    /**
     * Gibt die Quest-Dauer in Sekunden zurück
     * @return Dauer in Sekunden
     */
    public long getQuestDurationSeconds() {
        return getDuration() / 1000;
    }

    /**
     * Gibt den deutschen Namen des Slayer-Typs zurück
     * @return Deutscher Name
     */
    public String getGermanName() {
        switch (slayerType.toLowerCase()) {
            case "zombie":
                return "Zombie";
            case "spider":
                return "Spinne";
            case "wolf":
                return "Wolf";
            case "enderman":
                return "Enderman";
            case "blaze":
                return "Blaze";
            default:
                return slayerType;
        }
    }
}