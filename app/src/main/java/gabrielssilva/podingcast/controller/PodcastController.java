package gabrielssilva.podingcast.controller;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.database.FilesDbHelper;
import gabrielssilva.podingcast.helper.FilesHelper;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.parser.JsonHandler;
import gabrielssilva.podingcast.web.DownloadFeedTask;

public class PodcastController implements CallbackListener {

    private CallbackListener externalCallbackListener;

    public PodcastController(CallbackListener externalCallbackListener) {
        this.externalCallbackListener = externalCallbackListener;
    }

    public void fetchPodcast(String feedAddress, int numOfEpisodes) {
        DownloadFeedTask downloadTask = new DownloadFeedTask(this);
        Params params = new Params(feedAddress, numOfEpisodes);
        downloadTask.execute(params);
    }

    public void removePodcast(Context context, Podcast podcast) {
        for (Episode episode : podcast.getEpisodes()) {
            FilesHelper.deleteFile(episode.getFilePath());
        }

        FilesDbHelper filesDbHelper = new FilesDbHelper(context);
        filesDbHelper.removePodcast(podcast.getPodcastName());
    }


    @Override
    public void onSuccess(Object result) {
        JSONObject jsonPodcast = (JSONObject) result;

        try {
            JsonHandler jsonHandler = new JsonHandler(jsonPodcast);
            Podcast podcast = jsonHandler.getPodcast();

            this.externalCallbackListener.onSuccess(podcast);
        } catch (JSONException e) {
            e.printStackTrace();
            onFailure(null);
        }
    }

    @Override
    public void onFailure(Object result) {
        this.externalCallbackListener.onFailure(result);
    }


    public class Params {
        public String url;
        public int maxItems;

        public Params(String url, int maxItems) {
            this.url = url;
            this.maxItems = maxItems;
        }
    }
}
