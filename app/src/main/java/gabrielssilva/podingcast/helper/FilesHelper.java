package gabrielssilva.podingcast.helper;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FilesHelper {

    public final static String PODINGCAST_FOLDER = "Podingcast";

    public static File getAppFolder() {
        File storageFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
        File podingcastFolder = new File(storageFolder, PODINGCAST_FOLDER);

        boolean created = podingcastFolder.mkdir();
        if (!created && !podingcastFolder.exists()) {
            Log.e("EpisodesController", "Unable to create the App folder");
        }

        return podingcastFolder;
    }
}