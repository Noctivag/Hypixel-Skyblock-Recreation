package de.noctivag.skyblock.engine.dialog.types;

import java.util.UUID;

/**
 * Dialog Session
 * 
 * Represents an active dialog session between a player and an NPC.
 * Tracks the current dialog state and progression.
 */
public class DialogSession {
    
    private final UUID playerId;
    private final String npcId;
    private DialogNode currentNode;
    private final long startTime;
    
    public DialogSession(UUID playerId, String npcId, DialogNode startNode) {
        this.playerId = playerId;
        this.npcId = npcId;
        this.currentNode = startNode;
        this.startTime = System.currentTimeMillis();
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public String getNpcId() {
        return npcId;
    }
    
    public DialogNode getCurrentNode() {
        return currentNode;
    }
    
    public void setCurrentNode(DialogNode node) {
        this.currentNode = node;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getSessionDuration() {
        return System.currentTimeMillis() - startTime;
    }
}
