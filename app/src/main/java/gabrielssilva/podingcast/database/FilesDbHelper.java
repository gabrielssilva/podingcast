package gabrielssilva.podingcast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FilesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
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
        String columns[] = { FilesDbContract.FeedEntry.FEED_NAME };
        String sortOrder = FilesDbContract.FeedEntry.FEED_NAME + " ASC";

        return database.query(FilesDbContract.FeedEntry.TABLE_NAME, columns, null, null, null,
                null, sortOrder);
    }

    public Cursor getFeedFiles(String feedName) {
        SQLiteDatabase database = getReadableDatabase();
        String feedId = ""+this.getFeedId(database, feedName);

        String columns[] = { FilesDbContract.FileEntry.FILE_NAME, FilesDbContract.FileEntry.FILE_PATH };
        String selection = FilesDbContract.FileEntry.FEED_ID + " = ?";
        String selectionArgs[] = { feedId };
        String sortOrder = FilesDbContract.FileEntry._ID + " ASC";

        return database.query(FilesDbContract.FileEntry.TABLE_NAME, columns, selection,
                selectionArgs, null, null, sortOrder);
    }

    public Cursor getFilePath(String fileName) {
        SQLiteDatabase database = getReadableDatabase();

        String columns[] = { FilesDbContract.FileEntry.FILE_PATH };
        String selection = FilesDbContract.FileEntry.FILE_NAME + " = ?";
        String selectionArgs[] = { fileName };

        return database.query(FilesDbContract.FileEntry.TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);
    }

    public void insertPodcast(String feedName, String fileTitle, String filePath) {
        /* This function will be called on loops, sometimes.
           Maybe Find a nice way to get the database just once.
          */
        SQLiteDatabase database = getWritableDatabase();

        insertFeed(database, feedName);

        int feedId = getFeedId(database, feedName);
        insertFile(database, feedId, fileTitle, filePath);
    }

    private int getFeedId(SQLiteDatabase database, String feedName) {
        String columns[] = { FilesDbContract.FeedEntry._ID };
        String selection = FilesDbContract.FeedEntry.FEED_NAME + " like ?";
        String selectionArgs[] = { feedName };

        Cursor queryResult = database.query(FilesDbContract.FeedEntry.TABLE_NAME, columns,
                selection, selectionArgs, null, null, null);

        queryResult.moveToFirst();
        int columnIndex = queryResult.getColumnIndexOrThrow(FilesDbContract.FeedEntry._ID);

        return queryResult.getInt(columnIndex);
    }

    private void insertFeed(SQLiteDatabase database, String feedName) {
        ContentValues values = new ContentValues();
        values.put(FilesDbContract.FeedEntry.FEED_NAME, feedName);

        database.insertWithOnConflict(FilesDbContract.FeedEntry.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void insertFile(SQLiteDatabase database, int feedId, String fileName, String filePath) {
        ContentValues values = new ContentValues();
        values.put(FilesDbContract.FileEntry.FEED_ID, feedId);
        values.put(FilesDbContract.FileEntry.FILE_NAME, fileName);
        values.put(FilesDbContract.FileEntry.FILE_PATH, filePath);

        database.insertWithOnConflict(FilesDbContract.FileEntry.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
    }
}