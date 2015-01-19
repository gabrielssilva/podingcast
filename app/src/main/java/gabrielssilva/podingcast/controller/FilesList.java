package gabrielssilva.podingcast.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.database.FilesDbContract;
import gabrielssilva.podingcast.database.FilesDbHelper;

public class FilesList {

    private FilesDbHelper dbHelper;

    public FilesList(Context context) {
        dbHelper = new FilesDbHelper(context);
    }

    public List<String> getAllFeeds() {
        Cursor queryResult = dbHelper.getAllFeeds();
        int columnIndex = queryResult.getColumnIndexOrThrow(FilesDbContract.FeedEntry.FEED_NAME);

        return cursorToList(queryResult, columnIndex);
    }

    public List<String> getFeedFiles(String feedName) {
        Cursor queryResult = dbHelper.getFeedFiles(feedName);
        int columnIndex = queryResult.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_NAME);

        return cursorToList(queryResult, columnIndex);
    }

    public String getFilePath(String fileName) {
        Cursor queryResult = dbHelper.getFilePath(fileName);
        int columnIndex = queryResult.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_PATH);

        queryResult.moveToFirst();
        return queryResult.getString(columnIndex);
    }

    private List<String> cursorToList(Cursor cursor, int columnIndex) {
        List<String> list = new ArrayList<String>();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            list.add(cursor.getString(columnIndex));
        }

        return list;
    }
}