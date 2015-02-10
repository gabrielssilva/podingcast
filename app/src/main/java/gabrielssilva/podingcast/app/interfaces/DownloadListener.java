package gabrielssilva.podingcast.app.interfaces;

import android.app.DownloadManager;

public interface DownloadListener {
    public DownloadManager getDownloadManager();
    public long getDownloadID();
    public void setDownloadID(long downloadID);
}
