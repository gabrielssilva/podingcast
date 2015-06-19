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

        String title = getTitle(jsonChannel);
        String feedAddress = this.jsonPodcast.getString("podingcast:link");
        String imageAddress = jsonChannel.getString("itunes:image");
        Podcast podcast = new Podcast(title, feedAddress, imageAddress);

        if (!jsonChannel.isNull("item")) {
            List<Episode> episodes = getEpisodes(jsonChannel.getJSONArray("item"));
            podcast.setEpisodes(episodes);
        }

        return podcast;
    }

    private String getTitle(JSONObject jsonChannel) throws JSONException {
        String name;

        if (jsonChannel.isNull("title")) {
            name = jsonChannel.getString("itunes:author");
        } else {
            name = jsonChannel.getString("title");
        }

        return name;
    }

    private List<Episode> getEpisodes(JSONArray jsonEpisodes) throws JSONException {
        List<Episode> episodes = new ArrayList<>();

        for (int i=0; i<jsonEpisodes.length(); i++) {
            JSONObject jsonEpisode = jsonEpisodes.getJSONObject(i);
            JSONObject jsonEnclosure = this.getEnclosure(jsonEpisode);

            Episode episode = new Episode();
            episode.setEpisodeName(jsonEpisode.getString("title"));
            episode.setUrl(jsonEnclosure.getString("url"));
            episode.setDescription(jsonEpisode.getString("description"));
            episode.setContent(jsonEpisode.getString("content:encoded"));
            episode.setDuration(jsonEpisode.getString("itunes:duration"));

            episodes.add(episode);
        }

        return episodes;
    }

    private JSONObject getEnclosure(JSONObject jsonEpisode) throws JSONException {
        JSONObject jsonEnclosure;

        try {
            jsonEnclosure = jsonEpisode.getJSONObject("enclosure");
        } catch (JSONException e) {
            jsonEnclosure = jsonEpisode.getJSONArray("enclosure").getJSONObject(0);
        }

        return jsonEnclosure;
    }
}