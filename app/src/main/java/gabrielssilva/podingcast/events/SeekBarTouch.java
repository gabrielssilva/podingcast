package gabrielssilva.podingcast.events;

import android.widget.SeekBar;

import gabrielssilva.podingcast.app.PlayerEventListener;

public class SeekBarTouch implements SeekBar.OnSeekBarChangeListener {

    private PlayerEventListener playerEventListener;

    public SeekBarTouch(PlayerEventListener playerEventListener) {
        this.playerEventListener = playerEventListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.playerEventListener.stopUpdatingSeekBar();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int seekPosition = seekBar.getProgress();
        this.playerEventListener.getService().genericSeekPosition(seekPosition);
        this.playerEventListener.startUpdatingSeekBar();
    }
}
