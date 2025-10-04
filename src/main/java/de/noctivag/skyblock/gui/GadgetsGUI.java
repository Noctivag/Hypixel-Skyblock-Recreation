package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GadgetsGUI extends CustomGUI {
	private final SkyblockPlugin plugin;

	public GadgetsGUI(SkyblockPlugin plugin) {
		super(27, Component.text("§a§lGadgets"));
		this.plugin = plugin;
	}

	public void open(Player player) {
		clearInventory();
		setItem(11, createGuiItem(Material.SLIME_BALL, "§aSprung-Gadget", "§7Wirft dich in die Luft"));
		setItem(15, createGuiItem(Material.FIREWORK_ROCKET, "§bFeuerwerk", "§7Zünde ein Feuerwerk"));
		setItem(22, createGuiItem(Material.ARROW, "§cZurück", "§7Zu Kosmetik"));
		player.openInventory(getInventory());
	}
}
