package gabrielssilva.podingcast.controller;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Filter;

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
        File[] filteredFiles;

        // Using our Filter, declared above.
        FilterByExtension filter = new FilterByExtension();
        filteredFiles = this.podingcastFolder.listFiles(filter);
        allFiles = Arrays.asList(filteredFiles);

        return allFiles;
    }

    public File getFile(int index) {
        List<File> allFiles = getAllFiles();

        return allFiles.get(index);
    }

    /* A private class to filter our files.
     * This class is pretty small, so declare it here should not make harm.
     */
    private class FilterByExtension implements FilenameFilter {

        private final String ACCEPTED_EXTENSION = ".mp3";

        @Override
        public boolean accept(File file, String name) {
            return name.toLowerCase().endsWith(this.ACCEPTED_EXTENSION);
        }
    }
}
