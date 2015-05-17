package gabrielssilva.podingcast.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Podcast implements Parcelable {

    public static final Creator<Podcast> CREATOR = new MyCreator();

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

    public Podcast(Parcel parcel) {
        this.podcastName = parcel.readString();
        this.rssAddress = parcel.readString();
        this.numberOfEpisodes = parcel.readInt();

        this.episodes = new ArrayList<>();
        parcel.readList(episodes, Episode.class.getClassLoader());
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.podcastName);
        parcel.writeString(this.rssAddress);
        parcel.writeInt(this.numberOfEpisodes);
        parcel.writeList(this.episodes);
    }


    private static class MyCreator implements Creator<Podcast> {

        @Override
        public Podcast createFromParcel(Parcel parcel) {
            return new Podcast(parcel);
        }

        @Override
        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    }
}