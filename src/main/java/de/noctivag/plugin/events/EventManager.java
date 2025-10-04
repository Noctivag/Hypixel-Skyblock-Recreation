package de.noctivag.plugin.events;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {
    private final Plugin plugin;
    private final Map<String, Event> activeEvents = new ConcurrentHashMap<>();
    private final Map<String, Arena> arenas = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerEvents = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> eventTasks = new ConcurrentHashMap<>();
    
    public EventManager(Plugin plugin) {
        this.plugin = plugin;
        // initializeArenas() wird nicht mehr im Konstruktor aufgerufen
        // um Bukkit-Task-Registrierung vor Plugin-Aktivierung zu vermeiden
    }
    
    /**
     * Initialisiert die Arenen nach der Plugin-Aktivierung.
     * Diese Methode sollte in onEnable() aufgerufen werden.
     */
    public void startArenaInitialization() {
        initializeArenas();
    }
    
    private void initializeArenas() {
        // Wait for worlds to be loaded
        if (Bukkit.getWorlds().isEmpty()) {
            plugin.getLogger().warning("No worlds loaded yet, delaying arena initialization...");
            // Schedule arena initialization for later
            Bukkit.getScheduler().runTaskLater(plugin, this::initializeArenas, 20L); // 1 second delay
            return;
        }
        
        World world = Bukkit.getWorlds().get(0);
        if (world == null) {
            plugin.getLogger().severe("Failed to get default world for arena initialization!");
            return;
        }
        
        // Boss Arena 1 - Ender Dragon
        Arena enderArena = new Arena("ender_dragon", "Ender Dragon Arena", 
            new Location(world, 100, 100, 100),
            new Location(world, 120, 120, 120));
        arenas.put("ender_dragon", enderArena);
        
        // Boss Arena 2 - Wither
        Arena witherArena = new Arena("wither", "Wither Arena",
            new Location(world, 200, 100, 200),
            new Location(world, 220, 120, 220));
        arenas.put("wither", witherArena);
        
        // Boss Arena 3 - Custom Boss (Titan)
        Arena customArena = new Arena("custom_boss", "Titan Arena",
            new Location(world, 300, 100, 300),
            new Location(world, 320, 120, 320));
        arenas.put("custom_boss", customArena);
        
        // Boss Arena 4 - Elder Guardian
        Arena elderArena = new Arena("elder_guardian", "Elder Guardian Arena",
            new Location(world, 400, 100, 400),
            new Location(world, 420, 120, 420));
        arenas.put("elder_guardian", elderArena);
        
        // Boss Arena 5 - Ravager
        Arena ravagerArena = new Arena("ravager", "Ravager Arena",
            new Location(world, 500, 100, 500),
            new Location(world, 520, 120, 520));
        arenas.put("ravager", ravagerArena);
        
        // Boss Arena 6 - Phantom King
        Arena phantomArena = new Arena("phantom_king", "Phantom King Arena",
            new Location(world, 600, 100, 600),
            new Location(world, 620, 120, 620));
        arenas.put("phantom_king", phantomArena);
        
        // Boss Arena 7 - Blaze King
        Arena blazeArena = new Arena("blaze_king", "Blaze King Arena",
            new Location(world, 700, 100, 700),
            new Location(world, 720, 120, 720));
        arenas.put("blaze_king", blazeArena);
        
        // Boss Arena 8 - Enderman Lord
        Arena endermanArena = new Arena("enderman_lord", "Enderman Lord Arena",
            new Location(world, 800, 100, 800),
            new Location(world, 820, 120, 820));
        arenas.put("enderman_lord", endermanArena);
    }
    
    public void joinEvent(Player player, String eventId) {
        if (playerEvents.containsKey(player.getUniqueId())) {
            player.sendMessage("§cDu bist bereits in einem Event!");
            return;
        }
        
        Event event = getEvent(eventId);
        if (event == null) {
            player.sendMessage("§cEvent nicht gefunden!");
            return;
        }
        
        if (event.getMaxPlayers() <= event.getPlayers().size()) {
            player.sendMessage("§cEvent ist voll!");
            return;
        }
        
        // Check if player has required items/balance
        if (!canJoinEvent(player, event)) {
            return;
        }
        
        // Deduct cost
        if (event.getCost() > 0) {
            plugin.getEconomyManager().withdrawMoney(player, event.getCost());
        }
        
        // Add player to event
        event.addPlayer(player);
        playerEvents.put(player.getUniqueId(), eventId);
        
        // Teleport to arena
        Arena arena = arenas.get(event.getArenaId());
        if (arena != null) {
            Location spawnLoc = arena.getSpawnLocation();
            player.teleport(spawnLoc);
            player.sendMessage("§aDu bist dem Event beigetreten!");
            player.sendMessage("§eEvent: §f" + event.getName());
            player.sendMessage("§eSpieler: §f" + event.getPlayers().size() + "/" + event.getMaxPlayers());
        }
        
        // Start event if enough players
        if (event.getPlayers().size() >= event.getMinPlayers() && !event.isActive()) {
            startEvent(eventId);
        }
    }
    
    public void leaveEvent(Player player) {
        String eventId = playerEvents.get(player.getUniqueId());
        if (eventId == null) {
            player.sendMessage("§cDu bist in keinem Event!");
            return;
        }
        
        Event event = activeEvents.get(eventId);
        if (event != null) {
            event.removePlayer(player);
        }
        
        playerEvents.remove(player.getUniqueId());
        
        // Teleport back to spawn
        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        player.sendMessage("§aDu hast das Event verlassen!");
    }
    
    private boolean canJoinEvent(Player player, Event event) {
        // Check balance
        if (event.getCost() > 0 && !plugin.getEconomyManager().hasBalance(player, event.getCost())) {
            player.sendMessage("§cDu kannst dir dieses Event nicht leisten: " + 
                plugin.getEconomyManager().formatMoney(event.getCost()));
            return false;
        }
        
        // Check level requirement
        if (event.getRequiredLevel() > 0 && player.getLevel() < event.getRequiredLevel()) {
            player.sendMessage("§cDu benötigst Level " + event.getRequiredLevel() + "!");
            return false;
        }
        
        return true;
    }
    
    private void startEvent(String eventId) {
        Event event = activeEvents.get(eventId);
        if (event == null) return;
        
        event.setActive(true);
        Arena arena = arenas.get(event.getArenaId());
        if (arena == null) return;
        
        // Announce event start
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("§6§lEvent gestartet!");
                player.sendMessage("§eBereite dich vor...");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
            }
        }
        
        // Start countdown
        BukkitTask countdownTask = new BukkitRunnable() {
            int countdown = 10;
            
            @Override
            public void run() {
                if (countdown <= 0) {
                    spawnBoss(event);
                    cancel();
                    return;
                }
                
                for (UUID playerId : event.getPlayers()) {
                    Player player = Bukkit.getPlayer(playerId);
                    if (player != null && player.isOnline()) {
                        // Use Adventure API for titles
                        Title title = Title.title(
                            Component.text("§c" + countdown), 
                            Component.text("§7Bereite dich vor!"), 
                            Title.Times.times(Duration.ofMillis(0), Duration.ofSeconds(1), Duration.ofMillis(0))
                        );
                        player.showTitle(title);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    }
                }
                
                countdown--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
        
        eventTasks.put(eventId + "_countdown", countdownTask);
    }
    
    private void spawnBoss(Event event) {
        Arena arena = arenas.get(event.getArenaId());
        if (arena == null) return;
        
        Location bossLoc = arena.getBossSpawnLocation();
        
        switch (event.getArenaId()) {
            case "ender_dragon":
                spawnEnderDragon(event, bossLoc);
                break;
            case "wither":
                spawnWither(event, bossLoc);
                break;
            case "custom_boss":
                spawnCustomBoss(event, bossLoc);
                break;
            case "elder_guardian":
                spawnElderGuardian(event, bossLoc);
                break;
            case "ravager":
                spawnRavager(event, bossLoc);
                break;
            case "phantom_king":
                spawnPhantomKing(event, bossLoc);
                break;
            case "blaze_king":
                spawnBlazeKing(event, bossLoc);
                break;
            case "enderman_lord":
                spawnEndermanLord(event, bossLoc);
                break;
        }
        
        // Start event monitoring
        BukkitTask monitorTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getPlayers().isEmpty() || event.getBoss() == null || event.getBoss().isDead()) {
                    endEvent(event);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
        
        eventTasks.put(event.getArenaId() + "_monitor", monitorTask);
    }
    
    private void spawnEnderDragon(Event event, Location loc) {
        EnderDragon dragon = (EnderDragon) loc.getWorld().spawnEntity(loc, EntityType.ENDER_DRAGON);
        dragon.customName(Component.text("§5§lEvent Ender Dragon"));
        
        // Make dragon more powerful
        dragon.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
        dragon.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
        
        event.setBoss(dragon);
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("§5§lDer Ender Dragon ist erschienen!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.5f);
            }
        }
    }
    
    private void spawnWither(Event event, Location loc) {
        Wither wither = (Wither) loc.getWorld().spawnEntity(loc, EntityType.WITHER);
        wither.customName(Component.text("§8§lEvent Wither"));
        
        // Make wither more powerful
        wither.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        wither.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
        
        event.setBoss(wither);
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("§8§lDer Wither ist erschienen!");
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2.0f, 0.5f);
            }
        }
    }
    
    private void spawnCustomBoss(Event event, Location loc) {
        // Create a custom boss (Giant Zombie with special abilities)
        Giant giant = (Giant) loc.getWorld().spawnEntity(loc, EntityType.GIANT);
        giant.customName(Component.text("§c§lEvent Boss - Titan"));
        
        // Make giant more powerful
        giant.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 3));
        giant.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 3));
        giant.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        
        event.setBoss(giant);
        
        // Special abilities task
        BukkitTask abilityTask = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (giant.isDead()) {
                    cancel();
                    return;
                }
                
                ticks++;
                
                // Ground slam every 5 seconds
                if (ticks % 100 == 0) {
                    groundSlam(giant, event);
                }
                
                // Summon minions every 10 seconds
                if (ticks % 200 == 0) {
                    summonMinions(giant, event);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        eventTasks.put(event.getArenaId() + "_abilities", abilityTask);
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("§c§lDer Titan ist erwacht!");
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SKELETON_AMBIENT, 2.0f, 0.5f);
            }
        }
    }
    
    private void spawnElderGuardian(Event event, Location loc) {
        ElderGuardian guardian = (ElderGuardian) loc.getWorld().spawnEntity(loc, EntityType.ELDER_GUARDIAN);
        guardian.customName(Component.text("§b§lEvent Elder Guardian"));
        
        // Make guardian more powerful
        guardian.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        guardian.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
        guardian.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        
        event.setBoss(guardian);
        
        // Special abilities task
        BukkitTask abilityTask = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (guardian.isDead()) {
                    cancel();
                    return;
                }
                
                ticks++;
                
                // Mining fatigue every 8 seconds
                if (ticks % 160 == 0) {
                    applyMiningFatigue(guardian, event);
                }
                
                // Water blast every 5 seconds
                if (ticks % 100 == 0) {
                    waterBlast(guardian, event);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        eventTasks.put(event.getArenaId() + "_abilities", abilityTask);
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("§b§lDer Elder Guardian ist erwacht!");
                player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 2.0f, 0.5f);
            }
        }
    }
    
    private void spawnRavager(Event event, Location loc) {
        Ravager ravager = (Ravager) loc.getWorld().spawnEntity(loc, EntityType.RAVAGER);
        ravager.customName(Component.text("§6§lEvent Ravager"));
        
        // Make ravager more powerful
        ravager.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 3));
        ravager.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
        ravager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        
        event.setBoss(ravager);
        
        // Special abilities task
        BukkitTask abilityTask = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (ravager.isDead()) {
                    cancel();
                    return;
                }
                
                ticks++;
                
                // Charge attack every 6 seconds
                if (ticks % 120 == 0) {
                    chargeAttack(ravager, event);
                }
                
                // Roar every 10 seconds
                if (ticks % 200 == 0) {
                    ravagerRoar(ravager, event);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        eventTasks.put(event.getArenaId() + "_abilities", abilityTask);
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("§6§lDer Ravager ist erwacht!");
                player.playSound(player.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 2.0f, 0.5f);
            }
        }
    }
    
    private void spawnPhantomKing(Event event, Location loc) {
        Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
        phantom.customName(Component.text("§8§lPhantom King"));
        
        // Make phantom more powerful
        phantom.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        phantom.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
        phantom.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        
        event.setBoss(phantom);
        
        // Special abilities task
        BukkitTask abilityTask = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (phantom.isDead()) {
                    cancel();
                    return;
                }
                
                ticks++;
                
                // Dive attack every 4 seconds
                if (ticks % 80 == 0) {
                    diveAttack(phantom, event);
                }
                
                // Summon phantoms every 15 seconds
                if (ticks % 300 == 0) {
                    summonPhantoms(phantom, event);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        eventTasks.put(event.getArenaId() + "_abilities", abilityTask);
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("§8§lDer Phantom King ist erwacht!");
                player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 2.0f, 0.5f);
            }
        }
    }
    
    private void spawnBlazeKing(Event event, Location loc) {
        Blaze blaze = (Blaze) loc.getWorld().spawnEntity(loc, EntityType.BLAZE);
        blaze.customName(Component.text("§c§lBlaze King"));
        
        // Make blaze more powerful
        blaze.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        blaze.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
        blaze.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
        
        event.setBoss(blaze);
        
        // Special abilities task
        BukkitTask abilityTask = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (blaze.isDead()) {
                    cancel();
                    return;
                }
                
                ticks++;
                
                // Fire storm every 5 seconds
                if (ticks % 100 == 0) {
                    fireStorm(blaze, event);
                }
                
                // Explosive fireballs every 8 seconds
                if (ticks % 160 == 0) {
                    explosiveFireballs(blaze, event);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        eventTasks.put(event.getArenaId() + "_abilities", abilityTask);
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                player.sendMessage("§c§lDer Blaze King ist erwacht!");
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2.0f, 0.5f);
            }
        }
    }
    
    private void spawnEndermanLord(Event event, Location loc) {
        Enderman enderman = (Enderman) loc.getWorld().spawnEntity(loc, EntityType.ENDERMAN);
        enderman.customName(Component.text("§5§lEnderman Lord"));
        
        // Make enderman more powerful
        enderman.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 3));
        enderman.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
        enderman.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        
        event.setBoss(enderman);
        
        // Special abilities task
        BukkitTask abilityTask = new BukkitRunnable() {
            int ticks = 0;
            
            @Override
            public void run() {
                if (enderman.isDead()) {
                    cancel();
                    return;
                }
                
                ticks++;
                
                // Teleport attack every 3 seconds
                if (ticks % 60 == 0) {
                    teleportAttack(enderman, event);
                }
                
                // Summon endermen every 12 seconds
                if (ticks % 240 == 0) {
                    summonEndermen(enderman, event);
                }
                
                // Block throwing every 6 seconds
                if (ticks % 120 == 0) {
                    throwBlocks(enderman, event);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        
        eventTasks.put(event.getArenaId() + "_abilities", abilityTask);
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                player.sendMessage("§5§lDer Enderman Lord ist erwacht!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_AMBIENT, 2.0f, 0.5f);
            }
        }
    }
    
    private void groundSlam(Entity boss, Event event) {
        Location loc = boss.getLocation();
        
        // Create explosion effect
        loc.getWorld().createExplosion(loc, 0.0f, false, false);
        
        // Damage nearby players
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline() && player.getLocation().distance(loc) <= 5.0) {
                player.damage(8.0, boss);
                player.sendMessage("§cDer Boss hat den Boden geschlagen!");
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            }
        }
        
        // Particle effect
        for (int i = 0; i < 20; i++) {
            double angle = 2 * Math.PI * i / 20;
            double x = loc.getX() + 3 * Math.cos(angle);
            double z = loc.getZ() + 3 * Math.sin(angle);
            Location particleLoc = new Location(loc.getWorld(), x, loc.getY(), z);
            loc.getWorld().spawnParticle(Particle.EXPLOSION, particleLoc, 1);
        }
    }
    
    private void summonMinions(Entity boss, Event event) {
        Location loc = boss.getLocation();
        
        // Summon 3 zombies around the boss
        for (int i = 0; i < 3; i++) {
            double angle = 2 * Math.PI * i / 3;
            double x = loc.getX() + 3 * Math.cos(angle);
            double z = loc.getZ() + 3 * Math.sin(angle);
            Location minionLoc = new Location(loc.getWorld(), x, loc.getY(), z);
            
            Zombie minion = (Zombie) loc.getWorld().spawnEntity(minionLoc, EntityType.ZOMBIE);
            minion.customName(Component.text("§7§lBoss Minion"));
            minion.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            
            // Make minion target players
            for (UUID playerId : event.getPlayers()) {
                Player player = Bukkit.getPlayer(playerId);
                if (player != null && player.isOnline()) {
                    minion.setTarget(player);
                    break;
                }
            }
        }
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                player.sendMessage("§7Der Boss beschwört Minions!");
                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 1.0f);
            }
        }
    }
    
    // Elder Guardian Abilities
    private void applyMiningFatigue(Entity boss, Event event) {
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline() && player.getLocation().distance(boss.getLocation()) <= 10.0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 200, 2));
                player.sendMessage("§cDer Elder Guardian hat dir Mining Fatigue verursacht!");
                player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
            }
        }
    }
    
    private void waterBlast(Entity boss, Event event) {
        Location loc = boss.getLocation();
        
        // Create water particles
        for (int i = 0; i < 30; i++) {
            double angle = 2 * Math.PI * i / 30;
            double x = loc.getX() + 5 * Math.cos(angle);
            double z = loc.getZ() + 5 * Math.sin(angle);
            Location particleLoc = new Location(loc.getWorld(), x, loc.getY(), z);
            loc.getWorld().spawnParticle(Particle.SPLASH, particleLoc, 10);
        }
        
        // Damage nearby players
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline() && player.getLocation().distance(loc) <= 5.0) {
                player.damage(6.0, boss);
                player.sendMessage("§bDer Elder Guardian hat dich mit Wasser getroffen!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1.0f, 1.0f);
            }
        }
    }
    
    // Ravager Abilities
    private void chargeAttack(Entity boss, Event event) {
        Player target = null;
        double closestDistance = Double.MAX_VALUE;
        
        // Find closest player
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                double distance = player.getLocation().distance(boss.getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    target = player;
                }
            }
        }
        
        if (target != null) {
            // Charge towards target
            boss.setVelocity(target.getLocation().toVector().subtract(boss.getLocation().toVector()).normalize().multiply(1.5));
            
            // Damage if hit
            if (target.getLocation().distance(boss.getLocation()) <= 3.0) {
                target.damage(10.0, boss);
                target.sendMessage("§6Der Ravager hat dich gerammt!");
                target.playSound(target.getLocation(), Sound.ENTITY_RAVAGER_ATTACK, 1.0f, 1.0f);
            }
        }
    }
    
    private void ravagerRoar(Entity boss, Event event) {
        Location loc = boss.getLocation();
        
        // Apply fear effect to nearby players
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline() && player.getLocation().distance(loc) <= 8.0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
                player.sendMessage("§6Der Ravager brüllt dich an!");
                player.playSound(player.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 2.0f, 0.5f);
            }
        }
    }
    
    // Phantom King Abilities
    private void diveAttack(Entity boss, Event event) {
        Player target = null;
        double closestDistance = Double.MAX_VALUE;
        
        // Find closest player
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                double distance = player.getLocation().distance(boss.getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    target = player;
                }
            }
        }
        
        if (target != null) {
            // Dive towards target
            Location targetLoc = target.getLocation().add(0, 2, 0);
            boss.teleport(targetLoc);
            
            // Damage on impact
            target.damage(8.0, boss);
            target.sendMessage("§8Der Phantom King stürzt auf dich herab!");
            target.playSound(target.getLocation(), Sound.ENTITY_PHANTOM_BITE, 1.0f, 1.0f);
        }
    }
    
    private void summonPhantoms(Entity boss, Event event) {
        Location loc = boss.getLocation();
        
        // Summon 2 phantoms
        for (int i = 0; i < 2; i++) {
            double angle = 2 * Math.PI * i / 2;
            double x = loc.getX() + 5 * Math.cos(angle);
            double z = loc.getZ() + 5 * Math.sin(angle);
            Location phantomLoc = new Location(loc.getWorld(), x, loc.getY() + 3, z);
            
            Phantom phantom = (Phantom) loc.getWorld().spawnEntity(phantomLoc, EntityType.PHANTOM);
            phantom.customName(Component.text("§7§lPhantom Minion"));
            
            // Make phantom target players
            for (UUID playerId : event.getPlayers()) {
                Player player = Bukkit.getPlayer(playerId);
                if (player != null && player.isOnline()) {
                    phantom.setTarget(player);
                    break;
                }
            }
        }
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                player.sendMessage("§8Der Phantom King beschwört Phantoms!");
                player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 1.0f, 1.0f);
            }
        }
    }
    
    // Blaze King Abilities
    private void fireStorm(Entity boss, Event event) {
        Location loc = boss.getLocation();
        
        // Create fire particles in a storm pattern
        for (int i = 0; i < 50; i++) {
            double angle = 2 * Math.PI * i / 50;
            double radius = 3 + Math.random() * 4;
            double x = loc.getX() + radius * Math.cos(angle);
            double z = loc.getZ() + radius * Math.sin(angle);
            double y = loc.getY() + Math.random() * 3;
            Location particleLoc = new Location(loc.getWorld(), x, y, z);
            
            loc.getWorld().spawnParticle(Particle.FLAME, particleLoc, 1);
            
            // Set fire on blocks
            if (particleLoc.getBlock().getType().isAir()) {
                particleLoc.getBlock().setType(Material.FIRE);
            }
        }
        
        // Damage players in storm
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline() && player.getLocation().distance(loc) <= 7.0) {
                player.damage(5.0, boss);
                player.setFireTicks(60);
                player.sendMessage("§cDu bist im Feuersturm gefangen!");
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
            }
        }
    }
    
    private void explosiveFireballs(Entity boss, Event event) {
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                // Shoot explosive fireball at player
                Location fireballLoc = boss.getLocation().add(0, 1, 0);
                org.bukkit.entity.Fireball fireball = boss.getWorld().spawn(fireballLoc, org.bukkit.entity.Fireball.class);
                
                // Set direction towards player
                fireball.setDirection(player.getLocation().toVector().subtract(fireballLoc.toVector()).normalize());
                fireball.setYield(2.0f); // Explosion power
                fireball.setIsIncendiary(true);
            }
        }
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                player.sendMessage("§cDer Blaze King schießt explosive Feuerbälle!");
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
            }
        }
    }
    
    // Enderman Lord Abilities
    private void teleportAttack(Entity boss, Event event) {
        Player target = null;
        double closestDistance = Double.MAX_VALUE;
        
        // Find closest player
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                double distance = player.getLocation().distance(boss.getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    target = player;
                }
            }
        }
        
        if (target != null) {
            // Teleport behind player
            Location teleportLoc = target.getLocation().add(
                target.getLocation().getDirection().multiply(-2).setY(0)
            );
            boss.teleport(teleportLoc);
            
            // Attack immediately
            target.damage(6.0, boss);
            target.sendMessage("§5Der Enderman Lord teleportiert sich hinter dich!");
            target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
    }
    
    private void summonEndermen(Entity boss, Event event) {
        Location loc = boss.getLocation();
        
        // Summon 3 endermen
        for (int i = 0; i < 3; i++) {
            double angle = 2 * Math.PI * i / 3;
            double x = loc.getX() + 4 * Math.cos(angle);
            double z = loc.getZ() + 4 * Math.sin(angle);
            Location endermanLoc = new Location(loc.getWorld(), x, loc.getY(), z);
            
            Enderman enderman = (Enderman) loc.getWorld().spawnEntity(endermanLoc, EntityType.ENDERMAN);
            enderman.customName(Component.text("§7§lEnderman Minion"));
            enderman.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            
            // Make enderman target players
            for (UUID playerId : event.getPlayers()) {
                Player player = Bukkit.getPlayer(playerId);
                if (player != null && player.isOnline()) {
                    enderman.setTarget(player);
                    break;
                }
            }
        }
        
        // Announce
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                player.sendMessage("§5Der Enderman Lord beschwört Endermen!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_AMBIENT, 1.0f, 1.0f);
            }
        }
    }
    
    private void throwBlocks(Entity boss, Event event) {
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline() && player.getLocation().distance(boss.getLocation()) <= 10.0) {
                // Throw a block at player
                Location blockLoc = player.getLocation().add(0, 2, 0);
                org.bukkit.entity.FallingBlock fallingBlock = boss.getWorld().spawnFallingBlock(
                    blockLoc, Material.STONE.createBlockData()
                );
                
                // Set velocity towards player
                fallingBlock.setVelocity(boss.getLocation().toVector().subtract(blockLoc.toVector()).normalize().multiply(-0.5));
                fallingBlock.setHurtEntities(true);
                fallingBlock.setDropItem(false);
                
                player.sendMessage("§5Der Enderman Lord wirft Blöcke auf dich!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0f, 1.0f);
            }
        }
    }
    
    private void endEvent(Event event) {
        event.setActive(false);
        
        // Calculate rewards
        for (UUID playerId : event.getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline() && event.getBoss() != null && event.getBoss().isDead()) {
                // Victory rewards
                List<EventReward> rewards = event.getReward();
                double totalReward = rewards.stream().mapToDouble(EventReward::getAmount).sum();
                // Give money through economy manager
                if (plugin.getEconomyManager() != null) {
                    plugin.getEconomyManager().giveMoney(player, totalReward);
                }
                // plugin.getEconomyManager().giveMoney(player, totalReward);
                
                player.sendMessage("§a§lEvent gewonnen!");
                player.sendMessage("§eBelohnung: §f" + totalReward + " coins");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                
                // Give experience
                player.giveExp(100);
                
                // Give unique rewards based on boss type
                giveUniqueRewards(player, event.getArenaId());
                
            } else {
                // Defeat message
                player.sendMessage("§c§lEvent verloren!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            }
            
            // Teleport back to spawn
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
        
        // Clean up
        playerEvents.entrySet().removeIf(entry -> entry.getValue().equals(event.getArenaId()));
        eventTasks.values().forEach(BukkitTask::cancel);
        eventTasks.clear();
        activeEvents.remove(event.getArenaId());
    }
    
    public Event getEvent(String eventId) {
        if (activeEvents.containsKey(eventId)) {
            return activeEvents.get(eventId);
        }
        
        // Create new event
        Arena arena = arenas.get(eventId);
        if (arena == null) return null;
        
        Event event = new Event(eventId, arena.getName(), arena.getId());
        activeEvents.put(eventId, event);
        return event;
    }
    
    public List<Event> getAvailableEvents() {
        return new ArrayList<>(activeEvents.values());
    }
    
    public boolean isPlayerInEvent(Player player) {
        return playerEvents.containsKey(player.getUniqueId());
    }
    
    public String getPlayerEvent(Player player) {
        return playerEvents.get(player.getUniqueId());
    }
    
    public void shutdown() {
        // End all active events
        for (Event event : activeEvents.values()) {
            endEvent(event);
        }
        
        // Cancel all tasks
        eventTasks.values().forEach(BukkitTask::cancel);
        eventTasks.clear();
        
        // Clear maps
        activeEvents.clear();
        playerEvents.clear();
    }
    
    private void giveUniqueRewards(Player player, String arenaId) {
        switch (arenaId) {
            case "ender_dragon" -> {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.DRAGON_EGG));
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.ELYTRA));
                player.sendMessage("§5§lDu hast ein Drachenei und Elytra erhalten!");
            }
            case "wither" -> {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 2));
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.WITHER_SKELETON_SKULL));
                player.sendMessage("§8§lDu hast Nether Stars und einen Wither-Skull erhalten!");
            }
            case "custom_boss" -> {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.TOTEM_OF_UNDYING));
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.DIAMOND, 5));
                player.sendMessage("§2§lDu hast ein Totem der Unsterblichkeit und Diamanten erhalten!");
            }
            case "elder_guardian" -> {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.PRISMARINE_SHARD, 10));
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.SPONGE, 3));
                player.sendMessage("§b§lDu hast Prismarin-Splitter und Schwämme erhalten!");
            }
            case "ravager" -> {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.IRON_AXE));
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.EMERALD, 8));
                player.sendMessage("§6§lDu hast eine Eisen-Axt und Smaragde erhalten!");
            }
            case "phantom_king" -> {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.PHANTOM_MEMBRANE, 5));
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.FEATHER, 10));
                player.sendMessage("§8§lDu hast Phantom-Membranen und Federn erhalten!");
            }
            case "blaze_king" -> {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.BLAZE_POWDER, 10));
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.FIRE_CHARGE, 5));
                player.sendMessage("§c§lDu hast Blaze-Pulver und Feuerkugeln erhalten!");
            }
            case "enderman_lord" -> {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.ENDER_PEARL, 8));
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.ENDER_EYE, 3));
                player.sendMessage("§5§lDu hast Ender-Perlen und Ender-Augen erhalten!");
            }
        }
    }
}
