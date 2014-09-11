package gabrielssilva.podingcast.events;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.net.Uri;
import android.view.View;

import gabrielssilva.podingcast.app.DownloadListener;

public class DownloadClick implements View.OnClickListener {

    private DownloadManager downloadManager;
    private DownloadListener listener;

    public DownloadClick(DownloadListener listener) {
        this.downloadManager = listener.getDownloadManager();
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        long downloadID;

        Request request = new Request(Uri.parse("http://www.vogella.de/img/lars/LarsVogelArticle7.png"));
        downloadID = this.listener.getDownloadManager().enqueue(request);

        this.listener.setDownloadID(downloadID);
    }

}
