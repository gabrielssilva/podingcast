package gabrielssilva.podingcast.app;

import android.content.res.Resources;
import android.os.Handler;

import gabrielssilva.podingcast.service.PlayerService;

public interface PlayerEventListener {
    public Resources getResources();
    public PlayerService getService();
    public Handler getHandler();

    public void updateSeekBar(int newProgress);
    public void startUpdatingSeekBar();
    public void stopUpdatingSeekBar();
}