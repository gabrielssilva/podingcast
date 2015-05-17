package gabrielssilva.podingcast.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Episode implements Parcelable {

    public static final Creator<Episode> CREATOR = new MyCreator();
    public static final String LOCAL = "status_local";
    public static final String NOT_LOCAL = "status_not_local";
    public static final String DOWNLOADING = "status_downloading";

    private String episodeName;
    private String filePath;
    private String url;
    private int lastPlayedPosition;
    private String status;

    public Episode() {

    }

    public Episode(String episodeName, String filePath, String url, int lastPlayedPosition) {
        this.episodeName = episodeName;
        this.filePath = filePath;
        this.url = url;
        this.lastPlayedPosition = lastPlayedPosition;
        this.status = LOCAL;
    }

    public Episode(Parcel parcel) {
        this(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt());
    }


    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLastPlayedPosition() {
        return lastPlayedPosition;
    }

    public void setLastPlayedPosition(int lastPlayedPosition) {
        this.lastPlayedPosition = lastPlayedPosition;
    }

    public boolean isLocal() {
        return this.status.equals(LOCAL) || this.status.equals(DOWNLOADING);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() { return this.status; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.episodeName);
        parcel.writeString(this.filePath);
        parcel.writeString(this.url);
        parcel.writeInt(this.lastPlayedPosition);
    }


    private static class MyCreator implements Creator<Episode> {

        @Override
        public Episode createFromParcel(Parcel parcel) {
            return new Episode(parcel);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    }
}
