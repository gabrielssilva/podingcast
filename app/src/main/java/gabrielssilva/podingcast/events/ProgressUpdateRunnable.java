package gabrielssilva.podingcast.events;

import android.os.Handler;

import gabrielssilva.podingcast.app.PlayerListener;
import gabrielssilva.podingcast.service.PlayerService;

public class ProgressUpdateRunnable implements Runnable {

    private PlayerListener playerListener;
    private PlayerService playerService;
    private Handler handler;
    private int currentProgress;

    public ProgressUpdateRunnable(PlayerListener playerListener) {
        this.playerListener = playerListener;
        this.playerService = playerListener.getService();
        this.handler = playerListener.getHandler();
        this.currentProgress = 0;
    }

    @Override
    public void run() {
        this.currentProgress = this.playerService.getAudioPosition();
        this.playerListener.updateSeekBar(this.currentProgress);

        this.handler.postDelayed(this, 100);
    }
}