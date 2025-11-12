package de.noctivag.skyblock.items.accessories;

import de.noctivag.skyblock.items.accessories.combat.*;
import de.noctivag.skyblock.items.accessories.farming.*;
import de.noctivag.skyblock.items.accessories.fishing.*;
import de.noctivag.skyblock.items.accessories.mining.*;
import de.noctivag.skyblock.items.accessories.speed.*;
import de.noctivag.skyblock.items.accessories.utility.*;
import de.noctivag.skyblock.items.accessories.special.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Central registry for all accessory items
 * 60+ Accessories including Talismans, Rings, Artifacts, and Relics
 */
public class AccessoryRegistry {

    private static final Map<String, BaseAccessory> accessories = new HashMap<>();
    private static final Logger logger = Logger.getLogger("SkyblockPlugin");
    private static boolean initialized = false;

    /**
     * Register all accessories - called on plugin startup
     */
    public static void registerAllAccessories() {
        if (initialized) {
            logger.warning("Accessories already registered!");
            return;
        }

        logger.info("Registering accessories...");
        int count = 0;

        // COMBAT ACCESSORIES (Bat Family)
        registerAccessory(new BatTalisman());
        registerAccessory(new BatRing());
        registerAccessory(new BatArtifact());
        count += 3;

        // COMBAT (Spider Family)
        registerAccessory(new SpiderTalisman());
        registerAccessory(new SpiderRingTalisman());
        registerAccessory(new SpiderArtifactTalisman());
        count += 3;

        // COMBAT (Wolf Family)
        registerAccessory(new WolfTalisman());
        registerAccessory(new WolfRingTalisman());
        count += 2;

        // COMBAT (Zombie Family)
        registerAccessory(new ZombieTalisman());
        registerAccessory(new ZombieRingTalisman());
        registerAccessory(new ZombieArtifactTalisman());
        count += 3;

        // COMBAT (Other)
        registerAccessory(new SkeletonTalisman());
        registerAccessory(new VampireTalisman());
        registerAccessory(new RedClawTalisman());
        count += 3;

        // COMBAT (Slayer)
        registerAccessory(new TarantulaTalisman());
        registerAccessory(new RevenantViscera());
        registerAccessory(new SvenPackmaster());
        count += 3;

        // SPEED ACCESSORIES (Speed Family)
        registerAccessory(new SpeedTalisman());
        registerAccessory(new SpeedRing());
        registerAccessory(new SpeedArtifact());
        count += 3;

        // SPEED (Feather Family)
        registerAccessory(new FeatherTalisman());
        registerAccessory(new FeatherRingTalisman());
        registerAccessory(new FeatherArtifactTalisman());
        registerAccessory(new CheetahTalisman());
        count += 4;

        // FARMING ACCESSORIES
        registerAccessory(new FarmingTalisman());
        count += 1;

        // MINING ACCESSORIES (Mine Affinity Family)
        registerAccessory(new MineAffinityTalisman());
        registerAccessory(new MineAffinityRingTalisman());
        registerAccessory(new MineAffinityArtifactTalisman());
        registerAccessory(new MagneticTalisman());
        registerAccessory(new TitaniumTalisman());
        registerAccessory(new MiningExpTalisman());
        registerAccessory(new MithrilGourmand());
        registerAccessory(new MinersPrize());
        count += 8;

        // FISHING ACCESSORIES (Sea Creature Family)
        registerAccessory(new SeaCreatureTalisman());
        registerAccessory(new SeaCreatureRingTalisman());
        registerAccessory(new SeaCreatureArtifactTalisman());
        registerAccessory(new BaitRingTalisman());
        registerAccessory(new FishAffinityTalisman());
        registerAccessory(new SpongeTalisman());
        count += 6;

        // FORAGING ACCESSORIES (Wood Affinity Family)
        registerAccessory(new WoodAffinityTalisman());
        registerAccessory(new WoodAffinityRingTalisman());
        registerAccessory(new CampfireTalisman());
        count += 3;

        // UTILITY ACCESSORIES (Personal Compactor)
        registerAccessory(new PersonalCompactorIIITalisman());
        registerAccessory(new PersonalCompactorIVTalisman());
        count += 2;

        // UTILITY (Piggy Bank Family)
        registerAccessory(new PiggyBankTalisman());
        registerAccessory(new CrackedPiggyBankTalisman());
        registerAccessory(new BrokenPiggyBankTalisman());
        count += 3;

        // UTILITY (Potion Affinity Family)
        registerAccessory(new PotionTalisman());
        registerAccessory(new PotionRingTalisman());
        registerAccessory(new PotionArtifactTalisman());
        count += 3;

        // UTILITY (Candy Family)
        registerAccessory(new CandyTalisman());
        registerAccessory(new CandyRingTalisman());
        registerAccessory(new CandyArtifactTalisman());
        count += 3;

        // UTILITY (Scavenger Family)
        registerAccessory(new ScavengerTalisman());
        registerAccessory(new ScavengerRingTalisman());
        count += 2;

        // UTILITY (Other)
        registerAccessory(new NightVisionTalisman());
        count += 1;

        // SPECIAL ACCESSORIES
        registerAccessory(new HegemonyTalisman());
        registerAccessory(new TreasureTalisman());
        registerAccessory(new TreasureRingTalisman());
        registerAccessory(new MelodyTalisman());
        registerAccessory(new CrookedArtifactTalisman());
        registerAccessory(new SealOfTheFamilyTalisman());
        registerAccessory(new RiftPrismTalisman());
        registerAccessory(new CrownOfGreed());
        registerAccessory(new MoltenNecklace());
        registerAccessory(new EnigmaSoul());
        count += 10;

        initialized = true;
        logger.info("Successfully registered " + count + " accessories!");
        logger.info("Magical Power system ready with " + accessories.size() + " unique items");
    }

    /**
     * Register a single accessory
     */
    private static void registerAccessory(BaseAccessory accessory) {
        String key = accessory.getClass().getSimpleName().toUpperCase();
        accessories.put(key, accessory);
    }

    /**
     * Get an accessory by its class name
     */
    public static BaseAccessory getAccessory(String name) {
        return accessories.get(name.toUpperCase());
    }

    /**
     * Get all registered accessories
     */
    public static Map<String, BaseAccessory> getAllAccessories() {
        return new HashMap<>(accessories);
    }

    /**
     * Get total magical power for a list of accessories
     */
    public static int calculateTotalMagicalPower(java.util.List<BaseAccessory> playerAccessories) {
        Map<BaseAccessory.AccessoryFamily, BaseAccessory> bestPerFamily = new HashMap<>();

        // Only count the best tier from each family
        for (BaseAccessory acc : playerAccessories) {
            BaseAccessory.AccessoryFamily family = acc.getFamily();

            if (!bestPerFamily.containsKey(family) ||
                acc.getTier().getTier() > bestPerFamily.get(family).getTier().getTier()) {
                bestPerFamily.put(family, acc);
            }
        }

        // Calculate total magical power
        int total = 0;
        for (BaseAccessory acc : bestPerFamily.values()) {
            int power = acc.getMagicalPower();

            // Hegemony gives double magical power
            if (acc.getClass().getSimpleName().contains("Hegemony")) {
                power *= 2;
            }

            total += power;
        }

        return total;
    }

    /**
     * Check if accessories are registered
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Get count of registered accessories
     */
    public static int getAccessoryCount() {
        return accessories.size();
    }
}
