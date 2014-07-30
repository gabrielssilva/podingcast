package gabrielssilva.podingcast.controller;

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

    }
}
