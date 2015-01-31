package gabrielssilva.podingcast.app;

import android.content.res.Resources;
import android.os.Handler;

public interface PlayerEventListener {

    public void playOrPause();
    public void seekToPosition(int deltaInMilliseconds);

    public void updateSeekBar();
    public void continueUpdatingSeekBar(int seekPosition);
    public void stopUpdatingSeekBar();
}