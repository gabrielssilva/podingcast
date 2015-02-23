package gabrielssilva.podingcast.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;

import gabrielssilva.podingcast.app.R;

public class Mp3Helper {

    private MediaMetadataRetriever dataRetriever;

    public Mp3Helper(Context context, File mp3File) {
        this.dataRetriever = new MediaMetadataRetriever();

        Uri uri = Uri.fromFile(mp3File);
        dataRetriever.setDataSource(context, uri);
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
}