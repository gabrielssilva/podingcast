package gabrielssilva.podingcast.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class DownloadNotifyService extends Service {

    public final static String DOWNLOAD_ID = "download_id";

    private DownloadNotifier downloadNotifier;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("DownloadNotifyService", "Service starting...");
        long downloadID = intent.getLongExtra(DOWNLOAD_ID, 0);

        this.downloadNotifier = new DownloadNotifier(downloadID);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadNotifier, intentFilter);

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(this.downloadNotifier);
    }
}