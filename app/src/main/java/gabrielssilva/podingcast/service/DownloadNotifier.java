package gabrielssilva.podingcast.service;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import gabrielssilva.podingcast.database.FilesDbHelper;
import gabrielssilva.podingcast.helper.Mp3Helper;
import gabrielssilva.podingcast.model.Episode;

public class DownloadNotifier extends BroadcastReceiver {

    public final static String ACTION_DOWNLOAD_OK = "gabrielssilva.podingcast.download_ok";
    public final static String ACTION_DOWNLOAD_FAIL = "gabrielssilva.podingcast.download_fail";

    private long downloadID;

    public DownloadNotifier(long downloadID) {
        Log.i("DownloadNotifier", "Notifier created...");
        this.downloadID = downloadID;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("DownloadNotifier", "broadcast received");

        long downloadedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(
                Context.DOWNLOAD_SERVICE);

        if (downloadedId == this.downloadID) {
            Query query = new Query();
            query.setFilterById(this.downloadID);

            Cursor cursor = downloadManager.query(query);

            if (cursor.moveToFirst()) {
                int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                    this.saveEpisode(context, cursor);
                    this.onDownloadSuccess(context);
                } else if (DownloadManager.STATUS_FAILED == cursor.getInt(statusIndex)) {
                    this.onDownloadError(context);
                }
            }
        } else {
            Log.i("DownloadNotifier", "Captured wrong download. Registering again...");
        }
    }

    private void onDownloadSuccess(Context context) {
        this.sendBroadcast(context, ACTION_DOWNLOAD_OK);
    }

    private void onDownloadError(Context context) {
        this.sendBroadcast(context, ACTION_DOWNLOAD_FAIL);
    }

    private void sendBroadcast(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    private void saveEpisode(Context context, Cursor cursor) {
        int descrIndex = cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION);
        int pathIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_URI);

        String filePath = cursor.getString(pathIndex);
        String uri = cursor.getString(uriIndex);
        Mp3Helper mp3Helper = new Mp3Helper(filePath);
        String fileName = mp3Helper.getFileTitle();

        Episode episode = new Episode(fileName, filePath, uri, 0);
        FilesDbHelper filesDbHelper = new FilesDbHelper(context);

        filesDbHelper.insertEpisode(cursor.getString(descrIndex), episode);
    }
}
