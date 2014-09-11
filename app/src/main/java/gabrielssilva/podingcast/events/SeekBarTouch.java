package gabrielssilva.podingcast.events;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import gabrielssilva.podingcast.app.PlayerListener;

public class SeekBarTouch implements SeekBar.OnSeekBarChangeListener {

    private PlayerListener playerListener;

    public SeekBarTouch(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.playerListener.stopUpdatingSeekBar();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int seekPosition = seekBar.getProgress();
        this.playerListener.getService().genericSeekPosition(seekPosition);
        this.playerListener.startUpdatingSeekBar();
    }
}
