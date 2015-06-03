package gabrielssilva.podingcast.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

import gabrielssilva.podingcast.app.R;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private int lastAudioPosition;
    private int currentAudioPosition;

    @Override
    public void onCreate() {
        this.mediaPlayer = new MediaPlayer();
        this.initializeMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setSmallIcon(R.drawable.ic_play);
        nBuilder.setContentTitle("Playing...");

        this.startForeground(1, nBuilder.build());
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayerBinder();
    }

    @Override
    public void onDestroy() {
        this.mediaPlayer.release();
        this.mediaPlayer = new MediaPlayer();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        this.stopForeground(true);
        this.stopSelf();

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
        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}
