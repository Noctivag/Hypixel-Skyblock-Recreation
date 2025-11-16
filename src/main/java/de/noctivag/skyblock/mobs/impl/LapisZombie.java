package de.noctivag.skyblock.mobs.impl;

import net.kyori.adventure.text.Component;
import de.noctivag.skyblock.mobs.CustomMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Lapis Zombie - Found in the Lapis Quarry (Gold Mine)
 * Reference: Hypixel Wiki - Lapis Zombie
 */
public class LapisZombie extends CustomMob {
    
    public LapisZombie(String mobId, Location spawnLocation) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              100.0,  // maxHealth
              25.0,   // damage
              5.0,    // defense
              15.0    // combatXP
        );
    }
    
    @Override
    public String getName() {
        return "§9Lapis Zombie";
    }
    
    @Override
    public String getLootTableId() {
        return "lapis_zombie";
    }
    
    @Override
    public void applyBaseAttributes(org.bukkit.entity.Entity entity) {
        super.applyBaseAttributes(entity);
        
        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            
            // Set equipment
            zombie.getEquipment().setHelmet(createLapisHelmet());
            zombie.getEquipment().setChestplate(createLapisChestplate());
            zombie.getEquipment().setLeggings(createLapisLeggings());
            zombie.getEquipment().setBoots(createLapisBoots());
            zombie.getEquipment().setItemInMainHand(createLapisPickaxe());
            
            // Set equipment drop chances
            zombie.getEquipment().setHelmetDropChance(0.1f);
            zombie.getEquipment().setChestplateDropChance(0.1f);
            zombie.getEquipment().setLeggingsDropChance(0.1f);
            zombie.getEquipment().setBootsDropChance(0.1f);
            zombie.getEquipment().setItemInMainHandDropChance(0.05f);
            
            // Make it a baby zombie for more challenge
            zombie.setBaby(false);
        }
    }
    
    /**
     * Create lapis helmet
     */
    private ItemStack createLapisHelmet() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta meta = helmet.getItemMeta();
        meta.displayName(Component.text("§9Lapis Helmet"));
        helmet.setItemMeta(meta);
        return helmet;
    }
    
    /**
     * Create lapis chestplate
     */
    private ItemStack createLapisChestplate() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta meta = chestplate.getItemMeta();
        meta.displayName(Component.text("§9Lapis Chestplate"));
        chestplate.setItemMeta(meta);
        return chestplate;
    }
    
    /**
     * Create lapis leggings
     */
    private ItemStack createLapisLeggings() {
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemMeta meta = leggings.getItemMeta();
        meta.displayName(Component.text("§9Lapis Leggings"));
        leggings.setItemMeta(meta);
        return leggings;
    }
    
    /**
     * Create lapis boots
     */
    private ItemStack createLapisBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = boots.getItemMeta();
        meta.displayName(Component.text("§9Lapis Boots"));
        boots.setItemMeta(meta);
        return boots;
    }
    
    /**
     * Create lapis pickaxe
     */
    private ItemStack createLapisPickaxe() {
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta meta = pickaxe.getItemMeta();
        meta.displayName(Component.text("§9Lapis Pickaxe"));
        pickaxe.setItemMeta(meta);
        return pickaxe;
    }
}
