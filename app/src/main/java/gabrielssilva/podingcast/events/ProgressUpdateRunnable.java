package gabrielssilva.podingcast.events;

import android.os.Handler;

import gabrielssilva.podingcast.app.PlayerEventListener;
import gabrielssilva.podingcast.service.PlayerService;

public class ProgressUpdateRunnable implements Runnable {

    private PlayerEventListener playerEventListener;
    private PlayerService playerService;
    private Handler handler;
    private int currentProgress;

    public ProgressUpdateRunnable(PlayerEventListener playerEventListener) {
        this.playerEventListener = playerEventListener;
        this.playerService = playerEventListener.getService();
        this.handler = playerEventListener.getHandler();
        this.currentProgress = 0;
    }

    @Override
    public void run() {
        this.currentProgress = this.playerService.getAudioPosition();
        this.playerEventListener.updateSeekBar(this.currentProgress);

        this.handler.postDelayed(this, 100);
    }
}