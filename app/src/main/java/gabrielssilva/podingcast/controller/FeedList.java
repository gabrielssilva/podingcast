package gabrielssilva.podingcast.controller;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedList {

    private File podingcastFolder;
    private final String PODINGCAST_FOLDER = "Podingcast";

    public FeedList() {
        File storageFolder = Environment.getExternalStorageDirectory();
        this.podingcastFolder = new File(storageFolder, this.PODINGCAST_FOLDER);

        this.checkDir();
    }

    private void checkDir() {
        if (this.podingcastFolder.exists() == false) {
            this.podingcastFolder.mkdir();
        }
    }

    public List<File> getAllFiles() {
        List<File> allFiles;
        allFiles = Arrays.asList(this.podingcastFolder.listFiles());

        return allFiles;
    }

    public File getFile(int index) {
        List<File> allFiles = getAllFiles();

        return allFiles.get(index);
    }
}
