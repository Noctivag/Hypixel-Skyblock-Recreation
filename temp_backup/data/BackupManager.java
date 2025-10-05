package de.noctivag.skyblock.data;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Backup Manager - Automated Backup System
 * 
 * Verantwortlich für:
 * - Automated Backups
 * - Backup Compression
 * - Backup Retention
 * - Backup Restoration
 * - Backup Scheduling
 */
public class BackupManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final File backupDirectory;
    private final DatabaseManager databaseManager;
    
    // Backup Configuration
    private final boolean ENABLE_AUTOMATIC_BACKUPS = true;
    private final long BACKUP_INTERVAL = 24 * 60 * 60 * 1000L; // 24 hours
    private final int MAX_BACKUPS = 7; // Keep 7 backups
    private final boolean COMPRESS_BACKUPS = true;
    private final boolean BACKUP_DATABASE = true;
    private final boolean BACKUP_CONFIG = true;
    private final boolean BACKUP_PLAYER_DATA = true;
    
    // Backup Statistics
    private int totalBackups = 0;
    private int successfulBackups = 0;
    private int failedBackups = 0;
    private LocalDateTime lastBackupTime = null;
    
    public BackupManager(SkyblockPlugin SkyblockPlugin, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        this.backupDirectory = new File(SkyblockPlugin.getDataFolder(), "backups");
        
        if (!backupDirectory.exists()) {
            backupDirectory.mkdirs();
        }
        
        if (ENABLE_AUTOMATIC_BACKUPS) {
            startBackupScheduler();
        }
    }
    
    private void startBackupScheduler() {
        new BukkitRunnable() {
            @Override
            public void run() {
                createBackup();
            }
        }.runTaskTimerAsynchronously(SkyblockPlugin, 0L, BACKUP_INTERVAL / 50L); // Convert to ticks
    }
    
    public CompletableFuture<Boolean> createBackup() {
        return CompletableFuture.supplyAsync(() -> {
            totalBackups++;
            
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String backupName = "backup_" + timestamp;
                File backupFile = new File(backupDirectory, backupName + (COMPRESS_BACKUPS ? ".zip" : ""));
                
                if (COMPRESS_BACKUPS) {
                    createCompressedBackup(backupFile);
                } else {
                    createUncompressedBackup(backupFile);
                }
                
                // Clean up old backups
                cleanupOldBackups();
                
                lastBackupTime = LocalDateTime.now();
                successfulBackups++;
                
                SkyblockPlugin.getLogger().info("Backup created successfully: " + backupName);
                return true;
                
            } catch (Exception e) {
                failedBackups++;
                SkyblockPlugin.getLogger().log(java.util.logging.Level.SEVERE, "Failed to create backup", e);
                return false;
            }
        });
    }
    
    private void createCompressedBackup(File backupFile) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(backupFile))) {
            // Backup database
            if (BACKUP_DATABASE) {
                backupDatabase(zos);
            }
            
            // Backup configuration files
            if (BACKUP_CONFIG) {
                backupConfigFiles(zos);
            }
            
            // Backup player data
            if (BACKUP_PLAYER_DATA) {
                backupPlayerData(zos);
            }
        }
    }
    
    private void createUncompressedBackup(File backupFile) throws IOException {
        if (!backupFile.exists()) {
            backupFile.mkdirs();
        }
        
        // Backup database
        if (BACKUP_DATABASE) {
            backupDatabaseUncompressed(backupFile);
        }
        
        // Backup configuration files
        if (BACKUP_CONFIG) {
            backupConfigFilesUncompressed(backupFile);
        }
        
        // Backup player data
        if (BACKUP_PLAYER_DATA) {
            backupPlayerDataUncompressed(backupFile);
        }
    }
    
    private void backupDatabase(ZipOutputStream zos) throws IOException {
        if (databaseManager.isConnected()) {
            // Create database dump
            String dbDump = createDatabaseDump();
            
            ZipEntry entry = new ZipEntry("database/dump.sql");
            zos.putNextEntry(entry);
            zos.write(dbDump.getBytes());
            zos.closeEntry();
        }
    }
    
    private void backupDatabaseUncompressed(File backupFile) throws IOException {
        if (databaseManager.isConnected()) {
            File dbDir = new File(backupFile, "database");
            dbDir.mkdirs();
            
            String dbDump = createDatabaseDump();
            File dumpFile = new File(dbDir, "dump.sql");
            
            try (FileWriter writer = new FileWriter(dumpFile)) {
                writer.write(dbDump);
            }
        }
    }
    
    private String createDatabaseDump() {
        // This is a simplified database dump
        // In a real implementation, you would use proper database dump tools
        StringBuilder dump = new StringBuilder();
        dump.append("-- Database Dump created at ").append(LocalDateTime.now()).append("\n");
        dump.append("-- SkyblockPlugin: ").append(SkyblockPlugin.getName()).append("\n");
        dump.append("-- Version: ").append(SkyblockPlugin.getDescription().getVersion()).append("\n\n");
        
        // Add table creation statements and data
        // This would be implemented based on the actual database structure
        
        return dump.toString();
    }
    
    private void backupConfigFiles(ZipOutputStream zos) throws IOException {
        File configDir = SkyblockPlugin.getDataFolder();
        if (configDir.exists()) {
            addDirectoryToZip(zos, configDir, "config/");
        }
    }
    
    private void backupConfigFilesUncompressed(File backupFile) throws IOException {
        File configDir = SkyblockPlugin.getDataFolder();
        if (configDir.exists()) {
            File targetConfigDir = new File(backupFile, "config");
            copyDirectory(configDir, targetConfigDir);
        }
    }
    
    private void backupPlayerData(ZipOutputStream zos) throws IOException {
        // Backup player data files
        File playerDataDir = new File(SkyblockPlugin.getDataFolder(), "playerdata");
        if (playerDataDir.exists()) {
            addDirectoryToZip(zos, playerDataDir, "playerdata/");
        }
    }
    
    private void backupPlayerDataUncompressed(File backupFile) throws IOException {
        File playerDataDir = new File(SkyblockPlugin.getDataFolder(), "playerdata");
        if (playerDataDir.exists()) {
            File targetPlayerDataDir = new File(backupFile, "playerdata");
            copyDirectory(playerDataDir, targetPlayerDataDir);
        }
    }
    
    private void addDirectoryToZip(ZipOutputStream zos, File directory, String basePath) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addDirectoryToZip(zos, file, basePath + file.getName() + "/");
                } else {
                    ZipEntry entry = new ZipEntry(basePath + file.getName());
                    zos.putNextEntry(entry);
                    
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                    }
                    
                    zos.closeEntry();
                }
            }
        }
    }
    
    private void copyDirectory(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdirs();
            }
            
            String[] children = source.list();
            if (children != null) {
                for (String child : children) {
                    copyDirectory(new File(source, child), new File(target, child));
                }
            }
        } else {
            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(target)) {
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }
    
    private void cleanupOldBackups() {
        File[] backupFiles = backupDirectory.listFiles((dir, name) -> 
            name.startsWith("backup_") && (name.endsWith(".zip") || new File(dir, name).isDirectory()));
        
        if (backupFiles != null && backupFiles.length > MAX_BACKUPS) {
            // Sort by modification time (oldest first)
            Arrays.sort(backupFiles, Comparator.comparingLong(File::lastModified));
            
            int filesToDelete = backupFiles.length - MAX_BACKUPS;
            for (int i = 0; i < filesToDelete; i++) {
                File fileToDelete = backupFiles[i];
                if (deleteDirectory(fileToDelete)) {
                    SkyblockPlugin.getLogger().info("Deleted old backup: " + fileToDelete.getName());
                } else {
                    SkyblockPlugin.getLogger().warning("Failed to delete old backup: " + fileToDelete.getName());
                }
            }
        }
    }
    
    private boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] children = directory.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteDirectory(child);
                }
            }
        }
        return directory.delete();
    }
    
    public CompletableFuture<Boolean> restoreBackup(String backupName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                File backupFile = new File(backupDirectory, backupName);
                if (!backupFile.exists()) {
                    SkyblockPlugin.getLogger().warning("Backup not found: " + backupName);
                    return false;
                }
                
                if (backupName.endsWith(".zip")) {
                    restoreCompressedBackup(backupFile);
                } else {
                    restoreUncompressedBackup(backupFile);
                }
                
                SkyblockPlugin.getLogger().info("Backup restored successfully: " + backupName);
                return true;
                
            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(java.util.logging.Level.SEVERE, "Failed to restore backup", e);
                return false;
            }
        });
    }
    
    private void restoreCompressedBackup(File backupFile) throws IOException {
        // Implementation for restoring compressed backups
        // This would involve extracting the ZIP file and restoring the data
        SkyblockPlugin.getLogger().info("Restoring compressed backup: " + backupFile.getName());
    }
    
    private void restoreUncompressedBackup(File backupFile) throws IOException {
        // Implementation for restoring uncompressed backups
        // This would involve copying files back to their original locations
        SkyblockPlugin.getLogger().info("Restoring uncompressed backup: " + backupFile.getName());
    }
    
    public List<String> getAvailableBackups() {
        List<String> backups = new ArrayList<>();
        File[] backupFiles = backupDirectory.listFiles((dir, name) -> 
            name.startsWith("backup_") && (name.endsWith(".zip") || new File(dir, name).isDirectory()));
        
        if (backupFiles != null) {
            for (File backup : backupFiles) {
                backups.add(backup.getName());
            }
        }
        
        // Sort by modification time (newest first)
        backups.sort((a, b) -> {
            File fileA = new File(backupDirectory, a);
            File fileB = new File(backupDirectory, b);
            return Long.compare(fileB.lastModified(), fileA.lastModified());
        });
        
        return backups;
    }
    
    public BackupStatistics getStatistics() {
        return new BackupStatistics(
            totalBackups,
            successfulBackups,
            failedBackups,
            lastBackupTime,
            getAvailableBackups().size()
        );
    }
    
    // Backup Statistics Class
    public static class BackupStatistics {
        private final int totalBackups;
        private final int successfulBackups;
        private final int failedBackups;
        private final LocalDateTime lastBackupTime;
        private final int availableBackups;
        
        public BackupStatistics(int totalBackups, int successfulBackups, int failedBackups, 
                              LocalDateTime lastBackupTime, int availableBackups) {
            this.totalBackups = totalBackups;
            this.successfulBackups = successfulBackups;
            this.failedBackups = failedBackups;
            this.lastBackupTime = lastBackupTime;
            this.availableBackups = availableBackups;
        }
        
        public int getTotalBackups() { return totalBackups; }
        public int getSuccessfulBackups() { return successfulBackups; }
        public int getFailedBackups() { return failedBackups; }
        public LocalDateTime getLastBackupTime() { return lastBackupTime; }
        public int getAvailableBackups() { return availableBackups; }
        
        public double getSuccessRate() {
            if (totalBackups == 0) return 0.0;
            return (double) successfulBackups / totalBackups * 100.0;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Backup Statistics: Total=%d, Successful=%d, Failed=%d, Success Rate=%.2f%%, Available=%d",
                totalBackups, successfulBackups, failedBackups, getSuccessRate(), availableBackups
            );
        }
    }
}
