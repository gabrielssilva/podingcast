package gabrielssilva.podingcast.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.database.FilesDbContract;
import gabrielssilva.podingcast.database.FilesDbHelper;

public class FilesController {

    private FilesDbHelper dbHelper;

    public FilesController(Context context) {
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

    public FileInfo getFileInfo(String fileName) {
        Cursor queryResult = dbHelper.getFileInfo(fileName);
        int pathColumnIndex = queryResult.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_PATH);
        int posColumnIndex = queryResult.getColumnIndexOrThrow(FilesDbContract.FileEntry.FILE_LAST_POS);

        queryResult.moveToFirst();
        String path = queryResult.getString(pathColumnIndex);
        int lastPosition = queryResult.getInt(posColumnIndex);

        return new FileInfo(fileName, path, lastPosition);
    }

    public void saveCurrentPosition(String fileName, int currentPosition) {
        this.dbHelper.updateLastPosition(fileName, currentPosition);
    }

    private List<String> cursorToList(Cursor cursor, int columnIndex) {
        List<String> list = new ArrayList<String>();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            list.add(cursor.getString(columnIndex));
        }

        return list;
    }

    public class FileInfo {
        public String name;
        public String path;
        public int lastPosition;

        public FileInfo(String name, String path, int lastPosition) {
            this.name = name;
            this.path = path;
            this.lastPosition = lastPosition;
        }
    }
}