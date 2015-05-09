package gabrielssilva.podingcast.database;

import android.provider.BaseColumns;

public final class FilesDbContract {

    private FilesDbContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "feed";
        public static final String FEED_NAME = "feed_name";
        public static final String FEED_ADDRESS = "feed_address";
        public static final String FEED_IMG_ADDRESS = "feed_img_address";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + FEED_NAME + " TEXT UNIQUE, "
                + FEED_ADDRESS + " TEXT, "
                + FEED_IMG_ADDRESS + " TEXT);";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static abstract class FileEntry implements BaseColumns {
        public static final String TABLE_NAME = "file";
        public static final String FEED_ID = "feed_id";
        public static final String FILE_NAME = "file_name";
        public static final String FILE_PATH = "file_path";
        public static final String URL = "url";
        public static final String FILE_LAST_POS = "file_current_pos";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + FEED_ID + " INTEGER, "
                + FILE_NAME + " TEXT UNIQUE, "
                + FILE_PATH + " TEXT, "
                + URL + " TEXT, "
                + FILE_LAST_POS + " INTEGER DEFAULT 0, "
                + " FOREIGN KEY (" + FEED_ID + ") REFERENCES " + FeedEntry.TABLE_NAME
                + " (" + FeedEntry._ID + ") ON DELETE CASCADE" + " ON UPDATE CASCADE);" ;
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}