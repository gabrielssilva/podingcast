package gabrielssilva.podingcast.events;

import android.widget.SeekBar;

import gabrielssilva.podingcast.app.PlayerEventListener;
import gabrielssilva.podingcast.service.PlayerService;
import gabrielssilva.podingcast.service.ServiceListener;

public class SeekBarTouch implements SeekBar.OnSeekBarChangeListener {

    private PlayerEventListener eventListener;
    private ServiceListener serviceListener;

    public SeekBarTouch(PlayerEventListener eventListener, ServiceListener serviceListener) {
        this.eventListener = eventListener;
        this.serviceListener = serviceListener;
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
        this.serviceListener.getService().genericSeekPosition(seekPosition);
        this.eventListener.startUpdatingSeekBar();
    }
}