package gabrielssilva.podingcast.controller;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

import gabrielssilva.podingcast.service.PlayerConnection;
import gabrielssilva.podingcast.service.PlayerService;

public class ServiceController {

    private Context context;
    private boolean bound;

    private PlayerService playerService;
    private ServiceConnection playerConnection;

    public ServiceController(Context context) {
        this.context = context;
    }

    public void initService() {
        this.playerConnection = new PlayerConnection(this);

        if (!bound) {
            Log.i("Player Fragment", "Creating a new intent");
            Intent playerIntent = new Intent(this.context, PlayerService.class);

            this.context.bindService(playerIntent, this.playerConnection, Context.BIND_AUTO_CREATE);
            this.context.startService(playerIntent);
        }
    }

    public void playFile(String fileName) {
        FilesController filesController = new FilesController(this.context);
        String filePath = filesController.getFilePath(fileName);

        this.playerService.loadAudio(filePath);
        this.playerService.playAudio();
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

    public void setService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public boolean isBound() {
        return this.bound;
    }

    public int getAudioDuration() {
        return this.playerService.getAudioDuration();
    }

    public int getAudioPosition() {
        return this.playerService.getAudioPosition();
    }

    public boolean isPlaying() {
        return this.playerService.isPlaying();
    }
}
