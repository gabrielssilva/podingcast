package gabrielssilva.podingcast.parser;

import org.json.JSONException;
import org.json.JSONObject;

import gabrielssilva.podingcast.model.Podcast;

public class JsonHandler {

    private JSONObject jsonPodcast;

    public JsonHandler(JSONObject jsonPodcast) {
        this.jsonPodcast = jsonPodcast;
    }

    public Podcast getPodcast() throws JSONException {
        JSONObject jsonRSS = this.jsonPodcast.getJSONObject("rss");
        JSONObject jsonChannel = jsonRSS.getJSONObject("channel");

        String title = jsonChannel.getString("title");
        String feedAddress = jsonChannel.getJSONObject("atom:link").getString("href");
        String imageAddress = jsonChannel.getJSONObject("image").getString("url");

        return new Podcast(title, feedAddress, imageAddress);
    }
}
