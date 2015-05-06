package gabrielssilva.podingcast.controller;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.model.Episode;

public class EpisodesController {

    public EpisodesController() {

    }

    public List<Episode> compareEpisodes(List<Episode> localEpisodes, List<Episode> feedEpisodes) {
        List<Episode> episodes = new ArrayList<>(localEpisodes);

        for(int i=0; i<feedEpisodes.size(); i++) {
            Episode feedEpisode = feedEpisodes.get(i);

            if (!isOnList(feedEpisode, localEpisodes)) {
                episodes.add(feedEpisode);
            }
        }

        return episodes;
    }

    private boolean isOnList(Episode episode, List<Episode> localEpisodes) {
        boolean result = false;

        for(int i=0; i<localEpisodes.size(); i++) {
            Episode localEpisode = localEpisodes.get(i);
            result = episode.getEpisodeName().equals(localEpisode.getEpisodeName());
        }

        return result;
    }
}
