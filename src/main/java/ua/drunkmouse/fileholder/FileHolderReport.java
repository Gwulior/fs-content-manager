package ua.drunkmouse.fileholder;

/**
 * Created by Dreval Viacheslav on 12.03.2017.
 */
public class FileHolderReport {

    private String originalPath;

    public FileHolderReport(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }
}
