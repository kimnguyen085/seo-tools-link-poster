package main.java.com.seo.auto.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

public class CustomFileUtils {

    public static Optional<File> getLatestDownloadedFileFromDirectory(final String directory){
        Path dir = Paths.get(StringUtils.isNotBlank(directory) ? directory : Constants.DOWNLOAD_DEFAULT_DIRECTORY);
        try{
            Optional<Path> savedFile = Files.list(dir).filter(file -> !Files.isDirectory(file)).max(Comparator.comparingLong(file -> file.toFile().lastModified()));
            return savedFile.isPresent() ? Optional.ofNullable(savedFile.get().toFile()) : Optional.empty();
        } catch (Exception exp){
            exp.printStackTrace();
        }
        return Optional.empty();
    }
    public static File getLatestDownloadedFileNew(String atmpDownloadDefaultDirectory){
        File directory = new File(atmpDownloadDefaultDirectory);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null)
        {
            for (File file : files)
            {
                if (file.lastModified() > lastModifiedTime)
                {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        return chosenFile;
    }

    public static Optional<File> getLatestDownloadedFile(){
       return getLatestDownloadedFileFromDirectory(null);
    }

    public static void createDirectory(String directory){
        try {

            Path path = Paths.get(directory);

            //java.nio.file.Files;
            Files.createDirectories(path);

            System.out.println("Directory is created!");

        } catch (IOException e) {

            System.err.println("Failed to create directory!" + e.getMessage());

        }
    }
    public static boolean moveFileToDir(final File fileToMove, final String newLocation){
        File renamedZipFile = new File(newLocation);
        return fileToMove.renameTo(renamedZipFile);
    }
    public static boolean moveFileToDirNew(final File fileToMove, final String newLocation,String newLocationDirectory){
        createDirectory(newLocationDirectory);
        File renamedZipFile = new File(newLocation);
        return fileToMove.renameTo(renamedZipFile);
    }
    public static void main(String[] args){
    }
}
