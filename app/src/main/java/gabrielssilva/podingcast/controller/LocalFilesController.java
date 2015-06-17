package gabrielssilva.podingcast.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.database.FilesDbContract;
import gabrielssilva.podingcast.database.FilesDbHelper;
import gabrielssilva.podingcast.helper.FilesHelper;
import gabrielssilva.podingcast.helper.Mp3Helper;
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
        int imgColIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FeedEntry.FEED_IMG_ADDRESS);

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Podcast podcast = new Podcast();
            podcast.setPodcastName(cursor.getString(nameColumnIndex));
            podcast.setRssAddress(cursor.getString(addrColumnIndex));
            podcast.setImageAddress(cursor.getString(imgColIndex));
            podcast.setEpisodes(this.getPodcastEpisodes(podcast));

            list.add(podcast);
        }

        return list;
    }

    public Podcast getPodcast(String podcastName) {
        Cursor cursor = dbHelper.getPodcast(podcastName);
        Podcast podcast = null;

        int nameColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FeedEntry.FEED_NAME);
        int addrColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FeedEntry.FEED_ADDRESS);
        int imgColIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FeedEntry.FEED_IMG_ADDRESS);

        if (cursor.moveToFirst()) {
            podcast = new Podcast();
            podcast.setPodcastName(cursor.getString(nameColumnIndex));
            podcast.setRssAddress(cursor.getString(addrColumnIndex));
            podcast.setImageAddress(cursor.getString(imgColIndex));
            podcast.setEpisodes(this.getPodcastEpisodes(podcast));
        }

        return podcast;
    }

    public Episode getEpisode(String episodeName) {
        Cursor cursor = dbHelper.getEpisode(episodeName);
        Episode episode = null;

        int nameColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_NAME);
        int pathColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_PATH);
        int urlColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FileEntry.URL);
        int posColumnIndex = cursor.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_LAST_POS);

        if (cursor.moveToFirst()) {
            episodeName = cursor.getString(nameColumnIndex);
            String filePath = cursor.getString(pathColumnIndex);
            String url = cursor.getString(urlColumnIndex);
            int lastPlayedPosition = cursor.getInt(posColumnIndex);

            if (FilesHelper.validFile(filePath)) {
                Mp3Helper mp3Helper = new Mp3Helper(filePath);
                episode = new Episode(episodeName, filePath, url, lastPlayedPosition);
                episode.setDuration(mp3Helper.getEpisodeDuration());
            }
        }

        return episode;
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
            if (FilesHelper.validFile(episode.getFilePath())) {
                Mp3Helper mp3Helper = new Mp3Helper(filePath);
                episode.setDuration(mp3Helper.getEpisodeDuration());
                list.add(episode);
            }
        }

        return list;
    }
}