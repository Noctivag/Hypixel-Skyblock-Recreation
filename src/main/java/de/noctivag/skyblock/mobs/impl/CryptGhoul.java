package de.noctivag.skyblock.mobs.impl;

import net.kyori.adventure.text.Component;
import de.noctivag.skyblock.mobs.CustomMob;
import de.noctivag.skyblock.mobs.abilities.ExplodeAbility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Crypt Ghoul - Found in the Graveyard
 * Reference: Hypixel Wiki - Crypt Ghoul
 * Special ability: Produces a small explosion upon death
 */
public class CryptGhoul extends CustomMob {
    
    public CryptGhoul(String mobId, Location spawnLocation) {
        super(mobId, EntityType.ZOMBIE, spawnLocation, 
              80.0,   // maxHealth
              20.0,   // damage
              3.0,    // defense
              12.0    // combatXP
        );
        
        // Add explosion ability on death
        addAbility(new ExplodeAbility(2.0f, false, true, 0));
    }
    
    @Override
    public String getName() {
        return "§8Crypt Ghoul";
    }
    
    @Override
    public String getLootTableId() {
        return "crypt_ghoul";
    }
    
    @Override
    public void applyBaseAttributes(org.bukkit.entity.Entity entity) {
        super.applyBaseAttributes(entity);
        
        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            
            // Set equipment
            zombie.getEquipment().setHelmet(createCryptHelmet());
            zombie.getEquipment().setChestplate(createCryptChestplate());
            zombie.getEquipment().setLeggings(createCryptLeggings());
            zombie.getEquipment().setBoots(createCryptBoots());
            zombie.getEquipment().setItemInMainHand(createCryptSword());
            
            // Set equipment drop chances
            zombie.getEquipment().setHelmetDropChance(0.05f);
            zombie.getEquipment().setChestplateDropChance(0.05f);
            zombie.getEquipment().setLeggingsDropChance(0.05f);
            zombie.getEquipment().setBootsDropChance(0.05f);
            zombie.getEquipment().setItemInMainHandDropChance(0.02f);
            
            // Make it a regular zombie
            zombie.setBaby(false);
        }
    }
    
    /**
     * Create crypt helmet
     */
    private ItemStack createCryptHelmet() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta meta = helmet.getItemMeta();
        meta.displayName(Component.text("§8Crypt Helmet"));
        helmet.setItemMeta(meta);
        return helmet;
    }
    
    /**
     * Create crypt chestplate
     */
    private ItemStack createCryptChestplate() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta meta = chestplate.getItemMeta();
        meta.displayName(Component.text("§8Crypt Chestplate"));
        chestplate.setItemMeta(meta);
        return chestplate;
    }
    
    /**
     * Create crypt leggings
     */
    private ItemStack createCryptLeggings() {
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemMeta meta = leggings.getItemMeta();
        meta.displayName(Component.text("§8Crypt Leggings"));
        leggings.setItemMeta(meta);
        return leggings;
    }
    
    /**
     * Create crypt boots
     */
    private ItemStack createCryptBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = boots.getItemMeta();
        meta.displayName(Component.text("§8Crypt Boots"));
        boots.setItemMeta(meta);
        return boots;
    }
    
    /**
     * Create crypt sword
     */
    private ItemStack createCryptSword() {
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.displayName(Component.text("§8Crypt Sword"));
        sword.setItemMeta(meta);
        return sword;
    }
}
