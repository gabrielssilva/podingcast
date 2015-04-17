package gabrielssilva.podingcast.parser;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHandler {

    private JSONObject jsonPodcast;

    public JsonHandler(JSONObject jsonPodcast) {
        this.jsonPodcast = jsonPodcast;
    }

    public String getTitle() throws JSONException {
        JSONObject jsonRSS = this.jsonPodcast.getJSONObject("rss");
        JSONObject jsonChannel = jsonRSS.getJSONObject("channel");

        return jsonChannel.getString("title");
    }
}
