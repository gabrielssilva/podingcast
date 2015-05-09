package gabrielssilva.podingcast.web;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
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
                int pathIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                int nameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE);

                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                    String filePath = cursor.getString(pathIndex);
                    String fileName = cursor.getString(nameIndex);

                    this.callbackListener.onSuccess(new Episode(fileName, filePath, 0));
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
