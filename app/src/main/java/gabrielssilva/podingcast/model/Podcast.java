package gabrielssilva.podingcast.model;

import java.util.ArrayList;
import java.util.List;

public class Podcast {

    private String podcastName;
    private String rssAddress;
    private String imageAddress;
    private int numberOfEpisodes;
    private List<Episode> episodes;

    public Podcast() {

    }

    public Podcast(String podcastName, String rssAddress, String imageAddress) {
        this.podcastName = podcastName;
        this.rssAddress = rssAddress;
        this.imageAddress = imageAddress;

        this.episodes = new ArrayList<>();
    }

    public String getRssAddress() {
        return rssAddress;
    }

    public void setRssAddress(String rssAddress) {
        this.rssAddress = rssAddress;
    }

    public String getPodcastName() {
        return podcastName;
    }

    public void setPodcastName(String podcastName) {
        this.podcastName = podcastName;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        this.numberOfEpisodes = episodes.size();
    }


}