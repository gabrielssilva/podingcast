package gabrielssilva.podingcast.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
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
    private int currentAudioPosition;
    private final IBinder playerBind = new PlayerBinder();

    public void onCreate() {
        super.onCreate();

        this.mediaPlayer = new MediaPlayer();
        this.initializeMediaPlayer();
        this.currentAudioPosition = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.playerBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        this.mediaPlayer.stop();
        this.mediaPlayer.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.seekTo(this.currentAudioPosition);
        mp.start();
    }

    public void playAudio() {
        this.mediaPlayer.reset();

        try {
            AssetFileDescriptor afd = getAssets().openFd("cast.mp3");
            this.mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            this.mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseAudio() {
        this.mediaPlayer.pause();
        this.currentAudioPosition = this.mediaPlayer.getCurrentPosition();
    }

    public void genericSeekPosition(int deltaInMilliseconds) {
        int newAudioPosition;

        this.pauseAudio();
        newAudioPosition = this.currentAudioPosition + deltaInMilliseconds;
        this.currentAudioPosition = newAudioPosition;
        Log.i("new position", ""+newAudioPosition);
        this.playAudio();
    }

    public void backThirtySeconds() {
        int deltaInMilliseconds = -30000;
        this.genericSeekPosition(deltaInMilliseconds);
    }

    public void skipThirtySeconds() {
        int deltaInMilliseconds = 30000;
        this.genericSeekPosition(deltaInMilliseconds);
    }

    private void initializeMediaPlayer() {
        this.mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
        this.mediaPlayer.setOnErrorListener(this);
    }

    public boolean isPlaying() {
        return this.mediaPlayer.isPlaying();
    }

    public class PlayerBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

}
