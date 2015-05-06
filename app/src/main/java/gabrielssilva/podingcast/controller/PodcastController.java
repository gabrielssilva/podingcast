package gabrielssilva.podingcast.controller;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.parser.JsonHandler;
import gabrielssilva.podingcast.web.DownloadFeedTask;
import gabrielssilva.podingcast.web.SendPodcastTask;

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

    public void sendPodcast(String podcastName, String rssAddress) {
        Podcast podcast = new Podcast(podcastName, rssAddress, null);

        try {
            JSONObject jsonObject = podcastToJson(podcast);
            StringEntity stringJsonObject = new StringEntity(jsonObject.toString());
            new SendPodcastTask().execute(stringJsonObject);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private JSONObject podcastToJson(Podcast podcast) throws JSONException {
        JSONObject jsonPodcast = new JSONObject();
        String podcastName = podcast.getPodcastName();
        String rssAddress = podcast.getRssAddress();

        jsonPodcast.put("podcast_name", podcastName);
        jsonPodcast.put("rss_address", rssAddress);

        return jsonPodcast;
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
            onFailure();
        }
    }

    @Override
    public void onFailure() {
        this.externalCallbackListener.onFailure();
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
