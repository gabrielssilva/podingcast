package gabrielssilva.podingcast.web;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import gabrielssilva.podingcast.app.interfaces.DownloadListener;

public class DownloadNotifier extends BroadcastReceiver {

    private DownloadListener listener;

    public DownloadNotifier(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        boolean isMyDownload = action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        if ( isMyDownload && this.isMyDownloadComplete()) {
            // The download has finished, do something with view...
        }
    }

    public boolean isMyDownloadComplete() {
        boolean isDownloadComplete = false;
        Query actionQuery = new Query();
        actionQuery.setFilterById(this.listener.getDownloadID());

        Cursor cursor = this.listener.getDownloadManager().query(actionQuery);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                isDownloadComplete = true;
            } else {
                isDownloadComplete = false;
            }
        }

        return isDownloadComplete;
    }
}
