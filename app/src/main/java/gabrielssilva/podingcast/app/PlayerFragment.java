package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import gabrielssilva.podingcast.events.OnPlayPauseClick;
import gabrielssilva.podingcast.events.OnSeekBarTouch;
import gabrielssilva.podingcast.events.OnJumpAudioPositionClick;
import gabrielssilva.podingcast.events.ProgressUpdateRunnable;
import gabrielssilva.podingcast.service.Connection;
import gabrielssilva.podingcast.service.PlayerConnection;
import gabrielssilva.podingcast.service.PlayerService;

public class PlayerFragment extends Fragment implements Connection, PlayerListener {

    private Button buttonPlayPause;
    private Button buttonSkipAudioPosition;
    private Button buttonBackAudioPosition;
    private SeekBar seekBar;

    private PlayerService playerService;
    private Handler handler;
    ProgressUpdateRunnable updateRunnable;
    private Intent playerIntent;
    private boolean bound = false;
    private ServiceConnection playerConnection;
    private Activity activity;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        Log.i("Player Fragment", "Creating Activity...");

        this.playerConnection = new PlayerConnection(this);
        this.handler = new Handler();

        this.activity = getActivity();
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onStart() {
        Log.i("Player Fragment", "Starting Activity...");
        super.onStart();
        this.initViews();

        if (playerIntent == null) {
            playerIntent = new Intent(this.activity, PlayerService.class);
            this.activity.bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);
            this.activity.startService(playerIntent);
        }
    }

    @Override
    public void onDestroy() {
        Log.i("Player Fragment", "Destroying Activity...");

        this.activity.unbindService(playerConnection);
        super.onDestroy();
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public PlayerService getService() {
        return this.playerService;
    }

    @Override
    public void updateSeekBar(int newProgress) {
        this.seekBar.setProgress(newProgress);
    }

    @Override
    public Handler getHandler() {
        return this.handler;
    }

    @Override
    public void setService(PlayerService playerService) {
        this.playerService = playerService;
        this.setButtonEvents();
    }

    @Override
    public void initSeekBar() {
        int audioDuration = this.playerService.getAudioDuration();
        OnSeekBarTouch seekBarTouchEvent = new OnSeekBarTouch(this);
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

    @Override
    public void startUpdatingSeekBar() {
        this.handler.postDelayed(this.updateRunnable, 100);
    }

    @Override
    public void stopUpdatingSeekBar() {
        this.handler.removeCallbacks(this.updateRunnable);
    }

    private void setButtonEvents() {
        OnPlayPauseClick playPauseEvent = new OnPlayPauseClick(this);
        this.buttonPlayPause.setOnClickListener(playPauseEvent);

        OnJumpAudioPositionClick skipAudioPositionClick = new OnJumpAudioPositionClick(this, 30000);
        this.buttonSkipAudioPosition.setOnClickListener(skipAudioPositionClick);

        OnJumpAudioPositionClick backAudioPositionClick = new OnJumpAudioPositionClick(this, -30000);
        this.buttonBackAudioPosition.setOnClickListener(backAudioPositionClick);
    }


    @Override
    public View getRootView() {
        return this.rootView;
    }
}
