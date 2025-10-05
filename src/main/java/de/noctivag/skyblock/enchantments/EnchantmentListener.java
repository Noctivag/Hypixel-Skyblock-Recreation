package de.noctivag.skyblock.enchantments;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Random;
import net.kyori.adventure.text.Component;

/**
 * Enchantment Listener - Handles enchantment effects and GUI interactions
 */
public class EnchantmentListener implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final CustomEnchantmentSystem enchantmentSystem;
    private final EnchantmentGUI enchantmentGUI;
    private final Random random;
    
    public EnchantmentListener(SkyblockPlugin SkyblockPlugin, CustomEnchantmentSystem enchantmentSystem, 
                              EnchantmentGUI enchantmentGUI) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.enchantmentSystem = enchantmentSystem;
        this.enchantmentGUI = enchantmentGUI;
        this.random = new Random();
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;
        
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || weapon.getType().isAir()) return;
        
        // Apply weapon enchantment effects
        applyWeaponEnchantments(player, target, weapon, event);
    }
    
    private void applyWeaponEnchantments(Player player, LivingEntity target, ItemStack weapon, 
                                        EntityDamageByEntityEvent event) {
        for (CustomEnchantmentSystem.CustomEnchantment enchantment : enchantmentSystem.getAllEnchantments()) {
            int level = enchantmentSystem.getEnchantmentLevel(weapon, enchantment);
            if (level <= 0) continue;
            
            switch (enchantment.getName()) {
                case "Sharpness" -> applySharpness(event, level);
                case "Giant Killer" -> applyGiantKiller(event, target, level);
                case "Execute" -> applyExecute(event, target, level);
                case "First Strike" -> applyFirstStrike(player, target, event, level);
                case "Triple-Strike" -> applyTripleStrike(player, target, event, level);
                case "Thunderlord" -> applyThunderlord(player, target, level);
                case "Fire Aspect" -> applyFireAspect(target, level);
                case "Venomous" -> applyVenomous(target, level);
                case "Life Steal" -> applyLifeSteal(player, event, level);
                case "Syphon" -> applySyphon(player, event, level);
                case "Scavenger" -> applyScavenger(player, event, level);
            }
        }
    }
    
    private void applySharpness(EntityDamageByEntityEvent event, int level) {
        double damage = event.getDamage();
        event.setDamage(damage + (level * 0.25)); // 25% damage per level
    }
    
    private void applyGiantKiller(EntityDamageByEntityEvent event, LivingEntity target, int level) {
        double targetHealth = target.getHealth();
        double maxHealth = target.getMaxHealth();
        double healthPercentage = targetHealth / maxHealth;
        
        if (healthPercentage > 0.5) { // Only affects high-health enemies
            double bonusDamage = event.getDamage() * (level * 0.05); // 5% per level
            event.setDamage(event.getDamage() + bonusDamage);
        }
    }
    
    private void applyExecute(EntityDamageByEntityEvent event, LivingEntity target, int level) {
        double targetHealth = target.getHealth();
        double maxHealth = target.getMaxHealth();
        double healthPercentage = targetHealth / maxHealth;
        
        if (healthPercentage < 0.3) { // Only affects low-health enemies
            double bonusDamage = event.getDamage() * (level * 0.1); // 10% per level
            event.setDamage(event.getDamage() + bonusDamage);
        }
    }
    
    private void applyFirstStrike(Player player, LivingEntity target, EntityDamageByEntityEvent event, int level) {
        // Check if this is the first hit (simplified check)
        if (random.nextDouble() < 0.3) { // 30% chance
            double bonusDamage = event.getDamage() * (level * 0.2); // 20% per level
            event.setDamage(event.getDamage() + bonusDamage);
            
            player.sendMessage("§aFirst Strike! " + String.format("%.1f", bonusDamage) + " bonus damage!");
        }
    }
    
    private void applyTripleStrike(Player player, LivingEntity target, EntityDamageByEntityEvent event, int level) {
        double chance = level * 0.1; // 10% chance per level
        if (random.nextDouble() < chance) {
            // Deal additional damage twice
            double additionalDamage = event.getDamage() * 0.5;
            event.setDamage(event.getDamage() + (additionalDamage * 2));
            
            player.sendMessage("§6Triple Strike! " + String.format("%.1f", additionalDamage * 2) + " bonus damage!");
            
            // Visual effects
            target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 10, 0.5, 0.5, 0.5, 0.1);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.5f);
        }
    }
    
    private void applyThunderlord(Player player, LivingEntity target, int level) {
        double chance = level * 0.15; // 15% chance per level
        if (random.nextDouble() < chance) {
            // Lightning effect
            target.getWorld().strikeLightningEffect(target.getLocation());
            
            // Damage nearby entities
            for (Entity nearby : target.getNearbyEntities(3, 3, 3)) {
                if (nearby instanceof LivingEntity nearbyEntity && nearby != player) {
                    nearbyEntity.damage(level * 2.0);
                }
            }
            
            player.sendMessage(Component.text("§eThunderlord strikes!"));
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        }
    }
    
    private void applyFireAspect(LivingEntity target, int level) {
        int fireTicks = level * 20; // 1 second per level
        target.setFireTicks(fireTicks);
    }
    
    private void applyVenomous(LivingEntity target, int level) {
        int duration = level * 60; // 3 seconds per level
        int amplifier = Math.min(level - 1, 4); // Max amplifier 4
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, amplifier));
    }
    
    private void applyLifeSteal(Player player, EntityDamageByEntityEvent event, int level) {
        double damage = event.getDamage();
        double healAmount = damage * (level * 0.05); // 5% per level
        double newHealth = Math.min(player.getHealth() + healAmount, player.getMaxHealth());
        player.setHealth(newHealth);
        
        if (healAmount > 0) {
            player.sendMessage("§c+" + String.format("%.1f", healAmount) + " ❤ Life Steal!");
        }
    }
    
    private void applySyphon(Player player, EntityDamageByEntityEvent event, int level) {
        // Simplified critical hit check
        if (random.nextDouble() < 0.2) { // 20% chance for "critical hit"
            double damage = event.getDamage();
            double healAmount = damage * (level * 0.1); // 10% per level
            double newHealth = Math.min(player.getHealth() + healAmount, player.getMaxHealth());
            player.setHealth(newHealth);
            
            player.sendMessage("§c+" + String.format("%.1f", healAmount) + " ❤ Syphon!");
        }
    }
    
    private void applyScavenger(Player player, EntityDamageByEntityEvent event, int level) {
        double damage = event.getDamage();
        double coinBonus = damage * (level * 0.1); // 10% per level
        
        // Add coins to player (simplified)
        player.sendMessage("§6+" + String.format("%.0f", coinBonus) + " coins §7(Scavenger)");
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        Inventory inventory = event.getInventory();
        String title = inventory.getType().toString();
        
        if (title != null && title.contains("Enchantment Table")) {
            event.setCancelled(true);
            handleEnchantmentGUIClick(player, event);
        }
    }
    
    private void handleEnchantmentGUIClick(Player player, InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        
        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;
        
        // Check for category buttons
        NamespacedKey categoryKey = new NamespacedKey(SkyblockPlugin, "category");
        if (meta.getPersistentDataContainer().has(categoryKey, PersistentDataType.STRING)) {
            String category = meta.getPersistentDataContainer().get(categoryKey, PersistentDataType.STRING);
            enchantmentGUI.openEnchantmentCategory(player, category);
            return;
        }
        
        // Check for enchantment books
        NamespacedKey bookKey = new NamespacedKey(SkyblockPlugin, "enchantment_book");
        if (meta.getPersistentDataContainer().has(bookKey, PersistentDataType.STRING)) {
            String bookData = meta.getPersistentDataContainer().get(bookKey, PersistentDataType.STRING);
            handleEnchantmentBookPurchase(player, bookData);
            return;
        }
        
        // Check for action buttons
        NamespacedKey buttonKey = new NamespacedKey(SkyblockPlugin, "button_action");
        if (meta.getPersistentDataContainer().has(buttonKey, PersistentDataType.STRING)) {
            String action = meta.getPersistentDataContainer().get(buttonKey, PersistentDataType.STRING);
            handleButtonAction(player, action);
            return;
        }
        
        // Check for enchantment items
        NamespacedKey enchantmentKey = new NamespacedKey(SkyblockPlugin, "enchantment_item");
        if (meta.getPersistentDataContainer().has(enchantmentKey, PersistentDataType.STRING)) {
            String enchantmentName = meta.getPersistentDataContainer().get(enchantmentKey, PersistentDataType.STRING);
            showEnchantmentLevels(player, enchantmentName);
            return;
        }
    }
    
    private void handleEnchantmentBookPurchase(Player player, String bookData) {
        String[] parts = bookData.split(":");
        if (parts.length != 3) return;
        
        String enchantmentName = parts[0];
        int level = Integer.parseInt(parts[1]);
        double cost = Double.parseDouble(parts[2]);
        
        // Check if player has enough coins (simplified)
        if (hasEnoughCoins(player, cost)) {
            // Give enchantment book to player
            ItemStack book = createEnchantmentBook(enchantmentName, level);
            player.getInventory().addItem(book);
            
            // Deduct coins (simplified)
            player.sendMessage("§aPurchased " + enchantmentName + " " + level + " for " + 
                             String.format("%.0f", cost) + " coins!");
        } else {
            player.sendMessage(Component.text("§cYou don't have enough coins!"));
        }
    }
    
    private void handleButtonAction(Player player, String action) {
        switch (action) {
            case "back" -> enchantmentGUI.openEnchantmentTable(player);
            case "remove" -> {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item != null && !item.getType().isAir()) {
                    clearItemEnchantments(item);
                    player.sendMessage(Component.text("§aRemoved all enchantments from your item!"));
                } else {
                    player.sendMessage(Component.text("§cYou must hold an item to remove enchantments!"));
                }
            }
            case "apply" -> {
                player.sendMessage(Component.text("§eDrag enchantment books onto items to apply them!"));
            }
        }
    }
    
    private void showEnchantmentLevels(Player player, String enchantmentName) {
        CustomEnchantmentSystem.CustomEnchantment enchantment = 
            enchantmentSystem.getEnchantment(enchantmentName);
        
        if (enchantment != null) {
            player.sendMessage("§6=== " + enchantment.getName() + " Levels ===");
            for (int level = 1; level <= enchantment.getMaxLevel(); level++) {
                double cost = enchantmentSystem.calculateEnchantmentCost(enchantment, level);
                player.sendMessage("§7Level " + level + ": §6" + String.format("%.0f", cost) + " coins");
            }
        }
    }
    
    private boolean hasEnoughCoins(Player player, double amount) {
        // Simplified coin check - in real implementation, check player's balance
        return true; // For demo purposes
    }
    
    private ItemStack createEnchantmentBook(String enchantmentName, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize("§d" + enchantmentName + " " + level));
        meta.lore(java.util.Arrays.asList(
            LegacyComponentSerializer.legacySection().deserialize("§7Right-click to apply to an item!"),
            LegacyComponentSerializer.legacySection().deserialize(""),
            LegacyComponentSerializer.legacySection().deserialize("§eDrag onto an item to enchant!")
        ));
        
        book.setItemMeta(meta);
        return book;
    }
    
    private void clearItemEnchantments(ItemStack item) {
        for (CustomEnchantmentSystem.CustomEnchantment enchantment : enchantmentSystem.getAllEnchantments()) {
            enchantmentSystem.applyEnchantment(item, enchantment, 0);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return;
        
        // Check if this is an enchantment book
        if (meta.hasLore() && meta.getLore().stream().anyMatch(line -> 
            line.toString().contains("§7Right-click to apply to an item!"))) {
            String displayName = meta.getDisplayName() != null ? meta.getDisplayName() : "";
            String enchantmentName = displayName.replace("§d", "").split(" ")[0];
            int level = Integer.parseInt(displayName.split(" ")[1]);
            
            event.getPlayer().sendMessage("§eDrag this book onto an item to apply " + 
                                        enchantmentName + " " + level + "!");
        }
    }
}
