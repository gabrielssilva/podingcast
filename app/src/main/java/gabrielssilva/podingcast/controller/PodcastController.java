package gabrielssilva.podingcast.controller;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.web.Send;

public class PodcastController {

    private static PodcastController instance;

    private PodcastController() {

    }

    public static PodcastController getInstance() {
        if (instance == null) {
            instance = new PodcastController();
        } else {
            // Do nothing.
        }

        return instance;
    }

    public void sendPodcast(String podcastName, String rssAddress) {
        Podcast podcast = new Podcast(podcastName, rssAddress);

        try {
            JSONObject jsonObject = podcastToJson(podcast);
            StringEntity stringJsonObject = new StringEntity(jsonObject.toString());
            new Send().execute(stringJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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
}
