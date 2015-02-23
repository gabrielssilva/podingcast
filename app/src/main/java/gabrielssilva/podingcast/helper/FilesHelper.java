package gabrielssilva.podingcast.helper;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public class FilesHelper {

    private static File podingcastFolder;

    private FilesHelper() { }

    private static void initFolder() {
        final String PODINGCAST_FOLDER = "Podingcast";

        if (podingcastFolder == null) {
            File storageFolder = Environment.getExternalStorageDirectory();
            podingcastFolder = new File(storageFolder, PODINGCAST_FOLDER);
        }

        if (!podingcastFolder.exists()) {
            podingcastFolder.mkdir();
        }
    }

    public static List<File> getAllFiles() {
        List<File> allFiles;
        File[] filteredFiles;

        initFolder();

        // Using our Filter, declared above.
        FilterByExtension filter = new FilterByExtension();
        filteredFiles = podingcastFolder.listFiles(filter);
        allFiles = Arrays.asList(filteredFiles);

        return allFiles;
    }

    /* A private class to filter our files.
     * This class is pretty small, so declare it here should make no harm.
     */
    private static class FilterByExtension implements FilenameFilter {

        private final String ACCEPTED_EXTENSION = ".mp3";

        @Override
        public boolean accept(File file, String name) {
            return name.toLowerCase().endsWith(this.ACCEPTED_EXTENSION);
        }
    }

}
