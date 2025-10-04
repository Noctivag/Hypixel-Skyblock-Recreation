package de.noctivag.skyblock.database;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.concurrent.ExecutionException;
// import java.util.concurrent.InterruptedException; // Not needed in this context

/**
 * DatabaseSchemaUpdater - Updates database schema for new features
 */
public class DatabaseSchemaUpdater {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    
    public DatabaseSchemaUpdater(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    /**
     * Updates database schema for new Hypixel SkyBlock features
     */
    public void updateSchema() {
        try (Connection connection = databaseManager.getConnection().get()) {
            createBoosterCookieTable(connection);
            createRecipeBookTable(connection);
            createCalendarTable(connection);
            createEventParticipationTable(connection);
            createGuildTables(connection);
            createAuctionTables(connection);
            createBazaarTables(connection);
            
            plugin.getLogger().info("Database schema updated successfully!");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to update database schema", e);
        } catch (InterruptedException | ExecutionException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to get database connection", e);
        }
    }
    
    /**
     * Creates table for Booster Cookie data
     */
    private void createBoosterCookieTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_cookie_data (
                id INT AUTO_INCREMENT PRIMARY KEY,
                player_uuid VARCHAR(36) NOT NULL,
                cookie_active BOOLEAN DEFAULT FALSE,
                cookie_expiry BIGINT DEFAULT 0,
                last_reminder BIGINT DEFAULT 0,
                total_cookies_used INT DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY unique_player (player_uuid)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            plugin.getLogger().info("Created player_cookie_data table");
        }
    }
    
    /**
     * Creates table for Recipe Book data
     */
    private void createRecipeBookTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_recipe_data (
                id INT AUTO_INCREMENT PRIMARY KEY,
                player_uuid VARCHAR(36) NOT NULL,
                recipe_id VARCHAR(64) NOT NULL,
                discovered_at BIGINT NOT NULL,
                times_crafted INT DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY unique_player_recipe (player_uuid, recipe_id),
                INDEX idx_player_uuid (player_uuid),
                INDEX idx_recipe_id (recipe_id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            plugin.getLogger().info("Created player_recipe_data table");
        }
    }
    
    /**
     * Creates table for Calendar events
     */
    private void createCalendarTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS calendar_events (
                id INT AUTO_INCREMENT PRIMARY KEY,
                event_id VARCHAR(64) NOT NULL,
                event_name VARCHAR(128) NOT NULL,
                event_type ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'SPECIAL') NOT NULL,
                event_rarity ENUM('COMMON', 'UNCOMMON', 'RARE', 'EPIC', 'LEGENDARY') NOT NULL,
                start_time BIGINT NOT NULL,
                duration_hours INT NOT NULL,
                is_active BOOLEAN DEFAULT FALSE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY unique_event_id (event_id),
                INDEX idx_event_type (event_type),
                INDEX idx_start_time (start_time),
                INDEX idx_is_active (is_active)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            plugin.getLogger().info("Created calendar_events table");
        }
    }
    
    /**
     * Creates table for event participation tracking
     */
    private void createEventParticipationTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_event_participation (
                id INT AUTO_INCREMENT PRIMARY KEY,
                player_uuid VARCHAR(36) NOT NULL,
                event_id VARCHAR(64) NOT NULL,
                participation_time BIGINT NOT NULL,
                completion_time BIGINT DEFAULT NULL,
                rewards_claimed BOOLEAN DEFAULT FALSE,
                score INT DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY unique_player_event (player_uuid, event_id),
                INDEX idx_player_uuid (player_uuid),
                INDEX idx_event_id (event_id),
                INDEX idx_participation_time (participation_time)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            plugin.getLogger().info("Created player_event_participation table");
        }
    }
    
    /**
     * Creates tables for Guild system
     */
    private void createGuildTables(Connection connection) throws SQLException {
        String guildsSql = """
            CREATE TABLE IF NOT EXISTS guilds (
                id VARCHAR(64) PRIMARY KEY,
                name VARCHAR(32) NOT NULL,
                tag VARCHAR(8) NOT NULL,
                description TEXT,
                leader_uuid VARCHAR(36) NOT NULL,
                level INT DEFAULT 1,
                experience BIGINT DEFAULT 0,
                coins DECIMAL(15,2) DEFAULT 0.00,
                member_count INT DEFAULT 1,
                max_members INT DEFAULT 10,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY unique_name (name),
                UNIQUE KEY unique_tag (tag),
                INDEX idx_leader_uuid (leader_uuid),
                INDEX idx_level (level)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        String guildMembersSql = """
            CREATE TABLE IF NOT EXISTS guild_members (
                id INT AUTO_INCREMENT PRIMARY KEY,
                guild_id VARCHAR(64) NOT NULL,
                player_uuid VARCHAR(36) NOT NULL,
                role ENUM('MEMBER', 'OFFICER', 'LEADER') DEFAULT 'MEMBER',
                joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                contribution_points INT DEFAULT 0,
                last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY unique_guild_player (guild_id, player_uuid),
                INDEX idx_guild_id (guild_id),
                INDEX idx_player_uuid (player_uuid),
                FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(guildsSql);
            stmt.executeUpdate(guildMembersSql);
            plugin.getLogger().info("Created guild tables");
        }
    }
    
    /**
     * Creates tables for Auction House system
     */
    private void createAuctionTables(Connection connection) throws SQLException {
        String auctionsSql = """
            CREATE TABLE IF NOT EXISTS auctions (
                id VARCHAR(64) PRIMARY KEY,
                seller_uuid VARCHAR(36) NOT NULL,
                item_data LONGTEXT NOT NULL,
                starting_bid DECIMAL(15,2) NOT NULL,
                current_bid DECIMAL(15,2) DEFAULT 0.00,
                buy_it_now DECIMAL(15,2) DEFAULT NULL,
                highest_bidder_uuid VARCHAR(36) DEFAULT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                ends_at TIMESTAMP NOT NULL,
                status ENUM('ACTIVE', 'ENDED', 'CANCELLED') DEFAULT 'ACTIVE',
                bid_count INT DEFAULT 0,
                created_at_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                INDEX idx_seller_uuid (seller_uuid),
                INDEX idx_highest_bidder_uuid (highest_bidder_uuid),
                INDEX idx_ends_at (ends_at),
                INDEX idx_status (status)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        String auctionBidsSql = """
            CREATE TABLE IF NOT EXISTS auction_bids (
                id INT AUTO_INCREMENT PRIMARY KEY,
                auction_id VARCHAR(64) NOT NULL,
                bidder_uuid VARCHAR(36) NOT NULL,
                bid_amount DECIMAL(15,2) NOT NULL,
                bid_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                is_winning_bid BOOLEAN DEFAULT FALSE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_auction_id (auction_id),
                INDEX idx_bidder_uuid (bidder_uuid),
                INDEX idx_bid_time (bid_time),
                FOREIGN KEY (auction_id) REFERENCES auctions(id) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(auctionsSql);
            stmt.executeUpdate(auctionBidsSql);
            plugin.getLogger().info("Created auction tables");
        }
    }
    
    /**
     * Creates tables for Bazaar system
     */
    private void createBazaarTables(Connection connection) throws SQLException {
        String bazaarOrdersSql = """
            CREATE TABLE IF NOT EXISTS bazaar_orders (
                id VARCHAR(64) PRIMARY KEY,
                player_uuid VARCHAR(36) NOT NULL,
                item_id VARCHAR(64) NOT NULL,
                order_type ENUM('BUY', 'SELL') NOT NULL,
                amount BIGINT NOT NULL,
                price_per_unit DECIMAL(10,4) NOT NULL,
                filled_amount BIGINT DEFAULT 0,
                remaining_amount BIGINT NOT NULL,
                status ENUM('ACTIVE', 'FILLED', 'CANCELLED') DEFAULT 'ACTIVE',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                INDEX idx_player_uuid (player_uuid),
                INDEX idx_item_id (item_id),
                INDEX idx_order_type (order_type),
                INDEX idx_status (status),
                INDEX idx_price_per_unit (price_per_unit)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        String bazaarHistorySql = """
            CREATE TABLE IF NOT EXISTS bazaar_history (
                id INT AUTO_INCREMENT PRIMARY KEY,
                item_id VARCHAR(64) NOT NULL,
                buy_price DECIMAL(10,4) NOT NULL,
                sell_price DECIMAL(10,4) NOT NULL,
                volume BIGINT NOT NULL,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_item_id (item_id),
                INDEX idx_timestamp (timestamp)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(bazaarOrdersSql);
            stmt.executeUpdate(bazaarHistorySql);
            plugin.getLogger().info("Created bazaar tables");
        }
    }
}
