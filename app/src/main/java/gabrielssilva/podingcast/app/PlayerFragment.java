package gabrielssilva.podingcast.app;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import gabrielssilva.podingcast.controller.ServiceController;
import gabrielssilva.podingcast.events.PlayPauseClick;
import gabrielssilva.podingcast.events.SeekBarTouch;
import gabrielssilva.podingcast.events.JumpAudioPositionClick;
import gabrielssilva.podingcast.events.ProgressUpdateRunnable;

public class PlayerFragment extends Fragment implements PlayerEventListener {

    private Button buttonPlayPause;
    private Button buttonSkipAudio;
    private Button buttonBackAudio;
    private SeekBar seekBar;

    private View rootView;
    private Handler handler;
    ProgressUpdateRunnable updateRunnable;
    private ServiceController serviceController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        this.serviceController = ((HomeActivity) this.getActivity()).getServiceController();
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

    private void initViews() {
        this.buttonPlayPause = (Button) this.rootView.findViewById(R.id.button_play_pause);
        this.buttonSkipAudio = (Button) this.rootView.findViewById(R.id.button_plus_30);
        this.buttonBackAudio = (Button) this.rootView.findViewById(R.id.button_minus_30);

        this.seekBar = (SeekBar) this.rootView.findViewById(R.id.seek_bar);
    }

    public void initSeekBar() {
        int audioDuration = this.serviceController.getAudioDuration();
        SeekBarTouch seekBarTouchEvent = new SeekBarTouch(this);
        this.updateRunnable = new ProgressUpdateRunnable(this);

        this.seekBar.setMax(audioDuration);
        this.seekBar.setOnSeekBarChangeListener(seekBarTouchEvent);
        this.updateSeekBar();
    }

    private void setButtonEvents() {
        PlayPauseClick playPauseEvent = new PlayPauseClick(this);
        this.buttonPlayPause.setOnClickListener(playPauseEvent);

        JumpAudioPositionClick skipAudioEvent = new JumpAudioPositionClick(this, 30000);
        this.buttonSkipAudio.setOnClickListener(skipAudioEvent);

        JumpAudioPositionClick backAudioEvent = new JumpAudioPositionClick(this, -30000);
        this.buttonBackAudio.setOnClickListener(backAudioEvent);
    }

    @Override
    public void playOrPause() {
        this.serviceController.playOrPause();
        this.updateButtonPlayPause();
    }

    private void updateButtonPlayPause() {
        Resources resources = this.getResources();
        String contentDescription;
        Drawable icon;

        if (this.serviceController.isPlaying()) {
            icon = resources.getDrawable(R.drawable.pause);
            contentDescription = "Pause";
        } else {
            icon = resources.getDrawable(R.drawable.play);
            contentDescription = "Play";
        }

        this.buttonPlayPause.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        this.buttonPlayPause.setContentDescription(contentDescription);
    }

    @Override
    public void seekToPosition(int deltaInMilliseconds) {
        this.serviceController.seekToPosition(deltaInMilliseconds, true);
    }

    @Override
    public void updateSeekBar() {
        int newProgress = this.serviceController.getAudioPosition();
        this.seekBar.setProgress(newProgress);
        this.handler.postDelayed(this.updateRunnable, 100);
    }

    @Override
    public void continueUpdatingSeekBar(int seekPosition) {
        this.serviceController.seekToPosition(seekPosition, false);
        this.handler.postDelayed(this.updateRunnable, 100);
    }

    @Override
    public void stopUpdatingSeekBar() {
        this.handler.removeCallbacks(this.updateRunnable);
    }
}