package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GadgetsGUI extends CustomGUI {
	private final SkyblockPlugin SkyblockPlugin;

	public GadgetsGUI(SkyblockPlugin SkyblockPlugin) {
		super(27, Component.text("§a§lGadgets"));
		this.SkyblockPlugin = SkyblockPlugin;
	}

	public void open(Player player) {
		clearInventory();
		setItem(11, createGuiItem(Material.SLIME_BALL, "§aSprung-Gadget", "§7Wirft dich in die Luft"));
		setItem(15, createGuiItem(Material.FIREWORK_ROCKET, "§bFeuerwerk", "§7Zünde ein Feuerwerk"));
		setItem(22, createGuiItem(Material.ARROW, "§cZurück", "§7Zu Kosmetik"));
		player.openInventory(getInventory());
	}
}
