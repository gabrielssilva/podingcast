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


    public void addPodcast(String feedAddress) {
        DownloadFeedTask downloadTask = new DownloadFeedTask(this);
        Params params = new Params(feedAddress, 10);
        downloadTask.execute(params);
    }

    public void sendPodcast(String podcastName, String rssAddress) {
        Podcast podcast = new Podcast(podcastName, rssAddress);

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
            this.externalCallbackListener.onSuccess(jsonHandler.getTitle());
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
        public int numOfEpisodes;

        public Params(String url, int numOfEpisodes) {
            this.url = url;
            this.numOfEpisodes = numOfEpisodes;
        }
    }
}
