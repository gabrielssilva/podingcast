package gabrielssilva.podingcast.events;

import android.widget.SeekBar;

import gabrielssilva.podingcast.app.PlayerEventListener;
import gabrielssilva.podingcast.controller.ServiceController;

public class SeekBarTouch implements SeekBar.OnSeekBarChangeListener {

    private PlayerEventListener eventListener;
    private ServiceController serviceController;

    public SeekBarTouch(PlayerEventListener eventListener, ServiceController serviceController) {
        this.eventListener = eventListener;
        this.serviceController = serviceController;
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
        this.serviceController.seekToPosition(seekPosition, false);
        this.eventListener.startUpdatingSeekBar();
    }
}