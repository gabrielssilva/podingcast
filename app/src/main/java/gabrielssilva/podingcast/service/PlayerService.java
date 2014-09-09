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

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Player Service", "Created Media Player");
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
        // Do nothing
    }

    public void loadAudio() {
        this.mediaPlayer.reset();

        try {
            AssetFileDescriptor afd = getAssets().openFd("cast.mp3");
            this.mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
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

    private void initializeMediaPlayer() {
        this.mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
        this.mediaPlayer.setOnErrorListener(this);

        this.loadAudio();
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
