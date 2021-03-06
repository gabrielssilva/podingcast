package gabrielssilva.podingcast.helper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import gabrielssilva.podingcast.app.R;

public class Mp3Helper {

    private MediaMetadataRetriever dataRetriever;

    public Mp3Helper(String mp3FilePath) {
        this.dataRetriever = new MediaMetadataRetriever();
        dataRetriever.setDataSource(mp3FilePath);
    }

    public String getFileTitle() {
        return this.dataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    public String getFeedName() {
        return this.dataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
    }

    public Bitmap getEpisodeCover(Resources resources) {
        byte[] bytesPicture = this.dataRetriever.getEmbeddedPicture();
        Bitmap picture;

        if (bytesPicture != null) {
            picture = BitmapFactory.decodeByteArray(bytesPicture, 0, bytesPicture.length);
        } else {
            picture = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        }

        return picture;
    }

    public String getEpisodeDuration() {
        int totalMilliseconds = Integer.parseInt(this.dataRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION));
        int seconds = Math.round(totalMilliseconds / 1000);
        int minutes = Math.round(seconds / 60);
        int hours = Math.round(minutes / 60);

        seconds = seconds % 60;
        minutes = minutes % 60;

        return hours+":"+minutes+":"+seconds;
    }
}