package gabrielssilva.podingcast.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.model.Episode;
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
        Podcast podcast = new Podcast(title, feedAddress, imageAddress);

        if (!jsonChannel.isNull("item")) {
            List<Episode> episodes = getEpisodes(jsonChannel.getJSONArray("item"));
            podcast.setEpisodes(episodes);
        }

        return podcast;
    }

    private List<Episode> getEpisodes(JSONArray jsonEpisodes) throws JSONException {
        List<Episode> episodes = new ArrayList<>();

        for (int i=0; i<jsonEpisodes.length(); i++) {
            JSONObject jsonEpisode = jsonEpisodes.getJSONObject(i);
            Episode episode = new Episode();
            episode.setEpisodeName(jsonEpisode.getString("title"));

            episodes.add(episode);
        }

        return episodes;
    }
}
