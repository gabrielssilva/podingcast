package gabrielssilva.podingcast.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.io.IOException;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.helper.Mp3Helper;
import gabrielssilva.podingcast.model.Episode;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {

    public final static int MINUS_DELTA = -30000;
    public final static int PLUS_DELTA = 30000;
    public final static String ACTION_PLAY_PAUSE = "gabrielssilva.podingcast.action_play_or_pause";
    public final static String ACTION_REPLAY_30 = "gabrielssilva.podingcast.action_replay_30";
    public final static String ACTION_FORWARD_30 = "gabrielssilva.podingcast.action_forward_30";

    private MediaPlayer mediaPlayer;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews notificationContent;
    private int lastAudioPosition;
    private int currentAudioPosition;

    @Override
    public void onCreate() {
        this.mediaPlayer = new MediaPlayer();
        this.notificationBuilder = new NotificationCompat.Builder(this);

        this.initializeMediaPlayer();
        this.startForeground(1, this.createNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String intentAction = intent.getAction();

        if (intentAction != null && intentAction.equals(ACTION_PLAY_PAUSE)) {
            this.playOrPause();
        } else if (intentAction != null && intentAction.equals(ACTION_REPLAY_30)) {
            this.seekToWithDelta(MINUS_DELTA);
        } else if (intentAction != null && intentAction.equals(ACTION_FORWARD_30)) {
            this.seekToWithDelta(PLUS_DELTA);
        }

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


    private Notification createNotification() {
        this.notificationContent = new RemoteViews(this.getPackageName(),
                R.layout.player_notification);
        this.setNotificationActions(this.notificationContent);
        this.notificationBuilder.setContent(this.notificationContent)
                .setSmallIcon(R.drawable.ic_launcher);

        return this.notificationBuilder.build();
    }

    private void setNotificationActions(RemoteViews content) {
        Intent intent = new Intent(this, PlayerService.class);
        intent.setAction(ACTION_PLAY_PAUSE);
        PendingIntent playPausePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        content.setOnClickPendingIntent(R.id.play_button, playPausePendingIntent);

        intent.setAction(ACTION_REPLAY_30);
        PendingIntent replayPendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        content.setOnClickPendingIntent(R.id.replay_30_button, replayPendingIntent);

        intent.setAction(ACTION_FORWARD_30);
        PendingIntent forwardPendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        content.setOnClickPendingIntent(R.id.forward_30_button, forwardPendingIntent);
    }

    public void playOrPause() {
        if (this.isPlaying()) {
            this.pauseAudio();
        } else {
            this.playAudio();
        }
    }

    private void initializeMediaPlayer() {
        this.mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaPlayer.setOnCompletionListener(this);

        this.loadAudio("");
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

    public void updateNotification(Episode episode) {
        Mp3Helper mp3Helper = new Mp3Helper(episode.getFilePath());
        Bitmap episodeCover = mp3Helper.getEpisodeCover(this.getResources());

        this.notificationContent.setImageViewBitmap(R.id.episode_cover, episodeCover);
        this.notificationContent.setTextViewText(R.id.episode_name, episode.getEpisodeName());
        this.startForeground(1, this.notificationBuilder.build());
    }

    public void playAudio() {
        this.mediaPlayer.seekTo(this.currentAudioPosition);
        this.mediaPlayer.start();

        this.notificationContent.setImageViewResource(R.id.play_button,
                R.drawable.ic_pause_black_48dp);
        this.startForeground(1, this.notificationBuilder.build());
    }

    public void pauseAudio() {
        this.mediaPlayer.pause();
        this.currentAudioPosition = this.mediaPlayer.getCurrentPosition();

        this.notificationContent.setImageViewResource(R.id.play_button,
                R.drawable.ic_play_arrow_black_48dp);
        this.startForeground(1, this.notificationBuilder.build());
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

    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}
