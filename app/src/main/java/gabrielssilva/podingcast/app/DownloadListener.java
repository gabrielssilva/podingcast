package gabrielssilva.podingcast.app;

import android.app.DownloadManager;
import android.view.View;

public interface DownloadListener {
    public View getRootView();
    public DownloadManager getDownloadManager();
    public long getDownloadID();
    public void setDownloadID(long downloadID);
}
