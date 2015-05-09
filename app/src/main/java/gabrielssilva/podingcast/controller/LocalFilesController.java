package gabrielssilva.podingcast.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.database.FilesDbContract;
import gabrielssilva.podingcast.database.FilesDbHelper;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.model.Podcast;

public class LocalFilesController {

    private FilesDbHelper dbHelper;

    public LocalFilesController(Context context) {
        dbHelper = new FilesDbHelper(context);
    }

    public List<Podcast> getAllPodcasts() {
        List<Podcast> list = new ArrayList<>();
        Cursor cursor = dbHelper.getAllFeeds();

        int nameColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FeedEntry.FEED_NAME);
        int addrColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FeedEntry.FEED_ADDRESS);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Podcast podcast = new Podcast();
            podcast.setPodcastName(cursor.getString(nameColumnIndex));
            podcast.setRssAddress(cursor.getString(addrColumnIndex));
            podcast.setEpisodes(this.getPodcastEpisodes(podcast));

            list.add(podcast);
        }

        return list;
    }

    public void updatePodcast(Podcast podcast) {
        List<Episode> episodes = this.getPodcastEpisodes(podcast);
        podcast.setEpisodes(episodes);
    }

    public void saveCurrentPosition(String fileName, int currentPosition) {
        this.dbHelper.updateLastPosition(fileName, currentPosition);
    }


    private List<Episode> getPodcastEpisodes(Podcast podcast) {
        List<Episode> list = new ArrayList<>();
        Cursor cursor = dbHelper.getFeedFiles(podcast.getPodcastName());

        int nameColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_NAME);
        int pathColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_PATH);
        int urlColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FileEntry.URL);
        int posColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_LAST_POS);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String episodeName = cursor.getString(nameColumnIndex);
            String filePath = cursor.getString(pathColumnIndex);
            String url = cursor.getString(urlColumnIndex);
            int lastPlayedPosition = cursor.getInt(posColumnIndex);

            Episode episode = new Episode(episodeName, filePath, url,lastPlayedPosition);
            list.add(episode);
        }

        return list;
    }
}