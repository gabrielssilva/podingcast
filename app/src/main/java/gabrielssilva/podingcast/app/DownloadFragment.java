package gabrielssilva.podingcast.app;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gabrielssilva.podingcast.app.interfaces.DownloadListener;
import gabrielssilva.podingcast.events.DownloadClick;
import gabrielssilva.podingcast.web.DownloadNotifier;

public class DownloadFragment extends Fragment implements DownloadListener {

    private DownloadNotifier receiver;
    private long downloadID;
    private DownloadManager downloadManager;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_download, container, false);
        this.view = rootView;

        this.receiver = new DownloadNotifier(this);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(receiver, intentFilter);

        this.downloadManager = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);

        DownloadClick downloadEvent = new DownloadClick(this);
        rootView.findViewById(R.id.download_button).setOnClickListener(downloadEvent);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getActivity().unregisterReceiver(this.receiver);
    }

    @Override
    public DownloadManager getDownloadManager() {
        return this.downloadManager;
    }

    @Override
    public long getDownloadID() {
        return this.downloadID;
    }

    @Override
    public void setDownloadID(long downloadID) {
        this.downloadID = downloadID;
    }
}
