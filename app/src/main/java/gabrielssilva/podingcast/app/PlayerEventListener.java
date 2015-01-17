package gabrielssilva.podingcast.app;

import android.content.res.Resources;
import android.os.Handler;

public interface PlayerEventListener {
    public Resources getResources();
    public Handler getHandler();

    public void updateSeekBar();
    public void startUpdatingSeekBar();
    public void stopUpdatingSeekBar();
}