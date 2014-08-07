package gabrielssilva.podingcast.app;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gabrielssilva.podingcast.events.OnDownloadClick;
import gabrielssilva.podingcast.web.DownloadNotifier;

public class FeedFragment extends Fragment implements DownloadListener {

    private long downloadID;
    private DownloadManager downloadManager;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        this.view = rootView;

        DownloadNotifier receiver = new DownloadNotifier(this);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(receiver, intentFilter);

        this.downloadManager = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);

        OnDownloadClick downloadEvent = new OnDownloadClick(this);
        rootView.findViewById(R.id.download_button).setOnClickListener(downloadEvent);

        return rootView;
    }

    @Override
    public View getRootView() {
        return this.view;
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
