package gabrielssilva.podingcast.events;

import android.os.Handler;

import gabrielssilva.podingcast.app.PlayerEventListener;
import gabrielssilva.podingcast.service.PlayerService;
import gabrielssilva.podingcast.service.ServiceListener;

public class ProgressUpdateRunnable implements Runnable {

    private PlayerEventListener eventListener;
    private Handler handler;

    public ProgressUpdateRunnable(PlayerEventListener eventListener) {
        this.eventListener = eventListener;
        this.handler = eventListener.getHandler();
    }

    @Override
    public void run() {
        this.eventListener.updateSeekBar();
        this.handler.postDelayed(this, 100);
    }
}