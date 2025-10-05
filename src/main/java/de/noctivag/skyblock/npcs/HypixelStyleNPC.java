package de.noctivag.skyblock.npcs;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;

/**
 * Hypixel-Style NPC with advanced animations and interactions
 */
public class HypixelStyleNPC {
    private final SkyblockPlugin SkyblockPlugin;
    private final String npcId;
    private final AdvancedNPCSystem.NPCType type;
    private Location location;
    private String displayName;
    private String customData;
    private Villager entity;
    private BukkitTask animationTask;
    private BukkitTask dialogueTask;
    private BukkitTask particleTask;
    private BukkitTask soundTask;
    
    // Animation states
    private boolean isIdle = true;
    private boolean isTalking = false;
    private boolean isLookingAtPlayer = false;
    private Player currentPlayer = null;
    private long lastInteraction = 0;
    
    // Dialogue system
    private List<String> greetings = new ArrayList<>();
    private List<String> farewells = new ArrayList<>();
    private Map<String, List<String>> dialogueOptions = new HashMap<>();
    private String currentDialogue = "";
    
    // Emotes and expressions
    private NPCEmote currentEmote = NPCEmote.NEUTRAL;
    private long emoteStartTime = 0;
    
    public HypixelStyleNPC(SkyblockPlugin SkyblockPlugin, String npcId, AdvancedNPCSystem.NPCType type, Location location, String displayName, String customData) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.npcId = npcId;
        this.type = type;
        this.location = location;
        this.displayName = displayName;
        this.customData = customData;
        
        initializeDialogue();
        spawn();
        startAdvancedAnimations();
        startParticleEffects();
        startSoundEffects();
    }
    
    private void initializeDialogue() {
        // Initialize dialogue based on NPC type
        switch (type) {
            case SHOP -> {
                greetings.addAll(Arrays.asList(
                    "§aWelcome to my shop!",
                    "§aLooking to buy something?",
                    "§aI have the best deals in town!",
                    "§aWhat can I get for you today?"
                ));
                farewells.addAll(Arrays.asList(
                    "§aCome back soon!",
                    "§aThanks for shopping!",
                    "§aSee you next time!",
                    "§aHave a great day!"
                ));
            }
            case QUEST -> {
                greetings.addAll(Arrays.asList(
                    "§bI have a quest for you!",
                    "§bAre you ready for an adventure?",
                    "§bI need your help with something!",
                    "§bInterested in earning some rewards?"
                ));
                farewells.addAll(Arrays.asList(
                    "§bGood luck on your quest!",
                    "§bCome back when you're done!",
                    "§bMay the odds be in your favor!",
                    "§bSee you soon, adventurer!"
                ));
            }
            case INFO -> {
                greetings.addAll(Arrays.asList(
                    "§eHow can I help you today?",
                    "§eNeed some information?",
                    "§eWhat would you like to know?",
                    "§eI'm here to help!"
                ));
                farewells.addAll(Arrays.asList(
                    "§eHope I could help!",
                    "§eCome back if you need more info!",
                    "§eHave a great day!",
                    "§eSee you around!"
                ));
            }
            case WARP -> {
                greetings.addAll(Arrays.asList(
                    "§dWhere would you like to go?",
                    "§dReady for a journey?",
                    "§dI can take you anywhere!",
                    "§dChoose your destination!"
                ));
                farewells.addAll(Arrays.asList(
                    "§dSafe travels!",
                    "§dEnjoy your trip!",
                    "§dSee you at your destination!",
                    "§dHave a good journey!"
                ));
            }
            case BANK -> {
                greetings.addAll(Arrays.asList(
                    "§6Welcome to the bank!",
                    "§6How can I help with your finances?",
                    "§6Need to manage your money?",
                    "§6Your money is safe with us!"
                ));
                farewells.addAll(Arrays.asList(
                    "§6Your money is secure!",
                    "§6Thanks for banking with us!",
                    "§6See you next time!",
                    "§6Have a prosperous day!"
                ));
            }
        }
    }
    
    private void spawn() {
        entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        entity.customName(net.kyori.adventure.text.Component.text(displayName));
        entity.setCustomNameVisible(true);
        entity.setAI(false);
        entity.setInvulnerable(true);
        entity.setSilent(true);
        
        // Set profession and appearance based on NPC type
        setNPCAppearance();
        
        // Add custom metadata
        entity.setMetadata("npc_id", new org.bukkit.metadata.FixedMetadataValue(SkyblockPlugin, npcId));
        entity.setMetadata("npc_type", new org.bukkit.metadata.FixedMetadataValue(SkyblockPlugin, type.name()));
    }
    
    private void setNPCAppearance() {
        switch (type) {
            case SHOP -> {
                entity.setProfession(Villager.Profession.CLERIC);
                entity.setVillagerType(Villager.Type.PLAINS);
            }
            case QUEST -> {
                entity.setProfession(Villager.Profession.CARTOGRAPHER);
                entity.setVillagerType(Villager.Type.TAIGA);
            }
            case INFO -> {
                entity.setProfession(Villager.Profession.LIBRARIAN);
                entity.setVillagerType(Villager.Type.SAVANNA);
            }
            case WARP -> {
                entity.setProfession(Villager.Profession.CLERIC);
                entity.setVillagerType(Villager.Type.DESERT);
            }
            case BANK -> {
                entity.setProfession(Villager.Profession.ARMORER);
                entity.setVillagerType(Villager.Type.JUNGLE);
            }
            case AUCTION -> {
                entity.setProfession(Villager.Profession.TOOLSMITH);
                entity.setVillagerType(Villager.Type.SNOW);
            }
            case GUILD -> {
                entity.setProfession(Villager.Profession.WEAPONSMITH);
                entity.setVillagerType(Villager.Type.SWAMP);
            }
            case PET -> {
                entity.setProfession(Villager.Profession.BUTCHER);
                entity.setVillagerType(Villager.Type.PLAINS);
            }
            case COSMETIC -> {
                entity.setProfession(Villager.Profession.LEATHERWORKER);
                entity.setVillagerType(Villager.Type.TAIGA);
            }
            case ADMIN -> {
                entity.setProfession(Villager.Profession.MASON);
                entity.setVillagerType(Villager.Type.SAVANNA);
            }
        }
    }
    
    private void startAdvancedAnimations() {
        animationTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead()) return;
                
                // Idle animations
                if (isIdle && !isTalking) {
                    performIdleAnimation();
                }
                
                // Look at nearby players
                if (isLookingAtPlayer && currentPlayer != null) {
                    lookAtPlayer(currentPlayer);
                }
                
                // Emote animations
                if (currentEmote != NPCEmote.NEUTRAL) {
                    performEmoteAnimation();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 10L); // Faster updates for smoother animations
    }
    
    private void startParticleEffects() {
        particleTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead()) return;
                
                // Type-specific particle effects
                switch (type) {
                    case SHOP -> spawnParticles(Particle.HAPPY_VILLAGER, 1);
                    case QUEST -> spawnParticles(Particle.ENCHANT, 2);
                    case INFO -> spawnParticles(Particle.END_ROD, 1);
                    case WARP -> spawnParticles(Particle.PORTAL, 3);
                    case BANK -> spawnParticles(Particle.HAPPY_VILLAGER, 1);
                    case AUCTION -> spawnParticles(Particle.HAPPY_VILLAGER, 2);
                    case GUILD -> spawnParticles(Particle.FLAME, 1);
                    case PET -> spawnParticles(Particle.HEART, 1);
                    case COSMETIC -> spawnParticles(Particle.NOTE, 3);
                    case ADMIN -> spawnParticles(Particle.DUST, 2);
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 40L);
    }
    
    private void startSoundEffects() {
        soundTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead()) return;
                
                // Random ambient sounds
                if (ThreadLocalRandom.current().nextInt(100) < 5) { // 5% chance
                    playAmbientSound();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 100L);
    }
    
    private void performIdleAnimation() {
        // Random idle movements
        int random = ThreadLocalRandom.current().nextInt(100);
        
        if (random < 30) {
            // Look around
            float yaw = entity.getLocation().getYaw() + ThreadLocalRandom.current().nextFloat() * 60 - 30;
            entity.setRotation(yaw, entity.getLocation().getPitch());
        } else if (random < 50) {
            // Slight head movement
            float pitch = entity.getLocation().getPitch() + ThreadLocalRandom.current().nextFloat() * 20 - 10;
            entity.setRotation(entity.getLocation().getYaw(), pitch);
        } else if (random < 70) {
            // Body sway
            Location loc = entity.getLocation();
            loc.setYaw(loc.getYaw() + ThreadLocalRandom.current().nextFloat() * 10 - 5);
            entity.teleport(loc);
        }
    }
    
    private void performEmoteAnimation() {
        long currentTime = java.lang.System.currentTimeMillis();
        long emoteDuration = 2000; // 2 seconds
        
        if (currentTime - emoteStartTime > emoteDuration) {
            currentEmote = NPCEmote.NEUTRAL;
            return;
        }
        
        switch (currentEmote) {
            case HAPPY -> {
                // Nodding animation
                float pitch = (float) Math.sin((currentTime - emoteStartTime) * 0.01) * 10;
                entity.setRotation(entity.getLocation().getYaw(), pitch);
            }
            case SAD -> {
                // Shaking head
                float yaw = (float) Math.sin((currentTime - emoteStartTime) * 0.02) * 15;
                entity.setRotation(entity.getLocation().getYaw() + yaw, entity.getLocation().getPitch());
            }
            case EXCITED -> {
                // Bouncing animation
                Location loc = entity.getLocation();
                loc.setY(loc.getY() + Math.sin((currentTime - emoteStartTime) * 0.01) * 0.1);
                entity.teleport(loc);
            }
            case THINKING -> {
                // Looking up
                entity.setRotation(entity.getLocation().getYaw(), -20);
            }
        }
    }
    
    private void lookAtPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            isLookingAtPlayer = false;
            currentPlayer = null;
            return;
        }
        
        Location npcLoc = entity.getLocation();
        Location playerLoc = player.getLocation();
        
        Vector direction = playerLoc.toVector().subtract(npcLoc.toVector()).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
        float pitch = (float) Math.toDegrees(Math.asin(-direction.getY()));
        
        entity.setRotation(yaw, pitch);
    }
    
    private void spawnParticles(Particle particle, int count) {
        Location loc = entity.getLocation().add(0, 2, 0);
        entity.getWorld().spawnParticle(particle, loc, count, 0.5, 0.5, 0.5, 0.1);
    }
    
    private void playAmbientSound() {
        Sound sound = switch (type) {
            case SHOP -> Sound.ENTITY_VILLAGER_TRADE;
            case QUEST -> Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
            case INFO -> Sound.ENTITY_VILLAGER_AMBIENT;
            case WARP -> Sound.ENTITY_ENDERMAN_TELEPORT;
            case BANK -> Sound.BLOCK_CHEST_OPEN;
            case AUCTION -> Sound.ENTITY_PLAYER_LEVELUP;
            case GUILD -> Sound.ENTITY_IRON_GOLEM_STEP;
            case PET -> Sound.ENTITY_WOLF_AMBIENT;
            case COSMETIC -> Sound.BLOCK_NOTE_BLOCK_PLING;
            case ADMIN -> Sound.ENTITY_WITHER_SPAWN;
        };
        
        entity.getWorld().playSound(entity.getLocation(), sound, 0.5f, 1.0f);
    }
    
    public void onPlayerInteract(Player player) {
        if (player == null) return;
        
        lastInteraction = java.lang.System.currentTimeMillis();
        isLookingAtPlayer = true;
        currentPlayer = player;
        
        // Start dialogue
        startDialogue(player);
        
        // Set emote
        setEmote(NPCEmote.HAPPY);
        
        // Play interaction sound
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
        
        // Stop looking at player after 5 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                isLookingAtPlayer = false;
                currentPlayer = null;
            }
        }.runTaskLater(SkyblockPlugin, 100L);
    }
    
    private void startDialogue(Player player) {
        if (dialogueTask != null) {
            dialogueTask.cancel();
        }
        
        isTalking = true;
        
        // Send greeting
        String greeting = greetings.get(ThreadLocalRandom.current().nextInt(greetings.size()));
        player.sendMessage("§8[§6" + displayName + "§8] §f" + greeting);
        
        // Show dialogue options after 2 seconds
        dialogueTask = new BukkitRunnable() {
            @Override
            public void run() {
                showDialogueOptions(player);
                isTalking = false;
            }
        }.runTaskLater(SkyblockPlugin, 40L);
    }
    
    private void showDialogueOptions(Player player) {
        // Create interactive dialogue based on NPC type
        switch (type) {
            case SHOP -> {
                player.sendMessage("§8[§6" + displayName + "§8] §fWhat would you like to do?");
                player.sendMessage(Component.text("§7• §e1. §fBrowse Items"));
                player.sendMessage(Component.text("§7• §e2. §fSell Items"));
                player.sendMessage(Component.text("§7• §e3. §fView Prices"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
            case QUEST -> {
                player.sendMessage("§8[§6" + displayName + "§8] §fI have quests available!");
                player.sendMessage(Component.text("§7• §e1. §fView Available Quests"));
                player.sendMessage(Component.text("§7• §e2. §fCheck Quest Progress"));
                player.sendMessage(Component.text("§7• §e3. §fClaim Rewards"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
            case INFO -> {
                player.sendMessage("§8[§6" + displayName + "§8] §fHow can I help you?");
                player.sendMessage(Component.text("§7• §e1. §fServer Information"));
                player.sendMessage(Component.text("§7• §e2. §fGame Rules"));
                player.sendMessage(Component.text("§7• §e3. §fCommands Help"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
            case WARP -> {
                player.sendMessage("§8[§6" + displayName + "§8] §fWhere would you like to go?");
                player.sendMessage(Component.text("§7• §e1. §fSpawn"));
                player.sendMessage(Component.text("§7• §e2. §fMarket"));
                player.sendMessage(Component.text("§7• §e3. §fArena"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
            case BANK -> {
                player.sendMessage("§8[§6" + displayName + "§8] §fBanking services available!");
                player.sendMessage(Component.text("§7• §e1. §fDeposit Money"));
                player.sendMessage(Component.text("§7• §e2. §fWithdraw Money"));
                player.sendMessage(Component.text("§7• §e3. §fCheck Balance"));
                player.sendMessage(Component.text("§7• §e4. §fLeave"));
            }
        }
    }
    
    public void setEmote(NPCEmote emote) {
        this.currentEmote = emote;
        this.emoteStartTime = java.lang.System.currentTimeMillis();
    }
    
    public void update() {
        if (entity != null && !entity.isDead()) {
            entity.customName(net.kyori.adventure.text.Component.text(displayName));
        }
    }
    
    public void remove() {
        if (animationTask != null) animationTask.cancel();
        if (dialogueTask != null) dialogueTask.cancel();
        if (particleTask != null) particleTask.cancel();
        if (soundTask != null) soundTask.cancel();
        
        if (entity != null && !entity.isDead()) {
            entity.remove();
        }
    }
    
    public void updateDisplayName(String newDisplayName) {
        this.displayName = newDisplayName;
        if (entity != null && !entity.isDead()) {
            entity.customName(net.kyori.adventure.text.Component.text(newDisplayName));
        }
    }
    
    public void updateCustomData(String newCustomData) {
        this.customData = newCustomData;
    }
    
    // Getters
    public String getNpcId() { return npcId; }
    public AdvancedNPCSystem.NPCType getType() { return type; }
    public Location getLocation() { return location; }
    public String getDisplayName() { return displayName; }
    public String getCustomData() { return customData; }
    public Villager getEntity() { return entity; }
    public boolean isTalking() { return isTalking; }
    public NPCEmote getCurrentEmote() { return currentEmote; }
    
    public enum NPCEmote {
        NEUTRAL, HAPPY, SAD, EXCITED, THINKING, ANGRY, SURPRISED
    }
}
