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
        Log.i("BroadcastNotifier", "Broadcast Received");
        if (intent.getAction().equals(this.okAction)) {
            this.callbackListener.onSuccess(null);
        } else {
            this.callbackListener.onFailure();
        }
    }
}