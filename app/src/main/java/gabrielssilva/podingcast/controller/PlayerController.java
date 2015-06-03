package gabrielssilva.podingcast.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

import gabrielssilva.podingcast.app.interfaces.PlayerListener;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.service.PlayerService;

public class PlayerController implements ServiceConnection {

    public static String SHARED_PREF_KEY = "gabrielssilva.podingcast.last_ep";
    public static String KEY_EP_NAME = "EPISODE_NAME";

    private Context context;
    private boolean bound;
    private Episode episode;

    private PlayerListener playerListener;
    private PlayerService playerService;
    private LocalFilesController localFilesController;

    public PlayerController(PlayerListener playerListener, Context context) {
        this.playerListener = playerListener;
        this.context = context;
        this.localFilesController = new LocalFilesController(context);

        this.initService();
    }

    private void initService() {
        Intent playerIntent = new Intent(this.context, PlayerService.class);

        this.context.startService(playerIntent);
        this.context.bindService(playerIntent, this, Context.BIND_AUTO_CREATE);
    }

    private Episode retrieveLastEpisode() {
        SharedPreferences sharedPrefs =
                this.context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String episodeName = sharedPrefs.getString(KEY_EP_NAME, "");

        return this.localFilesController.getEpisode(episodeName);
    }

    private void updateLastEpisode(Episode episode) {
        SharedPreferences sharedPrefs =
                this.context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(KEY_EP_NAME, episode.getEpisodeName()).apply();
    }


    public void playFile(Episode newEpisode) {
        // Save position from current episode and update it's reference to a new one.
        this.updateLastEpisode(newEpisode);
        this.saveCurrentPosition();
        this.episode = newEpisode;

        this.playerService.setLastAudioPosition(newEpisode.getLastPlayedPosition());
        this.playerService.loadAudio(newEpisode.getFilePath());
        this.playerService.playAudio();

        this.playerListener.updateViews(this.episode);
    }

    public void seekToPosition(int seekPosition, boolean preservePosition) {
        if (preservePosition) {
            this.playerService.seekToWithDelta(seekPosition);
        } else {
            this.playerService.genericSeekPosition(seekPosition);
        }
    }

    public void playOrPause() {
        if (this.isPlaying()) {
            this.playerService.pauseAudio();
        } else {
            this.playerService.playAudio();
        }
    }

    public void disconnectFromService() {
        if (bound) {
            this.context.unbindService(this);
        }
    }

    public void saveCurrentPosition() {
        if (this.episode != null) {
            this.localFilesController.saveCurrentPosition(this.episode.getEpisodeName(),
                    this.playerService.getAudioPosition());
        }
    }

    public int getAudioDuration() {
        if (this.bound) {
            return this.playerService.getAudioDuration();
        } else {
            return 0;
        }
    }

    public int getAudioPosition() {
        if (this.bound) {
            return this.playerService.getAudioPosition();
        } else {
            return 0;
        }
    }

    public boolean isPlaying() {
        return this.bound && this.playerService.isPlaying();
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.playerService = ((PlayerService.PlayerBinder) iBinder).getService();
        this.bound = true;

        this.episode = retrieveLastEpisode();
        this.playerListener.updateViews(this.episode);

        if (this.episode != null && !this.isPlaying()) {
            this.playerService.setLastAudioPosition(this.episode.getLastPlayedPosition());
            this.playerService.loadAudio(this.episode.getFilePath());
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        this.bound = false;
    }
}
