package de.noctivag.plugin.managers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import de.noctivag.plugin.utils.ColorUtils;
import net.kyori.adventure.text.Component;

import java.util.concurrent.atomic.AtomicLong;

public class RestartManager {
    private final Plugin plugin;
    private Thread task;
    private final AtomicLong remainingSeconds = new AtomicLong(0);

    public RestartManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean isScheduled() {
        return task != null;
    }

    public long getRemainingSeconds() {
        return remainingSeconds.get();
    }

    public void scheduleRestart(long seconds) {
        cancelRestart();
        remainingSeconds.set(seconds);
        
        // Use virtual thread for Folia compatibility
        task = Thread.ofVirtual().start(() -> {
            try {
                while (plugin.isEnabled() && remainingSeconds.get() > 0) {
                    long remain = remainingSeconds.getAndDecrement();
                    if (remain <= 0) {
                        // perform restart - execute on main thread
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            plugin.getLogger().info("Performing scheduled restart now...");
                            Bukkit.getServer().shutdown();
                        });
                        cancelRestart();
                        break;
                    } else if (remain == 60 || remain == 30 || remain == 10 || remain <= 5) {
                        Component msg = ColorUtils.translate("§cServer wird in " + remain + " Sekunden neu gestartet!");
                        for (var p : Bukkit.getOnlinePlayers()) p.sendMessage(msg);
                        plugin.getLogger().info("Server will restart in " + remain + " seconds");
                    }
                    
                    Thread.sleep(1000); // Wait 1 second = 1000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void cancelRestart() {
        if (task != null) {
            task.interrupt();
            task = null;
            remainingSeconds.set(0);
            Component msg = ColorUtils.translate("§aGeplanter Neustart wurde abgebrochen.");
            for (var p : Bukkit.getOnlinePlayers()) p.sendMessage(msg);
            plugin.getLogger().info("Scheduled restart cancelled");
        }
    }
}
