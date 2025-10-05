package de.noctivag.skyblock.utils;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility class for file operations with enhanced security and Rolling-Restart support
 */
public class FileUtils {
    
    /**
     * Entpackt einen ZIP-InputStream in ein Zielverzeichnis.
     * @param zipStream Der ZIP-InputStream aus den Plugin-Ressourcen.
     * @param destDir Das Zielverzeichnis.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    public static void unzip(InputStream zipStream, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try (ZipInputStream zis = new ZipInputStream(zipStream)) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }
    
    /**
     * Löscht ein Verzeichnis und all seine Inhalte rekursiv.
     * @param directory Das zu löschende Verzeichnis.
     * @return true, wenn erfolgreich, sonst false.
     */
    public static boolean deleteDirectory(File directory) {
        try {
            if (directory.exists()) {
                Files.walk(directory.toPath())
                     .sorted(Comparator.reverseOrder())
                     .map(Path::toFile)
                     .forEach(File::delete);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Hilfsmethode zur Vermeidung von Zip-Slip-Sicherheitslücken
     */
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }
    
    /**
     * Kopiert eine Datei
     */
    public static void copyFile(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    /**
     * Kopiert ein Verzeichnis rekursiv
     */
    public static void copyDirectory(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            
            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(source, file);
                    File destFile = new File(destination, file);
                    copyDirectory(srcFile, destFile);
                }
            }
        } else {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    /**
     * Erstellt ein Verzeichnis falls es nicht existiert
     */
    public static boolean createDirectoryIfNotExists(File directory) {
        if (directory != null && !directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }
    
    /**
     * Prüft ob eine Datei existiert
     */
    public static boolean fileExists(String path) {
        return new File(path).exists();
    }
    
    /**
     * Liest den Inhalt einer Datei als String
     */
    public static String readFileAsString(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }
    
    /**
     * Schreibt einen String in eine Datei
     */
    public static void writeStringToFile(File file, String content) throws IOException {
        createDirectoryIfNotExists(file.getParentFile());
        Files.write(file.toPath(), content.getBytes());
    }
    
    /**
     * Prüft ob ein Verzeichnis leer ist
     */
    public static boolean isDirectoryEmpty(File directory) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return true;
        }
        String[] files = directory.list();
        return files == null || files.length == 0;
    }
    
    /**
     * Erstellt eine Sicherungskopie eines Verzeichnisses
     */
    public static boolean backupDirectory(File source, File backup) throws IOException {
        if (!source.exists() || !source.isDirectory()) {
            return false;
        }
        
        if (backup.exists()) {
            deleteDirectory(backup);
        }
        
        copyDirectory(source, backup);
        return true;
    }
    
    /**
     * Stellt eine Sicherungskopie wieder her
     */
    public static boolean restoreDirectory(File backup, File destination) throws IOException {
        if (!backup.exists() || !backup.isDirectory()) {
            return false;
        }
        
        if (destination.exists()) {
            deleteDirectory(destination);
        }
        
        copyDirectory(backup, destination);
        return true;
    }
}