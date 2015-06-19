package gabrielssilva.podingcast.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.util.LongSparseArray;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.helper.FilesHelper;

public class DownloadNotifyService extends Service implements CallbackListener {

    public final static String DOWNLOAD_ID = "download_id";
    public final static String EPISODE_DESCRIPTION = "episode_description";
    public final static String EPISODE_CONTENT = "episode_content";

    private LongSparseArray<DownloadNotifier> notifiers;
    private BroadcastNotifier mainBroadcastNotifier;

    @Override
    public void onCreate() {
        this.notifiers = new LongSparseArray<>();

        this.mainBroadcastNotifier = new BroadcastNotifier(this,
                DownloadNotifier.ACTION_DOWNLOAD_OK);
        IntentFilter intentFilter = new IntentFilter(DownloadNotifier.ACTION_DOWNLOAD_OK);
        intentFilter.addAction(DownloadNotifier.ACTION_DOWNLOAD_FAIL);
        registerReceiver(this.mainBroadcastNotifier, intentFilter);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("DownloadNotifyService", "Service starting...");
        long downloadID = intent.getLongExtra(DOWNLOAD_ID, 0);
        String episodeDescription = intent.getStringExtra(EPISODE_DESCRIPTION);
        String episodeContent = intent.getStringExtra(EPISODE_CONTENT);

        DownloadNotifier downloadNotifier = new DownloadNotifier(downloadID, episodeDescription,
                episodeContent);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadNotifier, intentFilter);

        this.notifiers.append(downloadID, downloadNotifier);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d("DownloadNotifyService", "Killing service");

        unregisterReceiver(this.mainBroadcastNotifier);

        for (int i=0; i<this.notifiers.size(); i++) {
            unregisterReceiver(this.notifiers.valueAt(i));
        }
    }

    @Override
    public void onSuccess(Object result) {
        closeNotifier((long) result);
    }

    @Override
    public void onFailure(Object result) {
        closeNotifier((long) result);
    }


    private void closeNotifier(long downloadID) {
        Log.d("DownloadNotifyService", "Closing notifier");

        unregisterReceiver(this.notifiers.get(downloadID));
        this.notifiers.remove(downloadID);
        FilesHelper.removeDownloadReference(this, downloadID);

        if (this.notifiers.size() == 0) {
            Log.d("DownloadNotifyService", "Stopping Service");
            stopSelf();
        }
    }
}