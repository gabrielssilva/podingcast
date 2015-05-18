package gabrielssilva.podingcast.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;

public class BroadcastNotifier extends BroadcastReceiver {

    private CallbackListener callbackListener;
    private String okAction;

    public BroadcastNotifier(CallbackListener listener, String okAction) {
        this.callbackListener = listener;
        this.okAction = okAction;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BroadcastNotifier", "Broadcast Received");

        long downloadID = intent.getLongExtra(DownloadNotifier.EXTRA_DOWNLOAD_ID, 0);

        if (intent.getAction().equals(this.okAction)) {
            this.callbackListener.onSuccess(downloadID);
        } else {
            this.callbackListener.onFailure(downloadID);
        }
    }
}