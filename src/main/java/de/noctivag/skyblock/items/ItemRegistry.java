package de.noctivag.skyblock.items;

import de.noctivag.skyblock.items.weapons.*;
import de.noctivag.skyblock.items.armor.*;
import de.noctivag.skyblock.items.tools.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for all custom Skyblock items
 * Stores and retrieves items by ID
 */
public class ItemRegistry {

    private static final Map<String, CustomItem> items = new HashMap<>();

    /**
     * Initialize all items
     */
    public static void registerAllItems() {
        // Swords
        registerItem(new AspectOfTheEnd());
        registerItem(new AspectOfTheDragons());
        registerItem(new EdibleMace());
        registerItem(new EmberRod());
        registerItem(new EndStoneSword());
        registerItem(new FancySword());
        registerItem(new GoldSword());
        registerItem(new HunterKnife());
        registerItem(new LeapingSword());
        registerItem(new LividDagger());
        registerItem(new MidasSword());
        registerItem(new OminousBlade());
        registerItem(new OrnateSword());
        registerItem(new PigmanSword());
        registerItem(new RagnarokAxe());
        registerItem(new RapierSword());
        registerItem(new ReaperFalchion());
        registerItem(new ReaperScythe());
        registerItem(new RevenantFalchion());
        registerItem(new ShadowFury());
        registerItem(new SilverFang());
        registerItem(new TacticiansEdge());
        registerItem(new UnderdLordSword());
        registerItem(new ValkyrieSword());
        registerItem(new VoidEdge());
        registerItem(new VoidSword());
        registerItem(new YetiSword());
        registerItem(new AtomsplitKatana());
        registerItem(new BalrogsSword());
        registerItem(new BonemerangSword());
        registerItem(new ClaymoreSword());
        registerItem(new DemonSword());
        registerItem(new EnderSword());
        registerItem(new FloridZombiesword());
        registerItem(new GolemSword());
        registerItem(new GraveSword());
        registerItem(new HyperionSword());
        registerItem(new InkWandSword());
        registerItem(new JujuShortbow());
        registerItem(new LastBreathSword());
        registerItem(new MagmaBow());
        registerItem(new MakoBlade());
        registerItem(new NecronBlade());
        registerItem(new OrbitalLauncher());
        registerItem(new PhantomRod());
        registerItem(new PrecursorEye());
        registerItem(new ScorpiusBow());
        registerItem(new ScyllaBow());
        registerItem(new ShadeWand());
        registerItem(new SpiritSceptre());
        registerItem(new StarfallSword());
        registerItem(new TerminatorBow());
        registerItem(new ValkyrieBlade());
        registerItem(new VampiricAspect());
        registerItem(new VolcanoStaff());
        registerItem(new WitherBlade());

        // Armor Sets
        registerItem(new DivanHelmet());
        registerItem(new DivanChestplate());
        registerItem(new DivanLeggings());
        registerItem(new DivanBoots());

        registerItem(new NecronHelmet());
        registerItem(new NecronChestplate());
        registerItem(new NecronLeggings());
        registerItem(new NecronBoots());

        registerItem(new ShadowAssassinHelmet());
        registerItem(new ShadowAssassinChestplate());
        registerItem(new ShadowAssassinLeggings());
        registerItem(new ShadowAssassinBoots());

        registerItem(new StormHelmet());
        registerItem(new StormChestplate());
        registerItem(new StormLeggings());
        registerItem(new StormBoots());

        registerItem(new GoldenDragonHelmet());
        registerItem(new GoldenDragonChestplate());
        registerItem(new GoldenDragonLeggings());
        registerItem(new GoldenDragonBoots());

        registerItem(new SuperiorDragonHelmet());
        registerItem(new SuperiorDragonChestplate());
        registerItem(new SuperiorDragonLeggings());
        registerItem(new SuperiorDragonBoots());

        // Tools
        registerItem(new DwarvenDrillX355());
        registerItem(new GemstoneGauntlet());
        registerItem(new MithrilPickaxe());
        registerItem(new TitaniumPickaxe());
        registerItem(new DivanDrill());
        registerItem(new X655FuryPick());
        registerItem(new SludgePickaxe());
        registerItem(new JunglePickaxe());
        registerItem(new TrevorHoe());
        registerItem(new MathematicalHoe());
        registerItem(new NewtonHoe());

        // Fishing Rods
        registerItem(new AureleaRod());
        registerItem(new ShredderRod());
        registerItem(new MooingRod());
        registerItem(new InfernoRod());
        registerItem(new HellcastRod());
        registerItem(new SlugfishRod());

        System.out.println("Registered " + items.size() + " custom Skyblock items!");
    }

    /**
     * Register a custom item
     */
    private static void registerItem(CustomItem item) {
        items.put(item.getItemId(), item);
    }

    /**
     * Get item by ID
     */
    public static CustomItem getItem(String itemId) {
        return items.get(itemId);
    }

    /**
     * Get ItemStack by ID
     */
    public static ItemStack getItemStack(String itemId) {
        CustomItem item = getItem(itemId);
        return item != null ? item.create() : null;
    }

    /**
     * Get all registered items
     */
    public static Map<String, CustomItem> getAllItems() {
        return new HashMap<>(items);
    }

    /**
     * Check if item exists
     */
    public static boolean hasItem(String itemId) {
        return items.containsKey(itemId);
    }
}
