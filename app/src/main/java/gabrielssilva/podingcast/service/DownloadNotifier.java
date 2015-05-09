package gabrielssilva.podingcast.service;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.helper.Mp3Helper;
import gabrielssilva.podingcast.model.Episode;

public class DownloadNotifier extends BroadcastReceiver {

    private CallbackListener callbackListener;
    private DownloadManager downloadManager;
    private long downloadID;

    public DownloadNotifier(CallbackListener callbackListener, DownloadManager downloadManager,
                            long downloadID) {
        this.callbackListener = callbackListener;
        this.downloadManager = downloadManager;
        this.downloadID = downloadID;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

        if (downloadedId == this.downloadID) {
            Query query = new Query();
            query.setFilterById(this.downloadID);

            Cursor cursor = this.downloadManager.query(query);

            if (cursor.moveToFirst()) {
                int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int pathIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_URI);

                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                    String filePath = cursor.getString(pathIndex);
                    String uri = cursor.getString(uriIndex);
                    Mp3Helper mp3Helper = new Mp3Helper(filePath);
                    String fileName = mp3Helper.getFileTitle();

                    this.callbackListener.onSuccess(new Episode(fileName, filePath, uri, 0));
                } else if (DownloadManager.STATUS_FAILED == cursor.getInt(statusIndex)) {
                    this.callbackListener.onFailure();
                }
            }
        } else {
            Log.i("DownloadNotifier", "Captured wrong download. Returning...");
            this.callbackListener.onSuccess(null);
        }
    }
}
