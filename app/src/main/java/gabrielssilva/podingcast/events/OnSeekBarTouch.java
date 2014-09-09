package gabrielssilva.podingcast.events;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import gabrielssilva.podingcast.app.PlayerListener;

public class OnSeekBarTouch implements SeekBar.OnSeekBarChangeListener {

    private PlayerListener listener;

    public OnSeekBarTouch(PlayerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int seekPosition = seekBar.getProgress();
        this.listener.getService().genericSeekPosition(seekPosition);
    }
}
