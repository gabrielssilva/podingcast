package gabrielssilva.podingcast.events;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.net.Uri;
import android.view.View;

import gabrielssilva.podingcast.app.interfaces.DownloadListener;

public class DownloadClick implements View.OnClickListener {

    private DownloadListener listener;

    public DownloadClick(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        DownloadManager downloadManager = this.listener.getDownloadManager();
        long downloadID;

        Request request = new Request(Uri.parse("http://www.vogella.de/img/lars/LarsVogelArticle7.png"));
        downloadID = downloadManager.enqueue(request);

        this.listener.setDownloadID(downloadID);
    }

}
