package ua.drunkmouse.fileholder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Dreval Viacheslav on 05.03.2017.
 */
public class FileHolder {

    private Integer maxFilesInFolder = null;
    private Long currentFolder = null;
    private Path pathPrefix = null;
    private static final String slash = "/";


    public FileHolder(String pathPrefix, Integer maxFilesInFolder) {
        if (pathPrefix == null || pathPrefix.isEmpty()) {
            throw new IllegalStateException("PathPrefix must contain valid path to folder for saving files");
        }
        if (maxFilesInFolder == null) {
            throw new IllegalStateException("maxFilesInFolder must be valid Integer number to control max count files in each folder");
        }
        normalizePath(pathPrefix);
        this.pathPrefix = Paths.get(pathPrefix);
        this.maxFilesInFolder = maxFilesInFolder;
    }

    public byte[] loadFile(String relativePath) throws IOException {
        Path resolve = pathPrefix.resolve(relativePath);
        return Files.readAllBytes(resolve);
    }

    public FileHolderReport saveFile(byte[] file) throws IOException {
        return saveFile(file, null);
    }

    public FileHolderReport saveFile(byte[] file, String originalFilename) throws IOException {
        UUID uuid = UUID.randomUUID();
        Path folderPath = getFolder();

        Path writtenFilePath = Files.write(folderPath.resolve(resolveFileName(uuid.toString(), originalFilename)), file);
        Path path = pathPrefix.relativize(writtenFilePath);
        return new FileHolderReport(path.toString());
    }

    private Path getFolder() throws IOException {

        Path result = null;

        Optional<Path> max = Files.list(pathPrefix)
                .filter(Files::isDirectory)
                .max(this::pathCreationTimeCompare);

        if (max.isPresent()) {
            Path folder = max.get();
            if (!isMaxFilesReached(folder)) {
                result = folder;
            }
        } else {
            result = createNewDirectory();
        }

        return result;
    }

    private boolean isMaxFilesReached(Path folderPath) throws IOException {
        return Files.list(folderPath).count() > maxFilesInFolder;
    }

    private Path createNewDirectory() throws IOException {
        return Files.createDirectory(pathPrefix.resolve(UUID.randomUUID().toString()));
    }

    private int pathCreationTimeCompare(Path p1, Path p2) {
        try {
            BasicFileAttributes time1 = Files.readAttributes(p1, BasicFileAttributes.class);
            BasicFileAttributes time2 = Files.readAttributes(p2, BasicFileAttributes.class);

            return time1.creationTime().compareTo(time2.creationTime());
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String normalizePath(String pathPrefix) {
        if (!(pathPrefix.endsWith("\\") || pathPrefix.endsWith("/"))) {
            return pathPrefix + slash;
        }
        return pathPrefix;
    }

    private String resolveFileName(String generatedFilename, String originalFilename) {

        String result;
        String[] slitted;

        if (originalFilename != null && !originalFilename.isEmpty() && (slitted = originalFilename.split("\\.")).length > 1) {
            result = generatedFilename + "." + slitted[slitted.length - 1];
        } else {
            result = generatedFilename;
        }

        return result;
    }
}

