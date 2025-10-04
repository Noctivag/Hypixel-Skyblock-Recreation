package de.noctivag.skyblock.core.architecture;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * MongoDB Schema Implementation - Embedded Document Pattern for Player Profiles
 * 
 * This implementation follows the embedded document pattern as specified:
 * - PlayerProfile is the main document with embedded sub-documents
 * - All player data is embedded to minimize database queries on login
 * - Optimized for read-heavy workloads typical in gaming scenarios
 * 
 * Schema Design:
 * - PlayerProfile: Main document containing all player data
 * - Embedded: AccessoriesBag, SkillXP, CollectionProgress, RNGMeterState
 * - Optimized indexes for common queries
 */
public class MongoDBSchema {
    
    private static final Logger logger = Logger.getLogger(MongoDBSchema.class.getName());
    
    // Database and collection names
    private static final String DATABASE_NAME = "hypixel_skyblock_recreation";
    private static final String PLAYER_PROFILES_COLLECTION = "player_profiles";
    private static final String BAZAAR_ORDERS_COLLECTION = "bazaar_orders";
    private static final String AUCTION_HOUSE_COLLECTION = "auction_house";
    private static final String GUILD_DATA_COLLECTION = "guild_data";
    private static final String SERVER_METRICS_COLLECTION = "server_metrics";
    
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> playerProfiles;
    private MongoCollection<Document> bazaarOrders;
    private MongoCollection<Document> auctionHouse;
    private MongoCollection<Document> guildData;
    private MongoCollection<Document> serverMetrics;
    
    private final ExecutorService asyncExecutor;
    
    public MongoDBSchema() {
        this.asyncExecutor = Executors.newFixedThreadPool(8, r -> new Thread(r, "MongoDB-Async-Thread"));
    }
    
    /**
     * Initialize MongoDB connection and create indexes
     */
    public CompletableFuture<Void> initialize(String connectionString) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Connect to MongoDB
                mongoClient = MongoClients.create(connectionString);
                database = mongoClient.getDatabase(DATABASE_NAME);
                
                // Get collections
                playerProfiles = database.getCollection(PLAYER_PROFILES_COLLECTION);
                bazaarOrders = database.getCollection(BAZAAR_ORDERS_COLLECTION);
                auctionHouse = database.getCollection(AUCTION_HOUSE_COLLECTION);
                guildData = database.getCollection(GUILD_DATA_COLLECTION);
                serverMetrics = database.getCollection(SERVER_METRICS_COLLECTION);
                
                // Create indexes for optimal performance
                createIndexes();
                
                logger.info("MongoDB Schema initialized successfully");
                
            } catch (Exception e) {
                logger.severe("Failed to initialize MongoDB Schema: " + e.getMessage());
                throw new RuntimeException("MongoDB initialization failed", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Create player profile with embedded documents
     */
    public CompletableFuture<Void> createPlayerProfile(UUID playerUUID, String username) {
        return CompletableFuture.runAsync(() -> {
            try {
                Document playerProfile = new Document()
                    .append("_id", playerUUID.toString())
                    .append("username", username)
                    .append("lastLogin", new Date())
                    .append("createdAt", new Date())
                    
                    // Embedded AccessoriesBag
                    .append("accessoriesBag", new Document()
                        .append("unlockedSlots", 0)
                        .append("accessories", new ArrayList<>())
                        .append("talismanPowers", new HashMap<>()))
                    
                    // Embedded SkillXP Map
                    .append("skillXP", new Document()
                        .append("combat", 0)
                        .append("mining", 0)
                        .append("farming", 0)
                        .append("foraging", 0)
                        .append("fishing", 0)
                        .append("enchanting", 0)
                        .append("alchemy", 0)
                        .append("taming", 0)
                        .append("carpentry", 0)
                        .append("runecrafting", 0))
                    
                    // Embedded CollectionProgress Map
                    .append("collectionProgress", new Document()
                        .append("collections", new HashMap<>())
                        .append("milestones", new HashMap<>())
                        .append("unlockedRecipes", new ArrayList<>()))
                    
                    // Embedded RNGMeterState
                    .append("rngMeterState", new Document()
                        .append("bossProgress", new HashMap<>())
                        .append("lastReset", new Date())
                        .append("totalRngDrops", 0))
                    
                    // Player Statistics
                    .append("statistics", new Document()
                        .append("totalPlaytime", 0L)
                        .append("deaths", 0)
                        .append("kills", 0)
                        .append("blocksBroken", 0)
                        .append("itemsCrafted", 0))
                    
                    // Economy Data
                    .append("economy", new Document()
                        .append("coins", 100.0)
                        .append("bankBalance", 0.0)
                        .append("totalEarned", 100.0)
                        .append("totalSpent", 0.0))
                    
                    // Island Data
                    .append("island", new Document()
                        .append("islandId", UUID.randomUUID().toString())
                        .append("level", 1)
                        .append("size", 100)
                        .append("visitors", new ArrayList<>())
                        .append("structures", new HashMap<>()))
                    
                    // Inventory Data
                    .append("inventory", new Document()
                        .append("items", new ArrayList<>())
                        .append("enderChest", new ArrayList<>())
                        .append("armor", new HashMap<>())
                        .append("hotbar", new ArrayList<>()))
                    
                    // Pet Data
                    .append("pets", new Document()
                        .append("activePet", null)
                        .append("petInventory", new ArrayList<>())
                        .append("petStats", new HashMap<>()))
                    
                    // Minion Data
                    .append("minions", new Document()
                        .append("unlockedSlots", 1)
                        .append("placedMinions", new HashMap<>())
                        .append("minionStats", new HashMap<>()));
                
                playerProfiles.insertOne(playerProfile);
                logger.info("Created player profile for: " + username + " (" + playerUUID + ")");
                
            } catch (Exception e) {
                logger.severe("Error creating player profile: " + e.getMessage());
                throw new RuntimeException("Failed to create player profile", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Get complete player profile with all embedded data
     */
    public CompletableFuture<Optional<PlayerProfile>> getPlayerProfile(UUID playerUUID) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = playerProfiles.find(Filters.eq("_id", playerUUID.toString())).first();
                
                if (doc == null) {
                    return Optional.empty();
                }
                
                PlayerProfile profile = new PlayerProfile();
                profile.playerUUID = playerUUID;
                profile.username = doc.getString("username");
                profile.lastLogin = doc.getDate("lastLogin");
                profile.createdAt = doc.getDate("createdAt");
                
                // Parse embedded documents
                Document accessoriesBagDoc = doc.get("accessoriesBag", Document.class);
                if (accessoriesBagDoc != null) {
                    profile.accessoriesBag = parseAccessoriesBag(accessoriesBagDoc);
                }
                
                Document skillXPDoc = doc.get("skillXP", Document.class);
                if (skillXPDoc != null) {
                    profile.skillXP = parseSkillXP(skillXPDoc);
                }
                
                Document collectionProgressDoc = doc.get("collectionProgress", Document.class);
                if (collectionProgressDoc != null) {
                    profile.collectionProgress = parseCollectionProgress(collectionProgressDoc);
                }
                
                Document rngMeterDoc = doc.get("rngMeterState", Document.class);
                if (rngMeterDoc != null) {
                    profile.rngMeterState = parseRNGMeterState(rngMeterDoc);
                }
                
                return Optional.of(profile);
                
            } catch (Exception e) {
                logger.severe("Error getting player profile: " + e.getMessage());
                return Optional.empty();
            }
        }, asyncExecutor);
    }
    
    /**
     * Update player skill XP
     */
    public CompletableFuture<Void> updateSkillXP(UUID playerUUID, String skill, long xp) {
        return CompletableFuture.runAsync(() -> {
            try {
                playerProfiles.updateOne(
                    Filters.eq("_id", playerUUID.toString()),
                    Updates.set("skillXP." + skill, xp)
                );
                logger.fine("Updated skill XP for player: " + playerUUID + ", skill: " + skill + ", xp: " + xp);
                
            } catch (Exception e) {
                logger.severe("Error updating skill XP: " + e.getMessage());
                throw new RuntimeException("Failed to update skill XP", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Update collection progress
     */
    public CompletableFuture<Void> updateCollectionProgress(UUID playerUUID, String item, int amount, String origin) {
        return CompletableFuture.runAsync(() -> {
            try {
                Document collectionUpdate = new Document()
                    .append("amount", amount)
                    .append("origin", origin)
                    .append("lastUpdated", new Date());
                
                playerProfiles.updateOne(
                    Filters.eq("_id", playerUUID.toString()),
                    Updates.set("collectionProgress.collections." + item, collectionUpdate)
                );
                
                logger.fine("Updated collection progress for player: " + playerUUID + ", item: " + item + ", amount: " + amount);
                
            } catch (Exception e) {
                logger.severe("Error updating collection progress: " + e.getMessage());
                throw new RuntimeException("Failed to update collection progress", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Update RNG meter state
     */
    public CompletableFuture<Void> updateRNGMeterState(UUID playerUUID, String bossType, int progress) {
        return CompletableFuture.runAsync(() -> {
            try {
                playerProfiles.updateOne(
                    Filters.eq("_id", playerUUID.toString()),
                    Updates.set("rngMeterState.bossProgress." + bossType, progress)
                );
                
                logger.fine("Updated RNG meter state for player: " + playerUUID + ", boss: " + bossType + ", progress: " + progress);
                
            } catch (Exception e) {
                logger.severe("Error updating RNG meter state: " + e.getMessage());
                throw new RuntimeException("Failed to update RNG meter state", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Create Bazaar order
     */
    public CompletableFuture<String> createBazaarOrder(UUID playerUUID, String itemId, int amount, double price, boolean isBuyOrder) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document order = new Document()
                    .append("_id", new ObjectId())
                    .append("playerUUID", playerUUID.toString())
                    .append("itemId", itemId)
                    .append("amount", amount)
                    .append("remainingAmount", amount)
                    .append("price", price)
                    .append("isBuyOrder", isBuyOrder)
                    .append("createdAt", new Date())
                    .append("status", "active");
                
                bazaarOrders.insertOne(order);
                String orderId = order.getObjectId("_id").toString();
                
                logger.info("Created bazaar order: " + orderId + " for player: " + playerUUID);
                return orderId;
                
            } catch (Exception e) {
                logger.severe("Error creating bazaar order: " + e.getMessage());
                throw new RuntimeException("Failed to create bazaar order", e);
            }
        }, asyncExecutor);
    }
    
    /**
     * Get Bazaar orders for item (sorted by price)
     */
    public CompletableFuture<List<BazaarOrder>> getBazaarOrders(String itemId, boolean isBuyOrder, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Document> docs = bazaarOrders
                    .find(Filters.and(
                        Filters.eq("itemId", itemId),
                        Filters.eq("isBuyOrder", isBuyOrder),
                        Filters.eq("status", "active")
                    ))
                    .sort(isBuyOrder ? Filters.eq("price", -1) : Filters.eq("price", 1)) // Buy orders: highest first, Sell orders: lowest first
                    .limit(limit)
                    .into(new ArrayList<>());
                
                List<BazaarOrder> orders = new ArrayList<>();
                for (Document doc : docs) {
                    BazaarOrder order = new BazaarOrder();
                    order.orderId = doc.getObjectId("_id").toString();
                    order.playerUUID = UUID.fromString(doc.getString("playerUUID"));
                    order.itemId = doc.getString("itemId");
                    order.amount = doc.getInteger("amount");
                    order.remainingAmount = doc.getInteger("remainingAmount");
                    order.price = doc.getDouble("price");
                    order.isBuyOrder = doc.getBoolean("isBuyOrder");
                    order.createdAt = doc.getDate("createdAt");
                    order.status = doc.getString("status");
                    orders.add(order);
                }
                
                return orders;
                
            } catch (Exception e) {
                logger.severe("Error getting bazaar orders: " + e.getMessage());
                return new ArrayList<>();
            }
        }, asyncExecutor);
    }
    
    /**
     * Create indexes for optimal performance
     */
    private void createIndexes() {
        try {
            // Player profiles indexes
            playerProfiles.createIndex(Indexes.ascending("username"));
            playerProfiles.createIndex(Indexes.descending("lastLogin"));
            
            // Bazaar orders indexes
            bazaarOrders.createIndex(Indexes.compound(
                Indexes.ascending("itemId"),
                Indexes.ascending("isBuyOrder"),
                Indexes.ascending("price")
            ));
            bazaarOrders.createIndex(Indexes.ascending("playerUUID"));
            bazaarOrders.createIndex(Indexes.ascending("status"));
            
            // Auction house indexes
            auctionHouse.createIndex(Indexes.compound(
                Indexes.ascending("itemId"),
                Indexes.ascending("price")
            ));
            auctionHouse.createIndex(Indexes.ascending("sellerUUID"));
            auctionHouse.createIndex(Indexes.descending("endTime"));
            
            logger.info("MongoDB indexes created successfully");
            
        } catch (Exception e) {
            logger.warning("Error creating MongoDB indexes: " + e.getMessage());
        }
    }
    
    /**
     * Parse AccessoriesBag from Document
     */
    private AccessoriesBag parseAccessoriesBag(Document doc) {
        AccessoriesBag bag = new AccessoriesBag();
        bag.unlockedSlots = doc.getInteger("unlockedSlots", 0);
        bag.accessories = doc.getList("accessories", Document.class, new ArrayList<>());
        bag.talismanPowers = doc.get("talismanPowers", Map.class, new HashMap<>());
        return bag;
    }
    
    /**
     * Parse SkillXP from Document
     */
    private Map<String, Long> parseSkillXP(Document doc) {
        Map<String, Long> skillXP = new HashMap<>();
        for (String key : doc.keySet()) {
            Object value = doc.get(key);
            if (value instanceof Number) {
                skillXP.put(key, ((Number) value).longValue());
            }
        }
        return skillXP;
    }
    
    /**
     * Parse CollectionProgress from Document
     */
    private CollectionProgress parseCollectionProgress(Document doc) {
        CollectionProgress progress = new CollectionProgress();
        progress.collections = doc.get("collections", Map.class, new HashMap<>());
        progress.milestones = doc.get("milestones", Map.class, new HashMap<>());
        progress.unlockedRecipes = doc.getList("unlockedRecipes", String.class, new ArrayList<>());
        return progress;
    }
    
    /**
     * Parse RNGMeterState from Document
     */
    private RNGMeterState parseRNGMeterState(Document doc) {
        RNGMeterState state = new RNGMeterState();
        state.bossProgress = doc.get("bossProgress", Map.class, new HashMap<>());
        state.lastReset = doc.getDate("lastReset");
        state.totalRngDrops = doc.getInteger("totalRngDrops", 0);
        return state;
    }
    
    /**
     * Close MongoDB connection
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
        asyncExecutor.shutdown();
    }
    
    // Data structures
    public static class PlayerProfile {
        public UUID playerUUID;
        public String username;
        public Date lastLogin;
        public Date createdAt;
        public AccessoriesBag accessoriesBag;
        public Map<String, Long> skillXP;
        public CollectionProgress collectionProgress;
        public RNGMeterState rngMeterState;
    }
    
    public static class AccessoriesBag {
        public int unlockedSlots;
        public List<Document> accessories;
        public Map<String, Object> talismanPowers;
    }
    
    public static class CollectionProgress {
        public Map<String, Object> collections;
        public Map<String, Object> milestones;
        public List<String> unlockedRecipes;
    }
    
    public static class RNGMeterState {
        public Map<String, Object> bossProgress;
        public Date lastReset;
        public int totalRngDrops;
    }
    
    public static class BazaarOrder {
        public String orderId;
        public UUID playerUUID;
        public String itemId;
        public int amount;
        public int remainingAmount;
        public double price;
        public boolean isBuyOrder;
        public Date createdAt;
        public String status;
    }
}
