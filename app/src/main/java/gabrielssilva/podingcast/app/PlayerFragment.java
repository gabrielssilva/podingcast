package gabrielssilva.podingcast.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import gabrielssilva.podingcast.app.interfaces.PlayerEventListener;
import gabrielssilva.podingcast.app.interfaces.PlayerListener;
import gabrielssilva.podingcast.controller.PlayerController;
import gabrielssilva.podingcast.events.PlayPauseClick;
import gabrielssilva.podingcast.events.SeekBarTouch;
import gabrielssilva.podingcast.events.JumpAudioPositionClick;
import gabrielssilva.podingcast.events.ProgressUpdateRunnable;
import gabrielssilva.podingcast.helper.Mp3Helper;
import gabrielssilva.podingcast.model.Episode;

public class PlayerFragment extends Fragment implements PlayerEventListener,
        PlayerListener {

    private ImageButton buttonPlayPause;
    private ImageButton buttonSkipAudio;
    private ImageButton buttonBackAudio;
    private SeekBar seekBar;
    private ImageView imageView;

    private View rootView;
    private Handler handler;
    ProgressUpdateRunnable updateRunnable;
    private PlayerController playerController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        this.rootView = rootView;
        this.handler = new Handler();

        this.initViews();
        this.setButtonEvents();

        this.playerController = new PlayerController(this, this.getActivity());
        ((HomeActivity) this.getActivity()).setPlayerController(this.playerController);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopUpdatingSeekBar();

        this.playerController.saveCurrentPosition();
        this.playerController.disconnectFromService();

        Log.i("Activity Life Cycle", "Destroying activity");
    }

    @Override
    public void onResume() {
        super.onResume();
        this.updateSeekBar();
    }


    private void initViews() {
        this.buttonPlayPause = (ImageButton) this.rootView.findViewById(R.id.button_play_pause);
        this.buttonSkipAudio = (ImageButton) this.rootView.findViewById(R.id.button_plus_30);
        this.buttonBackAudio = (ImageButton) this.rootView.findViewById(R.id.button_minus_30);

        this.seekBar = (SeekBar) this.rootView.findViewById(R.id.seek_bar);
        this.imageView = (ImageView) this.rootView.findViewById(R.id.episode_cover);
    }

    private void setButtonEvents() {
        PlayPauseClick playPauseEvent = new PlayPauseClick(this);
        this.buttonPlayPause.setOnClickListener(playPauseEvent);

        JumpAudioPositionClick skipAudioEvent = new JumpAudioPositionClick(this, 30000);
        this.buttonSkipAudio.setOnClickListener(skipAudioEvent);

        JumpAudioPositionClick backAudioEvent = new JumpAudioPositionClick(this, -30000);
        this.buttonBackAudio.setOnClickListener(backAudioEvent);
    }

    private void updateButtonPlayPause() {
        String contentDescription;
        int selectorId;

        if (this.playerController.isPlaying()) {
            selectorId = R.drawable.ic_pause_black_48dp;
            contentDescription = "Pause";
        } else {
            selectorId = R.drawable.ic_play_arrow_black_48dp;
            contentDescription = "Play";
        }

        this.buttonPlayPause.setImageResource(selectorId);
        this.buttonPlayPause.setContentDescription(contentDescription);
    }

    private void setupSeekBar() {
        int audioDuration = this.playerController.getAudioDuration();
        SeekBarTouch seekBarTouchEvent = new SeekBarTouch(this);
        this.updateRunnable = new ProgressUpdateRunnable(this);

        this.seekBar.setMax(audioDuration);
        this.seekBar.setOnSeekBarChangeListener(seekBarTouchEvent);

        this.updateSeekBar();
        this.updateButtonPlayPause();
    }

    private void setEpisodeCover(String filePath) {
        Mp3Helper mp3Helper = new Mp3Helper(filePath);
        Bitmap episodeCover = mp3Helper.getEpisodeCover(this.getResources());

        this.imageView.setImageBitmap(episodeCover);
    }


    @Override
    public void playOrPause() {
        this.playerController.playOrPause();
        this.updateButtonPlayPause();
    }

    @Override
    public void seekToPosition(int deltaInMilliseconds) {
        this.playerController.seekToPosition(deltaInMilliseconds, true);
        this.updateButtonPlayPause();
    }

    @Override
    public void updateSeekBar() {
        int newProgress = this.playerController.getAudioPosition();

        this.seekBar.setProgress(newProgress);
        this.handler.postDelayed(this.updateRunnable, 100);
    }

    @Override
    public void startUpdatingSeekBar(int seekPosition) {
        this.playerController.seekToPosition(seekPosition, false);
        this.handler.postDelayed(this.updateRunnable, 100);
        this.updateButtonPlayPause();
    }

    @Override
    public void stopUpdatingSeekBar() {
        this.handler.removeCallbacks(this.updateRunnable);
    }

    @Override
    public void updateViews(Episode episode) {
        if (episode != null) {
            this.setupSeekBar();
            this.setEpisodeCover(episode.getFilePath());
            this.updateButtonPlayPause();
        }
    }
}