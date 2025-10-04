package de.noctivag.plugin.npcs;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

// import java.util.List;
// import java.util.UUID;

/**
 * NPC Event System - Custom events for NPC interactions
 */
public class NPCEventSystem {
    private final Plugin plugin;
    
    public NPCEventSystem(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void callNPCClickEvent(Player player, String npcId, String npcType) {
        NPCClickEvent event = new NPCClickEvent(player, npcId, npcType);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    public void callNPCQuestEvent(Player player, String npcId, String questId) {
        NPCQuestEvent event = new NPCQuestEvent(player, npcId, questId);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    public void callNPCDialogueEvent(Player player, String npcId, String dialogue) {
        NPCDialogueEvent event = new NPCDialogueEvent(player, npcId, dialogue);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    // Custom Events
    public static class NPCClickEvent extends Event {
        private static final HandlerList handlers = new HandlerList();
        private final Player player;
        private final String npcId;
        private final String npcType;
        
        public NPCClickEvent(Player player, String npcId, String npcType) {
            this.player = player;
            this.npcId = npcId;
            this.npcType = npcType;
        }
        
        public Player getPlayer() { return player; }
        public String getNpcId() { return npcId; }
        public String getNpcType() { return npcType; }
        
        @Override
        public HandlerList getHandlers() { return handlers; }
        public static HandlerList getHandlerList() { return handlers; }
    }
    
    public static class NPCQuestEvent extends Event {
        private static final HandlerList handlers = new HandlerList();
        private final Player player;
        private final String npcId;
        private final String questId;
        
        public NPCQuestEvent(Player player, String npcId, String questId) {
            this.player = player;
            this.npcId = npcId;
            this.questId = questId;
        }
        
        public Player getPlayer() { return player; }
        public String getNpcId() { return npcId; }
        public String getQuestId() { return questId; }
        
        @Override
        public HandlerList getHandlers() { return handlers; }
        public static HandlerList getHandlerList() { return handlers; }
    }
    
    public static class NPCDialogueEvent extends Event {
        private static final HandlerList handlers = new HandlerList();
        private final Player player;
        private final String npcId;
        private final String dialogue;
        
        public NPCDialogueEvent(Player player, String npcId, String dialogue) {
            this.player = player;
            this.npcId = npcId;
            this.dialogue = dialogue;
        }
        
        public Player getPlayer() { return player; }
        public String getNpcId() { return npcId; }
        public String getDialogue() { return dialogue; }
        
        @Override
        public HandlerList getHandlers() { return handlers; }
        public static HandlerList getHandlerList() { return handlers; }
    }
}
