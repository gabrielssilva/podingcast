package gabrielssilva.podingcast.controller;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.helper.FilesHelper;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.model.Podcast;

public class EpisodesController  {

    private Activity activity;

    public EpisodesController(Activity activity) {
        this.activity = activity;
    }


    public long downloadEpisode(Podcast podcast, Episode episode) {
        DownloadManager downloadManager = (DownloadManager)
                this.activity.getSystemService(Context.DOWNLOAD_SERVICE);

        Request request = new Request(Uri.parse(episode.getUrl()));
        request.setDescription(podcast.getPodcastName());
        request.setDestinationInExternalPublicDir(FilesHelper.PODINGCAST_FOLDER,
                episode.getEpisodeName()+".mp3");

        return downloadManager.enqueue(request);
    }

    public List<Episode> compareEpisodes(List<Episode> localEpisodes, List<Episode> feedEpisodes) {
        List<Episode> episodes = new ArrayList<>(localEpisodes);

        for(int i=0; i<feedEpisodes.size(); i++) {
            Episode feedEpisode = feedEpisodes.get(i);

            if (!isOnList(feedEpisode, localEpisodes)) {
                feedEpisode.setStatus(Episode.NOT_LOCAL);
                episodes.add(feedEpisode);
            }
        }

        return episodes;
    }


    private boolean isOnList(Episode episode, List<Episode> localEpisodes) {
        boolean result = false;

        for(int i=0; i<localEpisodes.size(); i++) {
            Episode localEpisode = localEpisodes.get(i);
            result |= episode.getUrl().equalsIgnoreCase(localEpisode.getUrl());
        }

        return result;
    }
}
