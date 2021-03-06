package gabrielssilva.podingcast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.model.Podcast;

public class FilesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Podingcast.db";

    public FilesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(FilesDbContract.FeedEntry.CREATE_TABLE);
        database.execSQL(FilesDbContract.FileEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(FilesDbContract.FileEntry.DROP_TABLE);
        database.execSQL(FilesDbContract.FeedEntry.DROP_TABLE);

        onCreate(database);
    }

    public Cursor getAllFeeds() {
        SQLiteDatabase database = getReadableDatabase();
        String columns[] = { FilesDbContract.FeedEntry.FEED_NAME,
                FilesDbContract.FeedEntry.FEED_ADDRESS,
                FilesDbContract.FeedEntry.FEED_IMG_ADDRESS};
        String sortOrder = FilesDbContract.FeedEntry.FEED_NAME + " ASC";

        return database.query(FilesDbContract.FeedEntry.TABLE_NAME, columns, null, null, null,
                null, sortOrder);
    }

    public Cursor getPodcast(String podcastName) {
        SQLiteDatabase database = getReadableDatabase();
        String columns[] = { FilesDbContract.FeedEntry.FEED_NAME,
                FilesDbContract.FeedEntry.FEED_ADDRESS,
                FilesDbContract.FeedEntry.FEED_IMG_ADDRESS};
        String selection = FilesDbContract.FeedEntry.FEED_NAME + " = ?";
        String selectionArgs[] = { podcastName };
        String sortOrder = FilesDbContract.FeedEntry.FEED_NAME + " ASC";

        return database.query(FilesDbContract.FeedEntry.TABLE_NAME, columns, selection,
                selectionArgs, null, null, sortOrder);
    }

    public Cursor getFeedFiles(String feedName) {
        SQLiteDatabase database = getReadableDatabase();
        String feedId = ""+this.getPodcastId(database, feedName);

        String columns[] = { FilesDbContract.FileEntry.FILE_NAME,
                FilesDbContract.FileEntry.FILE_PATH,
                FilesDbContract.FileEntry.URL,
                FilesDbContract.FileEntry.FILE_LAST_POS,
                FilesDbContract.FileEntry.DESCRIPTION,
                FilesDbContract.FileEntry.CONTENT };
        String selection = FilesDbContract.FileEntry.FEED_ID + " = ?";
        String selectionArgs[] = { feedId };
        String sortOrder = FilesDbContract.FileEntry._ID + " ASC";

        return database.query(FilesDbContract.FileEntry.TABLE_NAME, columns, selection,
                selectionArgs, null, null, sortOrder);
    }

    public Cursor getEpisode(String fileName) {
        SQLiteDatabase database = getReadableDatabase();

        String columns[] = { FilesDbContract.FileEntry.FILE_NAME,
                FilesDbContract.FileEntry.FILE_PATH,
                FilesDbContract.FileEntry.URL,
                FilesDbContract.FileEntry.FILE_LAST_POS,
                FilesDbContract.FileEntry.DESCRIPTION,
                FilesDbContract.FileEntry.CONTENT };
        String selection = FilesDbContract.FileEntry.FILE_NAME + " = ?";
        String selectionArgs[] = { fileName };
        String sortOrder = FilesDbContract.FileEntry._ID + " ASC";

        return database.query(FilesDbContract.FileEntry.TABLE_NAME, columns, selection,
                selectionArgs, null, null, sortOrder);
    }

    public void insertEpisode(String feedName, Episode episode) {
        /* This function will be called on loops, sometimes.
           Maybe Find a nice way to get the database just once.
          */
        SQLiteDatabase database = getWritableDatabase();
        insertPodcast(database, new Podcast(feedName, null, null));
        long feedId = getPodcastId(database, feedName);
        insertEpisode(database, feedId, episode);
    }

    public void removeEpisode(String episodeName) {
        SQLiteDatabase database = getWritableDatabase();
        String selection = FilesDbContract.FileEntry.FILE_NAME + " LIKE ?";
        String selectionArgs[] = { episodeName };

        database.delete(FilesDbContract.FileEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void insertPodcast(Podcast podcast) {
        SQLiteDatabase database = getWritableDatabase();
        insertPodcast(database, podcast);
    }

    public void removePodcast(String podcastName) {
        SQLiteDatabase database = getWritableDatabase();
        String selection = FilesDbContract.FeedEntry.FEED_NAME + " LIKE ?";
        String selectionArgs[] = { podcastName };

        database.delete(FilesDbContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void updateLastPosition(String fileName, int currentPosition) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FilesDbContract.FileEntry.FILE_LAST_POS, currentPosition);

        String selection = FilesDbContract.FileEntry.FILE_NAME + " like ?";
        String selectionArgs[] = { fileName };

        database.update(FilesDbContract.FileEntry.TABLE_NAME, values, selection, selectionArgs);
    }


    private int getPodcastId(SQLiteDatabase database, String feedName) {
        String columns[] = { FilesDbContract.FeedEntry._ID };
        String selection = FilesDbContract.FeedEntry.FEED_NAME + " like ?";
        String selectionArgs[] = { feedName };

        Cursor queryResult = database.query(FilesDbContract.FeedEntry.TABLE_NAME, columns,
                selection, selectionArgs, null, null, null);

        queryResult.moveToFirst();
        int columnIndex = queryResult.getColumnIndexOrThrow(FilesDbContract.FeedEntry._ID);

        return queryResult.getInt(columnIndex);
    }

    private void insertEpisode(SQLiteDatabase database, long feedId, Episode episode) {
        ContentValues values = new ContentValues();
        values.put(FilesDbContract.FileEntry.FEED_ID, feedId);
        values.put(FilesDbContract.FileEntry.FILE_NAME, episode.getEpisodeName());
        values.put(FilesDbContract.FileEntry.FILE_PATH, episode.getFilePath());
        values.put(FilesDbContract.FileEntry.URL, episode.getUrl());
        values.put(FilesDbContract.FileEntry.DESCRIPTION, episode.getDescription());
        values.put(FilesDbContract.FileEntry.CONTENT, episode.getContent());

        database.insertWithOnConflict(FilesDbContract.FileEntry.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void insertPodcast(SQLiteDatabase database, Podcast podcast) {
        ContentValues values = new ContentValues();
        values.put(FilesDbContract.FeedEntry.FEED_NAME, podcast.getPodcastName());
        values.put(FilesDbContract.FeedEntry.FEED_ADDRESS, podcast.getRssAddress());
        values.put(FilesDbContract.FeedEntry.FEED_IMG_ADDRESS, podcast.getImageAddress());


        database.insertWithOnConflict(FilesDbContract.FeedEntry.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
    }
}