package gabrielssilva.podingcast.helper;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;

public class Mp3Helper {

    private static MediaMetadataRetriever dataRetriever;

    private Mp3Helper() { }

    private static void initRetriever() {
        if (dataRetriever == null) {
            dataRetriever = new MediaMetadataRetriever();
        }
    }

    private static void setFileSource(Context context, File mp3File) {
        initRetriever();

        Uri uri = Uri.fromFile(mp3File);
        dataRetriever.setDataSource(context, uri);
    }

    public static String getFileTitle(Context context, File mp3File) {
        setFileSource(context, mp3File);
        return dataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    public static String getFeedName(Context context, File mp3File) {
        setFileSource(context, mp3File);
        return dataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
    }
}
