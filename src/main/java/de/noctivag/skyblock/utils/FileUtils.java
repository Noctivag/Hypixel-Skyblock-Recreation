package de.noctivag.skyblock.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

/**
 * Hilfsklasse für sichere Dateioperationen.
 * Bietet Funktionen für ZIP-Entpackung und Verzeichnislöschung mit Sicherheitsprüfungen.
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
     * Hilfsmethode zur Vermeidung von Zip-Slip-Sicherheitslücken.
     * @param destinationDir Das Zielverzeichnis.
     * @param zipEntry Der ZIP-Eintrag.
     * @return Die sichere Datei-Instanz.
     * @throws IOException Wenn ein Sicherheitsverstoß erkannt wird.
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
}
