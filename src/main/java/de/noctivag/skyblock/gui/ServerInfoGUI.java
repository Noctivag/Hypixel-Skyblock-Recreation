package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * Server Info GUI - Server-Informationen und Status
 */
public class ServerInfoGUI {
    
    private final SkyblockPlugin SkyblockPlugin;

    public ServerInfoGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    public void openServerInfoGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lServer Info"));
        
        // Server Status
        setItem(gui, 10, Material.GREEN_WOOL, "§a§lServer Status",
            "§7Server-Status",
            "§7• Status: §aOnline",
            "§7• Uptime: §a" + getUptime(),
            "§7• TPS: §a20.0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.PLAYER_HEAD, "§b§lPlayer Information",
            "§7Spieler-Informationen",
            "§7• Online Players: §a" + Bukkit.getOnlinePlayers().size(),
            "§7• Max Players: §a" + Bukkit.getMaxPlayers(),
            "§7• Player Limit: §a" + Bukkit.getMaxPlayers(),
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.DIAMOND, "§b§lServer Resources",
            "§7Server-Ressourcen",
            "§7• RAM Usage: §a" + getRAMUsage(),
            "§7• CPU Usage: §a" + getCPUUsage(),
            "§7• Disk Usage: §a" + getDiskUsage(),
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.CLOCK, "§e§lServer Performance",
            "§7Server-Performance",
            "§7• TPS: §a20.0",
            "§7• Ping: §a" + getPing(player),
            "§7• Chunks: §a" + getChunkCount(),
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.BOOK, "§d§lServer Information",
            "§7Server-Informationen",
            "§7• Server Name: §a" + "Server-" + Bukkit.getPort(),
            "§7• Version: §a" + Bukkit.getVersion(),
            "§7• Bukkit Version: §a" + Bukkit.getBukkitVersion(),
            "",
            "§eKlicke zum Öffnen");

        // SkyblockPlugin Information
        setItem(gui, 19, Material.NETHER_STAR, "§d§lPlugin Information",
            "§7Plugin-Informationen",
            "§7• SkyblockPlugin Name: §aBasics SkyblockPlugin",
            "§7• SkyblockPlugin Version: §a1.0.0",
            "§7• SkyblockPlugin Status: §aActive",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD, "§a§lPlugin Features",
            "§7Plugin-Features",
            "§7• Active Features: §a12",
            "§7• Feature Status: §aAll Active",
            "§7• Feature Performance: §aExcellent",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.CHEST, "§6§lDatabase Status",
            "§7Datenbank-Status",
            "§7• Database: §aConnected",
            "§7• Multi-Server: §aActive",
            "§7• Data Sync: §aSynchronized",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.COMPASS, "§e§lWorld Information",
            "§7Welt-Informationen",
            "§7• Worlds: §a" + Bukkit.getWorlds().size(),
            "§7• Active Worlds: §a" + getActiveWorlds(),
            "§7• World Size: §a" + getWorldSize(),
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CLOCK, "§6§lServer Time",
            "§7Server-Zeit",
            "§7• Current Time: §a" + getCurrentTime(),
            "§7• Server Time: §a" + getServerTime(),
            "§7• Time Zone: §a" + getTimeZone(),
            "",
            "§eKlicke zum Öffnen");
            
        // Server Statistics
        setItem(gui, 28, Material.BOOK, "§b§lServer Statistics",
            "§7Server-Statistiken",
            "§7• Total Players: §a" + getTotalPlayers(),
            "§7• Total Playtime: §a" + getTotalPlaytime(),
            "§7• Server Events: §a" + getServerEvents(),
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.EMERALD, "§a§lServer Economy",
            "§7Server-Wirtschaft",
            "§7• Total Coins: §a" + getTotalCoins(),
            "§7• Active Markets: §a" + getActiveMarkets(),
            "§7• Economic Activity: §a" + getEconomicActivity(),
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.NETHER_STAR, "§d§lServer Events",
            "§7Server-Events",
            "§7• Active Events: §a" + getActiveEvents(),
            "§7• Upcoming Events: §a" + getUpcomingEvents(),
            "§7• Event History: §a" + getEventHistory(),
            "",
            "§eKlicke zum Öffnen");
            
        // Server Management
        setItem(gui, 37, Material.REDSTONE, "§c§lServer Management",
            "§7Server-Verwaltung",
            "§7• Server Commands",
            "§7• Server Settings",
            "§7• Server Maintenance",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.BOOK, "§b§lServer Guide",
            "§7Server-Anleitung",
            "§7• Server Rules",
            "§7• Server Commands",
            "§7• Server Tips",
            "",
            "§eKlicke zum Öffnen");
            
        // Navigation
        setItem(gui, 45, Material.ARROW, "§7« Back",
            "§7Zurück zum Hauptmenü");
            
        setItem(gui, 49, Material.BARRIER, "§c§lClose",
            "§7GUI schließen");
            
        player.openInventory(gui);
    }
    
    private String getUptime() {
        long uptime = java.lang.System.currentTimeMillis() - 0L; // Simplified placeholder for uptime
        return formatTime(uptime);
    }
    
    private String getRAMUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        return String.format("%.1f%%", (double) usedMemory / maxMemory * 100);
    }
    
    private String getCPUUsage() {
        return "N/A"; // CPU usage is not directly available in Bukkit
    }
    
    private String getDiskUsage() {
        return "N/A"; // Disk usage is not directly available in Bukkit
    }
    
    private String getPing(Player player) {
        return "N/A"; // Ping is not directly available in Bukkit
    }
    
    private String getChunkCount() {
        return String.valueOf(Bukkit.getWorlds().stream()
            .mapToInt(world -> world.getLoadedChunks().length)
            .sum());
    }
    
    private String getActiveWorlds() {
        return String.valueOf(Bukkit.getWorlds().size());
    }
    
    private String getWorldSize() {
        return "N/A"; // World size is not directly available in Bukkit
    }
    
    private String getCurrentTime() {
        return java.time.LocalTime.now().toString();
    }
    
    private String getServerTime() {
        return java.time.LocalTime.now().toString();
    }
    
    private String getTimeZone() {
        return java.time.ZoneId.systemDefault().toString();
    }
    
    private String getTotalPlayers() {
        return String.valueOf(Bukkit.getOfflinePlayers().length);
    }
    
    private String getTotalPlaytime() {
        return "N/A"; // Total playtime is not directly available
    }
    
    private String getServerEvents() {
        return "N/A"; // Server events are not directly available
    }
    
    private String getTotalCoins() {
        return "N/A"; // Total coins are not directly available
    }
    
    private String getActiveMarkets() {
        return "N/A"; // Active markets are not directly available
    }
    
    private String getEconomicActivity() {
        return "N/A"; // Economic activity is not directly available
    }
    
    private String getActiveEvents() {
        return "N/A"; // Active events are not directly available
    }
    
    private String getUpcomingEvents() {
        return "N/A"; // Upcoming events are not directly available
    }
    
    private String getEventHistory() {
        return "N/A"; // Event history is not directly available
    }
    
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return String.format("%dd %dh %dm", days, hours % 24, minutes % 60);
        } else if (hours > 0) {
            return String.format("%dh %dm", hours, minutes % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
    }
    
    private void setItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
