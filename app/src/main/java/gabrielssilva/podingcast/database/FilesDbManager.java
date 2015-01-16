package gabrielssilva.podingcast.database;


import android.content.Context;

import java.io.File;
import java.util.List;

import gabrielssilva.podingcast.helper.FilesHelper;
import gabrielssilva.podingcast.helper.Mp3Helper;

public class FilesDbManager {

    private Context context;

    public FilesDbManager(Context context) {
        this.context = context;
    }

    public void refreshDatabase() {
        this.context.deleteDatabase(FilesDbHelper.DATABASE_NAME);

        FilesDbHelper dbHelper = new FilesDbHelper(context);
        List<File> allFiles = FilesHelper.getAllFiles();

        for (File file : allFiles) {
            /* Should I set the file source once here, or on each method call as now?
               I probably can gain some performance, but I can also forget to change the source.
               For now, I will keep like this. */
            String feedName = Mp3Helper.getFeedName(context, file);
            String fileTitle = Mp3Helper.getFileTitle(context, file);

            dbHelper.insertPodcast(feedName, fileTitle, file.getPath());
        }
    }
}
