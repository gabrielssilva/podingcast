package gabrielssilva.podingcast.app;

import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import gabrielssilva.podingcast.events.PlayPauseClick;
import gabrielssilva.podingcast.events.SeekBarTouch;
import gabrielssilva.podingcast.events.JumpAudioPositionClick;
import gabrielssilva.podingcast.events.ProgressUpdateRunnable;
import gabrielssilva.podingcast.service.ServiceListener;
import gabrielssilva.podingcast.service.PlayerService;

public class PlayerFragment extends Fragment implements PlayerEventListener {

    private Button buttonPlayPause;
    private Button buttonSkipAudioPosition;
    private Button buttonBackAudioPosition;
    private SeekBar seekBar;

    private Handler handler;
    ProgressUpdateRunnable updateRunnable;
    private ServiceListener serviceListener;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        this.serviceListener = (ServiceListener) getActivity();
        this.rootView = rootView;
        this.handler = new Handler();

        this.initViews();
        this.initSeekBar();
        this.setButtonEvents();

        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.i("Player Fragment", "Destroying Activity...");

        //this.activity.unbindService(playerConnection);
        super.onDestroy();
    }

    public void initSeekBar() {
        PlayerService playerService = this.serviceListener.getService();

        int audioDuration = playerService.getAudioDuration();
        SeekBarTouch seekBarTouchEvent = new SeekBarTouch(this, serviceListener);
        this.updateRunnable = new ProgressUpdateRunnable(this);

        this.seekBar.setMax(audioDuration);
        this.seekBar.setOnSeekBarChangeListener(seekBarTouchEvent);
        this.startUpdatingSeekBar();
    }

    private void initViews() {
        this.buttonPlayPause = (Button) this.rootView.findViewById(R.id.button_play_pause);
        this.buttonSkipAudioPosition = (Button) this.rootView.findViewById(R.id.button_plus_30);
        this.buttonBackAudioPosition = (Button) this.rootView.findViewById(R.id.button_minus_30);

        this.seekBar = (SeekBar) this.rootView.findViewById(R.id.seek_bar);
    }

    private void setButtonEvents() {
        PlayPauseClick playPauseEvent = new PlayPauseClick(this, serviceListener);
        this.buttonPlayPause.setOnClickListener(playPauseEvent);

        JumpAudioPositionClick skipAudioPositionClick = new JumpAudioPositionClick(serviceListener, 30000);
        this.buttonSkipAudioPosition.setOnClickListener(skipAudioPositionClick);

        JumpAudioPositionClick backAudioPositionClick = new JumpAudioPositionClick(serviceListener, -30000);
        this.buttonBackAudioPosition.setOnClickListener(backAudioPositionClick);
    }

    public void updateSeekBar() {
        int newProgress = this.serviceListener.getService().getAudioPosition();
        this.seekBar.setProgress(newProgress);
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void startUpdatingSeekBar() {
        this.handler.postDelayed(this.updateRunnable, 100);
    }

    public void stopUpdatingSeekBar() {
        this.handler.removeCallbacks(this.updateRunnable);
    }
}