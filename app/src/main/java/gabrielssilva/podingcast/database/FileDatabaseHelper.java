package gabrielssilva.podingcast.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FileDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Podingcast.db";

    public FileDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(FilesDatabaseContract.FeedEntry.CREATE_TABLE);
        database.execSQL(FilesDatabaseContract.FileEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(FilesDatabaseContract.FileEntry.DROP_TABLE);
        database.execSQL(FilesDatabaseContract.FeedEntry.DROP_TABLE);

        onCreate(database);
    }
}
