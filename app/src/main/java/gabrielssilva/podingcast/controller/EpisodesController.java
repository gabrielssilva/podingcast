package gabrielssilva.podingcast.controller;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.helper.FilesHelper;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.service.DownloadNotifier;

public class EpisodesController implements CallbackListener {

    private CallbackListener callbackListener;
    private Activity activity;

    public EpisodesController(CallbackListener callbackListener, Activity activity) {
        this.callbackListener = callbackListener;
        this.activity = activity;
    }


    public void downloadEpisode(Episode episode) {
        DownloadManager downloadManager = (DownloadManager)
                this.activity.getSystemService(Context.DOWNLOAD_SERVICE);

        Request request = new Request(Uri.parse(episode.getUrl()));
        request.setDescription(episode.getEpisodeName());
        request.setDestinationInExternalPublicDir(FilesHelper.PODINGCAST_FOLDER,
                episode.getEpisodeName()+".mp3");

        long downloadId = downloadManager.enqueue(request);

        DownloadNotifier downloadNotifier = new DownloadNotifier(this, downloadManager, downloadId);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.activity.registerReceiver(downloadNotifier, intentFilter);
    }

    public List<Episode> compareEpisodes(List<Episode> localEpisodes, List<Episode> feedEpisodes) {
        List<Episode> episodes = new ArrayList<>(localEpisodes);

        for(int i=0; i<feedEpisodes.size(); i++) {
            Episode feedEpisode = feedEpisodes.get(i);

            if (!isOnList(feedEpisode, localEpisodes)) {
                feedEpisode.setLocal(false);
                episodes.add(feedEpisode);
            }
        }

        return episodes;
    }


    private boolean isOnList(Episode episode, List<Episode> localEpisodes) {
        boolean result = false;

        for(int i=0; i<localEpisodes.size(); i++) {
            Episode localEpisode = localEpisodes.get(i);
            result = episode.getUrl().equalsIgnoreCase(localEpisode.getUrl());
        }

        return result;
    }


    @Override
    public void onSuccess(Object result) {
        this.callbackListener.onSuccess(result);
    }

    @Override
    public void onFailure() {
        this.callbackListener.onFailure();
    }
}
