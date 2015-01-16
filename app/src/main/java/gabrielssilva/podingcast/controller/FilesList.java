package gabrielssilva.podingcast.controller;

import android.content.Context;
import android.database.Cursor;

import java.io.File;
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
        List<String> allFeeds = new ArrayList<String>();
        int columnIndex = queryResult.getColumnIndexOrThrow(FilesDbContract.FeedEntry.FEED_NAME);

        for(queryResult.moveToFirst(); !queryResult.isAfterLast(); queryResult.moveToNext()) {
            allFeeds.add(queryResult.getString(columnIndex));
        }

        return allFeeds;
    }

    public List<File> getFeedFiles() {
        return null;
    }
}