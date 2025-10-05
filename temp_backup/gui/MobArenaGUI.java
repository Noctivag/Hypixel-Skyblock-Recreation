package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class MobArenaGUI extends CustomGUI {
    private final Player player;
    
    public MobArenaGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(SkyblockPlugin, "§4§lMob Arena", 54);
        this.player = player;
        setupItems();
    }
    
    private void setupItems() {
        setItem(10, Material.ZOMBIE_HEAD, "§2§lZombie Arena", "§7Kämpfe gegen Zombies");
        setItem(12, Material.SKELETON_SKULL, "§8§lSkeleton Arena", "§7Kämpfe gegen Skelette");
        setItem(14, Material.CREEPER_HEAD, "§a§lCreeper Arena", "§7Kämpfe gegen Creeper");
        setItem(16, Material.ENDERMAN_SPAWN_EGG, "§5§lEnderman Arena", "§7Kämpfe gegen Endermen");
        setItem(45, Material.ARROW, "§7Zurück", "§7Zum Hauptmenü");
    }
    
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
