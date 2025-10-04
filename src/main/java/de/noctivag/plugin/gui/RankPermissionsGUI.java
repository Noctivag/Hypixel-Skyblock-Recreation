package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankPermissionsGUI extends CustomGUI {
    private final Plugin plugin;
    private final String rankKey;

    public RankPermissionsGUI(Plugin plugin, String rankKey) {
        super(54, Component.text("§6§lPermissions - " + plugin.getRankManager().getDisplayName(rankKey)));
        this.plugin = plugin;
        this.rankKey = rankKey;
    }

    public void open(Player admin) {
        clearInventory();

        List<String> perms = new ArrayList<>(plugin.getRankManager().getPermissions(rankKey));
        int slot = 10;
        for (String perm : perms) {
            setItem(slot, createPermissionItem(perm, true));
            slot += (slot % 9 == 7) ? 3 : 1;
        }

        // Add placeholder to add new permission via chat command (future extension)
        setItem(49, createGuiItem(Material.ARROW, "§7Zurück", "§7Zu Rängen"));
        admin.openInventory(getInventory());
    }

    private ItemStack createPermissionItem(String permission, boolean enabled) {
        String name = (enabled ? "§a" : "§c") + permission;
        List<String> lore = new ArrayList<>();
        lore.add(enabled ? "§7Klicke zum Entfernen" : "§7Klicke zum Hinzufügen");
        return createGuiItem(enabled ? Material.LIME_DYE : Material.GRAY_DYE, name, lore.toArray(new String[0]));
    }

    public String getRankKey() { return rankKey; }
}


