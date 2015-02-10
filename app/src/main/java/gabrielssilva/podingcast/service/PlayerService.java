package gabrielssilva.podingcast.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private int lastAudioPosition;
    private int currentAudioPosition;
    private final IBinder playerBind = new PlayerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Service", "Initializing service (service)");
        this.mediaPlayer = new MediaPlayer();
        this.initializeMediaPlayer();

        return this.playerBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        this.mediaPlayer.release();
        this.mediaPlayer = new MediaPlayer();

        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.currentAudioPosition = 0;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // Nothing special.
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // Do nothing
    }

    public void loadAudio(String filePath) {
        this.currentAudioPosition = lastAudioPosition;
        this.mediaPlayer.reset();

        try {
            this.mediaPlayer.setDataSource(filePath);
            this.mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playAudio() {
        this.mediaPlayer.seekTo(this.currentAudioPosition);
        this.mediaPlayer.start();
    }

    public void pauseAudio() {
        this.mediaPlayer.pause();
        this.currentAudioPosition = this.mediaPlayer.getCurrentPosition();
    }

    public void genericSeekPosition(int newAudioPosition) {
        this.pauseAudio();
        this.currentAudioPosition = newAudioPosition;
        this.playAudio();
    }

    public void seekToWithDelta(int deltaInMilliseconds) {
        int newAudioPosition = this.mediaPlayer.getCurrentPosition() + deltaInMilliseconds;
        this.genericSeekPosition(newAudioPosition);
    }

    public int getAudioDuration() {
        return this.mediaPlayer.getDuration();
    }

    public int getAudioPosition() {
        return this.mediaPlayer.getCurrentPosition();
    }

    public void setLastAudioPosition(int lastAudioPosition) {
        this.lastAudioPosition = lastAudioPosition;
    }

    public boolean isPlaying() {
        return this.mediaPlayer.isPlaying();
    }


    private void initializeMediaPlayer() {
        this.mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
        this.mediaPlayer.setOnErrorListener(this);

        this.loadAudio("");
    }


    public class PlayerBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }
}
