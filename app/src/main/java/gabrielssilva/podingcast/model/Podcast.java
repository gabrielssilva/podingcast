package gabrielssilva.podingcast.model;

public class Podcast {

    private String podcastName;
    private String rssAddress;

    public Podcast() {

    }

    public Podcast(String podcastName, String rssAddress) {
        this.podcastName = podcastName;
        this.rssAddress = rssAddress;
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
}
