package gabrielssilva.podingcast.controller;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;

import java.io.File;

import gabrielssilva.podingcast.app.interfaces.ServiceListener;
import gabrielssilva.podingcast.helper.Mp3Helper;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.service.PlayerConnection;
import gabrielssilva.podingcast.service.PlayerService;

public class ServiceController {

    private Context context;
    private boolean bound;
    private Episode episode;

    private ServiceListener serviceListener;
    private PlayerService playerService;
    private ServiceConnection playerConnection;

    public ServiceController(ServiceListener serviceListener) {
        this.serviceListener = serviceListener;
        this.context = serviceListener.getApplicationContext();

        this.initService();
    }

    private void initService() {
        this.playerConnection = new PlayerConnection(this);

        Intent playerIntent = new Intent(this.context, PlayerService.class);
        this.context.bindService(playerIntent, this.playerConnection, Context.BIND_AUTO_CREATE);
        this.context.startService(playerIntent);
    }

    private void updatePlayer() {
        File episodeFile = new File(this.episode.getFilePath());
        Mp3Helper mp3Helper = new Mp3Helper(this.context, episodeFile);
        Bitmap bitmapCover = mp3Helper.getEpisodeCover(this.context.getResources());

        this.serviceListener.setEpisodeCover(bitmapCover);
        this.serviceListener.setSeekBar();
    }


    /*
     * The following two method will be called by the PlayerConnection,
     * as soon as the Service was created. Because of that, it is necessary
     * to check if the Ser
     */
    public void setService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public void playFile(Episode episode) {
        // Save position from current episode and update it's reference to a new one.
        this.saveCurrentPosition();
        this.episode = episode;

        this.playerService.setLastAudioPosition(episode.getLastPlayedPosition());
        this.playerService.loadAudio(episode.getFilePath());
        this.playerService.playAudio();

        this.updatePlayer();
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

    public void destroyService() {
        if (bound) {
            this.context.unbindService(this.playerConnection);
        }
    }

    public void saveCurrentPosition() {
        if (this.episode != null) {
            LocalFilesController localFilesController = new LocalFilesController(this.context);
            localFilesController.saveCurrentPosition(this.episode.getEpisodeName(), this.playerService.getAudioPosition());
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
}
