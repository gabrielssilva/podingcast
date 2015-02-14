package gabrielssilva.podingcast.events;

import android.widget.SeekBar;

import gabrielssilva.podingcast.app.interfaces.PlayerEventListener;

public class SeekBarTouch implements SeekBar.OnSeekBarChangeListener {

    private PlayerEventListener eventListener;

    public SeekBarTouch(PlayerEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.eventListener.stopUpdatingSeekBar();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int seekPosition = seekBar.getProgress();
        this.eventListener.startUpdatingSeekBar(seekPosition);
    }
}