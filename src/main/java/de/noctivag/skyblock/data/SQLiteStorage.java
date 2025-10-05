package de.noctivag.skyblock.data;

import de.noctivag.skyblock.SkyblockPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * SQLite storage implementation
 */
public class SQLiteStorage {
    
    private final SkyblockPlugin plugin;
    private Connection connection;
    
    public SQLiteStorage(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Initialize the SQLite connection
     */
    public void initialize() {
        try {
            String url = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/skyblock.db";
            connection = DriverManager.getConnection(url);
            plugin.getLogger().info("SQLite connection established!");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to SQLite database", e);
        }
    }
    
    /**
     * Get the database connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Close the database connection
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("SQLite connection closed!");
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to close SQLite connection", e);
            }
        }
    }
}
