package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.items.SkyblockItem;
import de.noctivag.skyblock.items.SkyblockItemBuilder;
import de.noctivag.skyblock.stats.StatType;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveSkyblockItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        de.noctivag.skyblock.items.DungeonStar stars = new de.noctivag.skyblock.items.DungeonStar(3);
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können dieses Kommando nutzen.");
            return true;
        }
        // Beispiel: /givesbitem
        SkyblockItem sword = new SkyblockItem("§bAspect of the AI", "Legendär");
        sword.setStat(StatType.STRENGTH, 100);
        sword.setStat(StatType.CRIT_DAMAGE, 50);
        sword.setStat(StatType.INTELLIGENCE, 150);
        sword.setReforgeType(de.noctivag.skyblock.items.ReforgeType.WISE);
        sword.setAbility("AI Beam: Schießt einen Laserstrahl (Rechtsklick)");
        sword.getEnchantments().add(de.noctivag.skyblock.items.EnchantmentType.SHARPNESS, 5);
        sword.getEnchantments().add(de.noctivag.skyblock.items.EnchantmentType.CRITICAL, 3);
        sword.setDungeonStar(stars);
        sword.setHotPotatoBooks(7);
        sword.getUltimateEnchantments().add(de.noctivag.skyblock.items.EnchantmentType.ENDER_SLAYER, 1);
        player.getInventory().addItem(SkyblockItemBuilder.build(sword, Material.DIAMOND_SWORD));
        player.sendMessage("§aDu hast ein Skyblock-Item mit Reforge, Enchantments, DungeonStars, Hot Potato Books und Ultimate Enchantments erhalten!");
        return true;
    }
}
