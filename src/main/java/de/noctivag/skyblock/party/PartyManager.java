package de.noctivag.skyblock.party;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PartyManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, Party> playerToParty;

    public PartyManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.playerToParty = new HashMap<>();
    }

    public boolean hasParty(UUID playerId) {
        return playerToParty.containsKey(playerId);
    }

    public Party getParty(UUID playerId) {
        return playerToParty.get(playerId);
    }

    public Party createParty(Player leader) {
        if (hasParty(leader.getUniqueId())) return getParty(leader.getUniqueId());
        Party party = new Party(leader.getUniqueId());
        playerToParty.put(leader.getUniqueId(), party);
        return party;
    }

    public void disbandParty(Player leader) {
        Party party = getParty(leader.getUniqueId());
        if (party == null || !party.getLeader().equals(leader.getUniqueId())) return;
        for (UUID member : new HashSet<>(party.getMembers())) {
            playerToParty.remove(member);
            Player p = Bukkit.getPlayer(member);
            if (p != null) p.sendMessage("§cDeine Party wurde aufgelöst.");
        }
    }

    public boolean invite(Player leader, Player target) {
        Party party = getParty(leader.getUniqueId());
        if (party == null) party = createParty(leader);
        if (party.getMembers().contains(target.getUniqueId())) return false;
        party.getInvites().add(target.getUniqueId());
        target.sendMessage("§e" + leader.getName() + " §7hat dich in eine Party eingeladen. Nutze §a/party accept " + leader.getName());
        return true;
    }

    public boolean accept(Player player, Player leader) {
        Party party = getParty(leader.getUniqueId());
        if (party == null) return false;
        if (!party.getInvites().remove(player.getUniqueId())) return false;
        party.getMembers().add(player.getUniqueId());
        playerToParty.put(player.getUniqueId(), party);
        broadcast(party, "§a" + player.getName() + " §7ist der Party beigetreten.");
        return true;
    }

    public void leave(Player player) {
        Party party = getParty(player.getUniqueId());
        if (party == null) return;
        if (party.getLeader().equals(player.getUniqueId())) {
            disbandParty(player);
            return;
        }
        party.getMembers().remove(player.getUniqueId());
        playerToParty.remove(player.getUniqueId());
        broadcast(party, "§c" + player.getName() + " §7hat die Party verlassen.");
    }

    public void broadcast(Party party, String message) {
        for (UUID member : party.getMembers()) {
            Player p = Bukkit.getPlayer(member);
            if (p != null) p.sendMessage(message);
        }
    }
}


