package de.noctivag.skyblock.features.pets.types;
import org.bukkit.inventory.ItemStack;

/**
 * Pet Stats class for pet statistics
 */
public class PetStats {
    private PetType petType;
    private int level;
    private PetRarity rarity;
    private double health;
    private double defense;
    private double strength;
    private double speed;
    private double intelligence;
    private double critChance;
    private double critDamage;
    private double attackSpeed;
    private double ferocity;
    private double magicFind;
    private double petLuck;
    private double seaCreatureChance;
    private double fishingSpeed;
    private double miningSpeed;
    private double farmingFortune;
    private double foragingFortune;

    public PetStats(PetType petType, int level, PetRarity rarity) {
        this.petType = petType;
        this.level = level;
        this.rarity = rarity;
        this.health = petType.getBaseHealth();
        this.defense = petType.getBaseDefense();
        this.strength = petType.getBaseStrength();
        this.speed = petType.getBaseSpeed();
        this.intelligence = petType.getBaseIntelligence();
        this.critChance = petType.getBaseCritChance();
        this.critDamage = petType.getBaseCritDamage();
        this.attackSpeed = petType.getBaseAttackSpeed();
        this.ferocity = petType.getBaseFerocity();
        this.magicFind = petType.getBaseMagicFind();
        this.petLuck = petType.getBasePetLuck();
        this.seaCreatureChance = petType.getBaseSeaCreatureChance();
        this.fishingSpeed = petType.getBaseFishingSpeed();
        this.miningSpeed = petType.getBaseMiningSpeed();
        this.farmingFortune = petType.getBaseFarmingFortune();
        this.foragingFortune = petType.getBaseForagingFortune();
    }

    public PetType getPetType() {
        return petType;
    }

    public int getLevel() {
        return level;
    }

    public PetRarity getRarity() {
        return rarity;
    }

    public double getHealth() {
        return health;
    }

    public double getDefense() {
        return defense;
    }

    public double getStrength() {
        return strength;
    }

    public double getSpeed() {
        return speed;
    }

    public double getIntelligence() {
        return intelligence;
    }

    public double getCritChance() {
        return critChance;
    }

    public double getCritDamage() {
        return critDamage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public double getFerocity() {
        return ferocity;
    }

    public double getMagicFind() {
        return magicFind;
    }

    public double getPetLuck() {
        return petLuck;
    }

    public double getSeaCreatureChance() {
        return seaCreatureChance;
    }

    public double getFishingSpeed() {
        return fishingSpeed;
    }

    public double getMiningSpeed() {
        return miningSpeed;
    }

    public double getFarmingFortune() {
        return farmingFortune;
    }

    public double getForagingFortune() {
        return foragingFortune;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRarity(PetRarity rarity) {
        this.rarity = rarity;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setIntelligence(double intelligence) {
        this.intelligence = intelligence;
    }

    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }

    public void setCritDamage(double critDamage) {
        this.critDamage = critDamage;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setFerocity(double ferocity) {
        this.ferocity = ferocity;
    }

    public void setMagicFind(double magicFind) {
        this.magicFind = magicFind;
    }

    public void setPetLuck(double petLuck) {
        this.petLuck = petLuck;
    }

    public void setSeaCreatureChance(double seaCreatureChance) {
        this.seaCreatureChance = seaCreatureChance;
    }

    public void setFishingSpeed(double fishingSpeed) {
        this.fishingSpeed = fishingSpeed;
    }

    public void setMiningSpeed(double miningSpeed) {
        this.miningSpeed = miningSpeed;
    }

    public void setFarmingFortune(double farmingFortune) {
        this.farmingFortune = farmingFortune;
    }

    public void setForagingFortune(double foragingFortune) {
        this.foragingFortune = foragingFortune;
    }
}
