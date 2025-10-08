package de.noctivag.skyblock.gui.admin;

import de.noctivag.skyblock.mobs.HypixelMobType;
import de.noctivag.skyblock.mobs.HypixelMobRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin-GUI zum Spawnen aller Hypixel-Mobs
 */
public class MobSpawnerAdminGUI {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§cMob Spawner (Admin)");
        List<HypixelMobType> mobs = new ArrayList<>(List.of(HypixelMobType.values()));
        for (int i = 0; i < Math.min(mobs.size(), 45); i++) {
            HypixelMobType mob = mobs.get(i);
            ItemStack mobEgg = new ItemStack(Material.ZOMBIE_SPAWN_EGG); // Platzhalter, kann je nach Mob angepasst werden
            ItemMeta meta = mobEgg.getItemMeta();
            meta.displayName(net.kyori.adventure.text.Component.text("§e" + mob.displayName));
            List<net.kyori.adventure.text.Component> lore = new ArrayList<>();
            lore.add(net.kyori.adventure.text.Component.text("§7Leben: §c" + mob.maxHealth));
            for (String ab : mob.abilities) lore.add(net.kyori.adventure.text.Component.text("§7" + ab));
            lore.add(net.kyori.adventure.text.Component.text("§8Typ: " + mob.behavior));
            meta.lore(lore);
            mobEgg.setItemMeta(meta);
            inv.setItem(i, mobEgg);
        }
        // Schließen-Button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(net.kyori.adventure.text.Component.text("§cSchließen"));
        close.setItemMeta(closeMeta);
        inv.setItem(49, close);
        player.openInventory(inv);
    }
}
