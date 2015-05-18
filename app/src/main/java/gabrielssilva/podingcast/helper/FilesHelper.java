package gabrielssilva.podingcast.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.util.LongSparseArray;

import java.io.File;
import java.util.Map;

public class FilesHelper {

    public final static String PODINGCAST_FOLDER = "Podingcast";
    public final static String DOWNLOADS_SHARED_PREF = "PODINGCAST_DOWNLOADS_SHARED_PREF";

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

    public static boolean validFile(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void saveDownloadReference(Context context, long downloadID, String url) {
        SharedPreferences sharedPref = context.getSharedPreferences(DOWNLOADS_SHARED_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(""+downloadID, url);
        editor.commit();
    }

    public static void removeDownloadReference(Context context, long downloadID) {
        SharedPreferences sharedPref = context.getSharedPreferences(DOWNLOADS_SHARED_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(""+downloadID);
        editor.apply();
    }

    public static LongSparseArray<String> getDownloadReferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(DOWNLOADS_SHARED_PREF,
                Context.MODE_PRIVATE);

        LongSparseArray<String> references = new LongSparseArray<>();
        Map<String, ?> map = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            references.append(Long.valueOf(entry.getKey()), (String) entry.getValue());
        }
        return references;
    }
}