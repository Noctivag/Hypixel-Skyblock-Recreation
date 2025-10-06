package de.noctivag.skyblock.guilds;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Guild System - Hypixel Skyblock Style
 * 
 * Features:
 * - Guild Creation and Management
 * - Guild Levels and Experience
 * - Guild Coins and Gems
 * - Guild Members and Roles
 * - Guild Permissions
 * - Guild Chat
 * - Guild Island
 * - Guild Wars
 * - Guild Events
 * - Guild Shop
 * - Guild Bank
 * - Guild Upgrades
 * - Guild Achievements
 * - Guild Leaderboards
 * - Guild Alliances
 * - Guild Rivalries
 * - Guild Quests
 * - Guild Rewards
 */
public class AdvancedGuildSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, Guild> playerGuilds = new ConcurrentHashMap<>();
    private final Map<String, Guild> guilds = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> guildTasks = new ConcurrentHashMap<>();
    
    public AdvancedGuildSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        loadGuilds();
        startGuildUpdateTask();
    }
    
    private void loadGuilds() {
        // Load guilds from database
        try {
            // TODO: Implement database loading when database methods are available
            SkyblockPlugin.getLogger().info("Loading guilds from database...");
        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to load guilds: " + e.getMessage());
        }
    }
    
    private void startGuildUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Guild guild : guilds.values()) {
                    guild.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L); // Every minute
    }
    
    public boolean createGuild(Player player, String guildName, String guildTag) {
        if (playerGuilds.containsKey(player.getUniqueId())) {
            player.sendMessage(Component.text("§cDu bist bereits in einer Gilde!"));
            return false;
        }
        
        if (guildName.length() < 3 || guildName.length() > 30) {
            player.sendMessage(Component.text("§cGildenname muss zwischen 3 und 30 Zeichen lang sein!"));
            return false;
        }
        
        if (guildTag.length() < 2 || guildTag.length() > 6) {
            player.sendMessage(Component.text("§cGildentag muss zwischen 2 und 6 Zeichen lang sein!"));
            return false;
        }
        
        // Check if guild name already exists
        for (Guild guild : guilds.values()) {
            if (guild.getName().equalsIgnoreCase(guildName)) {
                player.sendMessage(Component.text("§cDieser Gildenname ist bereits vergeben!"));
                return false;
            }
            if (guild.getTag().equalsIgnoreCase(guildTag)) {
                player.sendMessage(Component.text("§cDieser Gildentag ist bereits vergeben!"));
                return false;
            }
        }
        
        String guildId = UUID.randomUUID().toString();
        Guild guild = new Guild(guildId, guildName, guildTag, player.getUniqueId(), 1, 0, 0, 1, 5, "Eine neue Gilde!");
        guilds.put(guildId, guild);
        playerGuilds.put(player.getUniqueId(), guild);
        
        // Save to database
        databaseManager.executeUpdate("""
            INSERT INTO guilds (guild_id, guild_name, guild_tag, owner_uuid, level, experience, coins, member_count, max_members, description)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, guildId, guildName, guildTag, player.getUniqueId(), 1, 0, 0, 1, 5, "Eine neue Gilde!");
        
        databaseManager.executeUpdate("""
            INSERT INTO guild_members (guild_id, member_uuid, role, joined_at, contributed_coins, contributed_gems)
            VALUES (?, ?, 'owner', NOW(), 0, 0)
        """, guildId, player.getUniqueId());
        
        player.sendMessage("§aGilde erstellt: " + guildName + " [" + guildTag + "]");
        return true;
    }
    
    public boolean joinGuild(Player player, String guildName) {
        if (playerGuilds.containsKey(player.getUniqueId())) {
            player.sendMessage(Component.text("§cDu bist bereits in einer Gilde!"));
            return false;
        }
        
        Guild guild = null;
        for (Guild g : guilds.values()) {
            if (g.getName().equalsIgnoreCase(guildName)) {
                guild = g;
                break;
            }
        }
        
        if (guild == null) {
            player.sendMessage(Component.text("§cGilde nicht gefunden!"));
            return false;
        }
        
        if (guild.getMemberCount() >= guild.getMaxMembers()) {
            player.sendMessage(Component.text("§cDie Gilde ist voll!"));
            return false;
        }
        
        guild.addMember(player.getUniqueId(), GuildRole.MEMBER);
        playerGuilds.put(player.getUniqueId(), guild);
        
        // Save to database
        databaseManager.executeUpdate("""
            INSERT INTO guild_members (guild_id, member_uuid, role, joined_at, contributed_coins, contributed_gems)
            VALUES (?, ?, 'member', NOW(), 0, 0)
        """, guild.getId(), player.getUniqueId());
        
        databaseManager.executeUpdate("UPDATE guilds SET member_count = member_count + 1 WHERE guild_id = ?", guild.getId());
        
        player.sendMessage("§aDu bist der Gilde " + guild.getName() + " beigetreten!");
        guild.broadcastMessage("§a" + player.getName() + " ist der Gilde beigetreten!");
        return true;
    }
    
    public boolean leaveGuild(Player player) {
        Guild guild = playerGuilds.get(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(Component.text("§cDu bist in keiner Gilde!"));
            return false;
        }
        
        if (guild.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("§cAls Gildenbesitzer kannst du die Gilde nicht verlassen! Verwende /guild disband um die Gilde aufzulösen."));
            return false;
        }
        
        guild.removeMember(player.getUniqueId());
        playerGuilds.remove(player.getUniqueId());
        
        // Update database
        databaseManager.executeUpdate("DELETE FROM guild_members WHERE guild_id = ? AND member_uuid = ?", guild.getId(), player.getUniqueId());
        databaseManager.executeUpdate("UPDATE guilds SET member_count = member_count - 1 WHERE guild_id = ?", guild.getId());
        
        player.sendMessage("§cDu hast die Gilde " + guild.getName() + " verlassen!");
        guild.broadcastMessage("§c" + player.getName() + " hat die Gilde verlassen!");
        return true;
    }
    
    public boolean disbandGuild(Player player) {
        Guild guild = playerGuilds.get(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(Component.text("§cDu bist in keiner Gilde!"));
            return false;
        }
        
        if (!guild.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("§cNur der Gildenbesitzer kann die Gilde auflösen!"));
            return false;
        }
        
        // Remove all members
        for (UUID memberId : guild.getMembers().keySet()) {
            playerGuilds.remove(memberId);
        }
        
        // Remove from database
        databaseManager.executeUpdate("DELETE FROM guild_members WHERE guild_id = ?", guild.getId());
        databaseManager.executeUpdate("DELETE FROM guilds WHERE guild_id = ?", guild.getId());
        
        guilds.remove(guild.getId());
        
        player.sendMessage("§cGilde " + guild.getName() + " wurde aufgelöst!");
        return true;
    }
    
    public boolean promoteMember(Player player, String targetName, GuildRole newRole) {
        Guild guild = playerGuilds.get(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(Component.text("§cDu bist in keiner Gilde!"));
            return false;
        }
        
        GuildRole playerRole = guild.getMemberRole(player.getUniqueId());
        if (playerRole != GuildRole.LEADER && playerRole != GuildRole.OFFICER) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung, Mitglieder zu befördern!"));
            return false;
        }
        
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(Component.text("§cSpieler nicht gefunden!"));
            return false;
        }
        
        if (!guild.hasMember(target.getUniqueId())) {
            player.sendMessage(Component.text("§cDieser Spieler ist nicht in deiner Gilde!"));
            return false;
        }
        
        guild.setMemberRole(target.getUniqueId(), newRole);
        
        // Update database
        databaseManager.executeUpdate("UPDATE guild_members SET role = ? WHERE guild_id = ? AND member_uuid = ?", 
            newRole.name().toLowerCase(), guild.getId(), target.getUniqueId());
        
        player.sendMessage("§a" + target.getName() + " wurde zu " + newRole.getDisplayName() + " befördert!");
        target.sendMessage("§aDu wurdest zu " + newRole.getDisplayName() + " befördert!");
        return true;
    }
    
    public boolean kickMember(Player player, String targetName) {
        Guild guild = playerGuilds.get(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(Component.text("§cDu bist in keiner Gilde!"));
            return false;
        }
        
        GuildRole playerRole = guild.getMemberRole(player.getUniqueId());
        if (playerRole != GuildRole.LEADER && playerRole != GuildRole.OFFICER) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung, Mitglieder zu kicken!"));
            return false;
        }
        
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(Component.text("§cSpieler nicht gefunden!"));
            return false;
        }
        
        if (!guild.hasMember(target.getUniqueId())) {
            player.sendMessage(Component.text("§cDieser Spieler ist nicht in deiner Gilde!"));
            return false;
        }
        
        GuildRole targetRole = guild.getMemberRole(target.getUniqueId());
        if (targetRole == GuildRole.LEADER) {
            player.sendMessage(Component.text("§cDu kannst den Gildenbesitzer nicht kicken!"));
            return false;
        }
        
        if (playerRole == GuildRole.OFFICER && targetRole == GuildRole.OFFICER) {
            player.sendMessage(Component.text("§cOffiziere können andere Offiziere nicht kicken!"));
            return false;
        }
        
        guild.removeMember(target.getUniqueId());
        playerGuilds.remove(target.getUniqueId());
        
        // Update database
        databaseManager.executeUpdate("DELETE FROM guild_members WHERE guild_id = ? AND member_uuid = ?", guild.getId(), target.getUniqueId());
        databaseManager.executeUpdate("UPDATE guilds SET member_count = member_count - 1 WHERE guild_id = ?", guild.getId());
        
        player.sendMessage("§c" + target.getName() + " wurde aus der Gilde gekickt!");
        target.sendMessage("§cDu wurdest aus der Gilde " + guild.getName() + " gekickt!");
        guild.broadcastMessage("§c" + target.getName() + " wurde aus der Gilde gekickt!");
        return true;
    }
    
    public void contributeCoins(Player player, double amount) {
        Guild guild = playerGuilds.get(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(Component.text("§cDu bist in keiner Gilde!"));
            return;
        }
        
        // Check if player has enough coins
        // This would need to be integrated with the economy system
        
        guild.addCoins(amount);
        guild.addMemberContribution(player.getUniqueId(), amount, 0);
        
        // Update database
        databaseManager.executeUpdate("UPDATE guilds SET coins = coins + ? WHERE guild_id = ?", amount, guild.getId());
        databaseManager.executeUpdate("UPDATE guild_members SET contributed_coins = contributed_coins + ? WHERE guild_id = ? AND member_uuid = ?", 
            amount, guild.getId(), player.getUniqueId());
        
        player.sendMessage("§aDu hast " + amount + " Coins zur Gildenbank beigetragen!");
        guild.broadcastMessage("§a" + player.getName() + " hat " + amount + " Coins zur Gildenbank beigetragen!");
    }
    
    public Guild getPlayerGuild(UUID playerId) {
        return playerGuilds.get(playerId);
    }
    
    public Guild getGuild(String guildId) {
        return guilds.get(guildId);
    }
    
    public List<Guild> getAllGuilds() {
        return new ArrayList<>(guilds.values());
    }
    
}
