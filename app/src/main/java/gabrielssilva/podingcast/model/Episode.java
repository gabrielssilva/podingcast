package gabrielssilva.podingcast.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Episode {

    public static final String LOCAL = "status_local";
    public static final String NOT_LOCAL = "status_not_local";
    public static final String DOWNLOADING = "status_downloading";

    private String episodeName;
    private String filePath;
    private String url;
    private String duration;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getLastPlayedPosition() {
        return lastPlayedPosition;
    }

    public void setLastPlayedPosition(int lastPlayedPosition) {
        this.lastPlayedPosition = lastPlayedPosition;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() { return this.status; }
}
