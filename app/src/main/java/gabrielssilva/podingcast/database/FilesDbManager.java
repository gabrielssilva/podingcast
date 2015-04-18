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
        FilesDbHelper dbHelper = new FilesDbHelper(context);
        List<File> allFiles = FilesHelper.getAllFiles();

        for (File file : allFiles) {
            /* Should I set the file source once here, or on each method call as now?
               I probably can gain some performance, but I can also forget to change the source.
               For now, I will keep like this. */
            Mp3Helper mp3Helper = new Mp3Helper(this.context, file);
            String feedName = mp3Helper.getFeedName();
            String fileTitle = mp3Helper.getFileTitle();

            dbHelper.insertEpisode(feedName, fileTitle, file.getPath());
        }
    }
}
