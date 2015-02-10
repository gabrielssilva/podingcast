package gabrielssilva.podingcast.events;

import gabrielssilva.podingcast.app.interfaces.PlayerEventListener;

public class ProgressUpdateRunnable implements Runnable {

    private PlayerEventListener eventListener;

    public ProgressUpdateRunnable(PlayerEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void run() {
        this.eventListener.updateSeekBar();
    }
}