package gabrielssilva.podingcast.app;

import android.app.DownloadManager;
import android.view.View;

public interface DownloadListener extends EventListener {
    public DownloadManager getDownloadManager();
    public long getDownloadID();
    public void setDownloadID(long downloadID);
}